package com.zjlp.face.ums.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharArrayBuffer;
import org.apache.log4j.Logger;

import com.zjlp.face.ums.exception.UmsException;
import com.zjlp.face.ums.service.UmsService;



/**
 * @ClassName: UmsUtil 
 * @Description: (短信接口) 
 * @author Administrator
 * @date 2014年5月16日 下午5:40:42
 */
public class UmsUtil {
	private static Logger _logger = Logger.getLogger("umsInfoLog");
	public static String TRUE = "true";
	public static String FALSE = "false";
	public static String FLAG = "flag";
	private static String GBK = "gbk";
	private static HttpClient client = null;
	
	private static final String url = PropertiesUtil.getContexrtParam("ums.url");
	private static String spCode = PropertiesUtil.getContexrtParam("ums.spcode");
	private static String loginName = PropertiesUtil.getContexrtParam("ums.loginname");
	private static String password = PropertiesUtil.getContexrtParam("ums.password");
	private static String loginName4audio = PropertiesUtil.getContexrtParam("ums.loginname.audio");
	private static String password4audio = PropertiesUtil.getContexrtParam("ums.password.audio");


	/**
	 * @Title: send 
	 * @Description: (发送短信  result=0&description=发送成功) 
	 * @param mobile		手机号码，群发以","分割
	 * @return	boolean
	 * @date 2014年5月16日 下午5:46:22  
	 * @author Administrator
	 */
	public static boolean send(String messages,String mobile) throws UmsException{
		try {
			client = new DefaultHttpClient();
			//建立HttpPost对象
			HttpPost httpPost = createPost(messages, mobile);
			//发送Post,并返回一个HttpResponse对象  
			HttpResponse response=client.execute(httpPost);
			//如果状态码是200，则正常返回
			Map<String, String> map = passer(response);
			return TRUE.equals(map.get(FLAG));
		}catch (Exception e) {
			throw new UmsException("发送短信异常",e);
		}finally{
			if(null != client){
				client.getConnectionManager().shutdown();
			}
		}
	}
	
	public static String sendByJson(String messages, String mobile,Integer mediaType) throws UmsException{
		try {	
			client = new DefaultHttpClient();
			HttpPost httpPost =null;
			//建立HttpPost对象
			if(UmsService.MEDIA_TYPE_SMS.equals(mediaType)){
				 httpPost = createPost(messages, mobile);	
			}else if(UmsService.MEDIA_TYPE_AUDIO.equals(mediaType)){
				 httpPost = createPost4Audio(messages, mobile);	
			}else{
				_logger.error("[sendByJson]不支持 此类型的发送：mediaType"+mediaType);
				return null;
			}
			//发送Post,并返回一个HttpResponse对象  
			HttpResponse response=client.execute(httpPost);
			//如果状态码是200，则正常返回
			Map<String, String> map = passer(response);
			return JSONObject.fromObject(map).toString();
		}catch (Exception e) {
			throw new UmsException("发送短信异常",e);
		}finally{
			if(null != client){
				client.getConnectionManager().shutdown();
			}
		}
	}
	
	private static HttpPost createPost(String messages,String mobile) 
			throws UnsupportedEncodingException{
		HttpPost httpPost=new HttpPost(url);
		//建立一个NameValuePair数组，用于存储欲传递的参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		//添加参数
		nvps.add(new BasicNameValuePair("SpCode", spCode));
		nvps.add(new BasicNameValuePair("LoginName", loginName));
		nvps.add(new BasicNameValuePair("Password", password));
		nvps.add(new BasicNameValuePair("MessageContent", messages));
		nvps.add(new BasicNameValuePair("UserNumber", mobile));
		nvps.add(new BasicNameValuePair("SerialNumber",""));
		nvps.add(new BasicNameValuePair("ScheduleTime", ""));
		nvps.add(new BasicNameValuePair("ExtendAccessNum", ""));
		nvps.add(new BasicNameValuePair("f", "1"));
		//设置编码
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, GBK));
		return httpPost;
	}
	private static HttpPost createPost4Audio(String messages,String mobile) 
			throws UnsupportedEncodingException{
		HttpPost httpPost=new HttpPost(url);
		//建立一个NameValuePair数组，用于存储欲传递的参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		//添加参数
		nvps.add(new BasicNameValuePair("SpCode", spCode));
		nvps.add(new BasicNameValuePair("LoginName", loginName4audio));
		nvps.add(new BasicNameValuePair("Password", password4audio));
		nvps.add(new BasicNameValuePair("MessageContent", messages));
		nvps.add(new BasicNameValuePair("UserNumber", mobile));
		nvps.add(new BasicNameValuePair("SerialNumber",""));
		nvps.add(new BasicNameValuePair("ScheduleTime", ""));
		nvps.add(new BasicNameValuePair("ExtendAccessNum", ""));
		nvps.add(new BasicNameValuePair("f", "1"));
		//设置编码
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, GBK));
		return httpPost;
	}
	
	private static Map<String,String> passer(HttpResponse response) 
			throws Exception {
		Map<String, String> map = null;
		if (null != response && response.getStatusLine().getStatusCode()==200) {
			//如果是下载的文件，可以用response.getEntity().getContent返回InputStream
			//获得返回的字符串
			String result=getMessage(response.getEntity(), Charset.forName(GBK));
			_logger.info(result);
			map = _convert(result);
			map.put(FLAG, "0".equals(map.get("result")) ? TRUE : FALSE);
		} else {
			map = new HashMap<String, String>();
			map.put(FLAG, FALSE);
		}
		return map;
	}

	/**
	 * 转换响应参数
	 * @Title: _convert 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param str
	 * @return
	 * @throws UmsException
	 * @date 2014年6月9日 下午1:46:34  
	 * @author Administrator
	 */
	private static Map<String,String> _convert(String str)throws UmsException{
		Map<String,String> map = new HashMap<String,String>();
		try {
			String[] strs = str.split("&");
			for (String s : strs) {
				String[] ss= s.split("=");
				if(ss.length == 2){
					map.put(ss[0], ss[1]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new UmsException("转换响应参数异常");
		}
		return map;
	}
	
	 public static String getMessage(
	            final HttpEntity entity, final Charset charset) throws IOException, ParseException {
	        if (entity == null) {
	            throw new IllegalArgumentException("HTTP entity may not be null");
	        }
	        InputStream instream = entity.getContent();
	        if (instream == null) {
	            return null;
	        }
	        try {
	            if (entity.getContentLength() > Integer.MAX_VALUE) {
	                throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
	            }
	            int i = (int)entity.getContentLength();
	            if (i < 0) {
	                i = 4096;
	            }
	            Reader reader = new InputStreamReader(instream, charset);
	            CharArrayBuffer buffer = new CharArrayBuffer(i);
	            char[] tmp = new char[1024];
	            int l;
	            while((l = reader.read(tmp)) != -1) {
	                buffer.append(tmp, 0, l);
	            }
	            return buffer.toString();
	        } finally {
	            instream.close();
	        }
	    }
}
