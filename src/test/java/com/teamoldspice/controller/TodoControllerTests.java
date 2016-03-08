package com.teamoldspice.controller;


import com.teamoldspice.TodoApplication;
import com.teamoldspice.model.Person;
import com.teamoldspice.model.Todo;
import com.teamoldspice.repository.PersonRepository;
import com.teamoldspice.repository.TodoRepository;
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

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TodoApplication.class)
@WebAppConfiguration
@TestExecutionListeners(listeners={ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
@Transactional
@WithMockUser(username="test_user")
public class TodoControllerTests {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private MockMvc mockMvc;
    private Todo todo;
    private Person person;



    @Before
    public void setup() {
        mockMvc = webAppContextSetup(wac).build();
    }

    @Test
    public void testIndex_RendersIndexView_WithTodoListModel() throws Exception {
        Todo todo = createTodoWithTestUser();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("todo/index"))
                .andExpect(model().attribute("todoList", hasItem(todo)));
    }

    @Test
    public void testCreateForm_RendersCreateView_WithTodoModel() throws Exception {
        mockMvc.perform(get("/todo"))
                .andExpect(status().isOk())
                .andExpect(view().name("todo/create"))
                .andExpect(model().attribute("todo", new Todo()));
    }

    @Test
    public void testSubmitForm_PersistsATodo_AndRedirectsToIndex() throws Exception {
        mockMvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "some new task")
                        .param("completed", "false")
                        .sessionAttr("todo", new Todo()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));

        Todo createdTodo = todoRepository.findOneByName("some new task");
        assertEquals(false, createdTodo.getCompleted());
    }

    @Test
    public void testSubmitForm_HasError_WhenParamsAreMissing() throws Exception {
        mockMvc.perform(post("/todo")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr("todo", new Todo()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));

        Todo createdTodo = todoRepository.findOneByName("some new task");
        assertNull(createdTodo);
    }

    @Test
    public void testEdit_RendersCreateView_WithTodoModel() throws Exception {
        Todo todo = createTodoWithTestUser();

        mockMvc.perform(get("/todo/edit/" + todo.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("todo/create"))
                .andExpect(model().attribute("todo", todo));
    }

    @Test
    public void testEdit_HasErrorOnInvalidId() throws Exception {
        mockMvc.perform(get("/todo/edit/" + 1))
                .andExpect(status().isOk())
                .andExpect(view().name("todo/create"))
                .andExpect(model().attribute("todo", todo));

    }

    @Test
    public void testDelete_DestroysTodo_AndRedirectsToIndex() throws Exception {
        Todo todo = createTodoWithTestUser();

        mockMvc.perform(get("/todo/delete/" + todo.getId()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));

        assertEquals(0, todoRepository.count());
    }

    private Todo createTodoWithTestUser() {
        todo = new Todo("some task", false);
        todo = todoRepository.save(todo);

        person = new Person();
        person.setUsername("test_user");
        person.setPassword(passwordEncoder.encode("password"));
        person.setEnabled(true);

        person = personRepository.save(person);

        person.todos.add(todo);
        personRepository.save(person);

        return todo;
    }
}
