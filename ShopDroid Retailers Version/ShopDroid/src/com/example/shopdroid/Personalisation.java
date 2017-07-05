package com.example.shopdroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Personalisation extends Activity
{
	Button btDateFormat, btPdfPaperSize, btPasswordCheck, btApply, btClose;
	EditText etCurrency;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personalisation);
		
		btDateFormat = (Button)findViewById(R.id.btDateFormat);
		btDateFormat.setText("MM/DD/YYYY");
		
		btPdfPaperSize = (Button)findViewById(R.id.btPdfPaperSize);
		btPdfPaperSize.setText("Letter - 11 x 8.5");
		
		btPasswordCheck = (Button)findViewById(R.id.btPasswordCheck);
		btPasswordCheck.setText("Disabled");
		
		btApply = (Button)findViewById(R.id.btApply);
		btClose = (Button)findViewById(R.id.btClose);
		
		etCurrency = (EditText)findViewById(R.id.etCurrency);
		
	}
	
	public void onClickDateFormat(View v)
	{
		if(btDateFormat.getText() == "MM/DD/YYYY")
		{
			btDateFormat.setText("DD/MM/YYYY");
		}
		else
		{
			btDateFormat.setText("MM/DD/YYYY");
		}
	}
	
	public void onClickPdfPaperSize(View v)
	{
		if(btPdfPaperSize.getText() == "Letter - 11 x 8.5")
		{
			btPdfPaperSize.setText("A4 - 12 x 8.25");
		}
		else
		{
			btPdfPaperSize.setText("Letter - 11 x 8.5");
		}
	}
	
	public void onClickPasswordCheck(View v)
	{
		if(btPasswordCheck.getText() == "Disabled")
		{
			btPasswordCheck.setText("Enabled");
		}
		else
		{
			btPasswordCheck.setText("Disabled");
		}
	}
	
	public void onClickApply(View v)
	{
		
	}
	
	public void onClickClose(View v)
	{
		finish();
	}
}
