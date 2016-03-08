package com.teamoldspice.service;

import com.teamoldspice.model.CustomUserDetail;
import com.teamoldspice.model.Person;
import com.teamoldspice.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PersonService personService;

    @Override
    public CustomUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        //This method gets called by the framework when a user logs in for the first time
        Optional<Person> person = personRepository.findOneByUsername(username);
        return person
                .map(p -> new CustomUserDetail(
                        p.getUsername(),
                        p.getPassword()/*raw password from frontend*/,
                        personService.getRoles(p))
                )
                .orElseThrow(() -> new UsernameNotFoundException(username + " can not be found"));
    }
}
