package com.service.shuffle.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AsyncLoggingService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncLoggingService.class);
    private final RestTemplate restTemplate;
    @Value("${service-log.url}")
    private String serviceLogUrl;

    public AsyncLoggingService() {
        this.restTemplate = new RestTemplate();
    }

    @Async
    public void logRequest(int number, List<Integer> shuffledNumberList) {
        logger.info("Sending log request for number {}", number);
        try {
            restTemplate.postForLocation(serviceLogUrl, shuffledNumberList);
        } catch (Exception e) {
            logger.error("Logging failed", e);
        }
    }
}
