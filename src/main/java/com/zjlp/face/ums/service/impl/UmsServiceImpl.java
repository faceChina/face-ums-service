package com.zjlp.face.ums.service.impl;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.zjlp.face.ums.service.UmsService;
import com.zjlp.face.ums.util.PropertiesUtil;
import com.zjlp.face.ums.util.UmsUtil;

public class UmsServiceImpl implements UmsService {

	private Logger _logger = Logger.getLogger("umsInfoLog");
	
	private Logger _errorLogger  = Logger.getLogger("umsErrorLog");
	
	@Override
	public boolean send(String[] messages, String modelId, String mobile) {
		try{
			_logger.info("{\"messages\":\""+JSONArray.fromObject(messages).toString()+
					"\",\"modelId\":\""+modelId+"\",\"mobile\":\""+mobile+"\"}");
			String model = PropertiesUtil.getContexrtParam(
							new StringBuilder("ums.model.").append(modelId).toString());
			for (int i = 0; i < messages.length; i++) {
				model = model.replace(new StringBuilder("#arg").append(i).append("#").toString(), messages[i]);
			}
			_logger.info(new StringBuffer("短信内容：").append(model).toString());
			boolean bool = UmsUtil.send(model, mobile);
			_logger.info(new StringBuffer("发送短信返回结果：").append(bool).toString());
			return bool;
		} catch (Exception e) {
			_errorLogger.error("{\"err_info\":\""+e.getMessage()+"\",\"err_data\":{\"messages\":\""+JSONArray.fromObject(messages).toString()+
					"\",\"modelId\":\""+modelId+"\",\"mobile\":\""+mobile+"\"}}",e);
			return false;
		}
	}

	@Override
	public String sendByJson4Audio(String[] messages,
			String mobile) {
		try{
			_logger.info("{\"messages\":\""+JSONArray.fromObject(messages).toString()+
					"\",\"modelId\":\""+UmsService.SEND_AUDIO_MODEL+"\",\"mobile\":\""+mobile+"\"}");
			String model = PropertiesUtil.getContexrtParam(
							new StringBuilder("ums.model.").append(UmsService.SEND_AUDIO_MODEL).toString());
			for (int i = 0; i < messages.length; i++) {
				model = model.replace(new StringBuilder("#arg").append(i).append("#").toString(), messages[i]);
			}
			_logger.info(new StringBuffer("语音内容：").append(model).toString());
			String json = UmsUtil.sendByJson(model, mobile,UmsService.MEDIA_TYPE_AUDIO);
			_logger.info(new StringBuffer("发送语音返回结果：").append(json).toString());
			return json;
		} catch (Exception e) {
			_errorLogger.error("{\"err_info\":\""+e.getMessage()+"\",\"err_data\":{\"messages\":\""+JSONArray.fromObject(messages).toString()+
					"\",\"modelId\":\""+UmsService.SEND_AUDIO_MODEL+"\",\"mobile\":\""+mobile+"\"}}",e);
			Map<String, String> map = new HashMap<String, String>();
			map.put(UmsUtil.FLAG, UmsUtil.FALSE);
			return JSONObject.fromObject(map).toString();
		}
	
	}

	@Override
	public String sendByJson(String[] messages, String modelId, String mobile) {
		try{
			_logger.info("{\"messages\":\""+JSONArray.fromObject(messages).toString()+
					"\",\"modelId\":\""+modelId+"\",\"mobile\":\""+mobile+"\"}");
			String model = PropertiesUtil.getContexrtParam(
							new StringBuilder("ums.model.").append(modelId).toString());
			for (int i = 0; i < messages.length; i++) {
				model = model.replace(new StringBuilder("#arg").append(i).append("#").toString(), messages[i]);
			}
			_logger.info(new StringBuffer("短信内容：").append(model).toString());
			String json = UmsUtil.sendByJson(model, mobile,UmsService.MEDIA_TYPE_SMS);
			_logger.info(new StringBuffer("发送短信返回结果：").append(json).toString());
			return json;
		} catch (Exception e) {
			_errorLogger.error("{\"err_info\":\""+e.getMessage()+"\",\"err_data\":{\"messages\":\""+JSONArray.fromObject(messages).toString()+
					"\",\"modelId\":\""+modelId+"\",\"mobile\":\""+mobile+"\"}}",e);
			Map<String, String> map = new HashMap<String, String>();
			map.put(UmsUtil.FLAG, UmsUtil.FALSE);
			return JSONObject.fromObject(map).toString();
		}
	}

}
