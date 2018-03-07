package com.android.location.RecognitionUpdate;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.location.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

public class MovementFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    private static final String TAG = "Receiver_BS";

    protected GoogleApiClient apiClient;
    protected TextView statusTextView;
    protected Button removeButton;
    protected Button requestButton;
    protected ActivityDetectionBroadcastReceiver detectionBroadcastReceiver;


    public MovementFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.movement_main, container, false);

        statusTextView = (TextView) rootView.findViewById(R.id.status_textView);
        requestButton = (Button) rootView.findViewById(R.id.request_updates);
        removeButton = (Button) rootView.findViewById(R.id.remove_updates);

        detectionBroadcastReceiver = new ActivityDetectionBroadcastReceiver();
        googleApi();

        return rootView;
    }


    //helper method for google client services
    protected synchronized void googleApi() {
        apiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
    }


    @Override
    public void onStart() {
        super.onStart();
        apiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (apiClient.isConnected()) {
            apiClient.disconnect();
        }
    }

    @Override
    public void onPause() {
        //unregister the broadcast receiver that wss registered during the onResume
        LocalBroadcastManager.getInstance(getActivity())
                .unregisterReceiver(detectionBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //register the broadcast receiver that informs the activity of the DetectedActivity
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(detectionBroadcastReceiver,
                new IntentFilter(Constants.BROADCAST_ACTION));
    }

    public void setRequestButton(View view) {
        if (!apiClient.isConnected()) {
            Toast.makeText(getActivity(), getString(R.string.not_connected),
                    Toast.LENGTH_SHORT).show();
        }
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(apiClient,
                Constants.DETECTION_INTERVAL_IN_MILLISECONDS, getPendingIntentActivity())
                .setResultCallback(this);
        requestButton.setEnabled(false);
        removeButton.setEnabled(true);
    }

    public void setRemoveButton(View view) {
        if (!apiClient.isConnected()) {
            Toast.makeText(getActivity(), getString(R.string.not_connected),
                    Toast.LENGTH_SHORT).show();
        }
        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(apiClient,
                getPendingIntentActivity()).setResultCallback(this);
        requestButton.setEnabled(true);
        removeButton.setEnabled(false);
    }

    private PendingIntent getPendingIntentActivity() {
        Intent intent = new Intent(getActivity(), DetectedActivitiesIntentServices.class);

        return PendingIntent.getService(getActivity(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //helper method for detected activity
    public String getActivityString(int detectedActivityType) {
        Resources resources = getResources();

        switch (detectedActivityType) {
            case DetectedActivity.IN_VEHICLE:
                return resources.getString(R.string.in_vehicle);
            case DetectedActivity.ON_FOOT:
                return resources.getString(R.string.on_foot);
            case DetectedActivity.ON_BICYCLE:
                return resources.getString(R.string.on_bicycle);
            case DetectedActivity.RUNNING:
                return resources.getString(R.string.running);
            case DetectedActivity.STILL:
                return resources.getString(R.string.still);
            case DetectedActivity.TILTING:
                return resources.getString(R.string.tilting);
            case DetectedActivity.UNKNOWN:
                return resources.getString(R.string.unknown);
            case DetectedActivity.WALKING:
                return resources.getString(R.string.walking);
            default:
                return resources.getString(R.string.unIdentifiable_activity);
        }
    }

    @Override
    public void onResult(@NonNull Status status) {
        if (status.isSuccess()) {
            Log.e(TAG, "Successfully added Activity Detection.");
        } else {
            Log.d(TAG, "Error Adding or Removing Activity Detection." +
                    status.getStatusMessage());
        }
    }

    public class ActivityDetectionBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<DetectedActivity> updatedActivities =
                    intent.getParcelableArrayListExtra(Constants.ACTIVITY_EXTRA);

            String statusStr = "";
            for (DetectedActivity activity : updatedActivities) {
                statusStr += getActivityString(activity.getType()) +
                        activity.getConfidence() + "%\n";
            }
            statusTextView.setText(statusStr);

        }

    }
}



