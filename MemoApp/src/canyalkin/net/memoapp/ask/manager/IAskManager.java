package canyalkin.net.memoapp.ask.manager;

public interface IAskManager {
	
	void init();
	String getNext();
	String getMeaning();
	boolean isMeaningInclude(String s);
	public int getWrongCounter();
	public void increaseWrong();

}
