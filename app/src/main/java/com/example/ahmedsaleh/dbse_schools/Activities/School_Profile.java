package com.example.ahmedsaleh.dbse_schools.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedsaleh.dbse_schools.Helpers.QueryUtils;
import com.example.ahmedsaleh.dbse_schools.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class School_Profile extends AppCompatActivity {
    StringBuilder Url=new StringBuilder();
    String result;
    private double lat; //represent latitude for location on map
    private double lon;//represent longitude for loctaion on map
    private String location="Default"; //represent the city for location on map
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school__profile);
        final Intent intent=getIntent();
        String schoolid=intent.getStringExtra("id");
        TextView locationonmaplabel=(TextView)findViewById(R.id.school_profile_location_on_map_label);
        TextView locationonmap=(TextView)findViewById(R.id.school_profile_location_on_map);
        locationonmaplabel.setText(R.string.locationonmap);
        locationonmap.setText(R.string.your_string_here);
        locationonmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QueryUtils.showLocationOnMap(getApplicationContext(),lat,lon,location);
            }
        });
       Url.append(getString(R.string.url)+"co_working_space/"+schoolid+"?token="+SignIn.token);
        //connect();

    }

   private void connect()
    {
        OkHttpClient okHttpClient=new OkHttpClient();
        final okhttp3.Request request=new okhttp3.Request.Builder()
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
                            Log.i("tag","resulttttt "+result);
                            JSONObject jsonObject=new JSONObject(result);
                            JSONObject jsonObject1=jsonObject.getJSONObject("co_working_space");
                            TextView schoolnamelabel=(TextView)findViewById(R.id.school_profile_name_label);
                            TextView schoolname=(TextView)findViewById(R.id.school_profile_name);
                            if(jsonObject1.has("name")&&!jsonObject1.getString("name").equals("null"))
                            {
                              schoolnamelabel.setText(getString(R.string.name));
                                schoolname.setText(jsonObject1.getString("name"));
                            }
                            else
                            {
                                schoolnamelabel.setVisibility(View.GONE);
                                schoolname.setVisibility(View.GONE);
                            }
                            ImageView schoollogo=(ImageView)findViewById(R.id.school_profile_logo);
                            if(jsonObject1.has("logo")&&!jsonObject1.getString("logo").equals("null")&&jsonObject1.getString("logo").contains("storage"))
                            {
                                Picasso.with(getApplicationContext()).load(getString(R.string.imageurl)+jsonObject1.getString("logo")).into(schoollogo);
                            }
                            else
                            {
                                schoollogo.setVisibility(View.GONE);
                            }
                            TextView schoollocationlable=(TextView)findViewById(R.id.school_profile_location_label);
                            TextView schoollocation=(TextView)findViewById(R.id.school_profile_location);
                            if(jsonObject1.has("location")&&!jsonObject1.getString("location").equals("null"))
                            {
                                schoollocationlable.setText(getString(R.string.locatoin));
                                schoollocation.setText(jsonObject1.getString("location"));
                                location=jsonObject1.getString("location");
                            }
                            else
                            {
                                schoollocation.setVisibility(View.GONE);
                                schoollocationlable.setVisibility(View.GONE);
                            }
                            TextView schoolcontactslabel=(TextView)findViewById(R.id.school_profile_contacts_label);
                            TextView schoolcontacts=(TextView)findViewById(R.id.school_profile_contacts);
                            if(jsonObject1.has("contacts")&&!jsonObject1.getString("contacts").equals("null"))
                            {
                                schoolcontactslabel.setText(getString(R.string.contacts));
                                schoolcontacts.setText(QueryUtils.parser(jsonObject1.getString("contacts")));
                            }
                            else
                            {
                                schoolcontacts.setVisibility(View.GONE);
                                schoolcontactslabel.setVisibility(View.GONE);
                            }
                            TextView schoolwebsitelabel=(TextView)findViewById(R.id.school_profile_website_label);
                            TextView schoolwebsite=(TextView)findViewById(R.id.school_profile_website);
                            if(jsonObject1.has("website_url")&&!jsonObject1.getString("website_url").equals("null"))
                            {
                                schoolwebsitelabel.setText(getString(R.string.website));
                                schoolwebsite.setText(jsonObject1.getString("website_url"));
                            }
                            else
                            {
                                schoolwebsite.setVisibility(View.GONE);
                                schoolwebsitelabel.setVisibility(View.GONE);
                            }

                            TextView schoolfacebooklabel=(TextView)findViewById(R.id.school_profile_facebookpage_label);
                            TextView schoolfacebook=(TextView)findViewById(R.id.school_profile_facebookpage);
                            if(jsonObject1.has("facebook_page")&&!jsonObject1.getString("facebook_page").equals("null"))
                            {
                                schoolfacebooklabel.setText(getString(R.string.facebook_page));
                                schoolfacebook.setText(jsonObject1.getString("facebook_page"));
                            }
                            else
                            {
                                schoolfacebook.setVisibility(View.GONE);
                                schoolfacebooklabel.setVisibility(View.GONE);
                            }
                            TextView schooldeslable=(TextView)findViewById(R.id.school_profile_description_label);
                            TextView schooldes=(TextView)findViewById(R.id.school_profile_description);
                            if(jsonObject1.has("description")&&!jsonObject1.getString("description").equals("null"))
                            {
                                schooldes.setText(jsonObject1.getString("description"));
                                schooldeslable.setText(getString(R.string.description));
                            }
                            else
                            {
                                schooldes.setVisibility(View.GONE);
                                schooldeslable.setVisibility(View.GONE);
                            }


                            TextView citylable=(TextView)findViewById(R.id.school_profile_city_label);
                            TextView city=(TextView)findViewById(R.id.school_profile_city);
                            if(jsonObject1.has("city")&&!jsonObject1.getString("city").equals("null"))
                            {
                                citylable.setText(getString(R.string.city));
                                city.setText(jsonObject1.getString("city"));
                            }
                            else
                            {
                                city.setVisibility(View.GONE);
                                city.setVisibility(View.GONE);
                            }

                            TextView schoolclaslabel=(TextView)findViewById(R.id.school_profile_classification_label);
                            TextView schoolclas=(TextView)findViewById(R.id.school_profile_classification);
                            if(jsonObject1.has("classification")&&!jsonObject1.getString("classification").equals("null"))
                            {
                                schoolclaslabel.setText(getString(R.string.classification));
                                schoolclas.setText(QueryUtils.parser(jsonObject1.getString("classification")));
                            }
                            else
                            {
                                schoolcontacts.setVisibility(View.GONE);
                                schoolcontactslabel.setVisibility(View.GONE);
                            }
                            TextView schoolfeeslabel=(TextView)findViewById(R.id.school_profile_fees_label);
                            TextView schoolfees=(TextView)findViewById(R.id.school_profile_fees);
                            if(jsonObject1.has("fees")&&!jsonObject1.getString("fees").equals("null"))
                            {
                                schoolfeeslabel.setText(getString(R.string.fees));
                                schoolfees.setText(jsonObject1.getString("fees"));
                            }
                            else
                            {
                                schoolfeeslabel.setVisibility(View.GONE);
                                schoolfees.setVisibility(View.GONE);
                            }

                            TextView locationonmaplabel=(TextView)findViewById(R.id.school_profile_location_on_map_label);
                            TextView locationonmap=(TextView)findViewById(R.id.school_profile_location_on_map);
                            if(jsonObject1.has("x")&&!jsonObject1.getString("x").equals("null")&&jsonObject1.has("y")&&!jsonObject1.getString("y").equals("null"))
                            {
                                lat=jsonObject1.getDouble("x");
                                lon=jsonObject1.getDouble("y");
                            }
                            else
                            {
                                locationonmaplabel.setVisibility(View.GONE);
                                locationonmap.setVisibility(View.GONE);
                            }
                            TextView otherslable=(TextView)findViewById(R.id.school_profile_others_label);
                            TextView others=(TextView)findViewById(R.id.school_profile_others);
                            if(jsonObject1.has("others")&&jsonObject1.getString("others").equals("null"))
                            {
                                otherslable.setText(R.string.others);
                                others.setText(jsonObject1.getString("others"));
                            }
                            else
                            {
                                others.setVisibility(View.GONE);
                                otherslable.setVisibility(View.GONE);
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
