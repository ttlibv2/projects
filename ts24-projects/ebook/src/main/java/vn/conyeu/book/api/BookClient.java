package vn.conyeu.book.api;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import vn.conyeu.common.exception.NotFound;
import vn.conyeu.restclient.RClient;

import javax.net.ssl.SSLException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BookClient {

    public abstract String getBaseRoot();

    protected RClient client()  {
        try {
            SslContext sslContext = SslContextBuilder.forClient()
                    .protocols("SSLv3","TLSv1","TLSv1.1","TLSv1.2")
                    .ciphers( List.of("TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384"))
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            ConnectionProvider provider = ConnectionProvider.builder("fixed")
                    .maxConnections(500)
                    .maxIdleTime(Duration.ofSeconds(20))
                    .maxLifeTime(Duration.ofSeconds(60))
                    .pendingAcquireTimeout(Duration.ofSeconds(60))
                    .evictInBackground(Duration.ofSeconds(120)).build();

            HttpClient httpClient = HttpClient.create(provider)
                    .wiretap(true).responseTimeout(Duration.ofMinutes(2))
                    .followRedirect(true)//.option(ChannelOption.SO_TIMEOUT, 60_000*2)
                    .secure(t ->
                            t.sslContext(sslContext)
                                    .handshakeTimeout(Duration.ofMillis(20000))

                    )

                  //  .wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
                  // .followRedirect(true)
                    //.doOnConnected(connection -> connection.addHandlerLast(new ReadTimeoutHandler(60*5)))
                  //  .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000000)
            ;

            httpClient.warmup().block();

            return  RClient.builder()
                    .baseUrl(getBaseRoot())
                    .defaultHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Mobile Safari/537.36")
                    .clientConnector( new ReactorClientHttpConnector(httpClient))
                    .defaultContentType(MediaType.TEXT_HTML)
                    .build();

        }
        catch (SSLException exp) {
            throw new RuntimeException("SslContext", exp);
        }
    }

    protected Document getHtml(String htmlUrl) {
        return client().get().uri(htmlUrl)
                .retrieve().bodyToMono(String.class)
                .blockOptional().map(Jsoup::parse)
                .orElseThrow();
    }

    protected Element first(Element parent, String css) {
        return first(parent, css, false);
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