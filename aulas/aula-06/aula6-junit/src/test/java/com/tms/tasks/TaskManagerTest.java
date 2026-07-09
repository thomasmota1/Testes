package com.tms.tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskManagerTest {
    private TaskManager manager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        manager = new TaskManager();
        task1 = new Task("1", "Estudar JUnit", "Ler documentação");
        task2 = new Task("2", "Fazer exercícios", "Completar katas");
        task3 = new Task("3", "Revisar código", "Code review do PR");
    }

    @Test
    void testAddAndRemoveTask() {
        manager.addTask(task1);
        assertEquals(1, manager.getTaskCount());
        assertNotNull(manager.findById("1"));
        assertTrue(manager.removeTask("1"));
        assertFalse(manager.removeTask("1"));
    }

    @Test
    void testInvalidAndDuplicateTask() {
        assertThrows(IllegalArgumentException.class, () -> manager.addTask(null));
        manager.addTask(task1);
        assertThrows(IllegalStateException.class,
                () -> manager.addTask(new Task("1", "Duplicada", "")));
    }

    @Test
    void testMarkAndFilterTasks() {
        manager.addTask(task1);
        manager.addTask(task2);
        manager.markAsCompleted("1");
        assertEquals(List.of(task1), manager.filterByStatus(true));
        assertEquals(List.of(task2), manager.filterByStatus(false));
    }

    @Test
    void testMarkAllAsCompleted() {
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.markAllAsCompleted();
        assertTrue(manager.getAllTasks().stream().allMatch(Task::isCompleted));
    }

    @Test
    void testSortByCreationDate() throws InterruptedException {
        Task first = new Task("1", "Primeira", "");
        Thread.sleep(10);
        Task second = new Task("2", "Segunda", "");
        Thread.sleep(10);
        Task third = new Task("3", "Terceira", "");

        manager.addTask(third);
        manager.addTask(first);
        manager.addTask(second);

        assertEquals(List.of("1", "2", "3"), manager.getTasksSortedByCreationDate()
                .stream().map(Task::getId).toList());
    }

    @Test
    void testSaveAndLoad() throws IOException {
        Path tempFile = Files.createTempFile("tasks", ".txt");
        try {
            manager.addTask(task1);
            manager.addTask(task2);
            task1.markAsCompleted();
            manager.saveToFile(tempFile.toString());

            TaskManager loadedManager = new TaskManager();
            loadedManager.loadFromFile(tempFile.toString());
            Task loadedTask1 = loadedManager.findById("1");

            assertEquals(2, loadedManager.getTaskCount());
            assertTrue(loadedTask1.isCompleted());
            assertFalse(loadedManager.findById("2").isCompleted());
            assertEquals(task1.getTitle(), loadedTask1.getTitle());
            assertEquals(task1.getDescription(), loadedTask1.getDescription());
            assertEquals(task1.getCreatedAt(), loadedTask1.getCreatedAt());
            assertEquals(task1.getCompletedAt(), loadedTask1.getCompletedAt());
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }
}
