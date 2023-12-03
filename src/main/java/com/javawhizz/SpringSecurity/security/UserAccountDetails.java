package com.javawhizz.SpringSecurity.security;

import com.javawhizz.SpringSecurity.auth.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class UserAccountDetails implements UserDetails {
    private String username;
    private String password;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    private Set<? extends GrantedAuthority> grantedAuthorities;

    public UserAccountDetails(UserAccount userAccount){
        this.username =
                userAccount.getUsername();
        this.password =
                userAccount.getPassword();
        this.isAccountNonExpired =
                userAccount.isAccountNonExpired();
        this.isAccountNonLocked =
                userAccount.isAccountNonLocked();
        this.isCredentialsNonExpired =
                userAccount.isCredentialsNonExpired();
        this.isEnabled =
                userAccount.isEnabled();
        this.grantedAuthorities = grantedAuthorities(userAccount);
    }

    private Set<SimpleGrantedAuthority> grantedAuthorities(UserAccount userAccount){
        return userAccount
                .getRoles()
                .stream()
                .map(Role::getGrantedAuthorities)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
