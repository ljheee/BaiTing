package com.baiting.listener;

import java.awt.event.MouseEvent;

import javax.swing.JSlider;

import com.baiting.bean.Song;
import com.baiting.service.MusicPlayerService;

public class PlaySliderMouseListener extends MusicMouseListener {

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			MusicPlayerService player = MusicPlayerService.getInstance();
			Song song = player.getSong();
			if(song != null) {
				int value = ((JSlider)e.getSource()).getValue();
				double rate = (double)value/(double)MAX_PLAY_SLIDER;
				MusicPlayerService.getInstance().dragPlaySliber(rate);
			}
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			MusicPlayerService.getInstance().setStatus(DRAGING_PLAY_SLIDER);
		}
	}
}
