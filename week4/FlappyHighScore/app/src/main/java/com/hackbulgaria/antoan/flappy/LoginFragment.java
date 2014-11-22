package com.hackbulgaria.antoan.flappy;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.hackbulgaria.antoan.flappy.FragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.hackbulgaria.antoan.flappy.LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private OnLoginListener mListener;
    private View mView;
    private Context mContext;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_login, container, false);

        Button v = (Button) mView.findViewById(R.id.button);
        final EditText name = (EditText) mView.findViewById(R.id.name);
        final EditText email = (EditText) mView.findViewById(R.id.email);
        final RadioGroup radioGroup = (RadioGroup) mView.findViewById(R.id.radio_group);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton radio = (RadioButton) mView.findViewById(radioGroup.getCheckedRadioButtonId());
                mListener.onLogin(name.getText().toString(), email.getText().toString(), (radio != null ? radio.getText().toString() : null));
            }
        });

        return this.mView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext = activity;

        try {
            mListener = (OnLoginListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnLoginListener {
        public void onLogin(String name, String email, String school);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
