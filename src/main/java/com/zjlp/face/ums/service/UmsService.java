package com.zjlp.face.ums.service;

public interface UmsService {
	/**
	 * 发送类型 ：短信发送
	 */
	public static Integer MEDIA_TYPE_SMS = 1;
	/**
	 * 发送类型 语音发送
	 */
	public static Integer MEDIA_TYPE_AUDIO = 2;
	/**
	 * 语音发送模板
	 */
	public static Integer SEND_AUDIO_MODEL = 900;
	

	/***
	 * 发送短信
	 * @Title: send 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param messages
	 * @param modelId
	 * @param mobile
	 * @date 2014年8月4日 下午7:17:36  
	 * @author Administrator
	 */
	boolean send(String[] messages,String modelId,String mobile);
	
	/**
	 * 发送短信（返回详细信息）
	 * 
	 * @Title: sendByJson
	 * @Description: (这里用一句话描述这个方法的作用)
	 * @param messages
	 *            填充信息
	 * @param modelId
	 *            信息类型
	 * @param mobile
	 *            手机号码
	 * @return
	 * @date 2015年8月12日 上午10:22:41
	 * @author lys
	 */
	String sendByJson(String[] messages, String modelId, String mobile);
	/**
	 * 发送语音（返回详细信息）
	 * @Title: sendByJson4Audio
	 * @Description: (这里用一句话描述这个方法的作用)
	 * @param messages
	 *            填充信息
	 * @param mobile
	 *            手机号码
	 * @return
	 * @date 2015年11月2日 上午11:22:41
	 * @author: wangzhang
	 */
	String sendByJson4Audio(String[] messages, String mobile);
}
