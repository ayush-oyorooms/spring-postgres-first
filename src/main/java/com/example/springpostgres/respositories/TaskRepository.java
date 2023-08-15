package com.example.springpostgres.respositories;

import com.example.springpostgres.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {

    /**
     * Use the name of Entity class here, not the table in which it is present
     * @param act: the name of variable `account` which will be the output of query
     * @param userId: query should be passed to this param name (again, use it as declared in [AccountEntity]
     */
    @Query("SELECT task FROM TaskEntity task WHERE task.taskId >= :taskId")
    List<TaskEntity> getTasksWithUserIdMoreThan(@Param("taskId") Integer userId);

    // all task listing
    // task creation
//    @Query("insert ")
//    String createNewAccount(AccountEntity newAccount);
    // task deletion
    // task updating
    // cache?
}
