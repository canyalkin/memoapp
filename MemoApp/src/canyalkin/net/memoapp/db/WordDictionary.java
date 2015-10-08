package canyalkin.net.memoapp.db;

import java.util.Date;

public class WordDictionary {
	
	private String keyword;
	private String meaning;
	private Date createDate;
	private int numAsked;
	private int numWrong;
	
	
	public WordDictionary() {
		// TODO Auto-generated constructor stub
	}


	public String getKeyword() {
		return keyword;
	}


	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}


	public String getMeaning() {
		return meaning;
	}


	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public int getNumAsked() {
		return numAsked;
	}


	public void setNumAsked(int numAsked) {
		this.numAsked = numAsked;
	}


	public int getNumWrong() {
		return numWrong;
	}


	public void setNumWrong(int numWrong) {
		this.numWrong = numWrong;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((keyword == null) ? 0 : keyword.hashCode());
		result = prime * result + ((meaning == null) ? 0 : meaning.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WordDictionary other = (WordDictionary) obj;
		if (keyword == null) {
			if (other.keyword != null)
				return false;
		} else if (!keyword.equals(other.keyword))
			return false;
		if (meaning == null) {
			if (other.meaning != null)
				return false;
		} else if (!meaning.equals(other.meaning))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "WordDictionary [keyword=" + keyword + ", meaning=" + meaning + ", createDate=" + createDate
				+ ", numAsked=" + numAsked + ", numWrong=" + numWrong + "]";
	}
	
	
	

}
