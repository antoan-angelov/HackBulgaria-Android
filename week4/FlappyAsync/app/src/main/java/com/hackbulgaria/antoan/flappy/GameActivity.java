package com.hackbulgaria.antoan.flappy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class GameActivity extends Activity implements GameFragment.OnGameOverListener, LoginFragment.OnLoginListener {

    public final static int FRAGMENT_LOGIN = 1;
    public final static int FRAGMENT_GAMEPLAY = 2;

    private String mName;
    private String mEmail;
    private String mSchool;
    private int mScore;
    private int mCurrentFragmentId;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

        initLoginFragment();
    }

    private void initLoginFragment() {
        mCurrentFragmentId = FRAGMENT_LOGIN;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mCurrentFragment = LoginFragment.newInstance();

        fragmentTransaction.replace(R.id.fragment_container, mCurrentFragment);
        fragmentTransaction.commit();
    }

    private void initGameFragment() {
        mCurrentFragmentId = FRAGMENT_GAMEPLAY;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mCurrentFragment = GameFragment.newInstance();

        fragmentTransaction.replace(R.id.fragment_container, mCurrentFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onLogin(String name, String email, String school) {
        this.mName = name;
        this.mEmail = email;
        this.mSchool = school;
        mScore = 0;

        new SendUserInfoAsyncTask().execute();
    }

    @Override
    public void onGameOver(int score) {
        mScore = score;
        new SendUserInfoAsyncTask().execute();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return (ni != null);
    }

    public class SendUserInfoAsyncTask extends AsyncTask<Void, Void, Void> {

        private AlertDialog.Builder mBuilder;

        @Override
        protected Void doInBackground(Void... urls) {

            if(!isNetworkConnected()) {
                showAlert("No internet connection", "Please enable mobile internet or Wi-Fi.");
                return null;
            }

            URL url = null;
            try {
                url = new URL("http://95.111.103.224:8080/Flappy/scores");
                HttpPut httpPut = new HttpPut(url.toString());

                JSONObject json = new JSONObject();
                json.put("name", mName);
                json.put("mail", mEmail);
                json.put("whereFrom", mSchool);
                json.put("score", mScore);

                StringEntity entity = new StringEntity(json.toString());
                entity.setContentType("application/json");
                httpPut.setEntity(entity);

                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
                DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
                httpClient.execute(httpPut);

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GameActivity.this, "Score uploaded successfully!", Toast.LENGTH_LONG).show();
                    }
                });

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                showAlert("A problem occurred", "There was a problem with the request. Please try again later.");
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(mBuilder != null) {
                ((LoginFragment) mCurrentFragment).hideProgressBar();
                mBuilder.show();
            }
            else if(mCurrentFragmentId == FRAGMENT_LOGIN) {
                initGameFragment();
            }
        }

        private void showAlert(final String title, final String message) {
            mBuilder = new AlertDialog.Builder(GameActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null);
        }
    }
}
