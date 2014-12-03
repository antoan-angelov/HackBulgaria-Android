package com.hackbulgaria.antoan.expenselist;

import android.os.AsyncTask;

/**
 * Created by Antoan on 03-Dec-14.
 */
public class DeleteEntryTask extends AsyncTask<Integer, Void, Void> {

    private ExpenseListHelper mDbHelper;

    DeleteEntryTask(ExpenseListHelper db) {
        this.mDbHelper = db;
    }

    @Override
    protected Void doInBackground(Integer... ids) {
        mDbHelper.open();

        for (int id : ids) {
            mDbHelper.deleteEntry(id);
        }

        mDbHelper.close();

        return null;
    }
}
