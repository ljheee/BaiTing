package com.baiting.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.baiting.Music;
import com.baiting.bean.NetSong;
import com.baiting.font.Fonts;
import com.baiting.http.netsong.BaiduSearchSongList;
import com.baiting.http.netsong.NetSongList;
import com.baiting.listener.MusicKeyListener;
import com.baiting.listener.MusicMouseListener;
import com.baiting.ui.SearchButtonUI;
import com.baiting.util.CommonUtil;
import com.baiting.util.StringUtil;

public class NetSearchPanel extends Music{
	
	private JPanel panel,searchPanel,hotPanel,netSongListPanel;
	private SearchButtonUI searchButton;
	private JTextField inputNameField;
	private JLabel newSongLabel,hotSongLabel,jdSongLabel,netSongLabel;
	private int clickNewSongLabel,clickHotSongLabel,clickJdSongLabel,clickNetSongLabel;
	private static NetSearchPanel instance;
	private static Thread searchSongListThread;
	private static String INPUT_DEFAULT_VALUE;
	
	private NetSearchPanel() {
		init();
	}
	
	private void init() {
		INPUT_DEFAULT_VALUE = getConfigMap().get("search.text.field.default.value").toString();
		if(panel == null) {
			panel = new JPanel();
			panel.setOpaque(false);
			panel.setLayout(new BorderLayout(0,0));
			
			searchPanel = new JPanel();
			searchPanel.setOpaque(false);
			
			hotPanel = new JPanel();
			hotPanel.setOpaque(false);
			hotPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.decode(getConfigMap().get("net.panel.border.color").toString())));
			
			netSongListPanel = new JPanel();
			netSongListPanel.setLayout(new BorderLayout(0,0));
			netSongListPanel.setOpaque(false);
			
			panel.add(searchPanel,BorderLayout.NORTH);
			panel.add(hotPanel,BorderLayout.WEST);
			panel.add(netSongListPanel,BorderLayout.CENTER);
			
		}
	}
	
	public synchronized static NetSearchPanel getInstance() {
		if(instance == null){
			instance = new NetSearchPanel();
		}
		return instance;
	}
	
	public JPanel create() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setSearchPanelLayout();
				setHotPanelLayout();
				setNetSongListPanelLayout();
			}
		});
		return panel;
	}
	
	public JPanel getPanel() {
		return create();
	}
	
	
	private void setSearchPanelLayout() {
		inputNameField = new JTextField();
		inputNameField.setFont(Fonts.songTi14());
		inputNameField.setPreferredSize(new Dimension(getWidth()/3, 25));
		inputNameField.setText(INPUT_DEFAULT_VALUE);
		inputNameField.setForeground(Color.GRAY);
		
		searchButton = new SearchButtonUI();
		searchButton.setFont(Fonts.songTi14());
		searchButton.setForeground(CommonUtil.getColor(getConfigMap().get("net.search.foreground.color").toString()));
		searchButton.setText(getConfigMap().get("net.search.label").toString());
		searchButton.setPreferredSize(new Dimension(70, 24));
		
		searchPanel.setLayout(new FlowLayout());
		
		searchPanel.add(inputNameField);
		searchPanel.add(searchButton);
		searchPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode(getConfigMap().get("net.panel.border.color").toString())));
		searchButton.addMouseListener(new MusicMouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					searchSong();
				}
			}
		});
		inputNameField.addKeyListener(new MusicKeyListener(){
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == KeyEvent.VK_ENTER) {
					searchSong();
				}
			}
		});
		inputNameField.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				String value = inputNameField.getText();
				if(StringUtil.isEmpty(value)) {
					inputNameField.setText(INPUT_DEFAULT_VALUE);
					inputNameField.setForeground(Color.GRAY);
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				String value = inputNameField.getText();
				if(INPUT_DEFAULT_VALUE.equals(value)) {
					inputNameField.setText("");
					inputNameField.setForeground(Color.BLACK);
				}
			}
		});
	}
	
	private void searchSong() {
		String value = inputNameField.getText();
		if(!StringUtil.isEmpty(value) && !INPUT_DEFAULT_VALUE.equals(value)) {
			String[] cols = value.trim().split(" ");
			NetSong netSong = new NetSong();
			if(cols.length>1) {
				netSong.setSongName(cols[0].trim());
				netSong.setSinger(cols[1].trim());
			} else {
				netSong.setSongName(value.trim());
			}
			BaiduSearchSongList searchSongList = new BaiduSearchSongList(netSong);
			if(null != searchSongListThread) {
				searchSongListThread.interrupt();
			}
			netSongType = 0;
			clickNewSongLabel = 0;
			clickHotSongLabel = 0;
			clickJdSongLabel = 0;
			clickNetSongLabel = 0;
			searchSongListThread = new Thread(searchSongList);
			searchSongListThread.start();
		}
	}
	
	private void setHotPanelLayout() {
		JPanel hotTitlePanel = new JPanel();
		hotTitlePanel.setOpaque(true);
		hotTitlePanel.setBackground(CommonUtil.getColor(getConfigMap().get("net.hot.title.panel.background.color").toString()));
		
		JLabel hotTitleLabel = new JLabel();
		hotTitleLabel.setFont(Fonts.songTiB13());
		hotTitleLabel.setText(getConfigMap().get("net.hot.title.label").toString());
		hotTitleLabel.setPreferredSize(new Dimension(100, 18));
		hotTitleLabel.setOpaque(false);
		hotTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		hotTitlePanel.add(hotTitleLabel);
		//hotTitlePanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.decode("#C5948C")));
		
		JPanel nameTabPanel = new JPanel();
		nameTabPanel.setOpaque(false);
		GridLayout gridLayout = new GridLayout(10,1,0,0);
		nameTabPanel.setLayout(gridLayout);
		
		newSongLabel = new JLabel();
		newSongLabel.setFont(Fonts.songTiB13());
		newSongLabel.setText(getConfigMap().get("new.song.label").toString());
		newSongLabel.setHorizontalAlignment(SwingConstants.CENTER);
		newSongLabel.setOpaque(true);
		newSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("new.song.label.background.color").toString()));
		newSongLabel.setForeground(CommonUtil.getColor(getConfigMap().get("new.song.label.foreground.color").toString()));
		newSongLabel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.decode(getConfigMap().get("new.song.label.border.color").toString())));
		
		clickNewSongLabel = 1;
		
		hotSongLabel = new JLabel();
		hotSongLabel.setFont(Fonts.songTiB13());
		hotSongLabel.setText(getConfigMap().get("hot.song.label").toString());
		hotSongLabel.setHorizontalAlignment(SwingConstants.CENTER);
		hotSongLabel.setOpaque(true);
		hotSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.background.color").toString()));
		hotSongLabel.setForeground(CommonUtil.getColor(getConfigMap().get("hot.song.label.foreground.color").toString()));
		hotSongLabel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, CommonUtil.getColor(getConfigMap().get("new.song.label.border.color").toString())));
		
		jdSongLabel = new JLabel();
		jdSongLabel.setFont(Fonts.songTiB13());
		jdSongLabel.setText(getConfigMap().get("jd.song.label").toString());
		jdSongLabel.setHorizontalAlignment(SwingConstants.CENTER);
		jdSongLabel.setOpaque(true);
		
		jdSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.background.color").toString()));
		jdSongLabel.setForeground(CommonUtil.getColor(getConfigMap().get("hot.song.label.foreground.color").toString()));
		jdSongLabel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, CommonUtil.getColor(getConfigMap().get("new.song.label.border.color").toString())));
		
		netSongLabel = new JLabel();
		netSongLabel.setFont(Fonts.songTiB13());
		netSongLabel.setText(getConfigMap().get("net.song.label").toString());
		netSongLabel.setHorizontalAlignment(SwingConstants.CENTER);
		netSongLabel.setOpaque(true);
        netSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.background.color").toString()));
		netSongLabel.setForeground(CommonUtil.getColor(getConfigMap().get("hot.song.label.foreground.color").toString()));
		netSongLabel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, CommonUtil.getColor(getConfigMap().get("new.song.label.border.color").toString())));
		
		
		nameTabPanel.add(newSongLabel);
		nameTabPanel.add(hotSongLabel);
		nameTabPanel.add(jdSongLabel);
		nameTabPanel.add(netSongLabel);
		
		hotPanel.setLayout(new BorderLayout(0,0));
		hotPanel.add(hotTitlePanel,BorderLayout.NORTH);
		hotPanel.add(nameTabPanel,BorderLayout.CENTER);
		hotTitlePanel = null;
		nameTabPanel = null;
		
		newSongLabel.addMouseListener(new MusicMouseListener() {
			@Override
			public void mouseExited(MouseEvent e) {
				if(clickNewSongLabel == 0) {
					newSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.background.color").toString()));
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				if(clickNewSongLabel == 0) {
					newSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.selected.background.color").toString()));
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					if(netSongType != 1) {
						netSongType = 1;
						newSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("new.song.label.background.color").toString()));
						clickNewSongLabel = 1;
						
						hotSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.background.color").toString()));
						clickHotSongLabel = 0;
						
						jdSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.background.color").toString()));
						clickJdSongLabel = 0;
						
						netSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.background.color").toString()));
						clickNetSongLabel = 0;
						
						Object netSongListClassName = getConfigMap().get("net.song.list.class");
						if(null != netSongListClassName && !StringUtil.isEmpty(netSongListClassName.toString())) {
							try {
								NetSongList netSongList = (NetSongList)Class.forName(netSongListClassName.toString()).newInstance();
								netSongList.setType(NET_SONG_LIST_TOP100);
								if(null != searchSongListThread) {
									searchSongListThread.interrupt();
								}
								searchSongListThread = new Thread(netSongList);
								searchSongListThread.start();
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				}
			}
		});
		
		hotSongLabel.addMouseListener(new MusicMouseListener() {
			@Override
			public void mouseExited(MouseEvent e) {
				if(clickHotSongLabel == 0) {
					hotSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.background.color").toString()));
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				if(clickHotSongLabel == 0) {
					hotSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.selected.background.color").toString()));
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					if(netSongType != 2) {
						netSongType = 2;
						hotSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("new.song.label.background.color").toString()));
						clickHotSongLabel = 1;
						
						newSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.background.color").toString()));
						clickNewSongLabel = 0;
						
						jdSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.background.color").toString()));
						clickJdSongLabel = 0;
						
						netSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.background.color").toString()));
						clickNetSongLabel = 0;
						
						Object netSongListClassName = getConfigMap().get("net.song.list.class");
						if(null != netSongListClassName && !StringUtil.isEmpty(netSongListClassName.toString())) {
							try {
								NetSongList netSongList = (NetSongList)Class.forName(netSongListClassName.toString()).newInstance();
								netSongList.setType(NET_SONG_LIST_HOT500);
								if(null != searchSongListThread) {
									searchSongListThread.interrupt();
								}
								searchSongListThread = new Thread(netSongList);
								searchSongListThread.start();
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				}
			}
		});
		
		jdSongLabel.addMouseListener(new MusicMouseListener() {
			@Override
			public void mouseExited(MouseEvent e) {
				if(clickJdSongLabel == 0) {
					jdSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.background.color").toString()));
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				if(clickJdSongLabel == 0) {
					jdSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.selected.background.color").toString()));
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					if(netSongType != 3) {
						netSongType = 3;
						jdSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("new.song.label.background.color").toString()));
						clickJdSongLabel = 1;
						
						newSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.background.color").toString()));
						clickNewSongLabel = 0;
						
						hotSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.background.color").toString()));
						clickHotSongLabel = 0;
						
						netSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.background.color").toString()));
						clickNetSongLabel = 0;
						
						Object netSongListClassName = getConfigMap().get("net.song.list.class");
						if(null != netSongListClassName && !StringUtil.isEmpty(netSongListClassName.toString())) {
							try {
								NetSongList netSongList = (NetSongList)Class.forName(netSongListClassName.toString()).newInstance();
								netSongList.setType(NET_SONG_LIST_OLD);
								if(null != searchSongListThread) {
									searchSongListThread.interrupt();
								}
								searchSongListThread = new Thread(netSongList);
								searchSongListThread.start();
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				}
			}
		});
		
		
		netSongLabel.addMouseListener(new MusicMouseListener() {
			@Override
			public void mouseExited(MouseEvent e) {
				if(clickNetSongLabel == 0) {
					netSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.background.color").toString()));
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				if(clickNetSongLabel == 0) {
					netSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.selected.background.color").toString()));
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					if(netSongType != 4) {
						netSongType = 4;
						netSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("new.song.label.background.color").toString()));
						clickNetSongLabel = 1;
						
						newSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.background.color").toString()));
						clickNewSongLabel = 0;
						
						hotSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.background.color").toString()));
						clickHotSongLabel = 0;
						
						jdSongLabel.setBackground(CommonUtil.getColor(getConfigMap().get("hot.song.label.background.color").toString()));
						clickJdSongLabel = 0;
						
						Object netSongListClassName = getConfigMap().get("net.song.list.class");
						if(null != netSongListClassName && !StringUtil.isEmpty(netSongListClassName.toString())) {
							try {
								NetSongList netSongList = (NetSongList)Class.forName(netSongListClassName.toString()).newInstance();
								netSongList.setType(NET_SONG_LIST_NET);
								if(null != searchSongListThread) {
									searchSongListThread.interrupt();
								}
								searchSongListThread = new Thread(netSongList);
								searchSongListThread.start();
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				}
			}
		});
	}
	
	private void setNetSongListPanelLayout() {
		NetSongPanel netSongPanel = NetSongPanel.getInstance();
		netSongListPanel.add(netSongPanel.create(),BorderLayout.CENTER);
		netSongPanel = null;
	}
	
}
