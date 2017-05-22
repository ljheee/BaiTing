package com.baiting.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.baiting.Music;
import com.baiting.bean.Song;
import com.baiting.font.Fonts;
import com.baiting.listener.NextLabelMouseListener;
import com.baiting.listener.PlayLabelMouseListener;
import com.baiting.listener.PlaySliderMouseListener;
import com.baiting.listener.PreviousLabelMouseListener;
import com.baiting.listener.StopLabelMouseListener;
import com.baiting.listener.VoiceLabelMouseListener;
import com.baiting.service.MusicPlayerService;

public class MusicPlayControllerLayout extends Music {

	private JPanel panel,leftPanel,rightPanel,topRightPanel,bottomRightPanel;
	private JLabel preLabel,nextLabel,playLabel,stopLabel,voiceLabel,songInfoLabel,totalTimeLabel,currentTimeLabel;
	private static MusicPlayControllerLayout instance;
	private static int clickPlay = 1;
	private static int clickMute = 0;
	
	private JSlider volumeSlider,playSlider;
	//private boolean playSliderDragging;//指示当前的进度条是否在拖动中
	
	private MusicPlayControllerLayout() {
		panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BorderLayout(0, 0));
		
		leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(getWidth()/2, getBottomHeight()));
		leftPanel.setOpaque(false);
		
		rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(getWidth()/2, getBottomHeight()));
		rightPanel.setLayout(new BorderLayout(0, 0));
		rightPanel.setOpaque(false);
		
		panel.add(leftPanel,BorderLayout.WEST);
		panel.add(rightPanel,BorderLayout.EAST);
	}
	
	
	public JPanel create() {
		leftPanelLayout();
		rightPanelLayout();
		mouseListener();
		return panel;
	}
	
	public static MusicPlayControllerLayout getInstance() {
		synchronized (MusicPlayControllerLayout.class) {
			if(instance == null) {
				instance = new MusicPlayControllerLayout();
			}
		}
		return instance;
	}
	
	private void leftPanelLayout() {
		ImageIcon preIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("previous.icon").toString());
		preLabel = new JLabel(preIcon);
		//preIcon = null;
		preLabel.setToolTipText(getConfigMap().get("previous.label.title").toString());
		
		ImageIcon playIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("play.icon").toString());
		playLabel = new JLabel(playIcon);
		//playIcon = null;
		playLabel.setToolTipText(getConfigMap().get("play.label.title").toString());
		
		ImageIcon nextIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("next.icon").toString());
		nextLabel = new JLabel(nextIcon);
		//nextIcon = null;
		nextLabel.setToolTipText(getConfigMap().get("next.label.title").toString());
		
		ImageIcon stopIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("stop.icon").toString());
		stopLabel = new JLabel(stopIcon);
		//stopIcon  = null;
		stopLabel.setToolTipText(getConfigMap().get("stop.label.title").toString());
		
		ImageIcon voiceIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("sound.icon").toString());
		voiceLabel = new JLabel(voiceIcon);
		//voiceIcon = null;
		voiceLabel.setToolTipText(getConfigMap().get("sound.label.title").toString());
		
		volumeSlider = new JSlider();
		volumeSlider.setPreferredSize(new Dimension(80, 22));
		volumeSlider.setOpaque(false);
		volumeSlider.setMaximum(MAX_VOLUME_SLIDER);
		volumeSlider.setMinimum(0);
		volumeSlider.setToolTipText(getConfigMap().get("volume.slider.tip.text").toString());
		//volumeSlider.setUI(new VolumeSliderUI());
		volumeSlider.setPaintTicks(true);
		
		leftPanel.add(preLabel);
		leftPanel.add(playLabel);
		leftPanel.add(nextLabel);
		leftPanel.add(stopLabel);
		leftPanel.add(voiceLabel);
		leftPanel.add(volumeSlider);
	}
	
	private void rightPanelLayout() {
		topRightPanel = new JPanel();
		topRightPanel.setLayout(new BorderLayout(0,0));
		playSlider = new JSlider();
		playSlider.setPreferredSize(new Dimension(getWidth()/2-20,23));
		playSlider.setValue(0);
		playSlider.setMaximum(MAX_PLAY_SLIDER);
		playSlider.setMinimum(0);
		playSlider.setOpaque(false);
		playSlider.setToolTipText(getConfigMap().get("play.slider.tip.text").toString());
		//playSlider.setUI(new PlaySliderUI());
		playSlider.setPaintTicks(true);
		
		topRightPanel.add(playSlider,BorderLayout.CENTER);
		topRightPanel.setOpaque(false);
		
		bottomRightPanel = new JPanel();
		bottomRightPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		songInfoLabel = new JLabel();
		songInfoLabel.setFont(Fonts.songTi12());
		totalTimeLabel = new JLabel("00:00");
		totalTimeLabel.setFont(Fonts.songTi12());
		
		currentTimeLabel = new JLabel(" 00:00");
		currentTimeLabel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.RED));
		currentTimeLabel.setFont(Fonts.songTi12());
		
		bottomRightPanel.setOpaque(false);
		
		bottomRightPanel.add(songInfoLabel);
		bottomRightPanel.add(totalTimeLabel);
		bottomRightPanel.add(currentTimeLabel);
		
		rightPanel.add(topRightPanel,BorderLayout.NORTH);
		rightPanel.add(bottomRightPanel,BorderLayout.CENTER);
		
	}
	
	private void mouseListener() {
		 preLabel.addMouseListener(new PreviousLabelMouseListener(preLabel, this));
       nextLabel.addMouseListener(new NextLabelMouseListener(nextLabel, this));
		
		 playLabel.addMouseListener(new PlayLabelMouseListener(playLabel, stopLabel, this));
		 stopLabel.addMouseListener(new StopLabelMouseListener(stopLabel, this));
       voiceLabel.addMouseListener(new VoiceLabelMouseListener(voiceLabel, volumeSlider, this));
	    
       playSlider.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    playSlider.addMouseListener(new PlaySliderMouseListener());
	    
	    volumeSlider.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    volumeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				MusicPlayerService player = MusicPlayerService.getInstance();
				Song song = player.getSong();
				if(song != null) {
					int value = ((JSlider)e.getSource()).getValue();
					MusicPlayerService.getInstance().setVolume((float)value,MUSIC_NOT_MUTE);
				}
			}
		});
	}
	
	

	/**
	 * 播放按钮改为播放图标
	 */
	public void setPlayButtonToPlayIcon() {
		ImageIcon playIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("play.icon").toString());
		playLabel.setIcon(playIcon);
		clickPlay = 1;
	}
	
	/**
	 * 播放按钮改为暂停图标
	 */
	public void setPlayButtonToPauseIcon() {
		ImageIcon playIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("pause.icon").toString());
		playLabel.setIcon(playIcon);
		clickPlay = 0;
	}
	
	/**
	 * 播放状态时鼠标经过
	 */
	public void setPlayButtonToPlayMouseEnteredIcon() {
		ImageIcon playIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("play.mouseEntered.icon").toString());
		playLabel.setIcon(playIcon);
	}
	
	/**
	 * 暂停状态时鼠标经过
	 */
	public void setPlayButtonToPauseMouseEnteredIcon() {
		ImageIcon playIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("pause.mouseEntered.icon").toString());
		playLabel.setIcon(playIcon);
	}
	
	/**
	 * 声音图标
	 */
	public void setVolumeToSoundIcon() {
		ImageIcon voiceIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("sound.icon").toString());
		voiceLabel.setIcon(voiceIcon);
	}
	
	/**
	 * 声音图标变为静音
	 */
	public void setVolumeToMuteIcon() {
		ImageIcon voiceIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("mute.icon").toString());
		voiceLabel.setIcon(voiceIcon);
	}
	
	public void setVolumeToClickMuteIcon() {
		ImageIcon voiceIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("mute.click.icon").toString());
		voiceLabel.setIcon(voiceIcon);
	}
	
	public void setStopToStopIcon() {
		ImageIcon stopIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("stop.icon").toString());
		stopLabel.setIcon(stopIcon);
	}
	
	public void setStopToStopMouseEnteredIcon() {
		ImageIcon stopIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("stop.mouseEntered.icon").toString());
		stopLabel.setIcon(stopIcon);
	}
	
	public void setNextToNextIcon() {
		ImageIcon nextIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("next.icon").toString());
		nextLabel.setIcon(nextIcon);
	}
	
	public void setNextToNextMouseEnteredIcon() {
		ImageIcon nextIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("next.mouseEntered.icon").toString());
		nextLabel.setIcon(nextIcon);
	}
	
	
	public void setPreviousToPreIcon() {
		ImageIcon nextIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("previous.icon").toString());
		preLabel.setIcon(nextIcon);
	}
	
	public void setPreviousToPreMouseEnteredIcon() {
		ImageIcon nextIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("previous.mouseEntered.icon").toString());
		preLabel.setIcon(nextIcon);
	}
	
	
	

	public JLabel getPlayLabel() {
		return playLabel;
	}

	public void setPlayLabel(JLabel playLabel) {
		this.playLabel = playLabel;
	}

	public static int getClickPlay() {
		return clickPlay;
	}

	public static void setClickPlay(int clickPlay) {
		MusicPlayControllerLayout.clickPlay = clickPlay;
	}

	public JLabel getSongInfoLabel() {
		return songInfoLabel;
	}

	public void setSongInfoLabel(JLabel songInfoLabel) {
		this.songInfoLabel = songInfoLabel;
	}

	public JLabel getTotalTimeLabel() {
		return totalTimeLabel;
	}

	public void setTotalTimeLabel(JLabel totalTimeLabel) {
		this.totalTimeLabel = totalTimeLabel;
	}

	public JLabel getCurrentTimeLabel() {
		return currentTimeLabel;
	}

	public JSlider getPlaySlider() {
		return playSlider;
	}


	public static int getClickMute() {
		return clickMute;
	}


	public static void setClickMute(int clickMute) {
		MusicPlayControllerLayout.clickMute = clickMute;
	}
	
}
