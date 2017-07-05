package com.example.shopdroid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.ListActivity;

public class ProductCategories extends Activity
{
	private ArrayList<String> results = new ArrayList<String>();
	private SQLiteDatabase db;
    ListView lvCategories;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_categories);
		
		/*lvCategories = (ListView)findViewById(R.id.lvProductCategories);
		
		openAndQueryDatabase();
		 
		populateCategories();*/
		/*
		db=openOrCreateDatabase("ShopDroid", Context.MODE_PRIVATE, null);
		Cursor c=db.rawQuery("SELECT * FROM product_categories WHERE category_id = 1", null);
		categoryId.clear();
        if(c.moveToFirst())
        {
        	Toast toast = Toast.makeText(getApplicationContext(), c.getString(1), Toast.LENGTH_LONG);
        	toast.show();
       
        }
		*/
		/*category.clear();
		categoryId.clear();
        if(c.moveToFirst())
        {
        	do
        	{
        		categoryId.add(c.getInt(1));
        		category.add(c.getString(2));
        	}
        	while (c.moveToNext());
       
        }
        DisplayAdapter disadpt = new DisplayAdapter(ProductCategories.this,categoryId, category);
        lvCategories.setAdapter(disadpt);
        c.close();*/
	}
	
	/*public void openAndQueryDatabase()
	{
		try 
		{
			db=openOrCreateDatabase("ShopDroid", Context.MODE_PRIVATE, null);
			Cursor c = db.rawQuery("SELECT * FROM product_categories", null);
			if (c != null ) 
			{
				if (c.moveToFirst()) 
				{
					do 
					{
						String category = c.getString(c.getColumnIndex("category"));
						results.add(" " + category);
					}while (c.moveToNext());
				}
			}
		}
		catch (SQLiteException se ) 
		{
			Log.e(getClass().getSimpleName(), "Could not create or Open the database");
		} 
		finally 
		{
			if (db != null)
			db.close();
		}
	}
	
	public void populateCategories()
	{
		setListAdapter(new ArrayAdapter<String>(this, R.id.lvProductCategories, results));
		getListView().setTextFilterEnabled(true);
	}*/
	
	public void onClickNew(View v)
	{
		Intent intent = new Intent(getApplicationContext(), AddNewProductCategory.class);
		startActivity(intent);
	}
	
	public void onClickClose(View v)
	{
		finish();
	}
	
	public void onClickDelete(View v)
	{
		
	}
	
	
}
