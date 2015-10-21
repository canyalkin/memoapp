package canyalkin.net.memoapp.ask.manager;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.xml.transform.Templates;

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
		List<WordDictionary> list = dao.readAll();
		if(list.size()<16){
			wordList=list;
		}else{
			List<WordDictionary> tempList=new LinkedList<WordDictionary>();
			Random random = new Random();
			HashSet<Integer> indexSet=new  HashSet<Integer>();
			int firstPart=list.size()/2;
			
			while(tempList.size()<8){
				int index = random.nextInt(firstPart);
				
				while(indexSet.contains(index)){
					index++;
					index=index%(firstPart);
				}
				tempList.add(list.get(index));
				indexSet.add(index);
				
			}
			
			indexSet.clear();
			int bound=0;
			if(list.size()%2==0){
				bound=list.size()/2;
			}else{
				bound=list.size()/2+1;
			}
			while(tempList.size()<16){
				int index=random.nextInt(bound);
				while(indexSet.contains(index)){
					index++;
					index=index%(bound);
				}
				tempList.add(list.get(index+firstPart));
				indexSet.add(index);
				
			}
			wordList=tempList;
		}
		
		//shuffleList(wordList);
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
			List<String> meaningList = current.getMeaning();
			for (String string : meaningList) {
				if( Math.ceil(string.length()/2.0) <= s.length() && string.contains(s)){
					wrongCounter=0;
					return true;
				}
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
			StringBuilder sb=new StringBuilder();
			List<String> meaningList = current.getMeaning();
			for (int i=0;i<meaningList.size();i++) {
				if(i<meaningList.size()-1){
					sb.append(meaningList.get(i)+", ");
				}else{
					sb.append(meaningList.get(i));
				}
			}
			return sb.toString();
		}
		return "";
	}

	@Override
	public void increaseWrong() {
		wrongCounter++;
		
	}


}
