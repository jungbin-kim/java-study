package kim.jungbin.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuples;

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


    @Test
    public void flux_empty_collectList() {
        var fluxEmptyCollectList = Flux.empty()
                                       .collectList()
                                       .log("flux_empty_collectList");

        StepVerifier.create(fluxEmptyCollectList)
                    .expectNextMatches(List::isEmpty)
                    .verifyComplete();

        var fluxReturnEmpty = Flux.fromIterable(List.of())
                                  .doOnNext(r -> System.out.println("Flux empty so doOnNext is not called."))
                                  .log("flux_fromIterable_empty");
        StepVerifier.create(fluxReturnEmpty)
                    .verifyComplete();

    }


    @Test
    public void flux_in_flux() {
        var iList = List.of(1, 2, 3, 4);
        var jList = List.of(5, 6, 7, 8);
        var fluxInFlux = Flux.fromIterable(iList)
                             .flatMap(i -> {
                                 return Flux.fromIterable(jList)
                                            .map(j -> Tuples.of(i, j));
                             })
                             .doOnNext(tuples -> {
                                 System.out.println("Tuples = " + tuples);
                             });
        StepVerifier.create(fluxInFlux)
                    .expectNextCount(iList.size() * jList.size())
                    .verifyComplete();
    }
}
