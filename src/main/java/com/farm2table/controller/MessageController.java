package com.farm2table.controller;

import com.farm2table.model.Conversation;
import com.farm2table.model.Message;
import com.farm2table.service.ConversationService;
import com.farm2table.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ConversationService conversationService;

    @GetMapping("/conversation/{conversationId}")
    public List<Message> getConversationMessages(@PathVariable Long conversationId) {
        return messageService.getConversationMessages(conversationId);
    }

    @PostMapping("/send")
    public Message sendMessage(@RequestBody Message message) {
        Conversation conversation = conversationService.getOrCreateConversation(
                message.getSenderId(), message.getReceiverId());
        message.setConversationId(conversation.getId());
        return messageService.sendMessage(message);
    }

    @PostMapping("/ai-response")
    public String getAIResponse(@RequestBody String userMessage) {
        return messageService.getAIResponse(userMessage);
    }
}