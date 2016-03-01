package com.teamoldspice.controller;

import com.teamoldspice.model.Todo;
import com.teamoldspice.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@EnableAutoConfiguration
public class TodoController {

    @Autowired
    TodoRepository repository;

    @RequestMapping("/")
    public String hello(){

        ArrayList<String> todoStrings = new ArrayList<String>();

        for(Todo todo: repository.findAll()) {
            todoStrings.add(todo.getName());
        }

        return todoStrings.toString();
    }
}
