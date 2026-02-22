package com.aditya.olap_performance.repository;

 import com.aditya.olap_performance.domain.Train;
 import com.example.performance.db.tables.records.TrainRecord;
 import lombok.RequiredArgsConstructor;
 import lombok.extern.log4j.Log4j;
 import lombok.extern.log4j.Log4j2;
 import org.jooq.DSLContext;
 import org.jooq.impl.DSL;
 import org.springframework.data.repository.reactive.ReactiveCrudRepository;
 import org.springframework.stereotype.Service;
 import reactor.core.publisher.Flux;
 import reactor.core.publisher.Mono;

 import static com.example.performance.db.Tables.TRAIN;

@RequiredArgsConstructor
@Service
@Log4j2
public  class ClickHouseCrudRepository   {

    private final DSLContext dsl;


    public Flux<Train> findAll(){

        return Flux.from(dsl.selectFrom(TRAIN).limit(1)).map(r-> r.into(Train.class)) ;
    }}
