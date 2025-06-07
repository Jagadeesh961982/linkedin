package com.jagadeesh.linkedin.connections_service.auth;

public class UserContextHolder {

    private static final ThreadLocal<Long> currentUserId=new ThreadLocal<>();
    
    static void setCurrentUserId(Long userId) {
        currentUserId.set(userId);
    }
    public static Long getCurrentUserId() {
        return currentUserId.get();
    }
    static void clear() {
        currentUserId.remove();
    }
    public static boolean isUserLoggedIn() {
        return currentUserId.get() != null;
    }
    public static void removeCurrentUserId() {
        currentUserId.remove();
    }

}
