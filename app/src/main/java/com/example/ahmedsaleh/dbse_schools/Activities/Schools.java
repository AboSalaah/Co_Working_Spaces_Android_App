package com.example.ahmedsaleh.dbse_schools.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.example.ahmedsaleh.dbse_schools.R;
import com.example.ahmedsaleh.dbse_schools.Helpers.School;
import com.example.ahmedsaleh.dbse_schools.Adapters.SchoolAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.OkHttpClient;

import okhttp3.Callback;
import okhttp3.Response;
public class Schools extends AppCompatActivity {
    private StringBuilder Url=new StringBuilder();
    private String result;
    private SchoolAdapter schoolAdapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schools);
        listView=(ListView)findViewById(R.id.schools_list_view);
        final Intent intent=getIntent();
        String govname=intent.getStringExtra("name");
        ArrayList<School>arr=new ArrayList<>();
       arr.add(new School("radwa","1"));
       schoolAdapter=new SchoolAdapter(this,new ArrayList<School>());
        listView.setAdapter(schoolAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                School school=(School)parent.getItemAtPosition(position);
                Intent intent=new Intent(Schools.this,School_Profile.class);
                intent.putExtra("id",school.getmId());
                startActivity(intent);
            }
        });
        Url.append(getString(R.string.url)+"schoollocation/"+govname+"?token="+SignIn.token);
        //connect();
    }

    /**
     * Function to make the connection to get the desired schools data and update the UI
     * @returns void
     * @author Ahmed Saleh
     */
    void connect()
    {
        OkHttpClient okHttpClient=new OkHttpClient();
        okhttp3.Request request=new okhttp3.Request.Builder()
                .url(Url.toString())
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getApplicationContext(), getString(R.string.connectionproblem),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                     result=response.body().string().toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.i("tag","resultttt  "+result);
                                ArrayList<School>schools=new ArrayList<School>();
                                JSONArray jsonArray=new JSONArray(result);
                                for(int i=0;i<jsonArray.length();++i)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    if(jsonObject.has("name")&&!jsonObject.getString("name").equals("null"))
                                    {
                                        String schoolname=jsonObject.getString("name");
                                        if(jsonObject.has("id"))
                                        {
                                            String schoolid=String.valueOf(jsonObject.getInt("id"));
                                            if(jsonObject.has("logo")&&!jsonObject.getString("logo").equals("null")&&jsonObject.getString("logo").contains("storage"))
                                            {
                                                String schoollogo=getString(R.string.imageurl)+jsonObject.getString("logo");
                                                schools.add(new School(schoolname,schoolid,schoollogo));

                                            }
                                        }
                                    }
                                }
                                Log.i("tag","size el araaay "+schools.size());
                                schoolAdapter.clear();
                                schoolAdapter.addAll(schools);
                                schoolAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });



            }
        });

    }






}
