package com.android.location.GeofenceUpdate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by chairmo on 2/26/2018.
 */

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private static final int JOB_ID = 573;
    @Override
    public void onReceive(Context context, Intent intent) {

        // Enqueues a JobIntentService passing the context and intent as parameters
        GeofenceTransitionIntentServices.enqueueWork(context, GeofenceTransitionIntentServices.class, JOB_ID, intent);
    }

}

