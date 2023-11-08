package com.example.springbootvaadin.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {
	private String name;
	private boolean done;
	private LocalDateTime creationDate;
	private String creator;
	
	
}
