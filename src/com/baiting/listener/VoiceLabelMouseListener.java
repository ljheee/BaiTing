package com.baiting.listener;

import java.awt.Cursor;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JSlider;

import com.baiting.layout.MusicPlayControllerLayout;
import com.baiting.service.MusicPlayerService;

public class VoiceLabelMouseListener extends MusicMouseListener {

	private JLabel voiceLabel;
	private JSlider volumeSlider;
	private MusicPlayControllerLayout mpcl;
	
	public VoiceLabelMouseListener(JLabel voiceLabel,JSlider volumeSlider,MusicPlayControllerLayout mpcl) {
		this.voiceLabel = voiceLabel;
		this.volumeSlider = volumeSlider;
		this.mpcl= mpcl;
	}
	

	@Override
	public void mouseExited(MouseEvent e) {
		if(MusicPlayControllerLayout.getClickMute() == 0) {
			mpcl.setVolumeToSoundIcon();
		} else {
			mpcl.setVolumeToMuteIcon();
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		voiceLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		if(MusicPlayControllerLayout.getClickMute() == 0) {
			mpcl.setVolumeToSoundIcon();
		} else{
			mpcl.setVolumeToClickMuteIcon();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() ==  MouseEvent.BUTTON1) {
			voiceLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			if(MusicPlayControllerLayout.getClickMute() == 0) {
				mpcl.setVolumeToMuteIcon();
				MusicPlayControllerLayout.setClickMute(1);
				volumeSlider.setEnabled(false);
				MusicPlayerService.getInstance().setVolume(0,MUSIC_MUTE);
			} else if(MusicPlayControllerLayout.getClickMute() == 1) {
				mpcl.setVolumeToSoundIcon();
				MusicPlayControllerLayout.setClickMute(0);
				volumeSlider.setEnabled(true);
				int value = volumeSlider.getValue();
				MusicPlayerService.getInstance().setVolume(value,MUSIC_NOT_MUTE);
			}
		}
	}
}
