package com.teamoldspice.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
public class ResourceServer extends ResourceServerConfigurerAdapter{
    @Override
    public void configure(HttpSecurity http) throws Exception {
//        http.requestMatchers().antMatchers("/rest/**", "/rest/person/**")
//                .and().authorizeRequests()
//                .anyRequest().access("#oauth2.hasScope('read')");

        http.authorizeRequests().antMatchers("/rest/**", "/rest/person/**")
                .authenticated().and()
                .httpBasic().realmName("TodoApp");
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception{
        resources.resourceId("todos");
    }
}
