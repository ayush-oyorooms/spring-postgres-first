package com.example.springpostgres.entities;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

//@Data
@Entity
@Table(name="Tasks")
@RedisHash("TaskEntity_redis_key")
public class TaskEntity implements Serializable {
    private static final long serialVersionUID = 7156526077883281623L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int taskId;
    @Column(name = "title")
    @Indexed
    @JsonProperty("title")
    private String title;

    @Column(name = "description")
    @JsonProperty("description")
    private String description;

    @Column(name = "created_on")
    @JsonProperty("createdOn")
    private Date createdOn;

    @Column(name = "deadline")
    @JsonProperty("deadline")
    private Date deadline;

    @Column(name = "completed")
    @JsonProperty("isCompleted")
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
