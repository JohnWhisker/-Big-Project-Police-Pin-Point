package com.example.mapdemo;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by alecksjohansson on 4/24/16.
 */
public interface MyLocationListener {

    public void onLocationUpdate(LatLng newPos);
    public void writeToCloud(Police police);
    public void onMapLongClick(LatLng latLng);
    public void setUpClusterer();
    public void onLocationChanged(Location location);

}
