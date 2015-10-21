package canyalkin.net.memoapp.db;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {
	
	 public static final String TABLE_WORD_DICTIONARY = "WORD_DICTIONARY";
	 public static final String TABLE_KEY_WORD_VALUES="KEY_WORD_VALUES";
	 public static final String COLUMN_KEY_WORD = "WORD";
	 public static final String COLUMN_VALUE = "VALUE";
	 public static final String COLUMN_CREATE_DATE = "CREATE_DATE";
	 public static final String COLUMN_NUM_ASKED= "NUM_ASKED";
	 public static final String COLUMN_WRONG_ANSWER= "WRONG_ANSWER";
	public static final String COLUMN_VALUE_KEY_WORD = "VALUE_KEY_WORD";
	
	private static final String CONST_UNIQUE="CONST_UNIQUE";

	 private static final String DATABASE_NAME = "word.dictionary.db";
	 private static final int DATABASE_VERSION = 2;
	 
	 private static final String DATABASE_CREATE_KEY_TABLE = "create table "
		      + TABLE_WORD_DICTIONARY +
		      "("
		      + COLUMN_KEY_WORD+ " text primary key, " 
		      //+ COLUMN_MEANING + " text not null," 
		      + COLUMN_CREATE_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
		      + COLUMN_NUM_ASKED + " int not null default 0,"
		      + COLUMN_WRONG_ANSWER+ " int not null default 0);";

	 
	 private static final String DATABASE_CREATE_VALUE_TABLE= "create table "
	 		+ TABLE_KEY_WORD_VALUES 
	 		+ "("
	 		+ COLUMN_VALUE_KEY_WORD + " text not null,"
	 		+ COLUMN_VALUE + " text not null,"
	 		+ "CONSTRAINT "+CONST_UNIQUE+" UNIQUE("+COLUMN_VALUE_KEY_WORD+","+COLUMN_VALUE+"),"
	 		+ "FOREIGN KEY(" + COLUMN_VALUE_KEY_WORD + ") REFERENCES "+TABLE_WORD_DICTIONARY+" ("+ COLUMN_KEY_WORD +"));"
	 		;
	 
	 private static final String CREATE_INDEX_WORNG_ANSWER="CREATE INDEX idx_wrong_answer ON "+ TABLE_WORD_DICTIONARY +"("+COLUMN_WRONG_ANSWER+");";
	 private static final String CREATE_INDEX_KEY_MEANING="CREATE INDEX idx_key_meaning ON "+ TABLE_KEY_WORD_VALUES +"("+COLUMN_VALUE_KEY_WORD+");";
	 private static final String TAG = "MySQLiteHelper";
	 
	 

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("MySQLiteHelper", "creating db...");
		db.execSQL(DATABASE_CREATE_KEY_TABLE);
		db.execSQL(CREATE_INDEX_WORNG_ANSWER);
		db.execSQL(DATABASE_CREATE_VALUE_TABLE);
		db.execSQL(CREATE_INDEX_KEY_MEANING);
		Log.d("MySQLiteHelper", "creating db has finished!");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion==1){
			
			LinkedList<WordDictionary> wordList=new LinkedList<WordDictionary>();
			
			String SELECT_ALL="select WORD,MEANING,CREATE_DATE,NUM_ASKED,WRONG_ANSWER from "+TABLE_WORD_DICTIONARY;
			Cursor cursor = db.rawQuery(SELECT_ALL, null);
			cursor.moveToFirst();
		    
		    while(!cursor.isAfterLast()){
		    	WordDictionary wd=new WordDictionary();
		    	wd.setKeyword(cursor.getString(0));
		    	LinkedList<String> l=new LinkedList<String>();
		    	l.add(cursor.getString(1));
		    	wd.setMeaning(l);
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
		    
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORD_DICTIONARY);
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_KEY_WORD_VALUES);
		    db.execSQL("DROP INDEX IF EXISTS idx_wrong_answer" );
		    db.execSQL("DROP INDEX IF EXISTS idx_key_meaning" );
		    
		    onCreate(db);
		    
		    SimpleDateFormat dateFormat = new SimpleDateFormat(
	                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
	         
		    for (WordDictionary wordDictionary : wordList) {
		    	ContentValues insertValues = new ContentValues();
		    	insertValues.put(COLUMN_KEY_WORD, wordDictionary.getKeyword());
		    	insertValues.put(COLUMN_CREATE_DATE, dateFormat.format(wordDictionary.getCreateDate()));
		    	insertValues.put(COLUMN_NUM_ASKED, wordDictionary.getNumAsked());
		    	insertValues.put(COLUMN_WRONG_ANSWER, wordDictionary.getNumWrong());
		    	db.insert(TABLE_WORD_DICTIONARY, null, insertValues);
		    	
		    	insertValues = new ContentValues();
		    	insertValues.put(COLUMN_VALUE_KEY_WORD,wordDictionary.getKeyword());
		    	insertValues.put(COLUMN_VALUE,wordDictionary.getMeaning().get(0));
		    	
		    	db.insert(TABLE_KEY_WORD_VALUES, null, insertValues);
		    	
			}
		    
		    
			
		}

	}

}
