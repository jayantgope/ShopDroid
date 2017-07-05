package com.example.shopdroid2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session 
{
	private SharedPreferences prefsCustomers;

    public Session(Context context) 
    {
    	prefsCustomers = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public void setUsername(String username) 
    {
    	prefsCustomers.edit().putString("username", username).commit();
        //prefs.commit();
    }

    public String getUsername() 
    {
        String username = prefsCustomers.getString("username", null);
        return username;
    }
    public void destroySession()
    {
    	SharedPreferences.Editor editor = prefsCustomers.edit();
        editor.clear();
        editor.commit();
    }
}