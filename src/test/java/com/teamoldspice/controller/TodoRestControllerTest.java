package com.teamoldspice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.teamoldspice.TodoApplication;
import com.teamoldspice.model.Todo;
import com.teamoldspice.repository.TodoRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TodoApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:9000")
@DatabaseSetup("classpath:testData.xml")
@TestExecutionListeners(inheritListeners = false, listeners = { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
public class TodoRestControllerTest{

    @Autowired
    WebApplicationContext wac;

    @Autowired
    TodoRepository todoRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp(){

        mockMvc = webAppContextSetup(wac).build();

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("test_user", "password", authorities);

        TestSecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Test
    public void testListTodo() throws Exception {
        mockMvc.perform(get("/rest/todo/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].name", is("buy milk")))
                .andExpect(jsonPath("$[0].completed", is(false)));
    }

    @Test
    public void testCreateTodo() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(post("/rest/todo/create")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(new Todo("hug my owl", false))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("hug my owl")))
                .andExpect(jsonPath("$.completed", is(false)));

    }

    @Test
    public void testUpdateFailsWhenNoTodo() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(post("/rest/todo/update/10000000" )
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(new Todo())))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateTodo() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Todo todo = todoRepository.findOne(1L);
        todo.setName("hug my owli");
        todo.setCompleted(true);
        mockMvc.perform(post("/rest/todo/update/" + todo.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("hug my owli")))
                .andExpect(jsonPath("$.completed", is(true)));

    }

    @Test
    public void testDeleteTodo() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Todo todo = todoRepository.findOne(1L);
        mockMvc.perform(get("/rest/todo/delete/" + todo.getId()))
                .andExpect(status().isOk());

        todo = todoRepository.findOne(1L);

        assertThat(todo).isNull();
    }
}
