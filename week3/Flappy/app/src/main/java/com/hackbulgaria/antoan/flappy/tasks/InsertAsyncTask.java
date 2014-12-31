package com.hackbulgaria.antoan.flappy.tasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.AsyncTask;

import com.hackbulgaria.antoan.flappy.constants.Constants;
import com.hackbulgaria.antoan.flappy.wrappers.UserWrapper;
import com.hackbulgaria.antoan.flappy.listeners.IScoreInsertedListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Antoan on 30-Dec-14.
 */
public class InsertAsyncTask extends AsyncTask<Void, Void, UserWrapper> {
    private IScoreInsertedListener mListener;
    private Context mAppContext;
    private UserWrapper mUser;

    public InsertAsyncTask(Context context, UserWrapper user, IScoreInsertedListener listener) {
        mAppContext = context;
        mUser = user;
        mListener = listener;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mAppContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected UserWrapper doInBackground(Void... params) {

        if(!isNetworkAvailable()) {
            return null;
        }

        try {
            JSONObject jsonObject = new JSONObject();

            if (mUser.getId() != 0) {
                jsonObject.put("id", mUser.getId());
            }

            jsonObject.put("name", mUser.getName());
            jsonObject.put("score", mUser.getScore());

            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(Constants.API_INSERT);
            StringEntity entity = new StringEntity(jsonObject.toString());
            entity.setContentType("application/json");

            post.setEntity(entity);
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "application/json");
            HttpResponse httpresponse = client.execute(post);

            String responseText = EntityUtils.toString(httpresponse.getEntity());
            JSONObject obj = new JSONObject(responseText);

            try {
                int insertId = obj.getInt("insertId");
                mUser.setId(insertId);
                return mUser;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (ParseException e) {
            e.printStackTrace();
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
    protected void onPostExecute(UserWrapper user) {
        mListener.OnScoreInserted(user);
    }
}
