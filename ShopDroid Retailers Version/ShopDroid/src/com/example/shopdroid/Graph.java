package com.example.shopdroid;

import java.util.ArrayList;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Graph extends Activity
{
	SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graph);
		
		//Get the bundle
		Bundle bundle = getIntent().getExtras();

		//Extract the data…
		String ReportType = bundle.getString("ReportType"); 
		String DataXAxis = bundle.getString("DataXAxis"); 
		String DataYAxis = bundle.getString("DataYAxis");
		String XAxis = bundle.getString("XAxis"); 
		String YAxis = bundle.getString("YAxis");
		String DateFrom = bundle.getString("DateFrom");
		String DateTo = bundle.getString("DateTo");
		String TransactionType = bundle.getString("TransactionType");
		if(ReportType.equals("Stock Status"))
		{
			onStock(DataXAxis, DataYAxis);
		}
		else if(ReportType.equals("Transaction"))
		{
			onTransaction(DateFrom, DateTo, XAxis, YAxis, TransactionType);
			/*Toast.makeText(getApplicationContext(), "Date From : " + DateFrom,
	                Toast.LENGTH_LONG).show();*/
		}
		
	}
	public void onTransaction(String dateFrom, String dateTo, String dataXAxis, String dataYAxis, String transactionType)
	{
		int i=0;
		db=openOrCreateDatabase("ShopDroid", Context.MODE_PRIVATE, null);
		if(dataXAxis.equals("quantity") && dataYAxis.equals("product_name"))
		{
			//String sqlQuery = "select t.*, p.category, p.product_name from transactions as t, products as p  INNER JOIN products ON t.pid = p._id where t.date between'"+dateFrom+"' AND '"+dateTo +"'";
			String sqlQuery = "SELECT * from transactions where date between '"+dateFrom+"' AND '"+dateTo+"' AND transaction_mode = '"+ transactionType + "'";
			Cursor MyCursor = db.rawQuery(sqlQuery, null);
	    	ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
	    	ArrayList<String> labels = new ArrayList<String>();
			float quantityFirst;
			if (MyCursor.moveToFirst()) 
	        {
	        	do 
	            {
	        		quantityFirst=Float.parseFloat(MyCursor.getString(MyCursor.getColumnIndex("quantity")));
	        		entries.add(new BarEntry(quantityFirst, i));
	        		labels.add(MyCursor.getString(MyCursor.getColumnIndex("product_name")));
	        		i++;
	            } while (MyCursor.moveToNext());
	        }
			BarDataSet dataset = new BarDataSet(entries,"Products");
			dataset.setValueTextColor(Color.BLACK);	
			BarChart chart = new BarChart(this);
			setContentView(chart);
			BarData data = new BarData(labels, dataset);
			chart.setData(data);
			data.setDrawValues(true);
			
			chart.setDescription("No of Quantities");
			dataset.setColors(ColorTemplate.COLORFUL_COLORS);
			chart.animateY(5000);
		}
		else if(dataXAxis.equals("quantity") && dataYAxis.equals("category"))
		{
			String sqlQuery = "SELECT DISTINCT t.quantity as quantity, p.category from transactions as t, products as p  INNER JOIN products ON t.pid = p._id where  t.transaction_mode = '"+transactionType+"' AND t.date between'"+dateFrom+"' AND '"+dateTo +"'";
			//String sqlQuery = "select category, sum(quantity) as quantity from products group by category";
			Cursor MyCursor = db.rawQuery(sqlQuery, null);
	    	ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
	    	ArrayList<String> labels = new ArrayList<String>();
			float quantityFirst;
			if (MyCursor.moveToFirst()) 
	        {
	        	do 
	            {
	        		quantityFirst=Float.parseFloat(MyCursor.getString(MyCursor.getColumnIndex("quantity")));
	        		entries.add(new BarEntry(quantityFirst, i));
	        		labels.add(MyCursor.getString(MyCursor.getColumnIndex("category")));
	        		i++;
	            } while (MyCursor.moveToNext());
	        }
			BarDataSet dataset = new BarDataSet(entries,"Categories");
			dataset.setValueTextColor(Color.BLACK);	
			BarChart chart = new BarChart(this);
			setContentView(chart);
			BarData data = new BarData(labels, dataset);
			chart.setData(data);
			data.setDrawValues(true);
			
			chart.setDescription("Quantities");
			dataset.setColors(ColorTemplate.COLORFUL_COLORS);
			chart.animateY(5000);
		}
		else if(dataXAxis.equals("unit_cost") && dataYAxis.equals("product_name"))
		{
			//String sqlQuery = "select t.*, p.category, p.product_name from transactions as t, products as p  INNER JOIN products ON t.pid = p._id where t.date between'"+dateFrom+"' AND '"+dateTo +"'";
			//String sqlQuery = "SELECT * from transactions where date between '"+dateFrom+"' AND '"+dateTo+"' AND transaction_mode = '"+ transactionType + "'";
			String sqlQuery = "select product_name, (quantity * unit_cost) as 'values' from transactions";
			Cursor MyCursor = db.rawQuery(sqlQuery, null);
	    	ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
	    	ArrayList<String> labels = new ArrayList<String>();
			float quantityFirst;
			if (MyCursor.moveToFirst()) 
	        {
	        	do 
	            {
	        		quantityFirst=Float.parseFloat(MyCursor.getString(MyCursor.getColumnIndex("values")));
	        		entries.add(new BarEntry(quantityFirst, i));
	        		labels.add(MyCursor.getString(MyCursor.getColumnIndex("product_name")));
	        		i++;
	            } while (MyCursor.moveToNext());
	        }
			BarDataSet dataset = new BarDataSet(entries,"Products");
			dataset.setValueTextColor(Color.BLACK);	
			BarChart chart = new BarChart(this);
			setContentView(chart);
			BarData data = new BarData(labels, dataset);
			chart.setData(data);
			data.setDrawValues(true);
			
			chart.setDescription("Total Worth (in Rs.)");
			dataset.setColors(ColorTemplate.COLORFUL_COLORS);
			chart.animateY(5000);
		}
		else if(dataXAxis.equals("unit_cost") && dataYAxis.equals("category"))
		{
			//String sqlQuery = "select t.*, p.category, p.product_name from transactions as t, products as p  INNER JOIN products ON t.pid = p._id where t.date between'"+dateFrom+"' AND '"+dateTo +"'";
			//String sqlQuery = "SELECT * from transactions where date between '"+dateFrom+"' AND '"+dateTo+"' AND transaction_mode = '"+ transactionType + "'";
			String sqlQuery = "SELECT DISTINCT (t.unit_cost*t.quantity) as 'values', p.category from transactions as t, products as p  INNER JOIN products ON t.pid = p._id where  t.transaction_mode = '"+transactionType+"' AND t.date between '"+dateFrom+"' AND '"+dateTo+"'";
			Cursor MyCursor = db.rawQuery(sqlQuery, null);
	    	ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
	    	ArrayList<String> labels = new ArrayList<String>();
			float quantityFirst;
			if (MyCursor.moveToFirst()) 
	        {
	        	do 
	            {
	        		quantityFirst=Float.parseFloat(MyCursor.getString(MyCursor.getColumnIndex("values")));
	        		entries.add(new BarEntry(quantityFirst, i));
	        		labels.add(MyCursor.getString(MyCursor.getColumnIndex("category")));
	        		i++;
	            } while (MyCursor.moveToNext());
	        }
			BarDataSet dataset = new BarDataSet(entries,"Categories");
			dataset.setValueTextColor(Color.BLACK);	
			BarChart chart = new BarChart(this);
			setContentView(chart);
			BarData data = new BarData(labels, dataset);
			chart.setData(data);
			data.setDrawValues(true);
			
			chart.setDescription("Total Worth (in Rs.)");
			dataset.setColors(ColorTemplate.COLORFUL_COLORS);
			chart.animateY(5000);
		}
	}
	
	public void onStock(String dataXAxis, String dataYAxis)
	{
		int i=0;
		db=openOrCreateDatabase("ShopDroid", Context.MODE_PRIVATE, null);
		if(dataXAxis.equals("quantity") && dataYAxis.equals("product_name"))
		{
			String sqlQuery = "SELECT * FROM products";
			Cursor MyCursor = db.rawQuery(sqlQuery, null);
	    	ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
	    	ArrayList<String> labels = new ArrayList<String>();
			float quantityFirst;
			if (MyCursor.moveToFirst()) 
	        {
	        	do 
	            {
	        		quantityFirst=Float.parseFloat(MyCursor.getString(MyCursor.getColumnIndex("quantity")));
	        		entries.add(new BarEntry(quantityFirst, i));
	        		labels.add(MyCursor.getString(MyCursor.getColumnIndex("product_name")));
	        		i++;
	            } while (MyCursor.moveToNext());
	        }
			BarDataSet dataset = new BarDataSet(entries,"Products");
			dataset.setValueTextColor(Color.BLACK);	
			BarChart chart = new BarChart(this);
			setContentView(chart);
			BarData data = new BarData(labels, dataset);
			chart.setData(data);
			data.setDrawValues(true);
			
			chart.setDescription("No of Quantities");
			dataset.setColors(ColorTemplate.COLORFUL_COLORS);
			chart.animateY(5000);
		}
		else if(dataXAxis.equals("quantity") && dataYAxis.equals("category"))
		{
			String sqlQuery = "select category, sum(quantity) as quantity from products group by category";
			Cursor MyCursor = db.rawQuery(sqlQuery, null);
	    	ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
	    	ArrayList<String> labels = new ArrayList<String>();
			float quantityFirst;
			if (MyCursor.moveToFirst()) 
	        {
	        	do 
	            {
	        		quantityFirst=Float.parseFloat(MyCursor.getString(MyCursor.getColumnIndex("quantity")));
	        		entries.add(new BarEntry(quantityFirst, i));
	        		labels.add(MyCursor.getString(MyCursor.getColumnIndex("category")));
	        		i++;
	            } while (MyCursor.moveToNext());
	        }
			BarDataSet dataset = new BarDataSet(entries,"Categories");
			dataset.setValueTextColor(Color.BLACK);	
			BarChart chart = new BarChart(this);
			setContentView(chart);
			BarData data = new BarData(labels, dataset);
			chart.setData(data);
			data.setDrawValues(true);
			
			chart.setDescription("No of Quantities");
			dataset.setColors(ColorTemplate.COLORFUL_COLORS);
			chart.animateY(5000);
		}
		else if(dataXAxis.equals("unit_cost") && dataYAxis.equals("product_name"))
		{
			String sqlQuery = "select product_name, (quantity * unit_cost) as 'values' from products";
			Cursor MyCursor = db.rawQuery(sqlQuery, null);
	    	ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
	    	ArrayList<String> labels = new ArrayList<String>();
			float quantityFirst;
			if (MyCursor.moveToFirst()) 
	        {
	        	do 
	            {
	        		quantityFirst=Float.parseFloat(MyCursor.getString(MyCursor.getColumnIndex("values")));
	        		entries.add(new BarEntry(quantityFirst, i));
	        		labels.add(MyCursor.getString(MyCursor.getColumnIndex("product_name")));
	        		i++;
	            } while (MyCursor.moveToNext());
	        }
			BarDataSet dataset = new BarDataSet(entries,"Products");
			dataset.setValueTextColor(Color.BLACK);	
			BarChart chart = new BarChart(this);
			setContentView(chart);
			BarData data = new BarData(labels, dataset);
			chart.setData(data);
			data.setDrawValues(true);
			
			chart.setDescription("Total Worth (in Rs.)");
			dataset.setColors(ColorTemplate.COLORFUL_COLORS);
			chart.animateY(5000);
		}
		else if(dataXAxis.equals("unit_cost") && dataYAxis.equals("category"))
		{
			String sqlQuery = "select category, sum(quantity*unit_cost) as 'values' from products group by category";
			Cursor MyCursor = db.rawQuery(sqlQuery, null);
	    	ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
	    	ArrayList<String> labels = new ArrayList<String>();
			float quantityFirst;
			if (MyCursor.moveToFirst()) 
	        {
	        	do 
	            {
	        		quantityFirst=Float.parseFloat(MyCursor.getString(MyCursor.getColumnIndex("values")));
	        		entries.add(new BarEntry(quantityFirst, i));
	        		labels.add(MyCursor.getString(MyCursor.getColumnIndex("category")));
	        		i++;
	            } while (MyCursor.moveToNext());
	        }
			BarDataSet dataset = new BarDataSet(entries,"Categories");
			dataset.setValueTextColor(Color.BLACK);	
			BarChart chart = new BarChart(this);
			setContentView(chart);
			BarData data = new BarData(labels, dataset);
			chart.setData(data);
			data.setDrawValues(true);
			
			chart.setDescription("Total Worth (in Rs.)");
			dataset.setColors(ColorTemplate.COLORFUL_COLORS);
			chart.animateY(5000);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
