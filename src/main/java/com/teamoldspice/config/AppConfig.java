package com.teamoldspice.config;

import com.teamoldspice.service.CustomUserDetailsService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

import java.util.HashSet;

@Configuration
public class AppConfig implements InitializingBean {

    @Autowired
    SpringTemplateEngine templateEngine;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService);
//        return authenticationProvider;
//    }

//    @Bean
//    public AnonymousAuthenticationFilter anonymousAuthFilter(){
//        return new AnonymousAuthenticationFilter("todoAnon");
//    }
//
//    @Bean
//    public AnonymousAuthenticationProvider anonymousAuthenticationProvider(){
//        return new AnonymousAuthenticationProvider("todoAnon");
//    }


    @Override
    public void afterPropertiesSet() throws Exception {
        templateEngine.setAdditionalDialects(new HashSet<IDialect>() {{
            add(new SpringSecurityDialect());
        }});
    }
}
