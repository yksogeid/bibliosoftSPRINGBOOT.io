package com.registro.usuarios.modelo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private String fullName;

    // Constructor
    public CustomUserDetails(String email, String password, Collection<? extends GrantedAuthority> authorities, String fullName) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.fullName = fullName;
    }

    // Métodos de la interfaz UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
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

    public String getFullName() {
        return fullName;
    }
}
