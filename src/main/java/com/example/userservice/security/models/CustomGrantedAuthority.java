package com.example.userservice.security.models;

import com.example.userservice.Models.Role;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.security.core.GrantedAuthority;
@JsonDeserialize
public class CustomGrantedAuthority implements GrantedAuthority {
    private String authority;
    //private Role role;
    public CustomGrantedAuthority(){

    }
    public CustomGrantedAuthority(Role role){
        // this.role=role;
        this.authority=role.getName();
    }
    @Override
    public String getAuthority() {
        return authority;
    }
}