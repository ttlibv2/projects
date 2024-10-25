package vn.conyeu.restclient.factory;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.restclient.DelegateRequestFactory;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

public class DelegateHttpComponentsClientHttpRequestFactory implements DelegateRequestFactory {
    private final HttpComponentsClientHttpRequestFactory delegate;

    public DelegateHttpComponentsClientHttpRequestFactory(HttpComponentsClientHttpRequestFactory delegate) {
        this.delegate = Asserts.notNull(delegate);
    }

    /**
     * Set the underlying connect timeout in milliseconds.
     * A value of 0 specifies an infinite timeout.
     */
    @Override
    public void setConnectTimeout(Integer connectTimeout) {
        delegate.setConnectTimeout(connectTimeout);
        delegate.setConnectionRequestTimeout(connectTimeout);
    }

    /**
     * Set the underlying connect timeout in milliseconds.
     * A value of 0 specifies an infinite timeout.
     */
    @Override
    public void setConnectTimeout(Duration connectTimeout) {
        delegate.setConnectTimeout(connectTimeout);
        delegate.setConnectionRequestTimeout(connectTimeout);
    }

    /**
     * Set the underlying read timeout in milliseconds.
     */
    @Override
    public void setReadTimeout(Integer readTimeout) {

    }

    /**
     * Set the underlying read timeout as {@code Duration}.
     * <p>Default is 10 seconds.
     */
    @Override
    public void setReadTimeout(Duration readTimeout) {

    }

    /**
     * Create a new {@link ClientHttpRequest} for the specified URI and HTTP method.
     * <p>The returned request can be written to, and then executed by calling
     * {@link ClientHttpRequest#execute()}.
     * @param uri the URI to create a request for
     * @param httpMethod the HTTP method to execute
     * @return the created request
     * @throws IOException in case of I/O errors
     */
    @Override
    public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
        return delegate.createRequest(uri, httpMethod);
    }
}