package com.example.ahmedsaleh.dbse_schools.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmedsaleh.dbse_schools.R;
import com.example.ahmedsaleh.dbse_schools.Helpers.School;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ahmed Saleh on 4/23/2017.
 * Custom Class that generates the school item layout to be shown in the listview
 */
public class SchoolAdapter extends ArrayAdapter<School>{
    Context ctx;

    /**
     * Initalize the adapter
     * @param context Application Context
     * @param schools ArrayList of Schools
     */
    public SchoolAdapter(Context context, ArrayList<School> schools)
    {
        super(context,0,schools);
        ctx=context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        School s=getItem(position);

        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.school,parent,false);
        }
        TextView schoolname=(TextView)convertView.findViewById(R.id.school_name_text_view);
        schoolname.setText(s.getmName());
        if(s.gethas())
        {ImageView schoollogo=(ImageView)convertView.findViewById(R.id.school_logo_image_view);
        Picasso.with(ctx).load(s.getMimageUrl()).into(schoollogo);}
        return convertView;
    }


    }

