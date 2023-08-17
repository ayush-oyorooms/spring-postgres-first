package com.example.springpostgres.controllers;

import com.example.springpostgres.entities.TaskEntity;
import com.example.springpostgres.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.redisson.Redisson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.json.RedisJsonCommands;
import redis.clients.jedis.search.FTCreateParams;
import redis.clients.jedis.search.IndexDataType;
import redis.clients.jedis.search.schemafields.NumericField;
import redis.clients.jedis.search.schemafields.TextField;

import java.util.*;

@RestController
@RequestMapping("/tasks")

/**
 * CRD
 * left: updation
 * refactor to tasks
 */
public class TaskController {
    @Autowired
    TaskService taskService;

    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping()
    @ResponseBody()
    public List<TaskEntity> getTaskEntityList() {
        return taskService.getAllTasks();
    }

    @GetMapping("/filter")
    @ResponseBody()
    public List<TaskEntity> getTasksWithUserIdMoreThan(@RequestParam Integer taskId) {
        System.out.println("_______________-------------------______________---------__________---------___________---");
        return taskService.getTasksWithIdMoreThan(taskId);
    }

    /*
    curl -s -X POST localhost:8080/accounts/new \
            -H "Content-Type: application/json" \
            -d '{"taskname": "test_uname", "password": "testpswd", "email": "test_uname@tmpmail.com", "created_at": "2023-06-04"}'
    */
    @Cacheable(value = "saaaample")
    @GetMapping("/bruh")
    public String bruh(@RequestParam String word) {
        System.out.println("yoooooooooooooooooooooooooooooooo");
        return word;
//        return accountService.getAccountsWithIdMoreThan(taskId);
    }

    @PostMapping("new")
    public TaskEntity addNewTask(@RequestBody TaskEntity taskEntity) {
        return taskService.addNewTask(taskEntity);
    }

    @GetMapping("{id}")
    public TaskEntity getTaskWithId(@PathVariable("id") Integer taskId) {
        System.out.println("******************************************888FIND******************************");
        Optional<TaskEntity> taskEntity = taskService.getTaskById(taskId);
        System.out.println(taskEntity);
        return taskEntity.get();
    }

    // Return a custom response that deletion unsuccessful because of id not being present
    @CacheEvict(value = "taskId", key = "#taskId")
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteTaskWithId(@PathVariable("id") Integer taskId) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        HttpStatus httpStatus = HttpStatus.OK;
        Optional<TaskEntity> taskEntity = taskService.getTaskById(taskId);
        System.out.println(taskEntity.isPresent());
        if (taskEntity.isPresent()) {
            taskService.deleteTaskById(taskId);
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

    @PutMapping("{id}")
    public ResponseEntity<Object> updateTaskWithId(@PathVariable("id") Integer taskId, @RequestBody TaskEntity taskEntity) {
        Optional<TaskEntity> taskEntityOptional = taskService.getTaskById(taskId);
        Map<String, Object> resMap = new HashMap<>();
        HttpStatus httpStatus;
        if (taskEntityOptional.isPresent()) {
            taskService.updateTaskWithId(taskEntityOptional.get(), taskEntity);
            resMap.put("update status", "Success");
            resMap.put("updated taskId", taskId);
            httpStatus = HttpStatus.OK;
        } else {
            resMap.put("update_status", "Failed");
            resMap.put("reason", "User with provided taskId does not exist");
            resMap.put("alphabetically", "should be first");
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(resMap, httpStatus);
    }
}
