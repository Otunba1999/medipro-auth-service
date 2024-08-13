package com.otunba.medipro.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    ADMIN(Set.of("user:read", "user:write", "contact:read", "contact:write", "profile:read", "profile:write")),
    DOCTOR(Set.of("user:read", "appointment:read", "appointment:write")),
    NURSE(Set.of("user:read", "appointment:read")),
    PATIENT(Set.of("user:read"));
    private final Set<String> permissions;
    Role(Set<String> permissions) {
        this.permissions = permissions;
    }
    public Set<String> getPermission() {return permissions;}
    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> grantedAuthorities = getPermission().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + name()));
        return grantedAuthorities;
    }
}
