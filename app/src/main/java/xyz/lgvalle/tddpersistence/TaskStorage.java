package xyz.lgvalle.tddpersistence;

import java.util.List;

/**
 * Created by lgvalle on 08/01/2017.
 */
public interface TaskStorage {
    void insert(TaskDBModel taskDBModel);

    void delete(TaskDBModel taskDBModel);

    List<TaskDBModel> findAllExpiredBy(long expirationDate);

    TaskDBModel findByName(String taskName);
}
