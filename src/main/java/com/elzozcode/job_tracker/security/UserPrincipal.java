package com.elzozcode.job_tracker.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class UserPrincipal implements UserDetails {

    private final Long userId;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Long companyId;

    public UserPrincipal(Long userId, String email, String role, Long companyId) {
        this.userId = userId;
        this.email = email;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
        this.companyId = companyId;
    }

    public boolean isCompany() {
        return authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_COMPANY"));
    }

    public boolean isUser() {
        return authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}