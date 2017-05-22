package com.baiting.layout;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import com.baiting.Music;
import com.baiting.font.Fonts;
import com.baiting.util.CommonUtil;

public class MusicTableCellRenderer extends JLabel implements TableCellRenderer {
	
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
		if (isSelected) {
			//label.setOpaque(true);
			label.setBackground(new Color(251, 195, 204));
			label.setForeground(Color.BLACK);
			label.setFont(Fonts.songTiB12());
			label.setText(value.toString());
			return label;
		} else {
			//label.setOpaque(true);
			if (row % 2 == 0) {
				label.setBackground(CommonUtil.getColor(Music.getConfigMap().get("table.cell.renderer.background.color1").toString()));
			} else {
				label.setBackground(CommonUtil.getColor(Music.getConfigMap().get("table.cell.renderer.background.color2").toString()));
			}
			label.setFont(Fonts.songTi12());
			label.setText(value.toString());
			return label;
		}

	}

}
