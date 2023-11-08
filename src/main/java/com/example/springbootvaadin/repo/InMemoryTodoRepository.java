package com.example.springbootvaadin.repo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.example.springbootvaadin.model.Todo;

import jakarta.annotation.PostConstruct;


@Repository
public class InMemoryTodoRepository implements TodoRepository {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Todo> todos = new ArrayList<>();
	private Random random = new Random();
	
	
	@PostConstruct
	public void init() {
		for (int i = 1; i <= 20; i++) {
			Todo todo = new Todo();
			todo.setCreationDate(LocalDateTime.now());
			todo.setCreator("admin");
			if(random.nextBoolean()) {
				todo.setDone(true);
			}
			todo.setName("test Todo " + i);
			todos.add(todo);
		}
	}

	@Override
	public List<Todo> getAllTodos() {
		return this.todos;
	}

	@Override
	public void addTodo(Todo todo) {
		this.todos.add(todo);
	}

	@Override
	public void remove(Set<Todo> selectedItems) {
		for (Todo todo : selectedItems) {
			this.todos.remove(todo);
		}
	}

}
