package com.baiting.layout;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.baiting.Music;
import com.baiting.font.Fonts;
import com.baiting.util.CommonUtil;

public class SongDownedTableCellRenderer extends MusicTableCellRenderer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8630490067498349993L;
	
	protected JLabel label;
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
		label = new JLabel();
		label.setOpaque(false);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		super.setOpaque(false);
		if (isSelected) {
			label.setOpaque(true);
			label.setBackground(new Color(251, 195, 204));
			label.setForeground(Color.BLACK);
			label.setFont(Fonts.songTiB12());
			if(column == 1) {
				ImageIcon playIcon2 = new ImageIcon(Music.getIconPath()+Music.getSeparator()+Music.getConfigMap().get("listening.icon").toString());
				label.setIcon(playIcon2);
				playIcon2 = null;
			} else if(column == 2) {
				ImageIcon playIcon2 = new ImageIcon(Music.getIconPath()+Music.getSeparator()+Music.getConfigMap().get("add.icon").toString());
				label.setIcon(playIcon2);
				playIcon2 = null;
			} else {
				label.setText(value.toString());
			}
			return label;
		} else {
			label.setOpaque(true);
			if (row % 2 == 0) {
				label.setBackground(CommonUtil.getColor(Music.getConfigMap().get("table.cell.renderer.background.color1").toString()));
			} else {
				label.setBackground(CommonUtil.getColor(Music.getConfigMap().get("table.cell.renderer.background.color2").toString()));
			}
			label.setFont(Fonts.songTi12());
			if(column == 1) {
				ImageIcon playIcon2 = new ImageIcon(Music.getIconPath()+Music.getSeparator()+Music.getConfigMap().get("listening.icon").toString());
				label.setIcon(playIcon2);
				playIcon2 = null;
			} else if(column == 2) {
				ImageIcon playIcon2 = new ImageIcon(Music.getIconPath()+Music.getSeparator()+Music.getConfigMap().get("add.icon").toString());
				label.setIcon(playIcon2);
				playIcon2 = null;
			} else {
				label.setText(value.toString());
			}
			return label;
		}

	}

}
