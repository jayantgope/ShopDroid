package com.example.shopdroid;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SignIn extends Activity
{
	private EditText etUsername, etPassword;
	private Button btLogin;
	private ProgressDialog pDialog;
	private Session session;
	private String prefsUsername;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		session = new Session(getApplicationContext());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in);
		etUsername = (EditText) findViewById(R.id.etUsername);
		etPassword = (EditText) findViewById(R.id.etpassword);
		btLogin = (Button) findViewById(R.id.btLogin);
		btLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				pDialog = new ProgressDialog(SignIn.this);
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
				String url = URL1.url_login_shop;

				client.post(url, params, new AsyncHttpResponseHandler() {
					// When the response returned by REST has Http
					// response code '200'
					@Override
					public void onSuccess(String response) {
						// Hide Progress Dialog

						pDialog.dismiss();
						try {
							final JSONObject jObj = new JSONObject(response);
							if (jObj.getBoolean("login")) {
								Intent i = new Intent(SignIn.this,
										MainActivity.class);
								i.putExtra("userid", jObj.getString("userid"));

								startActivity(i);
								runOnUiThread(new Runnable() {
									public void run() {
										try {
											prefsUsername = jObj.getString("username");
											session.setUsername(prefsUsername);
											Toast.makeText(SignIn.this,
													"Welcome " + jObj.getString("shop_name"),
													Toast.LENGTH_SHORT).show();
											finish();
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
							
							} else {
								Toast.makeText(SignIn.this,
										"Incorrect credentials", Toast.LENGTH_SHORT)
										.show();
							}

						} catch (Exception e) { 
							e.printStackTrace();
							Toast.makeText(getBaseContext(),
									e.toString(), Toast.LENGTH_LONG)
									.show();
						}

					}

					// When the response returned by REST has Http
					// response code other than '200' such as '404',
					// '500' or '403' etc
					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						pDialog.dismiss();
					}
				});


			}
		});
	}

	
	
}
