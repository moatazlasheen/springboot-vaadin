package com.example.springbootvaadin;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.springbootvaadin.service.TodoService;

@SpringBootTest
class SpringbootVaadinApplicationTests {
	
	@Autowired
	private TodoService todoService;

	@Test
	void contextLoads() {
		assertThat(todoService).isNotNull();
	}

}
