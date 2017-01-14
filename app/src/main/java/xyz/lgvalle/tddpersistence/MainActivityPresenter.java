package xyz.lgvalle.tddpersistence;

/**
 * Created by lgvalle on 08/01/2017.
 */

public class MainActivityPresenter {

    private final TaskRepository taskRepository;

    public MainActivityPresenter(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void save(Task task) {
        taskRepository.persist(task);
    }

}
