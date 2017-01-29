package xyz.lgvalle.tddpersistence.task;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskRepository {
    private final TaskMapper taskMapper;
    private final TaskStorage taskStorage;

    public TaskRepository(TaskMapper taskMapper, TaskStorage taskStorage) {
        this.taskMapper = taskMapper;
        this.taskStorage = taskStorage;
    }

    public void persistTask(Task task) {
        TaskDBModel taskDBModel = taskMapper.fromDomain(task);
        taskStorage.insert(taskDBModel);
    }

    public List<Task> tasksExpiredBy(Date date) {
        long expirationDate = date.getTime();
        return dbTasksToDomain(taskStorage.findAllExpiredBy(expirationDate));
    }

    public Task taskWithName(String taskName) {
        TaskDBModel taskDBModel = taskStorage.findByName(taskName);
        return taskMapper.toDomain(taskDBModel);
    }

    public List<Task> allTasks() {
        return dbTasksToDomain(taskStorage.findAll());
    }

    @NonNull
    private List<Task> dbTasksToDomain(List<TaskDBModel> allExpiredBy) {
        List<Task> tasks = new ArrayList<>();
        for (TaskDBModel taskDBModel : allExpiredBy) {
            tasks.add(taskMapper.toDomain(taskDBModel));
        }
        return tasks;
    }
}
