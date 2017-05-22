package com.baiting.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.baiting.MusicBackgroudPanel;
import com.baiting.MusicFrame;
import com.baiting.MusicWindow;

public class MusicWindowLayout extends MusicWindow {

	private static MusicFrame frame;
	private JPanel panelTop,panelLeft,panelRight,panelButtom;
	private ShowMainPanel showLRCPanel;

   boolean isDragged = false;
   private MusicBackgroudPanel backgroudPanel;
	
	public MusicWindowLayout() {
		super();
	}
	
	public MusicWindowLayout(String title,int width,int height) {
		super(title, width, height);
	}
	
	public MusicWindowLayout(String title,int width,int height,int locationX,int locationY) {
		super(title, width, height, locationX, locationY);
	}
	
	public static MusicFrame getFrame() {
		return frame;
	}

	public static void setFrame(MusicFrame frame) {
		MusicWindowLayout.frame = frame;
	}
	
	
	public void show() {
		frame = createWindow();

		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout(0,0));
		
		backgroudPanel = new MusicBackgroudPanel();
		backgroudPanel.setLayout(new BorderLayout(0,0));
		backgroudPanel.setOpaque(false);
		//contentPane.setFont(Fonts.songTi16());
		
		panelTop = new JPanel();
		
		panelLeft = new JPanel();
		panelLeft.setBackground(Color.WHITE);
		panelLeft.setOpaque(false);
		
		panelRight = new JPanel();
		panelRight.setOpaque(false);
		
		panelButtom = new JPanel();
		initPanel();
		backgroudPanel.add(panelTop,BorderLayout.NORTH);
		backgroudPanel.add(panelLeft,BorderLayout.CENTER);
		backgroudPanel.add(panelRight,BorderLayout.EAST);
		backgroudPanel.add(panelButtom,BorderLayout.SOUTH);
		//setDragable();
		contentPane.add(backgroudPanel,BorderLayout.CENTER);
	}
	
	private void initPanel() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				initPanelTop();
				initPanelLeft();
				initPanelRight();
				initPanelButtom();
				ShowMsgPanel msgPanel = ShowMsgPanel.getInstance();
				NetSongPanel.getInstance().create().add(msgPanel,BorderLayout.CENTER);
			}
		});
	}
	
	
	private void initPanelTop() {
		panelTop.setPreferredSize(new Dimension(getWidth(), MUSIC_WINDOW_TOP_HEIGHT));
		panelTop.setOpaque(false);
		panelTop.setLayout(new BorderLayout(0,0));
		TabMenuLayout tabMenuLayout = new TabMenuLayout();
		panelTop.add(tabMenuLayout.create(),BorderLayout.CENTER);
		//tabMenuLayout = null;
	}

	private void initPanelLeft() {
		//panelLeft.setBackground(Color.WHITE);
		ShowLyricPanel lrcPanel =  ShowLyricPanel.getInstance();
		showLRCPanel = ShowMainPanel.getInstance(lrcPanel.create());
		//lrcPanel = null;
		panelLeft.setLayout(new BorderLayout(0,0));
		panelLeft.add(showLRCPanel.create(),BorderLayout.CENTER);
	}
	
	private void initPanelRight() {
		//panelRight.setBackground(Color.WHITE);
		MusicListLayout musicListLayout = MusicListLayout.getInstance();
		panelRight.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.decode(getConfigMap().get("panel.right.border.color").toString())));
		panelRight.setLayout(new BorderLayout(0,0));
		panelRight.add(musicListLayout.create(),BorderLayout.NORTH);
		//musicListLayout = null;
	}
	
	
	
	private void initPanelButtom() {
		panelButtom.setPreferredSize(new Dimension(getWidth()-10, MUSIC_WINDOW_BOTTOM_HEIGHT));
		panelButtom.setOpaque(false);
		panelButtom.add(MusicPlayControllerLayout.getInstance().create());
	}

	
}