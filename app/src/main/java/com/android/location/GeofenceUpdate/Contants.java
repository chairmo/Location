package com.android.location.GeofenceUpdate;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by chairmo on 2/25/2018.
 */

public final class Contants {

    private Contants(){}
    private static final String PACKAGE_NAME = "com.google.android.location.Geofence";

    static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";

    /**
     * Used to set an expiration time for a geofence. After this amount of time Location Services
     * stops tracking the geofence.
     */
    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;

    /**
     * For this sample, geofences expire after twelve hours.
     */
    static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
    static final float GEOFENCE_RADIUS_IN_METERS = 1609; // 1 mile, 1.6 km


     static final HashMap<String, LatLng> KUJE_FCMB_AREA = new HashMap<>();
    static {
        KUJE_FCMB_AREA.put("Dominic Str", new LatLng(8.8779028, 7.2337371));

        // Googleplex.
        KUJE_FCMB_AREA.put("GOOGLE", new LatLng(37.422611,-122.0840577));


    }

}
