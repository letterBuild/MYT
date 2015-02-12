package com.yihu.myt.service.service.impl;

import com.common.json.JSONException;
import com.common.json.JSONObject;
import com.yihu.myt.dao.PostDAO;
import com.yihu.myt.service.service.IPostService;


public class PostService implements IPostService {

	// 接口地址
	private static final String peoplejqUrl = "http://116.228.55.5:8180/bstHealth/portal.do?functionid=30001";
	private static final String AddUserInfoUrl = "http://116.228.55.5:8180/bstHealth/portal.do?functionid=30002";
	private static final String FindUserInfoUrl = "http://116.228.55.5:8180/bstHealth/portal.do?functionid=30003";
	private static final String userAddFamliyUrl = "http://116.228.55.5:8180/bstHealth/portal.do?functionid=30004";
	private static final String userFindFamliyUrl = "http://116.228.55.5:8180/bstHealth/portal.do?functionid=30005";
	private static final String updateUserInfoUrl = "http://116.228.55.5:8180/bstHealth/portal.do?functionid=30006";
	private static final String updateUserFamliyUrl = "http://116.228.55.5:8180/bstHealth/portal.do?functionid=30007";
	
	private static final String regwaterUrl = "http://116.228.55.5:8180/bstHealth/portal.do?functionid=30008";
	private static final String getHospitalUrl = "http://116.228.55.5:8180/bstHealth/portal.do?functionid=30009";
	private static final String getdeptbygjzUrl = "http://116.228.55.5:8180/bstHealth/portal.do?functionid=30010";
	private static final String getDeptByHospitalNoUrl = "http://116.228.55.5:8180/bstHealth/portal.do?functionid=30011";
	private static final String getDoctorBygjz = "http://116.228.55.5:8180/bstHealth/portal.do?functionid=30012";
	private static final String getDoctorByDeptNoUrl = "http://116.228.55.5:8180/bstHealth/portal.do?functionid=30013";
	private static final String getDcotorInfoUrl = "http://116.228.55.5:8180/bstHealth/portal.do?functionid=30014";
	private static final String getdoctorSyHyUrl = "http://116.228.55.5:8180/bstHealth/portal.do?functionid=30015";
	private static final String YyghUrl = "http://116.228.55.5:8180/bstHealth/portal.do?functionid=30016";
	private static final String qxghUrl = "http://116.228.55.5:8180/bstHealth/portal.do?functionid=30017";
	
	private static final String delFamliyUrl = "http://116.228.55.5:8180/bstHealth/portal.do?functionid=30019";
	
	
	private static final String getMytUserCount = "http://116.228.55.5:8180/bstHealth/portal.do?functionid=50001";
	private static final String changeMytUserCount = "http://116.228.55.5:8180/bstHealth/portal.do?functionid=50002";
	/*
	private static final String changePostService = "http://10.0.1.2:8080/WSGW/rest";
	private static final String changePostServiceTest = "http://10.0.100.133:8081/WSGW/rest";*/

/*
	private static final String changePostService = "http://10.0.1.2:8080/WSGW/rest";
	private static final String changePostServiceTest = "http://10.0.100.133:8081/WSGW/rest";
*/
    private static final String changePostService = "http://172.18.20.123:8081/WSGW/rest";
	private static final String changePostServiceTest = "http://172.18.20.123:8081/WSGW/rest";
 
	private static final String ThirtyNinePostService = "http://askapi.39.net/";
	private static final String ThirtyNinePostServiceTest = "http://219.238.238.73:3938";
	//http://172.18.20.123:8081/
 	
	@Override
	public String telecounSelingUpdate(String type,String code){
		String api = "/apiv2/telecounseling/updateorder";
		//JSONObject param = new JSONObject();
		String content = "";
		content = content +  "?appkey="+"yihu";
		content = content +  "&appsecret="+"190ec8dc3ecd1cecda367c27535848e3";
		content = content +  "&statuscode="+type;
		content = content +  "&orderno="+code;
		System.out.println(ThirtyNinePostService+api+content);
		String userinfo = PostDAO.dopost(ThirtyNinePostService+api +content ,"" );
		System.out.println(userinfo);
		return userinfo;
	}

	@Override
	public String getDoctorDiseaseListTest(String doctorUid){
		String api = "baseinfo.DoctorInfoApi.queryDiseaseListByDoctorUid";
		JSONObject param = new JSONObject();
		try {
			param.put("doctorUid", doctorUid);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String content = "&Api=" + api +"&Param="+ param.toString();
		//System.out.println(changePostServiceTest+content);
		String userinfo = PostDAO.dopost(changePostServiceTest, content);
		return userinfo;
	}
	//account.UserAccount.getResourceCount
	@Override
	public String getResourceCount(String Serviceproviderid,String Cardid,String Productno,String Feeno){
		String api = "account.UserAccount.getResourceCount";
		JSONObject param = new JSONObject();
		try {
			param.put("Serviceproviderid", Serviceproviderid);
			param.put("Cardid", Cardid);
			param.put("Productno", Productno);
			param.put("Feeno", Feeno);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String content = "&Api=" + api +"&Param="+ param.toString();
		System.out.println(changePostService+content);
		String testPOST = "http://10.0.100.133:8081/WSGW/rest";
		//String userinfo = PostDAO.dopost(changePostService, content);
		String userinfo = PostDAO.dopost(changePostService, content);
		return userinfo;
	}
	@Override
	public String charge(String Cardid,String Feesn,String Relateid,String Operatorid,String Operatorname ,String Relatetype
			,String Ispermitowe,String Remark,String Cash,String Resource,String Serviceproviderid,String OutType, String ParamType, String AuthInfo){
		String api = "account.UserAccount.charge";
		JSONObject param = new JSONObject();
		try {
			param.put("Cardid", Cardid);
			param.put("Feesn", Feesn);
			param.put("Relateid", Relateid);
			param.put("Operatorid", Operatorid);
			param.put("Operatorname",Operatorname);
			param.put("Ispermitowe", Ispermitowe);
			param.put("Remark",  Remark);
			param.put("Cash", Cash);
			param.put("Resource", Resource);
			param.put("Serviceproviderid", Serviceproviderid);
			param.put("ParamType", ParamType);
			param.put("OutType", OutType);
			param.put("Relatetype", Relatetype);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String content = "&Api=" + api +"&Param="+ param.toString()+ "&AuthInfo="+AuthInfo;
		System.out.println(changePostService+content);
		
		String testPOST = "http://10.0.100.133:8081/WSGW/rest";
		//String userinfo = PostDAO.dopost(changePostService, content);
		String userinfo = PostDAO.dopost(changePostService, content);
		return userinfo;
	}
	@Override
	public String frozenWswyFee(String Accountsn,String Serviceid,String Frozenfee,String Productno,String Feeno
			,String Feename,String Relatesign ,String Operatorid,String Operatorname , String Serviceproviderid,
			String OutType , String ParamType,String AuthInfo){
		String api = "account.UserAccount.frozenWswyFee";
		JSONObject param = new JSONObject();
		try {
			param.put("Accountsn", Accountsn);
			param.put("Serviceid", Serviceid);
			param.put("Frozenfee", Frozenfee);
			param.put("Productno", Productno);
			param.put("Feeno", Feeno);
			param.put("Feename",Feename);
			param.put("Relatesign", Relatesign);
			param.put("Operatorid", Operatorid);
			param.put("Operatorname", Operatorname);
			param.put("Serviceproviderid", Serviceproviderid);
			param.put("ParamType", ParamType);
			param.put("OutType", OutType);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String content = "&Api=" + api +"&Param="+ param.toString()+ "&AuthInfo="+AuthInfo;
		String testPOST = "http://10.0.100.133:8081/WSGW/rest";
		//String userinfo = PostDAO.dopost(changePostService, content);
		String userinfo = PostDAO.dopost(changePostService, content);
		return userinfo;
	}
	@Override
	public String getDoctorDiseaseList(String doctorUid){
		String api = "baseinfo.DoctorInfoApi.queryDiseaseListByDoctorUid";
		JSONObject param = new JSONObject();
		try {
			param.put("doctorUid", doctorUid);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String content = "&Api=" + api +"&Param="+ param.toString();
		System.out.println(changePostService+content);
		String userinfo = PostDAO.dopost(changePostService, content);
		return userinfo;
	}
	@Override
	public String queryComplexDoctorList_v2(String param){
		String api = "baseinfo.DoctorInfoApi.queryComplexDoctorList_v2";
		String content = "&Api=" + api +"&Param="+ param;
		System.out.println(changePostService+content);
		String userinfo = PostDAO.dopost(changePostService, content);
		return userinfo;
	}
	public String queryComplexDoctorList(String param){
		String api = "baseinfo.DoctorInfoApi.queryComplexDoctorList";
		String content = "&Api=" + api +"&Param="+ param;
		System.out.println(changePostService+content);
		String userinfo = PostDAO.dopost(changePostService, content);
		return userinfo;
	}
	//二级科室 差一级
	public String getBigAndSmallDepartmentBySmallDepartmentId(String param){
		String api = "baseinfo.CommonApi.getBigAndSmallDepartmentBySmallDepartmentId";
		String content = "&Api=" + api +"&Param="+ param;
		System.out.println(changePostService+content);
		String userinfo = PostDAO.dopost(changePostService, content);
		return userinfo;
	}
	
	
	//二级科室 差一级
	public String queryFeeTemplateId(String param){
		String api = "baseinfo.DoctorServiceApi.queryFeeTemplateId";
		String content = "&Api=" + api +"&Param="+ param;
		System.out.println(changePostService+content);
		String userinfo = PostDAO.dopost(changePostServiceTest, content);
		return userinfo;
	}
	
	
	
	@Override
	public String insertBill(String param){
		String api = "baseinfo.DoctorServiceApi.insertBill";
		String content = "&Api=" + api +"&Param="+ param;
		System.out.println(changePostService+content);
		String userinfo = PostDAO.dopost(changePostServiceTest, content);
		return userinfo;
	}
	@Override
	public String sendMsg(String param){
		String api = "arrownock.IMApiImpl.sendMsg";
		String content = "&Api=" + api +"&Param="+ param;
		System.out.println(changePostService+content);
		String str = PostDAO.dopost(changePostService, content);
		return str;
	}
	@Override
	public String sendMsgJ(String param){
		String api = "jpush.MsgApi.sendMsg";
		String content = "&Api=" + api +"&Param="+ param;
		System.out.println(changePostService+content);
		String str = PostDAO.dopost(changePostService, content);
		return str;
	}
	@Override
	public String getMytUserCount(String userPhone){
		String content = "&cellphone="+ userPhone;
		System.out.println(getMytUserCount+content);
		String userinfo = PostDAO.dopost(getMytUserCount, content);
		return userinfo;
	}
	@Override
	public String changeMytUserCount(String userPhone){
		String content = "&cellphone="+ userPhone;
		System.out.println(changeMytUserCount+content);
		String userinfo = PostDAO.dopost(changeMytUserCount, content);
		return userinfo;
	}
	@Override
	public String addUserInfo(String UserId, String UserName, String UserSex,
			String UserCardId, String UserPhone) {
		// TODO Auto-generated method stub
		String content = "&UserId=" + UserId + "&UserName=" + UserName + "&UserSex="
				+ UserSex + "&UserCardId=" + UserCardId + "&UserPhone="
				+ UserPhone;
		System.out.println(AddUserInfoUrl+content);
		String userinfo = PostDAO.dopost(AddUserInfoUrl, content);
		return userinfo;
	}
	
	@Override
	public String findUserInfo(String UserId) {
		// TODO Auto-generated method stub
		String content = "&UserId=" + UserId;
		System.out.println(FindUserInfoUrl+content);
		String userinfo = PostDAO.dopost(FindUserInfoUrl, content);
		return userinfo;
	}
	
	@Override
	public String updateUserFamliy(String MemberId, String MemberName,
			String MemberSex, String MemberCardId, String MemberPhone,
			String MemberRelation, String MemberAddress, String MemberEmail) {
		// TODO Auto-generated method stub
		String content = "&MemberId=" + MemberId + "&MemberName=" + MemberName + "&MemberSex="
				+ MemberSex + "&MemberCardId=" + MemberCardId + "&MemberPhone="
				+ MemberPhone+"&MemberRelation="+MemberRelation+"&MemberAddress="+MemberAddress+"&MemberEmail="+MemberEmail;
		System.out.println(updateUserFamliyUrl+content);
		String userFamliy = PostDAO.dopost(updateUserFamliyUrl, content);
		return userFamliy;
	}
	
	@Override
	public String updateUserInfo(String UserId, String UserName,
			String UserSex, String UserCardId, String UserPhone) {
		// TODO Auto-generated method stub
		String content = "&UserId=" + UserId + "&UserName=" + UserName + "&UserSex="
				+ UserSex + "&UserCardId=" + UserCardId + "&UserPhone="
				+ UserPhone;
		System.out.println(updateUserInfoUrl+content);
		String userinfo = PostDAO.dopost(updateUserInfoUrl, content);
		return userinfo;
	}
	
	@Override
	public String userAddFamliy(String UserId, String MemberName,
			String MemberSex, String MemberCardId, String MemberPhone,
			String MemberRelation, String MemberAddress, String MemberEmail) {
		// TODO Auto-generated method stub
		String content = "&UserId=" + UserId + "&MemberName=" + MemberName + "&MemberSex="
				+ MemberSex + "&MemberCardId=" + MemberCardId + "&MemberPhone="
				+ MemberPhone+"&MemberRelation="+MemberRelation+"&MemberAddress="+MemberAddress+"&MemberEmail="+MemberEmail;
		System.out.println(userAddFamliyUrl+content);
		String userinfo = PostDAO.dopost(userAddFamliyUrl, content);
		return userinfo;
	}
	
	@Override
	public String userFindFamliy(String UserId) {
		// TODO Auto-generated method stub
		String content = "&UserId=" + UserId;
		System.out.println(userFindFamliyUrl+content);
		String userFamliy = PostDAO.dopost(userFindFamliyUrl, content);
		return userFamliy;
	}
	
	@Override
	public String userJq(String Cellphone, String CityCode) {
		// TODO Auto-generated method stub
		String content = "&Cellphone="+Cellphone+"&CityCode="+CityCode;
		System.out.println(peoplejqUrl+content);
		String jqinfo = PostDAO.dopost(peoplejqUrl, content);
		return jqinfo;
	}
	
	@Override
	public String getHospital(String GUID, String AreaId, String Keyword,
			String CurrentPage, String PageSize) {
		// TODO Auto-generated method stub
		String content = "&GUID=" + GUID + "&AreaId=" + AreaId + "&Keyword="
				+ Keyword + "&CurrentPage=" + CurrentPage + "&PageSize="
				+ PageSize;
		System.out.println(getHospitalUrl + content);
		String hospitalinfo = PostDAO.dopost(getHospitalUrl, content);
		return hospitalinfo;
	}

	@Override
	public String getdeptbygjz(String GUID, String AreaId, String Keyword,
			String CurrentPage, String PageSize) {
		// TODO Auto-generated method stub
		String content = "&GUID=" + GUID + "&AreaId=" + AreaId + "&Keyword="
				+ Keyword + "&CurrentPage=" + CurrentPage + "&PageSize="
				+ PageSize;
		System.out.println(getdeptbygjzUrl + content);
		String deptinfo = PostDAO.dopost(getdeptbygjzUrl, content);
		return deptinfo;
	}

	@Override
	public String getDeptByHospitalNo(String GUID, String HospitalId,
			String CPId, String CurrentPage, String PageSize) {
		// TODO Auto-generated method stub
		String content = "&GUID=" + GUID + "&HospitalId=" + HospitalId
				+ "&CPId=" + CPId + "&CurrentPage=" + CurrentPage
				+ "&PageSize=" + PageSize;
		System.out.println(getDeptByHospitalNoUrl + content);
		String deptinfo = PostDAO.dopost(getDeptByHospitalNoUrl, content);
		return deptinfo;
	}

	@Override
	public String getDoctorBygjz(String GUID, String AreaId, String Keyword,
			String CurrentPage, String PageSize) {
		// TODO Auto-generated method stub
		String content = "&GUID=" + GUID + "&AreaId=" + AreaId + "&Keyword="
				+ Keyword + "&CurrentPage=" + CurrentPage + "&PageSize="
				+ PageSize;
		System.out.println(getDoctorBygjz + content);
		String deptinfo = PostDAO.dopost(getDoctorBygjz, content);
		return deptinfo;
	}

	@Override
	public String getDoctorByDeptNo(String GUID, String HospitalId,
			String DeptId, String CPId, String CurrentPage, String PageSize) {
		// TODO Auto-generated method stub
		String content = "&GUID=" + GUID + "&HospitalId=" + HospitalId
				+ "&DeptId=" + DeptId + "&CPId=" + CPId + "&CurrentPage="
				+ CurrentPage + "&PageSize=" + PageSize;
		System.out.println(getDoctorByDeptNoUrl + content);
		String doctorInfo = PostDAO.dopost(getDoctorByDeptNoUrl, content);
		return doctorInfo;
	}

	@Override
	public String getDcotorInfo(String GUID, String HospitalId, String DeptId,
			String DocId, String CPId) {
		// TODO Auto-generated method stub
		String content = "&GUID=" + GUID + "&HospitalId=" + HospitalId
				+ "&DeptId=" + DeptId + "&DocId=" + DocId + "&CPId=" + CPId;
		System.out.println(getDcotorInfoUrl + content);
		String doctorInfo = PostDAO.dopost(getDcotorInfoUrl, content);
		return doctorInfo;
	}

	@Override
	public String getdoctorSyHy(String GUID, String SchemeId, String CPId) {
		// TODO Auto-generated method stub
		String content = "&GUID=" + GUID + "&SchemeId=" + SchemeId + "&CPId="
				+ CPId;
		System.out.println(getdoctorSyHyUrl + content);
		String doctorsyhy = PostDAO.dopost(getdoctorSyHyUrl, content);
		return doctorsyhy;
	}

	@Override
	public String qxgh(String OrderId,String CPId) {
		// TODO Auto-generated method stub
		String content = "&OrderId=" + OrderId + "&CPId="+CPId;
		System.out.println(qxghUrl + content);
		String qxghinfo = PostDAO.dopost(qxghUrl, content);
		PostDAO.writeLog("传入参数:"+qxghUrl + content+"****返回参数:"+qxghinfo);
		return qxghinfo;
	}

	@Override
	public String regWater(String UserId, String SortBy, String CurrentPage,
			String PageSize) {
		// TODO Auto-generated method stub
		String content = "&UserId=" + UserId + "&SortBy=" + SortBy
				+ "&CurrentPage=" + CurrentPage + "&PageSize=" + PageSize;
		System.out.println(regwaterUrl + content);
		String regwaterInfo = PostDAO.dopost(regwaterUrl, content);
		return regwaterInfo;
	}

	//删除家庭成员
	public String delFamliy(String UserId,String MemberId){
		String content = "&UserId=" + UserId + "&MemberId=" + MemberId;
		System.out.println(delFamliyUrl + content);
		String delResult = PostDAO.dopost(delFamliyUrl, content);
		return delResult;
	}

	@Override
	public String addDoctorLove(String param,String AuthInfo) {
		String api = "IntegralSys.LoveValueApi.addDoctorLove";
		String content = "&Api=" + api +"&Param="+ param+ "&AuthInfo="+AuthInfo;
		System.out.println(changePostServiceTest+content);
		String userinfo = PostDAO.dopost(changePostService, content);
		return userinfo;
	}
	
	

	@Override
	public String upfile(String param,String filename){
		String api = "WBJ";
		String content = "&Api=" + api +"&File="+ "true"+"&filename="+filename+"&param="+param;
		System.out.println("http://f1.yihuimg.com/TFS/TFSServlet"+content);
		String userinfo = PostDAO.dopost("http://f1.yihuimg.com/TFS/TFSServlet", content);
		return userinfo;
	}
	
}
