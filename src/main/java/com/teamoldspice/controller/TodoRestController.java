package com.teamoldspice.controller;

import com.teamoldspice.exception.ResourceNotFoundException;
import com.teamoldspice.model.Todo;
import com.teamoldspice.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class TodoRestController {

    public static final String INDEX_VIEW = "todo/index";
    public static final String CREATE_VIEW = "todo/create";

    @Autowired
    PersonService personService;

    @RequestMapping("/rest/todo/list")
    @Secured("ROLE_USER")
    public Set<Todo> listTodo(Model model) {
        return personService.findAllTodos();
    }

    @RequestMapping(value="/rest/todo/create", method= RequestMethod.POST)
    @Secured("ROLE_USER")
    public Todo createTodo(@RequestBody Todo todo, Model model) {
        return personService.saveTodo(todo);
    }


    @RequestMapping(value="/rest/todo/update/{id}", method= RequestMethod.POST)
    @Secured("ROLE_USER")
    public Todo updateTodo(@RequestBody Todo todo, @PathVariable("id") Integer id) {
        if (null == personService.findTodo(id)) throw new ResourceNotFoundException();

        return personService.saveTodo(todo);
    }


    @RequestMapping(value="/rest/todo/delete/{id}")
    @Secured("ROLE_USER")
    public String deleteTodo(@PathVariable("id") Integer id){

        personService.deleteTodo(id);

        return "[]";

    }
}
