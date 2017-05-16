package com.example.ahmedsaleh.dbse_schools.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.example.ahmedsaleh.dbse_schools.Helpers.Co_Working_Space;
import com.example.ahmedsaleh.dbse_schools.R;
import com.example.ahmedsaleh.dbse_schools.Adapters.Co_Working_Spaces_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.OkHttpClient;

import okhttp3.Callback;
import okhttp3.Response;
public class Co_Working_Spaces extends AppCompatActivity {
    private StringBuilder Url=new StringBuilder();
    private String result;
    private Co_Working_Spaces_Adapter coWorkingSpacesAdapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_working_spaces);
        listView=(ListView)findViewById(R.id.co_working_spaces_list_view);
        final Intent intent=getIntent();
        String govname=intent.getStringExtra("name");
        ArrayList<Co_Working_Space>arr=new ArrayList<>();
        arr.add(new Co_Working_Space("creativo","1",3));
        arr.add(new Co_Working_Space("creativo","1",2));
        arr.add(new Co_Working_Space("creativo","1",1.5f));
        arr.add(new Co_Working_Space("creativo","1",1.4f));
        arr.add(new Co_Working_Space("creativo","1",5));
        arr.add(new Co_Working_Space("creativo","1",4));
        arr.add(new Co_Working_Space("creativo","1",0.5f));
       coWorkingSpacesAdapter =new Co_Working_Spaces_Adapter(this,new ArrayList<Co_Working_Space>(arr));
        listView.setAdapter(coWorkingSpacesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Co_Working_Space coWorkingSpace =(Co_Working_Space)parent.getItemAtPosition(position);
                Intent intent=new Intent(Co_Working_Spaces.this,School_Profile.class);
                intent.putExtra("id", coWorkingSpace.getmId());
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
                                ArrayList<Co_Working_Space> coWorkingSpaces =new ArrayList<Co_Working_Space>();
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
                                                //coWorkingSpaces.add(new Co_Working_Space(schoolname,schoolid,schoollogo));

                                            }
                                        }
                                    }
                                }
                                Log.i("tag","size el araaay "+ coWorkingSpaces.size());
                                coWorkingSpacesAdapter.clear();
                                coWorkingSpacesAdapter.addAll(coWorkingSpaces);
                                coWorkingSpacesAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });



            }
        });

    }






}
