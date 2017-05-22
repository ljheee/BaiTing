package com.baiting.listener;

import java.awt.Cursor;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import com.baiting.bean.Song;
import com.baiting.layout.MusicListLayout;
import com.baiting.layout.MusicPlayControllerLayout;
import com.baiting.service.MusicPlayerService;
import com.baiting.service.SongListService;

public class PreviousLabelMouseListener extends MusicMouseListener {

	private JLabel preLabel;
	private MusicPlayControllerLayout mpcl;

	public PreviousLabelMouseListener(JLabel preLabel,MusicPlayControllerLayout mpcl) {
		this.preLabel = preLabel;
		this.mpcl = mpcl;
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		mpcl.setPreviousToPreIcon();
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		mpcl.setPreviousToPreMouseEnteredIcon();
		preLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() ==  MouseEvent.BUTTON1) {
			preLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			SongListService listService = new SongListService();
			Song songTmp = null;
			MusicPlayerService player = MusicPlayerService.getInstance();
			Song song = player.getSong();
			if(song != null) {
				songTmp = listService.getSong(song.getPlayListName(), song.getNo()-1);
			}
			if(songTmp != null) {
				mpcl.setPlayButtonToPlayIcon();
				MusicPlayerService.getInstance().stop();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				mpcl.setPlayButtonToPlayMouseEnteredIcon();
				MusicListLayout.getInstance().getSongList().setSelectedIndex(songTmp.getNo()-1);
				MusicListLayout.getInstance().getSongList().setSelectionBackground(getSongListSelectionBackground());
				MusicListLayout.getInstance().getSongList().setSelectionForeground(getSongListSelectionForeground());
				
				MusicPlayerService.getInstance().play(songTmp);
			}
		}
	}
	
}
