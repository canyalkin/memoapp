package canyalkin.net.memoapp;

import java.util.LinkedList;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import canyalkin.net.memoapp.R.layout;
import canyalkin.net.memoapp.db.WordDictionary;
import canyalkin.net.memoapp.db.WordDictionaryDAO;

public class PopupAddActivity extends ActionBarActivity implements OnClickListener{
	
	private static final String POPUP = "PopupAddActivity";
	EditText editWord,editMeaning;
	TextView textWordtToRemember, textMeaning;
	
	Button okButton,cancelButton;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popup_add);
		Log.d(POPUP, "onCreate called!");
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.LinearLayout1);
		
		
		textWordtToRemember=new TextView(this);
		textWordtToRemember.setText(getString(R.string.enter_word));
		textWordtToRemember.setTextColor(Color.parseColor("#000000"));
		textWordtToRemember.setVisibility(View.VISIBLE);
		linearLayout.addView(textWordtToRemember);
		
		editWord=new EditText(this);
		//RelativeLayout.LayoutParams mRparams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		//editWord.setLayoutParams(mRparams);
		editWord.setVisibility(View.VISIBLE);
		editWord.setTextColor(Color.parseColor("#000000"));
		linearLayout.addView(editWord);
		
		
		textMeaning=new TextView(this);
		textMeaning.setText(getString(R.string.enter_meaning));
		textMeaning.setVisibility(View.VISIBLE);
		textMeaning.setPadding(0, 10, 0,0);
		textMeaning.setTextColor(Color.parseColor("#000000"));
		linearLayout.addView(textMeaning);
		
		editMeaning=new  EditText(this);
		editMeaning.setVisibility(View.VISIBLE);
		editMeaning.setTextColor(Color.parseColor("#000000"));
		linearLayout.addView(editMeaning);
		
		LinearLayout okCancelLayout=new LinearLayout(this);
		okButton=new Button(this);
		okButton.setId(1);
		okButton.setText("OK");
		okButton.setVisibility(View.VISIBLE);
		okButton.setOnClickListener(this);
		okCancelLayout.addView(okButton);
		
		cancelButton=new Button(this);
		cancelButton.setId(2);
		cancelButton.setText("Cancel");
		cancelButton.setVisibility(View.VISIBLE);
		cancelButton.setOnClickListener(this);
		okCancelLayout.addView(cancelButton);
		okCancelLayout.setVisibility(View.VISIBLE);
		linearLayout.addView(okCancelLayout);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.popup_add, menu);
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

	@Override
	public void onClick(View v) {
		
		Button b=(Button)v;
		if(b.getId()==1){
			//OK button clicked
			String keyWord = editWord.getText().toString().trim();
			String meaning =editMeaning.getText().toString().trim();
			if(keyWord.isEmpty()){
				Log.d(POPUP, "keyword empty");
				AppUtil.showAlertMessage(this,"empty keyword");
				return;
			}
			if(meaning.isEmpty()){
				Log.d(POPUP, "meaning empty");
				AppUtil.showAlertMessage(this,"empty meaning");
				return;
			}
			WordDictionaryDAO dao=new WordDictionaryDAO(this);
			WordDictionary wd=new WordDictionary();
			wd.setKeyword(keyWord);
			LinkedList<String> newWordList = new LinkedList<String>();
			newWordList.add(meaning);
			wd.setMeaning(newWordList);
			boolean isInserted = dao.insert(wd);
			Log.d(POPUP, "ok clicked isInserted:"+isInserted);
			if(isInserted){
				finish();
			}
		}else if(b.getId()==2){
			//Cancel button clicked
			Log.d(POPUP, "cancel clicked");
			finish();
		}
	}
	
	

	@Override
	protected void onStop() {
		Log.d("PopupAddActivity", "stopped!");
		super.onStop();
		
	}
}
