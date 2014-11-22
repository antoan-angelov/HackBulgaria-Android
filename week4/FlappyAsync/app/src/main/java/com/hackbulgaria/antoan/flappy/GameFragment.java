package com.hackbulgaria.antoan.flappy;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment {

    private OnGameOverListener mListener;
    private View mView;
    private Context mContext;
    private DrawingView mDrawingView;
    private AudioManager mAudioManager;

    public static GameFragment newInstance() {
        GameFragment fragment = new GameFragment();
        return fragment;
    }

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_game, container, false);

        mDrawingView = (DrawingView) this.mView.findViewById(R.id.drawing);

        return this.mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mDrawingView.resume();
        mAudioManager = new AudioManager(mContext);
        mDrawingView.setAudioManager(mAudioManager);
        mDrawingView.setOnGameOverListener(mListener);
    }

    @Override
    public void onPause() {
        super.onPause();

        mDrawingView.pause(true);

        mAudioManager.dispose();
        mAudioManager = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext = activity;

        try {
            mListener = (OnGameOverListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnGameOverListener {
        public void onGameOver(int score);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
