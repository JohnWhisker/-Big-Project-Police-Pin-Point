package com.example.mapdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

/**
 * Created by alecksjohansson on 4/24/16.
 */
public class MapFragmentHolder extends Fragment implements MyLocationListener {

    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_holder_fragment, container, false);
        setupMap();
        return view;
    }

    void setupMap() {
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mMapFragment != null) {
            mMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                }
            });
        }
    }

    protected void loadMap(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            // Map is ready
            Toast.makeText(getActivity(), "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
            MapActivityPermissionsDispatcher.getMyLocationWithCheck((MapActivity)getActivity());
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.setMyLocationEnabled(true);

            Log.d("DEBUG", "enabled location");
//            mMap.setOnMapLongClickListener(this);
        } else {
            Toast.makeText(getContext(), "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpClusterer() {
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
//        mClusterManager = new ClusterManager<Police>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
//        mMap.setOnCameraChangeListener(mClusterManager);
//        mMap.setOnMarkerClickListener(mClusterManager);
    }


    @Override
    public void onLocationUpdate(LatLng newPos) {
        if (mMap != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(newPos)
                    .zoom(11)
                    .bearing(90)
                    .tilt(0)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }
}
