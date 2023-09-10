package com.newbie.springboot3azure.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.InvalidObjectException;
import java.net.URI;
import java.util.function.Consumer;

@Service
@Slf4j
public class WebClientService {
    private final WebClient webClient;

    public WebClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    public static <T> Mono<T> execute(
            URI uri,
            HttpMethod method,
            BodyInserter<?, ? super ClientHttpRequest> body,
            Class<T> responseType,
            Consumer<HttpHeaders> headers) {

        var webClient = WebClient.builder().build();

        return webClient
                .method(method)
                .uri(uri)
                .body(body)
                .headers(headers)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.just(new InvalidObjectException("Server Error")))
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.just(new InvalidObjectException("Bad Request")))
                .bodyToMono(responseType);
    }
}
