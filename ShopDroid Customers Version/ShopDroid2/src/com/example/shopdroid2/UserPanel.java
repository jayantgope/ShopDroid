package com.example.shopdroid2;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UserPanel extends Activity 
{
	String userid;
	String latitude,longitude;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_panel);
		userid=getIntent().getStringExtra("userid");
	}
	public void nearestShop(View v)
	{
		Intent i=new Intent(getBaseContext(),NearestShops.class);
		i.putExtra("userid",userid);
		startActivity(i);
	}
	public void selectShop(View v)
	{
		
	}
	public void logout(View v)
	{
	    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	    SharedPreferences.Editor editor = preferences.edit();
		editor.putString("emailid","");
		editor.putString("password","");
		editor.commit();
		finish();
	    Intent i1 = new Intent(getBaseContext(), Login.class);
		i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i1);
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
						latitude= Double.toString(location.getLocation(UserPanel.this).getLatitude());
						longitude= Double.toString(location.getLocation(UserPanel.this).getLongitude());
				
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
						RequestParams params = new RequestParams();
						params.add("userid", userid);
						params.add("latitude", latitude);
						params.add("longitude", longitude);
						AsyncHttpClient client = new AsyncHttpClient();
						String url = URL1.updateLocation;
						client.post(url, params,
						new AsyncHttpResponseHandler() 
						{
							@Override
							public void onSuccess(String response) 
							{
								try 
								{
									JSONObject jObj = new JSONObject(response);
									Toast.makeText(getApplicationContext(),jObj.getString("msg"),Toast.LENGTH_SHORT).show();

								} 
								catch (Exception e) 
								{
									
								}

							}
							@Override
							public void onFailure(int statusCode,Throwable error, String content) 
							{
								
							}
						});
					}
				}
			});
		}
	}
}
