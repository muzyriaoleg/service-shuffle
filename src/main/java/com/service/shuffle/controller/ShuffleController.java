package com.service.shuffle.controller;

import com.service.shuffle.service.AsyncLoggingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/shuffle")
public class ShuffleController {

    private static final Logger logger = LoggerFactory.getLogger(ShuffleController.class);
    private final AsyncLoggingService asyncLoggingService;
    @Value("${service-log.url}")
    private String logServiceUrl;

    public ShuffleController(AsyncLoggingService asyncLoggingService) {
        this.asyncLoggingService = asyncLoggingService;
    }

    @PostMapping
    public ResponseEntity<List<Integer>> shuffle(@RequestBody int number) {
        if (number < 1 || number > 1000) {
            logger.error("Wrong number provided: {}", number);
            throw new IllegalArgumentException("Number must be between 1 and 1000.");
        }
        List<Integer> shuffledList = IntStream.rangeClosed(1, number)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(shuffledList);

        asyncLoggingService.logRequest(number, shuffledList);

        return ResponseEntity.ok(shuffledList);
    }
}
