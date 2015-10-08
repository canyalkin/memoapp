package canyalkin.net.memoapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import canyalkin.net.memoapp.ask.manager.IAskManager;
import canyalkin.net.memoapp.ask.manager.RandomAsk;

public class AskActivity extends ActionBarActivity {

	private static final String TAG_ASK_ACTIVITY = "AskActivity";
	IAskManager askManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_ask);
		askManager=new RandomAsk(this.getApplicationContext());
		
		final TextView keyWordTextView = (TextView)findViewById(R.id.textViewKeyWord);
		
		keyWordTextView.setText(askManager.getNext());
		final EditText editTextMeaning = (EditText)findViewById(R.id.editTextMeaning);
		editTextMeaning.setOnEditorActionListener(
				new OnEditorActionListener(){
				    @Override public boolean onEditorAction(    TextView v,    int actionId,    KeyEvent event){
				    	Log.d(TAG_ASK_ACTIVITY, "action id:"+actionId);
				    	//Log.d(TAG_ASK_ACTIVITY, "event "+event);
				      if (actionId==KeyEvent.KEYCODE_ENTER || actionId==6) {
				    	  if(askManager.isMeaningInclude(editTextMeaning.getText().toString())){
				    		  Log.d(TAG_ASK_ACTIVITY, "meaning true!");
				    		  keyWordTextView.setText(askManager.getNext());
				    		  editTextMeaning.setText("");
				    	  }
				    	  Log.d(TAG_ASK_ACTIVITY, "enter pressed");
				      }
				      return false;
				    }
				  }
				);
			
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
}
