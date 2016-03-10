package com.teamoldspice.controller;

import org.fluentlenium.adapter.FluentTest;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

public abstract class FluentTestBase extends FluentTest {

    @Autowired
    WebApplicationContext wac;

    @Override
    public WebDriver getDefaultDriver() {
        return MockMvcHtmlUnitDriverBuilder.webAppContextSetup(wac, SecurityMockMvcConfigurers.springSecurity()).build();
    }

    public void login(String username){

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, "password", authorities);

        TestSecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
