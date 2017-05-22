package com.baiting;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.sun.awt.AWTUtilities;

public class MusicFrame extends JFrame implements IMusic {

	private static final long serialVersionUID = 6717963981044106528L;
	public boolean isDragged = false;
	public MusicFrame jFrame;
	private Point loc = null;
    private Point tmp = null;
    
	public MusicFrame() {
		setSize(new Dimension(Music.MUSIC_WINDOW_WIDTH, Music.MUSIC_WINDOW_HEIGHT));
		ImageIcon imageIcon = new ImageIcon(Music.getIconPath()+"/"+Music.getConfigMap().get("logo.icon").toString());
		this.setIconImage(imageIcon.getImage()); 
		try {
			try {
				//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				String uiName = UIManager.getSystemLookAndFeelClassName();
				UIManager.setLookAndFeel(uiName);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		this.setUndecorated(true);
		this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		AWTUtilities.setWindowShape(this, new RoundRectangle2D.Double(0.0D, 0.0D, this.getWidth(), this.getHeight(), 8.0D,8.0D));  
		setDragable();
		
	}
	
	private void setDragable() {
		this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent e) {
               isDragged = false;
               setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            public void mousePressed(java.awt.event.MouseEvent e) {
               tmp = new Point(e.getX(), e.getY());
               isDragged = true;
               setCursor(new Cursor(Cursor.MOVE_CURSOR));
            }
        });
		this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent e) {
               if(isDragged) {
                   loc = new Point(getLocation().x + e.getX() - tmp.x,
                	getLocation().y + e.getY() - tmp.y);
                   setLocation(loc);
               }
            }
        });
	}
	
	public void windowMax() {
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	public void windowNormal() {
		this.setExtendedState(JFrame.NORMAL);
	}
	
	public void windowMin() {
		this.setExtendedState(JFrame.ICONIFIED);
	}
}
