package com.example.ahmedsaleh.dbse_schools.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ahmedsaleh.dbse_schools.Activities.Schools;
import com.example.ahmedsaleh.dbse_schools.Activities.SignIn;
import com.example.ahmedsaleh.dbse_schools.Adapters.Governorates_Adapter;
import com.example.ahmedsaleh.dbse_schools.Helpers.School;
import com.example.ahmedsaleh.dbse_schools.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragement extends Fragment {

    private StringBuilder Url=new StringBuilder();
    private String result;
    private Governorates_Adapter governoratesAdapter;
    public HomeFragement() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_fragement, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView listView=(ListView)getActivity().findViewById(R.id.list_view);
        ArrayList<String> arr=new ArrayList<>();
        arr.add("cairo");
        arr.add("giza");
        governoratesAdapter=new Governorates_Adapter(getContext(),new ArrayList<String>(arr));
        listView.setAdapter(governoratesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Governorate =(String) parent.getItemAtPosition(position);
                Intent intent=new Intent(getContext(),Schools.class);
                intent.putExtra("name",Governorate);
                startActivity(intent);

            }
        });

        Url.append(getString(R.string.url)+"schoollocation"+"?token="+ SignIn.token);
        //connect();




    }

    void connect()
    {
        OkHttpClient okHttpClient=new OkHttpClient();
        okhttp3.Request request=new okhttp3.Request.Builder()
                .url(Url.toString())
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"Connection Failed!", Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result=response.body().string().toString();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            Log.i("tag","el resulttttt  "+result);
                            ArrayList<String>data=new ArrayList<String>();
                            JSONArray jsonArray=new JSONArray(result);
                            for(int i=0;i<jsonArray.length();++i)
                            {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                if(jsonObject.has("city")&&!jsonObject.getString("city").equals("null"))
                                {

                                    String name=jsonObject.getString("city");
                                    data.add(name);



                                }
                            }
                            governoratesAdapter.clear();
                            governoratesAdapter.addAll(data);
                            governoratesAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });



            }
        });

    }


}
