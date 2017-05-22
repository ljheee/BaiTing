package com.baiting.test;

public class Test05 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ThreadTest01 test = ThreadTest01.getInstance();
		Thread thread = new Thread(test);
		thread.start();
		
		try {
			Thread.sleep(2000);
			test.setState(1);
			thread.notify();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
