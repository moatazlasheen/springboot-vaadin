package com.example.springbootvaadin.serverpush;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import com.vaadin.flow.shared.Registration;

public class Broadcaster {
	
	private static List<Consumer<String>> consumers = new LinkedList<>();
	private static Executor executor = Executors.newSingleThreadExecutor();
	
	public static synchronized Registration register(final Consumer<String> consumer) {
		consumers.add(consumer);
		
		return () -> {
			synchronized(Broadcaster.class) {
				consumers.remove(consumer);
			}
		};
	}
	
	public static synchronized void broadcast(String message) {
		for (Consumer<String> consumer : consumers) {
			executor.execute(() -> consumer.accept(message));
		}
	}

}
