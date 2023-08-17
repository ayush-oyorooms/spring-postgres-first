package com.example.springpostgres.services;

import com.example.springpostgres.entities.TaskEntity;
import com.example.springpostgres.respositories.TaskRepository;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Manager to take care of adding key to the repo if it doesn't exist in db and returning otherwise.
// This manager should be called by this service

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    // value: name with which the cache will be created
    @Cacheable(value = "allTasksCache")
    public List<TaskEntity> getAllTasks() {
        List<TaskEntity> allTasks = new ArrayList<>();
        taskRepository.findAll().forEach(allTasks::add);
        return allTasks;
    }

    @Cacheable(value = "TaskEntity", key = "#taskId")
    public List<TaskEntity> getTasksWithIdMoreThan(Integer taskId) {
        return taskRepository.getTasksWithUserIdMoreThan(taskId);
    }

    public TaskEntity addNewTask(TaskEntity taskEntity) {
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^TaskEntity BEGIN^^^^^^^^^^^^^^^");
        System.out.println(taskEntity);
        System.out.println(taskEntity.getTitle());
        System.out.println(taskEntity.getDescription());
        System.out.println(taskEntity.getCreatedOn());
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^TaskEntity END^^^^^^^^^^^^^^^");
        System.out.println("____________________________---_taskEntity STRING--------------------------");
        TaskEntity res =  taskRepository.save(taskEntity);
        System.out.println(res.getTaskId());
        return res;
    }

    @Cacheable(value = "taskId")
    public Optional<TaskEntity> getTaskById(Integer taskId) {
        return taskRepository.findById(taskId);
    }

//    @CacheEvict(key = "#taskId")
    public void deleteTaskById(Integer taskId) {
        taskRepository.deleteById(taskId);
    }

    @CachePut(value = "taskId", key = "#taskEntity.getTaskId()")
    public TaskEntity updateTaskWithId(TaskEntity taskEntityData, TaskEntity taskEntity) {
        if (taskEntity.getTitle() != null) {
            taskEntityData.setTitle(taskEntity.getTitle());
        }
        if (taskEntity.getDeadline() != null) {
            taskEntityData.setDeadline(taskEntity.getDeadline());
        }
        if (taskEntity.getDescription() != null) {
            taskEntityData.setDescription(taskEntity.getDescription());
        }
//        callbackToReturnRespEntity(taskEntityData);
        return taskEntityData;
    }
    private ResponseEntity<TaskEntity> callbackToReturnRespEntity(TaskEntity taskEntity) {
        return new ResponseEntity<>(taskRepository.save(taskEntity), HttpStatus.OK);
    }
}
