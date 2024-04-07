package com.example.userservice.security.models;

import com.example.userservice.Models.Role;
import com.example.userservice.Models.User;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@JsonDeserialize

public class CustomUserDetails implements UserDetails {

  //private User user;
private String password;
private String username;
private boolean accountNonExpired;
private boolean accountNonLocked;
private boolean credentialsNonExpired;
private boolean enabled;
private List<CustomGrantedAuthority>authorities;
private Long userId;

public CustomUserDetails(){

}
    public CustomUserDetails(User user) {
        // this.user=user;
        this.username = user.getName();
        this.password = user.getHashPassword();
        this.accountNonExpired = true;
        this.credentialsNonExpired = true;
        this.accountNonLocked = true;
        this.enabled = true;

        List<CustomGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            CustomGrantedAuthority customGrantedAuthority = new CustomGrantedAuthority(role);
            authorities.add(customGrantedAuthority);
        }
        this.authorities=authorities;
        this.userId= user.getId();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public Long getUserId(){
    return userId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
