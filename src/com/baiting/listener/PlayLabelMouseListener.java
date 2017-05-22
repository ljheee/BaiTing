package com.baiting.listener;

import java.awt.Cursor;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import com.baiting.bean.Song;
import com.baiting.layout.MusicListLayout;
import com.baiting.layout.MusicPlayControllerLayout;
import com.baiting.service.MusicPlayerService;
import com.baiting.service.SongListService;

public class PlayLabelMouseListener extends MusicMouseListener {

	private JLabel playLabel;
	private JLabel stopLabel;
	private MusicPlayControllerLayout mpcl;
	
	public PlayLabelMouseListener(JLabel playLabel,JLabel stopLabel,MusicPlayControllerLayout mpcl) {
		this.playLabel = playLabel;
		this.stopLabel = stopLabel;
		this.mpcl= mpcl;
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		
		playLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		if(MusicPlayControllerLayout.getClickPlay() == 1 ) {
			mpcl.setPlayButtonToPlayIcon();
			//playIcon = null;
			playLabel.setToolTipText("暂停");
			
		} else if(MusicPlayControllerLayout.getClickPlay()==0) {
			mpcl.setPlayButtonToPauseIcon();
		}

	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		playLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		if(MusicPlayControllerLayout.getClickPlay()==1) {
			mpcl.setPlayButtonToPlayMouseEnteredIcon();
			playLabel.setToolTipText("暂停");
			
		} else if(MusicPlayControllerLayout.getClickPlay()==0) {
			mpcl.setPlayButtonToPauseMouseEnteredIcon();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() ==  MouseEvent.BUTTON1) {
			playLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			stopLabel.setEnabled(true);
			MusicPlayerService player = MusicPlayerService.getInstance();
			Song song = player.getSong();
			if(song != null) {
				if(MusicPlayControllerLayout.getClickPlay()==1) {
					mpcl.setPlayButtonToPauseIcon();
					playLabel.setToolTipText("暂停");
					log.info("暂停图标.....");
					if(PAUSE_STATUS == MusicPlayerService.getInstance().getStatus()) {
						MusicPlayerService.getInstance().resumePlay();
					} else {
						MusicPlayerService.getInstance().play(song);
					}
				} else if(MusicPlayControllerLayout.getClickPlay()==0) {
					mpcl.setPlayButtonToPlayIcon();
				    MusicPlayerService.getInstance().pause();
				}
			} else {
				SongListService listService = new SongListService();
				Song songTmp = listService.getSong("", 1);
				listService = null;
				MusicListLayout.getInstance().getSongList().setSelectedIndex(songTmp.getNo()-1);
				MusicListLayout.getInstance().getSongList().setSelectionBackground(getSongListSelectionBackground());
				MusicListLayout.getInstance().getSongList().setSelectionForeground(getSongListSelectionForeground());
				
				
				mpcl.setPlayButtonToPlayIcon();
				playLabel.setToolTipText(getConfigMap().get("pause.label.title").toString());
				MusicPlayerService.getInstance().play(songTmp);
				songTmp = null;
			}
		}
	}
	
}
