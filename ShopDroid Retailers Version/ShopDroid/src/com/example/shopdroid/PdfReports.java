package com.example.shopdroid;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Header;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class PdfReports extends Activity
{
	private RadioGroup rgbReportType, rgbProductListOptions, rgbDetailedSummary;// rgbSortByProductCode, rgbSortByProductName, rgbSortByCategories;
	private TextView tvDateFrom, tvDateTo, tvReportOptions;
	private Button btDateFrom, btDateTo;
	private EditText etReportName;
	private String formattedDate;
	private Calendar myCalendar1 = Calendar.getInstance();
	private Calendar myCalendar2 = Calendar.getInstance();
	private SimpleDateFormat df;
	SQLiteDatabase db;
	String Product_Code, Bar_Code, Product_Name, Category, Location, Quantity, Unit_Cost, image;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pdf_reports);
		etReportName = (EditText)findViewById(R.id.etReportName);
		rgbProductListOptions = (RadioGroup)findViewById(R.id.rgbProductListOptions);
		
		rgbDetailedSummary = (RadioGroup)findViewById(R.id.rgbDetailedSummary);
		
		
		
		tvDateFrom = (TextView)findViewById(R.id.tvDateFrom);
		tvDateTo = (TextView)findViewById(R.id.tvDateTo);
		tvReportOptions = (TextView)findViewById(R.id.tvReportOptions);
		
		btDateFrom = (Button)findViewById(R.id.btDateFrom);
		btDateTo = (Button)findViewById(R.id.btDateTo);
		
		Calendar c = Calendar.getInstance();

		df = new SimpleDateFormat("dd-MM-yyyy");
		formattedDate = df.format(c.getTime());
		
		btDateTo.setText(formattedDate);
		btDateFrom.setText(formattedDate);
		
		rgbReportType = (RadioGroup)findViewById(R.id.rgbReportType);
		rgbReportType.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				int id = rgbReportType.getCheckedRadioButtonId();
				View radioButton = rgbReportType.findViewById(id);
				if(radioButton.getId() == R.id.rbProductList)
				{
					//llTransaction.setVisibility(View.GONE);
					//llProductList.setVisibility(View.VISIBLE);
					//Toast.makeText(getApplicationContext(), "Product List", Toast.LENGTH_SHORT).show();
					rgbProductListOptions.setVisibility(View.VISIBLE);
					tvReportOptions.setVisibility(View.VISIBLE);
					
					tvDateFrom.setVisibility(View.GONE);
					tvDateTo.setVisibility(View.GONE);
					btDateFrom.setVisibility(View.GONE);
					btDateTo.setVisibility(View.GONE);
					
					rgbDetailedSummary.setVisibility(View.GONE);
				}
				
				if(radioButton.getId() == R.id.rbTransaction)
				{
					//llProductList.setVisibility(View.GONE);
					/*llTransaction.setVisibility(View.VISIBLE);*/
					//Toast.makeText(getApplicationContext(), "Transaction", Toast.LENGTH_SHORT).show();
					tvDateFrom.setVisibility(View.VISIBLE);
					tvDateTo.setVisibility(View.VISIBLE);
					tvReportOptions.setVisibility(View.VISIBLE);
					btDateFrom.setVisibility(View.VISIBLE);
					btDateTo.setVisibility(View.VISIBLE);
					
					rgbDetailedSummary.setVisibility(View.VISIBLE);
					
					rgbProductListOptions.setVisibility(View.GONE);
				}
				
				if(radioButton.getId() == R.id.rbInventoryStatus)
				{
					//llProductList.setVisibility(View.GONE);
					/*llTransaction.setVisibility(View.VISIBLE);*/
					//Toast.makeText(getApplicationContext(), "Transaction", Toast.LENGTH_SHORT).show();
					tvDateFrom.setVisibility(View.GONE);
					tvDateTo.setVisibility(View.GONE);
					tvReportOptions.setVisibility(View.GONE);
					btDateFrom.setVisibility(View.GONE);
					btDateTo.setVisibility(View.GONE);
					
					rgbDetailedSummary.setVisibility(View.GONE);
					
					rgbProductListOptions.setVisibility(View.GONE);
					
					
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
		new DatePickerDialog(PdfReports.this, dateFrom, myCalendar1
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
		new DatePickerDialog(PdfReports.this, dateTo, myCalendar2
	            .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
	            myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
	}
	
	public void onClickProcess(View v)
	{
		String reportName = etReportName.getText().toString();
		// get selected radio button from radioGroup	
        int selectedId = rgbReportType .getCheckedRadioButtonId();

        // find the radio button by returned id
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        String selectedRadioButton = radioButton.getText().toString();
		if(selectedRadioButton.equals("Product List"))
		{
	        int ReportOptionsSelectedId =  rgbProductListOptions.getCheckedRadioButtonId();
	        RadioButton radioButtonReportOptions = (RadioButton) findViewById(ReportOptionsSelectedId);
	        String selectedRadioButtonReportOptions = radioButtonReportOptions.getText().toString();
			if(selectedRadioButtonReportOptions.equals("Sort by product code"))
			{
				db=openOrCreateDatabase("ShopDroid", Context.MODE_PRIVATE, null);
				Cursor c1 = db.rawQuery("SELECT * FROM products ORDER BY product_code", null);
			 	Calendar cal = new GregorianCalendar();
			 	String am_pm = (cal.get(Calendar.AM_PM)==0)?"AM":"PM";
			 	String currentDate = cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR)+"/" + " Created on  "+cal.get(Calendar.HOUR)+" : "+cal.get(Calendar.MINUTE)+" : "+cal.get(Calendar.SECOND)+" "+am_pm;
			    String filename= reportName + "_"+ cal.get(Calendar.DAY_OF_MONTH)+"_"+(cal.get(Calendar.MONTH)+1)+"_"+cal.get(Calendar.YEAR)+"_" + "_"+cal.get(Calendar.HOUR)+"_"+cal.get(Calendar.MINUTE)+"_"+cal.get(Calendar.SECOND)+"_"+am_pm +  ".pdf";
			    Document document=new Document();  // create the document
			    File root = new File(Environment.getExternalStorageDirectory(), "ShopDroid/Reports"); 
			    if (!root.exists()) 
			    {
			    	root.mkdirs();   
			    }
			    try
			    {
			    	// generate pdf file in that directory
				     File reportFile = new File(root,filename);  
				     PdfWriter.getInstance(document,new FileOutputStream(reportFile));
				     
				     // opening the directory
				     document.open();  
				     //creating paragraph  and adding value in it
				     Paragraph dateParagraph = new Paragraph();
				     String pdfheder1="Date : "+cal.get(Calendar.YEAR)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.DAY_OF_MONTH)+"";
				     dateParagraph.add(pdfheder1);
				     document.add(dateParagraph);
				     
				     Paragraph timeParagraph = new Paragraph();
				     String time= "Time (Created on) : "+cal.get(Calendar.HOUR)+" : "+cal.get(Calendar.MINUTE)+" : "+cal.get(Calendar.SECOND)+" "+am_pm +"";
				     timeParagraph.add(time);
				     document.add(timeParagraph);
				     
				     Paragraph reportTypeParagraph = new Paragraph();
				     String pdfheder2="Product List - Sorted By :  Product Code";
				     reportTypeParagraph.add(pdfheder2);
				     document.add(reportTypeParagraph);
				     
				     LineSeparator ls = new LineSeparator();
				     document.add(new Chunk(ls));
				     /*document.add(new Chunk("Product Code"));
				     document.add(new Chunk("Bar Code"));
				     document.add(new Chunk("Product Name"));
				     document.add(new Chunk("Category"));
				     document.add(new Chunk("Location"));
				     document.add(new Chunk("Unit Cost"));*/
				     
				     PdfPTable table = new PdfPTable(7); // Code 1
				     table.setWidthPercentage(100);
				     
				     // Code 2
				      table.addCell("Product Code");
				      table.addCell("Bar Code");
				      table.addCell("Product Name");
				      table.addCell("Category");
				      table.addCell("Location");
				      table.addCell("Stock");
				      table.addCell("Unit Cost");
				      // now fetch data from database and display it in pdf

				      while (c1.moveToNext()) 
				      {
				    	  // get the value from database
				          String productCode = c1.getString(1);
				          String barCode = c1.getString(2);
				          String productName = c1.getString(3);
				          String category = c1.getString(4);
				          String location = c1.getString(5);
				          String stock = c1.getString(6);
  		        	   	  String unitCost = c1.getString(7);
  		        	   	  
				          table.addCell(productCode);
				          table.addCell(barCode);
				          table.addCell(productName);
				          table.addCell(category);
				          table.addCell(location);
				          table.addCell(stock);
				          table.addCell(unitCost);
				      }
				      document.add(table);    
				      document.addCreationDate();
				        document.close();
				        Toast.makeText(getApplicationContext(), "Created...", Toast.LENGTH_LONG).show();


				          /*int temp_ex=Integer.parseInt(ex_bdgt);
				          ttlbud=ttlbud+temp_ex;
				          int temp_used=Integer.parseInt(used_bdgt);
				          usdbud=usdbud+temp_used;*/
		    	}
			     catch (Exception e)
			     {
			    	 
			     }
			     Intent intent = new Intent(Intent.ACTION_VIEW);
                //must give the early pdf file name in here as "DelCusReport.pdf"
			     File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ShopDroid/Reports", filename); 
                intent.setDataAndType( Uri.fromFile( file ), "application/pdf" );
                startActivity(intent.setDataAndType( Uri.fromFile( file ), "application/pdf" ));
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                Intent target = Intent.createChooser(intent, "Open File Using");
                try {
                    startActivity(target);
                } catch (ActivityNotFoundException e) 
                {
                    Log.e("PDFCreator", "ActivityNotFoundException:" + e);
                }
			}
			else if(selectedRadioButtonReportOptions.equals("Sort by product name"))
			{
				db=openOrCreateDatabase("ShopDroid", Context.MODE_PRIVATE, null);
				Cursor c1 = db.rawQuery("SELECT * FROM products ORDER BY product_name", null);
			 	Calendar cal = new GregorianCalendar();
			 	String am_pm = (cal.get(Calendar.AM_PM)==0)?"AM":"PM";
			 	String currentDate = cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR)+"/" + " Created on  "+cal.get(Calendar.HOUR)+" : "+cal.get(Calendar.MINUTE)+" : "+cal.get(Calendar.SECOND)+" "+am_pm;
			    String filename= reportName + "_"+ cal.get(Calendar.DAY_OF_MONTH)+"_"+(cal.get(Calendar.MONTH)+1)+"_"+cal.get(Calendar.YEAR)+"_" + "_"+cal.get(Calendar.HOUR)+"_"+cal.get(Calendar.MINUTE)+"_"+cal.get(Calendar.SECOND)+"_"+am_pm +  ".pdf";
			    Document document=new Document();  // create the document
			    File root = new File(Environment.getExternalStorageDirectory(), "ShopDroid/Reports"); 
			    if (!root.exists()) 
			    {
			    	root.mkdirs();   
			    }
			    try
			    {
			    	// generate pdf file in that directory
				     File reportFile = new File(root,filename);  
				     PdfWriter.getInstance(document,new FileOutputStream(reportFile));
				     
				     // opening the directory
				     document.open();  
				     //creating paragraph  and adding value in it
				     Paragraph dateParagraph = new Paragraph();
				     String pdfheder1="Date : "+cal.get(Calendar.YEAR)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.DAY_OF_MONTH)+"";
				     dateParagraph.add(pdfheder1);
				     document.add(dateParagraph);
				     
				     Paragraph timeParagraph = new Paragraph();
				     String time= "Time (Created on) : "+cal.get(Calendar.HOUR)+" : "+cal.get(Calendar.MINUTE)+" : "+cal.get(Calendar.SECOND)+" "+am_pm +"";
				     timeParagraph.add(time);
				     document.add(timeParagraph);
				     
				     Paragraph reportTypeParagraph = new Paragraph();
				     String pdfheder2="Product List - Sorted By :  Product Name";
				     reportTypeParagraph.add(pdfheder2);
				     document.add(reportTypeParagraph);
				     
				     LineSeparator ls = new LineSeparator();
				     document.add(new Chunk(ls));
				     
				     PdfPTable table = new PdfPTable(7); // Code 1
				     table.setWidthPercentage(100);
				     
				     // Code 2
				     table.addCell("Product Name");
				      table.addCell("Product Code");
				      table.addCell("Bar Code");
				      table.addCell("Category");
				      table.addCell("Location");
				      table.addCell("Stock");
				      table.addCell("Unit Cost");

				      while (c1.moveToNext()) 
				      {
				          String productCode = c1.getString(1);
				          String barCode = c1.getString(2);
				          String productName = c1.getString(3);
				          String category = c1.getString(4);
				          String location = c1.getString(5);
				          String stock = c1.getString(6);
  		        	   	  String unitCost = c1.getString(7);
  		        	   	  
  		        	   	table.addCell(productName);
  		        	   	  table.addCell(productCode);
				          table.addCell(barCode);
				          table.addCell(category);
				          table.addCell(location);
				          table.addCell(stock);
				          table.addCell(unitCost);
				      }
				      document.add(table);    
				      document.addCreationDate();
				        document.close();
				        Toast.makeText(getApplicationContext(), "Created...", Toast.LENGTH_LONG).show();
		    	}
			     catch (Exception e)
			     {
			    	 
			     }
			     Intent intent = new Intent(Intent.ACTION_VIEW);
                //must give the early pdf file name in here as "DelCusReport.pdf"
			     File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ShopDroid/Reports", filename); 
                intent.setDataAndType( Uri.fromFile( file ), "application/pdf" );
                startActivity(intent.setDataAndType( Uri.fromFile( file ), "application/pdf" ));
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                Intent target = Intent.createChooser(intent, "Open File Using");
                try {
                    startActivity(target);
                } catch (ActivityNotFoundException e) 
                {
                    Log.e("PDFCreator", "ActivityNotFoundException:" + e);
                }
			}
			else if(selectedRadioButtonReportOptions.equals("Sort by categories"))
			{
				db=openOrCreateDatabase("ShopDroid", Context.MODE_PRIVATE, null);
				Cursor c1 = db.rawQuery("SELECT * FROM products ORDER BY category", null);
			 	Calendar cal = new GregorianCalendar();
			 	String am_pm = (cal.get(Calendar.AM_PM)==0)?"AM":"PM";
			 	String currentDate = cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR)+"/" + " Created on  "+cal.get(Calendar.HOUR)+" : "+cal.get(Calendar.MINUTE)+" : "+cal.get(Calendar.SECOND)+" "+am_pm;
			    String filename= reportName + "_"+ cal.get(Calendar.DAY_OF_MONTH)+"_"+(cal.get(Calendar.MONTH)+1)+"_"+cal.get(Calendar.YEAR)+"_" + "_"+cal.get(Calendar.HOUR)+"_"+cal.get(Calendar.MINUTE)+"_"+cal.get(Calendar.SECOND)+"_"+am_pm +  ".pdf";
			    Document document=new Document();  // create the document
			    File root = new File(Environment.getExternalStorageDirectory(), "ShopDroid/Reports"); 
			    if (!root.exists()) 
			    {
			    	root.mkdirs();   
			    }
			    try
			    {
			    	// generate pdf file in that directory
				     File reportFile = new File(root,filename);  
				     PdfWriter.getInstance(document,new FileOutputStream(reportFile));
				     
				     // opening the directory
				     document.open();  
				     //creating paragraph  and adding value in it
				     Paragraph dateParagraph = new Paragraph();
				     String pdfheder1="Date : "+cal.get(Calendar.YEAR)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.DAY_OF_MONTH)+"";
				     dateParagraph.add(pdfheder1);
				     document.add(dateParagraph);
				     
				     Paragraph timeParagraph = new Paragraph();
				     String time= "Time (Created on) : "+cal.get(Calendar.HOUR)+" : "+cal.get(Calendar.MINUTE)+" : "+cal.get(Calendar.SECOND)+" "+am_pm +"";
				     timeParagraph.add(time);
				     document.add(timeParagraph);
				     
				     Paragraph reportTypeParagraph = new Paragraph();
				     String pdfheder2="Product List - Sorted By :  Product Categories";
				     reportTypeParagraph.add(pdfheder2);
				     document.add(reportTypeParagraph);
				     
				     LineSeparator ls = new LineSeparator();
				     document.add(new Chunk(ls));
				     
				     PdfPTable table = new PdfPTable(7); // Code 1
				     table.setWidthPercentage(100);
				     
				     // Code 2
				     table.addCell("Category");
				     table.addCell("Product Name");
				      table.addCell("Product Code");
				      table.addCell("Bar Code");
				      table.addCell("Location");
				      table.addCell("Stock");
				      table.addCell("Unit Cost");

				      while (c1.moveToNext()) 
				      {
				          String productCode = c1.getString(1);
				          String barCode = c1.getString(2);
				          String productName = c1.getString(3);
				          String category = c1.getString(4);
				          String location = c1.getString(5);
				          String stock = c1.getString(6);
  		        	   	  String unitCost = c1.getString(7);
  		        	   	  
  		        	   	  table.addCell(category);
  		        	   	  table.addCell(productCode);
  		        	   	  table.addCell(productName);
				          table.addCell(barCode);
				          table.addCell(location);
				          table.addCell(stock);
				          table.addCell(unitCost);
				      }
				      document.add(table);    
				      document.addCreationDate();
				        document.close();
				        Toast.makeText(getApplicationContext(), "Created...", Toast.LENGTH_LONG).show();
		    	}
			     catch (Exception e)
			     {
			    	 
			     }
			     Intent intent = new Intent(Intent.ACTION_VIEW);
                //must give the early pdf file name in here as "DelCusReport.pdf"
			     File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ShopDroid/Reports", filename); 
                intent.setDataAndType( Uri.fromFile( file ), "application/pdf" );
                startActivity(intent.setDataAndType( Uri.fromFile( file ), "application/pdf" ));
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                Intent target = Intent.createChooser(intent, "Open File Using");
                try {
                    startActivity(target);
                } catch (ActivityNotFoundException e) 
                {
                    Log.e("PDFCreator", "ActivityNotFoundException:" + e);
                }
			}
		}
		if(selectedRadioButton.equals("Transaction"))
		{
			String dateFrom = btDateFrom.getText().toString();
			String dateTo = btDateTo.getText().toString();
			int id = rgbDetailedSummary .getCheckedRadioButtonId();
	        RadioButton radioButton1 = (RadioButton) findViewById(id);
	        String reportOptions = radioButton1.getText().toString();
			db=openOrCreateDatabase("ShopDroid", Context.MODE_PRIVATE, null);
			Cursor c1 = db.rawQuery("SELECT DISTINCT t.date, p.product_code, t.product_name, t.transaction_mode, t.quantity, t.unit_cost, (t.unit_cost * t.quantity) as total_cost FROM transactions AS t, products AS p INNER JOIN products on t.pid = p._id where date between '"+dateFrom+"' AND '"+dateTo+"'", null);
		 	Calendar cal = new GregorianCalendar();
		 	String am_pm = (cal.get(Calendar.AM_PM)==0)?"AM":"PM";
		 	String currentDate = cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR)+"/" + " Created on  "+cal.get(Calendar.HOUR)+" : "+cal.get(Calendar.MINUTE)+" : "+cal.get(Calendar.SECOND)+" "+am_pm;
		    String filename= reportName + "_"+ cal.get(Calendar.DAY_OF_MONTH)+"_"+(cal.get(Calendar.MONTH)+1)+"_"+cal.get(Calendar.YEAR)+"_" + "_"+cal.get(Calendar.HOUR)+"_"+cal.get(Calendar.MINUTE)+"_"+cal.get(Calendar.SECOND)+"_"+am_pm +  ".pdf";
		    Document document=new Document();  // create the document
		    File root = new File(Environment.getExternalStorageDirectory(), "ShopDroid/Reports"); 
		    if (!root.exists()) 
		    {
		    	root.mkdirs();   
		    }
		    try
		    {
		    	// generate pdf file in that directory
			     File reportFile = new File(root,filename);  
			     PdfWriter.getInstance(document,new FileOutputStream(reportFile));
			     
			     // opening the directory
			     document.open();  
			     //creating paragraph  and adding value in it
			     Paragraph dateParagraph = new Paragraph();
			     String pdfheder1="Date : "+cal.get(Calendar.YEAR)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.DAY_OF_MONTH)+"";
			     dateParagraph.add(pdfheder1);
			     document.add(dateParagraph);
			     
			     Paragraph timeParagraph = new Paragraph();
			     String time= "Time (Created on) : "+cal.get(Calendar.HOUR)+" : "+cal.get(Calendar.MINUTE)+" : "+cal.get(Calendar.SECOND)+" "+am_pm +"";
			     timeParagraph.add(time);
			     document.add(timeParagraph);
			     if(reportOptions.equalsIgnoreCase("Detailed"))
			     {
				     Paragraph reportTypeParagraph = new Paragraph();
				     String pdfheder2="Transaction List -  Period : " + dateFrom + " To " + dateTo;
				     reportTypeParagraph.add(pdfheder2);
				     document.add(reportTypeParagraph);
				     
				     LineSeparator ls = new LineSeparator();
				     document.add(new Chunk(ls));
				     
				     PdfPTable table = new PdfPTable(7); // Code 1
				     table.setWidthPercentage(100);
				     table.setSpacingAfter(10f);
				     
				     // Code 2
				     table.addCell("Date");
				     table.addCell("Product Code");
				     table.addCell("Product Name");
				      table.addCell("Transaction Type");
				      table.addCell("Quantity");
				      table.addCell("Unit Cost");
				      table.addCell("Total Cost");
				      
				      int sum = 0;
				      while (c1.moveToNext()) 
				      {
				          
			        	  
			        	  	String transactionMode = c1.getString(c1.getColumnIndex("transaction_mode"));
				        	String productName = c1.getString(c1.getColumnIndex("product_name"));
				        	String productCode = c1.getString(c1.getColumnIndex("product_code"));
				        	String unitCost = c1.getString(c1.getColumnIndex("unit_cost"));
				        	String quantity = c1.getString(c1.getColumnIndex("quantity"));
				    		String date = c1.getString(c1.getColumnIndex("date"));
				    		String totalCost = c1.getString(c1.getColumnIndex("total_cost"));
				    		sum = sum + Integer.parseInt(totalCost);
				    		
				    		table.addCell(date);
		        	   	  	table.addCell(productCode);
		        	   	  	table.addCell(productName);
		        	   	  	table.addCell(transactionMode);
		        	   	  	table.addCell(quantity);
		        	   	  	table.addCell(unitCost);
		        	   	  	table.addCell(totalCost);
				      }
				      PdfPCell cell = new PdfPCell(new Phrase("Total Transaction : "));
				      cell.setColspan(6);
				      cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				      table.addCell(cell);
				      
				      
				      PdfPCell cell1 = new PdfPCell(new Phrase(" " + Integer.toString(sum)));
				      table.addCell(cell1);
			     
			      document.add(table);    
			      document.addCreationDate();
			        document.close();
			        Toast.makeText(getApplicationContext(), "Created...", Toast.LENGTH_LONG).show();
			     }
			     else if (reportOptions.equalsIgnoreCase("Summary"))
			     {
			    	 Paragraph reportTypeParagraph = new Paragraph();
				     String pdfheder2="Transaction List (SUMMARY) -  During " + dateFrom + " To " + dateTo;
				     reportTypeParagraph.add(pdfheder2);
				     document.add(reportTypeParagraph);
				     
				     LineSeparator ls = new LineSeparator();
				     document.add(new Chunk(ls));
				     
				     PdfPTable table = new PdfPTable(5); // Code 1
				     table.setWidthPercentage(100);
				     table.setSpacingAfter(10f);
				     
				     
				     table.addCell("Product Code");
				     table.addCell("Product Name");
				      table.addCell("Transaction Type");
				      table.addCell("Quantity");
				      table.addCell("Unit Cost");
				      
				      int sum = 0;
				      while (c1.moveToNext()) 
				      {
				          
			        	  
			        	  	String transactionMode = c1.getString(c1.getColumnIndex("transaction_mode"));
				        	String productName = c1.getString(c1.getColumnIndex("product_name"));
				        	String productCode = c1.getString(c1.getColumnIndex("product_code"));
				        	String unitCost = c1.getString(c1.getColumnIndex("unit_cost"));
				        	String quantity = c1.getString(c1.getColumnIndex("quantity"));
				    		String date = c1.getString(c1.getColumnIndex("date"));
				    		String totalCost = c1.getString(c1.getColumnIndex("total_cost"));
				    		sum = sum + Integer.parseInt(totalCost);
				    		
		        	   	  	table.addCell(productCode);
		        	   	  	table.addCell(productName);
		        	   	  	table.addCell(transactionMode);
		        	   	  	table.addCell(quantity);
		        	   	  	table.addCell(unitCost);
				      }
				      PdfPCell cell = new PdfPCell(new Phrase("Total Transaction : "));
				      cell.setColspan(4);
				      cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				      table.addCell(cell);
				      
				      
				      PdfPCell cell1 = new PdfPCell(new Phrase(" " + Integer.toString(sum)));
				      table.addCell(cell1);
				      document.add(table);    
				      document.addCreationDate();
				        document.close();
				        Toast.makeText(getApplicationContext(), "Created...", Toast.LENGTH_LONG).show();
			     }
			     
	    	}
		     catch (Exception e)
		     {
		    	 
		     }
		     Intent intent = new Intent(Intent.ACTION_VIEW);
            //must give the early pdf file name in here as "DelCusReport.pdf"
		     File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ShopDroid/Reports", filename); 
            intent.setDataAndType( Uri.fromFile( file ), "application/pdf" );
            startActivity(intent.setDataAndType( Uri.fromFile( file ), "application/pdf" ));
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            Intent target = Intent.createChooser(intent, "Open File Using");
            try {
                startActivity(target);
            } catch (ActivityNotFoundException e) 
            {
                Log.e("PDFCreator", "ActivityNotFoundException:" + e);
            }
		}
		if(selectedRadioButton.equals("Inventory Status"))
		{
			db=openOrCreateDatabase("ShopDroid", Context.MODE_PRIVATE, null);
			Cursor c1 = db.rawQuery("SELECT product_code, product_name, category, location, quantity, unit_cost, (unit_cost * quantity) as stock_value FROM products", null);
		 	Calendar cal = new GregorianCalendar();
		 	String am_pm = (cal.get(Calendar.AM_PM)==0)?"AM":"PM";
		 	String currentDate = cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR)+"/" + " Created on  "+cal.get(Calendar.HOUR)+" : "+cal.get(Calendar.MINUTE)+" : "+cal.get(Calendar.SECOND)+" "+am_pm;
		    String filename= reportName + "_"+ cal.get(Calendar.DAY_OF_MONTH)+"_"+(cal.get(Calendar.MONTH)+1)+"_"+cal.get(Calendar.YEAR)+"_" + "_"+cal.get(Calendar.HOUR)+"_"+cal.get(Calendar.MINUTE)+"_"+cal.get(Calendar.SECOND)+"_"+am_pm +  ".pdf";
		    Document document=new Document();  // create the document
		    File root = new File(Environment.getExternalStorageDirectory(), "ShopDroid/Reports"); 
		    if (!root.exists()) 
		    {
		    	root.mkdirs();   
		    }
		    try
		    {
		    	// generate pdf file in that directory
			     File reportFile = new File(root,filename);  
			     PdfWriter.getInstance(document,new FileOutputStream(reportFile));
			     
			     // opening the directory
			     document.open();  
			     //creating paragraph  and adding value in it
			     Paragraph dateParagraph = new Paragraph();
			     String pdfheder1="Date : "+cal.get(Calendar.YEAR)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.DAY_OF_MONTH)+"";
			     dateParagraph.add(pdfheder1);
			     document.add(dateParagraph);
			     
			     Paragraph timeParagraph = new Paragraph();
			     String time= "Time (Created on) : "+cal.get(Calendar.HOUR)+" : "+cal.get(Calendar.MINUTE)+" : "+cal.get(Calendar.SECOND)+" "+am_pm +"";
			     timeParagraph.add(time);
			     document.add(timeParagraph);
			     
			     Paragraph reportTypeParagraph = new Paragraph();
			     String pdfheder2="Inventory Status Report";
			     reportTypeParagraph.add(pdfheder2);
			     document.add(reportTypeParagraph);
			     
			     LineSeparator ls = new LineSeparator();
			     document.add(new Chunk(ls));
			     
			     PdfPTable table = new PdfPTable(7); // Code 1
			     table.setWidthPercentage(100);
			     
			     // Code 2
			     table.addCell("Product Code");
			     table.addCell("Product Name");
			     table.addCell("Category");
			      table.addCell("Location");
			      table.addCell("Stock");
			      table.addCell("Unit Cost");
			      table.addCell("Stock Value");
			      
			      int sum = 0;
			      while (c1.moveToNext()) 
			      {
			        	String productName = c1.getString(c1.getColumnIndex("product_name"));
			        	String productCode = c1.getString(c1.getColumnIndex("product_code"));
			        	String category = c1.getString(c1.getColumnIndex("category"));
			        	String location = c1.getString(c1.getColumnIndex("location"));
			        	String unitCost = c1.getString(c1.getColumnIndex("unit_cost"));
			        	String stock = c1.getString(c1.getColumnIndex("quantity"));
			    		String stockValue = c1.getString(c1.getColumnIndex("stock_value"));
			    		sum = sum + Integer.parseInt(stockValue);
		        	   	  
		        	   	  table.addCell(productCode);
		        	   	  table.addCell(productName);
			          table.addCell(category);
			          table.addCell(location);
			          table.addCell(stock);
			          table.addCell(unitCost);
			          table.addCell(stockValue);
			      }
			      PdfPCell cell = new PdfPCell(new Phrase("Grand Total : "));
			      cell.setColspan(6);
			      cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			      table.addCell(cell);
			      
			      
			      PdfPCell cell1 = new PdfPCell(new Phrase(" " + Integer.toString(sum)));
			      table.addCell(cell1);
			      document.add(table);    
			      document.addCreationDate();
			        document.close();
			        Toast.makeText(getApplicationContext(), "Created...", Toast.LENGTH_LONG).show();
	    	}
		     catch (Exception e)
		     {
		    	 
		     }
		     Intent intent = new Intent(Intent.ACTION_VIEW);
            //must give the early pdf file name in here as "DelCusReport.pdf"
		     File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ShopDroid/Reports", filename); 
            intent.setDataAndType( Uri.fromFile( file ), "application/pdf" );
            startActivity(intent.setDataAndType( Uri.fromFile( file ), "application/pdf" ));
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            Intent target = Intent.createChooser(intent, "Open File Using");
            try {
                startActivity(target);
            } catch (ActivityNotFoundException e) 
            {
                Log.e("PDFCreator", "ActivityNotFoundException:" + e);
            }
		}
	}

}
