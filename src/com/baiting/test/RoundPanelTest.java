package com.baiting.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class RoundPanelTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		JFrame frame = new JFrame();
		frame.setSize(new Dimension(200,150));
		frame.setVisible(true);
		frame.setExtendedState(JFrame.EXIT_ON_CLOSE);
		//frame.set
		JPanel rightTopPanel1 = new JPanel();
		rightTopPanel1.setPreferredSize(new Dimension(100,100));
		RoundRectLabel roundRectLabel_1 = new RoundRectLabel("", 10, 2); 
		roundRectLabel_1.setForeground(Color.white); 
		roundRectLabel_1.setBackground(new Color(91,124,168)); 
		roundRectLabel_1.setPreferredSize(new Dimension(80, 20)); 
		roundRectLabel_1.setText(""); 
		
		TransparentLabel testLabel = new TransparentLabel("测试", 0.5f);
		testLabel.setOpaque(true);
		testLabel.setBackground(Color.BLUE);
		testLabel.setPreferredSize(new Dimension(80, 30));
		rightTopPanel1.add(testLabel);
		//rightTopPanel1.add(roundRectLabel_1, BorderLayout.NORTH); 
		//============= 
		JPanel panel2 = new JPanel();
		panel2.setOpaque(false);
		RoundRectPanel roundRectPanel = new RoundRectPanel(0, 0); 
		roundRectPanel.setBorder(new RoundRectBorder(Color.BLUE,1));
		roundRectPanel.setPreferredSize(new Dimension(100,50));
		roundRectPanel.setBackground(Color.WHITE); 
		panel2.add(roundRectPanel, BorderLayout.CENTER); 
		roundRectPanel.setLayout(new BorderLayout(0, 0)); 
		roundRectPanel.setBorder(new RoundRectBorder(Color.lightGray,30)); 
		roundRectPanel.setOpaque(false);

		
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout(0,0));
	
		
		contentPane.add(rightTopPanel1,BorderLayout.EAST);
		
		//============== 
	}

}
