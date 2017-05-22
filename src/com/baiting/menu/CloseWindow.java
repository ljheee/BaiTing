package com.baiting.menu;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CloseWindow extends WindowAdapter {

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		super.windowClosed(e);
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}

	
}
