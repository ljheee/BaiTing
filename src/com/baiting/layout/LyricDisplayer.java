package com.baiting.layout;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.baiting.Music;
import com.baiting.bean.BackgroundPicture;
import com.baiting.bean.LyricStatement;
import com.baiting.font.Fonts;
import com.baiting.util.StringUtil;

public class LyricDisplayer extends JPanel {

	private static final Logger log = Logger.getLogger(LyricDisplayer.class.getName());
	
	private static final long serialVersionUID = 250794113230723615L;
	protected static Color currentLineColor = Color.YELLOW;
	protected static Color otherLineColor = Color.BLACK;
	protected final int UP_DOWN_LINES = 8;
	protected static List<LyricStatement> statements;
	protected static int index;
	protected BufferedImage backgroundImage = null;
	private String backGroundImagePath = null;
	protected Image bufferImage = null;
	private boolean isLocalPic;
	private Dimension bufferedSize;
	private Graphics2D g2d;
	private boolean isChangeBk = true;
	private static int picIndex = 0;
	private List<BackgroundPicture> bkList = null;
	private static String message = Music.getConfigMap().get("not.find.lrc.msg").toString();
	private static LyricDisplayer instance;
	private static int lrcX = 0;  //歌词的x坐标
	
	private LyricDisplayer() {
		super();
		this.setOpaque(false);
		setPreferredSize(new Dimension(Music.MUSIC_WINDOW_WIDTH-Music.MUSIC_WINDOW_RIGHT_WIDTH,Music.MUSIC_WINDOW_HEIGHT-2*Music.MUSIC_WINDOW_BOTTOM_HEIGHT));
	}
	
	public synchronized static LyricDisplayer getInstance() {
		if(instance == null) {
			instance = new LyricDisplayer();
		}
		return instance;
	}
	
	public void close() {
		if(instance != null)
		   instance = null;
	}

	public String getBackGroundImagePath() {
		return backGroundImagePath;
	}

	public void setBackGroundImagePath(String backGroundImagePath) {
		this.backGroundImagePath = backGroundImagePath;
	}

	/**
	 * 准备歌词，显示歌词
	 * @param statements
	 */
	public void prepareDisplay(List<LyricStatement> statements) {
		isChangeBk = true;
		LyricDisplayer.statements = statements;
		LyricDisplayer.index = 0;
		this.setFont(Fonts.songTi24());
		lrcX = 0;
	}

	/**
	 * 指定行凸显
	 * @param index
	 */
	public void displayLyric(int index) {
		if(index<0) {
			statements = null;
		}
		LyricDisplayer.index = index;
		this.drawBufferImage();
		this.paint(this.getGraphics());
	}

	/**
	 * g2b图像中间写字符串
	 * @param height
	 * @param lyric
	 * @param g2d
	 * @param color
	 */
	protected void drawLineInMiddle(int height, String lyric, Graphics2D g2d, Color color) {
		FontMetrics fm = g2d.getFontMetrics();
		g2d.setColor(color);
		//int x = (this.getWidth()-fm.stringWidth(lyric))/2;
		int x = (this.getWidth()-fm.stringWidth(lyric))/2;
		if(null != bkList && bkList.size()>0 && lrcX == 0) {
				String maxLenStat = lrcMaxStatement();
				if(!StringUtil.isEmpty(maxLenStat)) {
					lrcX = (int)(((this.getWidth()-fm.stringWidth(maxLenStat)))*9/10);
				}
				if(lrcX >0 ){
					x = lrcX;
				}
		} else if(lrcX != 0){
			x = lrcX;
		}
		g2d.drawString(lyric, x, height);
	}
	
	private String lrcMaxStatement() {
		String value = null;
		List<LyricStatement>  lrcStats = LyricDisplayer.statements;
		
		if(null != lrcStats && lrcStats.size()>0) {
			int statLen = lrcStats.get(0).getStatement().length();
			int maxLenLine = 0;
			for (int i=1;i<lrcStats.size();i++) {
				if(statLen<lrcStats.get(i).getStatement().length()) {
					statLen = lrcStats.get(i).getStatement().length();
					maxLenLine = i;
				}
			}
			value = lrcStats.get(maxLenLine).getStatement();
		}
		
		return value;
	}

	/**
	 * 重画图像
	 */
	protected void drawBufferImage() {
		BufferedImage tempBufferedImage = new BufferedImage(this.getWidth(),this.getHeight(),BufferedImage.TYPE_INT_RGB);//this.createImage(this.getWidth(),this.getHeight(),);
		this.bufferedSize = this.getSize();
		//if(tempBufferedImage == null) {
	//		tempBufferedImage  = new BufferedImage(this.bufferedSize.width,this.bufferedSize.height,BufferedImage.TYPE_INT_RGB);
			//tempBufferedImage = this.createImage(this.bufferedSize.width,this.bufferedSize.height);
			//super.setPreferredSize(new Dimension(this.bufferedSize.width, this.bufferedSize.height));
	//	}
		
		if(isChangeBk) {
			picIndex++;
		}
		bkList = Music.getBkPicList();
		InputStream in = null;
		if(null != bkList && bkList.size()>0 && isChangeBk) {
			if(picIndex>(bkList.size()-1)) {
				picIndex = 0;
			}
			BackgroundPicture bkPic = bkList.get(picIndex);
			if(bkPic.isLocal()) {
				File file = new File(bkPic.getPath());
				if(file.exists()) {
					try {
						in = new FileInputStream(file);
						isLocalPic = false;
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						in = null;
					}
				}
				file = null;
			} else {
				URL url = null;
				try {
					url = new URL(bkPic.getPath());
					URLConnection rulConnection = url.openConnection();
					rulConnection.setReadTimeout((int)(1000));
					rulConnection.setConnectTimeout((int)(1000));
					rulConnection.connect();
					if(null != rulConnection.getInputStream()) {
						in = rulConnection.getInputStream();
					} else {
						in = null;
					}
					isLocalPic = false;
				} catch (MalformedURLException e) {
					e.printStackTrace();
					log.info("网络图片加载超失败---[异常]--");
					in = null;
				} catch (IOException e) {
					log.info("网络图片加载超失败---[异常]--");
					//e.printStackTrace();
					in = null;
				} catch (Exception e) {
				    //e.printStackTrace();
					log.info("网络图片加载超失败---[异常，可能是加载超时]--");
					try {
						in.close();
					} catch (IOException e1) {
						e1.printStackTrace();
						in = null;
					}
					in = null;
				}  finally {
					url = null;
				}
			}
		} else if(!StringUtil.isEmpty(backGroundImagePath) && isChangeBk){
			File file = new File(backGroundImagePath);
			if(file.exists()) {
				try {
					in = new FileInputStream(file);
					isLocalPic = true;
				} catch (FileNotFoundException e) {
					//e.printStackTrace();
					log.info("本地图片加载超失败---[异常]--");
					in = null;
				}
			}
			file = null;
		}
		isChangeBk = false;
		if(null != in) {
			try {
				backgroundImage = ImageIO.read(in);
				//backgroundImage = backgroundImage.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT);
			} catch (IOException e) {
				e.printStackTrace();
				backgroundImage = null;
			} finally {
				/*try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}*/
			}
		} 
		g2d = (Graphics2D) tempBufferedImage.getGraphics();
		g2d.setFont(Fonts.songTi20());
		//g2d.
		
		int drawImageW = this.getWidth();
		int drawImageH = this.getHeight();
		int x = 0;
		int y = 0;
		
		if(null != backgroundImage && !isLocalPic) {
			drawImageW = 0;
			drawImageH = 0;
			//计算图片的缩放
			int currentPanelW = this.getWidth();
			int currentPanelH = this.getHeight();
			int imageW = backgroundImage.getWidth();
			int imageH = backgroundImage.getHeight();
			if(imageW>currentPanelW && imageW>=imageH) {
				float imageZoomRate = (float)imageW/imageH;
				drawImageW = currentPanelW;
				drawImageH = (int)(drawImageW/imageZoomRate);
			} else if(imageH>currentPanelH && imageH>=imageW) {
				float imageZoomRate = (float)imageH/imageW;
				drawImageH = currentPanelH;
				drawImageW = (int)(drawImageH/imageZoomRate);
				//log.info("高缩放--图片原大小["+imageW+","+imageH+"]--缩放后大小["+drawImageW+","+drawImageH+"]-");
				//log.info("高缩放--缩放比例-["+imageZoomRate+"]");
				
			} else if(imageH>currentPanelH && imageH<=imageW) {
				drawImageW = imageW;
				drawImageH = currentPanelH;
			} else {
				drawImageW = imageW;
				drawImageH = imageH;
				//计算图片显示的位置
			}
			x = (int)((currentPanelW/2)*0.05f);
			y = (currentPanelH-drawImageH)/2;
			if(y<0 || x<0) {
				log.info("当前宽高["+currentPanelW+","+currentPanelH+"],图片宽高["+drawImageW+","+drawImageH+"]");
			}
			//获取背景图片的颜色
			int[] rgb = new int[]{-1,-1,-1};
			int pixel = 0;
			try {
				pixel = backgroundImage.getRGB(1, 1);
				rgb[0] = (pixel & 0xff0000 )>>16 ; 
				rgb[1] = (pixel & 0xff00 )>>8; 
				rgb[2] = (pixel & 0xff );
				g2d.setColor(new Color(rgb[0],rgb[1],rgb[2]));
				g2d.fillRect(0, 0, currentPanelW, currentPanelH);
				/*
				pixel = backgroundImage.getRGB(drawImageW/2, drawImageH/2);
				rgb[0] = (pixel & 0xff0000 )>>16 ; 
				rgb[1] = (pixel & 0xff00 )>>8; 
				rgb[2] = (pixel & 0xff );*/
			} catch (Exception e) {
				e.printStackTrace();
				log.info("取到的坐标("+(x+1)+","+(y+1)+")");
				log.info("当前宽高["+currentPanelW+","+currentPanelH+"],图片宽高["+drawImageW+","+drawImageH+"]");
				log.info("图像所在的末坐标("+(x+drawImageW)+","+(y+drawImageW)+")");
			}
			if(rgb[0]>-1) {
				currentLineColor = new Color(255-rgb[0],255-rgb[1],255-rgb[2]);
				otherLineColor = new Color((255-rgb[0])/2,(255-rgb[1])/2,(255-rgb[2])/2);
			}
		}
		//设置透明
		//AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
	//	g2d.setComposite(ac);
		g2d.drawImage(this.backgroundImage,x, y, drawImageW,drawImageH,null);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		if (LyricDisplayer.statements != null && LyricDisplayer.statements.size() != 0) {
			// draw current line
			g2d.setFont(Fonts.songTiB24());
			this.drawLineInMiddle(this.getHeight()/2,LyricDisplayer.statements.get(index).getStatement(), g2d, currentLineColor);
			int perHeight = g2d.getFontMetrics().getHeight() + 5;
			g2d.setFont(Fonts.songTi20());
			// draw down lines
			for (int i = index-UP_DOWN_LINES; i < index; i++) {
				if (i < 0) {
					continue;
				}
				if (index-i > UP_DOWN_LINES/2) {
					// set transparance
					float ratio = (float)(i-index+UP_DOWN_LINES)/(UP_DOWN_LINES/2)/1.2f;
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ratio));
				} else {
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
				}
				this.drawLineInMiddle(this.getHeight()/2-(index-i)* perHeight, 
						LyricDisplayer.statements.get(i).getStatement(),
						g2d, otherLineColor);
			}
			// draw up lines
			for (int i=index+1; i < index+UP_DOWN_LINES; i++) {
				if (i >= LyricDisplayer.statements.size()) {
					break;
				}
				if (i-index > UP_DOWN_LINES/2) {
					// set transparance
					float ratio = (float) (index + UP_DOWN_LINES-i)/(UP_DOWN_LINES/2)/1.2f;
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ratio));
				} else {
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
				}
				this.drawLineInMiddle(this.getHeight()/2+(i-index)*perHeight, LyricDisplayer.statements.get(i).getStatement(), g2d, otherLineColor);
			}
		} else {
			// statements is empty
			this.drawLineInMiddle(this.getHeight()/2, message, g2d,currentLineColor);
		}
		// copyt the buffered image
		this.bufferImage = tempBufferedImage;
	}

	/**
	 * This method is override in order to display the lyric in the panel
	 * 
	 * @param g
	 */
	@Override
	public void paint(Graphics g) {
		if (this.isVisible() == false) {
			return;
		}
		try {
			super.paint(g);
			// draw buffered image
			if (this.bufferImage == null || this.getWidth() != this.bufferedSize.getWidth() || this.getHeight() != this.bufferedSize.getHeight()) {
				this.drawBufferImage();
			}
			g.drawImage(bufferImage, 0, 0, null);
		} catch (Exception e) {
			
		}
	}

	public static String getMessage() {
		return message;
	}

	public static void setMessage(String message) {
		LyricDisplayer.message = message;
	}

	public void setLocalPic(boolean isLocalPic) {
		this.isLocalPic = isLocalPic;
	}

	public boolean isChangeBk() {
		return isChangeBk;
	}

	public void setChangeBk(boolean isChangeBk) {
		this.isChangeBk = isChangeBk;
	}

}