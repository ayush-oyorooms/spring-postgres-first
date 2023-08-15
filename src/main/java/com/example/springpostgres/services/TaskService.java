package com.example.springpostgres.services;

import com.example.springpostgres.entities.TaskEntity;
import com.example.springpostgres.respositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<TaskEntity> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<TaskEntity> getTasksWithIdMoreThan(Integer userId) {
        return taskRepository.getTasksWithUserIdMoreThan(userId);
    }

    public TaskEntity addNewTask(TaskEntity taskEntity) {
        System.out.println(taskEntity.getTitle());
        System.out.println(taskEntity.getDescription());
        System.out.println(taskEntity.getCreatedOn());
        TaskEntity _taskEntity;
        if (taskEntity.getIsCompleted() != null) {
            _taskEntity = new TaskEntity(taskEntity.getTitle(), taskEntity.getDescription(),
                    taskEntity.getCreatedOn(), taskEntity.getIsCompleted());
        } else {
            _taskEntity = new TaskEntity(taskEntity.getTitle(), taskEntity.getDescription(),
                    taskEntity.getCreatedOn());
        }
        return taskRepository.save(_taskEntity);
    }

    public Optional<TaskEntity> getTaskById(Integer userId) {
        return taskRepository.findById(userId);
    }

    public void deleteTaskById(Integer userId) {
        taskRepository.deleteById(userId);
    }

    public ResponseEntity<TaskEntity> updateTaskWithId(TaskEntity taskEntityData, TaskEntity taskEntity) {
        if (taskEntity.getTitle() != null) {
            taskEntityData.setTitle(taskEntity.getTitle());
        }
        if (taskEntity.getDeadline() != null) {
            taskEntityData.setDeadline(taskEntity.getDeadline());
        }
        if (taskEntity.getDescription() != null) {
            taskEntityData.setDescription(taskEntity.getDescription());
        }
        return new ResponseEntity<>(taskRepository.save(taskEntityData), HttpStatus.OK);
    }
}
