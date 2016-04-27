package com.example.mapdemo;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by alecksjohansson on 4/24/16.
 */
public class EasyMode extends Fragment implements MyLocationListener {
    private TextView mSeepolice;
    private TextView mDontsee;
    private double mDistances;
    ArrayList<Police> mPoliceList;
    private Firebase mDb;
    private GoogleMap mMap;
    private LatLng mPolicePositon;
    private LatLng mCurrentPosition;
    private TextView mShowPolice;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.easy_fragment, container, false);
        mSeepolice = (TextView) view.findViewById(R.id.seePolice);
        mShowPolice = (TextView) view.findViewById(R.id.textView);
        mDb = new Firebase(Config.FIREBASE_URL);
        mCurrentPosition = new LatLng(0,0);
        mDontsee = (TextView) view.findViewById(R.id.dontSee);
        mPoliceList = new ArrayList<>();
        readDatabase();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

    }
    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);
        return Radius * c;
    }


    public void onLocationUpdate(LatLng newPos)
    {
        mCurrentPosition = newPos;
        }

    public void writeToCloud(Police police){}
    public void onMapLongClick(LatLng latLng){}
    public void setUpClusterer(){}
    public void onLocationChanged(Location location){}

    public void readDatabase() {
        Query query = mDb.orderByChild("id");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Police police = dataSnapshot.getValue(Police.class);
                mPoliceList.add(police);
                FindNearestPin(police);
                //dropMarker(police);
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void FindNearestPin(Police police)
    {
        for(int i =0 ; i<mPoliceList.size();i++)
        {
             long distances = Math.round(CalculationByDistance(mCurrentPosition,police.getPosition())*1000);
            if(distances <= 50)
            {
                mShowPolice.setText("ALL RIGHT THE POLICE IS HERE "+distances);
                Toast.makeText(getContext(),"YOU ARE AWAY FROM "+distances+" METERS from the Police",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getContext(),"YOU ARE AWAY FROM "+distances+" METERS from the Police",Toast.LENGTH_SHORT).show();
            }

        }

    }

}
