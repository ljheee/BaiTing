package com.baiting.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class RoundRectPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -69638421171352690L;
	private int fieldRoundRect;
	private int fieldArc = 1234;

	/**
	 * Create a Panel with round arc
	 * 
	 * @param roundRect
	 *            : round arc
	 * @param arc
	 *            : should be in {1,2,3,4,12,34,1234}, default is 1234
	 */
	public RoundRectPanel(int roundRect, int arc) {
		fieldArc = arc;
		fieldRoundRect = roundRect;
		setOpaque(true);
	}

	public void paintComponent(Graphics g) {

		int fieldX = 0;
		int fieldY = 0;
		int fieldWeight = getSize().width;
		int fieldHeight = getSize().height;
		Graphics2D g2d = (Graphics2D) g;
		g.setColor(Color.white);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.fillRect(fieldX, fieldY, fieldWeight, fieldHeight);
		g.setColor(getBackground());
		g2d.fillRoundRect(fieldX, fieldY, fieldWeight, fieldHeight,
				fieldRoundRect, fieldRoundRect);

		if (fieldArc == 1) {
			g2d.fillRect(fieldX, fieldY + fieldRoundRect / 2, fieldWeight,
					fieldHeight);
			g2d.fillRect(fieldX + fieldWeight - fieldRoundRect / 2, fieldY,
					fieldRoundRect / 2, fieldHeight);
		} else if (fieldArc == 2) {
			g2d.fillRect(fieldX, fieldY, fieldWeight - fieldRoundRect / 2,
					fieldHeight);
			g2d.fillRect(fieldWeight - fieldRoundRect / 2, fieldHeight
					- fieldRoundRect / 2, fieldRoundRect / 2,
					fieldRoundRect / 2);
		} else if (fieldArc == 3) {
			g2d.fillRect(fieldX, fieldY, fieldWeight, fieldRoundRect / 2);
			g2d.fillRect(fieldX, fieldY, fieldRoundRect / 2, fieldHeight);
		} else if (fieldArc == 4) {
			g2d.fillRoundRect(fieldX, fieldY, fieldWeight, fieldHeight,
					fieldRoundRect, fieldRoundRect);
			g2d.fillRect(fieldX, fieldY, fieldWeight, fieldRoundRect / 2);
			g2d.fillRect(fieldX + fieldWeight - fieldRoundRect / 2, fieldY,
					fieldRoundRect / 2, fieldHeight);
		} else if (fieldArc == 12) {
			g2d.fillRoundRect(fieldX, fieldY, fieldWeight, fieldHeight,
					fieldRoundRect, fieldRoundRect);
			g2d.fillRect(fieldX, fieldY + fieldRoundRect / 2, fieldWeight,
					fieldHeight);
		} else if (fieldArc == 34) {
			g2d.fillRoundRect(fieldX, fieldY, fieldWeight, fieldHeight,
					fieldRoundRect, fieldRoundRect);
			g2d.fillRect(fieldX, fieldY, fieldWeight, fieldRoundRect / 2);
		}
	}

}
