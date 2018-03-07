package com.android.location;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.location.GeofenceUpdate.GeofenceFragment;
import com.android.location.LocationUpdate.LocationFragment;
import com.android.location.RecognitionUpdate.MovementFragment;

/**
 * Created by chairmo on 2/18/2018.
 */

public class CategoryAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public CategoryAdapter(Context context, FragmentManager fragmentManager){
        super(fragmentManager);
        mContext = context;
    }
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new MovementFragment();
        } else if (position ==1) {
            return new LocationFragment();
        }else {
            return new GeofenceFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return mContext.getString(R.string.user_activities);
        }else if (position ==1){
            return mContext.getString(R.string.user_locations);
        }else {
            return mContext.getString(R.string.user_geofences);
        }
    }
}
