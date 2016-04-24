package com.example.mapdemo;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by alecksjohansson on 4/24/16.
 */
public interface MyLocationListener {

    public void onLocationUpdate(LatLng newPos);
}
