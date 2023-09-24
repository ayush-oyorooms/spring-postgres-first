package com.example.springpostgres.respositories;

import com.example.springpostgres.entities.TaskEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

//public interface TaskRepository extends CrudRepository<TaskEntity, Integer> {
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {

    /**
     * Use the name of Entity class here, not the table in which it is present
     * @param task: the name of variable `task` which will be the output of query
     * @param taskId: query should be passed to this param name (again, use it as declared in [TaskEntity]
     */

    @Query("SELECT task FROM TaskEntity task WHERE task.taskId >= :taskId")
    List<TaskEntity> getTasksWithUserIdMoreThan(@Param("taskId") Integer taskId);

    @Cacheable(value = "#taskId", unless = "#result == null")
    @Query("SELECT task FROM TaskEntity task WHERE task.taskId = :taskId")
    Optional<TaskEntity> findTaskById(@Param("taskId") Integer taskId);



    // all task listing
    // task creation
//    @Query("insert ")
//    String createNewAccount(AccountEntity newAccount);
    // task deletion
    // task updating
    // cache?
}
