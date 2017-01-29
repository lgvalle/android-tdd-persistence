package xyz.lgvalle.tddpersistence.task;

import java.util.Date;


public class TaskMapper {

    public TaskDBModel fromDomain(Task task) {
        Date expiration = task.getExpiration();
        if (expiration == null) {
            expiration = new Date();
        }
        return new TaskDBModel(
                task.getName(),
                expiration.getTime(),
                task.getListName());
    }

    public Task toDomain(TaskDBModel taskDBModel) {
        return new Task(
                taskDBModel.getName(),
                new Date(taskDBModel.getExpiration()),
                taskDBModel.getListName());
    }

}
