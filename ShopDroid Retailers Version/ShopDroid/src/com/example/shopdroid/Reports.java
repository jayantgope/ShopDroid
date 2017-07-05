package com.example.shopdroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Reports extends Activity
{
	Button btnPdfReports, btnGraphicalReports;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_options);
		//showReportDialog();
		
	}
	
	public void pdfReports(View v)
	{
		Intent intent = new Intent(getApplicationContext(), PdfReports.class);
		startActivity(intent);
		finish();
	}
	
	public void graphicalReports(View v)
	{
		Intent intent = new Intent(getApplicationContext(), GraphicalReports.class);
		startActivity(intent);
		finish();
	}
	
	public void onClickCancel(View v)
	{
		finish();
	}
}
