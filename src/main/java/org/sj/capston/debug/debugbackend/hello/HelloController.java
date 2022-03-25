package org.sj.capston.debug.debugbackend.hello;

import lombok.RequiredArgsConstructor;
import org.sj.capston.debug.debugbackend.common.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
@RequiredArgsConstructor
public class HelloController {

    private final HelloService helloService;

    @GetMapping("/{id}")
    public Result<HelloDto> getHello(@PathVariable long id) {

        Hello findHello = helloService.getById(id);
        HelloDto helloDto = new HelloDto(findHello);

        return Result.<HelloDto>builder()
                .success(true)
                .data(helloDto)
                .build();
    }

    @PostMapping
    public Result<Void> saveHello(@RequestBody HelloDto helloDto) {

        Hello hello = Hello.builder()
                .name(helloDto.getName())
                .description(helloDto.getDescription())
                .build();

        helloService.save(hello);

        return Result.<Void>builder()
                .success(true)
                .build();
    }
}
