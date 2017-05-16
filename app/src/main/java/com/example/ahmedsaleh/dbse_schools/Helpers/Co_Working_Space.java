package com.example.ahmedsaleh.dbse_schools.Helpers;

/**
 * Created by Ahmed Saleh on 4/23/2017.
 * This class represent the blueprint for all schools in the list view
 */
public class Co_Working_Space {
    private String mName;
    private String mimageUrl;
    private String mId;
    private boolean has;
    private float mRating;

    /**
     * initialize an co_working_space item
     * @param name name of the co_working_space
     * @param id  id of the co_working_space
     * @param imageurl url of the logo of the co_working_space
     */
    public Co_Working_Space(String name, String id, String imageurl,float rating)
    {
        mName=name; mimageUrl=imageurl; mId=id; has=true; mRating=rating;
    }

    public Co_Working_Space(String name, String id,float rating)
    {
        mName=name; mId=id; has=false; mRating=rating;
    }
    /**
     * Function returns name of the co_working_space
     * @return String
     */
    public String getmName(){return mName;}

    /**
     * Function returns id of the co_working_space
     * @return int
     */
    public String getmId(){return mId;}

    /**
     * Function returns logourl of the co_working_space
     * @return
     */
    public String getMimageUrl(){return mimageUrl;}
    public boolean gethas(){return  has;}

    /**
     * Function returns the rating of the workspace
     */
    public float getRating(){return mRating;}
}
