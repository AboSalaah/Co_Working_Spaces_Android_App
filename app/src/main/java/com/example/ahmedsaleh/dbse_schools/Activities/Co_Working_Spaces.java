package com.example.ahmedsaleh.dbse_schools.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
    public static String userType;
    private Button searchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_working_spaces);
        listView=(ListView)findViewById(R.id.co_working_spaces_list_view);
        final Intent intent=getIntent();
        String govname=intent.getStringExtra("name");
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
        mBuilder.setTitle(R.string.search);


       /* EditText co_working_space_name=(EditText)getActivity().findViewById(R.id.co_working_space_search_name);
        EditText co_working_space_city=(EditText)getActivity().findViewById(R.id.co_working_space_search_city);
        EditText co_working_space_price_from=(EditText)getActivity().findViewById(R.id.co_working_space_search_price_from);
        EditText co_working_space_price_to=(EditText)getActivity().findViewById(R.id.co_working_space_search_price_to);
        CheckBox air_condition=(CheckBox)getActivity().findViewById(R.id.air_condition_check_box);
        CheckBox privaterooms=(CheckBox)getActivity().findViewById(R.id.private_rooms_check_box);
        CheckBox printing_3d=(CheckBox)getActivity().findViewById(R.id.printing_3d_check_box);
        CheckBox pcb_printing=(CheckBox)getActivity().findViewById(R.id.pcb_printing_check_box);
        CheckBox smoking_area=(CheckBox)getActivity().findViewById(R.id.smoking_area_check_box);
        CheckBox girls_area=(CheckBox)getActivity().findViewById(R.id.girls_area_check_box);
        CheckBox cyber=(CheckBox)getActivity().findViewById(R.id.cyber_check_box);
        CheckBox data_show=(CheckBox)getActivity().findViewById(R.id.data_show_check_box);
        CheckBox laser_cutter=(CheckBox)getActivity().findViewById(R.id.laser_cutter_check_box);
        CheckBox cafeteria=(CheckBox)getActivity().findViewById(R.id.cafeteria_checkbox);
        CheckBox wifi=(CheckBox)getActivity().findViewById(R.id.wifi_check_box);
        */

        mBuilder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK butt
                //here i should prepare the url with the search parameters
               Toast.makeText(Co_Working_Spaces.this,"OK",Toast.LENGTH_SHORT).show();


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








}
