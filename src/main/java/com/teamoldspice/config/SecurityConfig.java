package com.teamoldspice.config;

import com.teamoldspice.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

//    @Autowired
//    DaoAuthenticationProvider authenticationProvider;

//    @Configuration
//    @Order(1)
//    public static class RestSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http.authorizeRequests().antMatchers("/rest/**", "/rest/person/**")
//                    .authenticated().and()
//                    .httpBasic().realmName("TodoApp");
//        }
//    }



    @Configuration
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {


        @Autowired
        PasswordEncoder passwordEncoder;

        @Autowired
        CustomUserDetailsService userDetailsService;


        @Override
        protected  void configure(HttpSecurity security) throws  Exception {
            security.authorizeRequests()
                        .antMatchers( "/beans/**", "/css/**", "/js/**").permitAll()
                        .antMatchers("/signup/**").hasAnyRole("USER", "ANONYMOUS")
                        .antMatchers("/rest/signup").hasAnyRole("ANONYMOUS")
                        .anyRequest().authenticated()
                        .and()
                    .formLogin()
                        .loginPage("/login")
                        .permitAll()
                        .and()
                    .logout()
                        .permitAll();

        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
//        auth.authenticationProvider(authenticationProvider);
        }

    }

}
