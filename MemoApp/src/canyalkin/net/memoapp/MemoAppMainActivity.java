package canyalkin.net.memoapp;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MemoAppMainActivity extends ActionBarActivity {

	private static final String TAG_MEMO_APP_MAIN_ACTIVITY = "MemoAppMainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_memo_app_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.memo_app_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void addItem(View view){
		Log.d(TAG_MEMO_APP_MAIN_ACTIVITY, "AddItem clikced!");
		Intent intent = new Intent(this, PopupAddActivity.class);
		startActivity(intent);
	}
	
	public void ask(View view){
		Log.d(TAG_MEMO_APP_MAIN_ACTIVITY, "Ask clikced!");
		Intent intent = new Intent(this, AskActivity.class);
		startActivity(intent);
	}
	
}
