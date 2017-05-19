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
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_profile);
        Intent intent=getIntent();
        EventId=intent.getStringExtra("id");
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

            }
        });
        if(!userType.equals(getString(R.string.wso))&&!userType.equals(getString(R.string.pwso))) {
            event_Desc_Action_Button.setVisibility(View.GONE);
            event_Address_Action_Button.setVisibility(View.GONE);
            event_Name_Action_Button.setVisibility(View.GONE);
            submitEdit.setVisibility(View.GONE);
        }
        Url.append(getString(R.string.url)+"event/"+EventId+"?token="+getString(R.string.token));
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

}
