package com.android.location.GeofenceUpdate;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.android.location.MainActivity;
import com.android.location.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chairmo on 2/23/2018.
 */

public class GeofenceTransitionIntentServices extends IntentService {

    protected static final String TAG = "geofences service";
    private static final int JOB_ID = 573;

    private static final String CHANNEL_ID = "channel_01";


    public GeofenceTransitionIntentServices() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, Class<GeofenceTransitionIntentServices>
            geofenceTransitionIntentServicesClass, int jobId, Intent intent) {

        enqueueWork(context, GeofenceTransitionIntentServices.class, JOB_ID, intent);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceErrorMessage.getErrorString(this,
                    geofencingEvent.getErrorCode());
            Log.i(TAG, errorMessage);
            return;
        }

        //get the transition type, either enter or exit
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition ==
                Geofence.GEOFENCE_TRANSITION_EXIT) {
            //get the transition details in string
            List<Geofence> geofencesTrigger = geofencingEvent.getTriggeringGeofences();

            //get the transition details as string
            String geofenceTransitionDetails = getGeofenceTransitionDetails(this,
                    geofenceTransition, geofencesTrigger);

            // Send notification and log the transition details.
            sendNotification(geofenceTransitionDetails);
            Log.i(TAG, geofenceTransitionDetails);
        }else {
            //log error

            Log.e(TAG, getString(R.string.transition_invalid_type) + geofenceTransition);

        }
    }

    private String getGeofenceTransitionDetails(Context context, int geofenceTransion,
                                                List<Geofence> geofenceList) {
        String geofenceTransitionString = geofenceTransionString(geofenceTransion);

        //get the id's of each geofence object that was triggered
        ArrayList triggerGeofenceList = new ArrayList();
        for (Geofence geofence : geofenceList) {
            triggerGeofenceList.add(geofence.getRequestId());
        }
        String triggerGeofence = TextUtils.join(",", triggerGeofenceList);

        return geofenceTransitionString + ": " + triggerGeofence;
    }

    private String geofenceTransionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.entered_geofence);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return getString(R.string.exited_geofence);
            default:
                return getString(R.string.unknown_geofence_action);
        }
    }

    private void sendNotification(String notificatioDetails) {
        //get an instance of the notification manager
        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);

            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            assert manager != null;
            manager.createNotificationChannel(mChannel);

            //create an explicit content that starts the main activity
            Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);

            //construct a task
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

            //push the content into the stack
            stackBuilder.addNextIntent(notificationIntent);

            //get a pending intent containing the entire stack
            PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            //get a notification builder compatible with the platform version
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            //define the notification setting
            builder.setSmallIcon(R.drawable.ic_launcher_background)
                    // In a real app, you may want to use a library like Volley
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                            R.drawable.ic_launcher_background))
                    .setColor(Color.RED).setContentTitle(notificatioDetails)
                    .setContentText("Click Notification to Return to App")
                    .setContentIntent(notificationPendingIntent);

            // Set the Channel ID for Android O.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannel(CHANNEL_ID); // Channel ID
            }

            //dismiss the notification once a user touches it
            builder.setAutoCancel(true);

            // Issue the notification
            manager.notify(0, builder.build());


        }
    }
}
