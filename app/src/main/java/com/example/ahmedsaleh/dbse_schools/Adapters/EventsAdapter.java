package com.example.ahmedsaleh.dbse_schools.Adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ahmedsaleh.dbse_schools.Helpers.Co_Working_Space;
import com.example.ahmedsaleh.dbse_schools.Helpers.Event;
import com.example.ahmedsaleh.dbse_schools.Helpers.QueryUtils;
import com.example.ahmedsaleh.dbse_schools.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ahmed Saleh on 5/19/2017.
 */
public class EventsAdapter extends ArrayAdapter<Event> {

    private Context ctx;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event s=getItem(position);

        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.event_list_item,parent,false);
        }
       TextView eventname=(TextView)convertView.findViewById(R.id.event_name_text_view);
        eventname.setText(s.getmEventName());
        TextView eventaddress=(TextView)convertView.findViewById(R.id.event_address_text_view);
        eventaddress.setText(s.getmEventAddress());
        return convertView;
    }

    /**
     * Initalize the adapter
     * @param context Application Context
     * @param Events_list ArrayList of events
     */
    public EventsAdapter(Context context, ArrayList<Event> Events_list)
    {
        super(context,0, Events_list);
        ctx=context;
    }
}
