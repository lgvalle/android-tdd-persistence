package xyz.lgvalle.tddpersistence;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import xyz.lgvalle.tddpersistence.db.ListRepository;
import xyz.lgvalle.tddpersistence.db.TaskReaderDbHelper;
import xyz.lgvalle.tddpersistence.list.List;
import xyz.lgvalle.tddpersistence.list.ListDBStorage;
import xyz.lgvalle.tddpersistence.list.ListStorage;
import xyz.lgvalle.tddpersistence.task.Task;
import xyz.lgvalle.tddpersistence.task.TaskDBStorage;
import xyz.lgvalle.tddpersistence.task.TaskMapper;
import xyz.lgvalle.tddpersistence.task.TaskRepository;
import xyz.lgvalle.tddpersistence.task.TaskStorage;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static xyz.lgvalle.tddpersistence.TaskNamedMatcher.aTaskNamed;
import static xyz.lgvalle.tddpersistence.TaskRepositoryTest.ListBuilder.aList;
import static xyz.lgvalle.tddpersistence.TaskRepositoryTest.TaskBuilder.aTask;


@RunWith(AndroidJUnit4.class)
public class TaskRepositoryTest {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private TaskRepository taskRepository;
    private ListRepository listRepository;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        TaskReaderDbHelper dbHelper = new TaskReaderDbHelper(appContext);
        TaskStorage taskStorage = new TaskDBStorage(dbHelper);
        TaskMapper mapper = new TaskMapper();

        taskRepository = new TaskRepository(mapper, taskStorage);

        ListStorage listStorage = new ListDBStorage(dbHelper);
        listRepository = new ListRepository(listStorage);

        DatabaseCleaner cleaner = new DatabaseCleaner(dbHelper);
        cleaner.clean();
    }

    @Test
    public void findsExpiredTasks() throws Exception {
        String deadline = "2017-01-14";

        addTasksToList(
                aList().withName("TODO LIST"),
                aTask().withName("Task 1 (-Valid-)").withExpirationDate("2017-01-31"),
                aTask().withName("Task 2 (Expired)").withExpirationDate("2017-01-01"),
                aTask().withName("Task 3 (-Valid-)").withExpirationDate("2017-02-11"),
                aTask().withName("Task 4 (-Valid-)").withExpirationDate("2017-02-14"),
                aTask().withName("Task 5 (Expired)").withExpirationDate("2017-01-13")
        );

        assertTasksExpiringOn(deadline,
                containsInAnyOrder(
                        aTaskNamed("Task 2 (Expired)"),
                        aTaskNamed("Task 5 (Expired)")
                )
        );
    }

    private void addTasksToList(ListBuilder listBuilder, final TaskBuilder... tasks)  {
        for (TaskBuilder taskBuilder : tasks) {
            Task task = taskBuilder.forList(persisted(listBuilder)).build();
            taskRepository.persistTask(task);
        }
    }

    private TestBuilder<List> persisted(final TestBuilder<List> listBuilder) {
        return new TestBuilder<List>() {
            @Override
            public List build() {
                List list = listBuilder.build();
                listRepository.persistList(list);
                return list;
            }
        };
    }

    private void assertTasksExpiringOn(String deadline, Matcher<Iterable<? extends Task>> taskMatcher) throws ParseException {
        Date date = dateFormat.parse(deadline);
        assertThat(taskRepository.tasksExpiredBy(date), taskMatcher);
    }

    public static class TaskBuilder implements TestBuilder<Task> {

        private String name;
        private Date date;
        private TestBuilder<List> listBuilder;

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

        public TaskBuilder forList(TestBuilder<List> listBuilder) {
            this.listBuilder = listBuilder;
            return this;
        }

        @Override
        public Task build() {
            List list = listBuilder.build();
            return new Task(name, date, list.getListName());
        }
    }

    public static class ListBuilder implements TestBuilder<List> {
        private String name;

        public static ListBuilder aList() {
            return new ListBuilder();
        }

        public ListBuilder withName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public List build() {
            return new List(name);
        }
    }
}