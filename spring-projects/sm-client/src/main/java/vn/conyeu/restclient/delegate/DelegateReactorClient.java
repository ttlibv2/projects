package vn.conyeu.restclient.delegate;

import org.springframework.context.SmartLifecycle;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Function;

public class DelegateReactorClient implements ClientHttpConnector, SmartLifecycle {
    private final ReactorClientHttpConnector delegate;

    public DelegateReactorClient(ReactorClientHttpConnector delegate) {
        this.delegate = delegate;
    }

    @Override
    public Mono<ClientHttpResponse> connect(HttpMethod method, URI uri, Function<? super ClientHttpRequest, Mono<Void>> requestCallback) {
        return delegate.connect(method, uri, requestCallback);
    }

    @Override
    public void start() {
        delegate.start();
    }

    @Override
    public void stop() {
        delegate.stop();
    }

    @Override
    public void stop(Runnable callback) {
        delegate.stop(callback);
    }

    @Override
    public boolean isRunning() {
        return delegate.isRunning();
    }

    @Override
    public boolean isAutoStartup() {
        return delegate.isAutoStartup();
    }

    @Override
    public int getPhase() {
        return delegate.getPhase();
    }

}