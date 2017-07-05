package com.example.shopdroid2;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class PaymentOption extends Activity
{	
	ProgressDialog dialog;
	private static final String TAG_NAME = "product_name";
	private static final String TAG_CAT = "category";
	private static final String TAG_PRICE = "price";
	private static final String TAG_STOCK = "stock";
	private static final String TAG_SHOP = "shop";
	private static final String TAG_BARCODE = "barcode";
	//private static final String TAG_product_id = "product_id";
	private Button btBuy;
	TextView edtProductCode, edtBarCode, edtProductName, edtLocation, edtStock, edtUnitCost, edtCategory, edtQuantity;
	TextView tvProductCode, tvSelectedShop, tvSelectedShopId, tvBarCode, tvProductName, tvLocation, tvStock, tvUnitCost, tvCategory, tvQuantity, tvWalletBalance,tvProductQuantity;
	String product_name, category, stock, shop, barcode, location, price, walletBalance;
	private Session session;
	private ShopSession shop_session;
	private String prefsUsername, prefShopUsername;
	private String formattedDate;
	private SimpleDateFormat df;
	String shop_id,userid;
	ArrayList<HashMap<String, String>> productList=new ArrayList<HashMap<String,String>>();
	ListView list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		session = new Session(getApplicationContext());
    	prefsUsername = session.getUsername();
    	shop_session = new ShopSession(getApplicationContext());
    	prefShopUsername = shop_session.getUsername();
    	if ((prefShopUsername == null))
		{
    		Toast.makeText(PaymentOption.this, "Select Shop to Continue.", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(getApplicationContext(), NearestShopsWithoutLogin.class);
			startActivity(intent);
		}
    	else if((prefsUsername == null))
		{
    		Bundle bundle1 = new Bundle();
    		bundle1.putString("status", "transaction");
    		Toast.makeText(PaymentOption.this, "Login to Continue.", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(getApplicationContext(), Login.class);
			intent.putExtras(bundle1);
			startActivity(intent);
		}
    	else
    	{
		setContentView(R.layout.payment_option);
		edtProductCode = (TextView)findViewById(R.id.tvProductCode);
		edtBarCode = (EditText)findViewById(R.id.etBarCode);
		edtProductName = (TextView)findViewById(R.id.tvProductName);
		edtLocation = (TextView)findViewById(R.id.tvProductLocation);
		edtCategory = (TextView)findViewById(R.id.tvProductCategory);
		edtStock = (TextView)findViewById(R.id.tvProductQuantity);
		edtUnitCost = (TextView)findViewById(R.id.tvProductUnitCost);
		edtQuantity = (EditText)findViewById(R.id.etProductQuantityToBuy);
		
		tvSelectedShop = (TextView)findViewById(R.id.tvSelectedShop);
		tvSelectedShopId = (TextView)findViewById(R.id.tvSelectedShopId);
		tvProductName = (TextView)findViewById(R.id.tvvProductName);
		tvWalletBalance = (TextView)findViewById(R.id.tvWalletBalance);
		tvLocation = (TextView)findViewById(R.id.tvvProductLocation);
		tvCategory = (TextView)findViewById(R.id.tvvProductCategory);
		tvStock = (TextView)findViewById(R.id.tvvProductQuantity);
		tvUnitCost = (TextView)findViewById(R.id.tvvProductUnitCost);
		tvProductQuantity = (TextView)findViewById(R.id.tvvProductQuantityToBuy);
		
		tvSelectedShopId.setText(prefShopUsername);
		
		edtBarCode.setEnabled(false);
		
		
		edtProductName.setVisibility(View.GONE);
		edtLocation.setVisibility(View.GONE);
		edtCategory.setVisibility(View.GONE);
		edtStock.setVisibility(View.GONE);
		edtUnitCost.setVisibility(View.GONE);
		edtQuantity.setVisibility(View.GONE);
		
		tvProductName.setVisibility(View.GONE);
		tvLocation.setVisibility(View.GONE);
		tvCategory.setVisibility(View.GONE);
		tvStock.setVisibility(View.GONE);
		tvUnitCost.setVisibility(View.GONE);
		tvProductQuantity.setVisibility(View.GONE);
		
		btBuy = (Button)findViewById(R.id.btBuy);
		btBuy.setVisibility(View.GONE);
		
		
		Bundle bundle = getIntent().getExtras();
		session = new Session(getApplicationContext());
    	prefsUsername = session.getUsername();

		// Extract the data…
		/*shop_id = bundle.getString("shop_id");
		userid=getIntent().getStringExtra("userid");*/
		// Extract the data
		getBalance();
    	}
		
	}
	public void onClickScanBarcode(View v)
	{
		  try 
		  {
			  Intent intent = new Intent(
			  "com.google.zxing.client.android.SCAN");
			  intent.putExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
			  startActivityForResult(intent, 0);
	
		  } 
		  catch (Exception e) 
		  {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
			  Toast.makeText(getApplicationContext(), "ERROR:" + e, 1).show();
		  }
	}
	
	public void onClickBuy(View v)
	{
		Calendar c = Calendar.getInstance();
		//System.out.println("Current time => " + c.getTime());
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formattedDate = df.format(c.getTime());
		
		int walletBalance = Integer.parseInt(tvWalletBalance.getText().toString());
		String shopId = tvSelectedShopId.getText().toString();
		String barcode = edtBarCode.getText().toString();
		int stock = Integer.parseInt(edtStock.getText().toString());
		int quantity = Integer.parseInt(edtQuantity.getText().toString());
		int unitCost = Integer.parseInt(edtUnitCost.getText().toString());
		int stock_left = stock - quantity;
		int wallet_balance_left = walletBalance - (unitCost * quantity);
		if(stock < quantity)
		{
			Toast.makeText(getApplicationContext(), "Stock not available, Please enter valid quantity.", Toast.LENGTH_SHORT).show();
		}
		else if((quantity * unitCost) > walletBalance )
		{
			Toast.makeText(getApplicationContext(), "You don't have enough credit balance in Wallet.", Toast.LENGTH_SHORT).show();
		}
		else
		{
			//Toast.makeText(getApplicationContext(), product_id, Toast.LENGTH_SHORT).show();
			dialog=ProgressDialog.show(PaymentOption.this, "Please wait..", "Saving details..",true,true);
			RequestParams params = new RequestParams();
			params.add("barcode", barcode);
			params.add("customer_id", prefsUsername);
			params.add("shop_id", shopId);
			params.add("date", formattedDate);
			params.add("quantity", Integer.toString(quantity));
			params.add("stock_left", Integer.toString(stock_left));
			params.add("wallet_balance_left", Integer.toString(wallet_balance_left));
			AsyncHttpClient client = new AsyncHttpClient();
			String url = URL1.buyProduct;
			client.post(url, params, new AsyncHttpResponseHandler() 
			{
				@Override
				public void onSuccess(String response) 
				{
					dialog.dismiss();
					try 
					{
						JSONObject jObj = new JSONObject(response);
						Toast.makeText(getApplicationContext(), jObj.getString("msg"),Toast.LENGTH_SHORT).show();
						if(jObj.getString("msg").contains("Failed"))
						{
		
						}
						else
						{
							finish();
						}
					} 
					catch (Exception e)
					{
					}

				}
				@Override
				public void onFailure(int statusCode, Throwable error, String content) 
				{
					dialog.dismiss();
				}
			});
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) 
        {
        	if (resultCode == RESULT_OK) 
        	{
        		edtBarCode.setText(data.getStringExtra("SCAN_RESULT").toString());
        		barcode = data.getStringExtra("SCAN_RESULT").toString();
        		getData();
        	} 
        	else if (resultCode == RESULT_CANCELED) 
        	{
        		Toast.makeText(getApplicationContext(), "Barcode Couldn't be Scanned.", Toast.LENGTH_SHORT).show();
        	}
        }
	}
	public void getBalance() 
	{
		dialog = ProgressDialog.show(PaymentOption.this, "Please wait..", "Getting details..", true, true);
		RequestParams params = new RequestParams();
		params.add("Username", prefsUsername);
		AsyncHttpClient client = new AsyncHttpClient();
		String url = URL1.getWalletBalance;
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
						walletBalance = c.getString("wallet_amount");
						count++;
					}
					tvWalletBalance.setText(walletBalance);
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
	public void getData() 
	{
		dialog = ProgressDialog.show(PaymentOption.this, "Please wait..","Getting details..", true, true);
		RequestParams params = new RequestParams();
		params.add("Barcode", barcode);
		params.add("selected_shop_id", prefShopUsername);
		AsyncHttpClient client = new AsyncHttpClient();
		String url = URL1.getProductsUsingBarcode;
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
						product_name = c.getString(TAG_NAME);
						category = c.getString(TAG_CAT);
						price = c.getString(TAG_PRICE);
						stock = c.getString(TAG_STOCK);
						shop = c.getString(TAG_SHOP);
						location = c.getString("location");
						//product_id = c.getString(TAG_product_id);
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
					edtBarCode.setText(barcode);
					edtCategory.setText(category);
					edtProductName.setText(product_name);
					edtLocation.setText(location);
					edtStock.setText(stock);
					edtUnitCost.setText(price);
					
					edtProductName.setVisibility(View.VISIBLE);
					edtLocation.setVisibility(View.VISIBLE);
					edtCategory.setVisibility(View.VISIBLE);
					edtStock.setVisibility(View.VISIBLE);
					edtUnitCost.setVisibility(View.VISIBLE);
					edtQuantity.setVisibility(View.VISIBLE);
					
					tvProductName.setVisibility(View.VISIBLE);
					tvLocation.setVisibility(View.VISIBLE);
					tvCategory.setVisibility(View.VISIBLE);
					tvStock.setVisibility(View.VISIBLE);
					tvUnitCost.setVisibility(View.VISIBLE);
					tvProductQuantity.setVisibility(View.VISIBLE);
					
					btBuy.setVisibility(View.VISIBLE);
					
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
