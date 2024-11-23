package com.example.service;


import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RateLimiterService {

    @Autowired
    private RateLimiterRegistry rateLimiterRegistry;

    public boolean isAllowed(String user) {
        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter(user);
        log.info("Checking rate limit for user: {}", user);
        return rateLimiter.acquirePermission();
    }
}
