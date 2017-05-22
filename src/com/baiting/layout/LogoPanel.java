package com.baiting.layout;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.baiting.IMusic;
import com.baiting.Music;

public class LogoPanel extends JPanel implements IMusic {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LogoPanel() {
		super();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		ImageIcon imageIcon = new ImageIcon(Music.getIconPath()+Music.getSeparator()+Music.getConfigMap().get("logo.header.icon").toString());
		g.drawImage(imageIcon.getImage(), 5, 5, null);
	}
	
}
