package canyalkin.net.memoapp;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import canyalkin.net.memoapp.alarm.management.MyAlarmBroadcastReceiver;

public class MemoAppMainActivity extends ActionBarActivity {

	private static final String TAG_MEMO_APP_MAIN_ACTIVITY = "MemoAppMainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_memo_app_main);
		Log.d(TAG_MEMO_APP_MAIN_ACTIVITY, "Main onCreate called!");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG_MEMO_APP_MAIN_ACTIVITY, "Main onResume called!");
		Intent intent = new Intent(this, MyAlarmBroadcastReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.cancel(sender);
		Log.d(TAG_MEMO_APP_MAIN_ACTIVITY, "Alarm canceled!");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG_MEMO_APP_MAIN_ACTIVITY, "Main stop called!");
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MyAlarmBroadcastReceiver alarmBroadcastReceiver=new MyAlarmBroadcastReceiver();
		alarmBroadcastReceiver.setOnetimeTimer(this);
		Log.d(TAG_MEMO_APP_MAIN_ACTIVITY, "Main onPause called!");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG_MEMO_APP_MAIN_ACTIVITY, "Main onDestroy called!");
		
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
