package com.teamoldspice.controller;

import com.teamoldspice.model.Person;
import com.teamoldspice.service.PersonService;
import com.teamoldspice.validator.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@EnableOAuth2Resource
public class PersonRestController {
    @Autowired
    PersonService personService;

    @RequestMapping(value = "/rest/person/save", method=RequestMethod.POST)
    public Person addUser(@RequestBody Map<String, String> personMap) throws Exception{

        return personService.createUser(new Person(personMap.get("username"), personMap.get("password")));
    }

    @RequestMapping("/rest/person/list")
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
