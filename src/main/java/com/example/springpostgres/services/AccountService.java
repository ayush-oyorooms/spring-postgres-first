package com.example.springpostgres.services;

import com.example.springpostgres.entities.AccountEntity;
import com.example.springpostgres.respositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepo;
    public List<AccountEntity> getAllAccounts() {
        return accountRepo.findAll();
    }

    public List<AccountEntity> getAccountsWithIdMoreThan(Integer userId) {
        return accountRepo.accountsWithUserIdMoreThan(userId);
    }

    public AccountEntity addNewAccount(AccountEntity accountEntity) {
        System.out.println(accountEntity.getUsername());
        System.out.println(accountEntity.getPassword());
        System.out.println(accountEntity.getEmail());
        System.out.println(accountEntity.getCreatedOn());
        AccountEntity _accountEntity = new AccountEntity(accountEntity.getUsername(), accountEntity.getPassword(),
                accountEntity.getEmail(), accountEntity.getCreatedOn());
        return accountRepo.save(_accountEntity);
    }

    public Optional<AccountEntity> getAccountById(Integer userId) {
        return accountRepo.findById(userId);
    }

    public void deleteAccountById(Integer userId) {
        accountRepo.deleteById(userId);
    }

    public ResponseEntity<AccountEntity> updateAccountById(AccountEntity accountEntityData, AccountEntity accountEntity) {
        if (accountEntity.getUsername() != null) {
            accountEntityData.setUsername(accountEntity.getUsername());
        }
        if (accountEntity.getEmail() != null) {
            accountEntityData.setEmail(accountEntity.getEmail());
        }
        if (accountEntity.getPassword() != null) {
            accountEntityData.setPassword(accountEntity.getPassword());
        }
        return new ResponseEntity<>(accountRepo.save(accountEntityData), HttpStatus.OK);
    }
}
