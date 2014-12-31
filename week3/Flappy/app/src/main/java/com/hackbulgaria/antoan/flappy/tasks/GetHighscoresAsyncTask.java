package com.hackbulgaria.antoan.flappy.tasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.hackbulgaria.antoan.flappy.constants.Constants;
import com.hackbulgaria.antoan.flappy.wrappers.UserWrapper;
import com.hackbulgaria.antoan.flappy.listeners.IHighScoresLoadedListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Antoan on 30-Dec-14.
 */
public class GetHighscoresAsyncTask extends AsyncTask<Void, Void, UserWrapper[]> {

    private IHighScoresLoadedListener mHighscoresLoaded;
    private Context mAppContext;

    public GetHighscoresAsyncTask(Context appContext, IHighScoresLoadedListener highScoresLoaded) {
        mHighscoresLoaded = highScoresLoaded;
        mAppContext = appContext;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mAppContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected UserWrapper[] doInBackground(Void... params) {

        if(!isNetworkAvailable()) {
            return null;
        }

        try {
            String url = Constants.API_SCORES;

            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(url);

            HttpResponse response = client.execute(get);

            String responseText = EntityUtils.toString(response.getEntity());

            JSONObject obj = new JSONObject(responseText);
            JSONArray array = obj.getJSONArray("scores");

            UserWrapper[] users = new UserWrapper[array.length()];
            for(int i=0; i<array.length(); i++) {
                JSONObject element = array.getJSONObject(i);
                users[i] = new UserWrapper(element.getInt("id"), element.getString("name"), element.getInt("score"));
            }

            return users;

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(UserWrapper[] users) {
        mHighscoresLoaded.OnHighscoresLoaded(users);
    }
}
