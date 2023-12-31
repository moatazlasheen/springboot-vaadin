package com.example.springbootvaadin.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springbootvaadin.model.Todo;
import com.example.springbootvaadin.repo.TodoRepository;

@Service
public class TodoServiceImpl implements TodoService {
	
	@Autowired
	private TodoRepository todoRepository;

	@Override
	public void remove(Set<Todo> todos) {
		todoRepository.deleteAll(todos);
	}

	@Override
	public void addTodo(Todo todo) {
		todoRepository.save(todo);
	}

	@Override
	public List<Todo> findAll() {
		return todoRepository.findAll();
	}

}
