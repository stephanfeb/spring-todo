package com.teamoldspice.controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.teamoldspice.TodoApplication;
import org.fluentlenium.core.domain.FluentWebElement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.withName;
import static org.fluentlenium.core.filter.FilterConstructor.withText;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TodoApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:9000")
@DatabaseSetup("classpath:testData.xml")
@TestExecutionListeners(inheritListeners = false, listeners = { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
public class TodoControllerIntegrationTest extends FluentTestBase{


    @Test
    public void showLoginPage_WhenNotAuthenticated() {

    }

    @Test
    public void userCreateTodo() {
        login("test_user");

        goTo("http://localhost:9000/");

        find("#create_todo").click();

        assertThat(find("h2", withText().contains("Create a Todo"))).hasSize(1);

        fill("input#name").with("new todo");
        find("input#completed").click();

        find("input#submit").click();

        assertThat(find("h2", withText().contains("Todo App"))).hasSize(1);

        assertThat(find("td", withText().contains("new todo"))).hasSize(1);
        assertThat(find("td", withText().contains("true"))).hasSize(1);
    }

    @Test
    public void showUserTodosAfterLogin() {
        login("test_user");

        goTo("http://localhost:9000/");

        assertThat(find("td", withText().contains("false"))).hasSize(2);
    }

    @Test
    public void notShowOtherUserTodos() {
        login("test_user2");

        goTo("http://localhost:9000/");

        assertThat(find("td", withText().contains("false"))).hasSize(0);
    }

    @Test
    public void updateTodo() {
        login("test_user");

        goTo("http://localhost:9000/");

        assertThat(find("td", withText().contains("true"))).hasSize(0);
        findFirst("a", withName().equalTo("update")).click();

        find("input#completed").click();
        find("input#submit").click();

        assertThat(find("td", withText().contains("true"))).hasSize(1);

    }

    @Test
    public void deleteTodo() {
        login("test_user");

        goTo("http://localhost:9000/");

        assertThat(find("tr", withName().equalTo("todo"))).hasSize(2);
        findFirst("a", withName().equalTo("delete")).click();
        assertThat(find("tr", withName().equalTo("todo"))).hasSize(1);
    }
}
