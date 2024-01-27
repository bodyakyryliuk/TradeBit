package com.tradebit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {
    @Bean(name = "botTaskExecutor")
    public ThreadPoolTaskExecutor botTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(270); // Set the core number of threads
        executor.setMaxPoolSize(800); // Set the maximum number of threads
        executor.setQueueCapacity(500); // Set the size of the queue
        executor.setKeepAliveSeconds(60); // Time during which a thread over core pool size will be alive
        executor.initialize();
        return executor;
    }
}
