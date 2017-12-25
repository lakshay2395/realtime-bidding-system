package com.realtimebidding.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages={"com.realtimebidding.*"})
@EnableMongoRepositories("com.realtimebidding.repository")
public class RealTimeBiddingApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealTimeBiddingApplication.class, args);
	}
}
