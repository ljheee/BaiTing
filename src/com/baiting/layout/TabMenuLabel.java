package com.baiting.layout;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.baiting.Music;
import com.baiting.bean.TabMenu;
import com.baiting.font.Fonts;
import com.baiting.http.netsong.NetSongList;
import com.baiting.listener.MusicMouseListener;
import com.baiting.util.StringUtil;

public class TabMenuLabel extends JLabel {

	private static final long serialVersionUID = 4063857957777738920L;
	
	private static int count;
	private JLabel label;
	private TabMenu tabMenu;
	private static List<TabMenu> tabMenuList = new ArrayList<TabMenu>();
	private static Thread searchSongListThread;
	
	public TabMenuLabel(String name,String text,JPanel panel) {
		count++;
		label = new JLabel();
	    if(StringUtil.isEmpty(name)) {
	    	name = "label"+count;
	    }
	    init(name,text,panel);
	    mouseListener();
	}
	
	private void init(String name,String text,JPanel panel) {
		label.setText(text);
		label.setName(name);
		label.setPreferredSize(new Dimension(100, 40));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(Fonts.tabSongTi());
		label.setOpaque(false);
		
		tabMenu = new TabMenu();
	    tabMenu.setIndex(count);
	    tabMenu.setName(name);
	    tabMenu.setText(text);
	    tabMenu.setPanel(panel);
	    tabMenu.setLabel(label);
	    tabMenu.setOnClick(0);
	    
		tabMenuList.add(tabMenu);
	}
	
	private TabMenu getTabMenu(JLabel label) {
		String labelName = label.getName();
		for(int i=1;i<=count;i++) {
			if(labelName.equals(tabMenuList.get(i-1).getName())) {
				return tabMenuList.get(i-1);
			}
		}
		return null;
	}
	
	public TabMenu getTabMenu(String name) {
		if(StringUtil.isEmpty(name)) {
			return null;
		}
		String labelName = name;
		for(int i=1;i<=count;i++) {
			if(labelName.equals(tabMenuList.get(i-1).getName())) {
				return tabMenuList.get(i-1);
			}
		}
		return null;
	}
	
	private void mouseListener() {
		 label.addMouseListener(new MusicMouseListener() {
			@Override
			public void mouseExited(MouseEvent e) {
				TabMenu tabMenuTmp = getTabMenu(label);
				if(tabMenuTmp.getOnClick() == 0) {
					label.updateUI();
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				label.setCursor(new Cursor(Cursor.HAND_CURSOR));
				label.setOpaque(false);
				Graphics g = label.getGraphics();
				ImageIcon imageIcon = new ImageIcon(Music.getIconPath()+Music.getSeparator()+Music.getConfigMap().get("tab.background.icon").toString());
			    g.drawImage(imageIcon.getImage(), 0, 0, null);
				label.update(g);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				ShowMainPanel.getPanel().removeAll();
				ShowMainPanel.getPanel().repaint();
				ShowMainPanel.setPanel(tabMenu.getPanel());
				ShowMainPanel.getPanel().revalidate();
				label.setCursor(new Cursor(Cursor.HAND_CURSOR));
				ImageIcon imageIcon = new ImageIcon(Music.getIconPath()+Music.getSeparator()+Music.getSeparator()+Music.getConfigMap().get("tab.background.icon").toString());
				Graphics g = label.getGraphics();
				g.drawImage(imageIcon.getImage(), 0, 0, null);
				imageIcon = null;
				label.update(g);
				tabMenu.setOnClick(1);
				Music.setTabSelectedName(label.getName());
				
				for (int i = 0; i < tabMenuList.size(); i++) {
					if(!label.getName().equals(tabMenuList.get(i).getName())) {
						TabMenu tabMenuTmp = tabMenuList.get(i);
						tabMenuTmp.setOnClick(0);
						tabMenuTmp.getLabel().updateUI();
					}
				}
				if("netSearchLabel".equals(label.getName())) {
					
					Object netSongListClassName = getConfigMap().get("net.song.list.class");
					if(null != netSongListClassName && !StringUtil.isEmpty(netSongListClassName.toString())) {
						try {
							NetSongList netSongList = (NetSongList)Class.forName(netSongListClassName.toString()).newInstance();
							netSongList.setType(0);
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
		});
	}
	
	public JLabel create() {
		return label;
	}
	
	public int getCount() {
		return count;
	}
	
}
