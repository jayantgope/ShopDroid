package com.example.shopdroid2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.shopdroid2.MainActivity.FetchLocation;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SelectShop extends Activity
{
	ProgressDialog dialog;
	private ShopSession shop_session;
	String prefShopUsername, address, city, state, pincode, shop_name;
	TextView tvSelectedShopId, tvShopAddress, tvShopName;
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		shop_session = new ShopSession(getApplicationContext());
    	prefShopUsername = shop_session.getUsername();
    	if((prefShopUsername == null))
		{
    		Toast.makeText(SelectShop.this, "Select Shop First.", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(getApplicationContext(), NearestShopsWithoutLogin.class);
			startActivity(intent);
		}
    	else
    	{
    		setContentView(R.layout.select_shop);
    		tvSelectedShopId = (TextView)findViewById(R.id.tvShopId);
    		tvShopAddress = (TextView)findViewById(R.id.tvShopAddress);
    		tvShopName = (TextView)findViewById(R.id.tvShopName);
    		tvSelectedShopId.setText("Shop Id : " + prefShopUsername);
    		getShopDetails();
    	}
	}
	public void getShopDetails() 
	{
		dialog = ProgressDialog.show(SelectShop.this, "Please wait..", "Getting details..", true, true);
		RequestParams params = new RequestParams();
		params.add("ShopId", prefShopUsername);
		AsyncHttpClient client = new AsyncHttpClient();
		String url = URL1.getShopDetails;
		client.post(url, params, new AsyncHttpResponseHandler() 
		{
			@Override
			public void onSuccess(String response) 
			{
				dialog.dismiss();
				try 
				{
					JSONArray jArray = new JSONArray(response);
					int count = 0;
					while (count < jArray.length()) 
					{
						JSONObject c = jArray.getJSONObject(count);
						shop_name = c.getString("shop_name");
						address = c.getString("address");
						city = c.getString("city");
						state = c.getString("state");
						pincode = c.getString("pincode");
						count++;
					}
					tvShopName.setText("Shop Name : " + shop_name);
					tvShopAddress.setText("Address : " + address + ", " + city + ", " + state + ", " + pincode + ".");
					/*ListAdapter adapter = new SimpleAdapter(PaymentOption.this,
							productList, R.layout.products_list_items,
							new String[] { TAG_SHOP,TAG_NAME, TAG_CAT, TAG_PRICE,
									TAG_STOCK, TAG_SHOP }, new int[] {R.id.shopname,
									R.id.product_name, R.id.category,
									R.id.price, R.id.stock, R.id.shop });
					list.setAdapter(adapter);*/
					if(!(count>0))
					{
						Toast.makeText(getBaseContext(), "No Products Found", Toast.LENGTH_SHORT).show();
					}
				}
				catch (JSONException e) 
				{
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "No products available", Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onFailure(int statusCode, Throwable error, String content) 
			{
				dialog.dismiss();
			}
		});
	}
	public void addListenerOnSearchProducts(View v)
	{
		Intent intent = new Intent(getApplicationContext(), SearchProducts.class);
		startActivity(intent);
	}
	public void addListenerOnRemoveShop(View v)
	{
		shop_session.destroySession();
    	Toast.makeText(getApplicationContext(), "Shop Removed Successfully.",Toast.LENGTH_SHORT).show();
	}
}
