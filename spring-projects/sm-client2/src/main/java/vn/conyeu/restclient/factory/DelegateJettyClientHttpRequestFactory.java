package vn.conyeu.restclient.factory;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.JettyClientHttpRequestFactory;
import vn.conyeu.restclient.DelegateRequestFactory;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

public class DelegateJettyClientHttpRequestFactory implements DelegateRequestFactory {
    private final JettyClientHttpRequestFactory delegate;

    public DelegateJettyClientHttpRequestFactory(JettyClientHttpRequestFactory delegate) {
        this.delegate = delegate;
    }

    /**
     * Set the underlying connect timeout in milliseconds.
     * A value of 0 specifies an infinite timeout.
     */
    @Override
    public void setConnectTimeout(Integer connectTimeout) {
        delegate.setConnectTimeout(connectTimeout);
    }

    /**
     * Set the underlying connect timeout in milliseconds.
     * A value of 0 specifies an infinite timeout.
     */
    @Override
    public void setConnectTimeout(Duration connectTimeout) {
        delegate.setConnectTimeout(connectTimeout);
    }

    /**
     * Set the underlying read timeout in milliseconds.
     */
    @Override
    public void setReadTimeout(Integer readTimeout) {
        delegate.setReadTimeout(readTimeout);
    }

    /**
     * Set the underlying read timeout as {@code Duration}.
     * <p>Default is 10 seconds.
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