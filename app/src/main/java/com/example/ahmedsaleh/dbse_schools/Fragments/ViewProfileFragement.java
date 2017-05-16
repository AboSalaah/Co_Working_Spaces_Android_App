package com.example.ahmedsaleh.dbse_schools.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedsaleh.dbse_schools.Activities.SignIn;
import com.example.ahmedsaleh.dbse_schools.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProfileFragement extends Fragment {

    TextView firstLetter;
    String result=null;
    StringBuilder URL;
    public static String userType;

    public ViewProfileFragement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_profile_fragement, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My Profile");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        firstLetter = (TextView) getActivity().findViewById(R.id.View_Profile_textincircle);
        URL = new StringBuilder(getString(R.string.url)+"visitor/"+String.valueOf(SignIn.id)+"?token=");
        URL.append(SignIn.token);
        //connect();

    }


    void connect()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL.toString())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"Connection Failed!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException
            {
                result=response.body().string().toString();
                Log.v("Response code", String.valueOf(response.code()));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        try {
                            JSONObject json =new JSONObject(result);
                            TextView name = (TextView) getActivity().findViewById(R.id.View_Profile_username_header);
                            name.setText(json.getString("name"));
                            TextView mail = (TextView) getActivity().findViewById(R.id.View_Profile_email_header);
                            mail.setText(json.getString("email"));
                            firstLetter.setText(String.valueOf(Character.toUpperCase(json.getString("name").charAt(0))));
                            TextView username = (TextView) getActivity().findViewById(R.id.username_viewprofile2);
                            username.setText(json.get("username").toString());
                            TextView gender = (TextView) getActivity().findViewById(R.id.Gender_viewprofile2);
                            gender.setText(json.get("gender").toString());
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                });

            }
        });

    }
}
