package com.example.springbootvaadin.ui;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.springbootvaadin.model.Todo;
import com.example.springbootvaadin.repo.TodoRepository;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.ThemeVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;

@Route("todos/:user")
public class TodoUI extends VerticalLayout implements BeforeEnterObserver, HasDynamicTitle{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private TodoRepository todoRepository;
	
	private String user; 
	
	Grid<Todo> grid;
	
	
	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		createApplicationHeader();
		createTodoButtons();
		createTodosGrid();
		createGridItemsSelectionControls();
		
	}


	private void createGridItemsSelectionControls() {
		final HorizontalLayout horizontalLayout = new HorizontalLayout();
		final Button selectAllButton = new Button("Select All");
		selectAllButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		selectAllButton.setIcon(VaadinIcon.PLUS.create());
		selectAllButton.addClickListener(event-> {
			grid.asMultiSelect().select(todoRepository.getAllTodos());
		});
		
		final Button deSelectAllButton = new Button("Deselect All");
		deSelectAllButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		deSelectAllButton.setIcon(VaadinIcon.MINUS.create());
		deSelectAllButton.addClickListener(event-> {
			grid.asMultiSelect().deselectAll();
		});
		
		horizontalLayout.add(selectAllButton, deSelectAllButton);
		add(horizontalLayout);
	}


	private void createApplicationHeader() {
		H2 title = new H2("Todos application : " + user.toUpperCase());
		add(title);
	}


	private void createTodoButtons() {
		final HorizontalLayout buttonsLayout = new HorizontalLayout();
		final Button addTodoButton = new Button("New Todo");
		addTodoButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_SUCCESS);
		addTodoButton.addClickListener(event -> {
			showAddTodoDialog();
		});
		
		final Button deleteButton = new Button("Delete");
		deleteButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
		deleteButton.addClickListener(event -> {
			todoRepository.remove(grid.getSelectedItems());
			refreshTodos();
		});
		
		buttonsLayout.add(addTodoButton, deleteButton);
		add(buttonsLayout);
	}


	private void showAddTodoDialog() {
		Dialog dialog = new Dialog();
		VerticalLayout dialogLayout = new VerticalLayout();
		dialog.add(dialogLayout);
		
		TextField todoName = new TextField("name");
		dialogLayout.add(todoName);
		
		
		Button saveButton = new Button("Save");
		saveButton.addClickListener(event -> {
			Todo todo = new Todo();
			todo.setName(todoName.getValue());
			todo.setCreationDate(LocalDateTime.now());
			todo.setCreator(user);
			todo.setDone(false);
			todoRepository.addTodo(todo);
			dialog.close();
			refreshTodos();
		});
		
		Button cancelButton = new Button("Cancel");
		cancelButton.addClickListener(event -> {
			dialog.close();
		});
		
		dialog.getFooter().add(saveButton, cancelButton);
		
		
		
		dialog.open();
	}


	private void createTodosGrid() {
		grid = new Grid<>();
		grid.addColumn(Todo::getName);
		grid.addColumn(Todo::getCreator);
		grid.addColumn(Todo::getCreationDate);
		grid.addComponentColumn(todo -> {
			Icon icon;
			if(todo.isDone()) {
				icon = VaadinIcon.CHECK.create();
				icon.setColor("green");
			} else {
				icon = VaadinIcon.CLOSE.create();
				icon.setColor("red");
			}
			return icon;
		});
		grid.setSelectionMode(SelectionMode.MULTI);
		
		grid.getColumns().get(0).setHeader("Name");
		grid.getColumns().get(1).setHeader("Creator");
		grid.getColumns().get(2).setHeader("Creation Date");
		grid.getColumns().get(3).setHeader("Done");
		
		add(grid);
		refreshTodos();
	}


	private void refreshTodos() {
		grid.setItems(todoRepository.getAllTodos());
	}


	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		this.user = event.getRouteParameters().get("user").orElse("");
	}


	@Override
	public String getPageTitle() {
		return "Todos : " + user;
	}
	
}
