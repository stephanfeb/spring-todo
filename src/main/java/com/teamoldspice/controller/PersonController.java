package com.teamoldspice.controller;

import com.teamoldspice.model.Person;
import com.teamoldspice.model.SignupForm;
import com.teamoldspice.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@EnableAutoConfiguration
public class PersonController {
    @Autowired
    PersonService personService;

    @RequestMapping("/person/new")
    public String newUser(){
        return "/person/new";
    }

    @RequestMapping("/person/save")
    @Secured("ROLE_USER")
    public String addUser(@ModelAttribute Person newPerson) throws Exception{

        personService.createUser(newPerson);

        return "redirect:/person/list";
    }

    @RequestMapping("/person/list")
    @Secured("ROLE_USER")
    public String listUsers(Model model) throws Exception{

        List<Person> personList = personService.findAll();
        model.addAttribute("userList", personList);
        return "/person/list";
    }


    @RequestMapping("/login")
    @Secured("ROLE_USER")
    public String login(){
        return "login";
    }

    @RequestMapping("/signup")
    @Secured("hasRole('ROLE_ANONYMOUS') | hasRole('ROLE_USER')")
    public String showSignup(){
        return "signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    public String signup(@ModelAttribute SignupForm signupForm) throws Exception{

        //todo: 1) validated email, validate passwords
        personService.createUser(new Person(signupForm.getEmail(), signupForm.getPassword()));

       return "redirect:/login";
    }

}
