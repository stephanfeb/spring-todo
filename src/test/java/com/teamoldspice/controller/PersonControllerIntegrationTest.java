package com.teamoldspice.controller;

import com.teamoldspice.TodoApplication;
import com.teamoldspice.model.Person;
import com.teamoldspice.repository.AuthorityRepository;
import com.teamoldspice.repository.PersonRepository;
import com.teamoldspice.service.PersonService;
import org.fluentlenium.adapter.FluentTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.assertj.FluentLeniumAssertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TodoApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:9000")
//@Transactional
public class PersonControllerIntegrationTest extends FluentTest{

    private FirefoxDriver webDriver = new FirefoxDriver();

    @Autowired
    PersonRepository personRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    PersonService personService;


    @Before
    public void setUp(){
    }

    @Override
    public WebDriver getDefaultDriver() {
        return webDriver;
    }

    @Test
    public void showLoginPage(){
        goTo("http://localhost:9000/login");

        assertTrue(find("input", withName("username")).size() == 1);
        assertTrue(find("input", withName("password")).size() == 1);
    }

    @Test
    @WithMockUser(username="anonymous", roles={"ANONYMOUS"})
    public void showLoginPage_WhenNotAuthenticated(){
        goTo("http://localhost:9000/person/list");

        assertTrue(find("input", withName("username")).size() == 1);
        assertTrue(find("input", withName("password")).size() == 1);
    }

    @Test
    @WithMockUser(username="test_user", roles = {"ROLE_USER"})
    public void showUserListWhenAuthenticated() throws Exception{
        //create a new user first
        Person user = new Person("test_user", "password");
        personService.createUser(user);

        //then login
        goTo("http://localhost:9000/login");
        fill("#username").with("test_user");
        fill("#password").with("password");

        List<Person> personSet = personRepository.findAll();
        //submit("#do_login");
        find("#do_login").click();
        goTo("http://localhost:9000/person/list") ;


        assertTrue(find("div", containingText("test_user")).size() > 0);

    }

}
