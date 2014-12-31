package com.hackbulgaria.antoan.flappy.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.hackbulgaria.antoan.flappy.R;
import com.hackbulgaria.antoan.flappy.wrappers.UserWrapper;
import com.hackbulgaria.antoan.flappy.listeners.IScoreInsertedListener;
import com.hackbulgaria.antoan.flappy.tasks.InsertAsyncTask;

/**
 * Created by Antoan on 30-Dec-14.
 */
public class EnterNameDialogFragment extends DialogFragment implements IScoreInsertedListener {
    private View mRoot;
    private int mId;
    private String mName;
    private int mScore;
    private Context mAppContext;

    public static EnterNameDialogFragment newInstance(int score) {
        EnterNameDialogFragment f = new EnterNameDialogFragment();
        Bundle args = new Bundle();
        args.putInt("score", score);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mScore = getArguments().getInt("score");

        SharedPreferences prefs = getActivity().getSharedPreferences("score", Context.MODE_PRIVATE);
        mId = prefs.getInt("mId", 0);
        mName = prefs.getString("name", null);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        else {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_dialog_enter_name, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        getDialog().getWindow().getDecorView().setSystemUiVisibility(getActivity().getWindow().getDecorView().getSystemUiVisibility());

        mAppContext = mRoot.getContext().getApplicationContext();

        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
                wm.updateViewLayout(getDialog().getWindow().getDecorView(), getDialog().getWindow().getAttributes());
            }
        });


        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                "font.ttf");

        final EditText name = (EditText) mRoot.findViewById(R.id.enter_name);
        name.setTypeface(face);
        if(mName != null) {
            name.setText(mName);
        }

        TextView header = (TextView) mRoot.findViewById(R.id.header);
        header.setTypeface(face);
        header.setText("Your score is "+mScore);

        Button cancel = (Button) mRoot.findViewById(R.id.btn_cancel);
        cancel.setTypeface(face);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        Button ok = (Button) mRoot.findViewById(R.id.btn_ok);
        ok.setTypeface(face);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = getActivity().getSharedPreferences("score", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("name", name.getText().toString());
                editor.apply();

                UserWrapper insert = new UserWrapper(mId, name.getText().toString(), mScore);

                new InsertAsyncTask(getActivity().getApplicationContext(),
                        insert, EnterNameDialogFragment.this).execute();
            }
        });

        return mRoot;
    }

    public void OnScoreInserted(UserWrapper user) {

        if (user == null) {
            Toast.makeText(mAppContext, "An error occurred.", Toast.LENGTH_LONG).show();
            return;
        }

        SharedPreferences prefs = mAppContext.getSharedPreferences("score", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("id", user.getId());
        editor.putString("name", user.getName());
        editor.putInt("score", user.getScore());
        editor.apply();

        Toast.makeText(mAppContext, "Your score was successfully saved!", Toast.LENGTH_LONG).show();

        if(getDialog() != null) {
            getDialog().dismiss();
        }
    }

}
