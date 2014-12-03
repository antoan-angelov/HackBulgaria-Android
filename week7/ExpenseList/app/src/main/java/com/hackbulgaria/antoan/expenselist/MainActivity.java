package com.hackbulgaria.antoan.expenselist;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements OnEntriesLoaded {

    private ExpenseListAdapter mAdapter;
    private ExpenseListHelper mDbHelper;

    public MainActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText label = (EditText) findViewById(R.id.label);
                EditText price = (EditText) findViewById(R.id.price);
                ExpenseEntry item = new ExpenseEntry(label.getText().toString(), Float.parseFloat(price.getText().toString()));
                mAdapter.add(item);
                mDbHelper.addEntry(item);
                label.setText("");
                price.setText("");
            }
        });

        ListView listView = (ListView) findViewById(R.id.list);
        ArrayList<ExpenseEntry> items = new ArrayList<ExpenseEntry>();

        if (savedInstanceState != null) {
            ArrayList<String> labels = savedInstanceState.getStringArrayList("labels");
            ArrayList<String> prices = savedInstanceState.getStringArrayList("prices");
            for (int i = 0; i < labels.size(); i++) {
                ExpenseEntry item = new ExpenseEntry(labels.get(i), Float.parseFloat(prices.get(i)));
                items.add(item);
            }
        }

        mAdapter = new ExpenseListAdapter(this, mDbHelper, items);
        listView.setAdapter(mAdapter);

        mDbHelper = new ExpenseListHelper(this);
        new RetrieveAllEntriesTask(mDbHelper, this).execute();
    }

    @Override
    public void onEntriesLoaded(List<ExpenseEntry> entries) {
        mAdapter.addAll(entries);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        ArrayList<String> labels = new ArrayList<String>();
        ArrayList<String> prices = new ArrayList<String>();

        List<ExpenseEntry> items = mAdapter.getItems();
        for (ExpenseEntry item : items) {
            labels.add(item.getLabel());
            prices.add(String.valueOf(item.getPrice()));
        }

        outState.putStringArrayList("labels", labels);
        outState.putStringArrayList("prices", prices);

        super.onSaveInstanceState(outState);
    }


}
