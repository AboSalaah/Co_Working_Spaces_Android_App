package com.example.ahmedsaleh.dbse_schools.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedsaleh.dbse_schools.Helpers.Co_Working_Space;
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

public class Co_Working_Space_Profile extends AppCompatActivity {
    private StringBuilder Url=new StringBuilder();
    private String result;
    private double lat; //represent latitude for location on map
    private double lon;//represent longitude for loctaion on map
    private String location="Default"; //represent the city for location on map
    private RatingBar ratingBar;
    private double finalRating;
    public static String userType="def";
    FloatingActionButton editWorkSpace;
    private Button co_working_space_events_button;
    private String havews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_working_space_profile);
        final Intent intent=getIntent();
        final String co_working_spaceid=intent.getStringExtra("id");
        havews=intent.getStringExtra("have");
        editWorkSpace=(FloatingActionButton)findViewById(R.id.fab);
        co_working_space_events_button=(Button)findViewById(R.id.co_working_space_profile_events_button);
        co_working_space_events_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Co_Working_Space_Profile.this,Events.class);
                intent.putExtra("id", co_working_spaceid);
                startActivity(intent);
            }
        });
        if(userType.equals(getString(R.string.wso))||userType.equals(getString(R.string.pwso))||havews.equals("false"))
        {
            editWorkSpace.setVisibility(View.GONE);
        }
        editWorkSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent i = new Intent(Co_Working_Space_Profile.this,SignIn.class);
               // startActivity(i);
            }
        });
        TextView locationonmaplabel=(TextView)findViewById(R.id.co_working_space_profile_location_on_map_label);
        TextView locationonmap=(TextView)findViewById(R.id.co_working_space_profile_location_on_map);
        locationonmaplabel.setText(R.string.locationonmap);
        locationonmap.setText(R.string.your_string_here);
        locationonmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QueryUtils.showLocationOnMap(getApplicationContext(),lat,lon,location);
            }
        });
        ratingBar=(RatingBar)findViewById(R.id.rating_bar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(Co_Working_Space_Profile.this,"rating :"+rating,Toast.LENGTH_SHORT).show();
                finalRating=rating;
            }
        });


        Url.append(getString(R.string.url)+"co_working_space/"+co_working_spaceid+"?token="+SignIn.token);
        // connect();


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
                            TextView co_working_spacenamelabel=(TextView)findViewById(R.id.co_working_space_profile_name_label);
                            TextView co_working_spacename=(TextView)findViewById(R.id.co_working_space_profile_name);
                            if(jsonObject1.has("name")&&!jsonObject1.getString("name").equals("null"))
                            {
                                co_working_spacenamelabel.setText(getString(R.string.name));
                                co_working_spacename.setText(jsonObject1.getString("name"));
                            }
                            else
                            {
                                co_working_spacenamelabel.setVisibility(View.GONE);
                                co_working_spacename.setVisibility(View.GONE);
                            }
                            ImageView co_working_spacelogo=(ImageView)findViewById(R.id.co_working_space_profile_logo);
                            if(jsonObject1.has("logo")&&!jsonObject1.getString("logo").equals("null")&&jsonObject1.getString("logo").contains("storage"))
                            {
                                Picasso.with(getApplicationContext()).load(getString(R.string.imageurl)+jsonObject1.getString("logo")).into(co_working_spacelogo);
                            }
                            else
                            {
                                co_working_spacelogo.setVisibility(View.GONE);
                            }
                            TextView co_working_spacelocationlable=(TextView)findViewById(R.id.co_working_space_profile_location_label);
                            TextView co_working_spacelocation=(TextView)findViewById(R.id.co_working_space_profile_location);
                            if(jsonObject1.has("location")&&!jsonObject1.getString("location").equals("null"))
                            {
                                co_working_spacelocationlable.setText(getString(R.string.locatoin));
                                co_working_spacelocation.setText(jsonObject1.getString("location"));
                                location=jsonObject1.getString("location");
                            }
                            else
                            {
                                co_working_spacelocation.setVisibility(View.GONE);
                                co_working_spacelocationlable.setVisibility(View.GONE);
                            }
                            TextView co_working_spacecontactslabel=(TextView)findViewById(R.id.co_working_space_profile_contacts_label);
                            TextView co_working_spacecontacts=(TextView)findViewById(R.id.co_working_space_profile_contacts);
                            if(jsonObject1.has("contacts")&&!jsonObject1.getString("contacts").equals("null"))
                            {
                                co_working_spacecontactslabel.setText(getString(R.string.contacts));
                                co_working_spacecontacts.setText(QueryUtils.parser(jsonObject1.getString("contacts")));
                            }
                            else
                            {
                                co_working_spacecontacts.setVisibility(View.GONE);
                                co_working_spacecontactslabel.setVisibility(View.GONE);
                            }
                            TextView co_working_spacewebsitelabel=(TextView)findViewById(R.id.co_working_space_profile_website_label);
                            TextView co_working_spacewebsite=(TextView)findViewById(R.id.co_working_space_profile_website);
                            if(jsonObject1.has("website_url")&&!jsonObject1.getString("website_url").equals("null"))
                            {
                                co_working_spacewebsitelabel.setText(getString(R.string.website));
                                co_working_spacewebsite.setText(jsonObject1.getString("website_url"));
                            }
                            else
                            {
                                co_working_spacewebsite.setVisibility(View.GONE);
                                co_working_spacewebsitelabel.setVisibility(View.GONE);
                            }

                            TextView co_working_spacefacebooklabel=(TextView)findViewById(R.id.co_working_space_profile_facebookpage_label);
                            TextView co_working_spacefacebook=(TextView)findViewById(R.id.co_working_space_profile_facebookpage);
                            if(jsonObject1.has("facebook_page")&&!jsonObject1.getString("facebook_page").equals("null"))
                            {
                                co_working_spacefacebooklabel.setText(getString(R.string.facebook_page));
                                co_working_spacefacebook.setText(jsonObject1.getString("facebook_page"));
                            }
                            else
                            {
                                co_working_spacefacebook.setVisibility(View.GONE);
                                co_working_spacefacebooklabel.setVisibility(View.GONE);
                            }
                            TextView co_working_spacedeslable=(TextView)findViewById(R.id.co_working_space_profile_description_label);
                            TextView co_working_spacedes=(TextView)findViewById(R.id.co_working_space_profile_description);
                            if(jsonObject1.has("description")&&!jsonObject1.getString("description").equals("null"))
                            {
                                co_working_spacedes.setText(jsonObject1.getString("description"));
                                co_working_spacedeslable.setText(getString(R.string.description));
                            }
                            else
                            {
                                co_working_spacedes.setVisibility(View.GONE);
                                co_working_spacedeslable.setVisibility(View.GONE);
                            }


                            TextView citylable=(TextView)findViewById(R.id.co_working_space_profile_city_label);
                            TextView city=(TextView)findViewById(R.id.co_working_space_profile_city);
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

                            TextView co_working_spaceclaslabel=(TextView)findViewById(R.id.co_working_space_profile_classification_label);
                            TextView co_working_spaceclas=(TextView)findViewById(R.id.co_working_space_profile_classification);
                            if(jsonObject1.has("classification")&&!jsonObject1.getString("classification").equals("null"))
                            {
                                co_working_spaceclaslabel.setText(getString(R.string.classification));
                                co_working_spaceclas.setText(QueryUtils.parser(jsonObject1.getString("classification")));
                            }
                            else
                            {
                                co_working_spacecontacts.setVisibility(View.GONE);
                                co_working_spacecontactslabel.setVisibility(View.GONE);
                            }
                            TextView co_working_spacefeeslabel=(TextView)findViewById(R.id.co_working_space_profile_fees_label);
                            TextView co_working_spacefees=(TextView)findViewById(R.id.co_working_space_profile_fees);
                            if(jsonObject1.has("fees")&&!jsonObject1.getString("fees").equals("null"))
                            {
                                co_working_spacefeeslabel.setText(getString(R.string.fees));
                                co_working_spacefees.setText(jsonObject1.getString("fees"));
                            }
                            else
                            {
                                co_working_spacefeeslabel.setVisibility(View.GONE);
                                co_working_spacefees.setVisibility(View.GONE);
                            }

                            TextView locationonmaplabel=(TextView)findViewById(R.id.co_working_space_profile_location_on_map_label);
                            TextView locationonmap=(TextView)findViewById(R.id.co_working_space_profile_location_on_map);
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
                            TextView otherslable=(TextView)findViewById(R.id.co_working_space_profile_others_label);
                            TextView others=(TextView)findViewById(R.id.co_working_space_profile_others);
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



                            TextView co_working_spacestatelable=(TextView)findViewById(R.id.co_working_space_profile_state_label);
                            TextView co_working_space_state=(TextView)findViewById(R.id.co_working_space_profile_state);
                            if(jsonObject1.has("state")&&jsonObject1.getString("state").equals("null"))
                            {
                                co_working_spacestatelable.setText(R.string.statelabel);
                                co_working_space_state.setText(jsonObject1.getString("state"));
                            }
                            else
                            {
                                co_working_spacestatelable.setVisibility(View.GONE);
                                co_working_space_state.setVisibility(View.GONE);
                            }

                            TextView co_working_spacetypelable=(TextView)findViewById(R.id.co_working_space_profile_type_label);
                            TextView co_working_space_type=(TextView)findViewById(R.id.co_working_space_profile_type);
                            if(jsonObject1.has("type")&&jsonObject1.getString("type").equals("null"))
                            {
                                co_working_spacetypelable.setText(R.string.typelabel);
                                co_working_space_type.setText(jsonObject1.getString("type"));
                            }
                            else
                            {
                                co_working_spacetypelable.setVisibility(View.GONE);
                                co_working_space_type.setVisibility(View.GONE);
                            }





                            TextView co_working_spaceratelable=(TextView)findViewById(R.id.co_working_space_profile_rate_label);
                            TextView co_working_space_rate=(TextView)findViewById(R.id.co_working_space_profile_rate);
                            if(jsonObject1.has("rate")&&jsonObject1.getString("rate").equals("null"))
                            {
                                co_working_spaceratelable.setText(R.string.statelabel);
                                co_working_space_rate.setText(jsonObject1.getString("rate"));
                            }
                            else {
                                co_working_spaceratelable.setVisibility(View.GONE);
                                co_working_space_rate.setVisibility(View.GONE);
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
