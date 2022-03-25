package org.sj.capston.debug.debugbackend.hello;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
@RequiredArgsConstructor
public class HelloController {

    private final HelloService helloService;

    @GetMapping
    public Hello getHello() {
        Hello hello = Hello.builder()
                .name("Test Hello")
                .build();
        long helloId = helloService.save(hello);
        Hello findHello = helloService.getById(helloId);
        return findHello;
    }
}
