package xyz.lgvalle.tddpersistence.task;

import java.util.List;

public interface TaskStorage {
    void insert(TaskDBModel taskDBModel);

    void delete(TaskDBModel taskDBModel);

    List<TaskDBModel> findAllExpiredBy(long expirationDate);

    List<TaskDBModel> findAll();

    TaskDBModel findByName(String taskName);
}
