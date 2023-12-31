package com.example.springbootvaadin.repo;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;

import com.example.springbootvaadin.model.Todo;

@Profile("mongo")
public interface MongoTodoRepository extends CrudRepository<Todo, String>, TodoRepository{

}
