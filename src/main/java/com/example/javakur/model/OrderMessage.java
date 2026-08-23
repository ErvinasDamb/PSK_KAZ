package com.example.javakur.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_messages")
@Getter
@Setter
@NoArgsConstructor
public class OrderMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private FoodOrder order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "sent_at", nullable = false, updatable = false)
    private LocalDateTime sentAt;

    public OrderMessage(FoodOrder order, User sender, String content) {
        this.order = order;
        this.sender = sender;
        this.content = content;
        this.sentAt = LocalDateTime.now();
    }

    @PrePersist
    protected void ensureSentAt() {
        if (sentAt == null) sentAt = LocalDateTime.now();
    }
}
