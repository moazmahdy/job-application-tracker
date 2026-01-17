package com.elzozcode.job_tracker.utils;

import com.elzozcode.job_tracker.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserPrincipal) {
            return (UserPrincipal) principal;
        }

        throw new RuntimeException("Invalid principal type");
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getUserId();
    }

    public static Long getCurrentCompanyId() {
        UserPrincipal user = getCurrentUser();

        if (user.getCompanyId() == null) {
            throw new RuntimeException("Current user is not a company");
        }

        return user.getCompanyId();
    }

    public static String getCurrentUserEmail() {
        return getCurrentUser().getEmail();
    }

    public static boolean isCurrentUserCompany() {
        try {
            return getCurrentUser().isCompany();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isCurrentUserRegularUser() {
        try {
            return getCurrentUser().isUser();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserPrincipal;
    }
}