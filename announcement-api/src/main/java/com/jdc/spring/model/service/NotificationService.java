package com.jdc.spring.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
	
	@Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyUsers(String message) {
        messagingTemplate.convertAndSend("/topic/announcements", message);
    }

}
