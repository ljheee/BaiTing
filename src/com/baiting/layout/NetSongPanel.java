package com.baiting.layout;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class NetSongPanel {
	
	private JPanel panel;
	private static NetSongPanel instance;
	
	private NetSongPanel() {
		panel = new JPanel();
		//panel.setOpaque(true);
		panel.setLayout(new BorderLayout(0, 0));
	}
	
	public static NetSongPanel getInstance() {
		if(null == instance) {
			instance = new NetSongPanel();
		}
		return instance;
	}
	
	public void setPanel(JPanel panel) {
		this.panel.add(panel);
	}
	
	public JPanel create() {
		return panel;
	}

}
