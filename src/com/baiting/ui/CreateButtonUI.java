package com.baiting.ui;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.baiting.Music;

public class CreateButtonUI extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5287318157295203002L;

	public CreateButtonUI() {
		super();
	}
	
	public CreateButtonUI(String text) {
		super(text);
	}
	
	public CreateButtonUI(Icon icon) {
		super(icon);
	}
	
	public CreateButtonUI(Action a) {
		super(a);
	}
	
	public CreateButtonUI(String text, Icon icon) {
		super(text,icon);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		ImageIcon imageIcon = new ImageIcon(Music.getIconPath()+Music.getSeparator()+Music.getConfigMap().get("create.button.background").toString());
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(imageIcon.getImage(), 0, 0, null);
		FontMetrics fm = g2d.getFontMetrics();
		g2d.setColor(this.getForeground());
		g2d.setFont(this.getFont());
		int x = (this.getWidth()-fm.stringWidth(this.getText()))/2;
		g2d.drawString(this.getText(), x+2, this.getHeight()/2+5);
		
	}
}
