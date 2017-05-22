package com.baiting.layout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.baiting.Music;
import com.baiting.font.Fonts;
import com.baiting.util.StringUtil;

public class ShowMsgPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5628819526024718970L;
	
	private static ShowMsgPanel instance;
	private String msg;
	protected final Color CURRENT_LINE_COLOR = Color.BLUE;
	protected final Color OTHER_LINE_COLOR = Color.BLACK;
	
	private ShowMsgPanel() {
		super();
		setPreferredSize(new Dimension(Music.MUSIC_WINDOW_WIDTH/2, Music.MUSIC_WINDOW_HEIGHT/2));
		setSize(new Dimension(Music.MUSIC_WINDOW_WIDTH/2, Music.MUSIC_WINDOW_HEIGHT/2));
	}
	
	public synchronized static ShowMsgPanel getInstance() {
		if(null == instance) {
			instance = new ShowMsgPanel();
		}
		return instance;
	}
	
	public void close() {
		instance = null;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
		this.setFont(Fonts.songTi20());
		this.paint(this.getGraphics());
	}
	
	@Override
	public void paint(Graphics g) {
		if (this.isVisible() == false) {
			return;
		}
		Graphics2D g2d = (Graphics2D)g;
		FontMetrics fm = g2d.getFontMetrics();
		g2d.setColor(CURRENT_LINE_COLOR);
		int x = 0;
		if(!StringUtil.isEmpty(msg)) {
			if(null != fm) {
				x = (this.getWidth()-fm.stringWidth(msg))/2;
			}
			g2d.drawString(msg, x, this.getHeight()/2);
		}
		super.paint(g2d);
	}
	
}
