package com.baiting.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Vector;

import com.baiting.Music;
import com.baiting.util.StringUtil;

public class HttpRequest extends Music {

	private String urlEncode;
	
	public HttpRequest() {
		
	}
	
	public HttpRequest(String encode) {
		this.urlEncode = encode;
	}
	
	/**
	 * GET发送请求
	 * @param urlStr
	 * @return
	 */
	public HttpResponse sendGet(String urlStr) throws IOException{
		return send(urlStr,"GET",null,null);
	}
	
	/**
	 * GET发送请求
	 * @param urlStr
	 * @return
	 */
	public HttpResponse sendGet(String urlStr,String urlEncode) throws IOException{
		this.urlEncode = urlEncode;
		return send(urlStr,"GET",null,null);
	}
	
	/**
	 * GET发送请求
	 * @param urlStr
	 * @param params
	 * @return
	 */
	public HttpResponse sendGet(String urlStr,Map<String,String> params) throws IOException{
		return send(urlStr,"GET",params,null);
	}
	
	/**
	 * GET发送请求
	 * @param urlStr
	 * @param params
	 * @param propertys
	 * @return
	 */
	public HttpResponse sendGet(String urlStr,Map<String,String> params,Map<String,String> propertys) throws IOException{
		return send(urlStr,"GET",params,propertys);
	}
	
	/**
	 * POST发送请求
	 * @param urlStr
	 * @return
	 */
	public HttpResponse sendPost(String urlStr) throws IOException {
		return send(urlStr,"POST",null,null);
	}
	
	/**
	 * POST发送请求
	 * @param urlStr
	 * @return
	 */
	public HttpResponse sendPost(String urlStr,String urlEncode) throws IOException {
		this.urlEncode = urlEncode;
		return send(urlStr,"POST",null,null);
	}
	
	/**
	 * POST发送请求
	 * @param urlStr
	 * @param params
	 * @return
	 */
	public HttpResponse sendPost(String urlStr,Map<String,String> params) throws IOException {
		return send(urlStr,"POST",params,null);
	}
	
	/**
	 * POST发送请求
	 * @param urlStr
	 * @param params
	 * @param propertys
	 * @return
	 */
	public HttpResponse sendPost(String urlStr,Map<String,String> params,Map<String,String> propertys) throws IOException {
		return send(urlStr,"POST",params,propertys);
	}
	
	/**
	 * 发送HTTP请求
	 * @param urlStr
	 * @param method
	 * @param params
	 * @param propertys
	 * @return
	 */
	public HttpResponse send(String urlStr,String method,Map<String,String> params,Map<String,String> propertys)
	throws MalformedURLException,IOException {
		HttpURLConnection urlConnection = null;
		if("GET".equalsIgnoreCase(method) && params != null) {
			StringBuffer param = new StringBuffer();
			int i = 0;
			for(String key:params.keySet()) {
				if(i==0) {
					param.append("?");
				} else {
					param.append("&");
				}
				param.append(key).append("=").append(params.get(key));
				i++;
			}
			//param = URLEncoder.encode("GBK");
			urlStr +=param;
		}
		URL url = new URL(urlStr);
		urlConnection = (HttpURLConnection)url.openConnection();
		//setHeader(urlConnection);
		urlConnection.setRequestMethod(method);
		urlConnection.setDoOutput(true);
		urlConnection.setDoInput(true);
		urlConnection.setUseCaches(false);
		if(propertys != null) {
		    for(String key:propertys.keySet()) {
		    	urlConnection.addRequestProperty(key, propertys.get(key));
		    }
		 }
		
		if("POST".equalsIgnoreCase(method)) {
			StringBuffer param = new StringBuffer();
			int i = 0;
			if(null != params) {
				for(String key:params.keySet()) {
					if(i!=0) {
					   param.append("&");
					}
					param.append(key).append("=").append(params.get(key));
					i++;
				}
				try {
					urlConnection.getOutputStream().write(param.toString().getBytes());
				    urlConnection.getOutputStream().flush();
				    urlConnection.getOutputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return makeContent(urlStr,urlConnection);
	}
	
	
	/**
	 * 获取响应信息(对象)
	 * @param urlStr
	 * @param urlConnection
	 * @return
	 */
	protected HttpResponse makeContent(String urlStr,HttpURLConnection urlConnection) throws IOException {
		HttpResponse httpResponse = new HttpResponse();
		WebEncoding web = new WebEncoding();
		if(StringUtil.isEmpty(urlEncode)) {
			urlEncode = web.getCharset(urlStr).toUpperCase();
		}
		if(urlEncode != null) {
			if(urlEncode.trim().startsWith("GB")) {
				urlEncode = "GBK";
			}
		}
        InputStream in = urlConnection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,urlEncode));
		Vector<String> collection = new Vector<String>();
		StringBuffer temp = new StringBuffer();
		String line = reader.readLine();
		while(line != null) {
			collection.add(line);
			temp.append(line).append("\r\n");
			line = reader.readLine();
		}
		reader.close();
		httpResponse.setContentCollection(collection);
		String encode = urlConnection.getContentEncoding();
		if(encode == null) {
			encode = this.urlEncode;
		}
		httpResponse.setUrlStr(urlStr);
		httpResponse.setDefaultPort(urlConnection.getURL().getDefaultPort());
		httpResponse.setFile(urlConnection.getURL().getFile());
		httpResponse.setHost(urlConnection.getURL().getHost());
		httpResponse.setPath(urlConnection.getURL().getPath());
		httpResponse.setPort(urlConnection.getURL().getPort());
		httpResponse.setProtocol(urlConnection.getURL().getProtocol());
		httpResponse.setQuery(urlConnection.getURL().getQuery());
		httpResponse.setRef(urlConnection.getURL().getRef());
		httpResponse.setUserInfo(urlConnection.getURL().getUserInfo());
		httpResponse.setContent(temp.toString());
		httpResponse.setContentEncoding(encode);
		httpResponse.setCode(urlConnection.getResponseCode());
		httpResponse.setMessage(urlConnection.getResponseMessage());
		httpResponse.setContentType(urlConnection.getContentType());
		httpResponse.setMethod(urlConnection.getRequestMethod());
		httpResponse.setConnectTimeout(urlConnection.getConnectTimeout());
		httpResponse.setReadTimeout(urlConnection.getReadTimeout());
		
		if(urlConnection != null) {
		  urlConnection.disconnect();
		}
		return httpResponse;
	}
	
	
	public void setHeader(URLConnection conn) {
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.3) Gecko/2008092510 Ubuntu/8.04 (hardy) Firefox/3.0.3");
        conn.setRequestProperty("Accept-Language", "en-us,en;q=0.7,zh-cn;q=0.3");
        conn.setRequestProperty("Accept-Encoding", "utf-8");
        conn.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        conn.setRequestProperty("Keep-Alive", "300");
        conn.setRequestProperty("connnection", "keep-alive");
        conn.setRequestProperty("If-Modified-Since", "Fri, 02 Jan 2009 17:00:05 GMT");
        conn.setRequestProperty("If-None-Match", "\"1261d8-4290-df64d224\"");
        conn.setRequestProperty("Cache-conntrol", "max-age=0");
        conn.setRequestProperty("Referer", "http://www.baidu.com");
    }

}
