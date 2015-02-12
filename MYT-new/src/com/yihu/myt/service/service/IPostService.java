package com.yihu.myt.service.service;

import com.coreframework.ioc.Impl;
@Impl("com.yihu.myt.service.service.impl.PostService")
public interface IPostService {
	public String queryComplexDoctorList(String param);
	//�û���Ȩ
	public String userJq(String Cellphone,String CityCode);
	
	//�û���Ϣ����
	public String addUserInfo(String UserId,String UserName,String UserSex,String UserCardId,String UserPhone);
	public String queryFeeTemplateId(String param);
	//�û���Ϣ��ѯ
	public String findUserInfo(String UserId);
	
	//�û����Ӽ�ͥ��Ա
	public String userAddFamliy(String UserId,String MemberName,String MemberSex,String MemberCardId,String MemberPhone,String MemberRelation,String MemberAddress,String MemberEmail);
	
	//�û���ѯ��ͥ��Ա
	public String userFindFamliy(String UserId);
	
	//�޸��û���Ϣ
	public String updateUserInfo(String UserId,String UserName,String UserSex,String UserCardId,String UserPhone);
	
	//�޸��û���ͥ��Ա
	public String updateUserFamliy(String MemberId,String MemberName,String MemberSex,String MemberCardId,String MemberPhone,String MemberRelation,String MemberAddress,String MemberEmail);
	
	
	//�õ�ҽԺ��Ϣ
	public String getHospital(String GUID,String AreaId,String Keyword,String CurrentPage,String PageSize);
	
	//���ݹؼ��ֲ�ѯ������Ϣ
	public String getdeptbygjz(String GUID,String AreaId,String Keyword,String CurrentPage,String PageSize);
	
	//����ҽԺ�����ѯ������Ϣ
	public String getDeptByHospitalNo(String GUID,String HospitalId,String CPId,String CurrentPage,String PageSize);
	
	//���ݹؼ��ֲ�ѯҽ����Ϣ
	public String getDoctorBygjz(String GUID,String AreaId,String Keyword,String CurrentPage,String PageSize);
	
	//���ݿ��ұ����ѯҽ����Ϣ
	public String getDoctorByDeptNo(String GUID,String HospitalId,String DeptId,String CPId,String CurrentPage,String PageSize);
	
	//��ѯҽ���Ű���Ϣ
	public String getDcotorInfo(String GUID,String HospitalId,String DeptId,String DocId,String CPId);
	
	//��ѯҽ�����ú�Դ��Ϣ
	public String getdoctorSyHy(String GUID,String SchemeId,String CPId);
	
/*	//ԤԼ�ҺŵǼ�
	public String Yygh(userInfo info);*/
	
	//ȡ��ԤԼ�Һ�
	public String qxgh(String OrderId,String CPId);
	
	//�û�ԤԼ�굥��ѯ
	public String regWater(String UserId,String SortBy,String CurrentPage,String PageSize);
	
	//ɾ����ͥ��Ա
	public String delFamliy(String UserId,String MemberId);
	
	//�û�ҽ����ѯ�ж�
	public String getMytUserCount(String userPhone);
	//�û�ҽ����ѯ����
	String changeMytUserCount(String userPhone);

	String getDoctorDiseaseListTest(String doctorUid);

	String getDoctorDiseaseList(String doctorUid);


	String telecounSelingUpdate(String type, String code);
	//�û�ҽ����ѯ����

	public String queryComplexDoctorList_v2(String param);
	//�������� ��ѯһ��
	public String getBigAndSmallDepartmentBySmallDepartmentId(String param);

	String sendMsg(String param);

	String sendMsgJ(String param);


	String getResourceCount(String Serviceproviderid, String Cardid,
			String Productno, String Feeno);


	String charge(String Cardid, String Feesn, String Relateid,
			String Operatorid, String Operatorname, String Relatetype,
			String Ispermitowe, String Remark, String Cash, String Resource,
			String Serviceproviderid,String OutType, String ParamType, String AuthInfo);

	String frozenWswyFee(String Accountsn, String Serviceid, String Frozenfee,
			String Productno, String Feeno, String Feename, String Relatesign,
			String Operatorid, String Operatorname, String Serviceproviderid,
			String OutType, String ParamType, String AuthInfo);

	String insertBill(String param);
	String upfile(String param,String filename);
	String addDoctorLove(String param,String AuthInfo);
 
}