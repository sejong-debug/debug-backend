package org.sj.capstone.debug.debugbackend.util;

import org.sj.capstone.debug.debugbackend.dto.issue.IssueDetectionDto;
import org.sj.capstone.debug.debugbackend.error.ErrorCode;
import org.sj.capstone.debug.debugbackend.error.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class IssueDetectionClient {

    private final WebClient webClient;

    public IssueDetectionClient(
            @Value("${ai-server.schema}") String schema,
            @Value("${ai-server.host}") String host,
            @Value("${ai-server.port}") String port,
            @Value("${ai-server.issue-detection.path}") String path,
            WebClient.Builder webClientBuilder) {
        String baseUrl = UriComponentsBuilder.newInstance()
                .scheme(schema)
                .host(host)
                .port(StringUtils.hasText(port) ? port : null)
                .path(path)
                .build().toUri().toString();
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE);
                })
                .build();
    }

    public IssueDetectionDto request(long boardImageId, MultipartFile image) {
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("boardImageId", boardImageId);
            builder.part("image", new ByteArrayResource(image.getBytes()))
                    .filename(Objects.requireNonNull(image.getOriginalFilename()))
                    .contentType(MediaType.valueOf(Objects.requireNonNull(image.getContentType())));
            return webClient.post()
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(IssueDetectionDto.class)
                    .block();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.IMAGE_IO_EXCEPTION, e.getMessage());
        }
    }
}
