package kim.jungbin.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TestFlux {

    @Test
    public void flux_collectList() {
        var fluxReturnAllEmpty = Flux.range(1, 10)
                                     .flatMap(i -> Mono.empty())
                                     .collectList()
                                     .log("flux_emptyList");

        StepVerifier.create(fluxReturnAllEmpty)
                    .expectNextMatches(List::isEmpty)
                    .verifyComplete();

        AtomicInteger ai = new AtomicInteger();
        var fluxReturnEmptyOrValue = Flux.range(1, 10)
                                         .flatMap(i -> {
                                             if (i < 5) {
                                                 return Mono.empty();
                                             }
                                             ai.addAndGet(1);
                                             return Mono.just(i);
                                         })
                                         .collectList()
                                         .log("flux_list");

        StepVerifier.create(fluxReturnEmptyOrValue)
                    .expectNextMatches(list -> list.size() == ai.get())
                    .verifyComplete();
    }
}
