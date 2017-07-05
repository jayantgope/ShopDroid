package com.example.shopdroid;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class ProductList extends Activity
{
	SQLiteDatabase db;
	ListView lvProducts;
	private SimpleCursorAdapter dataAdapter;
	private ProductsDBAdapter dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_list);

		dbHelper = new ProductsDBAdapter(this);
		dbHelper.open();
		displayListView();
	}
	@Override
	protected void onResume() 
	{
		super.onResume();
		displayListView();
	}
	private void displayListView() 
	{
		  Cursor cursor = dbHelper.fetchAllProducts();
			/*db = openOrCreateDatabase("ShopDroid", Context.MODE_PRIVATE, null);
			Cursor cursor = db.rawQuery("SELECT * FROM products", null);	*/
		  // The desired columns to be bound
		  String[] columns = new String[] 
		  {
		    ProductsDBAdapter.KEY_PRODUCT_CODE,
		    ProductsDBAdapter.KEY_PRODUCT_NAME,
		    ProductsDBAdapter.KEY_CATEGORY,
		    ProductsDBAdapter.KEY_UNIT_COST,
		    ProductsDBAdapter.KEY_QUANTITY
		  };

		  // the XML defined views which the data will be bound to
		  int[] to = new int[] 
		  { 
		    R.id.tvDisplayProdCode,
		    R.id.tvDisplayProdName,
		    R.id.tvDisplayCategory,
		    R.id.tvDisplayUnitCost,
		    R.id.tvDisplayStock
		  };

		  // create the adapter using the cursor pointing to the desired data 
		  //as well as the layout information
		  dataAdapter = new SimpleCursorAdapter
				  (
		    this, R.layout.listview_items, 
		    cursor, 
		    columns, 
		    to,
		    0);

		  ListView listView = (ListView) findViewById(R.id.lvProductListView);
		  // Assign adapter to ListView
		  listView.setAdapter(dataAdapter);
		  
		  listView.setOnItemClickListener(new OnItemClickListener() {
			   @Override
			   public void onItemClick(AdapterView<?> listView, View view, 
			     int position, long id) {
			   // Get the cursor, positioned to the corresponding row in the result set
			   Cursor cursor = (Cursor) listView.getItemAtPosition(position);

			   // Get the state's capital from this row in the database.
			   String ProductId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
			   
			   Intent i = new Intent(getApplicationContext(), UpdateProductDetails.class);
			   
			   //Create the bundle
			   Bundle bundle = new Bundle();

			   //Add your data to bundle
			   bundle.putString("Product_ID", ProductId);

			   //Add the bundle to the intent
			   i.putExtras(bundle);

			   //Fire that second activity
			   startActivity(i);
			   
			  /* Cursor MyCursor = dbHelper.fetchAllProducts(ProductId);
			   String Product_Code, Bar_Code, Product_Name, Category, Location, Quantity, Unit_Cost, Image;
			   Product_Code = MyCursor.getString(2);
			   Bar_Code = MyCursor.getString(3);
			   Product_Name = MyCursor.getString(4);
			   Category = MyCursor.getString(5);
			   Location = MyCursor.getString(6);
			   Quantity = MyCursor.getString(7);
			   Unit_Cost = MyCursor.getString(8);
			   Image = "Hello";
			   UpdateProductDetails update = new UpdateProductDetails(ProductId, Product_Code, Bar_Code, Product_Name, Category, Location, Quantity, Unit_Cost, Image);
			   Intent intent = new Intent(getApplicationContext(), UpdateProductDetails.class);
			   startActivity(intent);*/
			   }
			  });
		 }
		}
