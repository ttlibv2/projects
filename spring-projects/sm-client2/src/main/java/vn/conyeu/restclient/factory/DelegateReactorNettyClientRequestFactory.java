package vn.conyeu.restclient.factory;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ReactorNettyClientRequestFactory;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.restclient.DelegateRequestFactory;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

public class DelegateReactorNettyClientRequestFactory implements DelegateRequestFactory {
    private final ReactorNettyClientRequestFactory delegate;

    public DelegateReactorNettyClientRequestFactory(ReactorNettyClientRequestFactory delegate) {
        this.delegate = Asserts.notNull(delegate);
    }

    @Override
    public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
        return delegate.createRequest(uri, httpMethod);
    }

    /**
     * Set the underlying connect timeout in milliseconds.
     * A value of 0 specifies an infinite timeout.
     * <p>Default is 30 seconds.
     */
    public void setConnectTimeout(Integer connectTimeout) {
        delegate.setConnectTimeout(connectTimeout);
    }

    /**
     * Set the underlying connect timeout in milliseconds.
     * A value of 0 specifies an infinite timeout.
     * <p>Default is 30 seconds.
     */
    @Override
    public void setConnectTimeout(Duration connectTimeout) {
        delegate.setConnectTimeout(connectTimeout);
    }

    /**
     * Set the underlying read timeout in milliseconds.
     * <p>Default is 10 seconds.
     */
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

    /**
     * Set the timeout for the HTTP exchange in milliseconds.
     * <p>Default is 5 seconds.
     */
    public void setExchangeTimeout(long exchangeTimeout) {
        delegate.setExchangeTimeout(exchangeTimeout);
    }

    /**
     * Set the timeout for the HTTP exchange.
     * <p>Default is 5 seconds.
     */
    public void setExchangeTimeout(Duration exchangeTimeout) {
        delegate.setExchangeTimeout(exchangeTimeout);
    }
}