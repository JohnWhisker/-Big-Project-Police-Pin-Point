package com.example.mapdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by alecksjohansson on 4/24/16.
 */
public class EasyMode extends Fragment {
    private TextView mSeepolice;
    private TextView mDontsee;
    private double mDistances;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.easy_fragment, container, false);
        mSeepolice = (TextView) view.findViewById(R.id.seePolice);
        mDontsee = (TextView) view.findViewById(R.id.dontSee);
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
    public void FindNearestPin()
    {
       LatLng currentPosition = ((MapActivity)getActivity()).getmCurrentPosition();
         ArrayList<Police> police = ((MapActivity)getActivity()).getmPoliceList();
        for(int i = 0 ; i < police.size(); i++)
        {
            police.get(i).getPosition();
            CalculationByDistance(currentPosition,police.get(i).getPosition());
            Log.d("Calculation","KM"+mDistances);

        }

    }


}
