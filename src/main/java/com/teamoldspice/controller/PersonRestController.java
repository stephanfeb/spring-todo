package com.teamoldspice.controller;

import com.teamoldspice.model.Person;
import com.teamoldspice.model.SignupForm;
import com.teamoldspice.service.PersonService;
import com.teamoldspice.validator.PersonValidator;
import javassist.tools.web.BadHttpRequest;
import org.apache.catalina.connector.Response;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class PersonRestController {
    @Autowired
    PersonService personService;

    @RequestMapping(value = "/rest/person/save", method=RequestMethod.POST)
    @Secured("ROLE_USER")
    public Person addUser(@RequestBody Map<String, String> personMap) throws Exception{

        return personService.createUser(new Person(personMap.get("username"), personMap.get("password")));
    }

    @RequestMapping("/rest/person/list")
    @Secured("ROLE_USER")
    public List<Person> listUsers(){
        return personService.findAll();
    }

    // TODO: create rest login

    @RequestMapping(value = "/rest/signup", method = RequestMethod.POST)
    public ResponseEntity<Person> signup(@RequestBody Map<String, String> personMap) throws Exception{
        PersonValidator validator = new PersonValidator();
        Person person = new Person(personMap.get("username"), personMap.get("password"));
        BindingResult result = new BeanPropertyBindingResult(person, "Person");
        validator.validate(person, result);

        if (result.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(personService.createUser(person));

    }
}
