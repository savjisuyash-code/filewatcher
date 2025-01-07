package com.LogWatcher.demo.service;

import com.LogWatcher.demo.controller.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

@Service
public class BroadCastService {
    @Autowired
    WebSocketController webSocketController;

    public void broadCast(List<String> items){
       items.forEach(n -> webSocketController.broadcast(n));
    }

}
