package com.teamoldspice.controller;

import com.teamoldspice.model.Person;
import com.teamoldspice.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@EnableAutoConfiguration
public class AuthenticationController {
    @Autowired
    PersonService personService;

    @RequestMapping("/login")
    @Secured("IS_AUTHENTICATED_ANONYMOUSLY")
    public String login(){
        return "login";
    }


    @RequestMapping
    public String newUser(){
        return "/person/new";
    }

    @RequestMapping("/person/save")
    @Secured("ROLE_USER")
    public String addUser(@ModelAttribute Person newPerson){

        personService.createUser(newPerson);

        return "redirect:/person/list";
    }

    @RequestMapping("/person/list")
    @Secured("ROLE_USER")
    public String listUsers(Model model){

        List<Person> personList = personService.findAll();
        model.addAttribute("userList", personList);
        return("/person/list");
    }
}
