package canyalkin.net.memoapp.db;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;
import canyalkin.net.memoapp.AppUtil;

public class WordDictionaryDAO extends AbstractDAO{
	
	private static final String TAG = "WordDictionaryDAO";
	
	
	private final String[] allColumns = { MySQLiteHelper.COLUMN_KEY_WORD,
		      MySQLiteHelper.COLUMN_MEANING,MySQLiteHelper.COLUMN_CREATE_DATE, MySQLiteHelper.COLUMN_NUM_ASKED, MySQLiteHelper.COLUMN_WRONG_ANSWER};
	
	public WordDictionaryDAO(Context context) {
		super(context);
	}
	
	
	public boolean templateInsert(Object o){
		WordDictionary wordDictionary = (WordDictionary)o;
		ContentValues values = new ContentValues();
	    values.put(MySQLiteHelper.COLUMN_KEY_WORD, wordDictionary.getKeyword());
	    values.put(MySQLiteHelper.COLUMN_MEANING, wordDictionary.getMeaning());
		try{
			db.insertOrThrow(MySQLiteHelper.TABLE_WORD_DICTIONARY, null, values);
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
	
	public List<WordDictionary> templateReadAll(){
		List<WordDictionary> wordList=new ArrayList<WordDictionary>(100);		
	    Cursor cursor = db.query(MySQLiteHelper.TABLE_WORD_DICTIONARY,
	            allColumns, null, null, null, null, null);
	    
	    cursor.moveToFirst();
	    
	    while(!cursor.isAfterLast()){
	    	WordDictionary wd=new WordDictionary();
	    	wd.setKeyword(cursor.getString(0));
	    	wd.setMeaning(cursor.getString(1));
	    	String dateString = cursor.getString(2);
	    	DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	try {
	    		Date date = iso8601Format.parse(dateString);
	    		wd.setCreateDate(date);
	    	} catch (ParseException e) {
	    		Log.e(TAG, "Parsing ISO8601 datetime failed", e);
	    	}
	    	wd.setNumAsked(cursor.getInt(3));
	    	wd.setNumWrong(cursor.getInt(4));
	    	wordList.add(wd);
	    	cursor.moveToNext();
	    	
	    }
	    return wordList;
		
	}

}
