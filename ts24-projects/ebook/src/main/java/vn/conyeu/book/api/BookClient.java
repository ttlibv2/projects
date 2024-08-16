package vn.conyeu.book.api;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import vn.conyeu.common.exception.NotFound;
import vn.conyeu.restclient.RestClient;

import java.util.stream.Collectors;

public abstract class BookClient {

    abstract String getBaseRoot();

    protected RestClient client() {
        return  RestClient.builder().baseUrl(getBaseRoot())
                .build();
    }

    protected Document getHtml(String htmlUrl) {
        return client().get().uri(htmlUrl)
                .retrieve().bodyToMono(String.class)
                .blockOptional().map(Jsoup::parse)
                .orElseThrow();
    }

    protected Element first(Element parent, String css, boolean error) {
        Element el = parent.selectFirst(css);
        if(el == null && error) throw new NotFound().message("The css query '%s' not found", css);
        else return el;
    }

    protected Elements select(Element parent, String css, boolean error) {
        Elements el = parent.select(css);
        if(el == null && error) throw new NotFound().message("The css query '%s' not found", css);
        else return el;
    }

    protected String get_text(Element el, String css) {
        return el.select(css).text();
    }

    protected String get_text(Element el, String css, String sep) {
        return el.select(css).stream().map(Element::text).collect(Collectors.joining(sep));
    }

    protected String get_html(Element el, String css) {
        return el.select(css).html();
    }

    protected String get_attr(Element el, String css, String attr) {
        return first(el, css, true).attr(attr);
    }

    protected Long toLong(String str) {
        return Long.parseLong(str);
    }
}