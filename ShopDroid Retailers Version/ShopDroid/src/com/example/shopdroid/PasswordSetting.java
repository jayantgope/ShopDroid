package com.example.shopdroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class PasswordSetting extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_setting);
	}
	
	public void onClickApply(View v)
	{
		
	}
	
	public void onClickClose(View v)
	{
		finish();
	}
}
