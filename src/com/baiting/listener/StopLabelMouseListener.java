package com.baiting.listener;

import java.awt.Cursor;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import com.baiting.layout.MusicPlayControllerLayout;
import com.baiting.service.MusicPlayerService;

public class StopLabelMouseListener extends MusicMouseListener {

	private JLabel stopLabel;
	private MusicPlayControllerLayout mpcl;
	
	public StopLabelMouseListener(JLabel stopLabel,MusicPlayControllerLayout mpcl) {
		this.stopLabel = stopLabel;
		this.mpcl= mpcl;
	}
	@Override
	public void mouseExited(MouseEvent e) {
		mpcl.setStopToStopIcon();
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		mpcl.setStopToStopMouseEnteredIcon();
		stopLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() ==  MouseEvent.BUTTON1) {
			stopLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			mpcl.setStopToStopIcon();
			stopLabel.setEnabled(false);
			MusicPlayerService.getInstance().stop();
			mpcl.setPlayButtonToPlayIcon();
		}
	}
}
