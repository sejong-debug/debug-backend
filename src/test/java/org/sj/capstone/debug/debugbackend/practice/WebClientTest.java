package org.sj.capstone.debug.debugbackend.practice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.sj.capstone.debug.debugbackend.common.TestDataConfig;
import org.sj.capstone.debug.debugbackend.dto.common.ApiResult;
import org.sj.capstone.debug.debugbackend.dto.issue.IssueDetectionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;


@SpringBootTest
@Slf4j
@Import(TestDataConfig.class)
@Transactional
@Disabled
class WebClientTest {

    @Autowired
    WebClient.Builder webClientBuilder;

    String baseUrl = "http://127.0.0.1:5000/";

    @Test
    void practice1() {
        WebClient webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
        Mono<String> stringMono = webClient.get().retrieve().bodyToMono(String.class);
        System.out.println("실행 확인");
        System.out.println("stringMono = " + stringMono.blockOptional().get());
        System.out.println("WebClient end");
    }

    @Test
    void practice2() throws IOException {

        MockMultipartFile image = new MockMultipartFile("image", "test.png",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream("/Users/jody/Downloads/img.jpg"));

        WebClient webClient = webClientBuilder
                .baseUrl(baseUrl + "/detect-issue")
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE);
                    httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                })
                .filter(logRequest())
                .build();

        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("image", new ByteArrayResource(image.getBytes()))
                .contentType(MediaType.valueOf(Objects.requireNonNull(image.getContentType())))
                .filename(image.getOriginalFilename())
        ;
        multipartBodyBuilder.part("boardImageId", 10L);
        /*BodyInserters.FormInserter<Object> multipart = BodyInserters.fromMultipartData(
                "image", new ByteArrayResource(image.getBytes())).with(
                "boardImageId", 1L);*/

        Mono<IssueDetectionDto> mono = webClient.post()
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .retrieve()
                .bodyToMono(IssueDetectionDto.class)
                .flatMap(body -> {
                    log.info("body: {}", body);
                    return Mono.just(body);
                })
                .log();
        System.out.println("실행 확인");
        System.out.println("mono = " + mono.block());
        System.out.println("WebClient end");
    }

    // This method returns filter function which will log request data
    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

}
