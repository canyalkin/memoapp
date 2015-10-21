package canyalkin.net.memoapp.db;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.YuvImage;
import android.util.Log;
import canyalkin.net.memoapp.AppUtil;

public class WordDictionaryDAO extends AbstractDAO{
	
	private static final String TAG = "WordDictionaryDAO";
	
	
	private final String SELECT_KEYS = "SELECT "
			+ MySQLiteHelper.COLUMN_KEY_WORD + "," + MySQLiteHelper.COLUMN_CREATE_DATE+","
			+ MySQLiteHelper.COLUMN_NUM_ASKED + "," + MySQLiteHelper.COLUMN_WRONG_ANSWER 
			+" FROM "+MySQLiteHelper.TABLE_WORD_DICTIONARY
			+ " order by ( ("+MySQLiteHelper.COLUMN_WRONG_ANSWER+"+1)*1.0 / " + "("+ MySQLiteHelper.COLUMN_NUM_ASKED+"+1)) desc" ;
			
	
	private final String SELECT_VALUES="SELECT "+MySQLiteHelper.COLUMN_VALUE+" FROM "+MySQLiteHelper.TABLE_KEY_WORD_VALUES 
			+ " where " + MySQLiteHelper.COLUMN_VALUE_KEY_WORD + "= ?" ;
	
	
	public WordDictionaryDAO(Context context) {
		super(context);
	}
	
	
	public boolean templateInsert(Object o){
		WordDictionary wordDictionary = (WordDictionary)o;
		ContentValues keyTableValues = new ContentValues();
	    keyTableValues.put(MySQLiteHelper.COLUMN_KEY_WORD, wordDictionary.getKeyword());

		try{
			long retVal = db.insertOrThrow(MySQLiteHelper.TABLE_WORD_DICTIONARY, null, keyTableValues);
			if(retVal!=-1){
				insertValues(db,wordDictionary);
			}else{
				AppUtil.showAlertMessage(context, "Error on adding!");
			}
		}catch(SQLiteConstraintException constraintException){
			Log.e(TAG, "constraint exception:"+constraintException.toString());
			AppUtil.showAlertMessage(context, "Word has been entered before.");
			return false;
		}catch(SQLException sqlEx){
			Log.e(TAG, "sql exception:"+sqlEx.toString());
			return false;
		}
		return true;
	}
	
	private void insertValues(SQLiteDatabase db, WordDictionary wordDictionary) {
		String key = wordDictionary.getKeyword();
		List<String> valueList=wordDictionary.getMeaning();
		for (String string : valueList) {
			ContentValues values = new ContentValues();
		    values.put(MySQLiteHelper.COLUMN_VALUE_KEY_WORD,key);
		    values.put(MySQLiteHelper.COLUMN_VALUE,string);
		    try{
		    	db.insertOrThrow(MySQLiteHelper.TABLE_KEY_WORD_VALUES,null,values);
		    }catch(SQLiteConstraintException sqliteCE){
		    	Log.e(TAG, "sql exception:"+sqliteCE.toString());
		    	AppUtil.showAlertMessage(context, "Word has been entered before.");
		    	continue;
		    }catch (SQLException sqle) {
		    	Log.e(TAG, "sql exception:"+sqle.toString());
		    	AppUtil.showAlertMessage(context, "Value can not be added.");	
			}
		}
		
	}


	public List<WordDictionary> templateReadAll(){
		List<WordDictionary> wordList=new LinkedList<WordDictionary>();
		Cursor cursor=db.rawQuery(SELECT_KEYS, null);
		
	    cursor.moveToFirst();
	    
	    while(!cursor.isAfterLast()){
	    	WordDictionary wd=new WordDictionary();
	    	wd.setKeyword(cursor.getString(0));
	    	LinkedList<String> valueList=new LinkedList<String>();
	    	Cursor valueCursor=db.rawQuery(SELECT_VALUES, new String[]{String.valueOf(wd.getKeyword())});
	    	valueCursor.moveToFirst();
	    	while(!valueCursor.isAfterLast()){
	    		valueList.add(valueCursor.getString(0));
	    		valueCursor.moveToNext();
	    	}
	    	wd.setMeaning(valueList);
	    	String dateString = cursor.getString(1);
	    	DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	try {
	    		Date date = iso8601Format.parse(dateString);
	    		wd.setCreateDate(date);
	    	} catch (ParseException e) {
	    		Log.e(TAG, "Parsing ISO8601 datetime failed", e);
	    	}
	    	wd.setNumAsked(cursor.getInt(2));
	    	wd.setNumWrong(cursor.getInt(3));
	    	wordList.add(wd);
	    	Log.d(TAG, wd.toString());
	    	cursor.moveToNext();
	    	
	    }
	    return wordList;
		
	}
	
	public boolean updateWrongAnsAndNumAsked(String key,int wrongNum,int numAsked){
		
		open();
		ContentValues newValues = new ContentValues();
		newValues.put(MySQLiteHelper.COLUMN_WRONG_ANSWER, wrongNum);
		newValues.put(MySQLiteHelper.COLUMN_NUM_ASKED, numAsked);

		String[] args = new String[]{key};
		int ret = db.update(MySQLiteHelper.TABLE_WORD_DICTIONARY, newValues, MySQLiteHelper.COLUMN_KEY_WORD + "=?", args);
		close();
		if(ret>0){
			return true;
		}
		return false;
		
	}

}
