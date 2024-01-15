package com.tradebit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {
    @Bean(name = "botTaskExecutor")
    public ThreadPoolTaskExecutor botTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Set the core number of threads
        executor.setMaxPoolSize(20); // Set the maximum number of threads
        executor.setQueueCapacity(50); // Set the size of the queue
        executor.initialize();
        return executor;
    }
}
