package com.baiting.test;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;
import javax.swing.JLabel;

public class TransparentLabel extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3531547837398475686L;
	private float alpha;

	public TransparentLabel(String str, float alpha) {
		this(str, null, JLabel.LEFT, alpha);
	}

	public TransparentLabel(String str, int align, float alpha) {
		this(str, null, align, alpha);
	}

	public TransparentLabel(String str, Icon icon, int align, float alpha) {
		super(str, icon, align);
		this.alpha = alpha;
	}

	/** override to add aplha composite */
	public void paintComponent(Graphics g) {
		((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		super.paintComponent(g);
	}
}
