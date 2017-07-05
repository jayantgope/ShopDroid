package com.example.shopdroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class TransactionsDBAdapter 
{

 public static final String KEY_ROWID = "_id";
 public static final String KEY_DATE = "date";
 public static final String KEY_PID = "pid";
 public static final String KEY_PRODUCT_NAME = "product_name";
 public static final String KEY_TRANSACTION_MODE = "transaction_mode";
 public static final String KEY_UNIT_COST = "unit_cost";
 public static final String KEY_QUANTITY = "quantity";

 private static final String TAG = "TransactionsDbAdapter";
 private DatabaseHelper mDbHelper;
 private SQLiteDatabase mDb;

 private static final String DATABASE_NAME = "ShopDroid";
 private static final String SQLITE_TABLE = "transactions";
 private static final int DATABASE_VERSION = 1;

 private final Context mCtx;

 private static final String DATABASE_CREATE =
  "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
  KEY_ROWID + " integer PRIMARY KEY autoincrement," +
  KEY_TRANSACTION_MODE + "," +
  KEY_PID + "," +
  KEY_PRODUCT_NAME + "," +
  KEY_UNIT_COST +"," +
  KEY_QUANTITY + "," +
  KEY_DATE + "," +
  " UNIQUE (" + KEY_ROWID +"));";

 private static class DatabaseHelper extends SQLiteOpenHelper 
 {
	  DatabaseHelper(Context context) 
	  {
		  super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }


  @Override
  public void onCreate(SQLiteDatabase db) 
  {
	  Log.w(TAG, DATABASE_CREATE);
	  db.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
  {
	   Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
	     + newVersion + ", which will destroy all old data");
	   db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
	   onCreate(db);
  }
 }

 public TransactionsDBAdapter(Context ctx) {
  this.mCtx = ctx;
 }

 public TransactionsDBAdapter open() throws SQLException 
 {
  mDbHelper = new DatabaseHelper(mCtx);
  mDb = mDbHelper.getWritableDatabase();
  return this;
 }

 public void close() {
  if (mDbHelper != null) {
   mDbHelper.close();
  }
 }
 	
 /*public long createTransactions(String product_code, String bar_code, String product_name, String category, String location, int quantity, int unit_cost, String image) 
 {
  ContentValues initialValues = new ContentValues();
  initialValues.put(KEY_PRODUCT_CODE, product_code);
  initialValues.put(KEY_BAR_CODE, bar_code);
  initialValues.put(KEY_PRODUCT_NAME, product_name);
  initialValues.put(KEY_CATEGORY, category);
  initialValues.put(KEY_LOCATION, location);
  initialValues.put(KEY_QUANTITY, quantity);
  initialValues.put(KEY_UNIT_COST, unit_cost);
  initialValues.put(KEY_IMAGE, image);
  return mDb.insert(SQLITE_TABLE, null, initialValues);
 }*/

 /*public boolean deleteAllCountries() {

  int doneDelete = 0;
  doneDelete = mDb.delete(SQLITE_TABLE, null , null);
  Log.w(TAG, Integer.toString(doneDelete));
  return doneDelete > 0;

 }*/

 public Cursor fetchAllTransactions(String inputText) throws SQLException 
 {
	  Log.w(TAG, inputText);
	  Cursor mCursor = null;
	  if (inputText == null  ||  inputText.length () == 0)  {
	  mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID, KEY_TRANSACTION_MODE,
			  KEY_PID, KEY_PRODUCT_NAME, KEY_UNIT_COST, KEY_QUANTITY, KEY_DATE}, 
	     null, null, null, null, null);
  }
  else 
  {
	  int id = Integer.parseInt(inputText);
	  mCursor = mDb.query(true, SQLITE_TABLE, new String[]{KEY_ROWID, KEY_TRANSACTION_MODE,
			  KEY_PID, KEY_PRODUCT_NAME, KEY_UNIT_COST, KEY_QUANTITY, KEY_DATE}, 
	  KEY_ROWID + " = " + id + "", null,
	  null, null, null, null);
  }
  if (mCursor != null) 
  {
	  mCursor.moveToFirst();
  }
  
  return mCursor;

 }

 public Cursor fetchAllTransactions() 
 {

  Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID, KEY_TRANSACTION_MODE,
		  KEY_PID, KEY_PRODUCT_NAME, KEY_UNIT_COST, KEY_QUANTITY, KEY_DATE}, 
    null, null, null, null, null);

  if (mCursor != null) {
   mCursor.moveToFirst();
  }
  return mCursor;
 }

/* public void insertSomeCountries() {

  createCountry("AFG","Afghanistan","Asia","Southern and Central Asia");
  createCountry("ALB","Albania","Europe","Southern Europe");
  createCountry("DZA","Algeria","Africa","Northern Africa");
  createCountry("ASM","American Samoa","Oceania","Polynesia");
  createCountry("AND","Andorra","Europe","Southern Europe");
  createCountry("AGO","Angola","Africa","Central Africa");
  createCountry("AIA","Anguilla","North America","Caribbean");

 }*/

}
