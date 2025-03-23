package com.farm2table.service.impl;

import com.farm2table.model.Conversation;
import com.farm2table.repository.ConversationRepository;
import com.farm2table.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Override
    public Conversation getOrCreateConversation(Long participantOneId, Long participantTwoId) {
        Optional<Conversation> conversation = conversationRepository
                .findByParticipantOneIdAndParticipantTwoId(participantOneId, participantTwoId);

        if (conversation.isPresent()) {
            return conversation.get();
        } else {
            Conversation newConversation = new Conversation();
            newConversation.setParticipantOneId(participantOneId);
            newConversation.setParticipantTwoId(participantTwoId);
            newConversation.setLastUpdated(LocalDateTime.now());
            return conversationRepository.save(newConversation);
        }
    }
}