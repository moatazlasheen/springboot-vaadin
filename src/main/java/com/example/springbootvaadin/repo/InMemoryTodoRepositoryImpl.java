package com.example.springbootvaadin.repo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.example.springbootvaadin.model.Todo;

import jakarta.annotation.PostConstruct;


@Repository
@Profile("inMemory")
public class InMemoryTodoRepositoryImpl implements TodoRepository {
	
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
			todo.setId(String.valueOf(i));
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
	public List<Todo> findAll() {
		return this.todos;
	}

	@Override
	public void deleteAll(Iterable<? extends Todo> todos) {
		for (Todo todo : todos) {
			this.todos.remove(todo);
		}
	}

	@Override
	public Todo save(Todo todo) {
		this.todos.add(todo);
		return todo;
	}

}
