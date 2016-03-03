package com.teamoldspice.repository;

import com.teamoldspice.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findOneByUsername(String username);

}
