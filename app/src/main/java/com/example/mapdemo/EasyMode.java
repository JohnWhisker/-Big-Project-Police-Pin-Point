package com.example.mapdemo;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.RunnableFuture;

import cz.msebera.android.httpclient.Header;

/**
 * Created by alecksjohansson on 4/24/16.
 */
public class EasyMode extends Fragment implements MyLocationListener {

    TextView tvDistance;
    TextView tvZone;
    ArrayList<Police> mPoliceList;
    private Firebase mDb;
    String mAddress = "";
    private LatLng mCurrentPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.easy_fragment, container, false);
        tvDistance = (TextView) view.findViewById(R.id.tvDistance);
        tvZone = (TextView) view.findViewById(R.id.tvZone);
        mDb = new Firebase(Config.FIREBASE_URL);
        mCurrentPosition = new LatLng(0, 0);
        mPoliceList = new ArrayList<>();
        readDatabase();
        return view;
    }

    public void RunningBackgroud() {
        final Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                long distance = FindNearestPin(mCurrentPosition);
                if (distance > 1000) {
                    tvDistance.setTextColor(getResources().getColor(R.color.green_text));
                    tvZone.setText("");
                    tvZone.setVisibility(View.GONE);
                    tvDistance.setText("There're no police nearby");
                } else {
                    if (distance < 100) {
                        tvZone.setVisibility(View.VISIBLE);
                        tvDistance.setTextColor(getResources().getColor(R.color.red_text));
                        tvZone.setTextColor(getResources().getColor(R.color.red_text));
                        tvZone.setText("KILL ZONE");
                        tvDistance.setText(distance + " meters");
                    } else {
                        tvZone.setVisibility(View.VISIBLE);
                        tvDistance.setTextColor(getResources().getColor(R.color.yellow_text));
                        tvZone.setTextColor(getResources().getColor(R.color.yellow_text));
                        tvZone.setText("DANGEROUS ZONE\n");
                        tvDistance.setText(distance + " meters");
                    }
                }
                h.postDelayed(this, 3000);
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        //Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
        //       + " Meter   " + meterInDec);
        return Radius * c;
    }


    public void onLocationUpdate(LatLng newPos) {
        mCurrentPosition = newPos;
    }

    public void writeToCloud(Police police) {
    }

    public void onMapLongClick(LatLng latLng) {
    }

    public void setUpClusterer() {
    }

    public void onLocationChanged(Location location) {
    }

    public void readDatabase() {
        Query query = mDb.orderByChild("id");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Police police = dataSnapshot.getValue(Police.class);
                mPoliceList.add(police);
                RunningBackgroud();
                //dropMarker(police);
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Police police = dataSnapshot.getValue(Police.class);
                mPoliceList.add(police);
                mPoliceList.notifyAll();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Police police = dataSnapshot.getValue(Police.class);
                if (findMatch(police) != -1) {
                    mPoliceList.remove(findMatch(police));
                }
                synchronized (mPoliceList) {
                    mPoliceList.notifyAll();
                }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public int findMatch(Police police) {
        for (int i = 0; i < mPoliceList.size(); i++) {
            if (police.equals(mPoliceList.get(i))) {
                Log.d("FOUND IT",i+" is the one");
                return i;
            }

        }
        Log.d("DEBUG", "Returning -1");
        return -1;
    }

    public void fetchAddress(LatLng latLng) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latLng.latitude + "," + latLng.longitude + "&sensor=true&key=AIzaSyBYfD3NAzh2Q2AdHUpT_qvJSBA_RaCO924";
        Log.d("URL", url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray result = response.getJSONArray("results");
                    mAddress = result.getJSONObject(0).getString("formatted_address");
                    Log.d("Address", "The add is: " + mAddress);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

    public long FindNearestPin(LatLng latLng) {
        long distance = 10000000;
        LatLng nearest = null;
        for (int i = 0; i < mPoliceList.size(); i++) {
            long distances = Math.round(CalculationByDistance(latLng, mPoliceList.get(i).getPosition()) * 1000);
            if (distances < distance) {
                distance = distances;
                nearest = mPoliceList.get(i).getPosition();

            }
        }
        if (nearest != null) {
            fetchAddress(nearest);

        }
        return distance;

    }


}
