package com.grand.android.rememberparkinglocation;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.sql.ConnectionEventListener;


public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {
    // minimum distance between location updates, in meters
    private static float LOCATION_REFRESH_DISTANCE = 2;
    // minimum distance between location updates, in meters
    private static long LOCATION_REFRESH_TIME = 1000;

    // Current best location estimate
    private Location mCurrentLocation;

    private GoogleMap mMap;

    LocationManager mLocationManager;

    private GoogleApiClient mGoogleApiClient;

    private final LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            mCurrentLocation = location;
            updateDisplay();
            Log.i("onLocationChanged", "****");
            Log.i("Longitude:", String.valueOf(mCurrentLocation.getLongitude()));
            Log.i("getLatitude:", String.valueOf(mCurrentLocation.getLatitude()));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i("onStatusChanged", "");
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, mLocationListener);

        if(mGoogleApiClient == null){
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        setContentView(R.layout.activity_main);
        ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

    }

    // Update display
    private void updateDisplay() {
        if(mCurrentLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 20));
        }
    }

    public void parking(View view){
        Log.i("parking", "");
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                .title("Parking Location"))
                .setIcon(BitmapDescriptorFactory.defaultMarker());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("onConnected", "");
        mMap = googleMap;
        // Enabling MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);
        if(mCurrentLocation != null) {
            Log.i("Longitude:", String.valueOf(mCurrentLocation.getLongitude()));
            Log.i("getLatitude:", String.valueOf(mCurrentLocation.getLatitude()));

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("onConnected", "");
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        Log.i("Longitude:", String.valueOf(mCurrentLocation.getLongitude()));
        Log.i("getLatitude:", String.valueOf(mCurrentLocation.getLatitude()));
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("onConnectionSuspended", "");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("onConnectionFailed", "");
    }
}
