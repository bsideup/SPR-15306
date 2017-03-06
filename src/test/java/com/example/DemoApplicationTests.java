package com.example;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoApplicationTests {

    @LocalServerPort
    Integer port;

    @Test
    public void test() throws Exception {
        Flux<Map> result = WebClient.create()
                .get()
                .uri("http://localhost:" + port + "/")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .flatMap(response -> response.bodyToFlux(Map.class));

        StepVerifier.create(result)
                .expectNext(Collections.singletonMap("i", 1))
                .then(() -> Assert.assertTrue("should subscribe", DemoApplication.subscribed.get()))
                .thenCancel()
                .verify();

        // Give it some time...
        TimeUnit.SECONDS.sleep(1);

        Assert.assertFalse("should unsubscribe", DemoApplication.subscribed.get());
    }


}
