package com.bellolucas.tasks_management.entities.task;

import com.bellolucas.tasks_management.dto.task.CreateTaskDTO;
import com.bellolucas.tasks_management.entities.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "assignee", referencedColumnName = "id")
    private User assignee;

    private LocalDateTime deadline;
    private Boolean completed;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Task(CreateTaskDTO data, User assignee) {
        this.title = data.title();
        this.description = data.description();
        this.priority = data.priority();
        this.assignee = assignee;
        this.deadline = data.getDeadlineAsLocalDateTime();
        this.completed = false;
    }

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
