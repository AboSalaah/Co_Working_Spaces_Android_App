package com.example.ahmedsaleh.dbse_schools.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ahmedsaleh.dbse_schools.Adapters.Co_Working_Spaces_Adapter;
import com.example.ahmedsaleh.dbse_schools.Helpers.Co_Working_Space;
import com.example.ahmedsaleh.dbse_schools.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Temp_Co_Working_Spaces extends AppCompatActivity {

    private StringBuilder Url=new StringBuilder();
    private String result;
    private String workspaceMoney=new String();//free or paid
    private Co_Working_Spaces_Adapter coWorkingSpaceAdapter;
    private ListView listView;
    private String confirmCode;
    private Map<String,String> params;
    private EditText confirmCodeEditText;
    private Button searchButton;
    public static String userType;
    public static String realName=new String();
    public static String phoneNumber=new String();
    public static String gender=new String();
    public static String email=new String();
    public static String username=new String();
    public static String password=new String();
    private JSONObject jsonObject;
    private Spinner moneey;
    String govname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp__co__working__spaces);
        listView=(ListView)findViewById(R.id.temp_co_working_spaces_list_view);
        final Intent intent=getIntent();
        govname=intent.getStringExtra("name");
        final String realname=intent.getStringExtra("realname");
        searchButton=(Button)findViewById(R.id.temp_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenSearchDialog();
            }
        });
        ArrayList<Co_Working_Space> arr=new ArrayList<>();

        coWorkingSpaceAdapter=new Co_Working_Spaces_Adapter(this,new ArrayList<Co_Working_Space>());
        listView.setAdapter(coWorkingSpaceAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Co_Working_Space coWorkingSpace=(Co_Working_Space) parent.getItemAtPosition(position);
                //here i'll send to nasr the request
                if(!coWorkingSpaceAdapter.isEmpty())
                {
                    Url.setLength(0);
                    Url.append(getString(R.string.url)+"workspaceverfiy");
                    params=new HashMap<String, String>();
                    params.put("workspace_id",coWorkingSpace.getmId());
                    connectoToVerfifyWorkSpace();
                    //verifyWorkspaceEmail();
                }
            }
        });
        Url.append(getString(R.string.url)+"workspacelist/"+govname);
        connect();
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
                            ArrayList<Co_Working_Space>schools=new ArrayList<Co_Working_Space>();
                            JSONArray jsonArray=new JSONArray(result);
                            for(int i=0;i<jsonArray.length();++i)
                            {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                if(jsonObject.has("name")&&!jsonObject.getString("name").equals("null"))
                                {
                                    String co_working_space_name=jsonObject.getString("name");
                                    if(jsonObject.has("id"))
                                    {
                                        String co_working_space_id=String.valueOf(jsonObject.getInt("id"));
                                        if(jsonObject.has("logo")&&!jsonObject.getString("logo").equals("null")&&jsonObject.getString("logo").contains("storage"))
                                        {
                                            String co_working_space_logo=getString(R.string.imageurl)+jsonObject.getString("logo");
                                            if(jsonObject.has("rate"))
                                            {
                                                double co_working_space_rate=jsonObject.getDouble("rate");
                                                schools.add(new Co_Working_Space(co_working_space_name,co_working_space_id,co_working_space_logo,(float)co_working_space_rate));
                                            }
                                        }

                                    }
                                }
                            }
                            Log.i("tag","size el araaay "+schools.size());
                            coWorkingSpaceAdapter.clear();
                            coWorkingSpaceAdapter.addAll(schools);
                            coWorkingSpaceAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });



            }
        });

    }
    void connectoToVerfifyWorkSpace()
    {
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
                Log.v("Response code", String.valueOf(response.code()));
                //el response mn naaasr
                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        try {
                            JSONObject json = new JSONObject(result);
                            confirmCode = json.get("code").toString();
                            Toast.makeText(Temp_Co_Working_Spaces.this, json.get("msg").toString(), Toast.LENGTH_LONG).show();
                            verifyWorkspaceEmail();

                        } catch (JSONException e) {
                            Toast.makeText(Temp_Co_Working_Spaces.this,"sending to this email failed!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }
                });


            }
        });
    }


    private void moveToSignInActivity(){
        Intent i = new Intent(Temp_Co_Working_Spaces.this,SignIn.class);
        startActivity(i);
    }

    private void verifyWorkspaceEmail()
    {
        final AlertDialog.Builder mBuilder=new AlertDialog.Builder(Temp_Co_Working_Spaces.this);
        final View mview=getLayoutInflater().inflate(R.layout.codeinputdialog,null);
        mBuilder.setTitle(R.string.confirmworkingspaceemail);
        mBuilder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                confirmCodeEditText = (EditText) mview.findViewById(R.id.confirmation_code_editText);
                String mystr = confirmCodeEditText.getText().toString();
                if(mystr.equals(confirmCode)) {
                    Url.setLength(0);
                    Url.append(getString(R.string.url)+"signup");
                    dialog.dismiss();
                       connectToPost();
                } else {

                    Toast.makeText(Temp_Co_Working_Spaces.this, "Wrong Code", Toast.LENGTH_LONG).show();
                }
               // dialog.dismiss();
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


    void connectToPost() {
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
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.v("responsehhhhhhhhh", call.request().body().toString());
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Temp_Co_Working_Spaces.this,"Connection Failed!", Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {
                result = response.body().string().toString();
                Log.v("Response code", String.valueOf(response.code()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONObject json = new JSONObject(result);
                            String msg = json.get("msg").toString();
                            Toast.makeText(Temp_Co_Working_Spaces.this, msg, Toast.LENGTH_LONG).show();
                            //
                            moveToSignInActivity();

                            finish();
                        } catch (JSONException e) {
                            Toast.makeText(Temp_Co_Working_Spaces.this, "Registeration Failed!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }

    void OpenSearchDialog()
    {
        final AlertDialog.Builder mBuilder=new AlertDialog.Builder(Temp_Co_Working_Spaces.this,android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        final View mview=getLayoutInflater().inflate(R.layout.search_dialog,null);
        //mBuilder.setTitle(R.string.search);


        final EditText co_working_space_name=(EditText)mview.findViewById(R.id.co_working_space_search_name);
        final EditText co_working_space_city=(EditText)mview.findViewById(R.id.co_working_space_search_city);
        moneey=(Spinner)mview.findViewById(R.id.search_money_spinner);
        ArrayAdapter<CharSequence> types;
        types=ArrayAdapter.createFromResource(this,R.array.money,android.R.layout.simple_spinner_item);
        types.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moneey.setAdapter(types);
        moneey.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item=(String)parent.getItemAtPosition(position);
                if(item.equals(getString(R.string.free)))workspaceMoney=getString(R.string.free);
                else if(item.equals(getString(R.string.paid)))workspaceMoney=getString(R.string.paid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                workspaceMoney="";
            }
        });
        final CheckBox air_condition=(CheckBox)mview.findViewById(R.id.air_condition_check_box);
        final CheckBox privaterooms=(CheckBox)mview.findViewById(R.id.private_rooms_check_box);
        final CheckBox printing_3d=(CheckBox)mview.findViewById(R.id.printing_3d_check_box);
        final CheckBox pcb_printing=(CheckBox)mview.findViewById(R.id.pcb_printing_check_box);
        final CheckBox smoking_area=(CheckBox)mview.findViewById(R.id.smoking_area_check_box);
        final CheckBox girls_area=(CheckBox)mview.findViewById(R.id.girls_area_check_box);
        final CheckBox cyber=(CheckBox)mview.findViewById(R.id.cyber_check_box);
        final CheckBox data_show=(CheckBox)mview.findViewById(R.id.data_show_check_box);
        final CheckBox laser_cutter=(CheckBox)mview.findViewById(R.id.laser_cutter_check_box);
        final CheckBox cafeteria=(CheckBox)mview.findViewById(R.id.cafeteria_checkbox);
        final CheckBox wifi=(CheckBox)mview.findViewById(R.id.wifi_check_box);

        mBuilder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK butt
                //here i should prepare the url with the search parameters
                Url.setLength(0);
                Url.append(getString(R.string.url)+"workspacesearch");
                Toast.makeText(Temp_Co_Working_Spaces.this,"OK",Toast.LENGTH_SHORT).show();

                jsonObject=new JSONObject();
                if(!co_working_space_name.getText().toString().isEmpty())
                {
                    try {
                        jsonObject.put("name",co_working_space_name.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if(!co_working_space_city.getText().toString().isEmpty()){


                    try {
                        jsonObject.put("city",co_working_space_city.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                try {


                    if (air_condition.isChecked()) {

                        jsonObject.put("air_conditioning", 1);
                    }
                    if (privaterooms.isChecked()) {

                        jsonObject.put("private_rooms",1);
                    }
                    if (data_show.isChecked())
                    {
                        jsonObject.put("data_show", 1);
                    }
                    if (wifi.isChecked())
                    {
                        jsonObject.put("wifi", 1);
                    }
                    if (laser_cutter.isChecked()) {

                        jsonObject.put("laser_cutter",1);
                    }
                    if (printing_3d.isChecked())
                    {

                        jsonObject.put("printing_3D", 1);
                    }
                    if (pcb_printing.isChecked()) {

                        jsonObject.put("PCB_printing", "1");
                    }
                    if (girls_area.isChecked())
                    {
                        jsonObject.put("girls_area", 1);
                    }
                    if (smoking_area.isChecked())
                    {
                        jsonObject.put("smoking_area",1);
                    }
                    if (cafeteria.isChecked())
                    {
                        jsonObject.put("cafeteria", 1);
                    }
                    if (cyber.isChecked())
                    {
                        jsonObject.put("cyber", 1);
                    }

                    if (!workspaceMoney.isEmpty()) {

                        jsonObject.put("type", workspaceMoney);
                    }

                    jsonObject.put("state", govname);
                    SearchConnection();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        });
        mBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                Toast.makeText(Temp_Co_Working_Spaces.this,"Cancel",Toast.LENGTH_SHORT).show();
            }
        });
        mBuilder.setView(mview);
        AlertDialog dialog=mBuilder.create();
        dialog.show();
    }


    void SearchConnection()
    {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject parameter = new JSONObject(params);
        Log.i("my tag",parameter.toString());
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Log.i("my tagggg",jsonObject.toString());
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
                            Log.i("tag","resultttt  "+result);
                            ArrayList<Co_Working_Space>schools=new ArrayList<Co_Working_Space>();
                            JSONArray jsonArray=new JSONArray(result);
                            for(int i=0;i<jsonArray.length();++i)
                            {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                if(jsonObject.has("name")&&!jsonObject.getString("name").equals("null"))
                                {
                                    String co_working_space_name=jsonObject.getString("name");
                                    if(jsonObject.has("id"))
                                    {
                                        String co_working_space_id=String.valueOf(jsonObject.getInt("id"));
                                        if(jsonObject.has("logo")&&!jsonObject.getString("logo").equals("null")&&jsonObject.getString("logo").contains("storage"))
                                        {
                                            String co_working_space_logo=getString(R.string.imageurl)+jsonObject.getString("logo");
                                            if(jsonObject.has("rate"))
                                            {
                                                double co_working_space_rate=jsonObject.getDouble("rate");
                                                schools.add(new Co_Working_Space(co_working_space_name,co_working_space_id,co_working_space_logo,(float)co_working_space_rate));
                                            }
                                        }

                                    }
                                }
                            }
                            Log.i("tag","size el araaay "+schools.size());
                            coWorkingSpaceAdapter.clear();
                            coWorkingSpaceAdapter.addAll(schools);
                            coWorkingSpaceAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }



}
