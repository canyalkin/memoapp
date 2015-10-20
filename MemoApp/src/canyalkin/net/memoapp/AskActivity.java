package canyalkin.net.memoapp;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import canyalkin.net.memoapp.ask.manager.IAskManager;
import canyalkin.net.memoapp.ask.manager.RandomAsk;

public class AskActivity extends ActionBarActivity {

	private ImageView verifyImage;
	private static final String TAG_ASK_ACTIVITY = "AskActivity";
	private IAskManager askManager;
	private boolean isLocked=false;
	private Object LOCK=new Object();
	public static final int NOTIFICATION_ID=1;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG_ASK_ACTIVITY, "onResume");
		NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIFICATION_ID);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG_ASK_ACTIVITY, "onPause");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG_ASK_ACTIVITY, "onDestroy");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_ask);
		Log.d(TAG_ASK_ACTIVITY, "onCreate");
		askManager=new RandomAsk(this);

		final TextView keyWordTextView = (TextView)findViewById(R.id.textViewKeyWord);
		String word = getNextWordOrFinishActivity();
		keyWordTextView.setText(word);
		final EditText editTextMeaning = (EditText)findViewById(R.id.editTextMeaning);
		editTextMeaning.setOnEditorActionListener(
				new OnEditorActionListener(){
					@Override public boolean onEditorAction(    TextView v,    int actionId,    KeyEvent event){
						Log.d(TAG_ASK_ACTIVITY, "action id:"+actionId);
						if (actionId==KeyEvent.KEYCODE_ENTER || actionId==6) {
							if(askManager.isMeaningInclude(editTextMeaning.getText().toString())){
								Log.d(TAG_ASK_ACTIVITY, "meaning true!");
								showSuccessDialog(800);
							}else{
								Log.d(TAG_ASK_ACTIVITY, "meaning false!");
								if(askManager.getWrongCounter()==3){
									showWordInstantly(askManager.getMeaning(),800);
								}
							}
							Log.d(TAG_ASK_ACTIVITY, "enter pressed");
						}
						return false;
					}
				}
				);

	}

	private void showSuccessDialog(int i) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.correct);
		builder.setCancelable(false);
		builder.setIcon(R.drawable.ic_success);
		
		final AlertDialog dlg = builder.create();
		
		dlg.show();
		Log.d(TAG_ASK_ACTIVITY, "dialog show called");
		final Timer t = new Timer();
		
		t.schedule(new TimerTask() {
			public void run() {
				dlg.dismiss(); // when the task active then close the dialog
				t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
				getNewWordAndSet();
			}
		}, i);

	}

	private void showWordInstantly(String meaning, int i) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(AskActivity.this);
		builder.setTitle(R.string.word);
		builder.setMessage(meaning);
		builder.setCancelable(false);

		final AlertDialog dlg = builder.create();

		dlg.show();
		Log.d(TAG_ASK_ACTIVITY, "dialog show called");
		final Timer t = new Timer();
		t.schedule(new TimerTask() {
			public void run() {
				dlg.dismiss(); // when the task active then close the dialog
				t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
				getNewWordAndSet();
			}
		}, i);

	}


	private String getNextWordOrFinishActivity() {
		Log.d(TAG_ASK_ACTIVITY, "getNextWordOrFinishActivity");
		String word=askManager.getNext();
		if(word.equals("")){
			finish();
		}
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
			}
		});
		
		return word;
	}

	public void skip(View view){

		showWordInstantly(askManager.getMeaning(), 500);
		askManager.increaseWrong();


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ask, menu);
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

	private void getNewWordAndSet() {
		final TextView keyWordTextView = (TextView)findViewById(R.id.textViewKeyWord);
		final EditText editTextMeaning = (EditText)findViewById(R.id.editTextMeaning);
		
		final String word = getNextWordOrFinishActivity();
		
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				keyWordTextView.setText(word);
				editTextMeaning.setText("");
			}
		});
		
	}
}
