package com.playground.webflux.api;

import com.playground.webflux.domain.Todo;
import com.playground.webflux.domain.TodoRepository;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.time.LocalDateTime;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@RestController
public class HelloAPI {

    private int cont = 0;
    @Autowired
    private TodoRepository repository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    @Qualifier("jdbcSchedule")
    private Scheduler jdbcSchedule;

    @GetMapping
    @ResponseBody
    public Publisher<String> sayHello() {
        return Mono.just("Hello World Web Flux");
    }

    @ResponseBody
    @GetMapping("/{id}")
    public Mono<Todo> getById(@PathVariable Long id) {
        return Mono.justOrEmpty(this.repository.findById(id));
    }


    @GetMapping("/all")
    public Flux<Todo> getAll() {
        return Flux.defer(() -> Flux.fromIterable(this.repository.findAll())).subscribeOn(this.jdbcSchedule);
    }

    @PostMapping
    public Mono<Todo> save(@RequestBody Todo todo) {
        return Mono.fromCallable(() -> this.transactionTemplate.execute(action -> repository.save(todo)));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> remove(@PathVariable Long id) {
        return Mono.fromCallable(() -> this.transactionTemplate.execute(action -> {
            this.repository.deleteById(id);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        })).subscribeOn(jdbcSchedule);
    }

    @GetMapping(value = "/items/{name}")
    public Mono<String> getItems(@PathVariable String name) throws InterruptedException {
        Thread.sleep(10000L);
        System.out.println("Chamada" + LocalDateTime.now() + " contador: " + cont++ + " " + name);
        WebClient client = WebClient.create("https://api.mercadolibre.com");
        return client.get()
                .uri("/items/MLB946735475", "1")
                .retrieve()
                .bodyToMono(String.class);

    }

    private PersonHandler handler = new PersonHandler();

    @Bean
    public RouterFunction<ServerResponse> teste(){
        return route(GET("/oi"), handler::listPeople)
                .and(route(GET("/oi/{name}"), req -> {
                    System.out.println(req.pathVariable("name"));
                    return ServerResponse.notFound().build();
                }));

    }
}


class PersonHandler {

    public Mono<ServerResponse> listPeople(ServerRequest request) {
        Mono<String> mono = Mono.just("Hello WebFlux");
        return ServerResponse.ok().body(mono, String.class);
    }

}