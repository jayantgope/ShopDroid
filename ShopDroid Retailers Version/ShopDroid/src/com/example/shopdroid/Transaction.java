package com.example.shopdroid;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class Transaction extends Activity
{
	int productId;
	SQLiteDatabase db;
	private Button btDatePicker;
	private String formattedDate;
	private Calendar myCalendar = Calendar.getInstance();
	private SimpleDateFormat df;
	private RadioGroup rbgTransactionMode;
	String TransactionMode, Date, ProductCode, ProductName, Barcode;
	int UnitCost, Quantity, Stock;
	RadioGroup rdgTransactionMode;
	RadioButton rdbTransactionMode;
	Button btnDate;
	EditText etProductCode, etUnitCost, etQuantity, etProductName, etCategory, etLocation, etStock;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transaction);
		
		db=openOrCreateDatabase("ShopDroid", Context.MODE_PRIVATE, null);
        //db.execSQL("CREATE TABLE IF NOT EXISTS transactions(transaction_id INTEGER PRIMARY KEY AUTOINCREMENT, transaction_mode VARCHAR NOT NULL, date VARCHAR NOT NULL, product_code VARCHAR NOT NULL, unit_cost INT, quantity INT NOT NULL)");
		String sql = "CREATE  TABLE  IF NOT EXISTS transactions(_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL, transaction_mode VARCHAR NOT NULL , pid INTEGER NOT NULL , product_name VARCHAR NOT NULL, unit_cost  DOUBLE NOT NULL , quantity INTEGER NOT NULL , date DATETIME NOT NULL, FOREIGN KEY (pid) REFERENCES products (_id))";
		db.execSQL(sql);
		//db.execSQL("DROP TABLE transactions");
		btDatePicker = (Button) findViewById(R.id.btDatePicker);
		
		Calendar c = Calendar.getInstance();
		//System.out.println("Current time => " + c.getTime());

		df = new SimpleDateFormat("dd-MM-yyyy");
		formattedDate = df.format(c.getTime());
		
		btDatePicker.setText(formattedDate);
		
		etUnitCost = (EditText)findViewById(R.id.etProductUnitCost);
		
		rbgTransactionMode = (RadioGroup) findViewById(R.id.rbgTransactionMode);
		rbgTransactionMode.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				int id=rbgTransactionMode.getCheckedRadioButtonId();
				View radioButton = rbgTransactionMode.findViewById(id);
				if(radioButton.getId()==R.id.rbSales)
				{
					etUnitCost.setEnabled(false);
	            }
				else
				{
					etUnitCost.setEnabled(true);
				}
			}
		});
		
		rdgTransactionMode = (RadioGroup)findViewById(R.id.rbgTransactionMode);
		int SelectedId = rdgTransactionMode.getCheckedRadioButtonId();
		rdbTransactionMode = (RadioButton) findViewById(SelectedId);
		TransactionMode = rdbTransactionMode.getText().toString();
		btnDate = (Button)findViewById(R.id.btDatePicker);
		etProductCode = (EditText)findViewById(R.id.etProductCode);
		etUnitCost = (EditText)findViewById(R.id.etProductUnitCost);
		etQuantity = (EditText)findViewById(R.id.etProductQuantity);
		etProductName = (EditText)findViewById(R.id.etProductName);
		etCategory = (EditText)findViewById(R.id.etProductCategory);
		etLocation = (EditText)findViewById(R.id.etProductLocation);
		etStock = (EditText)findViewById(R.id.etStock);
	}
	
	DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() 
	{

	    @Override
	    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
	    {
	        myCalendar.set(Calendar.YEAR, year);
	        myCalendar.set(Calendar.MONTH, monthOfYear);
	        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	        updateDate();
	    }

	};
	public void onDateClick (View v)
	{
		
		new DatePickerDialog(Transaction.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
		/*
		btDatePicker.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				new DatePickerDialog(Transaction.this, date, myCalendar
	                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
	                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
				
			}
		});*/
	}
	private void updateDate ()
	{
		df = new SimpleDateFormat("dd-MM-yyyy");
		btDatePicker.setText(df.format(myCalendar.getTime()));
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	 {
	        super.onActivityResult(requestCode, resultCode, data);
	        if (requestCode == 0) 
			  {
				  if (resultCode == RESULT_OK) 
				  {
					  //Toast.makeText(getApplicationContext(), "Scanned Successful.", Toast.LENGTH_LONG).show();
					  Barcode = data.getStringExtra("SCAN_RESULT").toString();
					  getProductDetailsUsingBarcode(Barcode);
				  } 
				  else if (resultCode == RESULT_CANCELED) 
				  {
					  Toast.makeText(getApplicationContext(), "Barcode Couldn't be Scanned.", Toast.LENGTH_SHORT).show();
				  }
			  }
	  }
	public void getProductDetailsUsingBarcode(String PassedBarcode)
	{
		String sql = "SELECT * FROM products WHERE bar_code = '"+Barcode+"'";
		Cursor MyCursor = db.rawQuery(sql, null);
        System.out.println("COUNT : " + MyCursor.getCount());
        //Toast toast= Toast.makeText(getApplicationContext(),  "lol" , Toast.LENGTH_SHORT);
        if (MyCursor.moveToFirst()) 
        {
        	Toast toast= Toast.makeText(getApplicationContext(),  "Product found." , Toast.LENGTH_SHORT);
        	toast.show();
        	etProductCode.setText(MyCursor.getString(1));
        	etProductName.setText(MyCursor.getString(3));
    		etCategory.setText(MyCursor.getString(4));
    		etLocation.setText(MyCursor.getString(5));
    		etStock.setText(MyCursor.getString(6));
    		etUnitCost.setText(MyCursor.getString(7));
    		productId = MyCursor.getInt(1);
        }
        else if(MyCursor.getCount() == 0)
        {
        	Toast toast= Toast.makeText(getApplicationContext(),  "Product doesn't exists." , Toast.LENGTH_SHORT);
        	toast.show();
        }
	}
	
	public void onClickConfirm(View v)
	{
		ProductCode = etProductCode.getText().toString();
		if(ProductCode.isEmpty())
		{
			Toast toast= Toast.makeText(getApplicationContext(),  "Please enter the valid product code." , Toast.LENGTH_SHORT);
			toast.show();
		}
		String sql = "SELECT * FROM products WHERE product_code = '"+ProductCode+"'";
		Cursor MyCursor = db.rawQuery(sql, null);
        System.out.println("COUNT : " + MyCursor.getCount());
        //Toast toast= Toast.makeText(getApplicationContext(),  "lol" , Toast.LENGTH_SHORT);
        if (MyCursor.moveToFirst()) 
        {
        	Toast toast= Toast.makeText(getApplicationContext(),  "Product found." , Toast.LENGTH_SHORT);
        	toast.show();
        	
        	etProductName.setText(MyCursor.getString(3));
    		etCategory.setText(MyCursor.getString(4));
    		etLocation.setText(MyCursor.getString(5));
    		etStock.setText(MyCursor.getString(6));
    		etUnitCost.setText(MyCursor.getString(7));
    		productId = MyCursor.getInt(1);
        }
        else if(MyCursor.getCount() == 0)
        {
        	Toast toast= Toast.makeText(getApplicationContext(),  "Product doesn't exists." , Toast.LENGTH_SHORT);
        	toast.show();
        }
	}
	public void onClickSave(View v)
	{
		Date = btnDate.getText().toString();
		ProductCode = etProductCode.getText().toString();
		ProductName = etProductName.getText().toString();
		UnitCost = Integer.parseInt(etUnitCost.getText().toString());
		Stock = Integer.parseInt(etStock.getText().toString());
		int SelectedId = rdgTransactionMode.getCheckedRadioButtonId();
		rdbTransactionMode = (RadioButton) findViewById(SelectedId);
		TransactionMode = rdbTransactionMode.getText().toString();
		Quantity = Integer.parseInt(etQuantity.getText().toString());
		/*if(etQuantity.getText().toString().isEmpty())
		{
			Toast.makeText(getApplicationContext(), "Product Quantity cannot be left blank.", Toast.LENGTH_SHORT).show();
		}
		else
		{
			if(TransactionMode == "Sales")
			{
				Toast.makeText(getApplicationContext(), "Sales", Toast.LENGTH_SHORT).show();
			}
			if(TransactionMode == "Purchase")
			{
				Toast.makeText(getApplicationContext(), "Purchase", Toast.LENGTH_SHORT).show();
			}
		}*/
		if(TransactionMode.equalsIgnoreCase("Sales"))
		{
			
			if(Quantity > Stock)
			{
				Toast.makeText(getApplicationContext(), "Sorry, Stock is less than number of products.", Toast.LENGTH_LONG).show();
			}
			else
			{
				Stock = Stock - Quantity;
				try 
				{
					//String query = "INSERT INTO transactions(transaction_mode, date, product_code, unit_cost, quantity) VALUES ('"+ TransactionMode +",'"+ Date +"','"+ ProductCode +"',"+ UnitCost +","+ Quantity +");";
					db.execSQL("INSERT INTO transactions (transaction_mode, pid, product_name, unit_cost, quantity, date) VALUES('"+TransactionMode+"',"+productId+", '"+ ProductName+"',"+UnitCost+","+Quantity+",'"+Date+"')");
					db.execSQL("UPDATE products SET quantity = " + Stock + " WHERE product_code = '" + ProductCode + "'");
					Toast to = Toast.makeText(getApplicationContext(), "Transaction added successfully.", Toast.LENGTH_SHORT);
		            to.show();
				}
				catch(Exception e)
				{
					Toast toast= Toast.makeText(getApplicationContext(),  e.toString() , Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		}
		if(TransactionMode.equalsIgnoreCase("Purchase"))
		{
			Stock = Stock + Quantity;
			try 
			{
				db.execSQL("INSERT INTO transactions (transaction_mode, pid, product_name, unit_cost, quantity, date) VALUES('"+TransactionMode+"',"+productId+", '"+ ProductName+"',"+UnitCost+","+Quantity+",'"+Date+"')");
				db.execSQL("UPDATE products SET quantity = " + Stock + " WHERE product_code = '" + ProductCode + "'");
				db.execSQL("UPDATE products SET unit_cost = " + UnitCost + " WHERE product_code = '" + ProductCode + "'");
				Toast to = Toast.makeText(getApplicationContext(), "Transaction added successfully.", Toast.LENGTH_SHORT);
	            to.show();
			}
			catch(Exception e)
			{
				Toast toast= Toast.makeText(getApplicationContext(),  e.toString() , Toast.LENGTH_SHORT);
				toast.show();
			}
		}
		
	}
}
