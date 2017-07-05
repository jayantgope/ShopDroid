package com.example.shopdroid;

import com.dropbox.client2.session.Session.AccessType;

public class Constants 
{
	public static final String OVERRIDEMSG = "File name with this name already exists.Do you want to replace this file?";
	final static public String DROPBOX_APP_KEY = "bhpv8q0rwhr78nv";
	final static public String DROPBOX_APP_SECRET = "6a9sw158qk433oj";
	public static boolean mLoggedIn = false;

	final static public AccessType ACCESS_TYPE = AccessType.DROPBOX;

	final static public String ACCOUNT_PREFS_NAME = "prefs";
	final static public String ACCESS_KEY_NAME = "ACCESS_KEY";
	final static public String ACCESS_SECRET_NAME = "ACCESS_SECRET";
}
