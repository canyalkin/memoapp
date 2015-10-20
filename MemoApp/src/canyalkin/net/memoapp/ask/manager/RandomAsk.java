package canyalkin.net.memoapp.ask.manager;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.util.Log;
import canyalkin.net.memoapp.db.WordDictionary;
import canyalkin.net.memoapp.db.WordDictionaryDAO;

public class RandomAsk implements IAskManager {
	
	private static final String RANDOM_ASK_ACT = "Random Ask Act.";
	private Context context;
	private List<WordDictionary> wordList;
	private WordDictionary current=null;
	private int wrongCounter=0;
	
	public RandomAsk(Context context) {
		this.context=context;
		init();
	}

	@Override
	public void init() {
		WordDictionaryDAO dao=new WordDictionaryDAO(context);	
		wordList = dao.readAll();
		shuffleList(wordList);
	}

	@Override
	public String getNext() {
		if(current!=null && wrongCounter>0){
			WordDictionaryDAO dao=new WordDictionaryDAO(context);	
			boolean retVal=dao.updateWrongAnsAndNumAsked(current.getKeyword(), current.getNumWrong()+1, current.getNumAsked()+1);
			Log.d(RANDOM_ASK_ACT, "wrongCounter>0 update retval:"+retVal);
			
		}else if(current!=null && wrongCounter==0){
			WordDictionaryDAO dao=new WordDictionaryDAO(context);	
			boolean retVal=dao.updateWrongAnsAndNumAsked(current.getKeyword(), current.getNumWrong(), current.getNumAsked()+1);
			Log.d(RANDOM_ASK_ACT, "wrongCounter=0 update retval:"+retVal);
		}
		
		wrongCounter=0;
		
		if(wordList!=null && wordList.size()!=0){
			current=wordList.get(0);
			wordList.remove(0);
			return current.getKeyword();
		}
		current=null;
		return "";
	}

	@Override
	public boolean isMeaningInclude(String s) {
		if(s!=null && s.length()>0){
			s=s.toLowerCase();
			Log.d(RANDOM_ASK_ACT, "to lower:"+s);
		}
		if(current!=null){
			
			if( Math.ceil(current.getMeaning().length()/2.0) <= s.length() && current.getMeaning().contains(s)){
				wrongCounter=0;
				return true;
			}
		}
		wrongCounter++;
		return false;
	}
	private void shuffleList(List a) {
	    int n = a.size();
	    Random random = new Random();
	    random.nextInt();
	    for (int i = 0; i < n; i++) {
	      int change = i + random.nextInt(n - i);
	      swap(a, i, change);
	    }
	  }

	  private static void swap(List a, int i, int change) {
	    Object helper = a.get(i);
	    a.set(i, a.get(change));
	    a.set(change, helper);
	  }

	  @Override
	  public int getWrongCounter() {
		return wrongCounter;
	  }

	@Override
	public String getMeaning() {
		if(current!=null){
			return current.getMeaning();
		}
		return "";
	}

	@Override
	public void increaseWrong() {
		wrongCounter++;
		
	}


}
