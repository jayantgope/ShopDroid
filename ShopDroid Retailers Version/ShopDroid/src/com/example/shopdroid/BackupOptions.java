package com.example.shopdroid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.TokenPair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

public class BackupOptions extends Activity
{
	private final String DIR = "/";
	private File f;
	File backupDB;
	private boolean mLoggedIn, onResume;
	private DropboxAPI<AndroidAuthSession> mApi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.backup_options);
		AndroidAuthSession session = buildSession();
		mApi = new DropboxAPI<AndroidAuthSession>(session);

		// checkAppKeySetup();
		setLoggedIn(false);
	}
	private AndroidAuthSession buildSession() 
	{
		AppKeyPair appKeyPair = new AppKeyPair(Constants.DROPBOX_APP_KEY,
				Constants.DROPBOX_APP_SECRET);
		AndroidAuthSession session;

		String[] stored = getKeys();
		if (stored != null) {
			AccessTokenPair accessToken = new AccessTokenPair(stored[0],
					stored[1]);
			session = new AndroidAuthSession(appKeyPair, Constants.ACCESS_TYPE,
					accessToken);
		} 
		else 
		{
			session = new AndroidAuthSession(appKeyPair, Constants.ACCESS_TYPE);
		}

		return session;
	}
	private String[] getKeys() 
	{
		SharedPreferences prefs = getSharedPreferences(
				Constants.ACCOUNT_PREFS_NAME, 0);
		String key = prefs.getString(Constants.ACCESS_KEY_NAME, null);
		String secret = prefs.getString(Constants.ACCESS_SECRET_NAME, null);
		if (key != null && secret != null) 
		{
			String[] ret = new String[2];
			ret[0] = key;
			ret[1] = secret;
			return ret;
		} 
		else 
		{
			return null;
		}
	}
	
	public void onClickCancel(View v)
	{
		finish();
	}
	public void onClickSDCard(View v)
	{
		try 
		{
	        File sd = Environment.getExternalStorageDirectory();
	        File data = Environment.getDataDirectory();
	        Calendar cal = new GregorianCalendar();
		 	String am_pm = (cal.get(Calendar.AM_PM)==0)?"AM":"PM";
		 	String currentDate = cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR)+"/" + " Created on  "+cal.get(Calendar.HOUR)+" : "+cal.get(Calendar.MINUTE)+" : "+cal.get(Calendar.SECOND)+" "+am_pm;
		    String filename= "ShopDroid_Backup" + "_"+ cal.get(Calendar.DAY_OF_MONTH)+"_"+(cal.get(Calendar.MONTH)+1)+"_"+cal.get(Calendar.YEAR)+"_" + "_"+cal.get(Calendar.HOUR)+"_"+cal.get(Calendar.MINUTE)+"_"+cal.get(Calendar.SECOND)+"_"+am_pm +  ".db";
	        if (sd.canWrite()) 
	        {
	            String currentDBPath = "//data//" + "com.example.shopdroid"+ "//databases//" + "ShopDroid";
	            File root = new File(Environment.getExternalStorageDirectory(), "ShopDroid/Backup"); 
			    if (!root.exists()) 
			    {
			    	root.mkdirs();   
			    }
	            String backupDBPath = "/ShopDroid/Backup/" + filename;
	            File currentDB = new File(data, currentDBPath);
	            backupDB = new File(sd, backupDBPath);
	            FileChannel src = new FileInputStream(currentDB).getChannel();
	            FileChannel dst = new FileOutputStream(backupDB).getChannel();
	            dst.transferFrom(src, 0, src.size());
	            src.close();
	            dst.close();
	            Toast.makeText(getApplicationContext(), "Backup Successful !!",Toast.LENGTH_SHORT).show();
	            finish();
	        }
	    } 
		catch (Exception e) 
		{
	        Toast.makeText(getApplicationContext(), "Backup Failed!", Toast.LENGTH_SHORT).show();
	    }
	}
	public void onClickDropbox(View v)
	{
		try 
		{
	        File sd = Environment.getExternalStorageDirectory();
	        File data = Environment.getDataDirectory();
	        Calendar cal = new GregorianCalendar();
		 	String am_pm = (cal.get(Calendar.AM_PM)==0)?"AM":"PM";
		 	String currentDate = cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR)+"/" + " Created on  "+cal.get(Calendar.HOUR)+" : "+cal.get(Calendar.MINUTE)+" : "+cal.get(Calendar.SECOND)+" "+am_pm;
		    String filename= "ShopDroid_Backup" + "_"+ cal.get(Calendar.DAY_OF_MONTH)+"_"+(cal.get(Calendar.MONTH)+1)+"_"+cal.get(Calendar.YEAR)+"_" + "_"+cal.get(Calendar.HOUR)+"_"+cal.get(Calendar.MINUTE)+"_"+cal.get(Calendar.SECOND)+"_"+am_pm +  ".db";
	        String currentDBPath = "//data//" + "com.example.shopdroid"+ "//databases//" + "ShopDroid";
            File root = new File(Environment.getExternalStorageDirectory(), "ShopDroid/Backup"); 
		    if (!root.exists()) 
		    {
		    	root.mkdirs();   
		    }
            String backupDBPath = "/ShopDroid/Backup/" + filename;
            File currentDB = new File(data, currentDBPath);
            backupDB = new File(sd, backupDBPath);
            FileChannel src = new FileInputStream(currentDB).getChannel();
            FileChannel dst = new FileOutputStream(backupDB).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
            Toast.makeText(getApplicationContext(), "Backup Successful !!",Toast.LENGTH_SHORT).show();
            finish();
	    } 
		catch (Exception e) 
		{
	        Toast.makeText(getApplicationContext(), "Backup Failed!", Toast.LENGTH_SHORT).show();
	    }
			createDir();
			if (mLoggedIn) 
			{
				logOut();
			}
			if (Utils.isOnline(BackupOptions.this)) 
			{
				mApi.getSession().startAuthentication(BackupOptions.this);
				onResume = true;
			} 
			else 
			{
				Utils.showNetworkAlert(BackupOptions.this);
			}
			/*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			f = new File(Utils.getPath(),new Date().getTime()+".jpg");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			startActivityForResult(intent, TAKE_PHOTO);*/
	}
	private void logOut() 
	{
		mApi.getSession().unlink();
		clearKeys();
	}
	private void clearKeys() 
	{
		SharedPreferences prefs = getSharedPreferences(Constants.ACCOUNT_PREFS_NAME, 0);
		Editor edit = prefs.edit();
		edit.clear();
		edit.commit();
	}

	private void createDir() 
	{
		File dir = new File(Utils.getPath());
		if (!dir.exists()) 
		{
			dir.mkdirs();
		}
	}
	public void setLoggedIn(boolean loggedIn) 
	{
		mLoggedIn = loggedIn;
		if (loggedIn) 
		{
			UploadFile upload = new UploadFile(BackupOptions.this, mApi, DIR, backupDB);
			upload.execute();
			onResume = false;

		}
	}
	private void storeKeys(String key, String secret) 
	{
		SharedPreferences prefs = getSharedPreferences(
				Constants.ACCOUNT_PREFS_NAME, 0);
		Editor edit = prefs.edit();
		edit.putString(Constants.ACCESS_KEY_NAME, key);
		edit.putString(Constants.ACCESS_SECRET_NAME, secret);
		edit.commit();
	}

	private void showToast(String msg) 
	{
		Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
		error.show();
	}

	@Override
	protected void onResume() 
	{
		AndroidAuthSession session = mApi.getSession();

		if (session.authenticationSuccessful()) 
		{
			try 
			{
				session.finishAuthentication();

				TokenPair tokens = session.getAccessTokenPair();
				storeKeys(tokens.key, tokens.secret);
				setLoggedIn(onResume);
			} 
			catch (IllegalStateException e) 
			{
				showToast("Couldn't authenticate with Dropbox:"
						+ e.getLocalizedMessage());
			}
		}
		super.onResume();
	}
}
