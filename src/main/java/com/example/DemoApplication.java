package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@SpringBootApplication
@RestController
public class DemoApplication {

	public static AtomicBoolean subscribed = new AtomicBoolean();

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping(value = "/", produces = "text/event-stream")
	public Flux<Map<String, Integer>> test() {
		return Flux.just(1)
				.log("just(1)")
				.repeatWhen(it -> it.delayElementsMillis(1_000))
				.log("flux")
				.doOnSubscribe(__ -> subscribed.set(true))
				.doOnCancel(() -> subscribed.set(false))
				.doOnComplete(() -> subscribed.set(false))
				.map(i -> Collections.singletonMap("i", i))
				.distinctUntilChanged();
	}
}
