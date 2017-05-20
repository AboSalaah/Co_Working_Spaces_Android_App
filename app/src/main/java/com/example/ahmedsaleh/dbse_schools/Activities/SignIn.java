package com.example.ahmedsaleh.dbse_schools.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class SignIn extends AppCompatActivity {

   private EditText userOrEmail;
    private EditText password;
    private Button signInButton;
    EditText emailForgetPassword;
    TextView forgotPassword;
    TextView signUpTextView;
    ImageView facebookPage;
    String result=null;
    StringBuilder URL;
    public static String token;
    public static int id;
    public static String userType;
    public static String workSpaceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        userOrEmail = (EditText) findViewById(R.id.signIn_user_email);
        password = (EditText) findViewById(R.id.signIn_password);
        signInButton=(Button) findViewById(R.id.sign_in_button);
        facebookPage =(ImageView) findViewById(R.id.Facebook_Button);
        signUpTextView = (TextView) findViewById(R.id.SignIn_SignUp_text);
        forgotPassword = (TextView) findViewById(R.id.forget_password) ;
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL = new StringBuilder(getString(R.string.url)+"signin");
                if(validate()){
                    connect();
                }
            }
        });
        facebookPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent facebookIntent = openBbSE_FacebookPage(SignIn.this);
                Intent chooser = Intent.createChooser(facebookIntent,"Open By");
                startActivity(chooser);
            }
        });
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToSignUpActivity();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SignIn.this);
                final View mview=getLayoutInflater().inflate(R.layout.forgot_password_dialog,null);
                mBuilder.setTitle(R.string.forget_pass_title1);
                mBuilder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        URL = new StringBuilder(getString(R.string.url)+"forgetpassword");
                        emailForgetPassword = (EditText) mview.findViewById(R.id.forget_password_code_editText);
                        connectForgetPassword();
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
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_signUp) {
            moveToSignUpActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //  this function for validating the user input for signin
    private boolean validate()
    {
        EditText email=(EditText)findViewById(R.id.signIn_user_email);
        EditText password=(EditText)findViewById(R.id.signIn_password);
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
        return true;
    }
    private void moveToSignUpActivity(){
        Intent i = new Intent(SignIn.this,SignUp.class);
        startActivity(i);
    }
    private void moveToMainActivity(){
        Intent i = new Intent(SignIn.this,MainActivity.class);
        startActivity(i);
        finish();
    }
    public static Intent openBbSE_FacebookPage(Context context) {
        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://page/1220130878052358"));
        } catch (Exception e){

            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/pg/DatabaseSE.CO"));
        }
    }

    void connectForgetPassword(){
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, String> params = new HashMap<String, String>();
        JSONObject parameter = new JSONObject(params);

        params.put("email",emailForgetPassword.getText().toString());
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, parameter.toString());
        Request request = new Request.Builder()
                .url(URL.toString())
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SignIn.this,"Connection Failed!", Toast.LENGTH_LONG).show();
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
                            Log.v("message",msg);
                            Toast.makeText(SignIn.this,msg, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            Toast.makeText(SignIn.this,"The selected email is invalid!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }

    void connect()
    {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, String> params = new HashMap<String, String>();
        params.put("login", String.valueOf(userOrEmail.getText()));
        params.put("password", String.valueOf(password.getText()));
        JSONObject parameter = new JSONObject(params);
        OkHttpClient client = new OkHttpClient();
        Log.i("sign in url",URL.toString());
        RequestBody body = RequestBody.create(JSON, parameter.toString());
        Request request = new Request.Builder()
                .url(URL.toString())
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SignIn.this,"Connection Failed!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {
                result = response.body().string().toString();
                Log.v("Response code", String.valueOf(response.code()));
                Log.i("sign in result",result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(result);
                            String error = json.get("error").toString();
                            Toast.makeText(SignIn.this,"Wrong User or Password", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            try {
                                JSONObject json = new JSONObject(result);
                                token=json.getString("token");
                                id=json.getInt("id");
                                userType=json.getString("type");
                                if(userType.equals("VISITOR")){workSpaceId="-1";moveToMainActivity();}
                                else
                                {
                                    workSpaceId=json.getString("workspaceid");
                                    Intent intent=new Intent(SignIn.this,EventProfile.class);
                                    intent.putExtra("id", workSpaceId);
                                    startActivity(intent);
                                }
                            } catch (JSONException ex){
                                ex.printStackTrace();
                            }
                        }

                    }
                });

            }
        });
    }


}
