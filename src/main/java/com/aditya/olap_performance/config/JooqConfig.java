package com.aditya.olap_performance.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.StatementType;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static io.r2dbc.spi.ConnectionFactoryOptions.DATABASE;
import static io.r2dbc.spi.ConnectionFactoryOptions.DRIVER;
import static io.r2dbc.spi.ConnectionFactoryOptions.HOST;
import static io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD;
import static io.r2dbc.spi.ConnectionFactoryOptions.PORT;
import static io.r2dbc.spi.ConnectionFactoryOptions.USER;

@Configuration
public class JooqConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get(ConnectionFactoryOptions.builder()
                .option(DRIVER, "clickhouse")
                .option(HOST, "localhost")
                .option(PORT, 8123)
                .option(USER, "default")
                .option(PASSWORD, "password123")
                .option(DATABASE, "default")
                .option(Option.valueOf("connectionTimeout"), Duration.ofSeconds(30))
                .option(Option.valueOf("socketTimeout"), Duration.ofSeconds(60))
                .build());
    }

    @Bean
    public DSLContext dsl(ConnectionFactory connectionFactory) {
        var dsl = DSL.using(new DefaultConfiguration()
                .set(SQLDialect.CLICKHOUSE)
                .set(connectionFactory)
        );

        dsl.configuration().settings().withStatementType(StatementType.STATIC_STATEMENT);
        return dsl;
    }
}
