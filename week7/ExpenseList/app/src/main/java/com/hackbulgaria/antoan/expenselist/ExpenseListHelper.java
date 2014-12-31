package com.hackbulgaria.antoan.expenselist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import static com.hackbulgaria.antoan.expenselist.ExpenseListContract.ExpenseTableEntry.*;

/**
 * Created by Antoan on 03-Dec-14.
 */
public class ExpenseListHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ExpenseListContract.ExpenseTableEntry.TABLE_NAME + " (" +
                    ExpenseListContract.ExpenseTableEntry._ID + " INTEGER PRIMARY KEY," +
                    ExpenseListContract.ExpenseTableEntry.COLUMN_LABEL + " TEXT, " +
                    ExpenseListContract.ExpenseTableEntry.COLUMN_PRICE + " REAL);";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ExpenseListContract.ExpenseTableEntry.TABLE_NAME;
    private SQLiteDatabase db;

    public ExpenseListHelper(Context context) {
        super(context, ExpenseListContract.ExpenseTableEntry.TABLE_NAME, null, DATABASE_VERSION);
    }

    public void open() {
        if (db == null)
            db = getWritableDatabase();
    }

    public void close() {
        if (db != null) {
            db.close();
            db = null;
        }
    }

    public void addEntry(ExpenseEntry entry) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ExpenseListContract.ExpenseTableEntry.COLUMN_LABEL, entry.getLabel());
        values.put(ExpenseListContract.ExpenseTableEntry.COLUMN_PRICE, entry.getPrice());

        db.insert(
                ExpenseListContract.ExpenseTableEntry.TABLE_NAME,
                null,
                values);
    }

    public List<ExpenseEntry> getEntries() {
        List<ExpenseEntry> res = new ArrayList<ExpenseEntry>();
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                ExpenseListContract.ExpenseTableEntry._ID,
                ExpenseListContract.ExpenseTableEntry.COLUMN_LABEL,
                ExpenseListContract.ExpenseTableEntry.COLUMN_PRICE,
        };

        String query = new FluentApiBuilder().select("*").from(ExpenseListContract.ExpenseTableEntry.TABLE_NAME).build();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String label = cursor.getString(1);
                float price = cursor.getFloat(2);
                res.add(new ExpenseEntry(id, label, price));
            } while (cursor.moveToNext());
        }

        return res;
    }

    public void deleteEntry(int id) {
        String query = new FluentApiBuilder().delete().from(TABLE_NAME).where(_ID).eq(String.valueOf(id)).build();
        db.rawQuery(query, null);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
