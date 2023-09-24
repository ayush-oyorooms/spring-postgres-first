package com.example.springpostgres.controllers;

import com.example.springpostgres.entities.TaskEntity;
import com.example.springpostgres.services.TaskService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")

public class TaskController {
    @Autowired
    TaskService taskService;

    @GetMapping()
    @ResponseBody()
    public List<TaskEntity> getTaskEntityList() {
        return taskService.getAllTasks();
    }

    @GetMapping("/filter")
    @ResponseBody()
    public List<TaskEntity> getTasksWithUserIdMoreThan(@RequestParam Integer taskId) {
        return taskService.getTasksWithIdMoreThan(taskId);
    }

//    @GetMapping("/bruh")
//    public String bruh(@RequestParam String word) {
//        System.out.println("yoooooooooooooooooooooooooooooooo");
//        taskService.bruhMethod(word);
//        return word;
//    }

    @PostMapping("new")
    public TaskEntity addNewTask(@RequestBody TaskEntity taskEntity) {
        return taskService.addNewTask(taskEntity);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getTaskWithId(@PathVariable("id") Integer taskId) {
        System.out.println("******************************************888FIND******************************");
        return taskService.createResponseEntityIrrespectiveOfIdMatch(taskId);
    }

    // Return a custom response that deletion unsuccessful because of id not being present
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteTaskWithId(@PathVariable("id") Integer taskId) {
        ResponseEntity<Object> hi = taskService.deleteTaskById(taskId);
        return hi;
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateTaskWithId(@PathVariable("id") Integer taskId, @RequestBody TaskEntity taskEntity) {
        ResponseEntity<Object> responseEntity = taskService.createResponseEntityIrrespectiveOfIdMatch(taskId);
        return taskService.updateTaskWithId(taskId, taskEntity);
//        Map<String, Object> resMap = new HashMap<>();
//        HttpStatus httpStatus;
//        if (responseEntity.getStatusCode() == HttpStatus.OK) {
//            TaskEntity te = (TaskEntity) responseEntity.getBody();
//            taskService.updateTaskWithId(te, taskEntity);
//            resMap.put("update status", "Success");
//            resMap.put("updated taskId", taskId);
//            httpStatus = HttpStatus.OK;
//        } else {
//            resMap.put("update_status", "Failed");
//            resMap.put("reason", "User with provided taskId does not exist");
//            resMap.put("alphabetically", "should be first");
//            httpStatus = HttpStatus.NOT_FOUND;
//        }
//        return new ResponseEntity<>(resMap, httpStatus);
    }
}
