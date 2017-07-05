package com.example.shopdroid2;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ProductListWithoutLogin extends Activity 
{
	ProgressDialog dialog;
	private static final String TAG_NAME = "product_name";
	private static final String TAG_CAT = "category";
	private static final String TAG_PRICE = "price";
	private static final String TAG_STOCK = "stock";
	private static final String TAG_SHOP = "shop";
	private static final String TAG_product_id = "product_id";
	String shop_id;
	ArrayList<HashMap<String, String>> productList=new ArrayList<HashMap<String,String>>();
	ListView list;
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_list);
		// Get the bundle
		Bundle bundle = getIntent().getExtras();

		// Extract the data…
		shop_id = bundle.getString("shop_id");
		list = (ListView) findViewById(R.id.lvProductList);
		productList = new ArrayList<HashMap<String, String>>();
		getData();
		list.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position,long id) 
			{
				HashMap<String,String> map =(HashMap<String,String>)list.getItemAtPosition(position);
				final String name = map.get(TAG_NAME);
				final String cat = map.get(TAG_CAT);
				//String ProductId = cursor.getString(cursor.getColumnIndexOrThrow("username"));
				Toast.makeText(getApplicationContext(), name+" "+cat, Toast.LENGTH_SHORT).show();
				final Dialog dialog1;
				dialog1 = new Dialog(ProductListWithoutLogin.this);
				dialog1.setContentView(R.layout.custum_option);
				Button btnBestPrice = (Button) dialog1.findViewById(R.id.btnBestPrice);
				btnBestPrice.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						Intent i=new Intent(ProductListWithoutLogin.this,BestProductListWithoutLogin.class);
						i.putExtra("name",name);
						i.putExtra("cat",cat);
						startActivity(i);
						dialog1.dismiss();
						finish();
					}
				});
			Button btnBuy = (Button) dialog1.findViewById(R.id.btnBuy);
			btnBuy.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					dialog1.dismiss();
					Toast.makeText(getBaseContext(), "Please login to continue", Toast.LENGTH_SHORT).show();
				}
			});
			dialog1.show();
			}
		});
	}
	public void getData() 
	{
		dialog = ProgressDialog.show(ProductListWithoutLogin.this, "Please wait..","Getting details..", true, true);
		RequestParams params = new RequestParams();
		params.add("shop_id", shop_id);
		AsyncHttpClient client = new AsyncHttpClient();
		String url = URL1.getProducts;
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
						String product_name = c.getString(TAG_NAME);
						String category = c.getString(TAG_CAT);
						String price = c.getString(TAG_PRICE);
						String stock = c.getString(TAG_STOCK);
						String shop = c.getString(TAG_SHOP);
						String product_id = c.getString(TAG_product_id);
						HashMap<String, String> persons = new HashMap<String, String>();
						persons.put(TAG_NAME, product_name);
						persons.put(TAG_CAT, category);
						persons.put(TAG_PRICE, price);
						persons.put(TAG_STOCK, stock);
						persons.put(TAG_SHOP, shop);
						persons.put(TAG_product_id, product_id);
						productList.add(persons);
						count++;
					}
					ListAdapter adapter = new SimpleAdapter(ProductListWithoutLogin.this,
					productList, R.layout.products_list_items,
					new String[] 
					{ 
						TAG_SHOP,TAG_NAME, TAG_CAT, TAG_PRICE,
						TAG_STOCK, TAG_SHOP 
					}, new int[] 
					{
						R.id.shopname,
						R.id.product_name, R.id.category,
						R.id.price, R.id.stock, R.id.shop 
					});
					list.setAdapter(adapter);
					
					if(!(count>0))
					{
						Toast.makeText(getBaseContext(), "No Products Found", Toast.LENGTH_SHORT).show();
					}
				}
				catch (JSONException e) 
				{
					e.printStackTrace();
					Toast.makeText(getApplicationContext(),"No products available", Toast.LENGTH_SHORT).show();
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
