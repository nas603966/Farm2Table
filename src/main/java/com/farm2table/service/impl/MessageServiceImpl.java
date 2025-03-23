package com.farm2table.service.impl;

import com.farm2table.model.Message;
import com.farm2table.repository.MessageRepository;
import com.farm2table.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<Message> getConversationMessages(Long conversationId) {
        return messageRepository.findByConversationIdOrderByTimestampAsc(conversationId);
    }

    @Override
    public Message sendMessage(Message message) {
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @Override
    public String getAIResponse(String userMessage) {
        // Simple mock AI response for now
        if (userMessage.toLowerCase().contains("price")) {
            return "Our prices are dynamically adjusted based on market trends. Let us know which product you're interested in.";
        }
        return "Thank you for your inquiry. We will get back to you soon.";
    }
}