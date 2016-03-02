package com.teamoldspice.controller;

import com.teamoldspice.model.Todo;
import com.teamoldspice.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;

@Controller
@EnableAutoConfiguration
public class TodoController {

    public static final String INDEX_VIEW = "index";
    public static final String _VIEW = "index";

    @Autowired
    TodoRepository repository;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("todoList", repository.findAllByOrderByIdAsc());
        return INDEX_VIEW;
    }

    @RequestMapping(value="/todo", method= RequestMethod.GET)
    public String createForm(Model model) {
        model.addAttribute("todo", new Todo());
        return "create";
    }

    @RequestMapping(value="/todo", method= RequestMethod.POST)
    public String submitForm(@ModelAttribute Todo todo, Model model) {
        repository.save(todo);
        return index(model);
    }

    @RequestMapping(value="/todo/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        Todo todo = repository.findOne(id.longValue());
        model.addAttribute("todo", todo);
        return "create";
    }

    @RequestMapping(value="/todo/delete/{id}")
    public String delete(@PathVariable("id") Integer id){
        if(repository.exists(id.longValue()))
            repository.delete(id.longValue());

        return "redirect:/";
    }


}
