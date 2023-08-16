package com.example.springpostgres.services;

import com.example.springpostgres.entities.TaskEntity;
import com.example.springpostgres.respositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Manager to take care of adding key to the repo if it doesn't exist in db and returning otherwise.
// This manager should be called by this service

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

//    public List<TaskEntity> getAllTasks() {
    @Cacheable(value = "taskCache", key = "#root.method.name")
    public List<TaskEntity> getAllTasks() {
        List<TaskEntity> allTasks = new ArrayList<>();
        taskRepository.findAll().forEach(allTasks::add);
        return allTasks;
    }

    @Cacheable(value = "TaskEntity", key = "#taskId")
    public List<TaskEntity> getTasksWithIdMoreThan(Integer taskId) {
        return taskRepository.getTasksWithUserIdMoreThan(taskId);
    }

    @Cacheable(value = "TaskEntity", key = "#taskEntity.taskId")
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

    public Optional<TaskEntity> getTaskById(Integer taskId) {
        return taskRepository.findById(taskId);
    }

    @CacheEvict(key = "#taskId")
    public void deleteTaskById(Integer taskId) {
        taskRepository.deleteById(taskId);
    }

    @CachePut()
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
