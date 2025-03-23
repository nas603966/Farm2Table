package com.farm2table.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long participantOneId;

    @Column(nullable = false)
    private Long participantTwoId;

    @OneToMany(mappedBy = "conversationId", cascade = CascadeType.ALL)
    private List<Message> messages;

    @Column(nullable = false)
    private LocalDateTime lastUpdated;
}