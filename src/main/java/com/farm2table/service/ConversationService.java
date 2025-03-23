package com.farm2table.service;

import com.farm2table.model.Conversation;

import java.util.Optional;

public interface ConversationService {

    Conversation getOrCreateConversation(Long participantOneId, Long participantTwoId);
}