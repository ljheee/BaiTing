package com.baiting.test;

import net.sf.json.JSONObject;

public class ParseJsonTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	/*	String jsonData = "{\"Users\":\"null\",\"version\":\"1.0\",\"items\":["+
	                      "{\"name\":\"xiaoli\",\"age\":\"12\"},"+
	                      "{\"name\":\"xiaoqiang\",\"age\":\"18\"},"+
	                      "{\"name\":\"xiaohui\",\"age\":\"20\"},"+
	                      "{\"name\":\"xiaoxiao\",\"age\":\"99\"}],"+
				          "\"totals\":\"4\"}";*/
		String jsonData = "{\"sid\":97450674,\"author\":\"\u003Cem\u003E\u5409\u514b\u96bd\u9038\u003C\\/em\u003E,\u003Cem\u003ESnoop Dogg\u003C\\/em\u003E\",\"sname\":\"\u003Cem\u003ESummer Time\u003C\\/em\u003E\"}";
	 /* List<Map<String,String>> lists = JsonUtil.parseJsonToList(jsonData,"songItem",new String[]{"sname"});
	  for (Map<String,String> map : lists) {
		Set<Map.Entry<String, String>> sets = map.entrySet();
		for (Iterator<Map.Entry<String, String>> iterator = sets.iterator(); iterator.hasNext();) {
			Map.Entry<String, String> entry = iterator.next();
			System.out.println(entry.getKey()+"="+entry.getValue());
		}
		System.out.println("======================");*/
		JSONObject jsonObj = JSONObject.fromObject(jsonData);
		System.out.println(jsonObj.get("sname"));
		System.out.println(jsonObj.get("author"));
		//JSONArray jsonArray = jsonObj.getJSONArray(items);

	}

}
