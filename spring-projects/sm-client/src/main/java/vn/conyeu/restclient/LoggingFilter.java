package vn.conyeu.restclient;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ClientHttpRequestDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

@Slf4j
@AllArgsConstructor
public class LoggingFilter implements ExchangeFilterFunction {

    /** Function&lt;logPrefix, ClientLogger&gt;*/
    private final Function<String, ClientLogger> loggerSupplier;

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        ClientLogger logger = loggerSupplier.apply(request.logPrefix());

        logger.forRequest(request);

        ClientRequest clientRequest = ClientRequest.from(request)
                .body(new CustomBodyInserter(request, logger)).build();

        return next.exchange(clientRequest)
                .doOnError(error -> logger.request().throwable(error))
                .doOnSuccess(response -> logger.submitRequest())
                .map(response -> response.mutate().body(transformer(logger, response)).build());
    }

    private Function<Flux<DataBuffer>, Flux<DataBuffer>> transformer(ClientLogger logger, ClientResponse response) {
        logger.forResponse(response);

        return transformer -> transformer
                .doOnNext((dataBuffer) -> {
                    logger.response().payload(dataBufferToString(dataBuffer));
                } )
                .doOnError(error -> logger.response().error(error))
                .doOnComplete(() -> {
                    logger.submitResponse();
                    logger.submitAll();
                });
    }

    private  static class CustomBodyInserter implements BodyInserter<Object, ClientHttpRequest> {
        final ClientRequest request;
        final ClientLogger logger;

        CustomBodyInserter(ClientRequest request, ClientLogger logger) {
            this.request = request;
            this.logger = logger;
        }

        @Override
        public Mono<Void> insert(ClientHttpRequest outputMessage, Context context) {
            return request.body().insert(new RequestDecorator(outputMessage, request, logger), context);
        }
    }

    private static class RequestDecorator extends ClientHttpRequestDecorator {
        final ClientRequest request;
        final ClientLogger logger;

        public RequestDecorator(ClientHttpRequest delegate, ClientRequest request, ClientLogger logger) {
            super(delegate);
            this.request = request;
            this.logger = logger;
        }

        public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
            return super.writeWith(Flux.from(body).doOnNext(data -> logger.request().payload(dataBufferToString(data))));
        }


    }

    static String dataBufferToString(DataBuffer data) {
        return data.toString(StandardCharsets.UTF_8);
    }
}