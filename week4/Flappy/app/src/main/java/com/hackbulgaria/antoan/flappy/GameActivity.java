package com.hackbulgaria.antoan.flappy;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;


public class GameActivity extends Activity implements GameFragment.OnGameOverListener, LoginFragment.OnLoginListener {

    private String mName;
    private String mEmail;
    private String mSchool;
    private int mScore;

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
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = LoginFragment.newInstance();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void initGameFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = GameFragment.newInstance();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onLogin(String name, String email, String school) {
        this.mName = name;
        this.mEmail = email;
        this.mSchool = school;

        initGameFragment();
    }

    @Override
    public void onGameOver(int score) {
        mScore = score;
        syncWithDatabase();
    }

    private void syncWithDatabase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
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

                    DefaultHttpClient httpClient = new DefaultHttpClient();
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
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
