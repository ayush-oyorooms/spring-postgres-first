package com.example.springpostgres.controllers;

import com.example.springpostgres.entities.AccountEntity;
import com.example.springpostgres.services.AccountService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/accounts")

/**
 * CRD
 * left: updation
 * refactor to tasks
 */
public class AccountController {
    @Autowired
    AccountService accountService;

    @GetMapping()
    @ResponseBody()
    public List<AccountEntity> accountEntityList() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/filter")
    @ResponseBody()
    public List<AccountEntity> getAccountsWithUserIdMoreThan(@RequestParam Integer userId) {
        System.out.println("yoooooooooooooooooooooooooooooooo");
        return accountService.getAccountsWithIdMoreThan(userId);
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
    public AccountEntity addNewAccount(@RequestBody AccountEntity accountEntity) {
        System.out.println(accountEntity.getEmail());
        return accountService.addNewAccount(accountEntity);
    }

    @GetMapping("{id}")
    public ResponseEntity<AccountEntity> getAccountById(@PathVariable("id") Integer userId) {
        Optional<AccountEntity> accountEntity = accountService.getAccountById(userId);
        if (accountEntity.isPresent()) {
            return new ResponseEntity<>(accountEntity.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Return a custom response that deletion unsuccessful because of id not being present
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteAccountById(@PathVariable("id") Integer userId) {
        Optional<AccountEntity> accountEntity = accountService.getAccountById(userId);
        Map<String, Object> resMap = new HashMap<String, Object>();
        HttpStatus httpStatus;
        if (accountEntity.isPresent()) {
            accountService.deleteAccountById(userId);
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
    public ResponseEntity<Object> updateAccountById(@PathVariable("id") Integer userId, @RequestBody AccountEntity accountEntity) {
        Optional<AccountEntity> accountEntityOptional = accountService.getAccountById(userId);
        Map<String, Object> resMap = new HashMap<>();
        HttpStatus httpStatus;
        if (accountEntityOptional.isPresent()) {
            accountService.updateAccountById(accountEntityOptional.get(), accountEntity);
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
