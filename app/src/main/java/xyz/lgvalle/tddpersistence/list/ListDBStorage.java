package xyz.lgvalle.tddpersistence.list;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import xyz.lgvalle.tddpersistence.db.TaskReaderDbHelper;

import static xyz.lgvalle.tddpersistence.db.ListReaderContract.ListEntry.TABLE_NAME;
import static xyz.lgvalle.tddpersistence.db.TaskReaderContract.TaskEntry.COLUMN_TASK_NAME;


public class ListDBStorage implements ListStorage {

    private final TaskReaderDbHelper taskReaderDbHelper;

    public ListDBStorage(TaskReaderDbHelper taskReaderDbHelper) {
        this.taskReaderDbHelper = taskReaderDbHelper;
    }

    private SQLiteDatabase openDB() {
        return taskReaderDbHelper.getWritableDatabase();
    }

    private void closeDB() {
        taskReaderDbHelper.close();
    }

    @Override
    public void insert(List list) {
        SQLiteDatabase db = openDB();

        ContentValues values = toContentValues(list);

        db.insertOrThrow(TABLE_NAME, null, values);

        closeDB();
    }

    private ContentValues toContentValues(List taskDBModel) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, taskDBModel.getListName());
        return values;
    }
}
