package com.example.ahmedsaleh.dbse_schools.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ahmedsaleh.dbse_schools.Adapters.Governorates_Adapter;
import com.example.ahmedsaleh.dbse_schools.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class TempGovernorates extends AppCompatActivity {

    private StringBuilder Url=new StringBuilder();
    private String result;
    private Governorates_Adapter governoratesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_governorates);
        ListView listView=(ListView)findViewById(R.id.list_view);
        ArrayList<String> arr=new ArrayList<>();
       // Intent intent = getIntent();
       // final String realname=intent.getStringExtra("realname");
        governoratesAdapter =new Governorates_Adapter(this,new ArrayList<String>());
        listView.setAdapter(governoratesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Governorate =(String) parent.getItemAtPosition(position);
                Intent intent=new Intent(TempGovernorates.this,Temp_Co_Working_Spaces.class);
                intent.putExtra("name",Governorate);
               // intent.putExtra("realname",realname);
                startActivity(intent);

            }
        });

        Url.append(getString(R.string.url)+"workspaceslist");
        connect();


    }

    void connect()
    {
        OkHttpClient okHttpClient=new OkHttpClient();
        okhttp3.Request request=new okhttp3.Request.Builder()
                .url(Url.toString())
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result=response.body().string().toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            Log.i("tag","el resulttttt  "+result);
                            ArrayList<String>data=new ArrayList<String>();
                            JSONArray jsonArray=new JSONArray(result);
                            for(int i=0;i<jsonArray.length();++i)
                            {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                if(jsonObject.has("city")&&!jsonObject.getString("city").equals("null"))
                                {

                                    String name=jsonObject.getString("city");
                                    data.add(name);



                                }
                            }
                            governoratesAdapter.clear();
                            governoratesAdapter.addAll(data);
                            governoratesAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });



            }
        });

    }
}
