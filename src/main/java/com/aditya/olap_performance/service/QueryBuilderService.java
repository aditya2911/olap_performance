package com.aditya.olap_performance.service;

import com.aditya.olap_performance.dto.DateRange;
import com.aditya.olap_performance.dto.FilterCondition;
import com.aditya.olap_performance.dto.Filters;
import com.aditya.olap_performance.dto.OrderBy;
import com.aditya.olap_performance.dto.QueryRequest;
import lombok.extern.log4j.Log4j2;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.OrderField;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.performance.db.tables.Train.TRAIN;

@Service
@Log4j2
public class QueryBuilderService {

    private static final Set<String> VALID_COLUMNS = new HashSet<>(Arrays.asList(
            "Id", "MSSubClass", "MSZoning", "LotFrontage", "LotArea", "Street", "Alley",
            "LotShape", "LandContour", "Utilities", "LotConfig", "LandSlope", "Neighborhood",
            "Condition1", "Condition2", "BldgType", "HouseStyle", "OverallQual", "OverallCond",
            "YearBuilt", "YearRemodAdd", "RoofStyle", "RoofMatl", "Exterior1st", "Exterior2nd",
            "MasVnrType", "MasVnrArea", "ExterQual", "ExterCond", "Foundation", "BsmtQual",
            "BsmtCond", "BsmtExposure", "BsmtFinType1", "BsmtFinSF1", "BsmtFinType2",
            "BsmtFinSF2", "BsmtUnfSF", "TotalBsmtSF", "Heating", "HeatingQC", "CentralAir",
            "Electrical", "FirstFlrSF", "SecondFlrSF", "LowQualFinSF", "GrLivArea",
            "BsmtFullBath", "BsmtHalfBath", "FullBath", "HalfBath", "BedroomAbvGr",
            "KitchenAbvGr", "KitchenQual", "TotRmsAbvGrd", "Functional", "Fireplaces",
            "FireplaceQu", "GarageType", "GarageYrBlt", "GarageFinish", "GarageCars",
            "GarageArea", "GarageQual", "GarageCond", "PavedDrive", "WoodDeckSF",
            "OpenPorchSF", "EnclosedPorch", "ThreeSsnPorch", "ScreenPorch", "PoolArea",
            "PoolQC", "Fence", "MiscFeature", "MiscVal", "MoSold", "YrSold", "SaleType",
            "SaleCondition", "SalePrice"
    ));

    private static final Set<String> VALID_OPERATORS = Set.of(
            "eq", "neq", "gt", "lt", "like", "in"
    );

    private final DSLContext dsl;

    public QueryBuilderService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public void validateQueryRequest(QueryRequest request) {
        if (request.getFilters() != null && request.getFilters().getAnd() != null) {
            for (FilterCondition filter : request.getFilters().getAnd()) {
                validateFilterField(filter.getField());
                validateOperator(filter.getOp());
                validateFilterValue(filter);
            }
        }

        if (request.getOrderBy() != null) {
            validateFilterField(request.getOrderBy().getField());
        }
    }

    private void validateFilterField(String field) {
        String normalizedField = normalizeFieldName(field);
        if (!VALID_COLUMNS.contains(normalizedField)) {
            throw new IllegalArgumentException("Invalid field: " + field + ". Valid fields are: " + VALID_COLUMNS);
        }
    }

    private void validateOperator(String op) {
        if (!VALID_OPERATORS.contains(op.toLowerCase())) {
            throw new IllegalArgumentException("Invalid operator: " + op + ". Valid operators are: " + VALID_OPERATORS);
        }
    }

    private void validateFilterValue(FilterCondition filter) {
        String op = filter.getOp().toLowerCase();
        Object value = filter.getValue();

        if ("in".equals(op)) {
            if (!(value instanceof List)) {
                throw new IllegalArgumentException("Operator 'in' requires a list value for field: " + filter.getField());
            }
            List<?> listValue = (List<?>) value;
            if (listValue.isEmpty()) {
                throw new IllegalArgumentException("Operator 'in' requires a non-empty list for field: " + filter.getField());
            }
        }
    }

    private String normalizeFieldName(String field) {
        if (field == null) {
            return null;
        }
        return field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    @SuppressWarnings("unchecked")
    public Condition buildConditions(QueryRequest request) {
        Condition condition = null;

        if (request.getDateRange() != null) {
            Condition dateCondition = buildDateRangeCondition(request.getDateRange());
            condition = condition != null ? condition.and(dateCondition) : dateCondition;
        }

        if (request.getFilters() != null && request.getFilters().getAnd() != null) {
            for (FilterCondition filter : request.getFilters().getAnd()) {
                Condition filterCondition = buildFilterCondition(filter);
                condition = condition != null ? condition.and(filterCondition) : filterCondition;
            }
        }

        return condition;
    }

    private Condition buildDateRangeCondition(DateRange dateRange) {
        LocalDateTime start = dateRange.getStart();
        LocalDateTime end = dateRange.getEnd();

        int startYear = start.getYear();
        int startMonth = start.getMonthValue();
        int endYear = end.getYear();
        int endMonth = end.getMonthValue();

        Field<Integer> yrSoldField = DSL.field(DSL.name("YrSold"), Integer.class);
        Field<Integer> moSoldField = DSL.field(DSL.name("MoSold"), Integer.class);

        Condition startCondition = yrSoldField.gt(startYear)
                .or(yrSoldField.eq(startYear).and(moSoldField.ge(startMonth)));

        Condition endCondition = yrSoldField.lt(endYear)
                .or(yrSoldField.eq(endYear).and(moSoldField.le(endMonth)));

        return startCondition.and(endCondition);
    }

    @SuppressWarnings("unchecked")
    private Condition buildFilterCondition(FilterCondition filter) {
        String fieldName = normalizeFieldName(filter.getField());
        Field<Object> field = DSL.field(DSL.name(fieldName), Object.class);
        String op = filter.getOp().toLowerCase();
        Object value = filter.getValue();

        return switch (op) {
            case "eq" -> field.eq(value);
            case "neq" -> field.notEqual(value);
            case "gt" -> field.gt(value);
            case "lt" -> field.lt(value);
            case "like" -> field.like("%" + value + "%");
            case "in" -> {
                List<?> listValue = (List<?>) value;
                yield field.in(listValue.toArray());
            }
            default -> throw new IllegalArgumentException("Unsupported operator: " + op);
        };
    }

    public List<OrderField<?>> buildOrderBy(QueryRequest request) {
        if (request.getOrderBy() == null || request.getOrderBy().getField() == null) {
            return List.of();
        }

        String fieldName = normalizeFieldName(request.getOrderBy().getField());
        Field<Object> field = DSL.field(DSL.name(fieldName), Object.class);
        boolean isDesc = "DESC".equalsIgnoreCase(request.getOrderBy().getDirection());

        return isDesc ? List.of(field.desc()) : List.of(field.asc());
    }

    public Mono<Long> countTotal(QueryRequest request) {
        Field<Long> hllCount =
                DSL.function("uniqHLL12", Long.class, DSL.field("Id")).as("count");

        Condition condition = buildConditions(request);

        return Mono.from(
                        dsl.select(hllCount)
                                .from(DSL.table("train"))
                                .where(condition)
                )
                .map(r -> r.get(hllCount));   // returns Long
    }

    public Flux<Record> executeQuery(QueryRequest request) {
        Condition condition = buildConditions(request);
        return Flux.from(dsl.selectFrom(TRAIN)
                .where(condition)
                .orderBy(buildOrderBy(request))
                .limit(request.getLimit())
                .offset(request.getOffset()));
    }
}
