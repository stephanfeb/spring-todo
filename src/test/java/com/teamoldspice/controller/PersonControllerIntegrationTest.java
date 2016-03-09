package com.teamoldspice.controller;

import com.gargoylesoftware.htmlunit.WebClient;
import com.teamoldspice.SecurityContextFactory;
import com.teamoldspice.TodoApplication;
import com.teamoldspice.model.CustomUserDetail;
import com.teamoldspice.model.Person;
import com.teamoldspice.repository.AuthorityRepository;
import com.teamoldspice.repository.PersonRepository;
import com.teamoldspice.service.CustomUserDetailsService;
import com.teamoldspice.service.PersonService;
import org.fluentlenium.adapter.FluentTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.IntegrationTestPropertiesListener;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.*;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextBeforeModesTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.test.web.servlet.setup.MockMvcConfigurer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.assertj.FluentLeniumAssertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TodoApplication.class)
@WebAppConfiguration
@Transactional
@IntegrationTest("server.port:9000")
public class PersonControllerIntegrationTest extends FluentTest{

    @Autowired
    PersonRepository personRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    PersonService personService;

    @Autowired
    WebApplicationContext wac;

    private HtmlUnitDriver webDriver;

    private WebClient webClient;

    public AuthorityRepository getAuthorityRepository() {
        return authorityRepository;
    }

    @Before
    public void setUp(){
//        webDriver = MockMvcHtmlUnitDriverBuilder.webAppContextSetup(wac, SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @After
    public void after() {
        SecurityContextHolder.clearContext();
    }

    public void login(){
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_THREADLOCAL);

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("test_user", "password", authorities);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Override
    public WebDriver getDefaultDriver() {
        return MockMvcHtmlUnitDriverBuilder.webAppContextSetup(wac, SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    public void showLoginPage(){
        goTo("http://localhost:9000/login");

        assertTrue(find("input", withName("username")).size() == 1);
        assertTrue(find("input", withName("password")).size() == 1);
        assertThat(find("input", withName("password"))).hasSize(1);
    }

    @Test
    public void showLoginPage_WhenNotAuthenticated(){
        SecurityContextHolder.getContext().setAuthentication(null);

        goTo("http://localhost:9000/person/list");

        assertTrue(find("input", withName("username")).size() == 1);
        assertTrue(find("input", withName("password")).size() == 1);
    }

    @Test
    @DirtiesContext
    public void showUserListWhenAuthenticated() throws Exception{
        login();

        goTo("http://localhost:9000/person/list"); ;

        assertTrue(find("div", containingText("test_user")).size() > 0);
    }

    @Test
    @DirtiesContext
    public void showUsernameOnUserProfilePage() {
        login();

        goTo("http://localhost:9000");

        assertTrue(true);
    }

    @Test
    public void showSignupPage() {
        goTo("http://localhost:9000/signup");

        assertTrue(true);
        assertThat(find("input#email")).hasSize(1);
        assertThat(find("input#password")).hasSize(1);
        assertThat(find("input#password_2")).hasSize(1);
    }
}
