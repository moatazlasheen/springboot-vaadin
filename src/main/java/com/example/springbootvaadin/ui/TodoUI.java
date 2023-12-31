package com.example.springbootvaadin.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.springbootvaadin.excel.ExcelGenerationService;
import com.example.springbootvaadin.model.Todo;
import com.example.springbootvaadin.pdf.PDFGenerationService;
import com.example.springbootvaadin.serverpush.Broadcaster;
import com.example.springbootvaadin.service.AttachementService;
import com.example.springbootvaadin.service.TodoService;
import com.itextpdf.text.DocumentException;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;

@Route("todos/:user")
public class TodoUI extends VerticalLayout implements BeforeEnterObserver, HasDynamicTitle{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private TodoService todoService;
	
	@Autowired
	private ExcelGenerationService excelGenerationService;
	
	@Autowired
	private PDFGenerationService pdfGenerationService;
	
	private String user; 
	
	private Grid<Todo> grid;
	
	private Registration broadcastRegisteration;
	
	@Autowired
	private AttachementService attachementService;
	
	private Div filesDiv = new Div();
	
	
	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		// application name
		createApplicationHeader();
		// create, delete and export (xls and pdf)
		createTodoButtons();
		// data table
		createTodosGrid();
		// select all and deselect all
		createGridItemsSelectionControls();
		// upload, list attachments
		createFilesControls();
		
		// handle server pushes
		final UI ui = attachEvent.getUI();
		broadcastRegisteration = Broadcaster.register(message -> 
			ui.access(() -> {
				refreshTodos();
				refreshFiles();
				Notification.show(message);
			})
		);
		
		
	}


	private void refreshFiles() {
		filesDiv.removeAll();
		filesDiv.add(createFilesListLayout());
	}


	private void createFilesControls() {
		// the upload component
		Upload fileUpload = createUploadComponent();
		
		// files list component
		VerticalLayout filesList = createFilesListLayout();
		filesDiv.add(filesList);
		add(fileUpload, filesDiv);
		
	}


	private VerticalLayout createFilesListLayout() {
		final VerticalLayout fileList = new VerticalLayout();
		attachementService.getAllFiles().stream().forEach(fileInfo -> {
			Anchor anchor = new Anchor(new StreamResource(fileInfo.getFileName(), () -> {
				try {
					return attachementService.getFileAsResource(fileInfo.getObjectId());
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}), null);
			anchor.setTarget("_blank");
			Button downloadButton = new Button(fileInfo.getFileName(), new Icon(VaadinIcon.DOWNLOAD_ALT));
			downloadButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
			downloadButton.getStyle().set("cursor", "pointer");
			anchor.add(downloadButton);
			Button deleteBtn = new Button(null, new Icon(VaadinIcon.FILE_REMOVE));
			deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
			deleteBtn.getStyle().set("cursor", "pointer");
			deleteBtn.addClickListener( event -> {
				attachementService.deleteFile(fileInfo.getObjectId());
				Broadcaster.broadcast("file " + fileInfo.getFileName() + " as deleted by " + user);
			});
			fileList.add(new HorizontalLayout(anchor, deleteBtn));
		});
		
		return fileList;
	}


	private void createGridItemsSelectionControls() {
		final HorizontalLayout horizontalLayout = new HorizontalLayout();
		final Button selectAllButton = new Button("Select All");
		selectAllButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		selectAllButton.setIcon(VaadinIcon.PLUS.create());
		selectAllButton.addClickListener(event-> grid.asMultiSelect().select(todoService.findAll()));
		
		final Button deSelectAllButton = new Button("Deselect All");
		deSelectAllButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		deSelectAllButton.setIcon(VaadinIcon.MINUS.create());
		deSelectAllButton.addClickListener(event-> grid.asMultiSelect().deselectAll());
		
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
		addTodoButton.addClickListener(event -> showAddTodoDialog());
		
		final Button deleteButton = new Button("Delete");
		deleteButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
		deleteButton.addClickListener(event -> {
			todoService.remove(grid.getSelectedItems());
			Broadcaster.broadcast("items were deleted by : " + user);
		});
		
		Anchor exportXlsxAnchor = new Anchor(new StreamResource("todos.xlsx", () -> {
			try {
				return excelGenerationService.generateExcel(grid.getSelectedItems());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}), null);
		Button exportXlsxButton = new Button("Export To Excel");
		exportXlsxButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
		exportXlsxAnchor.add(exportXlsxButton);
		
		Anchor exportPdfAnchor = new Anchor(new StreamResource("todos.pdf", () -> {
			try {
				return pdfGenerationService.generatePDF(grid.getSelectedItems());
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			} catch (DocumentException e) {
				throw new RuntimeException(e);
			}
		}), null);
		exportPdfAnchor.setTarget("_blank");
		Button exportPdfButton = new Button("Export To PDF");
		exportPdfButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
		exportPdfAnchor.add(exportPdfButton);
		
		buttonsLayout.add(addTodoButton, deleteButton, exportXlsxAnchor, exportPdfAnchor);
		add(buttonsLayout);
		
	}


	private Upload createUploadComponent() {
		MemoryBuffer memoryBuffer = new MemoryBuffer();
		Upload fileUpload = new Upload(memoryBuffer);
		fileUpload.addFinishedListener(event -> {
			final String fileName = event.getFileName();
			final InputStream is = memoryBuffer.getInputStream();
			attachementService.saveFile(fileName, is, event.getContentLength());
			Broadcaster.broadcast("New file " + fileName + " has been uploaded by " + user);
		});
		
		return fileUpload;
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
			todoService.addTodo(todo);
			dialog.close();
			Broadcaster.broadcast("New items were added by : " + user);
		});
		
		Button cancelButton = new Button("Cancel");
		cancelButton.addClickListener(event -> dialog.close());
		
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
		grid.setItems(todoService.findAll());
	}


	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		this.user = event.getRouteParameters().get("user").orElse("");
	}


	@Override
	public String getPageTitle() {
		return "Todos : " + user;
	}
	
	@Override
	protected void onDetach(DetachEvent detachEvent) {
		this.broadcastRegisteration.remove();
		this.broadcastRegisteration = null;
	}
	
}
