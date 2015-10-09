package canyalkin.net.memoapp.ask.manager;

import java.util.List;
import java.util.Random;

import android.content.Context;
import canyalkin.net.memoapp.db.WordDictionary;
import canyalkin.net.memoapp.db.WordDictionaryDAO;

public class RandomAsk implements IAskManager {
	
	private Context context;
	private List<WordDictionary> wordList;
	private WordDictionary current=null;
	
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
		if(current!=null){
			if( Math.ceil(current.getMeaning().length()/2.0) <= s.length() && current.getMeaning().contains(s)){
				return true;
			}
		}
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


}
