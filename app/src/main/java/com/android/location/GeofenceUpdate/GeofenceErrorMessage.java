package com.android.location.GeofenceUpdate;

import android.content.Context;
import android.content.res.Resources;

import com.android.location.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.GeofenceStatusCodes;

/**
 * Created by chairmo on 2/23/2018.
 */

public class GeofenceErrorMessage {
    //prevent instantiation of the class by making the constructor private
    private GeofenceErrorMessage() {
    }

    //return error string for geofence exception
    public static String getErrorString(Context context, Exception e) {
        if (e instanceof ApiException) {
            return getErrorString(context, ((ApiException) e).getStatusCode());
        } else {
            return context.getResources().getString(R.string.unknown_geofence_error);
        }
    }

    //Returns the error string for a geofencing error code.
    public static String getErrorString(Context context, int errorCode) {
        Resources resources = context.getResources();

        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return resources.getString(R.string.geofence_not_available);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return resources.getString(R.string.geofence_too_many_pending_intent);
            default:
                return resources.getString(R.string.unknown_geofence_error);
        }
    }
}
