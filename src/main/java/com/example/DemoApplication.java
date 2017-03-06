package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@SpringBootApplication
@RestController
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping(value = "/", produces = "text/event-stream")
	public Flux<Integer> test() {
		return Flux.just(1).log("bug").repeatWhen(it -> it.delayElementsMillis(1_000)).distinctUntilChanged();
	}
}
