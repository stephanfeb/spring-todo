package com.teamoldspice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.teamoldspice.TodoApplication;
import com.teamoldspice.repository.TodoRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TodoApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:9000")
@DatabaseSetup("classpath:testData.xml")
@TestExecutionListeners(inheritListeners = false, listeners = { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
public class PersonRestControllerTest extends RestControllerTestBase {

    @Autowired
    TodoRepository todoRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
        setupMockAndContext();
    }

    @After
    public void tearDown() {
        TestSecurityContextHolder.clearContext();
    }

    @Test
    public void testAddUser() throws Exception {
        login();
        Map personMap = new HashMap<>();
        personMap.put("username", "test_user_3@here.com");
        personMap.put("password", "password");
        mockMvc.perform(post("/rest/person/save")
                .session(session)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(personMap)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("test_user_3@here.com")))
                .andExpect(jsonPath("$").value(not(hasItem("password"))))
                .andExpect(jsonPath("$.enabled", is(true))
            );
    }

    @Test
    public void testListUsers() throws Exception {
        login();

        mockMvc.perform(get("/rest/person/list")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].username", is("test_user")))
                .andExpect(jsonPath("$[0].enabled", is(true)))
                .andExpect(jsonPath("$[1].username", is("test_user2")))
                .andExpect(jsonPath("$[1].enabled", is(true)));
    }

    @Test
    public void testSignup() throws Exception {
        Map personMap = new HashMap<>();
        personMap.put("username", "test_user_3@here.com");
        personMap.put("password", "password");
        mockMvc.perform(post("/rest/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(personMap)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("test_user_3@here.com")))
                .andExpect(jsonPath("$").value(not(hasItem("password"))))
                .andExpect(jsonPath("$.enabled", is(true))
                );
    }

    @Test
    public void testInvalidEmailGivesBadRequest() throws Exception {
        Map personMap = new HashMap<>();
        personMap.put("username", "test_user_3");
        personMap.put("password", "password");
        mockMvc.perform(post("/rest/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(personMap)))
                .andExpect(status().isBadRequest());
    }
}
