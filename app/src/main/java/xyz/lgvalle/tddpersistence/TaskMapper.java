package xyz.lgvalle.tddpersistence;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lgvalle on 08/01/2017.
 */

public class TaskMapper {

    public TaskDBModel fromDomain(Task task) {
        return new TaskDBModel(
                task.getName(),
                task.getExpiration().getTime()
        );
    }

    public Task toDomain(TaskDBModel taskDBModel) {
        return new Task(
                taskDBModel.getName(),
                new Date(taskDBModel.getExpiration())
        );
    }

}
