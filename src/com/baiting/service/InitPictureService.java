package com.baiting.service;

import java.io.File;

import com.baiting.bean.BackgroundPicture;
import com.baiting.util.StringUtil;

public class InitPictureService extends MusicService implements Runnable {

	@Override
	public void run() {
		loadingPics();
	}
	
	/**
	 * 载入图片
	 * @return
	 */
	private boolean loadingPics() {
		boolean isSuccess = false;
		String picDir = getSingerPicDir();
		File dir = new File(picDir);
		File[] fileList = null;
		if(dir.exists()) {
			fileList = dir.listFiles();
			BackgroundPicture bkPic = null;
			for (int i = 0; i < fileList.length; i++) {
				String picFileName = fileList[i].getName();
				int startPostion = picFileName.lastIndexOf(".");
				if(startPostion>0) {
					String suffix = picFileName.substring(startPostion+1, picFileName.length()).trim();
					if(StringUtil.isExistIgnoreCase(suffix, getSupportPictureFormatter())) {
						bkPic = new BackgroundPicture();
						bkPic.setLocal(true);
						bkPic.setPath(fileList[i].getAbsolutePath());
						addBkPicList(bkPic);
						isSuccess = true;
					}
				}//end[if]
			}//end[for]
			bkPic = null;
		}
		fileList = null;
		dir = null;
		return isSuccess;
	}
}
