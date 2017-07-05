package com.example.shopdroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity 
{
	//Button btnaddProduct, btnTransaction, btnTransactionList, btnProductList, btnReports, btnSettings;
	SQLiteDatabase db;
	private Session session;
	String prefsUsername;
	Button btSignOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=openOrCreateDatabase("ShopDroid", Context.MODE_PRIVATE, null);
        btSignOut = (Button)findViewById(R.id.btSignIn);
        session = new Session(getApplicationContext());
    	prefsUsername = session.getUsername();
		if((prefsUsername == null))
		{
	            btSignOut.setVisibility(View.GONE);
		 }
		else
		{
	    		btSignOut.setVisibility(View.VISIBLE);
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void addListenerOnAddProduct(View v)
    {
    	Intent intent = new Intent(getApplicationContext(), AddProduct.class);
		startActivity(intent);
    }
    
    public void addListenerOnTransaction(View v) 
    {
    	Intent intent = new Intent(getApplicationContext(), Transaction.class);
		startActivity(intent);
	}
    
    public void addListenerOnTransactionList(View v)
    {
    	Intent intent = new Intent(getApplicationContext(), TransactionList.class);
		startActivity(intent);
    }
    
    public void addListenerOnProductList(View v)
    {
    	Intent intent = new Intent(getApplicationContext(), ProductList.class);
		startActivity(intent);
    }
    
    public void addListenerOnReports (View v)
    {
    	Intent intent = new Intent(getApplicationContext(), Reports.class);
		startActivity(intent);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) 
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void addListenerOnSettings(View v)
    {
    	Intent intent = new Intent(getApplicationContext(), Settings.class);
		startActivity(intent);
    }
    
    public void addListenerOnRegister(View v)
    {
    	Intent intent = new Intent(getApplicationContext(), Registration.class);
		startActivity(intent);
    }
    
    public void addListenerOnBackup(View v)
    {
    	Intent intent = new Intent(getApplicationContext(), BackupOptions.class);
		startActivity(intent);
    }
    
    public void addListenerOnSynchronize(View v)
    {
    	Intent intent = new Intent(getApplicationContext(), Synchronize.class);
		startActivity(intent);
    }
    public void addListenerOnUpdateTransaction(View v)
    {
    	Intent intent = new Intent(getApplicationContext(), UpdateTransaction.class);
		startActivity(intent);
    }
    
    public void addListenerOnUpdateProductDetails(View v)
    {
    	Intent intent = new Intent(getApplicationContext(), UpdateProductDetails.class);
		startActivity(intent);
    }
    public void openGraph(View v)
    {
    	Intent intent = new Intent(getApplicationContext(), Graph.class);
		startActivity(intent);
    }
    public void addListenerOnSignOut(View v)
    {
    	session.destroySession();
    	btSignOut.setVisibility(View.GONE);
    	Toast.makeText(getApplicationContext(), "Logged out successfully.",Toast.LENGTH_SHORT).show();
    }
}
