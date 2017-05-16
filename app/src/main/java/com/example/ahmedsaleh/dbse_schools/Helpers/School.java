package com.example.ahmedsaleh.dbse_schools.Helpers;

/**
 * Created by Ahmed Saleh on 4/23/2017.
 * This class represent the blueprint for all schools in the list view
 */
public class School {
    private String mName;
    private String mimageUrl;
    private String mId;
    private boolean has;

    /**
     * initialize an school item
     * @param name name of the school
     * @param id  id of the school
     * @param imageurl url of the logo of the school
     */
    public School(String name,String id,String imageurl)
    {
        mName=name; mimageUrl=imageurl; mId=id; has=true;
    }

    public School(String name,String id)
    {
        mName=name; mId=id; has=false;
    }
    /**
     * Function returns name of the school
     * @return String
     */
    public String getmName(){return mName;}

    /**
     * Function returns id of the school
     * @return int
     */
    public String getmId(){return mId;}

    /**
     * Function returns logourl of the school
     * @return
     */
    public String getMimageUrl(){return mimageUrl;}
    public boolean gethas(){return  has;}
}
