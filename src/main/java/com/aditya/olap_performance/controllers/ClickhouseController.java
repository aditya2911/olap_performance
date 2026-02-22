package com.aditya.olap_performance.controllers;


import com.aditya.olap_performance.domain.Train;
import com.aditya.olap_performance.repository.ClickHouseCrudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/v1/clickhouse")
@RequiredArgsConstructor
public class ClickhouseController {
    
    private final ClickHouseCrudRepository clickHouseCrudRepository;
    @GetMapping
    public Flux<Train> getTrainRecord(){
        
        return clickHouseCrudRepository.findAll();
        
    }
}
