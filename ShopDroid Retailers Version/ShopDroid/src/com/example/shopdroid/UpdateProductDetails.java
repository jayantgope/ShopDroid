package com.example.shopdroid;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class UpdateProductDetails extends Activity
{
	EditText edtProductCode, edtBarCode, edtProductName, edtLocation, edtQuantity, edtUnitCost;
	Spinner spCategory;
	Button btnNewCategory, btnCaptureImage, btnBrowseImage, btnUSave, btnUCancel, btnEdit, btnDelete, btnCancel;
	private ImageView imageView;
	ImageButton ibnScanBarcode;
	private Bitmap bitmap;
	private Uri filePath;
	String Product_Code, Bar_Code, Product_Name, Category, Location, Quantity, Unit_Cost, image;
	private ProductsDBAdapter dbHelper;
	SQLiteDatabase db;
	byte[] decodedString; 
	private int PICK_IMAGE_REQUEST = 1;
	/*public UpdateProductDetails(String ProductId, String ProductCode, String BarCode, String ProductName, String Category, String Location, String Quantity, String unit_cost, String Image)
	{
		etProductCode.setText(ProductCode);
		etBarCode.setText(BarCode);
		etProductName.setText(ProductName);
		etLocation.setText(Location);
		etQuantity.setText(Quantity);
		etUnitCost.setText(unit_cost);
		
		List<String> spinnerArray =  new ArrayList<String>();
		spinnerArray.add(Category);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner sItems = (Spinner) findViewById(R.id.spnProductCategory);
		sItems.setAdapter(adapter);
		
		spnProductCategory.setSelection(1);
	}*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_product_details);
				
		 
		edtProductCode = (EditText)findViewById(R.id.etProductCode);
		edtBarCode = (EditText)findViewById(R.id.etBarCode);
		edtProductName = (EditText)findViewById(R.id.etProductName);
		edtLocation = (EditText)findViewById(R.id.etProductLocation);
		  edtQuantity = (EditText)findViewById(R.id.etProductQuantity);
		  edtUnitCost = (EditText)findViewById(R.id.etProductUnitCost);
		  spCategory = (Spinner)findViewById(R.id.spnProductCategory);
		  btnNewCategory = (Button)findViewById(R.id.btnNewProductCategory);
		  btnCaptureImage = (Button)findViewById(R.id.btCaptureImage);
		  btnBrowseImage = (Button)findViewById(R.id.btBrowseImage);
		  imageView = (ImageView)findViewById(R.id.ivUploadImage);
		  ibnScanBarcode = (ImageButton)findViewById(R.id.ibScanBarcode);
		  
		  btnUSave = (Button)findViewById(R.id.btUSave);
		  btnUCancel = (Button)findViewById(R.id.btUCancel);
		  btnEdit = (Button)findViewById(R.id.btEditButton);
		  btnDelete = (Button)findViewById(R.id.btDeleteButton);
		  btnCancel = (Button)findViewById(R.id.btCancelButton);
		  
		  
		//Get the bundle
		Bundle bundle = getIntent().getExtras();

		//Extract the data…
		String ProductId = bundle.getString("Product_ID"); 
		
	    
		db=openOrCreateDatabase("ShopDroid", Context.MODE_PRIVATE, null);
		String sqlQuery = "SELECT * FROM products WHERE _id = '" + ProductId + "'";
		Cursor MyCursor = db.rawQuery(sqlQuery, null);
        System.out.println("COUNT : " + MyCursor.getCount());
        if (MyCursor.moveToFirst()) 
        {
            do 
            {
            	Product_Code = MyCursor.getString(1);
        		Bar_Code = MyCursor.getString(2);
        	   	Product_Name = MyCursor.getString(3);
        	   	Category = MyCursor.getString(4);
        	   	Location = MyCursor.getString(5);
        	   	Quantity = MyCursor.getString(6);
        	   	Unit_Cost = MyCursor.getString(7);
        	   	image = MyCursor.getString(8);
            	/*Toast toast = Toast.makeText(getApplicationContext(), Category, Toast.LENGTH_LONG);
            	toast.show();*/
            	 
            } while (MyCursor.moveToNext());
        }
        
        LoadProductCategory(Category);
	   	
        edtProductCode.setText(Product_Code);
		edtBarCode.setText(Bar_Code);
		edtProductName.setText(Product_Name);
		edtLocation.setText(Location);
		edtQuantity.setText(Quantity);
		edtUnitCost.setText(Unit_Cost);
		
		byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
		bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
	    imageView.setImageBitmap(bitmap);
		SetDisabled();
		   
		
	}
	private int getIndex(Spinner spinner, String category)
	{	
		int index = 0;
		for (int i=0;i<spinner.getAdapter().getCount();i++)
		{
			if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(category))
			{
				index = i;
			}
		}
		return index;
	 }
	protected void onResume() 
	{
		super.onResume();
		LoadProductCategory(Category);
	}
	public void LoadProductCategory(String category)
	{
		ArrayList<String> my_array = new ArrayList<String>();
        my_array = getTableValues();
 
        Spinner My_spinner = (Spinner) findViewById(R.id.spnProductCategory);
        ArrayAdapter my_Adapter = new ArrayAdapter(this, R.layout.spinner_row, my_array);
        My_spinner.setAdapter(my_Adapter);
        My_spinner.setSelection(getIndex(My_spinner, Category));
        
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
	public void SetDisabled()
	{
		edtProductCode.setEnabled(false);
		edtBarCode.setEnabled(false);
		spCategory.setClickable(false);
		edtLocation.setEnabled(false);
		edtProductName.setEnabled(false);
		edtQuantity.setEnabled(false);
		edtUnitCost.setEnabled(false);
		btnNewCategory.setClickable(false);
		btnCaptureImage.setClickable(false);
		btnBrowseImage.setClickable(false);
		imageView.setClickable(false);
		ibnScanBarcode.setClickable(false);
	}
	
	public void onEdit(View v)
	{
		btnUSave.setVisibility(View.VISIBLE);
		btnUCancel.setVisibility(View.VISIBLE);
		btnEdit.setVisibility(View.GONE);
		btnDelete.setVisibility(View.GONE);
		btnCancel.setVisibility(View.GONE);
		spCategory.setClickable(true);
		edtBarCode.setEnabled(true);
		edtLocation.setEnabled(true);
		edtProductName.setEnabled(true);
		edtQuantity.setEnabled(true);
		edtUnitCost.setEnabled(true);
		btnNewCategory.setClickable(true);
		btnCaptureImage.setClickable(true);
		btnBrowseImage.setClickable(true);
		imageView.setClickable(true);
		ibnScanBarcode.setClickable(true);
		
	}
	
	public void onDelete(View v)
	{
		String productCode = edtProductCode.getText().toString();
		//Get the bundle
		Bundle bundle = getIntent().getExtras();
		//Extract the data…
		String ProductId = bundle.getString("Product_ID"); 
		String sql = "DELETE FROM products WHERE _id = '" + ProductId + "'";
		db.execSQL(sql);
		Toast.makeText(getApplicationContext(), "Product code " + Product_Code + " deleted successfully.", Toast.LENGTH_SHORT).show();
		finish();
	}
	
	public void onCancel(View v)
	{
		btnEdit.setVisibility(View.VISIBLE);
		btnDelete.setVisibility(View.VISIBLE);
		btnCancel.setVisibility(View.VISIBLE);
		
		edtBarCode.setEnabled(false);
		spCategory.setClickable(false);
		edtLocation.setEnabled(false);
		edtProductName.setEnabled(false);
		edtQuantity.setEnabled(false);
		edtUnitCost.setEnabled(false);
		btnNewCategory.setClickable(false);
		btnCaptureImage.setClickable(false);
		btnBrowseImage.setClickable(false);
		imageView.setClickable(false);
		ibnScanBarcode.setClickable(false);
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
	public void onNewProductCategory(View v)
	{
		Intent intent = new Intent(getApplicationContext(), AddNewProductCategory.class);
		startActivity(intent);
	}
	public void onClickCaptureImage(View v)
	  {
		  Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 2);
	  }
	public void onClickBrowseImage(View v)
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
	        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
	        byte[] imageBytes = baos.toByteArray();
	        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
	        //Toast.makeText(getApplicationContext(), encodedImage, Toast.LENGTH_LONG).show();
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
			  Product_Code = edtProductCode.getText().toString();
			  Bar_Code = edtBarCode.getText().toString();
			  Product_Name = edtProductName.getText().toString();
			  Category = spCategory.getSelectedItem().toString();
			  Location = edtLocation.getText().toString();
			  int Quantity = Integer.parseInt(edtQuantity.getText().toString());
			  double UnitCost = Double.parseDouble(edtUnitCost.getText().toString());
			  try 
			  {
				  db.execSQL("UPDATE products SET bar_code = '"+Bar_Code+"', "
				  		+ "product_name = '"+ Product_Name + "',"
				  				+ "category ='" + Category + "',"
				  						+ "location = '" + Location + "', "
				  								+ "quantity = " + Quantity + ", "
				  										+ "unit_cost = " + UnitCost + " WHERE product_code = '"+ Product_Code + "'");
				  Toast to = Toast.makeText(getApplicationContext(), "Product updated successfully.", Toast.LENGTH_SHORT);
				  to.show();
				  finish();
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
	  public void onClose(View v)
	  {
		  finish();
	  }
}
