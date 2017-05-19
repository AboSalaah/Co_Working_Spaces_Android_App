package com.example.ahmedsaleh.dbse_schools.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ahmedsaleh.dbse_schools.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Edit_CoWorkingSpace extends AppCompatActivity {

    EditText nameEditText;
    EditText linkVideoEditText;
    EditText contactsEditText;
    EditText websiteEditText;
    EditText facebookEditText;
    EditText descriptionEditText;
    EditText cityEditText;
    EditText classificationEditText;
    EditText feesEditText;
    EditText locationOnMapEditText;
    CheckBox airConditioningCheckbox;
    CheckBox privateRoomsCheckbox;
    CheckBox datashowCheckbox;
    CheckBox wifiCheckbox;
    CheckBox laserCutterCheckbox;
    CheckBox printing3DCheckbox;
    CheckBox PCBPrintingCheckbox;
    CheckBox girlsAreasCheckbox;
    CheckBox cafeteriaCheckbox;
    CheckBox cyberCheckbox;


    private StringBuilder Url=new StringBuilder();
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__co_working_space);
         nameEditText = (EditText) findViewById(R.id.Workspace_name_editText);
         linkVideoEditText = (EditText) findViewById(R.id.Link_Video_editText);
         contactsEditText = (EditText) findViewById(R.id.Workspace_contacts_editText);
         websiteEditText = (EditText) findViewById(R.id.Workspace_website_editText);
         facebookEditText = (EditText) findViewById(R.id.Workspace_facebook_editText);
         descriptionEditText = (EditText) findViewById(R.id.Workspace_description_editText);
         cityEditText = (EditText) findViewById(R.id.Workspace_city_editText);
         classificationEditText = (EditText) findViewById(R.id.Workspace_classification_editText);
         feesEditText = (EditText) findViewById(R.id.Workspace_fees_editText);
         locationOnMapEditText = (EditText) findViewById(R.id.Workspace_locationOnMap_editText);
         airConditioningCheckbox = (CheckBox) findViewById(R.id.air_condition_check_box_editprofile);
         privateRoomsCheckbox = (CheckBox) findViewById(R.id.private_rooms_check_box_editprofile);
         datashowCheckbox = (CheckBox) findViewById(R.id.data_show_check_box_editprofile);
         wifiCheckbox = (CheckBox) findViewById(R.id.wifi_check_box_editprofile);
         laserCutterCheckbox = (CheckBox) findViewById(R.id.laser_cutter_check_box_editprofile);
         printing3DCheckbox = (CheckBox) findViewById(R.id.printing_3d_check_box_editprofile);
         PCBPrintingCheckbox = (CheckBox) findViewById(R.id.pcb_printing_check_box_editprofile);
         girlsAreasCheckbox = (CheckBox) findViewById(R.id.girls_area_check_box_editprofile);
         cafeteriaCheckbox = (CheckBox) findViewById(R.id.cafeteria_checkbox_editprofile);
         cyberCheckbox = (CheckBox) findViewById(R.id.cyber_check_box_editprofile);


        Url = new StringBuilder(getString(R.string.url)+SignIn.type+String.valueOf(SignIn.id)+"?token=");
        Url.append(SignIn.token);

       // connectToGet();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save_edit_workspace) {
            //connectToPost();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void connectToGet()
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
                            nameEditText.setText(jsonObject.get("name").toString());
                            linkVideoEditText.setText(jsonObject.get("name").toString());
                            contactsEditText.setText(jsonObject.get("contacts").toString());
                            websiteEditText.setText(jsonObject.get("website_url").toString());
                            facebookEditText.setText(jsonObject.get("facebook_page").toString());
                            descriptionEditText.setText(jsonObject.get("description").toString());
                            cityEditText.setText(jsonObject.get("name").toString());
                            classificationEditText.setText(jsonObject.get("classification").toString());
                            feesEditText.setText(jsonObject.get("name").toString());
                            locationOnMapEditText.setText(jsonObject.get("name").toString());

                            if(Integer.parseInt(jsonObject.get("air_conditioning").toString()) == 1)airConditioningCheckbox.toggle();
                            if(Integer.parseInt(jsonObject.get("private_rooms").toString()) == 1)privateRoomsCheckbox.toggle();
                            if(Integer.parseInt(jsonObject.get("data_show").toString()) == 1)datashowCheckbox.toggle();
                            if(Integer.parseInt(jsonObject.get("wifi").toString()) == 1)wifiCheckbox.toggle();
                            if(Integer.parseInt(jsonObject.get("laser_cutter").toString()) == 1)laserCutterCheckbox.toggle();
                            if(Integer.parseInt(jsonObject.get("printing_3D").toString()) == 1)printing3DCheckbox.toggle();
                            if(Integer.parseInt(jsonObject.get("PCB_printing").toString()) == 1)PCBPrintingCheckbox.toggle();
                            if(Integer.parseInt(jsonObject.get("girls_area").toString()) == 1)girlsAreasCheckbox.toggle();
                            if(Integer.parseInt(jsonObject.get("smoking_area").toString()) == 1)cafeteriaCheckbox.toggle();
                            if(Integer.parseInt(jsonObject.get("cyber").toString()) == 1)cyberCheckbox.toggle();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });



            }
        });

    }

    void connectToPost() {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", String.valueOf(nameEditText.getText()));
        params.put("name", String.valueOf(linkVideoEditText.getText()));
        params.put("contacts", String.valueOf(contactsEditText.getText()));
        params.put("website_url", String.valueOf(websiteEditText.getText()));
        params.put("facebook_page", String.valueOf(facebookEditText.getText()));
        params.put("description", String.valueOf(descriptionEditText.getText()));
        params.put("name", String.valueOf(cityEditText.getText()));
        params.put("classification", String.valueOf(classificationEditText.getText()));
        params.put("name", String.valueOf(feesEditText.getText()));
        params.put("name", String.valueOf(locationOnMapEditText.getText()));


        if(airConditioningCheckbox.isChecked())params.put("air_conditioning","1"); else params.put("air_conditioning","0");
        if(airConditioningCheckbox.isChecked())params.put("private_rooms","1"); else params.put("private_rooms","0");
        if(airConditioningCheckbox.isChecked())params.put("data_show","1"); else params.put("data_show","0");
        if(airConditioningCheckbox.isChecked())params.put("wifi","1"); else params.put("wifi","0");
        if(airConditioningCheckbox.isChecked())params.put("laser_cutter","1"); else params.put("laser_cutter","0");
        if(airConditioningCheckbox.isChecked())params.put("printing_3D","1"); else params.put("printing_3D","0");
        if(airConditioningCheckbox.isChecked())params.put("PCB_printing","1"); else params.put("PCB_printing","0");
        if(airConditioningCheckbox.isChecked())params.put("girls_area","1"); else params.put("girls_area","0");
        if(airConditioningCheckbox.isChecked())params.put("smoking_area","1"); else params.put("smoking_area","0");
        if(airConditioningCheckbox.isChecked())params.put("cyber","1"); else params.put("cyber","0");

        params.put("id", String.valueOf(SignIn.id));
        JSONObject parameter = new JSONObject(params);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, parameter.toString());
        Request request = new Request.Builder()
                .url(Url.toString())
                .put(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                 runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Edit_CoWorkingSpace.this,"Connection Failed!", Toast.LENGTH_LONG).show();
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
                            String error = json.get("error").toString();
                            Toast.makeText(Edit_CoWorkingSpace.this, error, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            Toast.makeText(Edit_CoWorkingSpace.this, "Saved", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }

}
