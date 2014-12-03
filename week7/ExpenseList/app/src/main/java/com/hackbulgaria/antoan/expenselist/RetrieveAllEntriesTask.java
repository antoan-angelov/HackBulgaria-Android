package com.hackbulgaria.antoan.expenselist;

import android.os.AsyncTask;

import java.util.List;

/**
 * Created by Antoan on 03-Dec-14.
 */
public class RetrieveAllEntriesTask extends AsyncTask<Void, Void, List<ExpenseEntry>> {

    private final OnEntriesLoaded mCallback;
    private ExpenseListHelper mDbHelper;

    public RetrieveAllEntriesTask(ExpenseListHelper db, OnEntriesLoaded callback) {
        this.mCallback = callback;
        this.mDbHelper = db;
    }

    @Override
    protected List<ExpenseEntry> doInBackground(Void... params) {
        mDbHelper.open();

        List<ExpenseEntry> entries = mDbHelper.getEntries();

        mDbHelper.close();

        return entries;
    }

    @Override
    protected void onPostExecute(List<ExpenseEntry> entries) {
        super.onPostExecute(entries);
        if (mCallback != null) {
            mCallback.onEntriesLoaded(entries);
        }
    }
}