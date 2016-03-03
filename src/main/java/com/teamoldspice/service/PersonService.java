package com.teamoldspice.service;

import com.teamoldspice.model.Authority;
import com.teamoldspice.model.CustomUserDetail;
import com.teamoldspice.model.Person;
import com.teamoldspice.model.Todo;
import com.teamoldspice.repository.TodoRepository;
import com.teamoldspice.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PersonService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    PersonRepository personRepository;

    /*
    * Retrieve the currently authenticated user
    * */
    private Person getAuthUser(){

        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return personRepository.findOneByUsername(userDetail.getUsername());
    }

    public void createUser(Person newPerson){

        Person existingPerson = personRepository.findOneByUsername(newPerson.getUsername());

        if (null == existingPerson){
            //create a new user account
            newPerson.setPassword(passwordEncoder.encode(newPerson.getPassword()));
            personRepository.save(newPerson);
        }else{
            //update an existing user account
            newPerson.setId(existingPerson.getId());
            newPerson.setPassword(passwordEncoder.encode(existingPerson.getPassword()));
            personRepository.save(newPerson);
        }
    }

    public List<GrantedAuthority> getRoles(Person person){
       Set<Authority> authorities = person.roles;
       ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        for(Authority role: authorities){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }

        return grantedAuthorities;
    }

    public List<Person> findAll(){
        return personRepository.findAll();
    }

    /*Returns todos for currently authenticated user*/
    public Set<Todo> findAllTodos(){
        return getAuthUser().todos;
    }

    public void saveTodo(Todo todo){
        todoRepository.save(todo);

        Person person = getAuthUser();
        person.todos.add(todo);
        personRepository.save(person);
    }

    public Todo findTodo(Integer id){
        return todoRepository.findOne(id.longValue());
    }

    public void deleteTodo(Integer id){
        todoRepository.delete(id.longValue());
    }
}
