package org.sj.capstone.debug.debugbackend.practice;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URI;

@Disabled
class UriTest {

    @Test
    void uriBuilder() throws MalformedURLException {
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("ai-server")
                .path("detect-issue")
                .port(80)
                .build().toUri();
        System.out.println("uri = " + uri);
        System.out.println("uri.toURL().toString() = " + uri.toURL());
    }
}
