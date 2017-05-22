package com.baiting.bean;

public class LyricStatement implements IBaseBean {

	private static final long serialVersionUID = 7255002110457379806L;
	
	private int index;
	private long time;
	private String statement;
	
	public long getTime() {
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public String getStatement() {
		return statement;
	}
	
	public void setStatement(String statement) {
		this.statement = statement;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	
}
