package com.example.shopdroid2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ShopSession 
{

	private SharedPreferences prefsShops;

    public ShopSession(Context context) 
    {
    	prefsShops = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public void setUsername(String username) 
    {
    	prefsShops.edit().putString("shop_username", username).commit();
        //prefs.commit();
    }
    public String getUsername() 
    {
        String username = prefsShops.getString("shop_username", null);
        return username;
    }
    public void destroySession()
    {
    	SharedPreferences.Editor editor = prefsShops.edit();
        editor.clear();
        editor.commit();
    }
}
