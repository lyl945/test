package com.surong.leadloan.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

public class LocationUtil {
    private LocationClient mLocationClient;
    private MyLocationListener mMyLocationListener = new MyLocationListener();
    private static LocationUtil mSelf;
    private List<BDLocationListener> mListener = new ArrayList<BDLocationListener>();
    private Context mContext;

    public static synchronized LocationUtil getInstance(Context context) {
        if (mSelf == null) {
            mSelf = new LocationUtil(context);
        }
        return mSelf;
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // Receive Location
            if (mListener != null && mListener.size() > 0) {
                for (BDLocationListener l : mListener) {
                    l.onReceiveLocation(location);
                }
            }
        }

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub
			
		}

    }

    private LocationUtil(Context context) { 
        mContext = context.getApplicationContext();
        init();
    }

    private void init() {
        mLocationClient = new LocationClient(mContext);
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        mLocationClient.start();
    }

    public LocationClient getLocationClient() {
        if (mLocationClient == null) {
            init();
        }
        return mLocationClient;
    }

    public BDLocation getLocation() {
        return getLocationClient().getLastKnownLocation();
    }

    public void onDestroy() {
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mMyLocationListener);
            mLocationClient.stop();
            mLocationClient = null;
        }
    }

    public void request() {
        
        getLocationClient().requestLocation();
    }

    public void setLocationListener(BDLocationListener l) {
        mListener.add(l);
    }

    public void removeLocationListener(BDLocationListener l) {
        mListener.remove(l);
    }
}
