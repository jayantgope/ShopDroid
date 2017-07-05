package com.example.shopdroid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class AddNewProductCategory extends Activity
{
	SQLiteDatabase db;
	EditText etNewProductCategory;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_product_category);
		
		etNewProductCategory = (EditText)findViewById(R.id.etNewProductCategory);
		try
		{
			db=openOrCreateDatabase("ShopDroid", Context.MODE_PRIVATE, null);
	        db.execSQL("CREATE TABLE IF NOT EXISTS product_categories(category_id INTEGER PRIMARY KEY AUTOINCREMENT,category VARCHAR)");
		}
		catch (Exception e)
		{
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}
	
	public void onClickAddNewProductCategory(View v)
	{
		String productCategory = etNewProductCategory.getText().toString();
		if(productCategory.isEmpty())
		{
			Toast.makeText(getApplicationContext(), "Product category cannot be left blank.", Toast.LENGTH_SHORT).show();
		}
		else
		{
			try 
			{
	            db.execSQL("INSERT INTO product_categories (category) VALUES('" +etNewProductCategory.getText().toString()+ "');");
	            Toast to = Toast.makeText(getApplicationContext(), "New product category added successfully.", Toast.LENGTH_SHORT);
	            to.show();
	            finish();
	            /*Intent intent = new Intent(getApplicationContext(), AddProduct.class);
	    		startActivity(intent);*/
			}
			catch(Exception e)
			{
				Toast toast= Toast.makeText(getApplicationContext(),  e.toString() , Toast.LENGTH_SHORT);
				toast.show();
			}
		}
       /* Toast toast= Toast.makeText(getApplicationContext(),"Inserted", Toast.LENGTH_SHORT);
        toast.show();*/
	}
	
	public void onClickCancel(View v)
	{
		finish();
	}

}
