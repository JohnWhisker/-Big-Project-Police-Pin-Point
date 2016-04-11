package com.example.mapdemo;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JohnWhisker on 4/11/16.
 */
public class Police {
    private LatLng loc;
    private Date time;
    private Long timeInLong;
    private int Like;
    private int dislike;

    public Police(LatLng loc){
        this.loc = loc;
        this.time = new Date();
        timeInLong = getDateInMillis(this.time);

    }public Police(LatLng loc,Long time){
        this.loc = loc;
        this.timeInLong = time;

    }
    public static long getDateInMillis(Date dateinput) {
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

        long dateInMillis = 0;
        try {
            Date mDate = format.parse(""+dateinput);
            long timeInMilliseconds = mDate.getTime();

            return timeInMilliseconds;
        } catch (ParseException e) {
            Log.d("time","Exception while parsing date. " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }
        Date date = new Date();
        long now = getDateInMillis(date);
        if (time > now || time <= 0) {
            return null;
        }
        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public LatLng getLoc() {
        return loc;
    }

    public Date getTime() {
        return time;
    }

    public Long getTimeInLong() {
        return timeInLong;
    }

    public int getLike() {
        return Like;
    }

    public int getDislike() {
        return dislike;
    }

    @Override
    public String toString() {
        return ""+loc.latitude+":"+loc.longitude+":"+getDateInMillis(time);
    }
}

