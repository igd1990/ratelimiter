package com.personal;

public interface RateLimiter {
    
    boolean tryAcquire(String userid);
}
