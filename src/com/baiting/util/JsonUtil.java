package com.baiting.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil extends Util {

	
	/**
	 * 解析json
	 * @param jsonData
	 * @param items
	 * @param itemsKey
	 * @return
	 */
	public static List<Map<String,String>> parseJsonToList(String jsonData,String items,String[] itemsKey) {
		List<Map<String,String>> lists = new ArrayList<Map<String,String>>();
		if(!StringUtil.isEmpty(jsonData) && !StringUtil.isEmpty(jsonData)
				&& null != items && itemsKey.length>0) {
			JSONObject jsonObj = JSONObject.fromObject(jsonData);
			JSONArray jsonArray = jsonObj.getJSONArray(items);
			log.info("开始解析json格式---");
			if(jsonArray.size()>0) {
				JSONObject jsonObject = null;
				Map<String,String> map = null;
				for (int i = 0; i < jsonArray.size(); i++) {
					jsonObject = jsonArray.getJSONObject(i);
					map = new HashMap<String, String>();
					for (Iterator<?> iterator = jsonObject.keys(); iterator.hasNext();) {
						String key = (String) iterator.next();
						String value = jsonObject.getString(key);
						if(Arrays.binarySearch(itemsKey, key)>=0) {
							map.put(key, value);
						}
					}
					if(map.size()>0)
						lists.add(map);
				}
				jsonObject = null;
				map = null;
			}
			jsonObj = null;
			jsonArray = null;
		} else {
			log.info("Error---[参数传入有误]------");
		}
		return lists;
	}
	
}
