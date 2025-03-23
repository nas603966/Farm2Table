package com.farm2table.repository;

import com.farm2table.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByParticipantOneIdAndParticipantTwoId(Long participantOneId, Long participantTwoId);
}
