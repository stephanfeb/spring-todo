package com.teamoldspice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.Arrays;

/**
 * Created by neo on 15/3/16.
 */
@Configuration
//@EnableOAuth2Client
public class ResourceConfiguration {

//
//    @Bean
//    public OAuth2ProtectedResourceDetails todoAuthServerResourceDetails() {
//        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
//        details.setId("auth/server");
//        details.setClientId("todo");
//        details.setClientSecret("todopassword");
//        details.setAccessTokenUri("http://localhost:9999/uaa/oauth/token");
//        details.setUserAuthorizationUri("http://localhost:9999/uaa/oauth/authorize");
//        details.setScope(Arrays.asList("openid"));
//
//        return details;
//    }

//    @Bean
//    public OAuth2RestTemplate todoAuthRestTemplate(OAuth2ClientContext clientContext) {
//        return new OAuth2RestTemplate(todoAuthServerResourceDetails(), clientContext);
//    }
//
//    @Bean public OAuth2RestOperations restTemplate() {
//        AccessTokenRequest atr = new DefaultAccessTokenRequest();
//
//        return new OAuth2RestTemplate(todoAuthServerResourceDetails(), new DefaultOAuth2ClientContext(atr));
//    }

}
