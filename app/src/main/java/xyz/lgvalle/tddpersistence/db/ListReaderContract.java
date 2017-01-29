package xyz.lgvalle.tddpersistence.db;

import android.provider.BaseColumns;

public final class ListReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ListReaderContract() {
    }

    /* Inner class that defines the table contents */
    public static class ListEntry implements BaseColumns {
        public static final String TABLE_NAME = "lists";
        public static final String COLUMN_LIST_NAME = "name";
    }
}
