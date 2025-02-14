package vn.conyeu.book.extapi;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import vn.conyeu.commons.utils.Jsons;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class TruyenFull extends MainApi {
    final static String ROOT_URL = "https://truyenfull.bio";

    @Override
    public String baseUrl() {
        return ROOT_URL;
    }

    @Override
    public Optional<BookUrl> searchBook(SearchObj object) {
        return Optional.empty();
    }

    @Override
    public Optional<BookDto> readBook(String path) {
        Document doc = getHtml(path);

        Element elMain = doc.selectFirst(".col-truyen-main");
        if(elMain == null) return Optional.empty();

        BookDto dto = new BookDto();
        dto.setTitle(text(elMain, "h3.title", true));

        Element holder = first(elMain, ".info-holder", true);
        dto.setPoster(src(holder, "[itemprop=image]", true));
        dto.setAuthor(text(holder, "[itemprop=author]", true));
        dto.setGenres(listText(holder, "[itemprop=genre]", true));
        dto.setSource(text(holder, ".source", true));
        dto.setFull(text(holder, ".text-success", true).equals("Full"));
        dto.setTotalPage(getLastPage(elMain));
        dto.getChapters().addAll(getChapterUrls(elMain));
        dto.setCurrentPage(1);

        String rate = attr(elMain, ".rate .rate-holder", "data-score", true);
        dto.setRating(Double.parseDouble(rate));

        String avgRate = text(elMain, "[itemprop=ratingCount]", true);
        dto.setTotalRate(Long.parseLong(avgRate));

        Element html = first(elMain, "[itemprop=description]", true);
        dto.setSummary(editBookDesc(html));

















        return Optional.of(dto);
    }


    @Override
    public Optional<ChapterDto> readChapter(String path) {
        return Optional.empty();
    }

    public List<ChapterUrl> getChapterUrls(String link) {
        Element el = first(getHtml(link), ".col-truyen-main", true);
        return getChapterUrls(el);
    }

    public List<ChapterUrl> getChapterUrls(Element elMain) {
        Elements els = list(elMain, "ul.list-chapter li a", true);
        List<ChapterUrl> urls = new ArrayList<>();
        for(int pos=0;pos<els.size();pos++) {
            Element element = els.get(pos);
            String title = element.text();
            String url = element.attr("href");
            urls.add( ChapterUrl.create(title, url, pos+1));
        }

        return urls;
    }

    protected Integer getLastPage(Element el) {
        Element elP = first(el, ".pagination", false);
        if(elP != null) {
            Elements els = list(elP, "li a", false);
            String cp = els.get(els.size() - 2).attr("href");
            return extractNumPageFromLink(cp);
        }
        return 1;
    }


    protected Integer extractNumPageFromLink(String link) {
        int index = link.lastIndexOf("/trang-");
        if(index == -1) throw new RuntimeException("The link not legend '/trang-'");
        int last = link.indexOf("/", index+1);
        return Integer.parseInt(link.substring(index + 7, last));
    }


    protected String editBookDesc(Element element) {
       for(int pos=0;pos<element.childNodeSize();pos++) {
           Node node = element.childNode(pos);
           if(node instanceof TextNode tn) {
               if( tn.text().contains("Tác giả")) tn.remove();
               else if(tn.text().contains("Thể loại"))tn.remove();
               else if(tn.text().contains("Giới thiệu")) tn.remove();
           }
       }

        String content = element.html().replaceAll("<br>", "").trim()
                .replaceAll("\\n", "</p><p>").replaceAll("<p></p>", "")
                .replaceAll("<p><br></p>", "").trim();


        if(content.contains("</p><p>")) content = "<p>"+content+"</p>";



        return content;
    }

}