package com.example.shopdroid2;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Login extends Activity 
{
	private ProgressDialog pDialog;
	private EditText etUsername, etPassword;
	private Button btLogin;
	private Session session;
	private String prefsUsernameCustomers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		session = new Session(getApplicationContext());
		etUsername = (EditText) findViewById(R.id.etUsername);
		etPassword = (EditText) findViewById(R.id.etpassword);
		btLogin = (Button) findViewById(R.id.btLogin);
		btLogin.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View arg0) 
			{
				pDialog = new ProgressDialog(Login.this);
				pDialog.setMessage("Logging In...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
				String username = etUsername.getText().toString();
				String password = etPassword.getText().toString();
				RequestParams params = new RequestParams();
				params.add("username", username);
				params.add("password", password);

				AsyncHttpClient client = new AsyncHttpClient();

				// Don't forget to change the IP address to your LAN address. Port
				// no as
				// well.
				String url = URL1.url_login;

				client.post(url, params, new AsyncHttpResponseHandler() 
				{
					@Override
					public void onSuccess(String response) 
					{
						pDialog.dismiss();
						try 
						{
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getBoolean("login")) 
							{
								Bundle bundle = getIntent().getExtras();
								String status = bundle.getString("status");
								if(status.equalsIgnoreCase("transaction"))
								{
									Intent i = new Intent(Login.this,PaymentOption.class);
									i.putExtra("userid", jObj.getString("username"));
									startActivity(i);
									finish();
								}
								else if(status.equalsIgnoreCase("main_activity"))
								{
									Intent i = new Intent(Login.this,MainActivity.class);
									i.putExtra("userid", jObj.getString("username"));
									startActivity(i);
									finish();
								}
								runOnUiThread(new Runnable() 
								{
									public void run() 
									{
										try 
										{
											prefsUsernameCustomers = jObj.getString("username");
											session.setUsername(prefsUsernameCustomers);
											Toast.makeText(Login.this,
													"Welcome " + jObj.getString("firstName"),
													Toast.LENGTH_SHORT).show();
											finish();
										} 
										catch (JSONException e) 
										{
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
							}
							else 
							{
								Toast.makeText(Login.this,
										"Incorrect credentials", Toast.LENGTH_SHORT)
										.show();
							}

						} 
						catch (Exception e) 
						{ 
							e.printStackTrace();
							Toast.makeText(getBaseContext(),e.toString(), Toast.LENGTH_LONG).show();
						}
					}
					@Override
					public void onFailure(int statusCode, Throwable error,String content) 
					{
						pDialog.dismiss();
					}
				});
			}
		});
	}
}
