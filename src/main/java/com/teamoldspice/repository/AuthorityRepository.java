package com.teamoldspice.repository;

import com.teamoldspice.model.Authority;
import com.teamoldspice.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Optional<Authority> findOneByAuthority(String authority);

}
