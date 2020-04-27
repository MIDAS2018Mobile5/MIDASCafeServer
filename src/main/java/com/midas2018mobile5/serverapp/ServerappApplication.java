package com.midas2018mobile5.serverapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EntityScan(
        basePackageClasses = {Jsr310Converters.class},
        basePackages = {"com"})
@SpringBootApplication
public class ServerappApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerappApplication.class, args);
    }

    @Bean
    public String getCurrentPath() {
        return System.getProperty("user.dir");
    }
}
