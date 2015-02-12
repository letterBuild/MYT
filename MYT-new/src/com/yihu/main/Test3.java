package com.yihu.main;

import java.awt.List;
import java.util.ArrayList;

import org.apache.commons.collections.comparators.ComparableComparator;
import org.junit.Test;

import net.sf.json.JSONObject;

import com.boss.sdk.ExecutionContext;
import com.yihu.api.impl.EventUtilForMYT;
import com.yihu.api.impl.NetworkConsultingMedicalApiImpl;
import com.yihu.myt.enums.Color;
import com.yihu.myt.job.AskDoctorJob;
import com.yihu.myt.service.service.impl.OpenAuthService;
import com.yihu.myt.service.service.impl.PostService;
import com.yihu.myt.vo.OpenAuthVo;
import com.yihu.wsgw.api.InterfaceMessage;

public class Test3 {
	/*
	
	public static void main(String[] args) {
		NetworkConsultingMedicalApiImpl nm = new NetworkConsultingMedicalApiImpl();
		
		InterfaceMessage  msg = new InterfaceMessage();
		JSONObject json = new JSONObject();
		//json.put("doctorID", "710003065");
		//json.put("queID", 475);
		//json.put("openflag", "O");
		
		json.put("quesType", 1);
	   json.put("doctorUid", 710036823);
//		type=1  710003065
//		type=2 710036823
//		type=3 710036823
//		type=4 710036823
	json.put("pageSize", 5);
	json.put("pageIndex", 6);
		System.out.println(json.toString());
		System.out.println(json.toString());
		msg.setParam(json.toString());
		String  back=nm.getDoctorConsultingListV4(msg);
		System.out.println("返回的数据"+back);
	}
	*/
	@Test
	public void wujiajuntest1(){
		
		NetworkConsultingMedicalApiImpl nma= new NetworkConsultingMedicalApiImpl();
		InterfaceMessage  msg = new InterfaceMessage();
		JSONObject json = new JSONObject();
		
		json.put("department", "0300");
		json.put("pageSize", 5);
		json.put("pageIndex", 1);
		
		msg.setParam(json.toString());
		System.out.println(json);
		String  back=nma.queryQuesMainByDepartmentID(msg);
		System.out.println(back);
		
		
	}
	@Test
	public  void wujiajuntest2(){
		NetworkConsultingMedicalApiImpl nma= new NetworkConsultingMedicalApiImpl();
		InterfaceMessage  msg = new InterfaceMessage();
		JSONObject json = new JSONObject();
		
		json.put("doctorID", 676676);
		json.put("openflag", "C");
		
		msg.setParam(json.toString());
		System.out.println(json);
		String  back=nma.setDocQueOpenOrClose(msg);
		System.out.println(back);

		
		
	}
	
	@Test
	public  void  wujiajuntest3ANDROID(){
		
		
		JSONObject jsonkid = new JSONObject();
		jsonkid.put("title", "10月份月报");
		jsonkid.put("content", "10月总共预约数量 20000 成功咨询数量 10000");
		jsonkid.put("operCode", "13003");
		
		
		jsonkid.put("contentHtml", "10月总共预约数量 <span style=\"color: red;\">20000</span> 成功咨询数量<span style=\"color: red;\"> 10000</span>");
		jsonkid.put("ios", "10月总共预约数量 20000 成功咨询数量 10000<font  size=12 ><b>10月总共预约数量:</b></font> <font size=12 color='#909090'><b>20000</b></font><font  size=12 ><b>成功咨询数量 :</b></font> <font size=12 color='#909090'><b>10000</b></font>");

		JSONObject json = new JSONObject();
		json.put("androidParam", jsonkid.toString());
		System.out.println(json.toString());	
		
		PostService post = new PostService();
		
	}
	
	@Test
	public  void  wujiajuntest4PUSH(){
		
		
		
	    JSONObject iospara = new JSONObject();
	    iospara.put("ti", "电话诊室");
	    iospara.put("content", "1月总共预约数量 4330 成功咨询数量 4262 总分钟数 21976");
	    iospara.put("operCode", "13003");

		//
	    JSONObject androidpara = new JSONObject();
	    androidpara.put("contentHtml", "<font size='4' color='#666666'>1月份总共预约咨询<font size=4 color='#FF9900'>4330</font>人次，总共完成咨询<font size=4 color='#FF9900'>4262</font>人次，总时长：<font size=4 color='#FF9900'>21976</font>分钟</font>");
	    androidpara.put("ios", "<font color='#5b5b5b'  size='16' ><b>1月份总共预约咨询<font  color='#ff8100'> 4330 </font>人次，总共完成咨询<font  color='#ff8100'> 4262 </font>人次，总时长：<font  color='#ff8100'> 21976 </font>分钟</b></font>");
	    androidpara.put("title", "电话诊室");
	    androidpara.put("content", "1月总共预约数量 4330 成功咨询数量 4262 总分钟数 21976");
	    androidpara.put("operCode", "13003");
	    androidpara.put("head", " <center><b><big>1月份 - 月报</big></b></center>");
	    


		JSONObject json = new JSONObject();
		
		PostService post = new PostService();
		
		json.put("iosParam", iospara.toString());
		json.put("androidParam", androidpara.toString());
		json.put("toAppType", 1);
		
		
		
		
		//
		json.put("toUsers", "710040067");
		
		System.out.println("FASONG"+json.toString());
		String  back1 =post.sendMsgJ(json.toString());
		System.out.println(back1);
		
		
		
		
		//
		json.put("toUsers", "710040076");
		
		System.out.println("FASONG"+json.toString());
		String  back2 =post.sendMsgJ(json.toString());
		System.out.println(back2);
		
		//
		
		json.put("toUsers", "710096894");
		
		System.out.println("FASONG"+json.toString());
		String  back3 =post.sendMsgJ(json.toString());
		System.out.println(back3);
		
		
		//
		
		json.put("toUsers", "710107588");
		
		System.out.println("FASONG"+json.toString());
		String  back4 =post.sendMsgJ(json.toString());
		System.out.println(back4);
		

		
	}
	@Test
	public void  wujiajunsavevo() throws Exception{
		
		OpenAuthService  service = new OpenAuthService();
		
		OpenAuthVo vo = new OpenAuthVo();
		vo.setDoctorId("999999");
		vo.setOpenAuthFlag("D");
		vo.setQuesMainId("88888888");
       service.insertOpenAuth(vo);
	
	}
	@Test
	public  void  setDefualOCstatus_test5(){
		NetworkConsultingMedicalApiImpl nma= new NetworkConsultingMedicalApiImpl();
		InterfaceMessage  msg = new InterfaceMessage();
		JSONObject json = new JSONObject();
		
		json.put("doctorID", 2323232);
		json.put("openflag", "C");
		
		msg.setParam(json.toString());
		System.out.println(json);
		String  back=nma.setDefualOCstatus(msg);
		System.out.println(back);
		
		
		
		
	}
	
	
	@Test
	public  void queryDefualOCstatus_test6(){
		NetworkConsultingMedicalApiImpl nma= new NetworkConsultingMedicalApiImpl();
		InterfaceMessage  msg = new InterfaceMessage();
		JSONObject json = new JSONObject();
		
		json.put("doctorID", 888888888);

		
		msg.setParam(json.toString());
		System.out.println(json);
		String  back=nma.queryDefualOCstatus(msg);
		System.out.println(back);

		
		
	}
	
	
	@Test
	public  void  test7_returnandclose(){
		
		NetworkConsultingMedicalApiImpl nma= new NetworkConsultingMedicalApiImpl();
		InterfaceMessage  msg = new InterfaceMessage();
		JSONObject json = new JSONObject();
		
		json.put("queID", 192844);

		
		msg.setParam(json.toString());
		System.out.println(json);
		String  back=nma.returnMoneyAndClose(msg);
		System.out.println(back);

		
	}
	
	
	@Test
	public  void  test8_getQueReplyByQuesIDV2(){
		
		NetworkConsultingMedicalApiImpl nma= new NetworkConsultingMedicalApiImpl();
		InterfaceMessage  msg = new InterfaceMessage();
		JSONObject json = new JSONObject();
		
		json.put("quID", 465);
		json.put("pageSize", 1000);
		json.put("pageIndex", 1);
		json.put("userType", 3);

		
		msg.setParam(json.toString());
		System.out.println(json);
		String  back=nma.getQueReplyByQuesIDV2(msg);
		System.out.println(back);

		
	}
	@Test
	public  void   test9(){
		
		Color c = Color.GREEN;

		System.out.println(		"1111111"+c.name()+c.getName()+c.getIndex());
		
		if(c==Color.BLANK){
			System.err.println("这个是白色");
		}
		if(c==Color.GREEN){
			System.err.println("这个是绿色");
		}
		
	}
	
	
	

	@Test
	public  void  test10_queryQuesByDiseaseID(){
		
		NetworkConsultingMedicalApiImpl nma= new NetworkConsultingMedicalApiImpl();
		InterfaceMessage  msg = new InterfaceMessage();
		JSONObject json = new JSONObject();
		
		json.put("DiseaseId", 266);
		json.put("pageSize", 5);
		json.put("pageIndex", 1);

		
		msg.setParam(json.toString());
		System.out.println(json);
		String  back=nma.queryQuesByDiseaseID(msg);
		System.out.println(back);

		
	}
	
	@Test
	public void   test11(){
		
		String par = "@month  月份  的 总咨询量  @allcount  成功量 @successcount    ";
		
		par= par.replace("@month", "10");
		par=par.replace("@allcount", "10000");
		par	=par.replace("@successcount", "5000");
		System.out.println(par);
		
	}
	
	
	@Test
	public  void  test101111_replyQuertionDoctor(){
		
		NetworkConsultingMedicalApiImpl nma= new NetworkConsultingMedicalApiImpl();
		InterfaceMessage  msg = new InterfaceMessage();
		JSONObject json = new JSONObject();
		
		json.put("platform", 1);
		json.put("userID", 17995036);
		json.put("replyType", 3);
		json.put("queID", 344778);
		json.put("filesCount", 0);
		
		json.put("messCont", "12121212");
		json.put("doctorID", 710122441);
		json.put("interfaceVersion", 1);
		
		json.put("fileMess", "[]");
		
		

		
		msg.setParam(json.toString());
		System.out.println(json);
		String  back=nma.replyQuertionDoctor(msg);
		System.out.println(back);

		
	}
	
	@Test
	public   void  test12bug(){
		
		NetworkConsultingMedicalApiImpl ncmapi= new NetworkConsultingMedicalApiImpl();
		
		
		InterfaceMessage  msg = new InterfaceMessage();
		JSONObject json = new JSONObject();
		
		json.put("platform", 1);
		json.put("userID", 14459707);
		
		json.put("replyType", 0);
		
		json.put("queID", 266806);
		json.put("fileMess", "[]");
		
		json.put("doctorID", 710097499);

		json.put("messCont", "jdkrkrkkrkrkrk");
		
	

		json.put("filesCount", 1);
		
		msg.setParam(json.toString());
		String  back=ncmapi.replyQuertionDoctor(msg);
		System.out.println(back);
		
	}
	
	@Test
	public  void   test13setModels(){
NetworkConsultingMedicalApiImpl ncmapi= new NetworkConsultingMedicalApiImpl();
		
		
		InterfaceMessage  msg = new InterfaceMessage();
		JSONObject json = new JSONObject();
		
		json.put("template", "TFT有");
		json.put("doctorUid", 710040076);
		
		json.put("title", "hhjjhghjj");
		

		
		msg.setParam(json.toString());
		String  back=ncmapi.setModels(msg);
		System.out.println(back);
		
		
		
	}
	
    @Test
	public  void  test14(){
		AskDoctorJob  job = new AskDoctorJob();
		job.qusReturnPremium(new ExecutionContext());

		
	}
    @Test
    public   void  test15(){
    	
    	
    	
    	NetworkConsultingMedicalApiImpl nma= new NetworkConsultingMedicalApiImpl();
		InterfaceMessage  msg = new InterfaceMessage();
		JSONObject json = new JSONObject();
		
		json.put("doctorID", 710040067);
		json.put("openflag", "O");
		
		
		msg.setParam(json.toString());
		System.out.println(json);
		String  back=nma.setDocQueOpenOrClose(msg);
		System.out.println(back);
    	
    }
    @Test
    public  void  test16() throws Exception{
    	OpenAuthService  service = new OpenAuthService();
    	
    	OpenAuthVo  vo = new OpenAuthVo();
    	vo.setQuesMainId("266899");
    	vo.setDoctorId("710097499");
    	vo.setOpenAuthFlag("C");
    	service.updateDocidandopenflagByqueID(vo);
    	
    }
    @Test
    public void  test17() throws Exception{
    	
		String back =EventUtilForMYT.sendEvenForAddlove("710003629","492");
		
		System.out.println(back);
    }




	
	

}
