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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ahmedsaleh.dbse_schools.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUp extends AppCompatActivity {

    private RadioGroup genderRadioGroup;
    private String Gender;
    private ArrayAdapter<CharSequence> types; //array adapter to hold the types data for spinner
    private Spinner TypesSpinner;
    private String userType;//string for type of the user
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText phonenumber;
    private EditText realname;
    private Button firstNextButton;
    private Button secondNextButton;
    private Map<String, String> params;
    private EditText confirmCodeEditText;
    private String confirmCode;
    private String result=null;
    private StringBuilder URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        URL = new StringBuilder(getString(R.string.url)+"signupverify");
        genderRadioGroup=(RadioGroup)findViewById(R.id.signup_radio_group);
        realname=(EditText)findViewById(R.id.signup_realname);
        phonenumber=(EditText)findViewById(R.id.singup_phone_number);
        firstNextButton=(Button)findViewById(R.id.signup_first_next_button);
        secondNextButton=(Button)findViewById(R.id.signup_second_next_button);
       // genderRadioGroup.setVisibility(View.GONE);
       // realname.setVisibility(View.GONE);
       // phonenumber.setVisibility(View.GONE);
       // secondNextButton.setVisibility(View.GONE);
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = genderRadioGroup.findViewById(checkedId);
                int index = genderRadioGroup.indexOfChild(radioButton);
                if(index==0)Gender="MALE";
                else Gender="FEMALE";
            }
        });

        firstNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    params = new HashMap<String, String>();
                    String userName=username.getText().toString();
                    String Email=email.getText().toString();
                    String Password=password.getText().toString();
                    params.put("username",userName);
                    params.put("email",Email);
                    params.put("password",Password);
                    Temp_Co_Working_Spaces.username=userName;
                    Temp_Co_Working_Spaces.email=Email;
                    Temp_Co_Working_Spaces.password=Password;
                    connectToPostVerify();
                    verifyemail();
                }
            }
        });



        secondNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateOtherData()){
                    String userName=username.getText().toString();
                    String Email=email.getText().toString();
                    String Password=password.getText().toString();
                    String realName=realname.getText().toString();
                    String phoneNumber=phonenumber.getText().toString();
                    Temp_Co_Working_Spaces.username=userName;
                    Temp_Co_Working_Spaces.email=Email;
                    Temp_Co_Working_Spaces.password=Password;
                    Temp_Co_Working_Spaces.realName=realName;
                    Temp_Co_Working_Spaces.gender=Gender;
                    Temp_Co_Working_Spaces.userType=userType;
                    Temp_Co_Working_Spaces.phoneNumber=phoneNumber;
                    if(!userType.equals(getString(R.string.wso))&&!userType.equals(getString(R.string.pwso)))
                    { URL = new StringBuilder(getString(R.string.url)+"signup");
                        params = new HashMap<String, String>();
                        params.put("username",username.getText().toString());
                        params.put("email",email.getText().toString());
                        params.put("password",password.getText().toString());
                        params.put("gender",Gender);
                        params.put("name",realname.getText().toString());
                        params.put("type","VISITOR");
                        connectToPost();
                    }
                    else
                    {
                        moveToGovernoratesActivity();
                    }
                }
            }
        });



        TypesSpinner=(Spinner)findViewById(R.id.signup_type_spinner);
        types=ArrayAdapter.createFromResource(this,R.array.types,android.R.layout.simple_spinner_item);
        types.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TypesSpinner.setAdapter(types);
        TypesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item=(String)parent.getItemAtPosition(position);
                if(item.equals(getString(R.string.visitor_spinner))){
                    userType=getString(R.string.visitor);
                }
                else if(item.equals(getString(R.string.wso_spinner))){
                    userType=getString(R.string.wso);

                }
                else if(item.equals(getString(R.string.pwso_spinner))){
                    userType=getString(R.string.pwso);
                }
                else {userType="";}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    boolean validate()
    {
        if(username.getText().toString().isEmpty())
        {
            username.setError("User Name "+(getString(R.string.emptyerror)));
            return false;
        }
        if(email.getText().toString().isEmpty())
        {
            email.setError("E-mail "+getString(R.string.emptyerror));
            return false;
        }
        if(password.getText().toString().isEmpty())
        {
            password.setError("password "+getString(R.string.emptyerror));
            return false;
        }
        if(userType.isEmpty())
        {
            Toast.makeText(getApplicationContext(),getString(R.string.notypeerror),Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    void verifyemail()
    {

        final AlertDialog.Builder mBuilder=new AlertDialog.Builder(SignUp.this);
        final View mview=getLayoutInflater().inflate(R.layout.codeinputdialog,null);
        mBuilder.setTitle(R.string.confirmationcode);
        mBuilder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                confirmCodeEditText = (EditText) mview.findViewById(R.id.confirmation_code_editText);
                String mystr = confirmCodeEditText.getText().toString();
                if(mystr.equals(confirmCode)) {
                    realname.setVisibility(View.VISIBLE);
                    secondNextButton.setVisibility(View.VISIBLE);
                    if(userType.equals(getString(R.string.pwso))||userType.equals(getString(R.string.wso))){phonenumber.setVisibility(View.VISIBLE);
                        secondNextButton.setText(getString(R.string.yourworkspace));

                    }
                    else
                    {
                        secondNextButton.setText(getString(R.string.next));
                    }
                    genderRadioGroup.setVisibility(View.VISIBLE);


                } else {

                    Toast.makeText(SignUp.this, "Wrong Code", Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
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

    boolean validateOtherData(){
        if(realname.getText().toString().isEmpty()){
            realname.setError("User Name "+(getString(R.string.emptyerror)));
            return false;
        }
        if(userType.equals(getString(R.string.pwso))||userType.equals(getString(R.string.wso)))
        {
            if(phonenumber.getText().toString().isEmpty())
            {
                phonenumber.setError("Phone Number "+(getString(R.string.emptyerror)));
                return false;
            }
        }
        if (genderRadioGroup.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(getApplicationContext(),getString(R.string.nogendererror),Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    void connectToPostVerify() {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject parameter = new JSONObject(params);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, parameter.toString());
        Request request = new Request.Builder()
                .url(URL.toString())
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
                        Toast.makeText(SignUp.this,"Connection Failed!", Toast.LENGTH_LONG).show();
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
                            confirmCode = json.get("code").toString();
                            Toast.makeText(SignUp.this, json.get("msg").toString(), Toast.LENGTH_LONG).show();
                            verifyemail();

                        } catch (JSONException e) {
                            Toast.makeText(SignUp.this,"sending to this email failed!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }

    private void moveToSignInActivity(){
        Intent i = new Intent(SignUp.this,SignIn.class);
        startActivity(i);
    }
    private void moveToGovernoratesActivity(){
        Intent i = new Intent(SignUp.this,TempGovernorates.class);
        //i.putExtra("realname",realname.getText().toString());
        startActivity(i);
    }
    void connectToPost() {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject parameter = new JSONObject(params);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, parameter.toString());
        Request request = new Request.Builder()
                .url(URL.toString())
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
                        Toast.makeText(SignUp.this,"Connection Failed!", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(SignUp.this, msg, Toast.LENGTH_LONG).show();
                            //
                            moveToSignInActivity();

                            finish();
                        } catch (JSONException e) {
                            Toast.makeText(SignUp.this, "Registeration Failed!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }


}
