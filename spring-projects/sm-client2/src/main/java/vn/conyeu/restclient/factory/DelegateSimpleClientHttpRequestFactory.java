package vn.conyeu.restclient.factory;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import vn.conyeu.restclient.DelegateRequestFactory;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

public class DelegateSimpleClientHttpRequestFactory implements DelegateRequestFactory {
    private final SimpleClientHttpRequestFactory delegate;

    public DelegateSimpleClientHttpRequestFactory(SimpleClientHttpRequestFactory delegate) {
        this.delegate = delegate;
    }

    /**
     * Set the underlying URLConnection's connect timeout (in milliseconds).
     * A timeout value of 0 specifies an infinite timeout.
     * <p>Default is the system's default timeout.
     */
    public void setConnectTimeout(int connectTimeout) {
        delegate.setConnectTimeout(connectTimeout);
    }

    /**
     * Set the underlying URLConnection's connect timeout as {@code Duration}.
     * A timeout value of 0 specifies an infinite timeout.
     * <p>Default is the system's default timeout.
     */
    @Override
    public void setConnectTimeout(Duration connectTimeout) {
        delegate.setConnectTimeout(connectTimeout);
    }

    /**
     * Set the underlying URLConnection's read timeout (in milliseconds).
     * A timeout value of 0 specifies an infinite timeout.
     * <p>Default is the system's default timeout.
     */
    public void setReadTimeout(int readTimeout) {
        delegate.setReadTimeout(readTimeout);
    }

    /**
     * Set the underlying URLConnection's read timeout (in milliseconds).
     * A timeout value of 0 specifies an infinite timeout.
     * <p>Default is the system's default timeout.
     */
    @Override
    public void setReadTimeout(Duration readTimeout) {
        delegate.setReadTimeout(readTimeout);
    }

    @Override
    public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
        return delegate.createRequest(uri, httpMethod);
    }
}