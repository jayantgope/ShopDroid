package com.example.shopdroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session 
{
	private SharedPreferences prefs;

    public Session(Context context) 
    {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public void setUsername(String username) 
    {
        prefs.edit().putString("username", username).commit();
        //prefs.commit();
    }

    public String getUsername() 
    {
        String username = prefs.getString("username", null);
        return username;
    }
    public void destroySession()
    {
    	SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }
    
}