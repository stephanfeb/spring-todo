package com.teamoldspice.controller;

import com.teamoldspice.model.Todo;
import com.teamoldspice.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Controller
@EnableAutoConfiguration
public class TodoController {

    public static final String INDEX_VIEW = "index";

    @Autowired
    TodoRepository repository;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("todoList", repository.findAll());
        return INDEX_VIEW;
    }
}
