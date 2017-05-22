package com.baiting.test;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

public class RoundRectBorder extends AbstractBorder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3014786525354016765L;
	private Color fieldColor = Color.white;
	private int fieldThickness = 1;

	public RoundRectBorder(Color color, int thickness) {
		fieldColor = color;
		fieldThickness = thickness;
	}

	public RoundRectBorder(Color color) {
		fieldColor = color;
	}

	public Insets getBorderInsets(Component component) {
		int x = fieldThickness / 2;
		int y = fieldThickness / 2;
		int w = fieldThickness / 2;
		int h = fieldThickness / 2;
		Insets insets = new Insets(x, y, w, h);
		return insets;
	}

	public boolean isBorderOpaque() {
		return false;
	}

	public void paintBorder(Component component, Graphics g, int x, int y,
			int width, int height) {
		Color oldColor = Color.WHITE;
		g.setColor(fieldColor);
		g.fillRect(x, y, fieldThickness / 2, fieldThickness / 2);
		g.fillRect(x + width - fieldThickness / 2, y, fieldThickness / 2,
				fieldThickness / 2);
		g.fillRect(x + width - fieldThickness / 2, y + height - fieldThickness
				/ 2, fieldThickness / 2, fieldThickness / 2);
		g.fillRect(x, y + height - fieldThickness / 2, fieldThickness / 2,
				fieldThickness / 2);

		g.setColor(oldColor);
		g.fillArc(x, y, fieldThickness, fieldThickness, 90, 90);
		g.fillArc(x + width - fieldThickness, y, fieldThickness,
				fieldThickness, 0, 90);
		g.fillArc(x + width - fieldThickness, y + height - fieldThickness,
				fieldThickness, fieldThickness, 0, -90);
		g.fillArc(x, y + height - fieldThickness, fieldThickness,
				fieldThickness, -90, -90);

		g.setColor(oldColor);
	}

}
