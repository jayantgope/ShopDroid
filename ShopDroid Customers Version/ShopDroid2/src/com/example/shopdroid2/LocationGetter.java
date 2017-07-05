package com.example.shopdroid2;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationGetter extends Application 
{
	private static final long MIN_TIME_BW_UPDATES = 0;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
	private LocationManager locationManager;
	private ContextWrapper mContext;
	private boolean isGPSEnabled;
	private boolean isNetworkEnabled;
	private boolean canGetLocation;
	private Location location;
	private double latitude;
	private double longitude;
	public Location getLocation(Context ctx) 
	{
	    try 
	    {
	    	String context = Context.LOCATION_SERVICE;
			locationManager = (LocationManager)ctx. getSystemService(context);
	      
	        // getting GPS status
	        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	        // getting network status
	        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

	        if (!isGPSEnabled && !isNetworkEnabled) 
	        {
	            // no network provider is enabled
	        } 
	        else 
	        {
	            this.canGetLocation = true;
	            if (isNetworkEnabled) 
	            {
	                locationManager.requestLocationUpdates(
	                        LocationManager.NETWORK_PROVIDER,
	                        MIN_TIME_BW_UPDATES,
	                        MIN_DISTANCE_CHANGE_FOR_UPDATES,locationListener);
	                Log.d("Network", "Network Enabled");
	                if (locationManager != null) 
	                {
	                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	                    if (location != null) 
	                    {
	                        latitude = location.getLatitude();
	                        longitude = location.getLongitude();
	                    }
	                }
	            }
	            // if GPS Enabled get lat/long using GPS Services
	            if (isGPSEnabled) 
	            {
	                if (location == null) 
	                {
	                    locationManager.requestLocationUpdates(
	                            LocationManager.GPS_PROVIDER,
	                            MIN_TIME_BW_UPDATES,
	                            MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
	                    Log.d("GPS", "GPS Enabled");
	                    if (locationManager != null) 
	                    {
	                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	                        if (location != null) 
	                        {
	                            latitude = location.getLatitude();
	                            longitude = location.getLongitude();
	                        }
	                    }
	                }
	            }
	        }
	    } 
	    catch (Exception e) 
	    {
	        e.printStackTrace();
	    }
	    return location;
	}
	public double newlat() 
	{
		Location location = null;
		try
		{
			LocationManager locationManager;
			String context = Context.LOCATION_SERVICE;
			locationManager = (LocationManager) getSystemService(context);
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setCostAllowed(true);
			criteria.setPowerRequirement(Criteria.POWER_LOW);
			String provider = locationManager.getBestProvider(criteria, true);
			location = locationManager.getLastKnownLocation(provider);
			 //seconds and meter
			locationManager.requestLocationUpdates(provider, 6000, 10,locationListener);
		 
		}
		catch (Exception e)
		{
			
		}
		return updateWithNewLatitude(location);
	}
	public double newlong() 
	{
		Location location = null;
		try
		{
			LocationManager locationManager;
			String context = Context.LOCATION_SERVICE;
			locationManager = (LocationManager) getSystemService(context);
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setCostAllowed(true);
			criteria.setPowerRequirement(Criteria.POWER_LOW);
			String provider = locationManager.getBestProvider(criteria, true);
			location = locationManager.getLastKnownLocation(provider);
			locationManager.requestLocationUpdates(provider, 6000, 10,locationListener);
		}
		catch(Exception e)
		{
		}
		return updateWithNewLongitude(location);
	}
	private final LocationListener locationListener = new LocationListener() 
	{
		@Override
		public void onLocationChanged(Location location) 
		{
			updateWithNewLatitude(location);
			updateWithNewLongitude(location);
		}
		@Override
		public void onProviderDisabled(String provider) 
		{
			updateWithNewLatitude(null);
			updateWithNewLongitude(null);
		}
		@Override
		public void onProviderEnabled(String provider) 
		{
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) 
		{
		}
	};
	private double updateWithNewLatitude(Location location) 
	{

		// myLocationText = (TextView) findViewById(R.id.myLocationText);
		if (location != null) 
		{
			double lat = location.getLatitude();
			return lat;
			// double lng = location.getLongitude();
			// latLongString = "Lat:" + lat + "\nLong:" + lng;
		} 
		else 
		{
			return 0.0;
			// latLongString = "No location found";
		}
		// myLocationText.setText("Your Current Position is:\n" +
		// latLongString);
	}
	private double updateWithNewLongitude(Location location) 
	{

		// myLocationText = (TextView) findViewById(R.id.myLocationText);
		if (location != null) 
		{
			double lat = location.getLongitude();
			return lat;
			// double lng = location.getLongitude();
			// latLongString = "Lat:" + lat + "\nLong:" + lng;
		} 
		else 
		{
			return 0.0;
			// latLongString = "No location found";
		}
		// myLocationText.setText("Your Current Position is:\n" +
		// latLongString);
	}
}
