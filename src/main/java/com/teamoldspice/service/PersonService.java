package com.teamoldspice.service;

import com.teamoldspice.model.Authority;
import com.teamoldspice.model.CustomUserDetail;
import com.teamoldspice.model.Person;
import com.teamoldspice.model.Todo;
import com.teamoldspice.repository.AuthorityRepository;
import com.teamoldspice.repository.TodoRepository;
import com.teamoldspice.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import sun.security.util.AuthResources;

import java.util.*;

@Service
public class PersonService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    PersonRepository personRepository;

    /*
    * Retrieve the currently authenticated user
    * */
    private Optional<Person> getAuthUser(){

        Authentication authObj = SecurityContextHolder.getContext().getAuthentication();

        if (null == authObj) return Optional.empty();

        User userDetail = (User) authObj.getPrincipal();

        return personRepository.findOneByUsername(userDetail.getUsername());
    }

    public void createUser(Person newPerson){

        Optional<Person> existingPerson = personRepository.findOneByUsername(newPerson.getUsername());

        if (!existingPerson.isPresent()){

            //create a new user account
            newPerson.setPassword(passwordEncoder.encode(newPerson.getPassword()));
            Authority auth = authorityRepository.findOneByAuthority("ROLE_USER").get();
            newPerson.roles.add(auth);
            Person savedPerson = personRepository.saveAndFlush(newPerson);

        }else{

            //update an existing user account
            newPerson.setId(existingPerson.get().getId());
            newPerson.setPassword(passwordEncoder.encode(existingPerson.get().getPassword()));
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
        return getAuthUser()
                .map(person -> person.todos)
                .orElseGet(HashSet<Todo>::new);
    }

    public void saveTodo(Todo todo){
        todoRepository.save(todo);

        Optional<Person> optPerson= getAuthUser();
        optPerson.map(person -> {
            person.todos.add(todo);
            personRepository.save(person);
            return person;
        });
    }

    public Todo findTodo(Integer id){
        return todoRepository.findOne(id.longValue());
    }

    public void deleteTodo(Integer id){
        todoRepository.delete(id.longValue());
    }
}
