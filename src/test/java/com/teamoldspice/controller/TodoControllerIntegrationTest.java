package com.teamoldspice.controller;

import com.teamoldspice.TodoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by neo on 9/3/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TodoApplication.class)
@WebAppConfiguration
@Transactional
@IntegrationTest("server.port:9000")
public class TodoControllerIntegrationTest {


    @Test
    public void showLoginPage_WhenNotAuthenticated() {

    }

    @Test
    public void userCreateTodo() {
        // login user
        // click todo
        // fill up create todo form
        // click create
        // comes back to index page
        // show newly created todo
    }
}
