package com.example.shopdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Settings extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
	}
	
	public void addListenerOnProductCategories(View v)
	{
		Intent intent = new Intent(getApplicationContext(), ProductCategories.class);
		startActivity(intent);
	}
	
	public void addListenerOnPersonalisation(View v)
	{
		Intent intent = new Intent(getApplicationContext(), Personalisation.class);
		startActivity(intent);
	}
	
	public void addListenerOnPasswordSettings(View v)
	{
		Intent intent = new Intent(getApplicationContext(), PasswordSetting.class);
		startActivity(intent);
	}
}
