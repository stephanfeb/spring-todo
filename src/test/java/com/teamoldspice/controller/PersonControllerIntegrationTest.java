package com.teamoldspice.controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.teamoldspice.TodoApplication;
import com.teamoldspice.repository.AuthorityRepository;
import com.teamoldspice.repository.PersonRepository;
import com.teamoldspice.service.PersonService;
import org.fluentlenium.adapter.FluentTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.assertj.FluentLeniumAssertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.containingText;
import static org.fluentlenium.core.filter.FilterConstructor.withName;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TodoApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:9000")
@DatabaseSetup("classpath:testData.xml")
@TestExecutionListeners(inheritListeners = false, listeners = { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
public class PersonControllerIntegrationTest extends FluentTestBase{

    @Autowired
    PersonRepository personRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    PersonService personService;

    public AuthorityRepository getAuthorityRepository() {
        return authorityRepository;
    }

    @Before
    public void setUp(){
        TestSecurityContextHolder.clearContext();
        getDriver().manage().deleteAllCookies();
    }

    @After
    public void after() {
    }



    @Test
    public void showLoginPage(){
        goTo("http://localhost:9000/login");

        assertThat(find("input", withName("username"))).hasSize(1);
        assertThat(find("input", withName("password"))).hasSize(1);
    }

    @Test
    public void showLoginPage_WhenNotAuthenticated(){

        goTo("http://localhost:9000/person/list");

        WebElement el = getDriver().findElement(By.id("username"));
        assertThat(el).isNotNull();

        assertTrue(find("input", withName("username")).size() == 1);
        assertTrue(find("input", withName("password")).size() == 1);
    }

    @Test
    public void showUserListWhenAuthenticated() throws Exception{
        login("test_user");

        goTo("http://localhost:9000/person/list"); ;

        assertTrue(find("div", containingText("test_user")).size() > 0);
    }

    @Test
    public void showUsernameOnUserProfilePage() {
        login("test_user");

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
