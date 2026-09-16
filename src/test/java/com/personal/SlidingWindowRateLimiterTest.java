package com.personal;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import java.lang.Thread;

public class SlidingWindowRateLimiterTest {
    
    @Test
    public void newUserShouldBeAllowed()
    {
        SlidingWindowRateLimiter limiter = new SlidingWindowRateLimiter();
        boolean result = limiter.tryAcquire("alice");
        assertTrue(result);
    }

    @Test 
    public void userShouldBeRejectedAfterExceedingLimit() throws InterruptedException
    {
        SlidingWindowRateLimiter limiter = new SlidingWindowRateLimiter();
        boolean result = limiter.tryAcquire("Boby"); //request 1
        assertTrue(result);

        boolean result1 = limiter.tryAcquire("Boby"); //request 1
        assertTrue(result1);

        Thread.sleep(2000);
        boolean result2 = limiter.tryAcquire("Boby"); //request 2
        assertTrue(result2);

        boolean result3 = limiter.tryAcquire("Boby"); //request 3
        assertTrue(result3);

        boolean result4 = limiter.tryAcquire("Boby"); //request 4
        assertTrue(result);
        
        Thread.sleep(10000);
        boolean result5 = limiter.tryAcquire("Boby"); //request 5
        assertTrue(result5);

        boolean result6 = limiter.tryAcquire("Boby"); //request 6
        assertTrue(result6);

    }
}
