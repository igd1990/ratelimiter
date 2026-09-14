package com.personal;

import java.util.HashMap;
import java.util.Map;

public class FixedWindowRateLimiter implements RateLimiter{
    
    private  Map<String, UserState> windowState = new HashMap<>();

    private static final int LIMIT = 5;
    private static final long WINDOW_SIZE_MS = 10_000;


    @Override 
    public boolean tryAcquire(String userId) {
        
        long currentTime = System.currentTimeMillis();
        long currentWindowStart = currentTime - (currentTime % WINDOW_SIZE_MS); 

        // if the user has never made a request, the map will not have an entry for the user
        // create the user entry for the userId and allow the user
        if (!windowState.containsKey(userId)) {
            System.out.println("user " + userId + " does not exists, creating it");
            UserState state = new UserState();
            state.count = 1;
            state.windowStart = currentWindowStart;

            windowState.put(userId, state);
            return true;

        }
        
        // If the user's state belongs to an older window, reset it for the current window
        UserState state = windowState.get(userId);
        
        if (state.windowStart != currentWindowStart) {
            state.count = 1;
            state.windowStart = currentWindowStart;
            return  true;
        }

        // check if the user has already crossed limits in the current window, increase the counter
        // by 1 for every request
        if (state.count < LIMIT &&  (state.windowStart == currentWindowStart)) {
            state.count = state.count + 1;
            return  true;
        }
    
    System.out.println("User " + userId + " has already exceeded rate quota, try after sometime");
    return false;
    }
}
