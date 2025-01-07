package com.LogWatcher.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class WebSocketController extends TextWebSocketHandler {
    static CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @GetMapping("/")
    public String sayHello() {
        return "Hello, World!";
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("Session added !" + session.getId());
        this.sessions.add(session);
        System.out.println(sessions.size());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("Removing session!");
        sessions.remove(session);
    }


    public void broadcast(String message) {
        System.out.println("message: " + message);
        System.out.println(this.sessions.size());
        sessions.forEach(session -> {
            try {
                System.out.println("sending msg to: " + session.getId());
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                System.err.println("Error sending message to client: " + e.getMessage());
                sessions.remove(session);
            }
        });
    }


}
