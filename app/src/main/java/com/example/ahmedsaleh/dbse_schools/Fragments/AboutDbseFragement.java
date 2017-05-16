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
public class AboutDbseFragement extends Fragment {


    TextView description;
    TextView website;
    String result=null;
    StringBuilder URL;


    public AboutDbseFragement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_dbse_fragement, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("About DbSE");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        URL = new StringBuilder(getString(R.string.url)+"about");
        description = (TextView) getActivity().findViewById(R.id.dbse_description);
        website = (TextView) getActivity().findViewById(R.id.dbse_website);
       // connect();

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
                            description.setText(json.getString("description"));
                            website.setText(json.getString("website"));
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                });

            }
        });

    }


}
