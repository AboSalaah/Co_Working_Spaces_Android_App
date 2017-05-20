package com.example.ahmedsaleh.dbse_schools.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedsaleh.dbse_schools.Activities.SignIn;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragement extends Fragment {


    String result=null;
    StringBuilder URL;
    EditText realName;
    EditText userName;
    EditText email;
    EditText password;
    CheckBox male;
    CheckBox female;
    Button changeGenderButton;
    public static String userType;

    public EditProfileFragement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile_fragement, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Edit Profile");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        realName = (EditText) getActivity().findViewById(R.id.real_user_name);
        userName = (EditText) getActivity().findViewById(R.id.new_username);
        email = (EditText) getActivity().findViewById(R.id.new_email);
        password = (EditText) getActivity().findViewById(R.id.new_password);
        male = (CheckBox) getActivity().findViewById(R.id.male_checkbox_editProfile) ;
        female = (CheckBox) getActivity().findViewById(R.id.female_checkbox_editProfile) ;
        changeGenderButton = (Button) getActivity().findViewById(R.id.chooseGender_editProfile) ;
        changeGenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(male.isChecked()|| female.isChecked()){
                    male.toggle();
                    female.toggle();
                } else {
                    male.toggle();
                }
            }
        });
        URL = new StringBuilder(getString(R.string.url)+SignIn.userType.toLowerCase()+"/"+String.valueOf(SignIn.id)+"?token=");
        Log.v("myyyyyyyyyyyyyyyy",String.valueOf(SignIn.id));
        URL.append(SignIn.token);
       connectToGet();
        Button saveButton = (Button) getActivity().findViewById(R.id.Save_changes_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectToPost();
            }
        });

    }

    void connectToGet()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL.toString())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"Connection Failed!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException
            {
                result=response.body().string().toString();
                Log.v("Response code", String.valueOf(response.code()));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {

                        try {
                            JSONObject json = new JSONObject(result);
                            realName.setText(json.get("name").toString(), TextView.BufferType.EDITABLE);
                            userName.setText(json.get("username").toString(), TextView.BufferType.EDITABLE);
                            email.setText(json.get("email").toString(), TextView.BufferType.EDITABLE);
                            password.setText(json.get("password").toString(), TextView.BufferType.EDITABLE);
                            String gender = json.get("gender").toString();
                            if(gender.equals("MALE")){
                                male.toggle();
                            } else {
                                female.toggle();
                            }
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
        params.put("username", String.valueOf(userName.getText()));
        params.put("email", String.valueOf(email.getText()));
        params.put("password", String.valueOf(password.getText()));
        params.put("name", String.valueOf(realName.getText()));
        if(male.isChecked()){
            params.put("gender","MALE");
        } else {
            params.put("gender","FEMALE");
        }
        params.put("id", String.valueOf(SignIn.id));
        JSONObject parameter = new JSONObject(params);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, parameter.toString());
        Request request = new Request.Builder()
                .url(URL.toString())
                .put(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"Connection Failed!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {
                result = response.body().string().toString();
                Log.v("Response code", String.valueOf(response.code()));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(result);
                            String error = json.get("error").toString();
                            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }

}
