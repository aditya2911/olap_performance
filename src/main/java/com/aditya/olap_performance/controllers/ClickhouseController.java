package com.aditya.olap_performance.controllers;

import com.aditya.olap_performance.domain.Transaction;
import com.aditya.olap_performance.domain.Train;
import com.aditya.olap_performance.dto.PaginatedResponse;
import com.aditya.olap_performance.dto.QueryRequest;
import com.aditya.olap_performance.repository.ClickHouseCrudRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/clickhouse")
@RequiredArgsConstructor
public class ClickhouseController {

    private final ClickHouseCrudRepository clickHouseCrudRepository;

    @GetMapping
    public Flux<Train> getTrainRecord() {
        return clickHouseCrudRepository.findAll();
    }

    @PostMapping("/query")
    public Mono<ResponseEntity<PaginatedResponse<Train>>> query(@Valid @RequestBody QueryRequest request) {
        return clickHouseCrudRepository.search(request)
                .map(ResponseEntity::ok)
                .onErrorResume(IllegalArgumentException.class, e ->
                        Mono.just(ResponseEntity.badRequest().build()));
    }

    @PostMapping("/transactions/query")
    public Mono<ResponseEntity<PaginatedResponse<Transaction>>> queryTransactions(@Valid @RequestBody QueryRequest request) {
        return clickHouseCrudRepository.searchTransactions(request)
                .map(ResponseEntity::ok)
                .onErrorResume(IllegalArgumentException.class, e ->
                        Mono.just(ResponseEntity.badRequest().build()));
    }
}
