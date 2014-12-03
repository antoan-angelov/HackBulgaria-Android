package com.hackbulgaria.antoan.expenselist;

import android.provider.BaseColumns;

/**
 * Created by Antoan on 03-Dec-14.
 */
public class ExpenseListContract {

    public ExpenseListContract() { }

    public static abstract class ExpenseTableEntry implements BaseColumns {
        public static final String TABLE_NAME = "ExpenseList";
        public static final String COLUMN_LABEL = "label";
        public static final String COLUMN_PRICE = "price";
    }

}