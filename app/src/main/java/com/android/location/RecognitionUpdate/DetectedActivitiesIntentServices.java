package com.android.location.RecognitionUpdate;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

/**
 * Created by chairmo on 2/18/2018.
 */

public class DetectedActivitiesIntentServices extends IntentService {
    protected static final String TAG = "detection_is";

    public DetectedActivitiesIntentServices(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        Intent localIntent = new Intent(Constants.BROADCAST_ACTION);

        //get the list of probable activities associated with the current state
        ArrayList<DetectedActivity> detectedActivities = (ArrayList<DetectedActivity>)
                result.getProbableActivities();

        //log each activity
        Log.e(TAG, "Activities Detected.");

        //Broadcast the list of  detected activities
        localIntent.putExtra(Constants.ACTIVITY_EXTRA, detectedActivities);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

    }

}
