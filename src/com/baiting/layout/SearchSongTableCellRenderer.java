package com.baiting.layout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.baiting.Music;
import com.baiting.font.Fonts;
import com.baiting.util.CommonUtil;

public class SearchSongTableCellRenderer extends MusicTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1377815137771487287L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		label = new JLabel();
		label.setOpaque(false);
		super.setOpaque(false);
		if (isSelected) {
			label.setOpaque(true);
			if (column == 0 || column == 4 || column == 5 || column == 3) {
				label.setHorizontalAlignment(SwingConstants.CENTER);
				label.setPreferredSize(new Dimension(50, 30));
				if(column == 3){
					ImageIcon playIcon2 = new ImageIcon(Music.getIconPath()+Music.getSeparator()+Music.getConfigMap().get("listening.icon").toString());
					label.setIcon(playIcon2);
				} else if(column == 4) {
					ImageIcon playIcon2 = new ImageIcon(Music.getIconPath()+Music.getSeparator()+Music.getConfigMap().get("add.icon").toString());
					label.setIcon(playIcon2);
				} else if(column == 5) {
					ImageIcon playIcon2 = new ImageIcon(Music.getIconPath()+Music.getSeparator()+Music.getConfigMap().get("down.icon").toString());
					label.setIcon(playIcon2);
				} else {
					label.setText(value.toString());
				}
			} else if (column == 0) {
				label.setText(" " + (row + 1) + " ");
			} else {
				label.setPreferredSize(new Dimension(100, 30));
				label.setText(" " + value.toString());
			}
			label.setBackground(new Color(251, 195, 204));
			label.setForeground(Color.BLACK);
			label.setFont(Fonts.songTiB12());
			return label;
		} else {
			if (column == 0 || column == 3 || column == 4 || column == 5) {
				label.setHorizontalAlignment(SwingConstants.CENTER);
				label.setPreferredSize(new Dimension(50, 30));
				if (column == 0) {
					label.setText((row+1)+"");
				} else if(column == 3){
					ImageIcon playIcon2 = new ImageIcon(Music.getIconPath()+Music.getSeparator()+Music.getConfigMap().get("listening.icon").toString());
					label.setIcon(playIcon2);
				} else if(column == 4) {
					ImageIcon playIcon2 = new ImageIcon(Music.getIconPath()+Music.getSeparator()+Music.getConfigMap().get("add.icon").toString());
					label.setIcon(playIcon2);
				} else if(column == 5) {
					ImageIcon playIcon2 = new ImageIcon(Music.getIconPath()+Music.getSeparator()+Music.getConfigMap().get("down.icon").toString());
					label.setIcon(playIcon2);
				} else {
					label.setText(value.toString());
				}
			} else {
				label.setPreferredSize(new Dimension(100, 30));
				label.setText(" " + value.toString());
			}
			label.setOpaque(true);
			if (row % 2 == 0) {
				label.setBackground(CommonUtil.getColor(Music.getConfigMap().get("table.cell.renderer.background.color1").toString()));
			} else {
				label.setBackground(CommonUtil.getColor(Music.getConfigMap().get("table.cell.renderer.background.color2").toString()));
			}
			label.setFont(Fonts.songTi12());
			return label;
		}
		
	}

	
	
}
