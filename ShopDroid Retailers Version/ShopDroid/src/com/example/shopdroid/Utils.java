package com.example.shopdroid;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

public class Utils 
{
	public static String getPath() 
	{
		String path = "";
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) 
		{
			path = Environment.getExternalStorageDirectory().getAbsolutePath();
		} 
		else if ((new File("/mnt/emmc")).exists()) 
		{
			path = "/mnt/emmc";
		} 
		else 
		{
			path = Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		return path + "/theAppGuruz";
	}

	public static boolean isOnline(Context context) 
	{
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) 
		{
			return true;
		}
		return false;
	}

	public static void showNetworkAlert(Context context) 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Network Alert");
		builder.setMessage("Please check your network connection and try again");
		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() 
		{

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
			}
		});
		builder.show();
	}
}
