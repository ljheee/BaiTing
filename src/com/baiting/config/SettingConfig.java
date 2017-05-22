package com.baiting.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import com.baiting.Music;

public class SettingConfig extends Music {


	private static final String configFilePath = getBasePath()+"/setting/setting.properties";
	
	private static SettingConfig instance;
	
	private SettingConfig() {
		init();
	}
	
	public static SettingConfig getInstance() {
		if(null == instance) {
			instance = new SettingConfig();
		}
		return instance;
	}
	
	private void init() {
		File configFile = new File(configFilePath);
		if(!configFile.exists()) {
			return;
		}
		InputStream in = null;
		Properties prop = null;
		Set<?> keyValue = null;
		try {
			in = new FileInputStream(configFile);
			prop = new Properties();
			prop.load(in);
			keyValue = prop.keySet();
			if(null == CONFIG_MAP) {
				CONFIG_MAP = new HashMap<String, Object>();
			}
			for (Iterator<?> iterator = keyValue.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				String value = (String)prop.get(key);
				CONFIG_MAP.put(key, value);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.info("["+configFilePath+"]不存在-----");
		} catch (IOException e) {
			e.printStackTrace();
			log.info("读取["+configFilePath+"]文件时,IO异常-----");
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			keyValue = null;
			prop.clear();
			in = null;
			prop = null;
			initConstValue();
		}
	}
}
