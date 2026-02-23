package com.aditya.olap_performance.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.StatementType;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        // Build the ClickHouse ConnectionFactory manually

        return ConnectionFactories.get("r2dbc:clickhouse:http://default:password123@localhost:8123/default");
    }

    @Bean
    public DSLContext dsl(ConnectionFactory connectionFactory) {
        var dsl = DSL.using(new DefaultConfiguration()
                // jOOQ has native support for ClickHouse
                .set(SQLDialect.CLICKHOUSE)
                .set(connectionFactory)


        );

        dsl.configuration().settings().withStatementType(StatementType.STATIC_STATEMENT);
        return dsl;
    }
}
