package com.hackbulgaria.antoan.flappy.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.hackbulgaria.antoan.flappy.R;
import com.hackbulgaria.antoan.flappy.wrappers.UserWrapper;
import com.hackbulgaria.antoan.flappy.listeners.IHighScoresLoadedListener;
import com.hackbulgaria.antoan.flappy.tasks.GetHighscoresAsyncTask;

/**
 * Created by Antoan on 30-Dec-14.
 */
public class ViewHighscoresDialogFragment extends DialogFragment implements IHighScoresLoadedListener {

    private View mRoot;

    public static ViewHighscoresDialogFragment newInstance() {
        ViewHighscoresDialogFragment f = new ViewHighscoresDialogFragment();

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_dialog_highscores, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        getDialog().getWindow().getDecorView().setSystemUiVisibility(getActivity().getWindow().getDecorView().getSystemUiVisibility());

        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
                wm.updateViewLayout(getDialog().getWindow().getDecorView(), getDialog().getWindow().getAttributes());
            }
        });

        TextView namesTv = (TextView) mRoot.findViewById(R.id.names);
        TextView scoresTv = (TextView) mRoot.findViewById(R.id.scores);

        namesTv.setText("");
        scoresTv.setText("");

        Typeface face= Typeface.createFromAsset(getActivity().getAssets(),
                "font.ttf");

        namesTv.setTypeface(face);
        scoresTv.setTypeface(face);

        TextView tv = (TextView) mRoot.findViewById(R.id.scores_header);
        tv.setText("LOADING...");
        tv.setTypeface(face);

        mRoot.findViewById(R.id.cont).setVisibility(View.GONE);

        new GetHighscoresAsyncTask(getActivity().getApplicationContext(), this).execute();

        return mRoot;
    }

    @Override
    public void OnHighscoresLoaded(UserWrapper[] users) {

        if(users == null) {
            Toast.makeText(getActivity(), "An error occurred.", Toast.LENGTH_LONG).show();
            return;
        }

        mRoot.findViewById(R.id.cont).setVisibility(View.VISIBLE);
        TextView tv = (TextView) mRoot.findViewById(R.id.scores_header);
        tv.setText("HIGHSCORES!");
        TextView namesTv = (TextView) mRoot.findViewById(R.id.names);
        TextView scoresTv = (TextView) mRoot.findViewById(R.id.scores);
        StringBuilder names = new StringBuilder();
        StringBuilder scores = new StringBuilder();
        for(int i=1; i<=users.length; i++) {
            names.append(i + ". " + users[i - 1].getName() + "\n");
            scores.append(users[i - 1].getScore() + "\n");
        }

        namesTv.setText(names.toString());
        scoresTv.setText(scores.toString());
    }
}
