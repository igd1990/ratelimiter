package com.personal;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

public class SlidingWindowRateLimiter implements RateLimiter{
    
    private  Map<String, RollingUserState> windowState = new HashMap<>();

    private static final int LIMIT = 5;
    private static final long WINDOW_SIZE_MS = 10_000;

    @Override 
    public boolean tryAcquire(String userId) {
        
        long currentTime = System.currentTimeMillis();
       
        // if the user has never made a request, the map will not have an entry for the user
        // create the user entry for the userId and allow the user
        if (!windowState.containsKey(userId)) {
            System.out.println("user " + userId + " does not exists, creating it");
            RollingUserState state = new RollingUserState();
            state.timeStamps =  new ArrayDeque<>();
            state.timeStamps.offer(currentTime);

            windowState.put(userId, state);
            return true;

        }
        
        // The cutofftime is the time of the current timestamp minus the window size
        RollingUserState state = windowState.get(userId);
        long cutofftime = currentTime - WINDOW_SIZE_MS;
        System.out.println("cutofftime:" + cutofftime); 


        // Remove the timestamps for the user that belongs to the previous window
        while (state.timeStamps.peek() != null && state.timeStamps.peek() <= cutofftime) {
            // This takes the element out of the queue permanently
            Long removedTime = state.timeStamps.poll(); 
            System.out.println("Evicted old timestamp: " + removedTime);
        }

       
        // if the try is allowed, add the timestamp to the queue
        if (state.timeStamps.size() < LIMIT) {
            System.out.println("Current timestamps: " + state.timeStamps);
            state.timeStamps.offer(currentTime);
            return  true;
        }
    
    System.out.println("User " + userId + " has already exceeded rate quota, try after sometime");
    return false;
    }
}
