package com.example.shopdroid;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class AddProduct extends Activity
{
	SQLiteDatabase db;
	Button brwbtn;
	private int PICK_IMAGE_REQUEST = 1;
	private ImageView imageView;
	private Bitmap bitmap;
	private Uri filePath;
	String ProductCode, BarCode, ProductName, Category, Location, image;
	int Quantity, UnitCost;
	EditText edtProductCode, edtBarCode, edtProductName, edtLocation, edtQuantity, edtUnitCost;
	Spinner spCategory;
	private ProductsDBAdapter dbHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_product);
		
		 
		 edtProductCode = (EditText)findViewById(R.id.etProductCode);
		 edtBarCode = (EditText)findViewById(R.id.etBarCode);
		 edtProductName = (EditText)findViewById(R.id.etProductName);
		 edtLocation = (EditText)findViewById(R.id.etProductLocation);
		 edtQuantity = (EditText)findViewById(R.id.etProductQuantity);
		 edtUnitCost = (EditText)findViewById(R.id.etProductUnitCost);
		 spCategory = (Spinner)findViewById(R.id.spnProductCategory);
		/*db=openOrCreateDatabase("ShopDroid", Context.MODE_PRIVATE, null);
		db.execSQL("DROP TABLE products");
		Toast toast= Toast.makeText(getApplicationContext(),  "Drop" , Toast.LENGTH_SHORT);
		toast.show();*/
		/*ProductsDBAdapter ob = new ProductsDBAdapter(getApplicationContext());
		ob.open();*/
		
		brwbtn=(Button) findViewById(R.id.btBrowseImage);
		imageView = (ImageView) findViewById(R.id.ivUploadImage);
		
		
        db=openOrCreateDatabase("ShopDroid", Context.MODE_PRIVATE, null);
        //sql = "CREATE  TABLE product (product_code INTEGER PRIMARY KEY NOT NULL  UNIQUE , bar_code VARCHAR UNIQUE , product_name VARCHAR NOT NULL , category VARCHAR NOT NULL , location VARCHAR, quantity INTEGER NOT NULL , unit_cost DOUBLE NOT NULL , image VARCHAR)";
        db.execSQL("CREATE TABLE IF NOT EXISTS products(_id integer PRIMARY KEY autoincrement, product_code VARCHAR UNIQUE NOT NULL, bar_code UNIQUE, product_name VARCHAR NOT NULL, category VARCHAR NOT NULL, location VARCHAR, quantity INT NOT NULL, unit_cost DOUBLE NOT NULL, image VARCHAR)");
        //db.execSQL("DROP TABLE products");
        LoadProductCategory();
	}
	@Override
	protected void onResume() 
	{
		super.onResume();
		LoadProductCategory();
	}
	public void LoadProductCategory()
	{
		ArrayList<String> my_array = new ArrayList<String>();
        my_array = getTableValues();
 
        Spinner My_spinner = (Spinner) findViewById(R.id.spnProductCategory);
        ArrayAdapter my_Adapter = new ArrayAdapter(this, R.layout.spinner_row, my_array);
        My_spinner.setAdapter(my_Adapter);
        
	}
	public ArrayList<String> getTableValues() 
	{
		 
        ArrayList<String> my_array = new ArrayList<String>();
        try 
        {
            db = openOrCreateDatabase("ShopDroid", Context.MODE_PRIVATE, null);
            Cursor allrows = db.rawQuery("SELECT * FROM product_categories", null);
            System.out.println("COUNT : " + allrows.getCount());
 
            if (allrows.moveToFirst()) 
            {
                do 
                {
                    String category = allrows.getString(1);
                    my_array.add(category);
 
                } while (allrows.moveToNext());
            }
            /*allrows.close();
            db.close();*/
        } 
        catch (Exception e) 
        {
            Toast.makeText(getApplicationContext(), "Error encountered.",
                    Toast.LENGTH_LONG);
        }
        return my_array;
    }
	
	public void onNewProductCategory(View v)
	{
		Intent intent = new Intent(getApplicationContext(), AddNewProductCategory.class);
		startActivity(intent);
	}
	public void brwfunimg(View v)
	{
		Intent intent1 = new Intent();
        intent1.setType("image/*");
        intent1.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent1, "Select Picture"), PICK_IMAGE_REQUEST);
		
	}
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	 {
	        super.onActivityResult(requestCode, resultCode, data);

	        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) 
	        {
	            filePath = data.getData();
	            try 
	            {
	                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
	                imageView.setImageBitmap(bitmap);
	            } 
	            catch (IOException e) 
	            {
	                e.printStackTrace();
	            }
	        }
	        if (requestCode == 0) 
			  {
				  if (resultCode == RESULT_OK) 
				  {
					  Toast.makeText(getApplicationContext(), "Scanned Successful.", Toast.LENGTH_LONG).show();
					  edtBarCode.setText(data.getStringExtra("SCAN_RESULT").toString());
				  } 
				  else if (resultCode == RESULT_CANCELED) 
				  {
					  Toast.makeText(getApplicationContext(), "Barcode Couldn't be Scanned.", Toast.LENGTH_SHORT).show();
				  }
			  }
	        if(requestCode == 2 && resultCode == RESULT_OK)
	        {
	            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
	            imageView.setImageBitmap(bitmap);
	        }
 	  }
	  public String getStringImage(Bitmap bmp)
	  {
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
	        byte[] imageBytes = baos.toByteArray();
	        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
	        Toast.makeText(getApplicationContext(), encodedImage, Toast.LENGTH_LONG).show();
	        return encodedImage;
	  }
	  public void getImage()
	  {
		  if(bitmap == null)
		  {
			  image = "No Image";
		  }
		  else
		  {
			  image = getStringImage(bitmap);
	 	  }
	  }
	  public void onClickSave(View v)
	  {
		  getImage();
		  //getStringImage(bitmap);
		  /*image = getStringImage(bitmap);
		  Toast.makeText(getApplicationContext(), bitmap, duration)*/
		  if(edtProductCode.getText().toString().isEmpty())
		  {
			  Toast.makeText(getApplicationContext(), "Product code cannot be left blank.", Toast.LENGTH_SHORT).show();
		  }
		  else if(edtProductName.getText().toString().isEmpty())
		  {
			  Toast.makeText(getApplicationContext(), "Product Name cannot be left blank.", Toast.LENGTH_SHORT).show();
		  }
		  else if(edtQuantity.getText().toString().isEmpty())
		  {
			  Toast.makeText(getApplicationContext(), "Quantity cannot be left blank.", Toast.LENGTH_SHORT).show();
		  }
		  else if(edtUnitCost.getText().toString().isEmpty())
		  {
			  Toast.makeText(getApplicationContext(), "Unit Cost cannot be left blank.", Toast.LENGTH_SHORT).show();
		  }
		  else
		  {
			  ProductCode = edtProductCode.getText().toString();
			  BarCode = edtBarCode.getText().toString();
			  ProductName = edtProductName.getText().toString();
			  Category = spCategory.getSelectedItem().toString();
			  Location = edtLocation.getText().toString();
			  Quantity = Integer.parseInt(edtQuantity.getText().toString());
			  UnitCost = Integer.parseInt(edtUnitCost.getText().toString());
			  try 
			  {
				  db.execSQL("INSERT INTO products (product_code, bar_code, product_name, category, location, quantity, unit_cost, image) VALUES('"+ProductCode+"','"+BarCode+"','"+ProductName+"','"+Category+"','"+Location+"',"+Quantity+","+UnitCost+",'"+image+"');");
				  Toast to = Toast.makeText(getApplicationContext(), "Product added successfully.", Toast.LENGTH_SHORT);
				  to.show();
			  }
			  catch(SQLiteConstraintException UniqueConstratintException)
			  {
				  Toast toast= Toast.makeText(getApplicationContext(),  "Barcode already exists." , Toast.LENGTH_SHORT);
				  toast.show();
			  }
			  catch(Exception e)
			  {
				  Toast toast= Toast.makeText(getApplicationContext(),  e.toString() , Toast.LENGTH_SHORT);
				  toast.show();
			  }
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
	  public void onClickCancel(View v)
	  {
		  finish();
	  }
	  public void onClickCaptureImage(View v)
	  {
		  Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
          startActivityForResult(intent, 2);
	  }
}