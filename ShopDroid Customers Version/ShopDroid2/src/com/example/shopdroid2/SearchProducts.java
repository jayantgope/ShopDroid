package com.example.shopdroid2;
import com.example.shopdroid2.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SearchProducts extends Activity
{
	String keywords;
	EditText etKeywords;
	String barcode;
	String prefShopUsername;
	private ShopSession shop_session;
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		shop_session = new ShopSession(getApplicationContext());
    	prefShopUsername = shop_session.getUsername();
    	if((prefShopUsername == null))
		{
    		Bundle bundle1 = new Bundle();
    		bundle1.putString("status", "main_activity");
    		Toast.makeText(SearchProducts.this, "Select Shop First.", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(getApplicationContext(), NearestShops.class);
			intent.putExtras(bundle1);
			startActivity(intent);
		}
    	else
    	{
    		setContentView(R.layout.search_products);
    		etKeywords = (EditText)findViewById(R.id.etKeywords);
    	}
	}
	public void onClickSearch(View v)
	{
		
		String keywords = etKeywords.getText().toString();
		Intent i = new Intent(getApplicationContext(), SearchProductListWithoutLogin.class);
		   
		//Create the bundle
		Bundle bundle = new Bundle();

		//Add your data to bundle
		bundle.putString("Keywords", keywords);

		//Add the bundle to the intent
		i.putExtras(bundle);

		//Fire that second activity
		startActivity(i);
		
	}
	public void searchByDepartment(View v)
	{
		Intent i = new Intent(getApplicationContext(), DepartmentList.class);
		startActivity(i);
	}
	public void addListenerOnScanBarcode(View v)
	{
		/*Intent i = new Intent(getApplicationContext(), GetProductsUsingBarcode.class);
		startActivity(i);*/
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
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
 	{
		super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == 0) 
	    {
		 	if (resultCode == RESULT_OK) 
		 	{
		 		//Toast.makeText(getApplicationContext(), "Scanned Successful.", Toast.LENGTH_LONG).show();
		 		barcode = data.getStringExtra("SCAN_RESULT").toString();
		 		Intent i = new Intent(getApplicationContext(), GetProductsUsingBarcode.class);
			   
	 			//Create the bundle
		 		Bundle bundle = new Bundle();
	
		 		//Add your data to bundle
		 		bundle.putString("barcode", barcode);
	
		 		//Add the bundle to the intent
		 		i.putExtras(bundle);
	
		 		//Fire that second activity
		 		startActivity(i);
		 	} 
		 	else if (resultCode == RESULT_CANCELED) 
		 	{
		 		Toast.makeText(getApplicationContext(), "Barcode Couldn't be Scanned.", Toast.LENGTH_SHORT).show();
		 	}
	    }
 	}
}
