package com.farm2table.service;

import com.farm2table.model.Message;

import java.util.List;

public interface MessageService {

    List<Message> getConversationMessages(Long conversationId);

    Message sendMessage(Message message);

    String getAIResponse(String userMessage);
}