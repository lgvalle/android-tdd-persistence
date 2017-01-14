package xyz.lgvalle.tddpersistence;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import xyz.lgvalle.tddpersistence.db.TaskReaderDbHelper;

import static xyz.lgvalle.tddpersistence.TaskDBStorageTest.TaskBuilder.aTask;


/**
 * Created by lgvalle on 08/01/2017.
 */
@RunWith(AndroidJUnit4.class)
public class TaskDBStorageTest {

    private TaskDBStorage taskDBStorage;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        TaskReaderDbHelper taskReaderDbHelper = new TaskReaderDbHelper(appContext);
        taskDBStorage = new TaskDBStorage(taskReaderDbHelper);


        DatabaseCleaner cleaner = new DatabaseCleaner(taskReaderDbHelper);
        cleaner.clean();
    }

    @Test
    public void canSaveTask() {

        TaskDBModel task = aTask().withName("task 1").build();

        taskDBStorage.insert(task);

        Assert.assertTrue(false);
    }


    public static class TaskBuilder {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
            this.date = date;
            return this;
        }

        public TaskDBModel build() {
            TaskDBModel taskDBModel = new TaskDBModel(name, 1);
            return taskDBModel;
        }


    }

}