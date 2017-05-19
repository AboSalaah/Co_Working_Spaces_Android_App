package com.example.ahmedsaleh.dbse_schools.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    EditText othersEditText;

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
         othersEditText = (EditText) findViewById(R.id.Workspace_others_editText);
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
            connectToPost();
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
