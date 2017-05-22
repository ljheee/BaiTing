package com.baiting;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.baiting.util.CommonUtil;

public class MusicBackgroudPanel extends JPanel implements IMusic {

	private static final long serialVersionUID = -1345955907733147240L;
	private ImageIcon bgImage = new ImageIcon(Music.getConfigMap().get("background.icon").toString());

	public MusicBackgroudPanel() {
		super();
		setPreferredSize(new Dimension(Music.MUSIC_WINDOW_WIDTH, Music.MUSIC_WINDOW_HEIGHT));
		this.setOpaque(true);
		setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, CommonUtil.getColor(Music.getConfigMap().get("border.color").toString())));
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		GeneralPath path=new GeneralPath();
		RoundRectangle2D rect=new RoundRectangle2D.Double(0,0,this.getWidth(),getHeight(),10,10);
		path.append(rect,false);
		g2.setClip(path);
		g2.drawImage(bgImage.getImage(), 0, 0, this.getWidth(), getHeight(),this);
		super.paint(g2);
	}
	
}
