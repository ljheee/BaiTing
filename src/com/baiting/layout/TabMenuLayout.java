package com.baiting.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.baiting.Music;
import com.baiting.listener.MusicMouseListener;
import com.baiting.util.CommonUtil;

public class TabMenuLayout extends Music {

	private JPanel panel,leftLogoPanel,rightPanel;
	private JLabel minLabel,maxLabel,closeLabel;
	
	public TabMenuLayout() {
		panel = new JPanel();
		BorderLayout borderLayout = new BorderLayout(0,0);
		panel.setLayout(borderLayout);
		panel.setOpaque(false);
		
		leftLogoPanel = new JPanel();
		leftLogoPanel.setOpaque(false);
		leftLogoPanel.setLayout(new BorderLayout(0,0));
		
		rightPanel = new JPanel();
		rightPanel.setOpaque(false);
		rightPanel.setLayout(new BorderLayout(0,0));
		
		panel.add(leftLogoPanel,BorderLayout.WEST);
		panel.add(rightPanel,BorderLayout.CENTER);
		
		
	}
	
	public JPanel create() {
		JPanel panelTopW = new JPanel();
		panelTopW.setPreferredSize(new Dimension(getWidth(), 20));
		panelTopW.setOpaque(false);
		panelTopW.setLayout(new BorderLayout(0,0));
		
		//窗口最大最小化
		JPanel winButtomPanel = new JPanel();
		winButtomPanel.setPreferredSize(new Dimension(50,15));
		winButtomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,2,1));
		winButtomPanel.setOpaque(true);
		winButtomPanel.setBackground(CommonUtil.getColor(getConfigMap().get("maxmin.background.color").toString()));
		
		ImageIcon minIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("min.icon").toString());
		minLabel = new JLabel(minIcon);
		minLabel.setToolTipText(getConfigMap().get("min.label.title").toString());
		minLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
		minLabel.addMouseListener(new MusicMouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					MusicWindowLayout.getFrame().windowMin();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				minLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			
		});
		
		ImageIcon maxIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("max.icon").toString());
		maxLabel = new JLabel(maxIcon);
		maxLabel.setToolTipText(getConfigMap().get("max.label.title").toString());
		maxLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
		maxLabel.addMouseListener(new MusicMouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					if(MusicWindowLayout.getFrame().getExtendedState() != JFrame.MAXIMIZED_BOTH) {
						ImageIcon maxIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("resize.icon").toString());
						maxLabel.setIcon(maxIcon);
						MusicWindowLayout.getFrame().windowMax();
					} else {
						ImageIcon maxIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("max.icon").toString());
						maxLabel.setIcon(maxIcon);
						MusicWindowLayout.getFrame().windowNormal();
					}
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				maxLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});
		
		
		ImageIcon closeIcon = new ImageIcon(getIconPath()+getSeparator()+getConfigMap().get("close.icon").toString());
		closeLabel = new JLabel(closeIcon);
		closeIcon = null;
		closeLabel.setToolTipText(getConfigMap().get("close.label.title").toString());
		closeLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
		closeLabel.addMouseListener(new MusicMouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					System.exit(0);
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				closeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});
		
		winButtomPanel.add(minLabel);
		//winButtomPanel.add(maxLabel);
		winButtomPanel.add(closeLabel);
		panelTopW.add(winButtomPanel,BorderLayout.EAST);
		
		LogoPanel panelLogo = new LogoPanel();
		panelLogo.setPreferredSize(new Dimension(170, getHeight()-40));
		panelLogo.setOpaque(false);
		
		JPanel panelSkin = new JPanel();
		panelSkin.setPreferredSize(new Dimension(100, getHeight()-40));
		panelSkin.setOpaque(false);
		
		JPanel panelTopTab = new JPanel();
		FlowLayout flowLayout = new FlowLayout();
		panelTopTab.setLayout(flowLayout);
		panelTopTab.setOpaque(false);
		
		JPanel lrcPanel =  ShowLyricPanel.getInstance().create();
		TabMenuLabel lrcLabel = new TabMenuLabel("lrcLabel", getConfigMap().get("tab.lrc.label").toString(), lrcPanel);
		panelTopTab.add(lrcLabel.create());
		//lrcPanel = null;
		
		JPanel netSearchPanel =  NetSearchPanel.getInstance().create();
		TabMenuLabel netSearchLabel = new TabMenuLabel("netSearchLabel", getConfigMap().get("tab.net.search.label").toString(), netSearchPanel);
		panelTopTab.add(netSearchLabel.create());
		//netSearchPanel = null;
		
		JPanel downloadPanel =  DownloadPanel.getInstance().create();
		TabMenuLabel downloadLabel = new TabMenuLabel("downloadLabel", getConfigMap().get("tab.download.label").toString(), downloadPanel);
		panelTopTab.add(downloadLabel.create());
		//downloadPanel = null;
		
		leftLogoPanel.add(panelLogo,BorderLayout.CENTER);
		
		rightPanel.add(panelSkin,BorderLayout.EAST);
		rightPanel.add(panelTopW,BorderLayout.NORTH);
		rightPanel.add(panelTopTab, BorderLayout.CENTER);
		//panelSkin = null;
		//panelTopW = null;
		//panelTopTab = null;
		
		return panel;
	}
}
