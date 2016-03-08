package com.teamoldspice.controller;

import com.teamoldspice.exception.ResourceNotFoundException;
import com.teamoldspice.model.Todo;
import com.teamoldspice.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@EnableAutoConfiguration
public class TodoController {

    public static final String INDEX_VIEW = "todo/index";
    public static final String CREATE_VIEW = "todo/create";

    @Autowired
    PersonService personService;

    @RequestMapping("/")
    @Secured("ROLE_USER")
    public String index(Model model) {

        model.addAttribute("todoList", personService.findAllTodos());
        return INDEX_VIEW;
    }

    @Secured("ROLE_USER")
    @RequestMapping(value="/todo", method= RequestMethod.GET)
    public String createForm(Model model) {

        model.addAttribute("todo", new Todo());
        return CREATE_VIEW;
    }

    @Secured("ROLE_USER")
    @RequestMapping(value="/todo", method= RequestMethod.POST)
    public String submitForm(@ModelAttribute Todo todo, Model model) {

        personService.saveTodo(todo);
        return "redirect:/";
    }

    @Secured("ROLE_USER")
    @RequestMapping(value="/todo/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {

        Todo todo = personService.findTodo(id);
        if (null == todo) throw new ResourceNotFoundException();

        model.addAttribute("todo", todo);
        return CREATE_VIEW;
    }

    @Secured("ROLE_USER")
    @RequestMapping(value="/todo/delete/{id}")
    public String delete(@PathVariable("id") Integer id){

        personService.deleteTodo(id);

        return "redirect:/";
    }
}
