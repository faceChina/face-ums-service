package com.zjlp.face.ums.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-beans.xml")
@TransactionConfiguration(defaultRollback = false, transactionManager = "jzTransactionManager")
@ActiveProfiles("dev")
public class UmsServiceTest {

	@Autowired
	private UmsService umsService;
	
	@Test
	public void test_209(){                            //尊敬的老板{xx}您店内的订单{xxxxxxxxxxxxxxxxxxxxxxxx}客户已付款{xx}请尽快安排发货{xx}具体请登录刷脸App查看详情{xx}
		umsService.sendByJson(new String[]{"S876586897789"}, "203", "18655015835");
	}
}
