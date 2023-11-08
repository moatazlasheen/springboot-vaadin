package com.example.springbootvaadin.repo;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.example.springbootvaadin.model.Todo;

public interface TodoRepository extends Serializable {

	List<Todo> getAllTodos();

	void addTodo(Todo todo);

	void remove(Set<Todo> selectedItems);

}
