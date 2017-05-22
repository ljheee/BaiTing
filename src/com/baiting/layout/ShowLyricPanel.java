package com.baiting.layout;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.baiting.IMusic;
import com.baiting.Music;

public class ShowLyricPanel extends JPanel implements IMusic {

	private static final long serialVersionUID = -1335194683828267935L;
	
	private static ShowLyricPanel instance;
	
	private ShowLyricPanel() {
		super();
		this.setLayout(new BorderLayout(0, 0));
		setPreferredSize(new Dimension(Music.MUSIC_WINDOW_WIDTH-Music.MUSIC_WINDOW_RIGHT_WIDTH,Music.MUSIC_WINDOW_HEIGHT-2*Music.MUSIC_WINDOW_BOTTOM_HEIGHT));
	}

	public synchronized static ShowLyricPanel getInstance() {
		if(instance == null){
			instance = new ShowLyricPanel();
		}
		return instance;
	}
	
	public ShowLyricPanel create() {
		return instance;
	}
	
	public JPanel getPanel() {
		return instance;
	}

}
