package canyalkin.net.memoapp.ask.manager;

import java.util.List;

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
		List<WordDictionary> tempWordList = dao.readAll();
		wordList=tempWordList;
	}

	@Override
	public String getNext() {
		if(wordList.size()!=0){
			current=wordList.get(0);
			wordList.remove(0);
			return current.getKeyword();
		}
		current=null;
		return null;
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

}
