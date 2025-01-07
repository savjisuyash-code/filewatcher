package com.LogWatcher.demo;

import com.LogWatcher.demo.service.FileWatcherService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws IOException, InterruptedException {
//		FileWatcherService fileWatcherService = new FileWatcherService();
//		fileWatcherService.startWatching();
		System.out.println("Application starts");
		SpringApplication.run(DemoApplication.class, args);
	}

}
