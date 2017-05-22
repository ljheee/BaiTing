package com.baiting.test;

public class ThreadTest01 implements Runnable {

	private int state = 0;
	
	private static ThreadTest01 instance;
	
	private ThreadTest01() {
		
	}
	
	public synchronized static ThreadTest01 getInstance() {
		if(instance == null) {
			instance = new ThreadTest01();
		}
		return instance;
	}
		
	@Override
	public synchronized void run() {
		while(true) {
			try {
				if(state == 1) {
					this.notify();
				}
				if(state == 0){
					System.out.println("正在等待...");
					this.wait();
				}
				System.out.println("呵呵~~");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
