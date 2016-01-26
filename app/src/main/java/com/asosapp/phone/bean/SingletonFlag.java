package com.asosapp.phone.bean;


/**
 * 全局标志位 控制是否刷新
 * 
 * @author zhulr
 * 
 */
public class SingletonFlag {
	private Boolean releasing=true;
	private Boolean completed=true;
	private static SingletonFlag singletonFlag;

	private SingletonFlag() {
		
	}
	
	public static SingletonFlag instance(){
		if (singletonFlag == null) {
			singletonFlag = new SingletonFlag();
		}
		return singletonFlag;
	}

	public Boolean getReleasing() {
		return releasing;
	}

	public void setReleasing(Boolean releasing) {
		this.releasing = releasing;
	}

	public Boolean getCompleted() {
		return completed;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}
}
