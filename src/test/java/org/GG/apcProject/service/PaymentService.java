package org.GG.apcProject.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private static final String CB_NAME = "paymentService";

    /**
     * Processes payment for a user.
     * CircuitBreaker triggers fallback if failure occurs.
     */
    @CircuitBreaker(name = CB_NAME, fallbackMethod = "paymentFallback")
    public boolean processPayment(String userId, BigDecimal amount) {
        // Simulate external payment gateway call
        // Replace this with WebClient/RestTemplate call to real payment API
        System.out.println("Processing payment for user " + userId + " amount: " + amount);

        // Simulate random failure (20% failure rate)
        if (Math.random() < 0.2) {
            throw new RuntimeException("Simulated remote payment failure");
        }

        return true; // Payment successful
    }

    /**
     * Fallback method when CircuitBreaker opens or remote call fails.
     */
    public boolean paymentFallback(String userId, BigDecimal amount, Throwable t) {
        System.err.println("Payment failed for user " + userId + ". Fallback triggered. Reason: " + t.getMessage());
        // Record for manual processing or mark order as pending payment
        return false;
    }
}
