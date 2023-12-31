package com.example.springbootvaadin.repo;

import java.util.List;

import com.example.springbootvaadin.model.Todo;

public interface TodoRepository {

	List<Todo> findAll();

	void deleteAll(Iterable<? extends Todo> todos);

	Todo save(Todo todo);

}
