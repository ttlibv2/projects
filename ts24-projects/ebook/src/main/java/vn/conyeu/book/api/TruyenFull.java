package vn.conyeu.book.api;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import vn.conyeu.book.domain.BookDto;
import vn.conyeu.book.domain.ChapterDto;
import vn.conyeu.book.service.BookDtoService;
import vn.conyeu.book.service.ChapterDtoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class TruyenFull extends BookClient{
    static final Pattern chapterRegex =  Pattern.compile("\\S+/chuong-(\\d+)/?", Pattern.MULTILINE);;
    static final String urlRoot = "https://truyenfull.io";
    final BookDtoService bookService;
    final ChapterDtoService chapterService;

    public TruyenFull(BookDtoService bookService, ChapterDtoService chapterService) {
        this.bookService = bookService;
        this.chapterService = chapterService;
    }

    @Override
    public String getBaseRoot() {
        return urlRoot;
    }



    public Optional<BookDto> findBook(String url) {
        BookDto book = new BookDto();

        Document html = getHtml(url);

        Element el = first(html, ".col-truyen-main", true);
        Element colDesc = first(el, ".col-info-desc", true);
        book.setTitle(get_text(colDesc, "h3.title"));
        book.setSummary(get_html(colDesc, "div.desc-text"));
        book.setTotalRate(get_attr(colDesc, ".rate-holder", "data-score"));
        book.setTotalReview(get_text(colDesc, "span[itemprop=ratingCount]"));

        book.setExternalId(get_attr(el, "#truyen-id", "value"));
        book.setService("truyenfull.io");

        Element infoHolder = first(colDesc, ".info-holder", true);
        book.setAuthor(get_attr(infoHolder, "[itemprop=author]", "title"));
        book.setCategories(get_text(infoHolder, "[itemprop=genre]", ","));
        book.setSource(get_text(infoHolder, ".source"));
        book.setState(get_text(infoHolder, ".text-success"));
        book.setPoster(get_attr(infoHolder, "img[itemprop=image]", "src"));
        book.setTotalPage(toLong(get_attr(el, "#total-page", "value")));
        book.setSeoUrl(get_attr(el, "#truyen-ascii", "value"));
        book.setPostTime(get_attr(el, "#truyen-time", "value"));

        // chapters
        List<ChapterDto> chapters = extractChapterUrl(html);
        chapters.forEach(book::addChapter);

        return Optional.of(book);
    }

    public List<ChapterDto> getChapterLinks(String bookUrl, long page) {
        Document html = getHtml(bookUrl + "/trang-"+page+"/");
        return extractChapterUrl(html);
    }

    public Optional<ChapterDto> findChapter(String chapterUrl) {
        ChapterDto chapter = new ChapterDto();

        try {
            Document html = getHtml(chapterUrl);
            Element el = first(html, "#chapter-big-container", true);
            chapter.setTitle(first(el, ".chapter-title", true).text());

            Element htmlEl = first(el, "#chapter-c", true);
            chapter.setContentHtml(removeNode(htmlEl));

        }
        catch (WebClientResponseException exp){
            chapter.setLogError(exp.getMessage());
        }

        return Optional.of(chapter);
    }

    public Long getTotalPageBook(Document html) {
        Element el = first(html, ".col-truyen-main .pagination", false);
        if(el == null) return 1L;

        String[] last = get_attr(el, "a:contains(Cuối)", "title").split(" ");
        return toLong(last[last.length-1]);
    }

    private List<ChapterDto> extractChapterUrl(Document html) {
        List<ChapterDto> chapters = new ArrayList<>();

        Elements lsChapter = select(html, ".col-truyen-main #list-chapter ul.list-chapter a", true);
        for(Element elChapter:lsChapter) {
            String chapterUrl = elChapter.attr("href");
            ChapterDto dto = new ChapterDto();
            dto.setFullUrl(chapterUrl);
            dto.setTitle(elChapter.text().trim());
            dto.setSeoUrl(extractSeo(chapterUrl, "c"));
            chapters.add(dto);
        }

        return chapters;
    }

    public String getChapterUrlNext(String chapterUrl) {
        Matcher matcher = chapterRegex.matcher(chapterUrl);
        if(matcher.find()) {
            String str = matcher.replaceAll("$1");
            return "chuong-"+(Long.parseLong(str) + 1);
        }
        else throw new RuntimeException("chapter url invalid");
    }

    private String extractSeo(String url, String type) {
        if("c".equals(type)) {
            String num = chapterRegex.matcher(url).replaceAll("$1");
            return "chuong-"+num;
        }
        else if("b".equals(type)) {
            return url;
        }
        else throw new RuntimeException("invalid url");
    }

    private String removeNode(Element element) {
        element.select("[class*=ads-]").remove();
        String html = element.html();
        html = html.replaceAll("\n", "");
        html = html.replaceAll("\\s*<br>\\s*<br>", "</p><p>");
        return "<p>%s</p>".formatted(html);
    }
}