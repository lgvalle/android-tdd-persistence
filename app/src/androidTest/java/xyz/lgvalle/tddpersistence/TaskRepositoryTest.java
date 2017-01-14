package xyz.lgvalle.tddpersistence;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import xyz.lgvalle.tddpersistence.db.TaskReaderDbHelper;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static xyz.lgvalle.tddpersistence.TaskNamedMatcher.aTaskNamed;
import static xyz.lgvalle.tddpersistence.TaskRepositoryTest.TaskBuilder.aTask;


@RunWith(AndroidJUnit4.class)
public class TaskRepositoryTest {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private TaskRepository taskRepository;

    List<TestBuilder<Task>> persistentObjectBuilders = Arrays.<TestBuilder<Task>>asList(
            aTask().withName("A task").withExpirationDate("2017-03-16")
    );

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        TaskReaderDbHelper dbHelper = new TaskReaderDbHelper(appContext);
        TaskDBStorage storage = new TaskDBStorage(dbHelper);
        TaskMapper mapper = new TaskMapper();

        taskRepository = new TaskRepository(mapper, storage);

        DatabaseCleaner cleaner = new DatabaseCleaner(dbHelper);
        cleaner.clean();
    }

    @Test
    public void canSaveTask() {

        Task task = aTask().withName("Task 1").withExpirationDate("2017-01-31").build();

        taskRepository.persist(task);

        Assert.assertTrue(false);
    }

    @Test
    public void findsExpiredTasks() throws Exception {
        String deadline = "2017-01-14";

        addTasks(
                aTask().withName("Task 1 (-Valid-)").withExpirationDate("2017-01-31"),
                aTask().withName("Task 2 (Expired)").withExpirationDate("2017-01-01"),
                aTask().withName("Task 3 (-Valid-)").withExpirationDate("2017-02-11"),
                aTask().withName("Task 4 (-Valid-)").withExpirationDate("2017-02-14"),
                aTask().withName("Task 5 (Expired)").withExpirationDate("2017-01-13")
        );

        assertTasksExpiringOn(deadline,
                containsInAnyOrder(
                        aTaskNamed("Task 2 (Expired)"),
                        aTaskNamed("Task 5 (Expired)"))
        );
    }

    @Test
    public void roundTripsPersistentObjects() {
        for (TestBuilder builder: persistentObjectBuilders) {
            assertCanBePersisted(builder);
        }
    }

    private void assertCanBePersisted(TestBuilder builder) {
        assertReloadsWithSameStateAs(persistedObjectFrom(builder));
    }

    private void assertReloadsWithSameStateAs(Task original) {
        Task savedTask = taskRepository.taskWithName(original.getName());

        // Without reflection this can fail:
        // If a new field is added without including it on the equals method and it is wrongly mapped this matcher won't pick it up.
        assertThat(savedTask, equalTo(original));
    }

    private Task persistedObjectFrom(TestBuilder builder) {
        Task original = (Task) builder.build();
        taskRepository.persist(original);
        return original;

    }

    private void addTasks(final TaskBuilder... tasks) throws Exception {
        for (TaskBuilder task : tasks) {
            taskRepository.persist(task.build());
        }
    }

    private void assertTasksExpiringOn(String deadline, Matcher<Iterable<? extends Task>> taskMatcher) throws ParseException {
        Date date = dateFormat.parse(deadline);

        assertThat(taskRepository.tasksExpiredBy(date), taskMatcher);
    }

    public static class TaskBuilder implements TestBuilder<Task> {

        private String name;
        private Date date;

        public static TaskBuilder aTask() {
            return new TaskBuilder();
        }

        public TaskBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public TaskBuilder withExpirationDate(String strDate) {

            try {
                date = dateFormat.parse(strDate);
            } catch (ParseException ex) {
                date = null;
            }

            return this;
        }

        @Override
        public Task build() {
            return new Task(name, date);
        }


    }
}