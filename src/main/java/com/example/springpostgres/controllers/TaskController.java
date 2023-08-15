package com.example.springpostgres.controllers;

import com.example.springpostgres.entities.TaskEntity;
import com.example.springpostgres.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping()
    @ResponseBody()
    public List<TaskEntity> getTaskEntityList() {
        return taskService.getAllTasks();
    }

    @GetMapping("/filter")
    @ResponseBody()
    public List<TaskEntity> getTasksWithUserIdMoreThan(@RequestParam Integer userId) {
        System.out.println("yoooooooooooooooooooooooooooooooo");
        return taskService.getTasksWithIdMoreThan(userId);
    }

    /*
    curl -s -X POST localhost:8080/accounts/new \
            -H "Content-Type: application/json" \
            -d '{"username": "test_uname", "password": "testpswd", "email": "test_uname@tmpmail.com", "created_at": "2023-06-04"}'
    */
    @GetMapping("/bruh")
    public String bruh(@RequestParam String word) {
        System.out.println("yoooooooooooooooooooooooooooooooo");
        return word;
//        return accountService.getAccountsWithIdMoreThan(userId);
    }

    @PostMapping("new")
    public TaskEntity addNewTask(@RequestBody TaskEntity taskEntity) {
        return taskService.addNewTask(taskEntity);
    }

    @GetMapping("{id}")
    public ResponseEntity<TaskEntity> getTaskWithId(@PathVariable("id") Integer userId) {
        Optional<TaskEntity> taskEntity = taskService.getTaskById(userId);
        if (taskEntity.isPresent()) {
            return new ResponseEntity<>(taskEntity.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Return a custom response that deletion unsuccessful because of id not being present
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteTaskWithId(@PathVariable("id") Integer userId) {
        Optional<TaskEntity> taskEntity = taskService.getTaskById(userId);
        Map<String, Object> resMap = new HashMap<String, Object>();
        HttpStatus httpStatus;
        if (taskEntity.isPresent()) {
            taskService.deleteTaskById(userId);
            resMap.put("deletion_status", "Success");
            resMap.put("deleted_user_id", userId);
            httpStatus = HttpStatus.OK;
        } else {
            // reason pehle aata json mein deletion_status se
            resMap.put("deletion_status", "Failed");
            resMap.put("reason", "User with provided userid not found");
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(resMap, httpStatus);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateTaskWithId(@PathVariable("id") Integer userId, @RequestBody TaskEntity taskEntity) {
        Optional<TaskEntity> taskEntityOptional = taskService.getTaskById(userId);
        Map<String, Object> resMap = new HashMap<>();
        HttpStatus httpStatus;
        if (taskEntityOptional.isPresent()) {
            taskService.updateTaskWithId(taskEntityOptional.get(), taskEntity);
            resMap.put("update status", "Success");
            resMap.put("updated userId", userId);
            httpStatus = HttpStatus.OK;
        } else {
            resMap.put("update_status", "Failed");
            resMap.put("reason", "User with provided userId does not exist");
            resMap.put("alphabetically", "should be first");
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(resMap, httpStatus);
    }
}
