package com.zjlp.face.ums.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.bcloud.msg.http.HttpSender;
import com.zjlp.face.ums.exception.UmsException;
import com.zjlp.face.ums.service.UmsService;



/**
 * @ClassName: UmsUtil 
 * @Description: (短信接口) 
 * @author Administrator
 * @date 2014年5月16日 下午5:40:42
 */
public class LcUmsUtil {
	private static Logger _logger = Logger.getLogger("umsInfoLog");
	public static String TRUE = "true";
	public static String FALSE = "false";
	public static String FLAG = "flag";
	public static String RESULT = "result";
	
	private static final String LC_UMS_URL = PropertiesUtil.getContexrtParam("lc.ums.url");
	private static final String LC_UMS_ACCOUNT = PropertiesUtil.getContexrtParam("lc.ums.account");
	private static final String LC_UMS_PSWD = PropertiesUtil.getContexrtParam("lc.ums.pswd");
	private static final String LC_UMS_ACCOUNT_AUDIO = PropertiesUtil.getContexrtParam("lc.ums.account.audio");
	private static final String LC_UMS_PSWD_AUDIO = PropertiesUtil.getContexrtParam("lc.ums.pswd.audio");
	private static boolean LC_UMS_NEEDSTATUS =true;
	private static final String LC_UMS_EXTNO = null;
	private static final String LC_UMS_PRODUCT = null;
	

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
			
			String returnString = HttpSender.batchSend(LC_UMS_URL, LC_UMS_ACCOUNT, LC_UMS_PSWD, mobile, messages, LC_UMS_NEEDSTATUS, LC_UMS_PRODUCT, LC_UMS_EXTNO);
			Map<String, String> resultMap = _convert(returnString);
			return TRUE.equals(resultMap.get(RESULT));
		}catch (Exception e) {
			throw new UmsException("发送短信异常",e);
		}
	}
	
	public static String sendByJson(String messages, String mobile,Integer mediaType) throws UmsException{
		try {	
			String returnString=null;
			if(UmsService.MEDIA_TYPE_SMS.equals(mediaType)){
				//短信
				returnString = HttpSender.batchSend(LC_UMS_URL, LC_UMS_ACCOUNT, LC_UMS_PSWD, mobile, messages, LC_UMS_NEEDSTATUS, LC_UMS_PRODUCT, LC_UMS_EXTNO);
			}else if(UmsService.MEDIA_TYPE_AUDIO.equals(mediaType)){
				//语音
				returnString = HttpSender.batchSend(LC_UMS_URL, LC_UMS_ACCOUNT_AUDIO, LC_UMS_PSWD_AUDIO, mobile, messages, LC_UMS_NEEDSTATUS, LC_UMS_PRODUCT, LC_UMS_EXTNO);
			}else{
				_logger.error("[sendByJson]不支持 此类型的发送：mediaType"+mediaType);
				return null;
			}
			Map<String, String> resultMap = _convert(returnString);
			return JSONObject.fromObject(resultMap).toString();
		}catch (Exception e) {
			throw new UmsException("【sendByJson】语音、短信发送异常！[messages="+messages+"][mobile="+mobile+"][mediaType(1/2,短信/语音)="+mediaType+"]",e);
		}
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
			String[] strs = str.split("\n");
			
			String[] ss= strs[0].split(",");
			map.put("sendTime", ss[0]);
			map.put("result", ss[1]);
			if ("0".equals(ss[1])) {
				map.put(RESULT, TRUE);
				map.put("resultMsg", "成功");
				map.put("msgid", strs[1]);
				map.put(FLAG,TRUE);
			}else{
				map.put(RESULT, FALSE);
				String code = "code_"+ss[1];
				String msg = ErrorCode.valueOf(code).getErrorMesage();
				map.put("resultMsg",msg);
				map.put("msgid","");
				map.put(FLAG, FALSE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new UmsException("转换响应参数异常");
		}
		return map;
	}
}
