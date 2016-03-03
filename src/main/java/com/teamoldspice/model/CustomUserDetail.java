package com.teamoldspice.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.persistence.Id;
import java.util.Collection;

public class CustomUserDetail extends User{
    private User user;

    public CustomUserDetail(User user){
        super(user.getUsername(), user.getPassword(), user.getAuthorities());
        this.user = user;
    }

    public CustomUserDetail(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.user = new User(username, password, authorities);
    }


    @Override
    public String getUsername() {
        return user.getUsername();
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }


    public Boolean getEnabled() {
        return user.isEnabled();
    }

}
