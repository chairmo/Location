package com.android.location.RecognitionUpdate;

import com.google.android.gms.location.DetectedActivity;

/**
 * Created by chairmo on 2/18/2018.
 */

public final class Constants {
    public Constants(){}

    public static final String PACKAGE_NAME = "com.android.location";
    public static final String BROADCAST_ACTION = PACKAGE_NAME + ".BROADCAST_ACTION";
    public static final String ACTIVITY_EXTRA = PACKAGE_NAME + ".ACTIVITY_EXTRA";
    public static final String SHARED_PREFERENCE_NAME = PACKAGE_NAME + ".SHARED_PREFERENCE";
    public static final String ACTIVITY_UPDATES_REQUEST_KEY = PACKAGE_NAME +
            ".ACTIVITIES_UPDATES_REQUESTED";
    public static final String DETECTED_ACTIVITIES = PACKAGE_NAME + ".DETECTED_ACTIVITIES";
    public static final Long DETECTION_INTERVAL_IN_MILLISECONDS = 0l;

    public static final int[] MONITORED_ACTIVITIES = {
            DetectedActivity.IN_VEHICLE, DetectedActivity.ON_BICYCLE, DetectedActivity.ON_FOOT,
            DetectedActivity.RUNNING, DetectedActivity.STILL, DetectedActivity.TILTING,
            DetectedActivity.UNKNOWN, DetectedActivity.WALKING
    };

}
