package com.example.shopdroid;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class GraphicalReports extends Activity 
{
	private RadioGroup rgbGraphData,rdbGraphData;
	private RadioButton rbTransaction, rbStockStatus;
	private TextView tvDateFrom, tvDateTo, tvTransactionType, tvXAxis, tvYAxis, tvDataXAxis, tvDataYAxis;
	private Button btDateFrom, btDateTo, btXAxis, btYAxis, btDataXAxis, btDataYAxis;
	private Spinner spnTransactionType;
	private String formattedDate;
	private Calendar myCalendar1 = Calendar.getInstance();
	private Calendar myCalendar2 = Calendar.getInstance();
	private SimpleDateFormat df;
	SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graphical_reports);
		
		rgbGraphData = (RadioGroup)findViewById(R.id.rgbGraphType);
		
		tvDateFrom = (TextView)findViewById(R.id.tvDateFrom);
		tvDateTo = (TextView)findViewById(R.id.tvDateTo);
		tvTransactionType = (TextView)findViewById(R.id.tvTransactionType);
		tvXAxis = (TextView)findViewById(R.id.tvXAxis);
		tvYAxis = (TextView)findViewById(R.id.tvYAxis);
		tvDataXAxis = (TextView)findViewById(R.id.tvDataXAxis);
		tvDataYAxis = (TextView)findViewById(R.id.tvDataYAxis);
		
		btDateFrom = (Button)findViewById(R.id.btDateFrom);
		btDateTo = (Button)findViewById(R.id.btDateTo);
		
		btXAxis = (Button)findViewById(R.id.btXAxis);
		btXAxis.setText("Quantity");
		
		btYAxis = (Button)findViewById(R.id.btYAxis);
		btYAxis.setText("Products");
		
		btDataXAxis = (Button)findViewById(R.id.btDataXAxis);
		btDataXAxis.setText("Quantity");
		
		btDataYAxis = (Button)findViewById(R.id.btDataYAxis);
		btDataYAxis.setText("Products");
		
		spnTransactionType = (Spinner)findViewById(R.id.spnTransactionType);
		
		Calendar c = Calendar.getInstance();

		df = new SimpleDateFormat("dd-MM-yyyy");
		formattedDate = df.format(c.getTime());
		
		btDateTo.setText(formattedDate);
		btDateFrom.setText(formattedDate);
		
		rgbGraphData.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				int id = rgbGraphData.getCheckedRadioButtonId();
				View radioButton = rgbGraphData.findViewById(id);
				if(radioButton.getId() == R.id.rbTransaction)
				{
					//Making the textViews visible
					tvDateFrom.setVisibility(View.VISIBLE);
					tvDateTo.setVisibility(View.VISIBLE);
					tvTransactionType.setVisibility(View.VISIBLE);
					tvXAxis.setVisibility(View.VISIBLE);
					tvYAxis.setVisibility(View.VISIBLE);
					
					//Making the corresponding buttons visible
					btDateFrom.setVisibility(View.VISIBLE);
					btDateTo.setVisibility(View.VISIBLE);
					btXAxis.setVisibility(View.VISIBLE);
					btYAxis.setVisibility(View.VISIBLE);
					spnTransactionType.setVisibility(View.VISIBLE);
					
					//Making the textviews and buttons invisible which are of no interest
					tvDataXAxis.setVisibility(View.GONE);
					tvDataYAxis.setVisibility(View.GONE);
					btDataXAxis.setVisibility(View.GONE);
					btDataYAxis.setVisibility(View.GONE);
				}
				if(radioButton.getId() == R.id.rbStockStatus)
				{
					//Making the textViews invisible which are of no interest
					tvDateFrom.setVisibility(View.GONE);
					tvDateTo.setVisibility(View.GONE);
					tvTransactionType.setVisibility(View.GONE);
					tvXAxis.setVisibility(View.GONE);
					tvYAxis.setVisibility(View.GONE);
					
					//Making the corresponding buttons invisible which are of no interest
					btDateFrom.setVisibility(View.GONE);
					btDateTo.setVisibility(View.GONE);
					btXAxis.setVisibility(View.GONE);
					btYAxis.setVisibility(View.GONE);
					spnTransactionType.setVisibility(View.GONE);
					
					//Making the textviews and buttons visible
					tvDataXAxis.setVisibility(View.VISIBLE);
					tvDataYAxis.setVisibility(View.VISIBLE);
					btDataXAxis.setVisibility(View.VISIBLE);
					btDataYAxis.setVisibility(View.VISIBLE);
				}
				
			}
		});
	}
	
	DatePickerDialog.OnDateSetListener dateFrom = new DatePickerDialog.OnDateSetListener() 
	{
	    @Override
	    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
	    {
	        myCalendar1.set(Calendar.YEAR, year);
	        myCalendar1.set(Calendar.MONTH, monthOfYear);
	        myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth); 
	        df = new SimpleDateFormat("dd-MM-yyyy");
			btDateFrom.setText(df.format(myCalendar1.getTime()));
	    }
	};
	
	public void onDateFromClick (View v)
	{
		new DatePickerDialog(GraphicalReports.this, dateFrom, myCalendar1
	            .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
	            myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
	}
	
	DatePickerDialog.OnDateSetListener dateTo = new DatePickerDialog.OnDateSetListener() 
	{
	    @Override
	    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
	    {
	        myCalendar2.set(Calendar.YEAR, year);
	        myCalendar2.set(Calendar.MONTH, monthOfYear);
	        myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth); 
	        df = new SimpleDateFormat("dd-MM-yyyy");
			btDateTo.setText(df.format(myCalendar2.getTime()));
	    }
	};
	
	public void onDateToClick (View v)
	{
		new DatePickerDialog(GraphicalReports.this, dateTo, myCalendar2
	            .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
	            myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
	}
	
	public void onClickXAxis(View v)
	{
		if(btXAxis.getText() == "Quantity")
		{
			btXAxis.setText("Value");
		}
		else
		{
			btXAxis.setText("Quantity");
		}
	}
	
	public void onClickYAxis(View v)
	{
		if(btYAxis.getText() == "Products")
		{
			btYAxis.setText("Category");
		}
		else
		{
			btYAxis.setText("Products");
		}
	}
	
	public void onClickDataXAxis(View v)
	{
		if(btDataXAxis.getText() == "Quantity")
		{
			btDataXAxis.setText("Value");
		}
		else
		{
			btDataXAxis.setText("Quantity");
		}
	}
	
	public void onClickDataYAxis(View v)
	{
		if(btDataYAxis.getText() == "Products")
		{
			btDataYAxis.setText("Category");
		}
		else
		{
			btDataYAxis.setText("Products");
		}
	}
	public void onClickProcess(View v)
	{
		// get selected radio button from radioGroup
        int selectedId = rgbGraphData .getCheckedRadioButtonId();

        // find the radio button by returned id
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        String selectedRadioButton = radioButton.getText().toString();
		if(selectedRadioButton.equals("Transaction"))
		{
			String dateFrom = btDateFrom.getText().toString();
			String dateTo = btDateTo.getText().toString();
			String XAxis = btXAxis.getText().toString();
			String YAxis = btYAxis.getText().toString();
			String transactionType = spnTransactionType.getSelectedItem().toString();
			//String transactionType = "Sales";
			Toast.makeText(getApplicationContext(),  transactionType , Toast.LENGTH_SHORT).show();
			if(XAxis == "Quantity")
			{
				XAxis = "quantity";
			}
			if(XAxis == "Value")
			{
				XAxis = "unit_cost";
			}
			if(YAxis == "Products")
			{
				YAxis = "product_name";
			}
			if(YAxis == "Category")
			{
				YAxis = "category";
			}
			Intent i = new Intent(getApplicationContext(), Graph.class);
			   
		   //Create the bundle
			Bundle bundle = new Bundle();

		   //Add your data to bundle
			bundle.putString("ReportType", selectedRadioButton);
			bundle.putString("DateFrom", dateFrom);
			bundle.putString("DateTo", dateTo);
	   		bundle.putString("XAxis", XAxis);
	   		bundle.putString("YAxis", YAxis);
	   		bundle.putString("TransactionType", transactionType);

		   //Add the bundle to the intent
		   i.putExtras(bundle);

		   //Fire that second activity
		   	startActivity(i);
		}
		else if(selectedRadioButton.equals("Stock Status"))
		{
			/*Toast toast= Toast.makeText(getApplicationContext(),  selectedRadioButton , Toast.LENGTH_SHORT);
			toast.show();*/
			String dataXAxis = btDataXAxis.getText().toString();
			String dataYAxis = btDataYAxis.getText().toString();
			if(dataXAxis == "Quantity")
			{
				dataXAxis = "quantity";
			}
			if(dataXAxis == "Value")
			{
				dataXAxis = "unit_cost";
			}
			if(dataYAxis == "Products")
			{
				dataYAxis = "product_name";
			}
			if(dataYAxis == "Category")
			{
				dataYAxis = "category";
			}
			Intent i = new Intent(getApplicationContext(), Graph.class);
			   
		   //Create the bundle
			Bundle bundle = new Bundle();

		   //Add your data to bundle
			bundle.putString("ReportType", selectedRadioButton);
	   		bundle.putString("DataXAxis", dataXAxis);
	   		bundle.putString("DataYAxis", dataYAxis);

		   //Add the bundle to the intent
		   i.putExtras(bundle);

		   //Fire that second activity
		   	startActivity(i);
			
			
		}
	}
}
