package com.example.shopdroid2;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Registration extends Activity
{
	EditText etUsername,etPassword,etfName,etlName,etAddress,etPinCode,etContactNo;
	Spinner spnState,spnCity;
	ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration);
		etUsername=(EditText) findViewById(R.id.etUsername);
		etPassword=(EditText) findViewById(R.id.etPassword);
		etfName=(EditText) findViewById(R.id.etfName);
		etlName=(EditText) findViewById(R.id.etlName);
		etAddress=(EditText) findViewById(R.id.etAddress);
		
		etPinCode=(EditText) findViewById(R.id.etPinCode);
		etContactNo=(EditText) findViewById(R.id.etContactNo);
		spnState=(Spinner) findViewById(R.id.spnState);
		spnCity=(Spinner) findViewById(R.id.spnCity);
		
	}
	public void register(View v)
	{
		String username=etUsername.getText().toString();
		String password=etPassword.getText().toString();
		String fname=etfName.getText().toString();
		String lname=etlName.getText().toString();
		String address=etAddress.getText().toString();
		String pincode=etPinCode.getText().toString();
		String contact=etContactNo.getText().toString();
		String state=spnState.getSelectedItem().toString();
		String city=spnCity.getSelectedItem().toString();
		if(username.length()>0&&password.length()>0&&fname.length()>0)
		{
			dialog=ProgressDialog.show(Registration.this, "Please wait..", "Saving details..",true,true);
			RequestParams params = new RequestParams();
			params.add("username", username);
			params.add("password", password);
			params.add("fname", fname);
			params.add("lname", lname);
			params.add("address", address);
			params.add("pincode", pincode);
			params.add("contact", contact);
			params.add("state", state);
			params.add("city", city);
			AsyncHttpClient client = new AsyncHttpClient();
			String url = URL1.register;
			client.post(url, params, new AsyncHttpResponseHandler() 
			{
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
}
