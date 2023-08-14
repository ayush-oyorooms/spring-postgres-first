package com.example.springpostgres.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.sql.Date;

@Entity
@Table(name="ACCOUNTS")
public class AccountEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "last_login")
    private Date lastLogin;

    public AccountEntity() { }
    public AccountEntity(String username, String password, String email, Date createdOn) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdOn = createdOn;
        this.lastLogin = createdOn;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public Date getLastLogin() {
        return lastLogin;
    }
}
