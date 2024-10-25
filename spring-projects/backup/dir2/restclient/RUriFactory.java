package vn.conyeu.restclient;

import org.springframework.lang.Nullable;
import org.springframework.util.*;
import org.springframework.web.util.*;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Objects;

import java.net.URI;
import java.util.*;

public class RUriFactory extends DefaultUriBuilderFactory {
    private final UriComponentsBuilder baseUri;
    private final MultiValueMap<String, String> defaultQuery;

    public static RUriFactory fromUriString(String baseUrl) {
        return baseUrl == null ? new RUriFactory() : new RUriFactory(baseUrl);
    }

    /**
     * Default constructor without a base URI.
     * <p>The target address must be specified on each UriBuilder.
     */
    public RUriFactory() {
        this(UriComponentsBuilder.newInstance());
    }

    /**
     * Constructor with a base URI.
     * <p>The given URI template is parsed via
     * {@link UriComponentsBuilder#fromUriString} and then applied as a base URI
     * to every UriBuilder via {@link UriComponentsBuilder#uriComponents} unless
     * the UriBuilder itself was created with a URI template that already has a
     * target address.
     *
     * @param baseUriTemplate the URI template to use a base URL
     */
    public RUriFactory(String baseUriTemplate) {
        this(UriComponentsBuilder.fromUriString(baseUriTemplate));
    }

    /**
     * Variant of {@link #RUriFactory(String)} with a
     * {@code UriComponentsBuilder}.
     * @param baseUri the UriComponentsBuilder
     */
    public RUriFactory(UriComponentsBuilder baseUri) {
        super(Asserts.notNull(baseUri));
        this.defaultQuery = new LinkedMultiValueMap<>();
        this.baseUri = baseUri;
    }

    /**
     * Set the defaultQuery
     *
     * @param defaultQuery the value
     */
    public void setDefaultQuery(MultiValueMap<String, String> defaultQuery) {
        if(defaultQuery == null || defaultQuery.isEmpty()) this.defaultQuery.clear();
        else this.defaultQuery.putAll(defaultQuery);
    }

    /**
     * Initialize a builder with the given URI template.
     * @param uriTemplate the URI template to use
     * @return the builder instance
     */
    public UriBuilder uriString(String uriTemplate) {
        return new SimpleUriBuilder(uriTemplate);
    }

    /**
     * Create a URI builder with default settings.
     * @return the builder instance
     */
    public UriBuilder builder() {
        return new SimpleUriBuilder("");
    }

    protected final class SimpleUriBuilder implements UriBuilder {
        final UriComponentsBuilder uriComponentsBuilder;

        public SimpleUriBuilder(String uriTemplate) {
            this.uriComponentsBuilder = initUriComponentsBuilder(uriTemplate);
        }

        private UriComponentsBuilder initUriComponentsBuilder(String uriTemplate) {
            UriComponentsBuilder result;
            if(Objects.isBlank(uriTemplate)) {
                result = baseUri.cloneBuilder()
                        .replaceQueryParams(defaultQuery);
            }
            else {
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);
                UriComponents uri = builder.build();
                result = uri.getHost() != null ? builder
                        : baseUri.cloneBuilder()
                        .replaceQueryParams(defaultQuery).uriComponents(uri);
            }

            if (getEncodingMode().equals(EncodingMode.TEMPLATE_AND_VALUES)) {
                result.encode();
            }

            parsePathIfNecessary(result);
            return result;
        }

        private void parsePathIfNecessary(UriComponentsBuilder result) {
            if (shouldParsePath() && getEncodingMode().equals(EncodingMode.URI_COMPONENT)) {
                UriComponents uric = result.build();
                String path = uric.getPath();
                result.replacePath(null);
                for (String segment : uric.getPathSegments()) {
                    result.pathSegment(segment);
                }
                if (path != null && path.endsWith("/")) {
                    result.path("/");
                }
            }
        }

        /**{@inheritDoc}*/
        public SimpleUriBuilder scheme(@Nullable String scheme) {
            uriComponentsBuilder.scheme(scheme);
            return this;
        }

        /**{@inheritDoc}*/
        public SimpleUriBuilder userInfo(@Nullable String userInfo) {
            uriComponentsBuilder.userInfo(userInfo);
            return this;
        }

        /**{@inheritDoc}*/
        public SimpleUriBuilder host(@Nullable String host) {
            uriComponentsBuilder.host(host);
            return this;
        }

        /**{@inheritDoc}*/
        public SimpleUriBuilder port(int port) {
            uriComponentsBuilder.port(port);
            return this;
        }

        /**{@inheritDoc}*/
        public SimpleUriBuilder port(@Nullable String port) {
            uriComponentsBuilder.port(port);
            return this;
        }

        /**{@inheritDoc}*/
        public SimpleUriBuilder path(String path) {
            uriComponentsBuilder.path(path);
            return this;
        }

        /**{@inheritDoc}*/
        public SimpleUriBuilder replacePath(@Nullable String path) {
            uriComponentsBuilder.replacePath(path);
            return this;
        }

        /**{@inheritDoc}*/
        public SimpleUriBuilder pathSegment(String... pathSegments) {
            uriComponentsBuilder.pathSegment(pathSegments);
            return this;
        }

        /**{@inheritDoc}*/
        public SimpleUriBuilder query(String query) {
            uriComponentsBuilder.query(query);
            return this;
        }

        /**{@inheritDoc}*/
        public SimpleUriBuilder replaceQuery(@Nullable String query) {
            uriComponentsBuilder.replaceQuery(query);
            return this;
        }

        /**{@inheritDoc}*/
        public SimpleUriBuilder queryParam(String name, Object... values) {
            uriComponentsBuilder.queryParam(name, values);
            return this;
        }

        /**{@inheritDoc}*/
        public SimpleUriBuilder queryParam(String name, @Nullable Collection<?> values) {
            uriComponentsBuilder.queryParam(name, values);
            return this;
        }

        /**{@inheritDoc}*/
        public SimpleUriBuilder queryParamIfPresent(String name, Optional<?> value) {
            uriComponentsBuilder.queryParamIfPresent(name, value);
            return this;
        }

        /**{@inheritDoc}*/
        public SimpleUriBuilder queryParams(MultiValueMap<String, String> params) {
            uriComponentsBuilder.queryParams(params);
            return this;
        }

        /**{@inheritDoc}*/
        public SimpleUriBuilder replaceQueryParam(String name, Object... values) {
            uriComponentsBuilder.replaceQueryParam(name, values);
            return this;
        }

        /**{@inheritDoc}*/
        public SimpleUriBuilder replaceQueryParam(String name, @Nullable Collection<?> values) {
            uriComponentsBuilder.replaceQueryParam(name, values);
            return this;
        }

        /**{@inheritDoc}*/
        public SimpleUriBuilder replaceQueryParams(MultiValueMap<String, String> params) {
            uriComponentsBuilder.replaceQueryParams(params);
            return this;
        }

        /**{@inheritDoc}*/
        public SimpleUriBuilder fragment(@Nullable String fragment) {
            uriComponentsBuilder.fragment(fragment);
            return this;
        }

        /**{@inheritDoc}*/
        public URI build(Map<String, ?> uriVars) {
            Map<String, ?> variables = getDefaultUriVariables();
            if (!CollectionUtils.isEmpty(variables)) {
                Map<String, Object> map = new HashMap<>(variables.size() + uriVars.size());
                map.putAll(variables);
                map.putAll(uriVars);
                uriVars = map;
            }


            if (getEncodingMode().equals(EncodingMode.VALUES_ONLY)) {
                uriVars = UriUtils.encodeUriVariables(uriVars);
            }
            UriComponents uric = uriComponentsBuilder.build().expand(uriVars);
            return createUri(uric);
        }

        /**{@inheritDoc}*/
        public URI build(Object... uriVars) {
            Map<String, ?> variables = getDefaultUriVariables();

            if (ObjectUtils.isEmpty(uriVars) && !CollectionUtils.isEmpty(variables)) {
                return build(Collections.emptyMap());
            }

            if (getEncodingMode().equals(EncodingMode.VALUES_ONLY)) {
                uriVars = UriUtils.encodeUriVariables(uriVars);
            }
            UriComponents uric = uriComponentsBuilder.build().expand(uriVars);
            return createUri(uric);
        }

        private URI createUri(UriComponents uric) {
            if (getEncodingMode().equals(EncodingMode.URI_COMPONENT)) {
                uric = uric.encode();
            }
            return URI.create(uric.toString());
        }

        /**{@inheritDoc}*/
        public String toUriString() {
            return uriComponentsBuilder.build().toUriString();
        }
    }
}