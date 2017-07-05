package com.example.shopdroid2;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class DepartmentList extends Activity 
{
	ProgressDialog dialog;
	private static final String TAG_CAT = "category";
	String prefShopUsername;
	private ShopSession shop_session;
	ArrayList<HashMap<String, String>> productList=new ArrayList<HashMap<String,String>>();
	ListView list;
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.department_list);
		// Get the bundle
		Bundle bundle = getIntent().getExtras();
		
		shop_session = new ShopSession(getApplicationContext());
    	prefShopUsername = shop_session.getUsername();
		list = (ListView) findViewById(R.id.lvDepartmentListView);
		productList = new ArrayList<HashMap<String, String>>();
		getData();
		list.setOnItemClickListener(new OnItemClickListener() 
		{	
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) 
			{
				HashMap<String,String> map =(HashMap<String,String>)list.getItemAtPosition(position);
		  
				final String cat = map.get(TAG_CAT);
			   //String ProductId = cursor.getString(cursor.getColumnIndexOrThrow("username"));
				Intent i=new Intent(DepartmentList.this, DepartmentWiseProductListWithoutLogin.class);
				i.putExtra("cat",cat);
				startActivity(i);
				finish();
			}
		});
	}
	protected void onResume() 
	{
		super.onResume();
	}
	public void getData() 
	{
		dialog = ProgressDialog.show(DepartmentList.this, "Please wait..", "Getting details..", true, true);
		RequestParams params = new RequestParams();
		params.add("selected_shop_id", prefShopUsername);
		
		AsyncHttpClient client = new AsyncHttpClient();
		String url = URL1.getDepartments;

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
					JSONArray jArray = new JSONArray(response);
					int count = 0;
					while (count < jArray.length()) 
					{
						JSONObject c = jArray.getJSONObject(count);
						String category = c.getString(TAG_CAT);
						HashMap<String, String> persons = new HashMap<String, String>();
						persons.put(TAG_CAT, category);
						productList.add(persons);
						count++;
					}
					ListAdapter adapter = new SimpleAdapter(DepartmentList.this,
					productList, R.layout.department_list_items,
					new String[] 
					{  
						TAG_CAT 
					}, new int[] 
					{ 
						R.id.tvDisplayDepartments
					});
					list.setAdapter(adapter);
					
					if(!(count>0))
					{
						Toast.makeText(getBaseContext(), "No Departments Found in Shop : "+prefShopUsername, Toast.LENGTH_SHORT).show();
					}
				}
				
				catch (JSONException e) 
				{
					e.printStackTrace();
					Toast.makeText(getApplicationContext(),"No Departments available", Toast.LENGTH_SHORT).show();
				}
			}

			// When the response returned by REST has Http
			// response code other than '200' such as '404',
			// '500' or '403' etc
			@Override
			public void onFailure(int statusCode, Throwable error, String content) 
			{
				dialog.dismiss();
			}
		});
	}
}
