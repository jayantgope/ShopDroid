package com.example.shopdroid;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class UpdateTransaction extends Activity 
{
	static int productId;
	SQLiteDatabase db;
	private Button btDatePicker;
	private String formattedDate;
	private Calendar myCalendar = Calendar.getInstance();
	private SimpleDateFormat df;
	private RadioGroup rbgTransactionMode;
	String TransactionMode, Date, ProductCode;
	String ProductName, Category, Location, Stock, UnitCost, ProductId, Quantity, Barcode;
	RadioGroup rdgTransactionMode;
	RadioButton rdbTransactionMode, rdbSales, rdbPurchase;
	Button btnUSave, btnUCancel, btnEdit, btnDelete, btnCancel, btnConfirm, btnScan, btnList;
	Button btnDate;
	EditText etProductCode, etUnitCost, etQuantity, etProductName, etCategory, etLocation, etStock;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_transaction);
		rdgTransactionMode = (RadioGroup)findViewById(R.id.rbgTransactionMode);
		int SelectedId = rdgTransactionMode.getCheckedRadioButtonId();
		rdbTransactionMode = (RadioButton) findViewById(SelectedId);
		TransactionMode = rdbTransactionMode.getText().toString();
		rdbSales = (RadioButton)findViewById(R.id.rbSales);
		rdbPurchase = (RadioButton)findViewById(R.id.rbPurchase);
		
		btnDate = (Button)findViewById(R.id.btDatePicker);
		etProductCode = (EditText)findViewById(R.id.etUpdateTransactionProductCode);
		etUnitCost = (EditText)findViewById(R.id.etProductUnitCost);
		etQuantity = (EditText)findViewById(R.id.etProductQuantity);
		etProductName = (EditText)findViewById(R.id.etProductName);
		etCategory = (EditText)findViewById(R.id.etProductCategory);
		etLocation = (EditText)findViewById(R.id.etProductLocation);
		etStock = (EditText)findViewById(R.id.etStock);
		
		btnUSave = (Button)findViewById(R.id.btUSave);
		btnUCancel = (Button)findViewById(R.id.btUCancel);
		btnEdit = (Button)findViewById(R.id.btEditButton);
		btnDelete = (Button)findViewById(R.id.btDeleteButton);
		btnCancel = (Button)findViewById(R.id.btCancelButton);
		btnConfirm = (Button)findViewById(R.id.bt_verify_product_code);
		btnScan = (Button)findViewById(R.id.bt_scan_barcode);
		btnList = (Button)findViewById(R.id.bt_list_product);
	  
		btDatePicker = (Button) findViewById(R.id.btDatePicker);
		
		Calendar c = Calendar.getInstance();

		df = new SimpleDateFormat("dd-MM-yyyy");
		formattedDate = df.format(c.getTime());
		
		btDatePicker.setText(formattedDate);
		//Get the bundle
		Bundle bundle = getIntent().getExtras();
		
		//Extract the data…
		String TransactionId = bundle.getString("Transaction_ID");
		db = openOrCreateDatabase("ShopDroid", Context.MODE_PRIVATE, null);
		//String sql = "SELECT t.transaction_mode, t.date, t.quantity, p.product_name, p.category, p.location, p.quantity, p.unit_cost FROM transactions as t, products as p INNER JOIN products on t.pid = p._id WHERE t._id = "+TransactionId+"";
		//String sql = "SELECT t.*, p.* FROM transactions as t, products as p WHERE t._id = '"+TransactionId+"' AND p";
		//String sql = "SELECT * FROM products WHERE _id IN (SELECT pid FROM transactions WHERE _id = '"+TransactionId+"')";
		//String sql = "SELECT pid FROM transactions WHERE _id = '"+TransactionId+"'";
		//String sql = "SELECT * FROM transactions WHERE _id = '"+TransactionId+"'";
		String sql = "SELECT DISTINCT t.transaction_mode, t.pid, t.date, t.quantity, p.product_code, p.product_name, p.category, p.location, p.quantity as stock, p.unit_cost FROM transactions as t, products as p INNER JOIN products on t.pid = p._id WHERE t._id = '"+TransactionId+"'";
		//String sql = "SELECT * FROM products WHERE _id IN (SELECT pid FROM transactions WHERE _id = "+TransactionId+")";
		//String sql = "SELECT DISTINCT t.transaction_mode, t.pid, t.date, t.quantity, p.product_code, p.product_name, p.category, p.location, p.quantity as stock, p.unit_cost FROM transactions as t, products as p INNER JOIN products on t.pid = p._id WHERE t._id = '13'";
		Cursor MyCursor = db.rawQuery(sql, null);
		//Toast.makeText(getApplicationContext(),  Integer.toString(MyCursor.getCount()) , Toast.LENGTH_SHORT).show();
		if (MyCursor.moveToFirst()) 
        {
        	do
        	{
        		//quantityFirst=Float.parseFloat(MyCursor.getString(MyCursor.getColumnIndex("quantity")));
        		TransactionMode = MyCursor.getString(MyCursor.getColumnIndex("transaction_mode"));
        		ProductId = MyCursor.getString(MyCursor.getColumnIndex("pid"));
        		productId = MyCursor.getInt(MyCursor.getColumnIndex("pid"));
	        	ProductName = MyCursor.getString(MyCursor.getColumnIndex("product_name"));
	        	ProductCode = MyCursor.getString(MyCursor.getColumnIndex("product_code"));
	    		UnitCost = MyCursor.getString(MyCursor.getColumnIndex("unit_cost"));
	    		Quantity = MyCursor.getString(MyCursor.getColumnIndex("quantity"));
	    		Date = MyCursor.getString(MyCursor.getColumnIndex("date"));
	    		Category = MyCursor.getString(MyCursor.getColumnIndex("category"));
        		Location = MyCursor.getString(MyCursor.getColumnIndex("location"));
	        	Stock = MyCursor.getString(MyCursor.getColumnIndex("stock"));
        	}
        	while (MyCursor.moveToNext());
        }
		else if(MyCursor.getCount() == 0)
        {
        	Toast toast= Toast.makeText(getApplicationContext(),  "Product doesn't exists. " + TransactionId , Toast.LENGTH_SHORT);
        	toast.show();
        }
		btnDate.setText(Date);
		etProductName.setText(ProductName);
        etUnitCost.setText(UnitCost);
        etQuantity.setText(Quantity);
        etCategory.setText(Category);
        etLocation.setText(Location);
        etProductCode.setText(ProductCode);
        etStock.setText(Stock);
        if(TransactionMode.equals("Sales"))
        {
        	rdbSales.setChecked(true);
        	rdbPurchase.setChecked(false);
        }
        else if(TransactionMode.equals("Purchase"))
        {
        	rdbSales.setChecked(false);
        	rdbPurchase.setChecked(true);
        }
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
		
		new DatePickerDialog(UpdateTransaction.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
	}
	private void updateDate ()
	{
		df = new SimpleDateFormat("dd-MM-yyyy");
		btDatePicker.setText(df.format(myCalendar.getTime()));
	}
	public void onEdit(View v)
	{
		btnUSave.setVisibility(View.VISIBLE);
		btnUCancel.setVisibility(View.VISIBLE);
		btnEdit.setVisibility(View.GONE);
		btnDelete.setVisibility(View.GONE);
		btnCancel.setVisibility(View.GONE);
		
		btnDate.setClickable(true);
		btnConfirm.setClickable(true);
		btnScan.setClickable(true);
		btnList.setClickable(true);
		
		etProductCode.setEnabled(true);
		etQuantity.setEnabled(true);
		etUnitCost.setEnabled(true);
		
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
        if (MyCursor.moveToFirst()) 
        {
        	Toast toast= Toast.makeText(getApplicationContext(),  "Product found." , Toast.LENGTH_SHORT);
        	toast.show();
        	
        	etProductName.setText(MyCursor.getString(3));
    		etCategory.setText(MyCursor.getString(4));
    		etLocation.setText(MyCursor.getString(5));
    		etStock.setText(MyCursor.getString(6));
    		etUnitCost.setText(MyCursor.getString(7));
    		ProductId = MyCursor.getString(1);
        }
        else if(MyCursor.getCount() == 0)
        {
        	Toast toast= Toast.makeText(getApplicationContext(),  "Product doesn't exists." , Toast.LENGTH_SHORT);
        	toast.show();
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
    		ProductId = MyCursor.getString(1);
        }
        else if(MyCursor.getCount() == 0)
        {
        	Toast toast= Toast.makeText(getApplicationContext(),  "Product doesn't exists." , Toast.LENGTH_SHORT);
        	toast.show();
        }
	}
	public void onDelete(View v)
	{
		//Get the bundle
		Bundle bundle = getIntent().getExtras();
		//Extract the data…
		String TransactionId = bundle.getString("Transaction_ID");
		String sql = "DELETE FROM transactions WHERE _id = '" + TransactionId + "'";
		db.execSQL(sql);
		Toast.makeText(getApplicationContext(), "Transaction " + TransactionId + " deleted successfully.", Toast.LENGTH_SHORT).show();
		finish();
	}
	public void onClickSave(View v)
	{
		if(etProductCode.getText().toString().isEmpty())
		  {
			  Toast.makeText(getApplicationContext(), "Product code cannot be left blank.", Toast.LENGTH_SHORT).show();
		  }
		  else if(etQuantity.getText().toString().isEmpty())
		  {
			  Toast.makeText(getApplicationContext(), "Quantity cannot be left blank.", Toast.LENGTH_SHORT).show();
		  }
		  else if(etUnitCost.getText().toString().isEmpty())
		  {
			  Toast.makeText(getApplicationContext(), "Unit Cost cannot be left blank.", Toast.LENGTH_SHORT).show();
		  }
		  else
		  {
			  Date = btnDate.getText().toString();
			  ProductCode = etProductCode.getText().toString();
			  ProductName = etProductName.getText().toString();
			  int UnitCost = Integer.parseInt(etUnitCost.getText().toString());
			  int quantity = Integer.parseInt(etQuantity.getText().toString());
			  int productStock = Integer.parseInt(etStock.getText().toString());
			  int previousQuantity = Integer.parseInt(Quantity);
			  int previousStock = Integer.parseInt(Stock) + previousQuantity;
			  
			  //Get the bundle
			  Bundle bundle = getIntent().getExtras();
			  //Extract the data…
			  String TransactionId = bundle.getString("Transaction_ID");
			  if(TransactionMode.equalsIgnoreCase("Sales"))
				{
					if(quantity > previousStock)
					{
						Toast.makeText(getApplicationContext(), "Sorry, Stock is less than number of products.", Toast.LENGTH_LONG).show();
					}
					else
					{
						int newStock = previousStock - quantity;
						try 
						{
							db.execSQL("UPDATE transactions SET pid = '" + ProductId + "', product_name = '"+ProductName+"', date = '"+ Date + "', unit_cost = "+ UnitCost + ", quantity = " + quantity + " WHERE _id = '"+ TransactionId + "'");
							db.execSQL("UPDATE products SET quantity = " + newStock + " WHERE product_code = '" + ProductCode + "'");
							Toast.makeText(getApplicationContext(), "Transaction updated successfully.", Toast.LENGTH_SHORT).show();
				            finish();
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
					previousStock = Integer.parseInt(Stock);
					int newStock = previousStock + quantity;
					try 
					{
						db.execSQL("UPDATE transactions SET pid = '" + ProductId + "',product_name = '"+ProductName+"', date = '"+ Date + "', unit_cost = "+ UnitCost + ", quantity = " + quantity + " WHERE _id = "+ TransactionId + "");
						db.execSQL("UPDATE products SET quantity = " + newStock + " WHERE product_code = '" + ProductCode + "'");
						Toast.makeText(getApplicationContext(), "Transaction updated successfully.", Toast.LENGTH_SHORT).show();
			            finish();
					}
					catch(Exception e)
					{
						Toast toast= Toast.makeText(getApplicationContext(),  e.toString() , Toast.LENGTH_SHORT);
						toast.show();
					}
				}
		  }
	}
	public void onCancel(View v)
	{
		btnEdit.setVisibility(View.VISIBLE);
		btnDelete.setVisibility(View.VISIBLE);
		btnCancel.setVisibility(View.VISIBLE);
		
		btnDate.setClickable(false);
		btnConfirm.setClickable(false);
		btnScan.setClickable(false);
		btnList.setClickable(false);
		
		etProductCode.setEnabled(false);
		etQuantity.setEnabled(false);
		etUnitCost.setEnabled(false);
	}
	public void onClose(View v)
	{
		finish();
	}
}
