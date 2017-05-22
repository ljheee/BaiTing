package com.baiting.layout;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import com.baiting.Music;
import com.baiting.font.Fonts;
import com.baiting.util.CommonUtil;

public class MusicListCellRenderer extends MTableCellRenderer implements ListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8384301393234830290L;
	protected JLabel label;
	private boolean isAlignCenter = false;
	
	public MusicListCellRenderer(boolean isAlignCenter) {
		super();
		this.isAlignCenter = isAlignCenter;
	}
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value,	int index, boolean isSelected, boolean cellHasFocus) {
		label = new JLabel();
		label.setOpaque(false);
		label.setText(" "+value.toString());
		label.setFont(Fonts.songTi12());
		label.setToolTipText(value.toString());
		if(isAlignCenter) {
		   label.setHorizontalAlignment(SwingConstants.CENTER);
		}
		if(isSelected) {
			label.setFont(Fonts.songTiB12());
			//label.setOpaque(true);
			label.setBackground(Music.getSongListSelectionBackground());
			//label.setForeground(Music.getSongListSelectionForeground());
		} else if((index+1)%2==0) {
			//label.setOpaque(true);
			label.setBackground(CommonUtil.getColor(Music.getConfigMap().get("list.cell.renderer.background.color").toString()));
		}
		return label;
	}

}
