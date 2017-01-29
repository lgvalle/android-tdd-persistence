package xyz.lgvalle.tddpersistence.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import xyz.lgvalle.tddpersistence.db.TaskReaderContract.TaskEntry;

import static xyz.lgvalle.tddpersistence.db.ListReaderContract.*;

public class TaskReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Tasks.db";

    private static final String SQL_CREATE_TABLE_TASKS =
            "CREATE TABLE " + TaskEntry.TABLE_NAME + " (" +
                    TaskEntry._ID + " INTEGER PRIMARY KEY," +
                    TaskEntry.COLUMN_TASK_NAME + " TEXT," +
                    TaskEntry.COLUMN_TASK_EXPIRATION + " INTEGER," +
                    TaskEntry.COLUMN_TASK_LIST + " INTEGER)";
//    "FOREIGN KEY("+TaskEntry.COLUMN_TASK_LIST+") REFERENCES lists(_id) )";

    private static final String SQL_CREATE_TABLE_LISTS =
            "CREATE TABLE " + ListEntry.TABLE_NAME + " (" +
                    ListEntry._ID + " INTEGER PRIMARY KEY," +
                    ListEntry.COLUMN_LIST_NAME + " TEXT)";

    private static final String SQL_DELETE_TABLE_TASKS =
            "DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME;

    private static final String SQL_DELETE_TABLE_LISTS =
            "DROP TABLE IF EXISTS " + ListEntry.TABLE_NAME;

    public TaskReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_TASKS);
        db.execSQL(SQL_CREATE_TABLE_LISTS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_TABLE_TASKS);
        db.execSQL(SQL_DELETE_TABLE_LISTS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}