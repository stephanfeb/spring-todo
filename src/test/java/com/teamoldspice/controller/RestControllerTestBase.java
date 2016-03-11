package com.teamoldspice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by neo on 11/3/16.
 */
public abstract class RestControllerTestBase {
    @Autowired
    FilterChainProxy filterChainProxy;

    @Autowired
    WebApplicationContext wac;

    protected MockMvc mockMvc;
    protected MockHttpSession session;

    public void setupMockAndContext() {
        mockMvc = webAppContextSetup(wac).dispatchOptions(true).addFilters(filterChainProxy).build();
    }

    public void login(){

        TestSecurityContextHolder.clearContext();

        session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("test_user", "password", authorities);

        TestSecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

}
