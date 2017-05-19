package com.example.ahmedsaleh.dbse_schools.Helpers;

/**
 * Created by Ahmed Saleh on 5/19/2017.
 */
public class Event {


    /**
     * Initialize event
     * @param name event name
     * @param address event address
     * @param id event id
     */
    String mEventName;
    String mEventAddress;
    String mEventId;
    public Event(String name,String address,String id)
    {
        mEventName=name;
        mEventAddress=address;
        mEventId=id;
    }

    /**
     * Function to get event name
     * @return String event name
     */
    public String getmEventName(){return mEventName;}

    /**
     * Function to get event address
     * @return String event address
     */
    public String getmEventAddress(){return mEventAddress;}

    /**
     * Function to get event id
     * @return String event id
     */
    public String getmEventId(){return mEventId;}
}
