package com.teamoldspice.repository;

import com.teamoldspice.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findOneByUsername(String username);

}
