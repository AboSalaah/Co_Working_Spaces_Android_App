package com.example.ahmedsaleh.dbse_schools.Helpers;

/**
 * Created by Ahmed Saleh on 4/23/2017.
 * Class that represent a single Governorate
 */
public class Grid_View_Item {

    private String mName,mId;

    /**
     * Initialize the Governorate
     * @param name Governorate name
     * @param id  Governorate id
     */
    public Grid_View_Item(String name,String id)
    {
        mName=name; mId=id;
    }

    /**
     * Function returns Governorate name

     * @return String
     */
    public String getmName(){return mName;}

    /**
     * Function returns Governorate id
     * @return String
     */
    public String getmId(){return mId;}



}
