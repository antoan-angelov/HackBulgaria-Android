package com.hackbulgaria.antoan.expenselist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antoan on 03-Dec-14.
 */
public class ExpenseListAdapter extends ArrayAdapter<ExpenseEntry> {

    private ArrayList<ExpenseEntry> mItems;
    private ExpenseListHelper mDbHelper;

    public ExpenseListAdapter(Context context, ExpenseListHelper db, ArrayList<ExpenseEntry> items) {
        super(context, 0, items);
        this.mItems = items;
        this.mDbHelper = db;
    }

    public List<ExpenseEntry> getItems() {
        return mItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ExpenseEntry item = getItem(position);
        ViewHolder holder;


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, parent, false);
            holder = new ViewHolder();
            holder.label = (TextView) convertView.findViewById(R.id.label);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.delete = (ImageButton) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.label.setText(item.getLabel());
        holder.price.setText(String.valueOf(item.getPrice()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(item);
                new DeleteEntryTask(mDbHelper).execute(item.getId());
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView label;
        TextView price;
        ImageButton delete;
    }
}
