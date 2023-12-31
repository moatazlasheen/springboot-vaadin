package com.example.springbootvaadin.service;

import java.util.List;
import java.util.Set;

import com.example.springbootvaadin.model.Todo;

public interface TodoService {

	void remove(Set<Todo> todos);

	void addTodo(Todo todo);

	List<Todo> findAll();

}
