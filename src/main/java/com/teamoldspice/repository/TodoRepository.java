package com.teamoldspice.repository;


import com.teamoldspice.model.Todo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TodoRepository extends CrudRepository<Todo, Long>{

    List<Todo> findByName(String name);
}
