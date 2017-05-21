package com.example.ahmedsaleh.dbse_schools.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Events extends AppCompatActivity {
   private ListView EventsListView;
    private EventsAdapter eventsAdapter;
    private String result;
    private StringBuilder Url=new StringBuilder();
    private FloatingActionButton addEventButton;
    private String newEventName;
    private String newEventAddress;
    private String newEventdesc;
    private String workspaceid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Intent intent=getIntent();
        workspaceid=intent.getStringExtra("id");
        EventsListView=(ListView)findViewById(R.id.events_list_view);
        addEventButton=(FloatingActionButton)findViewById(R.id.fab_add_event);
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
        if(SignIn.userType.equals("visitor")||!SignIn.workSpaceId.equals(workspaceid))
        {
            addEventButton.setVisibility(View.GONE);
        }
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAddEventDialog();

            }
        });
        Url.append(getString(R.string.url)+"workspaceevents/"+workspaceid+"?token="+SignIn.token);
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), getString(R.string.connectionproblem),
                                Toast.LENGTH_LONG).show();
                    }
                });

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
    void OpenAddEventDialog()
    {
        Url.setLength(0);
        Url.append(getString(R.string.url)+"event?token="+SignIn.token);
        final AlertDialog.Builder mBuilder=new AlertDialog.Builder(Events.this);
        final View mview=getLayoutInflater().inflate(R.layout.new_event_dialog,null);
        mBuilder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {

                EditText eventname=(EditText)mview.findViewById(R.id.add_event_name_editText);
                 newEventName=eventname.getText().toString();

                EditText eventaddress=(EditText)mview.findViewById(R.id.add_event_address_editText);
                 newEventAddress=eventaddress.getText().toString();

                EditText eventdesc=(EditText)mview.findViewById(R.id.add_event_desc_edittext);
                 newEventdesc=eventdesc.getText().toString();
                // User clicked OK button

                dialog.dismiss();
                AddEventConnection();
            }
        });
        mBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        mBuilder.setView(mview);
        AlertDialog dialog=mBuilder.create();
        dialog.show();
    }
    void AddEventConnection()
    {
        HashMap<String,String> params=new HashMap<>();
        params.put("name",newEventName);
        params.put("address",newEventAddress);
        params.put("description",newEventdesc);
        params.put("workspace_id",workspaceid);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject parameter = new JSONObject(params);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, parameter.toString());
        Request request = new Request.Builder()
                .url(Url.toString())
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), getString(R.string.connectionproblem),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result = response.body().string().toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                           if(jsonObject.has("msg")) {String msg=jsonObject.getString("msg");
                            Toast.makeText(Events.this,msg,Toast.LENGTH_LONG).show();
                            Url.setLength(0);
                            Url.append(getString(R.string.url)+"workspaceevents/"+workspaceid+"?token="+SignIn.token);
                            connect();}
                            if(jsonObject.has("error"))
                            {
                                String msg=jsonObject.getString("error");
                                Toast.makeText(Events.this,msg,Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
