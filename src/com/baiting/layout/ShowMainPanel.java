package com.baiting.layout;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class ShowMainPanel {

	
	private static ShowMainPanel instance;
	private static JPanel showMainPanel;
	
	private ShowMainPanel(JPanel panel) {
		init(panel);
	}
	
	private void init(JPanel panel) {
		showMainPanel = new JPanel();
		showMainPanel.setOpaque(false);
		showMainPanel.setLayout(new BorderLayout(0,0));
		showMainPanel.add(panel,BorderLayout.CENTER);
	}
	
	public static ShowMainPanel getInstance(JPanel panel) {
		if(instance == null) {
			instance = new ShowMainPanel(panel);
		}
		return instance;
	}
	
	public static void setPanel(JPanel panel) {
		if(instance != null) {
			instance.create().add(panel);
		} else {
			System.out.println("对象为空.......");
		}
	}
	
   public JPanel create() {
		return showMainPanel;
	}
   
   public static JPanel getPanel() {
	   return showMainPanel;
   }

}
