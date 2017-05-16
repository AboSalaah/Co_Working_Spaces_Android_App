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
import com.example.ahmedsaleh.dbse_schools.Helpers.QueryUtils;
import com.example.ahmedsaleh.dbse_schools.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ahmed Saleh on 4/23/2017.
 * Custom Class that generates the co_working_space item layout to be shown in the listview
 */
public class Co_Working_Spaces_Adapter extends ArrayAdapter<Co_Working_Space>{
    Context ctx;

    /**
     * Initalize the adapter
     * @param context Application Context
     * @param coWorkingSpaces ArrayList of Co_Working_Spaces
     */
    public Co_Working_Spaces_Adapter(Context context, ArrayList<Co_Working_Space> coWorkingSpaces)
    {
        super(context,0, coWorkingSpaces);
        ctx=context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Co_Working_Space s=getItem(position);

        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.co_working_space,parent,false);
        }
        TextView schoolname=(TextView)convertView.findViewById(R.id.co_working_space_name_text_view);
        schoolname.setText(s.getmName());
        if(s.gethas())
        {
            ImageView schoollogo=(ImageView)convertView.findViewById(R.id.co_working_space_logo_image_view);
            Picasso.with(ctx).load(s.getMimageUrl()).into(schoollogo);}
        RatingBar ratingBar=(RatingBar)convertView.findViewById(R.id.list_view_rating_bar);
        float rating=s.getRating();
        ratingBar.setRating(rating);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(ctx, QueryUtils.getRatingBarColor(rating)), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(ContextCompat.getColor(ctx,QueryUtils.getRatingBarColor(rating)),PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(ContextCompat.getColor(ctx,R.color.notselectedstarscolor),PorterDuff.Mode.SRC_ATOP);
        return convertView;
    }


    }

