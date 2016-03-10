package com.teamoldspice.controller;

import com.teamoldspice.TodoApplication;
import com.teamoldspice.model.Authority;
import com.teamoldspice.model.Person;
import com.teamoldspice.model.SignupForm;
import com.teamoldspice.repository.AuthorityRepository;
import com.teamoldspice.repository.PersonRepository;
import com.teamoldspice.repository.TodoRepository;
import jdk.nashorn.internal.runtime.options.Option;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TodoApplication.class)
@WebAppConfiguration
@TestExecutionListeners(inheritListeners = false, listeners={
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
//@WithMockUser(username="test_user")
public class PersonControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    static final String TEST_USER =  "test_user_1";
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(wac).build();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testListUsers() throws Exception {

        mockMvc.perform(get("/person/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("/person/list"))
                .andExpect(model().attributeExists("userList"));

    }

    @Test
    @Transactional
    public void testAddUser() throws Exception {

        //find a way to add form params for post
        mockMvc.perform(post("/person/save")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("username", TEST_USER)
                    .param("password", "password")
                    .param("enabled", "true")
                    .sessionAttr("person", new Person())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/person/list"));


        //check person repository for user
        Optional<Person> optPerson = personRepository.findOneByUsername(TEST_USER);

        assertTrue(optPerson.isPresent());
        assertTrue(optPerson.get().getUsername().equals(TEST_USER));

    }

    @Test
    @Transactional
    public void testUserSignup() throws Exception {

        mockMvc.perform(post("/signup")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("email", TEST_USER)
                    .param("password", "password")
                    .param("password_2", "password")
                    .sessionAttr("signupForm", new SignupForm())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));

        Optional<Person> optPerson = personRepository.findOneByUsername(TEST_USER);

        assertTrue(optPerson.isPresent());
        assertTrue(optPerson.get().getUsername().equals(TEST_USER));
        assertTrue(optPerson.get().roles.contains(authorityRepository.findOneByAuthority("ROLE_USER").get()));
    }



}
