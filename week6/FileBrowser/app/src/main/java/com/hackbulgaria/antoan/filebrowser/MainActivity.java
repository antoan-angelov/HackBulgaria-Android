package com.hackbulgaria.antoan.filebrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private static final String ROOT_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath();
    private ListView mListView;
    private BrowserAdapter mAdapter;
    private String mCurrentDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list);

        mCurrentDirectory = ROOT_DIRECTORY;
        List<Item> items = fillList(mCurrentDirectory);
        mAdapter = new BrowserAdapter(this, items);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item) parent.getItemAtPosition(position);
                if (item.getType() == Item.TYPE_FOLDER) {
                    mCurrentDirectory = mCurrentDirectory + "/" + item.getName();
                    mAdapter.clear();
                    mAdapter.addAll(fillList(mCurrentDirectory));
                } else if (item.getType() == Item.TYPE_FILE) {
                    openFile(mCurrentDirectory + "/" + item.getName());
                } else if (item.getType() == Item.TYPE_PARENT_DIR) {
                    goToParentDir();
                }
            }
        });
    }

    private void openFile(String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(path);
        String type = map.getMimeTypeFromExtension(ext);

        if (type == null) {
            type = "*/*";
        }

        Uri data = Uri.fromFile(new File(path));

        intent.setDataAndType(data, type);
        startActivity(intent);
    }

    private boolean goToParentDir() {
        if (mCurrentDirectory.equals(ROOT_DIRECTORY))
            return false;

        File file = new File(mCurrentDirectory);
        mCurrentDirectory = file.getParentFile().getAbsolutePath();
        mAdapter.clear();
        mAdapter.addAll(fillList(mCurrentDirectory));
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!goToParentDir()) {
            super.onBackPressed();
        }
    }

    private List<Item> fillList(String path) {
        List<Item> res = new ArrayList<Item>();
        File dir = new File(path);

        if (dir.exists()) {
            File[] files = dir.listFiles();

            if(!path.equals(ROOT_DIRECTORY)) {
                res.add(Item.ITEM_PARENT_DIR);
            }

            for (File file : files) {
                if (file.isDirectory()) {
                    res.add(new Item(file.getName(), Item.TYPE_FOLDER));
                } else {
                    res.add(new Item(file.getName(), Item.TYPE_FILE));
                }
            }
        }

        return res;
    }

    private class BrowserAdapter extends ArrayAdapter<Item> {

        public BrowserAdapter(Context context, List<Item> objects) {
            super(context, R.layout.item_browser, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rootView = convertView;
            ViewHolder holder;
            Item item = getItem(position);

            if (rootView == null) {
                rootView = getLayoutInflater().inflate(R.layout.item_browser, null, false);
                TextView text = (TextView) rootView.findViewById(R.id.text);
                holder = new ViewHolder(text);
                rootView.setTag(holder);
            } else {
                holder = (ViewHolder) rootView.getTag();
            }

            holder.text.setText(item.getName());

            int style = 0;
            switch(item.getType()) {
                case Item.TYPE_FOLDER:
                case Item.TYPE_PARENT_DIR:
                    style = R.style.FolderItemStyle;
                    break;
                case Item.TYPE_FILE:
                    style = R.style.FileItemStyle;
                    break;
            }

            holder.text.setTextAppearance(getApplicationContext(), style);

            return rootView;
        }

        private class ViewHolder {
            public final TextView text;

            public ViewHolder(TextView tv) {
                this.text = tv;
            }
        }
    }

    private static class Item {

        public final static Item ITEM_PARENT_DIR = new Item("..", Item.TYPE_PARENT_DIR);

        public static final int TYPE_FILE = 1;
        public static final int TYPE_FOLDER = 2;
        public static final int TYPE_PARENT_DIR = 3;
        private String mName;
        private int mType;

        public Item(String name, int type) {
            this.mName = name;
            this.mType = type;
        }

        public String getName() {
            return mName;
        }

        public int getType() {
            return mType;
        }
    }
}
