package vn.conyeu.api;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import vn.conyeu.common.exception.NotFound;
import vn.conyeu.restclient.RestClient;

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


}