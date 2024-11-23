package com.example.controller;

import com.example.service.RateLimiterService;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
@Slf4j
public class ApiGatewayController {

    @Autowired
    private RateLimiterService rateLimiterService;

    @Autowired
    private RateLimiterRegistry rateLimiterRegistry;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/{api}")
    @RateLimiter(name="user1")
    public String api1(@PathVariable(value = "api") String api, @RequestParam("user") String user) {
        return handleRequest(api, user);
    }

    @ResponseBody
    @RequestMapping("/hello")
    public String hello() {
        log.info("hello");
        return "hello";
    }

    @PostMapping("/{api}")
    @RateLimiter(name="user1")
    public String api2(@PathVariable("api") String api, @RequestParam("user") String user) {
        return handleRequest(api, user);
    }

    @PutMapping("/{api}")
    @RateLimiter(name="user1")
    public String api3(@PathVariable("api") String api, @RequestParam("user") String user) {
        return handleRequest(api, user);
    }

    private String handleRequest(String api, String user) {
        log.info("Handling request for user: {}", user);
        if (rateLimiterService.isAllowed(user)) {
            // 记录流量
            String key = user + ":" + api;
            Long count = redisTemplate.opsForValue().increment(key, 1);
            if (count == 1) {
                redisTemplate.expire(key, 1, TimeUnit.MINUTES);
            }
            System.out.println("User " + user + " accessed " + api + " " + count + " times.");
            log.info("Request processed successfully for user: {}", user);
            return "Request processed successfully";
        } else {
            log.info("Rate limit exceeded for user: {}", user);
            return "Rate limit exceeded. Please try again later.";
        }
    }
}