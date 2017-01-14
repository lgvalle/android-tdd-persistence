package xyz.lgvalle.tddpersistence.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by lgvalle on 08/01/2017.
 */

public class AndroidTransactor {

    private static final String TAG = AndroidTransactor.class.getSimpleName();

    public interface UnitOfWork {
        void work() throws Exception;
    }


    private final TaskReaderDbHelper dbHelper;

    public AndroidTransactor(TaskReaderDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void perform(UnitOfWork unitOfWork) throws Exception {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();
        try {
            unitOfWork.work();
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            throw e;
        } finally {
            db.endTransaction();
        }
    }

}
