package com.example.ahmedsaleh.dbse_schools.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ahmedsaleh.dbse_schools.R;

import java.util.ArrayList;

/**
 * Created by Ahmed Saleh on 4/23/2017.
 * Custom Class for convert strings governorates data into list view items
 */
public class Governorates_Adapter extends ArrayAdapter<String>{

    private Context ctx;

    /**
     * Initialize the adapter
     * @param context Application context
     * @param gov governorates list
     */
    public Governorates_Adapter(Context context, ArrayList<String> gov)
    {
        super(context,0,gov);
        ctx=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String s=getItem(position);

        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.governorate_list_view_item,parent,false);
        }
        TextView governoratename=(TextView)convertView.findViewById(R.id.governorate_list_view_text_view);
        governoratename.setText(s);
        return convertView;

    }
}
