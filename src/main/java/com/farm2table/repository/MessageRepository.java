package com.farm2table.repository;

import com.farm2table.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByConversationIdOrderByTimestampAsc(Long conversationId);

    List<Message> findBySenderIdOrReceiverId(Long senderId, Long receiverId);
}
