package com.baiting.test;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;

public class RoundRectLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2725926641310761178L;
	private int fieldRoundRect;
	private int fieldArc = 1234;

	/**
	 * Create a label with round arc
	 * 
	 * @param text
	 *            : text show in label
	 * @param roundRect
	 *            : round arc
	 * @param arc
	 *            : should be in {1,2,3,4,12,34,1234}, default is 1234
	 */
	public RoundRectLabel(String text, int roundRect, int arc) {
		super(text);
		fieldArc = arc;
		fieldRoundRect = roundRect;
	}

	public void paintComponent(Graphics g) {
		g.setColor(getBackground());
		int fieldX = getBounds().x;
		int fieldY = getBounds().y;
		int fieldWeight = getSize().width;
		int fieldHeight = getSize().height;

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		if (fieldArc == 1) {
			g2d.fillRoundRect(fieldX, fieldY, fieldWeight, fieldHeight,
					fieldRoundRect, fieldRoundRect);
			g2d.fillRect(fieldX, fieldY + fieldRoundRect / 2, fieldWeight,
					fieldHeight);
			g2d.fillRect(fieldX + fieldWeight - fieldRoundRect / 2, fieldY,
					fieldRoundRect / 2, fieldHeight);
		} else if (fieldArc == 2) {
			g2d.fillRoundRect(fieldX, fieldY, fieldWeight, fieldHeight,
					fieldRoundRect, fieldRoundRect);
			g2d.fillRect(fieldX, fieldY + fieldRoundRect / 2, fieldWeight,
					fieldHeight);
			g2d.fillRect(fieldX, fieldY, fieldRoundRect / 2, fieldHeight);
		} else if (fieldArc == 3) {
			g2d.fillRoundRect(fieldX, fieldY, fieldWeight, fieldHeight,
					fieldRoundRect, fieldRoundRect);
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
		} else if (fieldArc == 1234) {
			g2d.fillRoundRect(fieldX, fieldY, fieldWeight, fieldHeight,
					fieldRoundRect, fieldRoundRect);
		}

		super.paintComponent(g);
	}

}
