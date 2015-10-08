package canyalkin.net.memoapp.ask.manager;

public interface IAskManager {
	
	void init();
	String getNext();
	boolean isMeaningInclude(String s);
	

}
