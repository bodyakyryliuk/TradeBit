package com.tradebit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BinanceServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BinanceServiceApplication.class, args);
    }

}
