package com.personal;

import java.util.HashMap;
import java.util.Map;

public class FixedWindowRateLimiter implements RateLimiter{
    
    Map<String, UserState> windowState = new HashMap<>();

    @Override 
    public boolean tryAcquire(String userId) {
        
        long milis = System.currentTimeMillis();

        if (!windowState.containsKey(userId)) {
            UserState state = new UserState();
            state.count = 1;
            state.windowStart = milis;

            windowState.put(userId, state);
            return true;

        }
    return false;
    }
}
