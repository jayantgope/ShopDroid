package com.example.shopdroid2;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class NearestShops extends Activity
{
	ProgressDialog dialog;
 	String prefShopUsername;
 	private ShopSession session;
    private static final String TAG_SHOP_NAME = "shopname";
    private static final String TAG_ID = "username";
    private static final String TAG_NAME = "state";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_ADD ="city";
    private static final String TAG_DISTANCE ="distance";
    String userid;
    ArrayList<HashMap<String, String>> shopList;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearest_shops);
        
        session = new ShopSession(getApplicationContext());
    	//prefShopUsername = session.getUsername();
        
        userid=getIntent().getStringExtra("userid");
        list = (ListView) findViewById(R.id.lvNearestShops);
        shopList = new ArrayList<HashMap<String,String>>();
        getData();
        
        list.setOnItemClickListener(new OnItemClickListener() 
        {
		   @Override
		   public void onItemClick(AdapterView<?> listView, View view, int position, long id) 
		   {
			   // Get the cursor, positioned to the corresponding row in the result set
			   //Cursor cursor = (Cursor) listView.getItemAtPosition(position);
			   HashMap<String,String> map =(HashMap<String,String>)listView.getItemAtPosition(position);
			   String value = map.get("username");
			   
			   prefShopUsername = value;
			   session.setUsername(prefShopUsername);
			   Toast.makeText(getApplicationContext(), "Selected Shop : " + value, Toast.LENGTH_SHORT).show();
			   
			   Intent i=new Intent(getBaseContext(),ProductList.class);
			   i.putExtra("shop_id",value);
			   i.putExtra("userid", userid);
			   startActivity(i);
		   }
        });
    }
    public void getData()
    {
    	dialog=ProgressDialog.show(NearestShops.this, "Please wait..", "Getting details..",true,true);
		RequestParams params = new RequestParams();
		params.add("userid", userid);
		AsyncHttpClient client = new AsyncHttpClient();
		String url = URL1.nearestShop;
		client.post(url, params, new AsyncHttpResponseHandler() 
		{
			@Override
			public void onSuccess(String response) 
			{
				dialog.dismiss();
				try 
				{
		            JSONArray jArray = new JSONArray(response);
		            int count=0;
		           while(count<jArray.length())
		           {
		        	   JSONObject c = jArray.getJSONObject(count);
		        	   String shopname = c.getString(TAG_SHOP_NAME);
		        	   String username = c.getString(TAG_ID);
		        	   String address = c.getString(TAG_ADDRESS);
		        	   String name = c.getString(TAG_NAME);
		        	   String city = c.getString(TAG_ADD);
		        	   String dis = c.getString(TAG_DISTANCE);
		        	   HashMap<String,String> shops = new HashMap<String,String>();
		        	   shops.put(TAG_SHOP_NAME,shopname);
		        	   shops.put(TAG_ID,username);
		        	   shops.put(TAG_ADDRESS,address);
		        	   shops.put(TAG_NAME,name);
		        	   shops.put(TAG_ADD,city);
		        	   shops.put(TAG_DISTANCE,dis+" (KM)");
		        	   shopList.add(shops);
		        	   count++;
		           }
		           ListAdapter adapter = new SimpleAdapter(
                    NearestShops.this, shopList, R.layout.list_item,
                    new String[]
            		{
                    		TAG_SHOP_NAME,TAG_ADDRESS,TAG_NAME,TAG_ADD,TAG_DISTANCE
            		},
                    new int[]
            		{
                    		R.id.shopname, R.id.username, R.id.state, R.id.city,R.id.distance
            		});
		            list.setAdapter(adapter);
		        } 
		        catch (NullPointerException e)
	            {
	            	Toast.makeText(getApplicationContext(), "No Shops available in your location.", Toast.LENGTH_SHORT).show();
	            }
		        catch (JSONException e) 
				{
		            e.printStackTrace();
		        }

			}
			@Override
			public void onFailure(int statusCode, Throwable error,String content) 
			{
				dialog.dismiss();
			}
		});
    }
}
