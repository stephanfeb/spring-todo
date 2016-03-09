package com.teamoldspice;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.security.test.context.support.WithUserDetails;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class SecurityContextFactory implements WithSecurityContextFactory<WithUserDetails> {
    @Override
    public SecurityContext createSecurityContext(WithUserDetails withUserDetails) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(withUserDetails.value(), "password", authorities);

        //SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authenticationToken);
        return securityContext;
    }

}
