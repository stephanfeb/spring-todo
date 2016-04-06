package com.teamoldspice.config;

import com.teamoldspice.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
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
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    @EnableOAuth2Sso
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
                    .csrf()
                    .csrfTokenRepository(csrfTokenRepository()).and()
                    .addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
//                    .formLogin()
//                        .loginPage("/login")
//                        .permitAll()
//                        .and()
//                    .logout()
//                        .permitAll();

        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
//        auth.authenticationProvider(authenticationProvider);
        }

        private Filter csrfHeaderFilter() {
            return new OncePerRequestFilter() {
                @Override
                protected void doFilterInternal(HttpServletRequest request,
                                                HttpServletResponse response, FilterChain filterChain)
                        throws ServletException, IOException {
                    CsrfToken csrf = (CsrfToken) request
                            .getAttribute(CsrfToken.class.getName());
                    if (csrf != null) {
                        Cookie cookie = new Cookie("XSRF-TOKEN",
                                csrf.getToken());
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                    filterChain.doFilter(request, response);
                }
            };
        }

        private CsrfTokenRepository csrfTokenRepository() {
            HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
            repository.setHeaderName("X-XSRF-TOKEN");
            return repository;
        }

    }

}
