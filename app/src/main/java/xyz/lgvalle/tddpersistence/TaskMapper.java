package xyz.lgvalle.tddpersistence;

import java.util.Date;

/**
 * Created by lgvalle on 08/01/2017.
 */

public class TaskMapper {

    public TaskDBModel fromDomain(Task task) {
        Date expiration = task.getExpiration();
        if (expiration == null) {
            expiration = new Date();
        }
        return new TaskDBModel(
                task.getName(),
                expiration.getTime()
        );
    }

    public Task toDomain(TaskDBModel taskDBModel) {
        return new Task(
                taskDBModel.getName(),
                new Date(taskDBModel.getExpiration())
        );
    }

}
