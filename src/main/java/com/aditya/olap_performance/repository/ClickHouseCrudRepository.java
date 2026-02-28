package com.aditya.olap_performance.repository;

import com.aditya.olap_performance.domain.CardProgrammeApplied;
import com.aditya.olap_performance.domain.Transaction;
import com.aditya.olap_performance.domain.TransactionStatus;
import com.aditya.olap_performance.domain.Train;
import com.aditya.olap_performance.dto.PaginatedResponse;
import com.aditya.olap_performance.dto.QueryRequest;
import com.aditya.olap_performance.service.QueryBuilderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Log4j2
public class ClickHouseCrudRepository {

    private final DSLContext dsl;
    private final QueryBuilderService queryBuilderService;

    public Flux<Train> findAll() {
        return Flux.from(dsl.selectFrom(DSL.table("train")).limit(1))
                .map(this::mapToTrain);
    }

    public Mono<PaginatedResponse<Train>> search(QueryRequest request) {
        queryBuilderService.validateQueryRequest(request, "train");

        Mono<Long> countMono = queryBuilderService.countTotal(request, "train");
        Flux<Record> recordsFlux = queryBuilderService.executeQuery(request, "train");

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

    public Mono<PaginatedResponse<Transaction>> searchTransactions(QueryRequest request) {
        queryBuilderService.validateQueryRequest(request, "transactions");

        Mono<Long> countMono = queryBuilderService.countTotal(request, "transactions");
        Flux<Record> recordsFlux = queryBuilderService.executeQuery(request, "transactions");

        return Mono.zip(countMono, recordsFlux.collectList())
                .map(tuple -> {
                    Long totalCount = tuple.getT1();
                    var records = tuple.getT2();

                    var transactions = records.stream()
                            .map(this::mapToTransaction)
                            .toList();

                    boolean hasMore = (request.getOffset() + request.getLimit()) < totalCount;

                    return PaginatedResponse.<Transaction>builder()
                            .data(transactions)
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
                record.get("FirstFlrSF", Long.class),
                record.get("SecondFlrSF", Long.class),
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
                record.get("ThreeSsnPorch", Long.class),
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

    private Transaction mapToTransaction(Record record) {
        return new Transaction(
                record.get("acceptorIdentification", String.class),
                record.get("localDateAndTimeGMT", LocalDateTime.class),
                parseTransactionStatus(record.get("status", String.class)),
                parseCardProgramme(record.get("cardProgrammeApplied", String.class)),
                record.get("transactionAmount", Double.class),
                record.get("retrievalReferenceNumber", String.class),
                record.get("cardAcceptorTransactionReference", String.class),
                record.get("originalTransactionUniqueIdentifier", String.class),
                record.get("technicalAcceptorTransactionReference", String.class),
                record.get("systemTraceAuditNumber", Long.class),
                record.get("authorisationCode", String.class),
                record.get("acquirerIdentification", String.class),
                record.get("pan", String.class),
                record.get("par", String.class),
                record.get("terminalSerialNumber", String.class),
                record.get("terminalIdentification", String.class),
                record.get("terminalModel", String.class),
                record.get("terminalManufacturer", String.class),
                record.get("acceptorMerchantName", String.class),
                record.get("acceptorTerminalMerchantIdentifier", String.class),
                record.get("dataProvider", String.class),
                record.get("transactionEventType", String.class),
                record.get("paymentMethod", String.class),
                record.get("paymentMethodType", String.class),
                record.get("paymentUseCase", String.class),
                record.get("paymentDomain", String.class),
                record.get("channel", String.class),
                record.get("cardDataEntryMode", String.class),
                record.get("authorisationRequestAmount", Long.class),
                record.get("transactionGlobalAmount", Long.class),
                record.get("currentTransactionAmount", Long.class),
                record.get("tipsAmount", Long.class),
                record.get("localDate", String.class),
                record.get("localTimeZone", String.class),
                record.get("utcOffset", String.class),
                record.get("transactionDurationMs", Long.class),
                record.get("authorisationDurationMs", Long.class),
                record.get("lastUpdateTimeGMT", LocalDateTime.class),
                record.get("cardExpiryDate", LocalDate.class),
                record.get("authorisationIndicator", Integer.class),
                record.get("onUsCardIndicator", Integer.class),
                record.get("transactionTestIndicator", Integer.class),
                record.get("transactionCurrency", String.class),
                record.get("transactionCurrencyCode", String.class),
                record.get("authorisationRequestCurrency", String.class),
                record.get("authorisationRequestCurrencyCode", String.class),
                record.get("merchantReceipt", String.class),
                record.get("customerReceipt", String.class),
                record.get("events", String.class)
        );
    }

    private TransactionStatus parseTransactionStatus(String value) {
        if (value == null) return null;
        return switch (value.toUpperCase()) {
            case "ACCEPTED" -> TransactionStatus.ACCEPTED;
            case "SETTLED" -> TransactionStatus.SETTLED;
            case "AUTHORISED" -> TransactionStatus.AUTHORISED;
            case "PARTIALLY_REFUSED" -> TransactionStatus.PARTIALLY_REFUSED;
            case "REJECTED" -> TransactionStatus.REJECTED;
            case "COMPLETELY_REFUSED" -> TransactionStatus.COMPLETELY_REFUSED;
            case "CAPTURE_ERROR" -> TransactionStatus.CAPTURE_ERROR;
            case "PROCESSING" -> TransactionStatus.PROCESSING;
            default -> null;
        };
    }

    private CardProgrammeApplied parseCardProgramme(String value) {
        if (value == null) return null;
        return switch (value.toUpperCase()) {
            case "CB" -> CardProgrammeApplied.CB;
            case "AMEX" -> CardProgrammeApplied.AMEX;
            case "VISA" -> CardProgrammeApplied.VISA;
            case "MASTERCARD" -> CardProgrammeApplied.MASTERCARD;
            default -> null;
        };
    }
}
