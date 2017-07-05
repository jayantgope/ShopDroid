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

public class DepartmentWiseProductListWithoutLogin extends Activity 
{
	ProgressDialog dialog;
	private static final String TAG_NAME = "product_name";
	private static final String TAG_CAT = "category";
	private static final String TAG_PRICE = "price";
	private static final String TAG_STOCK = "stock";
	private static final String TAG_SHOP = "shop";
	private static final String TAG_BARCODE = "barcode";
	//private static final String TAG_product_id = "product_id";
	private Session session;
	private String prefsUsername;

	String prefShopUsername;
	private ShopSession shop_session;
	
	String shop_id,userid;
	String Keywords;
	ArrayList<HashMap<String, String>> productList=new ArrayList<HashMap<String,String>>();
	ListView list;
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_list);
		// Get the bundle
		Bundle bundle = getIntent().getExtras();

		// Extract the data…
		Keywords = bundle.getString("cat");
		
		session = new Session(getApplicationContext());
    	prefsUsername = session.getUsername();
    	
    	shop_session = new ShopSession(getApplicationContext());
    	prefShopUsername = shop_session.getUsername();
		// Extract the data…
		shop_id = bundle.getString("shop_username");
		userid=getIntent().getStringExtra("userid");
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
				dialog1 = new Dialog(DepartmentWiseProductListWithoutLogin.this);
				dialog1.setContentView(R.layout.custum_option);
				Button btnBestPrice = (Button) dialog1.findViewById(R.id.btnBestPrice);
				// if button is clicked, close the custom dialog
				btnBestPrice.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						Intent i=new Intent(DepartmentWiseProductListWithoutLogin.this,BestProductListWithoutLogin.class);
						i.putExtra("name",name);
						i.putExtra("cat",cat);
						startActivity(i);
						dialog1.dismiss();
						finish();
					}
				});
				Button btnBuy = (Button) dialog1.findViewById(R.id.btnBuy);
				// if button is clicked, close the custom dialog
				btnBuy.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
		
						dialog1.dismiss();
						if((prefsUsername == null))
						{
							dialog1.dismiss();
							Bundle bundle1 = new Bundle();

							//Add your data to bundle
							bundle1.putString("status", "transaction");
							Toast.makeText(DepartmentWiseProductListWithoutLogin.this, "Login to Continue.", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(getApplicationContext(), Login.class);
							intent.putExtras(bundle1);
							startActivity(intent);
						}
						else
						{
							dialog1.dismiss();
							HashMap<String,String> map =(HashMap<String,String>)list.getItemAtPosition(position);
							final String name = map.get(TAG_NAME);
							final String cat = map.get(TAG_CAT);
							final String price = map.get(TAG_PRICE);
							//final String product_id = map.get(TAG_product_id);
							final String barcode = map.get(TAG_BARCODE);
							final String shop = map.get(TAG_SHOP);
							Intent i=new Intent(getBaseContext(),PaymentOption.class);
							i.putExtra("name",name);
							i.putExtra("cat",cat);
							i.putExtra("price",price);
							i.putExtra("barcode",barcode);
							i.putExtra("userid",userid);
							i.putExtra("shop",map.get(TAG_SHOP));
							startActivity(i);
						}
					}
				});
				dialog1.show();
			}
		});
	}
	public void getData() 
	{
		dialog = ProgressDialog.show(DepartmentWiseProductListWithoutLogin.this, "Please wait..", "Getting details..", true, true);
		RequestParams params = new RequestParams();
		params.add("Keywords", Keywords);
		params.add("selected_shop_id", prefShopUsername);
		AsyncHttpClient client = new AsyncHttpClient();
		String url = URL1.departmentWiseProducts;
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
						String barcode = c.getString(TAG_BARCODE);
						HashMap<String, String> persons = new HashMap<String, String>();
						persons.put(TAG_NAME, product_name);
						persons.put(TAG_CAT, category);
						persons.put(TAG_PRICE, price);
						persons.put(TAG_STOCK, stock);
						persons.put(TAG_SHOP, shop);
						persons.put(TAG_BARCODE, barcode);
						productList.add(persons);
						count++;
					}
					ListAdapter adapter = new SimpleAdapter(DepartmentWiseProductListWithoutLogin.this,
					productList, R.layout.products_list_items,
					new String[] 
					{ TAG_SHOP,TAG_NAME, TAG_CAT, TAG_PRICE,
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
					Toast.makeText(getApplicationContext(),
							"No products available", Toast.LENGTH_SHORT).show();
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
