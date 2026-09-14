package com.personal;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class FixedWindowRateLimiterTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void newUserShouldBeAllowed()
    {
        FixedWindowRateLimiter limiter = new FixedWindowRateLimiter();
        boolean result = limiter.tryAcquire("alice");
        assertTrue(result);
    }

    @Test 
    public void userShouldBeRejectedAfterExceedingLimit() 
    {
        FixedWindowRateLimiter limiter = new FixedWindowRateLimiter();
        boolean result = limiter.tryAcquire("Boby"); //request 1
        assertTrue(result);

        boolean result1 = limiter.tryAcquire("Boby"); //request 1
        assertTrue(result1);

        boolean result2 = limiter.tryAcquire("Boby"); //request 2
        assertTrue(result2);

        boolean result3 = limiter.tryAcquire("Boby"); //request 3
        assertTrue(result3);

        boolean result4 = limiter.tryAcquire("Boby"); //request 4
        assertTrue(result);

        boolean result5 = limiter.tryAcquire("Boby"); //request 5
        assertTrue(result5);

        boolean result6 = limiter.tryAcquire("Boby"); //request 6
        assertTrue(result6);

    }
}
