package com.example.shopdroid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*import com.blogspot.geekonjava.newjson.JSONParser;
import com.blogspot.geekonjava.newjson.MainActivity;
import com.blogspot.geekonjava.newjson.MainActivity.CreateNewProduct;*/

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class Registration extends Activity
{
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	private static String url_create_product = "http://192.168.43.177/webservice/shop_registration.php";
	private static final String TAG_SUCCESS = "success";
	Button btnRegister;
	Spinner spState, spCity;
	ProgressDialog dialog;
	String Username, Password, ShopName, Address, State, City, PinCode, MobileNo, EmailId;
	EditText edtUsername, edtPassword, edtShopname, edtAddress, edtPinCode, edtMobileNo, edtEmailId;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration);
		edtUsername = (EditText)findViewById(R.id.etUsername);
		edtPassword = (EditText)findViewById(R.id.etPassword);
		edtShopname = (EditText)findViewById(R.id.etShopName);
		edtAddress = (EditText)findViewById(R.id.etAddress);
		edtPinCode = (EditText)findViewById(R.id.etPinCode);
		edtMobileNo = (EditText)findViewById(R.id.etContactNo);
		spState = (Spinner)findViewById(R.id.spnState);
		spCity = (Spinner)findViewById(R.id.spnCity);
		btnRegister=(Button) findViewById(R.id.btRegister);
		
	}
	public void onClickRegister(View v)
	{
		String username=edtUsername.getText().toString();
		String password=edtPassword.getText().toString();
		String shop_name=edtShopname.getText().toString();
		String address=edtAddress.getText().toString();
		String pincode=edtPinCode.getText().toString();
		String contact=edtMobileNo.getText().toString();
		String state=spState.getSelectedItem().toString();
		String city=spCity.getSelectedItem().toString();
		
		if(username.length()>0 && password.length()>0 && shop_name.length()>0)
		{
			dialog=ProgressDialog.show(Registration.this, "Please wait..", "Saving details..",true,true);
			RequestParams params = new RequestParams();
			params.add("username", username);
			params.add("password", password);
			params.add("shop_name", shop_name);
			params.add("address", address);
			params.add("pincode", pincode);
			params.add("contact", contact);
			params.add("state", state);
			params.add("city", city);
			AsyncHttpClient client = new AsyncHttpClient();

			// Don't forget to change the IP address to your LAN address. Port no as
			// well.
			String url = URL1.url_register;
			

			client.post(url, params, new AsyncHttpResponseHandler()
			{
				// When the response returned by REST has Http
				// response code '200'
				@Override
				public void onSuccess(String response) 
				{
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
						// TODO: handle exception }
					}
				}
				@Override
				public void onFailure(int statusCode, Throwable error, String content) 
				{
					dialog.dismiss();
				}
			});
		}
		else
		{
			Toast.makeText(getBaseContext(), "Please check mandatory fields", Toast.LENGTH_SHORT).show();
		}
	}
	public void onClickClose(View v)
	{
		finish();
	}
	/*class CreateNewProduct extends AsyncTask<String, String, String> 
	{
		  @Override
		  protected void onPreExecute() 
		  {
			   super.onPreExecute();
			   pDialog = new ProgressDialog(Registration.this);
			   pDialog.setMessage("Registering..");
			   pDialog.setIndeterminate(false);
			   pDialog.setCancelable(true);
			   pDialog.show();
		  }
		  protected String doInBackground(String... args) 
		  {
			  	Username = edtUsername.getText().toString();
				Password = edtPassword.getText().toString();
				ShopName = edtShopname.getText().toString();
				Address = edtAddress.getText().toString();
				State = "New Delhi";
				City = "Delhi";
				PinCode = edtPinCode.getText().toString();
				MobileNo = edtMobileNo.getText().toString();
				EmailId = edtEmailId.getText().toString();
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", EmailId));
				params.add(new BasicNameValuePair("password", Password));
				params.add(new BasicNameValuePair("shop_name", ShopName));
		    	params.add(new BasicNameValuePair("address", Address));
		    	params.add(new BasicNameValuePair("state", State));
		    	params.add(new BasicNameValuePair("city", City));
		     	params.add(new BasicNameValuePair("pincode", PinCode));
		     	params.add(new BasicNameValuePair("mobile_no", MobileNo));
		     	// getting JSON Object
		     	// Note that create product url accepts POST method	
		     	JSONObject json = jsonParser.makeHttpRequest(url_create_product,"POST", params);
		     	// check log cat fro response
		     	Log.d("Create Response", json.toString());
		     	// check for success tag
		     	try 
		     	{
				    int success = json.getInt(TAG_SUCCESS);
				    if (success == 1) 
				    {
					     String successack = "Shop registered successfully.";
					     Log.d("Success Response", successack );  
				    } 
				    else 
				    {

				    }
		     	} 
		     	catch (JSONException e) 
		     	{
		     		e.printStackTrace();
		     	}
		   return null;
		  }
		  protected void onPostExecute(String file_url) 
		  {
			   // dismiss the dialog once done
			   pDialog.dismiss();
			   Toast.makeText(getApplicationContext(), "Shop registered successfully.", Toast.LENGTH_SHORT).show();
		  }
	}*/
}
