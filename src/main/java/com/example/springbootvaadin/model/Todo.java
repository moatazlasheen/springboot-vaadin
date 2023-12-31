package com.example.springbootvaadin.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Todo implements Serializable {
	/**
	 * 
	 */
	@Id
	private String id;
	private static final long serialVersionUID = 1L;
	private String name;
	private boolean done;
	private LocalDateTime creationDate;
	private String creator;
	
	
}
