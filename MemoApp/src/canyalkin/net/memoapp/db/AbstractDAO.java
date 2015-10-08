package canyalkin.net.memoapp.db;

import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public abstract class AbstractDAO{
	protected Context context;
	protected MySQLiteHelper dbHelper ;
	protected SQLiteDatabase db;
	
	public AbstractDAO(Context context) {
		this.context=context;
		this.dbHelper=new MySQLiteHelper(context);
	}
	
	
	protected void open() throws SQLException {
		 db = dbHelper.getWritableDatabase();
	}

	protected void close() {
		dbHelper.close();
	}
	
	protected abstract boolean templateInsert(Object object);
	protected abstract List templateReadAll();
	
	public boolean insert(Object object){
		open();
		boolean result = templateInsert(object);
		close();
		return result;
	}
	
	public List readAll(){
		open();
		List list = templateReadAll();
		close();
		return list;
	}
	
	

}
