package com.example.ahmedsaleh.dbse_schools.Helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ahmedsaleh.dbse_schools.Activities.Co_Working_Spaces;
import com.example.ahmedsaleh.dbse_schools.R;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Ahmed Saleh on 4/23/2017.
 */

public class QueryUtils {

    public static Bitmap converttobitmap(String logo)
    {
        byte[] decodedString = Base64.decode(logo, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    /**
     * Function that convert an integer to number of Dp pixles
     * @param context Application Context
     * @param size integer number to be converted
     * @return int number of Dp pixels
     */
    public static int getDppixels(Context context, int size)
    {
        float scale = context.getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (size*scale + 0.5f);
        return dpAsPixels;
    }

    /**
     * Function that take String contains multible items seperated with slash and parse it
     * @param s String to parse
     * @return String
     */
    public static String parser(String s)
    {
        Log.i("tag","el string hwa "+s);
        String parts[]=s.split("/");
        Log.i("tag","awl mkaan "+parts[0]);
        StringBuilder parse=new StringBuilder();
        parse.setLength(0);
        for(int i=0;i<parts.length;++i)
        {
            parse.append(parts[i]);
            parse.append("\n");
        }
        Log.i("tag","resultttt "+parse.toString()+"\n");
        return parse.toString();
    }
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, null);
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * function that send an intent to show geographical coordinates
     * it allows to show geographical coordinates from maps app or browser
     * @param ctx application context
     * @param lat latitude
     * @param longt longitude
     * @param location name of the location with this latitude and longitude
     *
     */
    public static void showLocationOnMap(Context ctx,double lat,double longt,String location) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://maps.google.com/maps?q=loc:" + lat + "," + longt+" ("+location+")"));
        PackageManager manager = ctx.getPackageManager();
        List<ResolveInfo> list = manager.queryIntentActivities(intent, 0);

        if (list != null && list.size() > 0) {
            ctx.startActivity(intent);
        } else {
            //No activity to handle the intent.
            Toast.makeText(ctx, "Your Device doesn't have any app to show map",
                    Toast.LENGTH_LONG).show();

        }

    }


    public static int getRatingBarColor(float rating)
    {
        if(rating<=0)return R.color.notselectedstarscolor;
        else if(rating>0&&rating<=1)return R.color.firstdegreeratingbar;
        else if(rating>1&&rating<=2)return R.color.seconddegreeratingbar;
        else if(rating>2&&rating<=3)return R.color.thirddegreeratingbar;
        else if(rating>3&&rating<=4)return R.color.fourthdegreeratingbar;
        else if(rating>4&&rating<=5)return R.color.fifthdegreeratingbar;
        return R.color.fifthdegreeratingbar;
    }



}
