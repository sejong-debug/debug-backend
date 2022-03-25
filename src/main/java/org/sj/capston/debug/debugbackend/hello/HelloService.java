package org.sj.capston.debug.debugbackend.hello;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HelloService {

    private final HelloRepository helloRepository;

    public Hello getById(Long id) {
        Optional<Hello> hello = helloRepository.findById(id);
        return hello.get();
    }

    @Transactional
    public long save(Hello hello) {
        return helloRepository.save(hello).getId();
    }
}
