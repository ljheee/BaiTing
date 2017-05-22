package com.baiting.ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JSlider;
import javax.swing.plaf.metal.MetalSliderUI;

public class VolumeSliderUI extends MetalSliderUI {

	/** 
     * 绘制指示物
     */
    public void paintThumb(Graphics g) {
    	//ImageIcon imageIcon = new ImageIcon(Music.getIconPath()+"/volume01.gif");
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //填充椭圆框为当前thumb位置
        g2d.fillOval(thumbRect.x, thumbRect.y, thumbRect.width-2,thumbRect.height-2);
        //也可以帖图(利用鼠标事件转换image即可体现不同状态)
        //g2d.drawImage(imageIcon.getImage(), thumbRect.x, thumbRect.y, thumbRect.width,thumbRect.height,null);
    }
    
    /** 
     * 绘制刻度轨迹
     */
    public void paintTrack(Graphics g) {
        int cy, cw;
        Rectangle trackBounds = trackRect;
        if (slider.getOrientation() == JSlider.HORIZONTAL) {
            Graphics2D g2 = (Graphics2D) g;
            cy = (trackBounds.height / 2) - 2;
            cw = trackBounds.width;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.translate(trackBounds.x, trackBounds.y + cy);
            // 背景设为灰色
            String bkColor = "#E0AAA1";
            String bkColor2 = "#f3ada1";
            String bkColor3 = "#b45544";
            String bkColor4 = "#FFCBDF";
            String bkColor5 = "#933f37";
            if(!slider.isEnabled()) {
            	bkColor = bkColor2 = bkColor3 = "#aaa9a9";
            	bkColor5 = "#999797";
            }
            g2.setPaint(Color.decode(bkColor));
            g2.fillRect(0, -cy, cw, cy * 2);
            int trackLeft = 0;
            int trackRight = 0;
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
                fillRight = !slider.isEnabled() ? trackRight - 1
                        : trackRight - 2;
            }
            // 设定渐变
            g2.setPaint(new GradientPaint(0, 0, Color.decode(bkColor2), cw, 0,Color.decode(bkColor3), true));
            g2.fillRect(0, -cy, fillRight - fillLeft, cy * 2);
            g2.setPaint(Color.decode(bkColor4));
            Polygon polygon = new Polygon();
            polygon.addPoint(0, cy);
            polygon.addPoint(0, -cy);
            polygon.addPoint(cw, -cy);
            g2.fillPolygon(polygon);
            polygon.reset();
            g2.setPaint(Color.decode(bkColor5));
            g2.drawLine(0, cy, cw - 1, cy);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
            g2.translate(-trackBounds.x, -(trackBounds.y + cy));
        } else {
            super.paintTrack(g);
        }
    }
	
}
