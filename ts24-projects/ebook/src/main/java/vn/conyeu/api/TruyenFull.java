package vn.conyeu.api;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import vn.conyeu.domain.BookDto;
import vn.conyeu.domain.Chapter;
import vn.conyeu.domain.ChapterDto;
import vn.conyeu.service.BookDtoService;
import vn.conyeu.service.ChapterDtoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TruyenFull extends BookClient{
    static final String urlRoot = "https://truyenfull.io";
    final BookDtoService bookService;
    final ChapterDtoService chapterService;

    public TruyenFull(BookDtoService bookService, ChapterDtoService chapterService) {
        this.bookService = bookService;
        this.chapterService = chapterService;
    }

    @Override
    String getBaseRoot() {
        return urlRoot;
    }

    public Optional<BookDto> findBook(String url) {
        url = "https://truyenfull.io/vo-nho-cuoi-cung-em-da-lon/";

        Document html = getHtml(url);
        Element el = first(html, ".col-truyen-main", true);


        BookDto book = new BookDto();
        List<ChapterDto> chapters = book.getChapters();

        // fisrt page
        extractLsChapter(book, html, chapters);
        bookService.save(book);

        Integer totalPage = getTotalPageBook(html);
        for(int index=1;index<totalPage;index++) {
            String url2 = "https://truyenfull.io/vo-nho-cuoi-cung-em-da-lon/trang-"+(index+1)+"/";

            chapters = new ArrayList<>();
            extractLsChapter(book, getHtml(url2), chapters);
            chapterService.saveAll(chapters);

        }

        bookService.save(book);


        return Optional.of(book);



    }

    public Optional<ChapterDto> findChapter(ChapterDto chapter) {
        Document html = getHtml(chapter.getFullUrl());
        Element el = first(html, "#chapter-big-container", true);

        chapter.setTitle(first(el, ".chapter-title", true).text());
        chapter.setContentHtml(first(el, "#chapter-c", true).outerHtml());
        return Optional.of(chapter);
    }

    public Integer getTotalPageBook(Document html) {
        Element el = first(html, ".col-truyen-main .pagination", false);
        if(el == null) return 1;

        Elements els = select(el, "a:contains(Cuối)", false);
        //;;return els.isEmpty() ? 1 : els.get(0).
        return 56;

    }

    public void extractLsChapter(BookDto book, Document html, List<ChapterDto> chapters) {
        Elements lsChapter = select(html, ".col-truyen-main ul.list-chapter a", true);
        for(Element elChapter:lsChapter) {
            ChapterDto dto = new ChapterDto();
            dto.setBook(book);
            dto.setTitle(elChapter.text().trim());
            dto.setFullUrl(elChapter.attr("href"));

            findChapter(dto);

            chapters.add(dto);
        }
    }
}