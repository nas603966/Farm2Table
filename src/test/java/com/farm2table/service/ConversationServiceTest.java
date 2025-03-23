package com.farm2table.service;

import com.farm2table.model.Conversation;
import com.farm2table.repository.ConversationRepository;
import com.farm2table.service.impl.ConversationServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class ConversationServiceTest {

    @Mock
    private ConversationRepository conversationRepository;

    @InjectMocks
    private ConversationServiceImpl conversationService;

    @Test
    void testGetOrCreateConversation() {
        when(conversationRepository.findByParticipantOneIdAndParticipantTwoId(1L, 2L))
                .thenReturn(Optional.empty());

        Conversation conversation = conversationService.getOrCreateConversation(1L, 2L);

        assertNotNull(conversation);
        verify(conversationRepository, times(1)).save(any(Conversation.class));
    }
}