package vn.conyeu.restclient;

import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import java.net.URI;
import java.util.Map;
import java.util.function.Function;

public class ClientURI {
    private final Object uri;
    private final Object uriVariables;
    private final Function<UriBuilder, URI>uriFunction;

    private ClientURI(Object uri, Object uriVariables, Function<UriBuilder, URI> uriFunction) {
        this.uri = uri;
        this.uriVariables = uriVariables;
        this.uriFunction = uriFunction;
    }

    void applyUriSpec(RestClient.RequestBodyUriSpec uriSpec) {

        //uriFunction
        if(uriFunction != null) {
            if(uri != null) uriSpec.uri((String) uri, uriFunction);
            else uriSpec.uri(uriFunction);
        }

        //URI
        else if(uri instanceof URI ur) {
            uriSpec.uri(ur);
        }

        //String
        else if(uri instanceof String us) {
            if(uriVariables instanceof Map map) uriSpec.uri(us, map);
            else if(uriVariables == null) uriSpec.uri(us);
            else if(uriVariables instanceof Object[] objects) uriSpec.uri(us, objects);
        }

    }


    /**
     * Specify the URI using an absolute, fully constructed {@link URI}.
     */
    public static ClientURI uri(URI uri) {
        return new ClientURI(uri, null, null);
    }

    /**
     * Specify the URI for the request using a URI template and URI variables.
     * If a {@link UriBuilderFactory} was configured for the client (e.g.
     * with a base URI) it will be used to expand the URI template.
     */
    public static ClientURI uri(String uri, Object... uriVariables){
        return new ClientURI(uri, uriVariables, null);
    }

    /**
     * Specify the URI for the request using a URI template and URI variables.
     * If a {@link UriBuilderFactory} was configured for the client (e.g.
     * with a base URI) it will be used to expand the URI template.
     */
    public static ClientURI  uri(String uri, Map<String, ?> uriVariables){
        return new ClientURI(uri, uriVariables, null);
    }

    /**
     * Specify the URI starting with a URI template and finishing off with a
     * {@link UriBuilder} created from the template.
     * @since 5.2
     */
    public static ClientURI uri(String uri, Function<UriBuilder, URI> uriFunction){
        return new ClientURI(uri, null, uriFunction);
    }

    /**
     * Specify the URI by through a {@link UriBuilder}.
     * @see #uri(String, Function)
     */
    public static ClientURI uri(Function<UriBuilder, URI> uriFunction){
        return new ClientURI(null, null, uriFunction);
    }

}