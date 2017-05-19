package com.example.ahmedsaleh.dbse_schools.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ahmedsaleh.dbse_schools.Adapters.EventsAdapter;
import com.example.ahmedsaleh.dbse_schools.Helpers.Co_Working_Space;
import com.example.ahmedsaleh.dbse_schools.Helpers.Event;
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

public class Events extends AppCompatActivity {
   private ListView EventsListView;
    private EventsAdapter eventsAdapter;
    private String result;
    private StringBuilder Url=new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Intent intent=getIntent();
        String workspaceid=intent.getStringExtra("id");
        EventsListView=(ListView)findViewById(R.id.events_list_view);
        ArrayList<Event>arr=new ArrayList<>();
        arr.add(new Event("dreampark","giza,egypt","1"));
        arr.add(new Event("dreampark","giza,egypt","1"));
        arr.add(new Event("dreampark","giza,egypt","1"));
        arr.add(new Event("dreampark","giza,egypt","1"));
        eventsAdapter=new EventsAdapter(this,new ArrayList<>(arr));
        EventsListView.setAdapter(eventsAdapter);
        EventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event =(Event) parent.getItemAtPosition(position);
                Intent intent=new Intent(Events.this,EventProfile.class);
                intent.putExtra("id", event.getmEventId());
                startActivity(intent);
            }
        });
        Url.append(getString(R.string.url)+"workspacesevents/"+workspaceid+"?token="+SignIn.token);
        //connect();

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
                            ArrayList<Event>events=new ArrayList<Event>();
                            JSONArray jsonArray=new JSONArray(result);
                            for(int i=0;i<jsonArray.length();++i)
                            {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                if(jsonObject.has("name")&&!jsonObject.getString("name").equals("null"))
                                {
                                    String event_name=jsonObject.getString("name");
                                    if(jsonObject.has("id"))
                                    {
                                        String event_id=String.valueOf(jsonObject.getInt("id"));
                                        if(jsonObject.has("address")&&!jsonObject.getString("address").equals("null"))
                                        {
                                               String event_address=jsonObject.getString("address");
                                                events.add(new Event(event_name,event_address,event_id));

                                        }

                                    }
                                }
                            }
                            Log.i("tag","size el araaay "+events.size());
                            eventsAdapter.clear();
                            eventsAdapter.addAll(events);
                           eventsAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });



            }
        });

    }
}
