package com.aditya.olap_performance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.r2dbc.autoconfigure.R2dbcAutoConfiguration;

@SpringBootApplication(exclude = {
		R2dbcAutoConfiguration.class,

})
public class OlapPerformanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OlapPerformanceApplication.class, args);
	}

}
