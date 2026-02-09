package ru.practicum.shareit.request;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "item_requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor_id", nullable = false)
    private User requestor;
    @Column(nullable = false)
    private LocalDateTime created;
}
