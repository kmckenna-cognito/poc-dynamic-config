package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@RefreshScope
public class ExampleService {
    private static final Logger log = LoggerFactory.getLogger(ExampleService.class);

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ExampleService.class, args);

        while (true) {
            log.trace("Trace");
            log.debug("Debug");
            log.info("Info");
            log.warn("Warn");

            Thread.sleep(3000);
            System.out.println();
        }
    }
}
