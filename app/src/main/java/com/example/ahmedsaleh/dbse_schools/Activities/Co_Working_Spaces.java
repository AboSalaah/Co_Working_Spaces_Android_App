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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.ahmedsaleh.dbse_schools.Helpers.Co_Working_Space;
import com.example.ahmedsaleh.dbse_schools.R;
import com.example.ahmedsaleh.dbse_schools.Adapters.Co_Working_Spaces_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class Co_Working_Spaces extends AppCompatActivity {
    private StringBuilder Url=new StringBuilder();
    private String result;
    private Co_Working_Spaces_Adapter coWorkingSpacesAdapter;
    private ListView listView;
    public static String userType;
    private Button searchButton;
    private String workspaceMoney=new String();//free or paid
    private String govname;
    private HashMap<String ,String>params;
    private Spinner moneey;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_working_spaces);
        listView=(ListView)findViewById(R.id.co_working_spaces_list_view);
        final Intent intent=getIntent();
        govname=intent.getStringExtra("name");
        searchButton=(Button)findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenSearchDialog();
            }
        });
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
                Intent intent=new Intent(Co_Working_Spaces.this,Co_Working_Space_Profile.class);
                intent.putExtra("id", coWorkingSpace.getmId());
                if(coWorkingSpace.getmId().equals(SignIn.workSpaceId))
                {
                    intent.putExtra("have","true");
                }
                else intent.putExtra("have","false");
                startActivity(intent);
            }
        });
        Url.append(getString(R.string.url)+"workspaceslist/"+govname+"?token="+SignIn.token);
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
                            coWorkingSpacesAdapter.clear();
                            coWorkingSpacesAdapter.addAll(schools);
                            coWorkingSpacesAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });



            }
        });

    }


    void OpenSearchDialog()
    {
        final AlertDialog.Builder mBuilder=new AlertDialog.Builder(Co_Working_Spaces.this,android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        final View mview=getLayoutInflater().inflate(R.layout.search_dialog,null);
        //mBuilder.setTitle(R.string.search);


        final EditText co_working_space_name=(EditText)mview.findViewById(R.id.co_working_space_search_name);
        final EditText co_working_space_city=(EditText)mview.findViewById(R.id.co_working_space_search_city);
        moneey=(Spinner)mview.findViewById(R.id.search_money_spinner);
        ArrayAdapter<CharSequence>types;
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
                Url.append(getString(R.string.url)+"workspacessearch?token="+SignIn.token);
               Toast.makeText(Co_Working_Spaces.this,"OK",Toast.LENGTH_SHORT).show();

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
                Toast.makeText(Co_Working_Spaces.this,"Cancel",Toast.LENGTH_SHORT).show();
            }
        });
        mBuilder.setView(mview);
        AlertDialog dialog=mBuilder.create();
        dialog.show();
    }


    void SearchConnection()
    {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject parameter = null;
        try {
            parameter = new JSONObject(String.valueOf(jsonObject));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                            coWorkingSpacesAdapter.clear();
                            coWorkingSpacesAdapter.addAll(schools);
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
