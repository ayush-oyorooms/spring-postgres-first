package com.example.springpostgres.services;

import com.example.springpostgres.entities.TaskEntity;
import com.example.springpostgres.respositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

// Manager to take care of adding key to the repo if it doesn't exist in db and returning otherwise.
// This manager should be called by this service

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public TaskService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    // value: name with which the cache will be created
    @Cacheable(value = "allTasksCache")
    public List<TaskEntity> getAllTasks() {
        List<TaskEntity> allTasks = new ArrayList<>();
        taskRepository.findAll().forEach(allTasks::add);
        return allTasks;
    }

    @Cacheable(value = "TaskEntitiesWithIdMoreThan", key = "#taskId")
    public List<TaskEntity> getTasksWithIdMoreThan(Integer taskId) {
        return taskRepository.getTasksWithUserIdMoreThan(taskId);
    }

    @CacheEvict(value = "allTasksCache", allEntries = true)
    public TaskEntity addNewTask(TaskEntity taskEntity) {
        return taskRepository.save(taskEntity);
    }

    private Optional<TaskEntity> searchById(Integer taskId) {
        return taskRepository.findTaskById(taskId);
    }

    public ResponseEntity<Object> createResponseEntityIrrespectiveOfIdMatch(Integer taskId) {
        Optional<TaskEntity> ote = searchById(taskId);
        Map<String, Object> resMap = new HashMap<>();
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        if (ote.isPresent()) {
            TaskEntity retrieved = retrieveAndCacheTaskEntity(ote);
            resMap.put("captured_entity", retrieved);
            httpStatus = HttpStatus.OK;
        } else {
            resMap.put("status", 404);
            resMap.put("aeason", "aask with provided Id does not exist");
            resMap.put("reason", "Task with provided Id does not exist");
        }
        return new ResponseEntity<>(resMap, httpStatus);
    }

    @Cacheable(value = "optionalTaskEntity")
    // obvly can't convert RespEntty to json obj
    public TaskEntity retrieveAndCacheTaskEntity(Optional<TaskEntity> optionalTaskEntity) {
        System.out.println("about to cacheeeeee");
        TaskEntity te = optionalTaskEntity.get();
        return te;
    }

    @Cacheable(value = "-------------------------")
    public String bruhMethod(String word) {
        return anotherBruhMethod(word + "xxxxxxxxxxxxxxxx");
    }

    public String anotherBruhMethod(String word) {
        String nw = word + "____hiiii";
        return nw;
    }

    @CacheEvict(value = "taskId", key = "#taskId")
    public ResponseEntity<Object> deleteTaskById(Integer taskId) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        HttpStatus httpStatus = HttpStatus.OK;
        Optional<TaskEntity> taskEntity = taskRepository.findTaskById(taskId);
        System.out.println(taskEntity.isPresent());
        if (taskEntity.isPresent()) {
//            evictTaskFromRedis(taskId);
            taskRepository.deleteById(taskId);
            resMap.put("deletion_status", "success");
            resMap.put("deleted task with id", taskId);
        } else {
            // reason pehle aata json mein deletion_status se
            resMap.put("deletion_status", "Failed");
            resMap.put("reason", "Task with provided taskid not found");
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(resMap, httpStatus);
    }
    @CachePut(value = "taskId", key = "#taskEntity.getTaskId()")
    public TaskEntity updateTaskWithTaskEntity(TaskEntity taskEntityData, TaskEntity taskEntity) {
        if (taskEntity.getTitle() != null) {
            taskEntityData.setTitle(taskEntity.getTitle());
        }
        if (taskEntity.getDeadline() != null) {
            taskEntityData.setDeadline(taskEntity.getDeadline());
        }
        if (taskEntity.getDescription() != null) {
            taskEntityData.setDescription(taskEntity.getDescription());
        }
        return taskEntityData;
    }

    public ResponseEntity<Object> updateTaskWithId(Integer taskId, TaskEntity taskEntity) {
        ResponseEntity<Object> re = createResponseEntityIrrespectiveOfIdMatch(taskId);
        if (re.getStatusCode() == HttpStatus.OK) {
            HashMap<String, Object> linkedHashMap = (HashMap<String, Object>) re.getBody();
            TaskEntity foundTaskEntity = (TaskEntity) linkedHashMap.get("captured_entity");
            updateTaskWithTaskEntity(foundTaskEntity, taskEntity);
        }
        return re;
    }
}
