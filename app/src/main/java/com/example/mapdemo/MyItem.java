package com.example.mapdemo;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by JohnWhisker on 4/10/16.
 */
public class MyItem implements ClusterItem, com.google.maps.android.clustering.ClusterItem {
    private final LatLng mPosition;

    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPossition() {
        return mPosition;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}
