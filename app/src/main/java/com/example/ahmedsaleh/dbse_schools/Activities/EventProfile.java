package com.example.ahmedsaleh.dbse_schools.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedsaleh.dbse_schools.Helpers.Co_Working_Space;
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

public class EventProfile extends AppCompatActivity {
    private FloatingActionButton event_Name_Action_Button;
    private FloatingActionButton event_Address_Action_Button;
    private FloatingActionButton event_Desc_Action_Button;
    private String EventId;
    private TextView eventname;
    private TextView eventaddress;
    private TextView eventdesc;
    private EditText eventnamee;
    private EditText eventaddresss;
    private EditText eventdescc;
    private String previouseventname;
    private String previouseventaddress;
    private String previouseventdesc;
    private String finaleventname;
    private String finaleventaddress;
    private String finaleventdesc;
    public static String userType="def";
    private Button submitEdit;
    private StringBuilder Url=new StringBuilder();
    private String result;
    private HashMap<String,String>params=new HashMap<>();
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_profile);
        Intent intent=getIntent();
        EventId=intent.getStringExtra("id");
        url=getString(R.string.url);
        eventname=(TextView)findViewById(R.id.event_profile_name);
        eventaddress=(TextView)findViewById(R.id.event_profile_address);
        eventdesc=(TextView)findViewById(R.id.event_profile_desc);
        eventnamee=(EditText)findViewById(R.id.event_profile_name_edit_text);
        eventaddresss=(EditText)findViewById(R.id.event_profile_address_edit_text);
        eventdescc=(EditText)findViewById(R.id.event_profile_desc_edit_text);
        event_Name_Action_Button=(FloatingActionButton)findViewById(R.id.fab_event_name);
        event_Address_Action_Button=(FloatingActionButton)findViewById(R.id.fab_event_address);
        event_Desc_Action_Button=(FloatingActionButton)findViewById(R.id.fab_event_desc);
        submitEdit=(Button)findViewById(R.id.sumbit_edit_event_button);
        event_Name_Action_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previouseventname=eventname.getText().toString();

                eventname.setVisibility(View.GONE);

                eventnamee.setVisibility(View.VISIBLE);
                eventnamee.setText(previouseventname);
            }
        });

        event_Address_Action_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previouseventaddress=eventaddress.getText().toString();
                eventaddress.setVisibility(View.GONE);
                eventaddresss.setVisibility(View.VISIBLE);
                eventaddresss.setText(previouseventaddress);

            }
        });

        event_Desc_Action_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previouseventdesc=eventdesc.getText().toString();
                eventdesc.setVisibility(View.GONE);
                eventdescc.setVisibility(View.VISIBLE);
                eventdescc.setText(previouseventdesc);
            }
        });
        submitEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ok=true;
                if(eventnamee.getVisibility()!=View.GONE)
                {
                    if(!eventnamee.getText().toString().isEmpty())
                    finaleventname=eventnamee.getText().toString();
                    else
                    {
                       eventnamee.setError("Event Name "+(getString(R.string.emptyerror)));
                        ok=false;
                    }
                }
                else
                {
                    finaleventname=eventname.getText().toString();
                }
                if(eventaddresss.getVisibility()!=View.GONE)
                {
                    if(!eventaddresss.getText().toString().isEmpty())
                    finaleventaddress=eventaddresss.getText().toString();
                    else
                    {
                        eventaddresss.setError("Event Address "+(getString(R.string.emptyerror)));
                        ok=false;
                    }
                }
                else
                {
                    finaleventaddress=eventaddress.getText().toString();
                }
                if(eventdescc.getVisibility()!=View.GONE)
                {
                    if(!eventdescc.getText().toString().isEmpty())
                    finaleventdesc=eventdescc.getText().toString();
                    else
                    {
                        eventdescc.setError("Event Description "+(getString(R.string.emptyerror)));
                        ok=false;
                    }

                }
                else
                {
                    finaleventdesc=eventdesc.getText().toString();
                }
                if(ok) {
                    params.clear();
                    params.put("name", finaleventname);
                    params.put("address", finaleventaddress);
                    params.put("description", finaleventdesc);
                    eventnamee.setVisibility(View.GONE);
                    eventaddresss.setVisibility(View.GONE);
                    eventdescc.setVisibility(View.GONE);
                    eventname.setVisibility(View.VISIBLE);
                    eventaddress.setVisibility(View.VISIBLE);
                    eventdesc.setVisibility(View.VISIBLE);
                    eventname.setText(finaleventname);
                    eventaddress.setText(finaleventaddress);
                    eventdesc.setText(finaleventdesc);
                    Url.setLength(0);
                    Url.append(url+"event/"+EventId+"?token="+SignIn.token);
                    EditEventConnection();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Fill The Empty Fields",Toast.LENGTH_LONG).show();
                }

            }
        });
        if(!SignIn.userType.equals(getString(R.string.wso))&&!SignIn.userType.equals(getString(R.string.pwso))) {
            event_Desc_Action_Button.setVisibility(View.GONE);
            event_Address_Action_Button.setVisibility(View.GONE);
            event_Name_Action_Button.setVisibility(View.GONE);
            submitEdit.setVisibility(View.GONE);
        }
        Url.append(url+"event/"+EventId+"?token="+SignIn.token);
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

                            JSONObject jsonObject=new JSONObject(result);
                            eventname.setText(jsonObject.getString("name"));
                            eventaddress.setText(jsonObject.getString("address"));
                            eventdesc.setText(jsonObject.getString("description"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });



            }
        });

    }
    private void EditEventConnection()
    {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject parameter = new JSONObject(params);
        Log.i("mytageventprofil",parameter.toString());
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, parameter.toString());
        Request request = new Request.Builder()
                .url(Url.toString())
                .put(body)
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
                            JSONObject json = new JSONObject(result);
                            String error = json.get("error").toString();
                            Toast.makeText(EventProfile.this, error, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            Toast.makeText(EventProfile.this, "Editing Saved", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }
                });

            }
        });

    }

}
