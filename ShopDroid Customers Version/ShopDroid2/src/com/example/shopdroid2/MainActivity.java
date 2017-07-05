package com.example.shopdroid2;

import org.json.JSONObject;

import com.example.shopdroid2.UserPanel.FetchLocation;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity 
{
	private Session session;
	String prefsUsername;
	Button btLogin, btLogOut;
	String latitude, longitude;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btLogin = (Button)findViewById(R.id.btLogin);
		btLogOut = (Button)findViewById(R.id.btLogout);
		session = new Session(getApplicationContext());
    	prefsUsername = session.getUsername();
		if((prefsUsername == null))
		{
			btLogin.setVisibility(View.VISIBLE);   
			btLogOut.setVisibility(View.GONE);
		}
		else
		{
	    		btLogOut.setVisibility(View.VISIBLE);
	    		btLogin.setVisibility(View.GONE); 
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		int id = item.getItemId();
		if (id == R.id.action_settings) 
		{
			FetchLocation fd=new FetchLocation();
			fd.execute();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void addListenerOnLocateShop (View v)
    {
		if((prefsUsername == null))
		{
			if(latitude==null||longitude==null)
			{
				Toast.makeText(getBaseContext(), "Please Turn on your Gps", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Intent intent = new Intent(getApplicationContext(), NearestShopsWithoutLogin.class);
		    	intent.putExtra("latitude", latitude);
		    	intent.putExtra("longitude", longitude);
				startActivity(intent);
			}
		}
		else
		{
			Intent i=new Intent(getBaseContext(),NearestShops.class);
			i.putExtra("userid",prefsUsername);
			startActivity(i);
		}
    }
	public void addListenerOnSearchProducts(View v)
    {
    	Intent intent = new Intent(getApplicationContext(), SearchProducts.class);
		startActivity(intent);
    }
	public void addListenerOnRegister(View v)
    {
    	Intent intent = new Intent(getApplicationContext(), Registration.class);
		startActivity(intent);
    }
	
	public void addListenerOnSelectShop(View v)
    {
    	Intent intent = new Intent(getApplicationContext(), SelectShop.class);
		startActivity(intent);
    }
	public void addListenerOnDirectPurchase(View v)
	{
		Intent intent = new Intent(getApplicationContext(), PaymentOption.class);
		startActivity(intent);
	}
	public void addListenerOnSelectedShop(View v)
	{
		Intent intent = new Intent(getApplicationContext(), SelectShop.class);
		startActivity(intent);
	}
	
	public void addListenerOnLogin(View v)
    {
		Bundle bundle1 = new Bundle();
		bundle1.putString("status", "main_activity");
		Intent intent = new Intent(getApplicationContext(), Login.class);
		intent.putExtras(bundle1);
		startActivity(intent);
    }
	public void addListenerOnLogout(View v)
    {
		session.destroySession();
    	btLogOut.setVisibility(View.GONE);
    	btLogin.setVisibility(View.VISIBLE);
    	Toast.makeText(getApplicationContext(), "Logged out successfully.",Toast.LENGTH_SHORT).show();
    	FetchLocation fd=new FetchLocation();
		fd.execute();
    }
	@Override
	protected void onResume() 
	{
		super.onResume();
		FetchLocation fd=new FetchLocation();
		fd.execute();
	}
	LocationGetter location;

	class FetchLocation extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... params) 
		{
			location=new LocationGetter();
			runOnUiThread(new Runnable() 
			{
				public void run() 
				{
					try
					{
						latitude= Double.toString(location.getLocation(MainActivity.this).getLatitude());
						longitude= Double.toString(location.getLocation(MainActivity.this).getLongitude());
					}
					catch(Exception e)
					{
						
					}
				}
			});
			return null;
		}
		@Override
		protected void onPostExecute(Void result) 
		{
			super.onPostExecute(result);
			runOnUiThread(new Runnable() 
			{
				public void run() 
				{
					if(latitude==null||longitude==null)
					{
						
					}
					else
					{
						Toast.makeText(getBaseContext(), "Current Location "+latitude+","+longitude, Toast.LENGTH_SHORT).show();
					}		
				}
			});
		}
	}		
}
