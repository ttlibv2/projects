package vn.conyeu.book.extapi;

import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import vn.conyeu.common.exception.NoContent;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Jsons;
import vn.conyeu.restclient.ClientBuilder;
import vn.conyeu.restclient.RClient;

import java.util.*;
import java.util.stream.Collectors;

public abstract class MainApi {

    protected ClientBuilder builder() {
        return RClient.builder().baseUrl(baseUrl());
    }

    protected Document getHtml(String url) {
        String html = builder().build().get().uri(url).retrieve().bodyToMono(String.class).blockOptional().orElseThrow(NoContent::new);
        return Jsoup.parse(html);
    }

    public abstract String baseUrl();

    public abstract Optional<BookUrl> searchBook(SearchObj object);

    /**
     * Read book from url
     * @param path the url book
     * */
    public abstract Optional<BookDto> readBook(String path);

    /**
     * Read chapter from url
     * @param path the url chapter
     * */
    public abstract Optional<ChapterDto> readChapter(String path);

    protected Set<String> listText(Element el, String css, boolean error) {
        return list(el, css, error).stream().map(e -> e.text().trim()).collect(Collectors.toSet());
    }
    protected String text(Element el, String css, boolean error) {
        Element el2 = first(el, css, error);
        return el2 == null ? null : el2.text().trim();
    }

    protected String src(Element el, String css, boolean error) {
        return attr(el, css, "src", error);
    }

    protected String href(Element el, String css, boolean error) {
        return attr(el, css, "href", error);
    }

    protected String attr(Element el, String css, String attr, boolean error) {
        Element el2 = first(el, css, error);
        return el2 == null ? null : el2.attr(attr);
    }

    protected Element html(Element el, String css, String attr, boolean error) {
       return first(el, css, error);
    }

    protected Element first(Element el, String css, boolean error) {
        Element el2 = el.selectFirst(css);
        if(el2 == null && error) throw new RuntimeException("The css %s path not find".formatted(css));
        else return el2;
    }

    protected Elements list(Element el, String css, boolean error) {
        Elements els = el.select(css);
        if(els.isEmpty() && error) throw new RuntimeException("The css %s path not find".formatted(css));
        return els;
    }

    @Getter @Setter
    public static class BookDto {
        private String url;
        private String id;
        private String title;
        private String alias;
        private String summary;
        private String poster;
        private String author;
        private Long view;
        private Double rating;
        private Long totalRate;
        private String source;
        private Set<String> genres;
        private Boolean full;
        private Long totalChapter;
        private Integer totalPage;
        private Integer currentPage;
        private List<ChapterUrl> chapterNew;
        private List<ChapterUrl> chapters;

        /**
         * Returns the chapters
         */
        public List<ChapterUrl> getChapters() {
            if(chapters == null)chapters = new ArrayList<>();
            return chapters;
        }

        @Override
        public String toString() {
            return Jsons.serializeToString(this, true);
        }
    }

    @Getter @Setter
    public static class ChapterDto {
        private String dtoId;
        private String url;
        private String alias;
        private String title;
        private String html;
        private Boolean vip;
        private Integer coin;
    }

    @Getter @Setter
    public static class SearchObj {
        private String author; // user1,...
        private String category; // ngon-tinh, tien-hiep
        private String keyword; // key...
        private String list; // truyen-full, truyen-hot,...
        private Boolean isNew; // truyen-moi
        private Boolean isHot; // truyen-hot
        private Boolean isFull; // truyen-full
        private String urlAlias; // custom url
        private ObjectMap custom;

        public ObjectMap custom() {
            if(custom == null)custom = new ObjectMap();
            return custom;
        }

    }

    @Getter @Setter
    public static class BookUrl {
        private String title;
        private String url;
        private String author;
        private String poster;
        private Boolean isNew;
        private Boolean isHot;
        private Boolean isFull;
        private String currentChapter;
    }

    @Getter @Setter
    public static class ChapterUrl {
        private String title;
        private String url;
        private Boolean vip;
        private Integer position;

        public ChapterUrl(String title, String url, Integer pos) {
            this.title = title;
            this.url = url;
            this.position = pos;
        }

        public static ChapterUrl create(String title, String url, Integer pos) {
            return new ChapterUrl(title, url, pos);
        }


    }

    @Getter @Setter
    public static class Comment {
        private String user;
        private String content;
        private String time;
        private String book;
        private String chapter;
        private List<Comment> children;
    }
}