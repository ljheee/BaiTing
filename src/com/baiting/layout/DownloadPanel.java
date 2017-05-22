package com.baiting.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.baiting.Music;
import com.baiting.font.Fonts;
import com.baiting.listener.MusicMouseListener;
import com.baiting.util.CommonUtil;

/**
 * 下载模块布局
 * @author lmq
 * @date 2012-03-14 22:04
 */
public class DownloadPanel extends Music{
	private JPanel panel,downLeftPanel,downRightPanel;
	private static DownloadPanel instance;
	private JLabel downingLabel,downedLabel,downFailLabel;
	private int downingClick,downedClick,downFailClick;
	
	private DownloadPanel() {
		init();
	}
	
	private void init() {
		if(panel == null) {
			panel = new JPanel();
			panel.setLayout(new BorderLayout(0,0));
			panel.setOpaque(false);
			
			downLeftPanel = new JPanel();
			downLeftPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.decode(getConfigMap().get("down.left.panel.border.color").toString())));
			downLeftPanel.setOpaque(false);
			
			downRightPanel = new JPanel();
			downRightPanel.setLayout(new BorderLayout(0,0));
			//downRightPanel.setOpaque(false);
			
			panel.add(downLeftPanel,BorderLayout.WEST);
			panel.add(downRightPanel,BorderLayout.CENTER);
		}
	}
	
	public synchronized static DownloadPanel getInstance() {
		if(instance == null){
			instance = new DownloadPanel();
		}
		return instance;
	}
	
	public JPanel create() {
		setDownLeftPanelLayout();
		return panel;
	}
	
	private void setDownLeftPanelLayout() {
		int width = 80;
		int height = 100;
		
		JPanel downTitlePanel = new JPanel();
		downTitlePanel.setOpaque(true);
		downTitlePanel.setBackground(CommonUtil.getColor(getConfigMap().get("down.panel.title.background.color").toString()));
		
		JLabel downTitleLabel = new JLabel();
		downTitleLabel.setFont(Fonts.songTiB13());
		downTitleLabel.setText(getConfigMap().get("down.info.label").toString());
		downTitleLabel.setPreferredSize(new Dimension(width, 15));
		downTitleLabel.setOpaque(false);
		downTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		downTitlePanel.add(downTitleLabel);
		
		JPanel nameTabPanel = new JPanel();
		nameTabPanel.setOpaque(false);
		GridLayout gridLayout = new GridLayout(15,1,0,0);
		nameTabPanel.setLayout(gridLayout);
		
		downingLabel = new JLabel();
		downingLabel.setText(getConfigMap().get("downing.info.label").toString());
		downingLabel.setOpaque(true);
		downingLabel.setFont(Fonts.songTiB13());
		downingLabel.setPreferredSize(new Dimension(width,height));
		downingLabel.setHorizontalAlignment(SwingConstants.CENTER);
		downingClick = 1;
		downingLabel.setBackground(CommonUtil.getColor(getConfigMap().get("downing.label.background.color").toString()));
		downingLabel.setForeground(CommonUtil.getColor(getConfigMap().get("downing.label.foreground.color").toString()));
		downingLabel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.decode(getConfigMap().get("downing.label.border.color").toString())));
		downingTable();
		
		downedLabel = new JLabel();
		downedLabel.setText(getConfigMap().get("downed.info.label").toString());
		downedLabel.setOpaque(true);
		downedLabel.setFont(Fonts.songTi13());
		downedLabel.setPreferredSize(new Dimension(width,height));
		downedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		downedLabel.setBackground(CommonUtil.getColor(getConfigMap().get("downed.label.background.color").toString()));
		downedLabel.setForeground(CommonUtil.getColor(getConfigMap().get("downed.label.foreground.color").toString()));
		downedLabel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, CommonUtil.getColor(getConfigMap().get("downed.label.border.color").toString())));
		
		downFailLabel = new JLabel();
		downFailLabel.setText(getConfigMap().get("down.fail.info.label").toString());
		downFailLabel.setOpaque(true);
		downFailLabel.setFont(Fonts.songTi13());
		downFailLabel.setPreferredSize(new Dimension(width,height));
		downFailLabel.setHorizontalAlignment(SwingConstants.CENTER);
		downFailLabel.setBackground(CommonUtil.getColor(getConfigMap().get("down.fail.label.background.color").toString()));
		downFailLabel.setForeground(CommonUtil.getColor(getConfigMap().get("down.fail.label.foreground.color").toString()));
		downFailLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, CommonUtil.getColor(getConfigMap().get("down.fail.label.border.color").toString())));
		
		
		nameTabPanel.setLayout(gridLayout);
		nameTabPanel.add(downingLabel);
		nameTabPanel.add(downedLabel);
		nameTabPanel.add(downFailLabel);
		
		downLeftPanel.setLayout(new BorderLayout(0,0));
		downLeftPanel.add(downTitlePanel,BorderLayout.NORTH);
		downLeftPanel.add(nameTabPanel,BorderLayout.CENTER);
		
		//鼠标监听
		downingLabel.addMouseListener(new MusicMouseListener() {
			@Override
			public void mouseExited(MouseEvent e) {
				if(downingClick == 0) {
					downingLabel.setBackground(CommonUtil.getColor(getConfigMap().get("downing.label.mouseExited.background.color").toString()));
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				if(downingClick == 0) {
					downingLabel.setBackground(CommonUtil.getColor(getConfigMap().get("downing.label.mouseEntered.background.color").toString()));
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					downingClick = 1;
					downingLabel.setBackground(CommonUtil.getColor(getConfigMap().get("downing.label.background.color").toString()));
					downingLabel.setFont(Fonts.songTiB13());
					
					downedClick = 0;
					downedLabel.setBackground(CommonUtil.getColor(getConfigMap().get("downed.label.background.color").toString()));
					downedLabel.setFont(Fonts.songTi13());
						
					downFailClick = 0;
					downFailLabel.setBackground(CommonUtil.getColor(getConfigMap().get("down.fail.label.background.color").toString()));
					downFailLabel.setFont(Fonts.songTi13());
					if(downloadType != 1) {
					  downloadType = 1;
					  downingTable();
					}
				}
			}
		});
		
		downedLabel.addMouseListener(new MusicMouseListener() {
			@Override
			public void mouseExited(MouseEvent e) {
				if(downedClick == 0) {
					downedLabel.setBackground(CommonUtil.getColor(getConfigMap().get("downed.label.background.color").toString()));
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				if(downedClick == 0) {
					downedLabel.setBackground(CommonUtil.getColor(getConfigMap().get("downing.label.mouseEntered.background.color").toString()));
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					downedClick = 1;
					downedLabel.setBackground(CommonUtil.getColor(getConfigMap().get("downing.label.background.color").toString()));
					downedLabel.setFont(Fonts.songTiB13());
						
					downingClick = 0;
					downingLabel.setBackground(CommonUtil.getColor(getConfigMap().get("downed.label.background.color").toString()));
					downingLabel.setFont(Fonts.songTi13());
						
					downFailClick = 0;
					downFailLabel.setBackground(CommonUtil.getColor(getConfigMap().get("downed.label.background.color").toString()));
					downFailLabel.setFont(Fonts.songTi13());
					if(downloadType != 2) {
						downloadType = 2;
						JScrollPane table = DownedLayout.getInstance().create();
						downRightPanel.removeAll();
						downRightPanel.repaint();
						downRightPanel.add(table,BorderLayout.CENTER);
						downRightPanel.updateUI();
					}
				}
			}
		});
		
		downFailLabel.addMouseListener(new MusicMouseListener() {
			@Override
			public void mouseExited(MouseEvent e) {
				if(downFailClick == 0) {
					downFailLabel.setBackground(CommonUtil.getColor(getConfigMap().get("downed.label.background.color").toString()));
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				if(downFailClick == 0) {
					downFailLabel.setBackground(CommonUtil.getColor(getConfigMap().get("downing.label.mouseEntered.background.color").toString()));
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					downFailClick = 1;
					downFailLabel.setBackground(CommonUtil.getColor(getConfigMap().get("downing.label.background.color").toString()));
					downFailLabel.setFont(Fonts.songTiB13());
						
					downingClick = 0;
					downingLabel.setBackground(CommonUtil.getColor(getConfigMap().get("downed.label.background.color").toString()));
					downingLabel.setFont(Fonts.songTi13());
						
					downedClick = 0;
					downedLabel.setBackground(CommonUtil.getColor(getConfigMap().get("downed.label.background.color").toString()));
					downedLabel.setFont(Fonts.songTi13());
					
					if(downloadType != 3) {
						downloadType = 3;
						JScrollPane table = DownFailLayout.getInstance().create();
						downRightPanel.removeAll();
						downRightPanel.repaint();
						downRightPanel.add(table,BorderLayout.CENTER);
						downRightPanel.updateUI();
					}
				}
			}
		});
	}
	
	private void downingTable() {
		JScrollPane table = DowningLayout.getInstance().create();
		downRightPanel.removeAll();
		downRightPanel.repaint();
		downRightPanel.add(table,BorderLayout.CENTER);
		downRightPanel.updateUI();
		
	}

	
	public void reloadDowning() {
		downingTable();
	}
}
