package com.personal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import java.lang.Thread;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Test
    public void concurrentRequestsShouldRespectLimit() throws InterruptedException {

        SlidingWindowRateLimiter limiter =
                new SlidingWindowRateLimiter();

        ExecutorService executor =
                Executors.newFixedThreadPool(100);

        AtomicInteger allowed = new AtomicInteger(0);
        AtomicInteger rejected = new AtomicInteger(0);     
        
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                    boolean result = limiter.tryAcquire("alice");

                    if (result) {
                        allowed.incrementAndGet();
                    }
                    else {
                        rejected.incrementAndGet();
                    }
                });
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(5, allowed.get());
        assertEquals(95, rejected.get());
    }
}
