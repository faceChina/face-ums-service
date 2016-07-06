package com.zjlp.face.ums.util;

public enum ErrorCode {
	
	code_101(101,"无此用户"),
	code_102(102,"密码错"),
	code_103(103,"提交过快（提交速度超过流速限制）"),
	code_104(104,"系统忙（因平台侧原因，暂时无法处理提交的短信）"),
	code_105(105,"敏感短信（短信内容包含敏感词）"),
	code_106(106,"消息长度错（>536或<=0）"),
	code_107(107,"包含错误的手机号码"),
	code_108(108,"手机号码个数错（群发>50000或<=0;单发>200或<=0）"),
	code_109(109,"无发送额度（该用户可用短信数已使用完）"),
	code_110(110,"不在发送时间内"),
	code_111(111,"超出该账户当月发送额度限制"),
	code_112(112,"无此产品，用户没有订购该产品"),
	code_113(113,"extno格式错（非数字或者长度不对）"),
	code_115(115,"自动审核驳回"),
	code_116(116,"签名不合法，未带签名（用户必须带签名的前提下）"),
	code_117(117,"IP地址认证错,请求调用的IP地址不是系统登记的IP地址"),
	code_118(118,"用户没有相应的发送权限"),
	code_119(119,"用户已过期");
	
	private ErrorCode(int errorCode, String errorMesage) {
		this.errorCode = errorCode;
		this.errorMesage = errorMesage;
	}
	private int errorCode;
	private String errorMesage;
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMesage() {
		return errorMesage;
	}
	public void setErrorMesage(String errorMesage) {
		this.errorMesage = errorMesage;
	}
}
