package com.example.springpostgres.entities;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name="Tasks")
public class TaskEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int taskId;
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "deadline")
    private Date deadline;

    @Column(name = "completed")
    private Boolean isCompleted;

    public TaskEntity() { }
    public TaskEntity(String title, String description, Date createdOn) {
        this.title = title;
        this.description = description;
        this.createdOn = createdOn;
        this.deadline = createdOn;
        this.isCompleted = false;
    }

    public TaskEntity(String title, String description, Date createdOn, Boolean isCompleted) {
        this.title = title;
        this.description = description;
        this.createdOn = createdOn;
        this.deadline = createdOn;
        this.isCompleted = isCompleted;
    }
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public Date getDeadline() {
        return deadline;
    }
}
