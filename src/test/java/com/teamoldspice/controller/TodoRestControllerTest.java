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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


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
    FilterChainProxy filterChainProxy;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    TodoRepository todoRepository;

    private MockMvc mockMvc;
    private MockHttpSession session;

    private ObjectMapper mapper;

    @Before
    public void setUp(){

        mockMvc = webAppContextSetup(wac).dispatchOptions(true).addFilters(filterChainProxy).build();

        TestSecurityContextHolder.clearContext();

        session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("test_user", "password", authorities);

        TestSecurityContextHolder.getContext().setAuthentication(authenticationToken);

        mapper = new ObjectMapper();
    }

    @Test
    public void testListTodo() throws Exception {
        mockMvc.perform(get("/rest/todo/list")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].name", is("buy milk")))
                .andExpect(jsonPath("$[0].completed", is(false)));
    }

    @Test
    public void testCreateTodo() throws Exception {
        mockMvc.perform(post("/rest/todo/create")
                .session(session)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(new Todo("hug my owl", false))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("hug my owl")))
                .andExpect(jsonPath("$.completed", is(false)));

    }

    @Test
    public void testUpdateFailsWhenNoTodo() throws Exception {
        mockMvc.perform(post("/rest/todo/update/10000000" )
                .session(session)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(new Todo())))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser("test_user")
    public void testUpdateTodo() throws Exception {
        Todo todo = todoRepository.findOne(1L);
        todo.setName("hug my owli");
        todo.setCompleted(true);
        mockMvc.perform(post("/rest/todo/update/" + todo.getId())
                .session(session)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("hug my owli")))
                .andExpect(jsonPath("$.completed", is(true)));

    }

    @Test
    public void testDeleteTodo() throws Exception {
        Todo todo = todoRepository.findOne(1L);
        mockMvc.perform(get("/rest/todo/delete/" + todo.getId())
                .session(session))
                .andExpect(status().isOk());

        todo = todoRepository.findOne(1L);

        assertThat(todo).isNull();
    }
}
