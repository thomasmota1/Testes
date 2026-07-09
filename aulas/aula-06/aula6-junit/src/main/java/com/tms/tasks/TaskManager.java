package com.tms.tasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaskManager {
    private final List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Tarefa não pode ser nula");
        }
        if (tasks.contains(task)) {
            throw new IllegalStateException("Tarefa já existe: " + task.getId());
        }
        tasks.add(task);
    }

    public boolean removeTask(String taskId) {
        return tasks.removeIf(task -> task.getId().equals(taskId));
    }

    public void markAsCompleted(String taskId) {
        Task task = findById(taskId);
        if (task != null) {
            task.markAsCompleted();
        }
    }

    public void markAllAsCompleted() {
        tasks.forEach(Task::markAsCompleted);
    }

    public Task findById(String taskId) {
        return tasks.stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst()
                .orElse(null);
    }

    public List<Task> filterByStatus(boolean completed) {
        return tasks.stream()
                .filter(task -> task.isCompleted() == completed)
                .toList();
    }

    public List<Task> getTasksSortedByCreationDate() {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getCreatedAt))
                .toList();
    }

    public List<Task> getAllTasks() {
        return List.copyOf(tasks);
    }

    public int getTaskCount() {
        return tasks.size();
    }

    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                Path.of(filename), StandardCharsets.UTF_8)) {
            for (Task task : tasks) {
                writer.write(task.getId() + ";" + task.getTitle() + ";"
                        + task.getDescription() + ";" + task.isCompleted() + ";"
                        + task.getCreatedAt() + ";"
                        + (task.getCompletedAt() == null ? "" : task.getCompletedAt()));
                writer.newLine();
            }
        }
    }

    public void loadFromFile(String filename) throws IOException {
        List<Task> loadedTasks = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(
                Path.of(filename), StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", -1);
                if (parts.length != 6) {
                    throw new IOException("Registro de tarefa inválido: " + line);
                }
                boolean completed = Boolean.parseBoolean(parts[3]);
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                LocalDateTime completedAt = parts[5].isEmpty()
                        ? null
                        : LocalDateTime.parse(parts[5]);
                loadedTasks.add(new Task(parts[0], parts[1], parts[2], completed,
                        createdAt, completedAt));
            }
        }
        tasks.clear();
        tasks.addAll(loadedTasks);
    }
}
