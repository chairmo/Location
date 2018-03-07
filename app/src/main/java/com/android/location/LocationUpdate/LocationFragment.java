package com.android.location.LocationUpdate;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.location.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static com.google.android.gms.common.api.GoogleApiClient.Builder;
import static com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import static com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;


public class LocationFragment extends Fragment implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {

    protected static final String TAG = "Location Services 1";
    protected GoogleApiClient apiClient;
    protected Location mLocation;
    protected TextView latTextView;
    protected TextView longTextView;
    protected LocationRequest mLocationRequest;

    private static final int REQUEST_CODE = 2;


    public LocationFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.location_main, container, false);

        latTextView = (TextView) rootView.findViewById(R.id.latitude);
        longTextView = (TextView) rootView.findViewById(R.id.longitude);

        isGooglePlayServicesAvailable(getActivity());

        //calling the client helper method
        googleApi();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        apiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        //stop the services if still connected
        if (apiClient.isConnected()) {
            apiClient.disconnect();
        }
    }

    //helper method for google client services
    protected synchronized void googleApi() {
        apiClient = new Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){

            }else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            }
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, mLocationRequest, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                //permission granted
                // mLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
            }else {

            }
        }
    }

    @Override
    public void onConnectionSuspended(int result) {
        Log.e(TAG, "Connection Suspended ");
        //re-connect when suspended
        apiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "ConnectionFailed: " + connectionResult.getErrorCode() );
    }

    public void onDisconnected(){
        Log.i(TAG, "Disconnected: ");
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, location.toString());
        //update the location
        latTextView.setText(String.valueOf(mLocation.getLatitude()));
        longTextView.setText(String.valueOf(mLocation.getLongitude()));
    }
    public boolean isGooglePlayServicesAvailable(Activity activity){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (status != ConnectionResult.SUCCESS){
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }
}
