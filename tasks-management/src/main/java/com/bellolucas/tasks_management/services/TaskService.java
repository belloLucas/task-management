package com.bellolucas.tasks_management.services;

import com.bellolucas.tasks_management.dto.task.CreateTaskDTO;
import com.bellolucas.tasks_management.dto.task.UpdateTaskDTO;
import com.bellolucas.tasks_management.entities.task.Task;
import com.bellolucas.tasks_management.entities.user.User;
import com.bellolucas.tasks_management.repositories.TaskRepository;
import com.bellolucas.tasks_management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public Task createTask(CreateTaskDTO data) {
        User assignee = userRepository.findById(data.assignee())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Task task = new Task();
        task.setTitle(data.title());
        task.setDescription(data.description());
        task.setPriority(data.priority());
        task.setAssignee(assignee);
        task.setDeadline(data.getDeadlineAsLocalDateTime());
        task.setCompleted(false);

        return taskRepository.save(task);
    }

    public Task updateTask(Long id, UpdateTaskDTO data) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada"));

        if (data.title() != null) {
            task.setTitle(data.title());
        }

        task.setDescription(data.description());

        if (data.priority() != null) {
            task.setPriority(data.priority());
        }

        if (data.assignee() != null) {
            User assignee = userRepository.findById(data.assignee())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
            task.setAssignee(assignee);
        }

        if (data.deadline() != null) {
            task.setDeadline(data.getDeadlineAsLocalDateTime());
        }

        if (data.completed() != null) {
            task.setCompleted(data.completed());
        }

        return taskRepository.save(task);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada"));
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAllByCompletedFalse();
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("Tarefa não encontrada");
        }
        taskRepository.deleteById(id);
    }
}
