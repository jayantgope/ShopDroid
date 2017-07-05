package com.example.shopdroid;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.*;

public class Synchronize extends Activity
{
	EditText etUsername,etPassword,etfName,etlName,etAddress,etPinCode,etContactNo;
	Spinner spnState,spnCity;
	private Session session;
	String prefsUsername;
	private String formattedDate;
	private SimpleDateFormat df;
	String ProductCode, BarCode, ProductName, Category, Location, Quantity, UnitCost, Image, ShopId, DateAdded;
	SQLiteDatabase db;
	ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		session = new Session(getApplicationContext());
		prefsUsername = session.getUsername();
		if((prefsUsername == null))
		{
	            Intent i = new Intent(getApplicationContext(), SignIn.class);
	            startActivity(i);
		 }
		else
		{
			try
			{
				db = openOrCreateDatabase("ShopDroid", Context.MODE_PRIVATE, null);
	            String select = "Select * FROM products";
	            Cursor allrows = db.rawQuery(select, null);
	            if (null!=allrows) 
	            {
	                while (allrows.moveToNext()) 
	                {
	                	//int ProductId = Integer.parseInt(allrows.getString(allrows.getColumnIndex("_id")));
	                	String Barcode = allrows.getString(allrows.getColumnIndex("bar_code"));
	                	int Quantity = Integer.parseInt(allrows.getString(allrows.getColumnIndex("quantity")));
	                	String Shopid = prefsUsername;
	                	//UpdateProductStock (Barcode, Shopid);
	                	SynchronizeProducts(Barcode);
	                }
	            }
	        } 
			catch (Exception e) 
			{
				Toast.makeText(getApplicationContext(), "LOL", Toast.LENGTH_SHORT).show();
	        }
		}
	}
	public void UpdateProductStock(String barcode, String shop_id)
	{
		dialog=ProgressDialog.show(Synchronize.this, "Please wait..", "Synchronizing..",true,true);
		RequestParams params = new RequestParams();
	    params.add("barcode", barcode);
	    params.add("shop_id", shop_id);
		AsyncHttpClient client = new AsyncHttpClient();
		String url = URL1.updateStock;
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
					String Barcode = null;
					int product_stock = 0;
					while (count < jArray.length()) 
					{
						JSONObject c = jArray.getJSONObject(count);
						product_stock = Integer.parseInt(c.getString("quantity"));
						Barcode = c.getString("barcode");
						UpdateStock(product_stock ,Barcode);
						count++;
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
		//SynchronizeProducts(barcode);
	}
	public void UpdateStock(int stock, String barcode)
	{
		Cursor allrows = null;
		int product_stock = 0;
		try
		{
			db = openOrCreateDatabase("ShopDroid", Context.MODE_PRIVATE, null);
	        String select = "Select * FROM products WHERE bar_code = '"+barcode+"'";
	        allrows = db.rawQuery(select, null);
	        if (null!=allrows) 
	        {
	            while (allrows.moveToNext()) 
	            {
	            	product_stock = Integer.parseInt(allrows.getString(allrows.getColumnIndex("quantity")));
	            }
	        }
	        product_stock = product_stock - stock;
	        String sql = "UPDATE products SET quantity = "+product_stock+" WHERE bar_code = '"+barcode+"'";
	        Toast.makeText(getApplicationContext(), sql, Toast.LENGTH_SHORT).show();
	        db.execSQL(sql);
	    } 
		catch (Exception e) 
		{
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
	    }
		SynchronizeProducts(barcode);
	}
	public void SynchronizeProducts(String Barcode)
	{
		Cursor allrows = null;
		try
		{
			Calendar c = Calendar.getInstance();
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			formattedDate = df.format(c.getTime());
			db = openOrCreateDatabase("ShopDroid", Context.MODE_PRIVATE, null);
	        String select = "Select * FROM products WHERE bar_code = '"+Barcode+"'";
	        allrows = db.rawQuery(select, null);
	        if (null!=allrows) 
	        {
	            while (allrows.moveToNext()) 
	            {
	            	ProductCode = allrows.getString(allrows.getColumnIndex("product_code"));
	            	BarCode = allrows.getString(allrows.getColumnIndex("bar_code"));
	            	ProductName = allrows.getString(allrows.getColumnIndex("product_name"));
	            	Category = allrows.getString(allrows.getColumnIndex("category"));
	            	Location = allrows.getString(allrows.getColumnIndex("location"));
	            	Quantity = allrows.getString(allrows.getColumnIndex("quantity"));
	            	UnitCost = allrows.getString(allrows.getColumnIndex("unit_cost"));
	            	Image = allrows.getString(allrows.getColumnIndex("image"));
	            	ShopId = prefsUsername;
	            	DateAdded = formattedDate;
	            }
	        }
	    } 
		catch (Exception e) 
		{
			//Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
	    }
		
		dialog=ProgressDialog.show(Synchronize.this, "Please wait..", "Synchronizing..",true,true);
		RequestParams params = new RequestParams();
	    params.add("product_code", ProductCode);
	    params.add("barcode", BarCode);
	    params.add("product_name", ProductName);
	    params.add("category", Category);
	    params.add("location", Location);
	    params.add("quantity", Quantity);
	    params.add("unit_cost", UnitCost);
	    params.add("image", Image);
	    params.add("shop_id", ShopId);
	    params.add("date_added", DateAdded);
		AsyncHttpClient client = new AsyncHttpClient();
		String url = URL1.synchronize;
		client.post(url, params, new AsyncHttpResponseHandler() 
		{
			@Override
			public void onSuccess(String response) 
			{
				// Hide Progress Dialog
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
