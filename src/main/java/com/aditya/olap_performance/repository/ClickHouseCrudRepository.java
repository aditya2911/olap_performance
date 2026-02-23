package com.aditya.olap_performance.repository;

import com.aditya.olap_performance.domain.Train;
import com.aditya.olap_performance.dto.PaginatedResponse;
import com.aditya.olap_performance.dto.QueryRequest;
import com.aditya.olap_performance.service.QueryBuilderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.jooq.Record;
 import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static com.example.performance.db.Tables.TRAIN;
@Service
@RequiredArgsConstructor
@Log4j2
public class ClickHouseCrudRepository {

    private final DSLContext dsl;
    private final QueryBuilderService queryBuilderService;

    public Flux<Train> findAll() {
        return Flux.from(dsl.selectFrom(TRAIN).limit(1))
                .map(this::mapToTrain);
    }

    public Mono<PaginatedResponse<Train>> search(QueryRequest request) {
        queryBuilderService.validateQueryRequest(request);

        Mono<Long> countMono = queryBuilderService.countTotal(request);
        Flux<Record> recordsFlux = queryBuilderService.executeQuery(request);

        return Mono.zip(countMono, recordsFlux.collectList())
                .map(tuple -> {
                    Long totalCount = tuple.getT1();
                    var records = tuple.getT2();

                    var trains = records.stream()
                            .map(this::mapToTrain)
                            .toList();

                    boolean hasMore = (request.getOffset() + request.getLimit()) < totalCount;

                    return PaginatedResponse.<Train>builder()
                            .data(trains)
                            .totalCount(totalCount)
                            .hasMore(hasMore)
                            .limit(request.getLimit())
                            .offset(request.getOffset())
                            .build();
                });
    }

    private Train mapToTrain(Record record) {

        return new Train(
                record.get("Id", String.class),
                record.get("MSSubClass", Long.class),
                record.get("MSZoning", String.class),
                record.get("LotFrontage", Double.class),
                record.get("LotArea", Long.class),
                record.get("Street", String.class),
                record.get("Alley", String.class),
                record.get("LotShape", String.class),
                record.get("LandContour", String.class),
                record.get("Utilities", String.class),
                record.get("LotConfig", String.class),
                record.get("LandSlope", String.class),
                record.get("Neighborhood", String.class),
                record.get("Condition1", String.class),
                record.get("Condition2", String.class),
                record.get("BldgType", String.class),
                record.get("HouseStyle", String.class),
                record.get("OverallQual", Long.class),
                record.get("OverallCond", Long.class),
                record.get("YearBuilt", Long.class),
                record.get("YearRemodAdd", Long.class),
                record.get("RoofStyle", String.class),
                record.get("RoofMatl", String.class),
                record.get("Exterior1st", String.class),
                record.get("Exterior2nd", String.class),
                record.get("MasVnrType", String.class),
                record.get("MasVnrArea", Double.class),
                record.get("ExterQual", String.class),
                record.get("ExterCond", String.class),
                record.get("Foundation", String.class),
                record.get("BsmtQual", String.class),
                record.get("BsmtCond", String.class),
                record.get("BsmtExposure", String.class),
                record.get("BsmtFinType1", String.class),
                record.get("BsmtFinSF1", Long.class),
                record.get("BsmtFinType2", String.class),
                record.get("BsmtFinSF2", Long.class),
                record.get("BsmtUnfSF", Long.class),
                record.get("TotalBsmtSF", Long.class),
                record.get("Heating", String.class),
                record.get("HeatingQC", String.class),
                record.get("CentralAir", String.class),
                record.get("Electrical", String.class),
                record.get("1stFlrSF", Long.class),
                record.get("2ndFlrSF", Long.class),
                record.get("LowQualFinSF", Long.class),
                record.get("GrLivArea", Long.class),
                record.get("BsmtFullBath", Long.class),
                record.get("BsmtHalfBath", Long.class),
                record.get("FullBath", Long.class),
                record.get("HalfBath", Long.class),
                record.get("BedroomAbvGr", Long.class),
                record.get("KitchenAbvGr", Long.class),
                record.get("KitchenQual", String.class),
                record.get("TotRmsAbvGrd", Long.class),
                record.get("Functional", String.class),
                record.get("Fireplaces", Long.class),
                record.get("FireplaceQu", String.class),
                record.get("GarageType", String.class),
                record.get("GarageYrBlt", Double.class),
                record.get("GarageFinish", String.class),
                record.get("GarageCars", Long.class),
                record.get("GarageArea", Long.class),
                record.get("GarageQual", String.class),
                record.get("GarageCond", String.class),
                record.get("PavedDrive", String.class),
                record.get("WoodDeckSF", Long.class),
                record.get("OpenPorchSF", Long.class),
                record.get("EnclosedPorch", Long.class),
                record.get("3SsnPorch", Long.class),
                record.get("ScreenPorch", Long.class),
                record.get("PoolArea", Long.class),
                record.get("PoolQC", String.class),
                record.get("Fence", String.class),
                record.get("MiscFeature", String.class),
                record.get("MiscVal", Long.class),
                record.get("MoSold", Long.class),
                record.get("YrSold", Long.class),
                record.get("SaleType", String.class),
                record.get("SaleCondition", String.class),
                record.get("SalePrice", Long.class)
        );
    }
}
