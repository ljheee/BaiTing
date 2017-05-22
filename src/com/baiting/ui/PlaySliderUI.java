package com.baiting.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JSlider;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalSliderUI;

public class PlaySliderUI extends MetalSliderUI {

	/** */
	/**
	 * 绘制指示物
	 */
	public void paintThumb(Graphics g) {
		// ImageIcon imageIcon = new
		// ImageIcon(Music.getIconPath()+"/volume01.gif");
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.decode("#FDF0EE"));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		// 填充椭圆框为当前thumb位置
		g2d.fillRect(thumbRect.x, thumbRect.y + 3, thumbRect.width,
				thumbRect.height / 2 + 1);
		// g2d.fillOval(thumbRect.x, thumbRect.y+2,
		// thumbRect.width,thumbRect.height/2+3);
		// 也可以帖图(利用鼠标事件转换image即可体现不同状态)
		g2d.setColor(Color.decode("#AC7A71"));
		g2d.drawLine(thumbRect.x, thumbRect.y + 3, thumbRect.x
				+ thumbRect.width - 1, thumbRect.y + 3);
		g2d.drawLine(thumbRect.x, thumbRect.y + 3 + thumbRect.height / 2 + 1,
				thumbRect.x + thumbRect.width - 1, thumbRect.y + 3
						+ thumbRect.height / 2 + 1);
		g2d.drawLine(thumbRect.x, thumbRect.y + 3, thumbRect.x, thumbRect.y + 3
				+ thumbRect.height / 2 + 1);
		g2d.drawLine(thumbRect.x + thumbRect.width - 1, thumbRect.y + 3,
				thumbRect.x + thumbRect.width - 1, thumbRect.y + 3
						+ thumbRect.height / 2 + 1);
	}

	/** */
	/**
	 * 绘制刻度轨迹
	 */
	public void paintTrack(Graphics g) {
		if (slider.getOrientation() == JSlider.HORIZONTAL) {
			Graphics2D g2 = (Graphics2D) g;

			// 背景设为灰色
			g2.translate(trackRect.x, trackRect.y);
			String bkColor = "#E29588";
			int trackLeft = 0;
			int trackTop = 0;
			int trackRight = 0;
			int trackBottom = 0;
			// Draw the track
			trackBottom = (trackRect.height - 1) - getThumbOverhang();
			trackTop = trackBottom - (getTrackWidth() - 1);
			trackRight = trackRect.width - 1;
			g2.setPaint(Color.decode("#7A8A99"));
			g2.drawRect(trackLeft, trackTop, (trackRight - trackLeft) - 1,(trackBottom - trackTop) - 1);
			
			g2.setPaint(Color.decode("#A3B8CC"));
			g2.drawLine(trackLeft + 1, trackBottom, trackRight, trackBottom);
			g2.drawLine(trackRight, trackTop + 1, trackRight, trackBottom);
			 
			g2.setColor(MetalLookAndFeel.getControlShadow());
			g2.drawLine(trackLeft + 1, trackTop + 1, trackRight - 2,trackTop +1); 
			g2.drawLine(trackLeft + 1, trackTop + 1, trackLeft + 1,trackBottom - 2);
			
            trackRight = trackRect.width - 1;
            int middleOfThumb = 0;
            int fillLeft = 0;
            int fillRight = 0;
            //坐标换算
            middleOfThumb = thumbRect.x + (thumbRect.width / 2);
            middleOfThumb -= trackRect.x;
			
			if (!drawInverted()) {
                fillLeft = !slider.isEnabled() ? trackLeft : trackLeft + 1;
                fillRight = middleOfThumb;
            } else {
                fillLeft = middleOfThumb;
                fillRight = !slider.isEnabled() ? trackRight - 1 : trackRight - 2;
            }
			g2.setPaint(Color.decode(bkColor));
			g2.fillRect(trackLeft + 1, trackTop + 1, fillRight - fillLeft, trackTop-1);
			g2.translate(-trackRect.x, -trackRect.y);
		} else {
			super.paintTrack(g);
		}
	}

}
