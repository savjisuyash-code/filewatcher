package com.LogWatcher.demo.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

@Service
public class FileWatcherService {

    @Autowired
    BroadCastService broadcastService;

    private static final Logger logger = LoggerFactory.getLogger(FileWatcherService.class);

    private final String logFileName = "logs.txt"; // Replace with your log file name
    long lastPos = 0;



    @PostConstruct
    public void startWatching() throws IOException, InterruptedException {
        String desktopPath = System.getProperty("user.home") + "/Desktop/alllogs/logs.txt";
        Path filePath = Paths.get(desktopPath);
        Path logDirectory = filePath.getParent();
        System.out.println("filepath " + filePath + " " + logDirectory);

        WatchService watchService = FileSystems.getDefault().newWatchService();

        logDirectory.register(watchService, ENTRY_MODIFY);

        System.out.println("Watching changes for the file!!");
        boolean poll = true;
        new Thread(() -> {
            while (true){
                WatchKey key = null;
                try {
                    key = watchService.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        printLastLines(Paths.get(String.valueOf(logDirectory), logFileName), 10);
                        broadcastService.broadCast(printLastLines(filePath, 10));
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    System.out.println("Watch key is no longer valid. Stopping watcher.");
                    break;
                }

            }
        }).start();

    }

    private List<String> printLastLines(Path filePath, int numLines) {
        List<String> list = new ArrayList<>();
        try {
            long totalLines = Files.lines(filePath).count();
            Files.lines(filePath)
                    .skip(Math.max(0, totalLines - numLines))
                    .forEach(list::add);
        } catch (IOException e) {
            logger.error("Error reading file: {}", e.getMessage());
        }
        return list;
    }
}

