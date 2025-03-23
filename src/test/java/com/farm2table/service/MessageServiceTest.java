package com.farm2table.service;

import com.farm2table.model.Message;
import com.farm2table.repository.MessageRepository;
import com.farm2table.service.impl.MessageServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    @Test
    void testSendMessage() {
        Message message = new Message(1L, 1L, 2L, 3L, "Hello!", LocalDateTime.now(), false);
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        Message result = messageService.sendMessage(message);

        assertEquals("Hello!", result.getContent());
        verify(messageRepository, times(1)).save(any(Message.class));
    }
}