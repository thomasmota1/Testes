package com.tms.tasks;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private final String id;
    private String title;
    private String description;
    private boolean completed;
    private final LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public Task(String id, String title, String description) {
        this(id, title, description, false, LocalDateTime.now(), null);
    }

    public Task(String id, String title, String description, boolean completed,
                LocalDateTime createdAt, LocalDateTime completedAt) {
        this.id = Objects.requireNonNull(id, "ID não pode ser nulo");
        this.title = Objects.requireNonNull(title, "Título não pode ser nulo");
        this.description = Objects.requireNonNull(description,
                "Descrição não pode ser nula");
        this.completed = completed;
        this.createdAt = Objects.requireNonNull(createdAt,
                "Data de criação não pode ser nula");
        this.completedAt = completedAt;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setTitle(String title) {
        this.title = Objects.requireNonNull(title, "Título não pode ser nulo");
    }

    public void setDescription(String description) {
        this.description = Objects.requireNonNull(description,
                "Descrição não pode ser nula");
    }

    public void markAsCompleted() {
        completed = true;
        completedAt = LocalDateTime.now();
    }

    public void markAsIncomplete() {
        completed = false;
        completedAt = null;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Task task)) {
            return false;
        }
        return id.equals(task.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
