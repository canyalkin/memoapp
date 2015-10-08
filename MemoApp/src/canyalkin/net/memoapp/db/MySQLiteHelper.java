package canyalkin.net.memoapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {
	
	 public static final String TABLE_WORD_DICTIONARY = "WORD_DICTIONARY";
	 public static final String COLUMN_KEY_WORD = "WORD";
	 public static final String COLUMN_MEANING = "MEANING";
	 public static final String COLUMN_CREATE_DATE = "CREATE_DATE";
	 public static final String COLUMN_NUM_ASKED= "NUM_ASKED";
	 public static final String COLUMN_WRONG_ANSWER= "WRONG_ANSWER";
	 

	 private static final String DATABASE_NAME = "word.dictionary.db";
	 private static final int DATABASE_VERSION = 1;
	 
	 private static final String DATABASE_CREATE = "create table "
		      + TABLE_WORD_DICTIONARY +
		      "("
		      + COLUMN_KEY_WORD+ " text primary key, " 
		      + COLUMN_MEANING + " text not null," 
		      + COLUMN_CREATE_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
		      + COLUMN_NUM_ASKED + " int not null default 0,"
		      + COLUMN_WRONG_ANSWER+ " int not null default 0);";
	 private static final String CREATE_INDEX_WORNG_ANSWER="CREATE INDEX idx_wrong_answer ON "+ TABLE_WORD_DICTIONARY +"("+COLUMN_WRONG_ANSWER+");";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("MySQLiteHelper", "creating db...");
		db.execSQL(DATABASE_CREATE);
		db.execSQL(CREATE_INDEX_WORNG_ANSWER);
		Log.d("MySQLiteHelper", "creating db has finished!");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		

	}

}
