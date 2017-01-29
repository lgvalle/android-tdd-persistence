package xyz.lgvalle.tddpersistence;


import xyz.lgvalle.tddpersistence.task.Task;
import xyz.lgvalle.tddpersistence.task.TaskRepository;

public class MainActivityPresenter {

    private final TaskRepository taskRepository;

    public MainActivityPresenter(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void save(Task task) {
        taskRepository.persistTask(task);
    }

}
