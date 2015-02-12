package com.yihu.api.impl;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.boss.sdk.OperatorInfo;
import com.coreframework.db.DB;
import com.coreframework.db.JdbcConnection;
import com.coreframework.ioc.Ioc;
import com.coreframework.log.LogBody;
import com.coreframework.log.Logger;
import com.coreframework.remoting.reflect.Rpc;
import com.coreframework.remoting.standard.DateOper;
import com.coreframework.vo.ReturnValue;
import com.coreframework.vo.ServiceResult;
import com.yihu.account.api.AccMembershipcardBean;
import com.yihu.account.api.BalanceReturnBean;
import com.yihu.account.api.IAccountService;
import com.yihu.account.api.ReturnValueBean;
import com.yihu.api.MsgApi;
import com.yihu.api.MsgManagerService;
import com.yihu.api.api.AskDoctorQuestionApi;
import com.yihu.api.api.DoctorBillDataApi;
import com.yihu.api.api.NetworkConsultingMedicalApi;
import com.yihu.baseinfo.api.CommonApi;
import com.yihu.baseinfo.api.DoctorAccountApi;
import com.yihu.baseinfo.api.DoctorInfoApi;
import com.yihu.baseinfo.api.DoctorServiceApi;
import com.yihu.baseinfo.api.HosDeptApi;
import com.yihu.myt.ConfigUtil;
import com.yihu.myt.IArraworkService;
import com.yihu.myt.IConswaterService;
import com.yihu.myt.IPauseService;
import com.yihu.myt.enums.MyDatabaseEnum;
import com.yihu.myt.mgr.ApiUtil;
import com.yihu.myt.mgr.BusinessManager;
import com.yihu.myt.myException.SmallDepartmentListNullException;
import com.yihu.myt.service.service.IAnswerRecordService;
import com.yihu.myt.service.service.IAskDoctorNewInterfaceV1Service;
import com.yihu.myt.service.service.IAssayResultMainService;
import com.yihu.myt.service.service.IBookEnrolService;
import com.yihu.myt.service.service.IConsumerOrdersService;
import com.yihu.myt.service.service.ICreditsRecordService;
import com.yihu.myt.service.service.IDepartmentsService;
import com.yihu.myt.service.service.IDiseaseService;
import com.yihu.myt.service.service.IDocSubCloseQuesService;
import com.yihu.myt.service.service.IDoctorDefaultAuthService;
import com.yihu.myt.service.service.IDoctorFreeCountEditService;
import com.yihu.myt.service.service.IDoctorFreeCountService;
import com.yihu.myt.service.service.IDoctorQueSeachStoneService;
import com.yihu.myt.service.service.IFilesService;
import com.yihu.myt.service.service.INewDoctorService;
import com.yihu.myt.service.service.IOpenAuthService;
import com.yihu.myt.service.service.IPatientService;
import com.yihu.myt.service.service.IPostService;
import com.yihu.myt.service.service.IQuesCloseWaterService;
import com.yihu.myt.service.service.IQuesEvaluateService;
import com.yihu.myt.service.service.IQuesMainService;
import com.yihu.myt.service.service.IQuesReplyMutualService;
import com.yihu.myt.service.service.IReplyModelService;
import com.yihu.myt.service.service.IReplyService;
import com.yihu.myt.service.service.ISelfHelpService;
import com.yihu.myt.service.service.IUserFreeCountService;
import com.yihu.myt.service.service.impl.OpenAuthService;
import com.yihu.myt.service.service.impl.PostService;
import com.yihu.myt.util.DBCache;
import com.yihu.myt.util.DateUtil;
import com.yihu.myt.util.LoggerUtil;
import com.yihu.myt.util.PostClient;
import com.yihu.myt.util.StringUtil;
import com.yihu.myt.vo.BookEnrolVo;
import com.yihu.myt.vo.BossAccountBean;
import com.yihu.myt.vo.ConsumerOrdersVo;
import com.yihu.myt.vo.CreditsRecordVo;
import com.yihu.myt.vo.DepartmentsVo;
import com.yihu.myt.vo.DocSubCloseQuesVo;
import com.yihu.myt.vo.DoctorDefaultAuthVo;
import com.yihu.myt.vo.DoctorFreeCountVo;
import com.yihu.myt.vo.DoctorQueSeachStoneVo;
import com.yihu.myt.vo.FilesVo;
import com.yihu.myt.vo.HotQuesMainVo;
import com.yihu.myt.vo.MytArraworkBean;
import com.yihu.myt.vo.MytConswaterBean;
import com.yihu.myt.vo.MytDoctorViewBean;
import com.yihu.myt.vo.MytPauseworkBean;
import com.yihu.myt.vo.OpenAuthVo;
import com.yihu.myt.vo.PatientVo;
import com.yihu.myt.vo.QuesMainVo;
import com.yihu.myt.vo.QuesReplyMutualVo;
import com.yihu.myt.vo.ReplyModelVo;
import com.yihu.myt.vo.ReplyVo;
import com.yihu.myt.vo.SelfHelpVo;
import com.yihu.myt.vo.UserFreeCountVo;
import com.yihu.smsgw.api.PublicForSmsService;
import com.yihu.wsgw.api.InterfaceMessage;
import com.yihu.wsgw.api.WsUtil;
public class NetworkConsultingMedicalApiImpl  implements NetworkConsultingMedicalApi {
	private static  org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(NetworkConsultingMedicalApiImpl.class);
	private static IQuesMainService quesMainService = Ioc
			.get(IQuesMainService.class);
	private static IPatientService patientService = Ioc
			.get(IPatientService.class);
	private static IFilesService filesService = Ioc.get(IFilesService.class);
	private static IConsumerOrdersService consumerOrdersService = Ioc
			.get(IConsumerOrdersService.class);
	private static IDepartmentsService departmentsService = Ioc
			.get(IDepartmentsService.class);
	private static IQuesEvaluateService quesEvaluateService = Ioc
			.get(IQuesEvaluateService.class);
	private static ICreditsRecordService creditsRecordService = Ioc
			.get(ICreditsRecordService.class);
	private static IReplyService replyService = Ioc.get(IReplyService.class);
	private static IQuesCloseWaterService quesCloseWaterService = Ioc
			.get(IQuesCloseWaterService.class);
	private static IQuesReplyMutualService quesReplyMutualService = Ioc
			.get(IQuesReplyMutualService.class);
	private static IAssayResultMainService assayResultMainService = Ioc
			.get(IAssayResultMainService.class);
	private static IDocSubCloseQuesService docSubCloseQuesService = Ioc
			.get(IDocSubCloseQuesService.class);
	private static IAnswerRecordService answerRecordService = Ioc
			.get(IAnswerRecordService.class);
	private static IDiseaseService diseaseService = Ioc
			.get(IDiseaseService.class);
	private static IDoctorQueSeachStoneService doctorQueSeachStoneService = Ioc
			.get(IDoctorQueSeachStoneService.class);//获取用户保存检索数据(科室/疾病)
	private static IDoctorFreeCountService doctorFreeCountService = Ioc
			.get(IDoctorFreeCountService.class);
	private static IDoctorFreeCountEditService doctorFreeCountEditService = Ioc
			.get(IDoctorFreeCountEditService.class);
	private static IUserFreeCountService userFreeCountService = Ioc
			.get(IUserFreeCountService.class);
	private static IPauseService pauseService = Ioc.get(IPauseService.class);
	private static IReplyModelService replyModelService = Ioc.get(IReplyModelService.class);
	private static IConswaterService conswaterService = Ioc
			.get(IConswaterService.class);
	private static IBookEnrolService bookEnrolService = Ioc
			.get(IBookEnrolService.class);
	private static IArraworkService arraworkService = Ioc
			.get(IArraworkService.class); // 医生排班服务接口
	private static ISelfHelpService selfHelpService = Ioc
			.get(ISelfHelpService.class); // 医生配置信息服务接口
	private static IPostService postService = Ioc.get(IPostService.class);
	private static IAskDoctorNewInterfaceV1Service askDoctorNewInterfaceService = Ioc
			.get(IAskDoctorNewInterfaceV1Service.class);
	public static void main(String[] args) throws Exception {
		JSONObject json = new JSONObject();
		/*	
	json.put("doctorUid", "710028071");
	doctorUid  title
		json.put("startDate", "2014-04-15");
		json.put("endDate", "2014-04-15");
		
		json.put("startTime", "13:00");
		json.put("endTime", "20:00");*/
	
	//	json.put("ids", "23953,21783,21183,21159,19079,18953,17928,16579,12627,11337");
		//{"endtime":"20:47","startdate":"2014-04-15","starttime":"13:00","pauseid":7851,"enddate":"2014-04-16"}
		/*json.put("twoDeptID", "0302");
		json.put("pageIndex", "0");
		json.put("quesType", "1");
		json.put("pageSize", "20");*/
		//{"userID":"12423803","doctorUid":"710036857"}
			/*json.put("doctorUid", "710028071");*/
		/*json.put("status", "1");
		json.put("modelID", "43");
		json.put("template", "修改测试模板1");
		json.put("title", "修改测试模板标题1");*/
		/*json.put("status", "1");
		json.put("modelID", "1");
		json.put("template", "修改测试模板1");
		json.put("title", "修改测试模板标题1");*/
	/*	
		json.put("doctorUid", "710028071");
		json.put("pageSize", "10");
		json.put("pageIndex", "0");*/
	/*	json.put("quID", "49712");
		json.put("pageSize", "10");
		json.put("pageIndex", "0");
		json.put("userType", "0");*/
		//String str = "{'queID':'92036','replyType':'0','userID':'10471728','platform':'1','fileMess':[],'messCont':'呃呃呃','filesCount':0,'doctorID':'710037147'}";
		//String str = "{'docPhone':'13950302791','bookenrolid':'63138'}";
		//String str ="{'doctorUid':'710050809','pageSize':'20','pageIndex':'0'} ";
		//String str = "	{'pageSize':'30','doctorUid':'710049299','deptID':'7000474','quesType':'1','pageIndex':'0'}";
	//	json.put("doctorUid", "710028071");
		/*json.put("template", "测试");
		json.put("doctorUid", "710028073");
		json.put("title", "710028073");*/
		String str ="{'docPhone':'13950302791','bookenrolid':'66111'}";
		String getDoctorConsultingList ="{'pageSize':20,'doctorUid':8897,'twoDeptID':7102,'quesType':3,'pageIndex':0}";
		String getDoctorConsultingListV3 ="{'pageSize':'20','doctorUid':'710064015','twoDeptID':'','pageIndex':'0','quesType':'4'}";
		String getDoctorSeachStone = "{'doctorUid':'710028096','seachType':'1'}";
		String saveDoctorSeachStone1 = "{'doctorStone':[{'seachID':215,'num':0,'state':1,'doctorUid':'710028096','seachType':1},{'seachID':200,'num':0,'state':1,'doctorUid':'710028096','seachType':1},{'seachID':197,'num':0,'state':1,'doctorUid':'710028096','seachType':1}],'saveType':1}";
		String saveDoctorSeachStone2 = "{'doctorStone':[{'seachID':412,'num':0,'state':1,'doctorUid':6260,'seachType':2}],'saveType':0}";
		String getDocReturnPhoneList = "{'pageSize':'10','doctorUid':'22623','pageIndex':'0'}";
		String deleteDoctorSeachStone = "{'id':'274498'}";
		String getQueReplyByQuesID = "{'pageSize':'5','quID':'174818','replyIDs':'269541,269542,269556,','pageIndex':'0','userType':'1'}";
		String getDoctorDeptsList = "{'pageSize':'10','deptID':'6119','quesType':'1','pageIndex':'0'}";
		String replyQuertionDoctor ="{'platform':'1','userID':'11650880','replyType':'0','queID':'120444','fileMess':[],'filesCount':1,'messCont':'1111','doctorID':'710047856'}";
		String getModels = "{'doctorUid':'710028096'}";
		String upModels = "{'modelID':12382,'template':'地方苟富贵','title':'qqqqqq','status':0}";
		String setDcPauseWork = "{'doctorUid':710028096,'startDate':'2014-09-05','endDate':'2014-09-08','startTime':'00：00','endTime':'23：59','doctorName':'郑文波'}";
		String deleteDcPauseWork = "{'doctorUid':710028096,'pauseWorkID':8380,'doctorName':'郑文波'}";
		String getDcPauseWork = "{'doctorUid':'710028096'}";
		String  getDoctorConsultingListV4 = "{'pageSize':'20','doctorUid':'710040076','pageIndex':'0','quesType':'1'}";
		String getDoctorConsultingListV2 = "{'doctorUid':'6041','userID':'15380460','pageIndex':'0','pageSize':'1','doctorUids':'0','twoDeptID':'0','quesType':'1'}";
		String quesFreezeForUpdate = "{'userID':'11406956','quID':'267139','price':'1500','OperatorId':'1000000','OperatorName':'医护网'}";
		InterfaceMessage msg = new InterfaceMessage();
		NetworkConsultingMedicalApiImpl dc = new NetworkConsultingMedicalApiImpl();
		msg.setParam(quesFreezeForUpdate);
		//String rt = dc.getDoctorWaitAnswerCount(msg);
		//String rt = dc.upModels(msg);
		//String rt = dc.getDoctorFreeCount(msg);
		//String rt = dc.setModels(msg);
		DBCache.initCacheByDB();
		String rt = dc.quesFreezeForUpdate(msg);
		
		/*
		json = new JSONObject();
		json.put("userID","11392346");json.put("quID","4047");
		json.put("OperatorId","100000");json.put("OperatorName","系统管理员");msg= new InterfaceMessage(); msg.setParam(json.toString());String rtstr = dc.quesFreezeForUpdate(msg);
*/
		
		System.out.println(rt);
	}
	/**
	 * 回复问题(用户)
	 * 
	 * @param quesContent
	 * @param userId
	 * @return
	 *//*
	public String replyQuertionUser(InterfaceMessage msg) {
		String tag = "replyQuertionUser";
		try {
			com.common.json.JSONObject json = new com.common.json.JSONObject(
					msg.getParam());
			int replyType = json.get("replyType") == null ? null : json
					.getInt("replyType");
			int queID = json.get("queID") == null ? null : json.getInt("queID");
			int doctorID = json.get("doctorID") == null ? null : json
					.getInt("doctorID");
			int userID = json.get("userID") == null ? null : json
					.getInt("userID");
			String messCont = json.get("messCont") == null ? null : json
					.getString("messCont");
			com.common.json.JSONArray fileMess = json.get("fileMess") == null ? null
					: json.getJSONArray("fileMess");
			int platform = json.get("platform") == null ? null : json
					.getInt("platform");
			int filesCount = json.get("filesCount") == null ? null : json
					.getInt("filesCount");
			QuesMainVo qusVo = new QuesMainVo();
			qusVo.setQUESMAIN_ID(queID);
			qusVo = quesMainService.queryQuesMainByCondition(qusVo);
			if (qusVo.equals(null) || qusVo == null ) {
				return WsUtil.getRetVal(msg.getOutType(), -2000, "未获得问题数据.");
			}
			if(qusVo.getQD_Status()==1 || qusVo.getQD_Status()==2){
				return WsUtil.getRetVal(msg.getOutType(), -2000, "问题已关闭.");
			}
			
			com.yihu.baseinfo.api.DoctorInfoApi doctorInfoApi = Rpc.get(
					DoctorInfoApi.class,
					ConfigUtil.getInstance().getUrl("url.baseinfo"));
			JdbcConnection conn = null;
			conn = DB.me().getConnection(MyDatabaseEnum.YiHuNet2008);
			conn.beginTransaction(5000);
			// 执行事务
			ReplyVo reply = new ReplyVo();
			reply.setREPLY_CreateTime(DateUtil.dateFormat(new Date()));
			reply.setREPLY_Content(messCont);
			reply.setASK_UserID(userID);
			reply.setASK_QuesID(queID);
			reply.setASK_DoctorID(doctorID);
			reply.setREPLY_Price(Double.valueOf(qusVo.getQD_Price()));
			reply.setREPLY_Type(replyType);
			reply.setREPLY_Platform(platform);
			reply.setREPLY_ContentType(1);// 补充问题
			reply.setREPLY_Status(1);
			reply.setREPLY_CheckStatus(1);
			reply.setREPLY_CheckTime(DateUtil.dateFormat(new Date()));
			reply.setREPLY_CheckManID(0);
			reply.setREPLY_CheckManName("admin");
			reply.setREPLY_UserType(2);
			reply.setREPLY_IsLook(0);
			reply.setREPLY_FilesCount(filesCount);
			if (qusVo.getASK_DoctorID() > 0 && qusVo.getQD_DocReplayCount() > 0) {// 医生已经回复过就为追问
				reply.setREPLY_Price(Double.valueOf(0));
				reply.setREPLY_ContentType(0);
			}

			QuesReplyMutualVo qrmVo = new QuesReplyMutualVo();
			qrmVo.setQueID(queID);
			qrmVo = quesReplyMutualService.queryQuesReplyMutual(qrmVo);
			if (qrmVo == null) {
				qrmVo = new QuesReplyMutualVo();
				qrmVo.setQueID(queID);
				qrmVo.setReplyCount(1);
				qrmVo.setReplyType(0);
				qrmVo.setLastUpdateTime(DateUtil.dateFormat(new Date()));
				qrmVo.setBeginTime(DateUtil.dateFormat(new Date()));
				Calendar rightNow = Calendar.getInstance();
				rightNow.setTime(new Date());
				rightNow.add(Calendar.YEAR, 100);
				Date dt1 = rightNow.getTime();
				qrmVo.setEndTime(DateUtil.dateFormat(dt1));
				int rtQRM = quesReplyMutualService.insertQuesReplyMutual(qrmVo);
				if (rtQRM < 0) {
					return ApiUtil.getJsonRt(-14444, "生成交互数据失败。");
				}
			} else {
				if (qrmVo.getReplyType() == 0) {
					qrmVo.setReplyCount(qrmVo.getReplyCount() + 1);
				} else {
					qrmVo.setReplyType(0);
					qrmVo.setReplyCount(1);
				}
				qrmVo.setLastUpdateTime(DateUtil.dateFormat(new Date()));
				int rtQRM = quesReplyMutualService.updateQuesReplyMutual(qrmVo,
						conn);
				if (rtQRM < 0) {
					return ApiUtil.getJsonRt(-14444, "生成交互数据失败。");
				}
			}
			int rt = replyService.insertReplyByCondition(reply, conn);
			if (rt < 0) {
				conn.rollback();
				return ApiUtil.getJsonRt(-14444, "操作失败。");
			}
			qusVo.setQD_IsUserReplay(1);
			qusVo.setQD_IsReplay(0);
			qusVo.setQD_IsAppAttend(1);
			if (filesCount > 0) {
				qusVo.setQD_FilesCount(qusVo.getQD_FilesCount() + filesCount);
			}
			int qurt = quesMainService.updateQMainByCondition(qusVo, conn);
			if (qurt < 0) {
				conn.rollback();
				return ApiUtil.getJsonRt(-14444, "操作失败。");
			}
			conn.commitTransaction(true);
			// 保存数据
			FilesVo file;

			String filsIds = "";
			if (filesCount > 0 && fileMess != null) {
				for (int i = 0; i < fileMess.length(); i++) {
					file = new FilesVo();
					file.setFILES_Status(fileMess.getJSONObject(i).getInt(
							"status"));
					file.setFILES_CreateTime(DateUtil.dateFormat(new Date()));
					file.setFILES_OldName(fileMess.getJSONObject(i).getString(
							"oldName"));
					file.setFILES_NewName(fileMess.getJSONObject(i).getString(
							"newName"));
					file.setFILES_TypeID(fileMess.getJSONObject(i).getInt(
							"typeID"));
					file.setFILES_ObjTypeID(fileMess.getJSONObject(i).getInt(
							"objTypeID"));
					file.setFILES_Path(fileMess.getJSONObject(i).getString(
							"path"));
					file.setFILES_Size(fileMess.getJSONObject(i).getInt("size"));
					file.setFILES_OperatorID(fileMess.getJSONObject(i).getInt(
							"operatorID"));
					int rtFlid = filesService.insertFiles(file);
					if (rtFlid < 0) {
						return ApiUtil.getJsonRt(-14444, "附件保存失败.");
					}
					filsIds = filsIds + rtFlid + ",";
				}
			}

			// 更新附件表ID
			FilesVo fl;
			fl = new FilesVo();
			fl.setFILES_ObjID(rt);
			if (StringUtil.isNotEmpty(filsIds)) {
				filsIds = StringUtils.substringBeforeLast(filsIds, ",");
				int rtfl = filesService.updateFilesForQuesID(fl, filsIds);
				if (rtfl < 0) {
					return ApiUtil.getJsonRt(-14444, "更新附件表ID失败。");
				}
			}

			DocSubCloseQuesVo docSubClose = new DocSubCloseQuesVo();
			docSubClose.setQueID(queID);
			docSubClose.setEndTime(DateUtil.dateFormat(new Date()));
			docSubCloseQuesService.updateDocSubCloseQuesByQueID(docSubClose);

			// System.out.println(theNewPath);
			// System.out.println(rt);
			
			 * if (replyType == 2) { ReplyVo replyly = new ReplyVo();
			 * replyly.setREPLY_Content(theNewPath); replyly.setREPLY_ID(rt);
			 * int uprt = replyService.updateReplyByCondition(replyly); if (uprt
			 * < 0) { return ApiUtil.getJsonRt(-14444, "操作失败。"); } }
			 
			JSONObject rtJson = JSONObject.fromObject(ApiUtil.getJsonRt(10000,
					"成功。"));
			if(qusVo.getQD_OrdersStatus() == 2|| qusVo.getQD_OrdersStatus() ==0){
				JSONArray rtJr = new JSONArray();
				JSONObject rtid = new JSONObject();
				rtid.put("replyid", rt);
				rtJr.add(rtid);
				rtJson.put("Result", rtJr);
				JSONObject pushJson = new JSONObject();
				JSONObject mesJson = new JSONObject();
				mesJson.put("sourceid", qusVo.getASK_UserID());
				mesJson.put("interactType", "100000");// 用户回复
				mesJson.put("targetid", queID);
				mesJson.put("content", rt);
				pushJson.put("Type", "100000");// 用户回复
				pushJson.put("SendCode", qusVo.getASK_UserID());
				pushJson.put("SendKind", 10);
				pushJson.put("RecvKind", 11);
				pushJson.put("SendName", "APP");
				pushJson.put("SendWay", 1);
				if (platform == 0) {
					pushJson.put("RecvWay", "11");
				} else {
					pushJson.put("RecvWay", "12");
				}
				pushJson.put("AppKey", "554d8e9ac52715ddca89311a");
				pushJson.put("masterSecret", "97c59461bce86655c94f8f6f");
				pushJson.put("RecvCode", qusVo.getASK_DoctorID());
				pushJson.put("Content", "APP");
				pushJson.put("ContentExtend", mesJson);
				// System.out.println(pushJson);
				MsgManagerService sms = Rpc.get(MsgManagerService.class, ConfigUtil
						.getInstance().getUrl("url.MsgManager"), 3000);
				pushMsg.setParam(pushJson.toString());
				String rtMsg = sms.pushMsg(pushMsg);
				JSONArray rtJr = new JSONArray();
				JSONObject rtid = new JSONObject();
				rtid.put("replyid", rt);
				rtJr.add(rtid);
				rtJson.put("Result", rtJr);
				MsgApi msgApi = Rpc.get(MsgApi.class, ConfigUtil.getInstance().getUrl("url.jpushMsg"));
				JSONObject mesJson = new JSONObject();
				JSONObject androidJson = new JSONObject();
				JSONObject iosJson = new JSONObject();
				androidJson.put("uri", "");
				androidJson.put("date",DateUtil.dateFormat(new Date()));
				androidJson.put("sourceId", userID);
				androidJson.put("sourceName", "");
				androidJson.put("contentId", rt);
				androidJson.put("content", messCont);
				androidJson.put("questionId", queID);
				androidJson.put("title", qusVo.getQUESMAIN_Content().subSequence(0, 15) +"......");
				if(qusVo.getQD_DocReplayCount()>0){
					
				}else{
					androidJson.put("title", "您收到1条新咨询，请点击查看。");
				}
				androidJson.put("operCode", "12000");
				androidJson.put("msgType", replyType);
				
				iosJson.put("questionId", queID);
				//iosJson.put("ti", qusVo.getQUESMAIN_Content().subSequence(0, 15) +"......");
				if(qusVo.getQD_DocReplayCount()>0){
					iosJson.put("ti", qusVo.getQUESMAIN_Content().subSequence(0, 15) +"......");
				}else{
					iosJson.put("ti", qusVo.getQUESMAIN_Content().subSequence(0, 15) +"......");
				}
				iosJson.put("operCode", 12000);
				iosJson.put("contentId", rt);
				iosJson.put("sourceId", userID);
				iosJson.put("sourceName", "");
				mesJson.put("androidParam", androidJson);
				mesJson.put("toAppType", 1);
				mesJson.put("toUsers", doctorID);
				mesJson.put("iosParam", iosJson);
				InterfaceMessage pushMsg = new InterfaceMessage();
				pushMsg.setParam(mesJson.toString());
				JSONObject authinfo = new JSONObject();
				authinfo.put("ClientId", "client.myt");
				pushMsg.setAuthInfo(authinfo.toString());
				String purt="";
				try {
					//purt = msgApi.sendMsgJ(pushMsg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(purt);
				}
				
				
			}
			// System.out.println(rtMsg);
			return rtJson.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}*/
	
	/**
	 * 回复问题(医生)
	 * 
	 * @param quesContent
	 * @param userId
	 * @return
	 */
	
	public String replyQuertionDoctor(InterfaceMessage msg) {
		String tag = "replyQuertionDoctor";
		try {
			com.yihu.account.api.IAccountService accountService = Rpc.get(
					IAccountService.class,
					ConfigUtil.getInstance().getUrl("url.account"));
			com.common.json.JSONObject json = new com.common.json.JSONObject(
					msg.getParam());
			int replyType = json.get("replyType") == null ? null : json
					.getInt("replyType");
			int queID = json.get("queID") == null ? null : json.getInt("queID");
			int doctorID = json.get("doctorID") == null ? null : json
					.getInt("doctorID");
			int userID = json.get("userID") == null ? null : json
					.getInt("userID");
			String messCont = json.get("messCont") == null ? null : json
					.getString("messCont");
			com.common.json.JSONArray fileMess = json.get("fileMess") == null ? null
					: json.getJSONArray("fileMess");
			int platform = json.get("platform") == null ? null : json
					.getInt("platform");
			int filesCount = json.get("filesCount") == null ? null : json
					.getInt("filesCount");
			com.yihu.baseinfo.api.DoctorAccountApi doctorAccountApi = Rpc.get(
					DoctorAccountApi.class,
					ConfigUtil.getInstance().getUrl("url.baseinfo"));
			QuesMainVo qusVo = new QuesMainVo();
			qusVo.setQUESMAIN_ID(queID);
			qusVo = quesMainService.queryQuesMainByCondition(qusVo);
			//
			qusVo.setOpenAuthFlag(null);
			if (qusVo.equals(null) || qusVo == null) {
				return WsUtil.getRetVal(msg.getOutType(), -2000, "未获得问题数据.");
			}
			if (qusVo.getQD_SourceType() == 1
					&& qusVo.getQD_DocReplayCount() == 0) {
				QuesMainVo qm = new QuesMainVo();
				qm.setASK_DoctorID(doctorID);
				qm.setQUESMAIN_ID(queID);
				int ifyours = quesMainService.querCountDcAnswerQus(qm);
				if (ifyours <= 0) {
					return WsUtil.getRetVal(msg.getOutType(), -2000,
							"抢答的问题已过期，请重新获取");
				}
			} else {
				if (!qusVo.getASK_DoctorID().equals(doctorID)
						|| qusVo.getQD_CheckStatus() == 0) {
					return WsUtil
							.getRetVal(msg.getOutType(), -2000, "无法回复该问题。");
				}
			}
			String s1 = DateUtil.dateFormat(new Date());
			String s2 = qusVo.getQUESMAIN_ExpiredTime();
			java.text.DateFormat df = new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			java.util.Calendar c1 = java.util.Calendar.getInstance();
			java.util.Calendar c2 = java.util.Calendar.getInstance();
			c1.setTime(df.parse(s1));
			c2.setTime(df.parse(s2));
			int result = c1.compareTo(c2);
			if (result >= 0 && qusVo.getQD_OrdersStatus() != 5
					&& qusVo.getQD_Price() != 0 && qusVo.getQD_DocReplayCount()==0) {// 如果时间已经过期执行退款
				ConsumerOrdersVo cVo = new ConsumerOrdersVo();
				cVo.setASK_QuesID(qusVo.getQUESMAIN_ID());
				cVo = consumerOrdersService.queryConsumerOrdersByQuesID(cVo);
				ReturnValueBean clrt = accountService.clearFrozen(userID,
						cVo.getCO_ID(), "12", "01");
				/*if (clrt.getCode() < 0) {
					return ApiUtil.getJsonRt(clrt.getCode(), clrt.getMessage());
				}*/
				// ConsumerOrdersVo cVo = new ConsumerOrdersVo();
				cVo.setCO_Status(-1);
				// cVo.setCO_ID(1);
				int cort = consumerOrdersService.updateCOrdersByCondition(cVo);
				if (cort < 0) {
					return ApiUtil.getJsonRt(-14444, "订单保存失败");
				}
				qusVo.setQD_Price(0);
				qusVo.setQD_OrdersStatus(5);
			}
			if (qusVo.getQD_SourceType() == 1
					&& qusVo.getQD_DocReplayCount() == 0) {
				// 第一次回答问题有爱心值
				JSONObject axJson = new JSONObject();
				axJson.put("doctorUid", qusVo.getASK_DoctorID());
				axJson.put("resId", queID);
				axJson.put("typeId", 2);
				ServiceResult<String> addlrt = doctorAccountApi
						.addDoctorLove(axJson.toString());
				if (addlrt.getCode() < 0) {
					return ApiUtil.getJsonRt(-14444, "爱心值写入失败。");
				}
				CreditsRecordVo crVo = new CreditsRecordVo();
				crVo.setASK_QuesID(queID);
				crVo.setASK_DoctorAccountID(qusVo.getASK_DoctorID());
				crVo.setASK_DoctorID(qusVo.getASK_DoctorID());
				crVo.setCR_Credits(1);
				crVo.setCR_CreditsType(2);
				crVo.setCR_Type(2);
				crVo.setCR_CreateTime(DateUtil.dateFormat(new Date()));
				int rtCr = creditsRecordService
						.insertCreditsRecord(crVo);
				if (rtCr < 0) {
					return ApiUtil.getJsonRt(-14444, "爱心值流水插入失败。");
				}
				
				//加入 事件是否  回复了3次 10次
				EventUtilForMYT.sendEvenForAddlove(String.valueOf(qusVo.getASK_DoctorID()), String.valueOf(queID));
				
			}
			ReplyVo reply = new ReplyVo();
			reply.setREPLY_CreateTime(DateUtil.dateFormat(new Date()));
			reply.setREPLY_Content(messCont);
			reply.setASK_UserID(userID);
			reply.setASK_QuesID(queID);
			reply.setASK_DoctorID(doctorID);
			reply.setREPLY_Price(Double.valueOf(qusVo.getQD_Price()));
			reply.setREPLY_Type(replyType);
			reply.setREPLY_Platform(platform);
			reply.setREPLY_ContentType(0);
			reply.setREPLY_Status(1);
			reply.setREPLY_CheckStatus(1);
			reply.setREPLY_CheckTime(DateUtil.dateFormat(new Date()));
			reply.setREPLY_CheckManID(0);
			reply.setREPLY_CheckManName("admin");
			reply.setREPLY_UserType(1);
			reply.setREPLY_IsLook(0);
			int rt = replyService.insertReply(reply);
			if (rt < 0) {
				return ApiUtil.getJsonRt(-14444, "操作失败。");
			}
			QuesReplyMutualVo qrmVo = new QuesReplyMutualVo();
			qrmVo.setQueID(queID);
			qrmVo = quesReplyMutualService.queryQuesReplyMutual(qrmVo);
			if (qrmVo == null) {
				qrmVo = new QuesReplyMutualVo();
				qrmVo.setQueID(queID);
				qrmVo.setReplyCount(1);
				qrmVo.setReplyType(1);
				qrmVo.setLastUpdateTime(DateUtil.dateFormat(new Date()));
				qrmVo.setBeginTime(DateUtil.dateFormat(new Date()));
				Calendar rightNow = Calendar.getInstance();
				rightNow.setTime(new Date());
				rightNow.add(Calendar.YEAR, 100);
				Date dt1 = rightNow.getTime();
				qrmVo.setEndTime(DateUtil.dateFormat(dt1));
				int rtQRM = quesReplyMutualService.insertQuesReplyMutual(qrmVo);
				if (rtQRM < 0) {
					return ApiUtil.getJsonRt(-14444, "生成交互数据失败。");
				}
			} else {
				if (qrmVo.getReplyType() == 1) {
					qrmVo.setReplyCount(qrmVo.getReplyCount() + 1);
				} else {
					qrmVo.setReplyType(1);
					qrmVo.setReplyCount(1);
				}
				qrmVo.setLastUpdateTime(DateUtil.dateFormat(new Date()));
				int rtQRM = quesReplyMutualService.updateQuesReplyMutual(qrmVo);
				if (rtQRM < 0) {
					return ApiUtil.getJsonRt(-14444, "生成交互数据失败。");
				}
			}

			FilesVo file;
			String filsIds = "";
			if (filesCount > 0 && fileMess != null) {
				for (int i = 0; i < fileMess.length(); i++) {
					file = new FilesVo();
					file.setFILES_Status(fileMess.getJSONObject(i).getInt(
							"status"));
					file.setFILES_CreateTime(DateUtil.dateFormat(new Date()));
					file.setFILES_OldName(fileMess.getJSONObject(i).getString(
							"oldName"));
					file.setFILES_NewName(fileMess.getJSONObject(i).getString(
							"newName"));
					file.setFILES_TypeID(fileMess.getJSONObject(i).getInt(
							"typeID"));
					file.setFILES_ObjTypeID(fileMess.getJSONObject(i).getInt(
							"objTypeID"));
					file.setFILES_Path(fileMess.getJSONObject(i).getString(
							"path"));
					file.setFILES_Size(fileMess.getJSONObject(i).getInt("size"));
					file.setFILES_OperatorID(fileMess.getJSONObject(i).getInt(
							"operatorID"));
					int rtFlid = filesService.insertFiles(file);
					if (rtFlid < 0) {
						return ApiUtil.getJsonRt(-14444, "附件保存失败。");
					}
					filsIds = filsIds + rtFlid + ",";
				}
			}
			Calendar dcNow = Calendar.getInstance();
			dcNow.setTime(new Date());
			dcNow.add(Calendar.DAY_OF_YEAR, 2);
			Date dc1 = dcNow.getTime();
			qusVo.setQUESMAIN_ExpiredTime(DateUtil.dateFormat(dc1));
			qusVo.setQD_DocReplayCount(qusVo.getQD_DocReplayCount() + 1);
			qusVo.setQD_IsUserReplay(0);
			qusVo.setQD_IsReplay(1);
			int qurt = quesMainService.updateQuesMain(qusVo);
			if (qurt < 0) {
				return ApiUtil.getJsonRt(-14444, "操作失败。");
			}
			// 更新附件表ID
			FilesVo fl;
			fl = new FilesVo();
			fl.setFILES_ObjID(rt);
			if (StringUtil.isNotEmpty(filsIds)) {
				filsIds = StringUtils.substringBeforeLast(filsIds, ",");
				int rtfl = filesService.updateFilesForQuesID(fl, filsIds);
				if (rtfl < 0) {
					return ApiUtil.getJsonRt(-14444, "更新附件表ID失败。");
				}
			}
			JSONObject rtJson = JSONObject.fromObject(ApiUtil.getJsonRt(10000,
					"成功。"));
			/*com.yihu.baseinfo.api.DoctorInfoApi doctorInfoApi = Rpc.get(
					DoctorInfoApi.class,
					ConfigUtil.getInstance().getUrl("url.baseinfo"));*/
			/*com.yihu.baseinfo.api.DoctorServiceApi doctorServiceApi = Rpc.get(
					DoctorServiceApi.class,
					ConfigUtil.getInstance().getUrl("url.baseinfo"));*/
			/*JSONObject jsonObj = new JSONObject();
			jsonObj.put("doctorUid", doctorID);
			// jsonObj.put("displayStatus", 1);
			jsonObj.put("main", 1);
			jsonObj.put("startRow", 0);
			jsonObj.put("pageSize", 0);
			jsonObj.put("columns", "textZX");
			InterfaceMessage interfacemessage = new InterfaceMessage();
			interfacemessage.setParam(jsonObj.toString());
			JSONObject dcRt = JSONObject.fromObject(doctorInfoApi
					.queryComplexDoctorList(interfacemessage));*/
			/*JSONArray dcJson = JSONArray
					.fromObject(dcRt.getJSONArray("Result"));*/
			JSONObject rtid = new JSONObject();
			/*rtid.put("firstOpen", 0);
			rtid.put("price", 0);
			JSONObject dcBillJson = new JSONObject();
			int countDoc = dcRt.getInt("Total");*/
			/*QuesMainVo qv = new QuesMainVo();
			qv.setASK_DoctorID(doctorID);
			int ct = quesMainService.querCountOverQus(qv);*/
			/*if (countDoc > 0 && ct<1  && qusVo.getQD_SourceType() == 1) {
				String serviceStatus = dcJson.getJSONObject(0).getString(
						"textZX");
				if (serviceStatus.equals("0")) {
				dcBillJson.put("doctorUid", doctorID);
				dcBillJson.put("serviceId", 5);
				dcBillJson.put("serviceRecordId", queID);
				ServiceResult<String> rtBill = doctorServiceApi
						.insertBill(dcBillJson.toString());
				if (rtBill.getCode() < 0) {
					return ApiUtil.getJsonRt(-14444, rtBill.getMessage());
				}
				rtid.put("firstOpen", 1);
				rtid.put("price", rtBill.getResult());
				}
			}*/
			JSONArray rtJr = new JSONArray();
			
			JSONObject pushJson = new JSONObject();
			JSONObject mesJson = new JSONObject();
			mesJson.put("sourceid", qusVo.getASK_DoctorID());
			mesJson.put("interactType", "110002");// 医生回复
			mesJson.put("targetid", queID);
			mesJson.put("content", rt);
			pushJson.put("Type", "110002");// 医生回复
			pushJson.put("SendCode", qusVo.getASK_DoctorID());
			pushJson.put("SendKind", 11);
			pushJson.put("RecvKind", 10);
			pushJson.put("SendName", "APP");
			pushJson.put("sendWay", 1);
			if (platform == 0) {
				pushJson.put("RecvWay", "11");
			} else {
				pushJson.put("RecvWay", "12");
			}
			pushJson.put("AppKey", "7a2d788ee03598a0613d1272");
			pushJson.put("masterSecret", "bb1571722129212ce2a9c4ef");
			pushJson.put("RecvCode", qusVo.getASK_UserID());
			pushJson.put("Content", "APP");
			pushJson.put("ContentExtend", mesJson);
			// System.out.println(pushJson);
			MsgManagerService sms = Rpc.get(MsgManagerService.class, ConfigUtil
					.getInstance().getUrl("url.MsgManager"), 3000);
			InterfaceMessage pushMsg = new InterfaceMessage();
			pushMsg.setParam(pushJson.toString());
			//postService.sendMsgJ(pushJson.toString());
			String rtMsg ="";
			try {
				 rtMsg = sms.pushMsg(pushMsg);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				System.out.println(rtMsg);
			}
			
			
			
			//推送
			
			
			
			//推送新增 describe  字段
			String  describe="";
			String doctorName=null;
			
			try{
		
		    describe="您咨询的@doc医生   @dept科室  已回复";
		    
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("doctorUid", doctorID);
			jsonObj.put("main", 1);
			jsonObj.put("pageIndex", 1);
			jsonObj.put("pageSize", 100);
			jsonObj.put("columns", "deptName,doctorName");

			JSONObject dcRt  =JSONObject.fromObject(postService.queryComplexDoctorList_v2(jsonObj.toString())) ;

			int code = dcRt.getInt("Code");
		//	String doctorName=null;
			String deptName =null;
			if(code > 0 ){
				JSONArray rts= dcRt.getJSONArray("Result");
				if(rts.size() == 0){
					System.out.println("未找到医生信息。");
				}
				 doctorName = rts.getJSONObject(0).getString("doctorName");
				 deptName = rts.getJSONObject(0).getString("deptName");
				 
			}else{
				System.out.println("未找到医生信息。");
			}
			
			System.out.println(doctorName+"-"+deptName);
			describe=describe.replace("@doc",doctorName );
			describe=describe.replace("@dept", deptName);
			System.out.println("real---"+describe);
			
	        }catch(Exception e){
				
	        	log.error(LoggerUtil.getTrace(e));
	        			
	        	return ApiUtil.getJsonRt(-14444,
						"  无法查询到此医生信息   （ 调用远程接口  baseinfo.DoctorInfoApi.queryComplexDoctorList_v2   失败)");
	        	
			}
			
			
			
			//MsgApi msgApi = Rpc.get(MsgApi.class, ConfigUtil.getInstance().getUrl("url.jpushMsg"));
			JSONObject nmesJson = new JSONObject();
			JSONObject androidJson = new JSONObject();
			JSONObject iosJson = new JSONObject();
			androidJson.put("uri", "");
			androidJson.put("date",DateUtil.dateFormat(new Date()));
			androidJson.put("sourceId",  doctorID);
			androidJson.put("sourceName", "");
			androidJson.put("contentId", rt);
			androidJson.put("content", messCont);
			androidJson.put("questionId",queID );
			androidJson.put("title", "您收到医生回复，请点击查看");
			androidJson.put("operCode", "12000");
			androidJson.put("msgType", replyType);
			//describe
	        androidJson.put("describe", describe);
			
			iosJson.put("questionId", queID);
			iosJson.put("ti", "您收到医生回复，请点击查看");
			iosJson.put("operCode", 12000);
			iosJson.put("contentId", rt);
			iosJson.put("sourceId", doctorID);
			iosJson.put("sourceName", "");
			//describe
            iosJson.put("describe", describe);
			
			rtid.put("replyid", rt);
			rtJr.add(rtid);
			rtJson.put("Result", rtJr);
		/*	“questionId”大问题的ID
			title**患者发来一条新消息
			"operCode"12000
			"contentId"单条问题的ID
			"sourceId" 患者的ID
			"sourceName"患者的姓名*/
			nmesJson.put("androidParam", androidJson);
			nmesJson.put("toAppType", 2);
			nmesJson.put("toUsers",userID);
			nmesJson.put("iosParam", iosJson);
			JSONObject authinfo = new JSONObject();
			authinfo.put("ClientId", "client.myt");
			nmesJson.put("Authinfo", authinfo.toString());
			String pstr = postService.sendMsgJ(nmesJson.toString());
			System.out.println(pstr);
			/*InterfaceMessage pushMsgN = new InterfaceMessage();
			pushMsgN.setParam(nmesJson.toString());
			JSONObject authinfo = new JSONObject();
			authinfo.put("ClientId", "client.myt");
			pushMsgN.setAuthInfo(authinfo.toString());
			String msgRt="";
			try {
				msgRt = msgApi.sendMsgJ(pushMsgN);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				System.out.println(msgRt);
			}*/
			
			
			
			
			
			//推送 weixin 
			
			try {


			JSONObject pushmsg=new  JSONObject();
			
			pushmsg.put("type", "2");
			pushmsg.put("doctorUid", doctorID);
			pushmsg.put("doctorName", doctorName);//账号人员名称
	
			pushmsg.put("diseaName", qusVo.getQUESMAIN_Content());//病情描述
			pushmsg.put("answerDescp", messCont);//回复内容
			
			
			pushmsg.put("qid", queID);//问题ID
			pushmsg.put("status", qusVo.getQD_Status());//问题状态
			
			String back = PostClient.dopushforWEIXIN(pushmsg.toString());
			System.out.println("微信推送：：：："+back);
			
		} catch (Exception e) {
			
			log.error(LoggerUtil.getTrace(e));
			e.printStackTrace();
		}
		
			
			
			
			
			
			
			
			
			
			
			String sb1 = DateUtil.dateFormat(new Date(), DateUtil.YMD_FORMAT);
			String sb2 = sb1 + " 09:00:00";
			String sb3 = sb1 + " 22:00:00";
			if (DateUtil.getIfInTime(sb2, sb3)
					&& StringUtil.isNotEmpty(qusVo.getQD_TipPhone())) {
				if (!qusVo.getQD_TipPhone().equals("0")) {
					JSONObject jsObj = new JSONObject();
					jsObj.put("doctorUid", qusVo.getASK_DoctorID());
					jsObj.put("erviceStatusSearch", 3);
					// jsonObj.put("displayStatus", 1);
					jsObj.put("main", 1);
					jsObj.put("pageIndex", 1);
					jsObj.put("pageSize", 100);
					jsObj.put("columns", "doctorAccount,doctorName");
					JSONObject dcsRt = JSONObject.fromObject(postService.queryComplexDoctorList_v2(jsObj.toString()));
					JSONArray dcsJson = JSONArray.fromObject(dcsRt
							.getJSONArray("Result"));
					int countsDoc = dcsRt.getInt("Total");
					if (countsDoc < 0) {
						return ApiUtil.getJsonRt(10000, "获取短信信息失败。");
					}
					PublicForSmsService smsService = Rpc.get(
							PublicForSmsService.class, ConfigUtil.getInstance()
									.getUrl("url.smsgw"), 16000);
					Integer str = 0;
					try {
						str = smsService
								.send(qusVo.getQD_TipPhone(),
										"您好！您的咨询已于"
												+ DateUtil.dateFormat(
														new Date(),
														DateUtil.YMDHMS_FORMAT)
												+ "得到"
												+ dcsJson
														.getJSONObject(0)
														.getString("doctorName")
												+ "医生的回复，请尽快登录健康之路APP或医护网查看。如果您不需要追问，该咨询将在24小时后关闭。",
										10101210);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("短信递送失败。");
					}
					if (str > 0) {
						System.out.println("短信递送成功。");
					}

				}
			}
			// System.out.print(rtMsg);
			return rtJson.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	/**
	 * 医生回拨用户电话
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public String docReturnPhone(InterfaceMessage msg) {
		String tag = "docReturnPhone";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			/*String bookenrolid = json.get("bookenrolid") == null ? null : json
					.getString("bookenrolid");*/
			int bookenrolid = json.get("bookenrolid") == null ? null : json
					.getInt("bookenrolid");
			String  docPhone =  json.get("docPhone") == null ? null : json
					.getString("docPhone");
			com.yihu.account.api.IAccountService accountService = Rpc.get(
					IAccountService.class,
					ConfigUtil.getInstance().getUrl("url.account"));
			BookEnrolVo vo = new BookEnrolVo();
			vo.setBOOKENROLID(bookenrolid);
			vo = bookEnrolService.queryBookEnrol(vo);
			/*com.yihu.account.api.AccLoginBean logic = accountService
					.getAccLoginObj(vo.);*/
			com.yihu.account.api.AccMembershipcardBean card = accountService
					.getMembershipcardObject(vo.getCARDID());

			BossAccountBean bossAccount = new BossAccountBean();
			bossAccount.setAccountSN(card.getAccountsn());
			bossAccount.setCardID(card.getCardnumber());
			bossAccount.setCardState(Integer.parseInt(card.getState().trim()));
			bossAccount.setCardtypesn(card.getCardtypesn());
			MytDoctorViewBean rltMdvBean = null;
			ServiceResult<MytDoctorViewBean> sr = arraworkService
					.getMytDoctorView(Integer.valueOf(vo.getOPERCONFID()));
			rltMdvBean = sr.getCode() == 1 ? sr.getResult() : null;
			/*List<MytArraworkBean> list = BusinessManager.getArraWorks(
					rltMdvBean.getBalancetype(), 0, "one", rltMdvBean
							.getOperconfid().toString(),
					rltMdvBean.getRemark(), rltMdvBean.getDoctorlevel()
							.toString(), rltMdvBean.getHospofficeid(),
					bossAccount, rltMdvBean.getOrgid());
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					MytArraworkBean arra = (MytArraworkBean) list.get(i);
					if ("1".equals(arra.getOperatorname())) {
						// 在线
						break;
					}
					if ((i + 1) == list.size()) {
						if ("0".equals(arra.getOperatorname())) {
							
							 * return new RestResult( new OutPut(10001,
							 * "该医生当前不在线"), null, restParam);
							 
							return WsUtil.getRetVal(msg.getOutType(), 10001,
									"该医生当前不在线");
						} else if ("2".equals(arra.getOperatorname())) {
							
							 * return new RestResult(new OutPut(10004,
							 * "医生停诊，无法受理咨询"), null, restParam);
							 
							return WsUtil.getRetVal(msg.getOutType(), 10004,
									"医生停诊，无法受理咨询");
						} else if ("4".equals(arra.getOperatorname())) {
							
							 * return new RestResult(new OutPut(10005,
							 * "医生通话中，无法受理咨询"), null, restParam);
							 
							return WsUtil.getRetVal(msg.getOutType(), 10005,
									"医生通话中，无法受理咨询");
						}
					}
				}
			}*/
			String[] retmsg = BusinessManager.checkCards(rltMdvBean
					.getBalancetype(), rltMdvBean.getDoctorlevel().toString(),
					rltMdvBean.getHospofficeid(), bossAccount,0, rltMdvBean.getOperconfid());
			if ("0".equals(retmsg[0])) {
				/*
				 * return new RestResult(new OutPut(10006, res[1]), null,
				 * restParam);
				 */
				return WsUtil.getRetVal(msg.getOutType(), -10006,"患者余额不足，无法回拨患者。");
			}

			SelfHelpVo selfHelp = new SelfHelpVo();
			selfHelp.setShMobile(docPhone);
			selfHelp.setOperTime(DateUtil.dateFormat(DateOper.getNowDateTime()));
			selfHelp.setState(1);
			selfHelp.setOperConfId(Integer.valueOf(vo.getOPERCONFID()));
			selfHelp.setShType(2);
			selfHelp.setBussId(vo.getBOOKENROLID());
			selfHelp.setUserPhone(vo.getREVERTPHONE());
			selfHelp.setCardId(card.getCardnumber());
			int rt = selfHelpService.insertSelfHelp(selfHelp);
			if (rt < 0) {
				return WsUtil.getRetVal(msg.getOutType(), -2000, "拨号数据写入失败。");
			}
			JSONObject rtjon = new JSONObject();
			JSONArray rtAr = new JSONArray();
			rtjon.put("Code", 10000);
			rtjon.put("Message", "成功");
			rtjon.put("Result", rtAr);
			return rtjon.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return  ApiUtil.getJsonRt(-14444, "加载异常!"
					+ StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return  ApiUtil.getJsonRt(-14444, "加载异常!"
					+ StringUtil.getException(e));
		}
	}
	
	/**
	 * 获取医生回拨列表
	 * 
	 * @param 
	 * @param 
	 * @return
	 */
	public String getDocReturnPhoneList(InterfaceMessage msg) {
		String tag = "getDocReturnPhoneList";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			String doctorUid =  json.get("doctorUid") == null ? null : json
					.getString("doctorUid");	
			Integer pageSize = json.get("pageSize") == null ? null : json
					.getInt("pageSize");
			Integer pageIndex = json.get("pageIndex") == null ? null : json
					.getInt("pageIndex"); 
			JSONObject rt = new JSONObject();
			BookEnrolVo vo = new BookEnrolVo();
			vo.setOPERCONFID(doctorUid);
			vo.setSTATE("1");
			vo.setREVERTRESULT("1");
			vo.setDATEWEEK("1");
			String tp1 ;
			String tp2;
			JSONArray rtJs =new  JSONArray();
			JSONObject bkJson;
			JSONObject bkListRt=JSONObject.fromObject(bookEnrolService.queryBookEnrolListByCondition(vo,pageSize,pageIndex));
			JSONArray list = bkListRt.getJSONArray("result");
			for(int i=0;i<list.size();i++){
				bkJson = new JSONObject();
				bkJson.put("remark",  list.getJSONObject(i).get("remark") == null ? "" : list.getJSONObject(i)
						.getString("remark"));
				if(StringUtil.isNotEmpty(list.getJSONObject(i).get("revertphone"))){
					tp1 = list.getJSONObject(i).getString("revertphone").substring(0, 3);
					tp2 = list.getJSONObject(i).getString("revertphone").substring( list.getJSONObject(i).getString("revertphone").length()-4, list.getJSONObject(i).getString("revertphone").length());
					bkJson.put("revertPhone", tp1 + "****" + tp2);
				}else{
					bkJson.put("revertPhone", "");
				}
				bkJson.put("custName", list.getJSONObject(i).get("custname"));
				bkJson.put("dateWeek", list.getJSONObject(i).get("dateweek"));
				bkJson.put("startTime", list.getJSONObject(i).get("starttime"));
				bkJson.put("endTime", list.getJSONObject(i).get("endtime"));
				bkJson.put("provincename", list.getJSONObject(i).get("provincename") == null ? "" : list.getJSONObject(i)
						.getString("provincename"));
				bkJson.put("bookenrolid", list.getJSONObject(i).get("bookenrolid"));
				rtJs.add(bkJson);
			}
			int count = bookEnrolService.queryBookEnrolCountByCondition(vo);
			rt  = JSONObject.fromObject(ApiUtil
					.getJsonRt(10000, "成功"));
			rt.put("Result", rtJs);
			rt.put("Total", count);
			return rt.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	/**
	 * 获取名咨询流水单列表
	 * 
	 * @param 
	 * @param 
	 * @return
	 */
	public String getMytBillList(InterfaceMessage msg) {
		String tag = "getMytBillList";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			String ids =  json.get("ids") == null ? null : json
					.getString("ids");	
			JSONObject rt = new JSONObject();
			ServiceResult<List<MytConswaterBean>> cons = conswaterService
					.getMytConswaterResultList(ids);
			JSONArray jsonArr = new JSONArray();
			String tp1 ;
			String tp2;
			if(cons.getCode()>0){
				for(MytConswaterBean con:cons.getResult()){
					JSONObject rtJons = new JSONObject();
					rtJons.put("startDateTime",DateUtil.dateFormat(con.getStartdatetime(), DateUtil.YMDHMS_FORMAT));
					rtJons.put("endDateTime",DateUtil.dateFormat(con.getEnddatetime(), DateUtil.YMDHMS_FORMAT));
					tp1 = con.getCustphone().substring(0, 3);
					tp2 = con.getCustphone().substring( con.getCustphone().length()-4, con.getCustphone().length());
					rtJons.put("custPhone", tp1+"****"+tp2);
					rtJons.put("consMin",con.getConsmin());
					rtJons.put("consID", con.getPkconswaterid());
					jsonArr.add(rtJons);
				}
				rt  = JSONObject.fromObject(ApiUtil
						.getJsonRt(10000, "成功"));
				rt.put("Result", jsonArr);
			}else{
				rt  = JSONObject.fromObject(ApiUtil
						.getJsonRt(-14444, "无数据"));
			}
			return rt.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	/**
	 * 获取医生模板列表
	 * 
	 * @param 
	 * @param 
	 * @return
	 */
	public String getModels(InterfaceMessage msg) {
		String tag = "getModels";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			Integer doctorUid = json.get("doctorUid") == null ? null : json
					.getInt("doctorUid");
			ReplyModelVo model = new ReplyModelVo();
			model.setModelUserID(doctorUid);
			model.setStatus(0);
			model.setModeType(0);
			model.setIfSystem(0);
			JSONObject rt = new JSONObject();
			JSONArray rtJs =  JSONArray.fromObject(replyModelService.queryReplyModelListByCondition(model));
			rt  = JSONObject.fromObject(ApiUtil
					.getJsonRt(10000, "成功"));
			rt.put("Result", rtJs);
			return rt.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	/**
	 * 设置医生模板
	 * 
	 * @param 
	 * @param 
	 * @return
	 */
	public String setModels(InterfaceMessage msg) {
		String tag = "setModels";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			Integer doctorUid = json.get("doctorUid") == null ? null : json
					.getInt("doctorUid");
			String template =  json.get("template") == null ? null : json
					.getString("template");	
			String title =  json.get("title") == null ? null : json
					.getString("title");	
			ReplyModelVo model = new ReplyModelVo();
			model.setStatus(0);
			model.setTemplate(template);
			model.setModeType(0);
			model.setTitle(title);
			model.setIfSystem(0);
			model.setInsertTime(DateUtil.dateFormat(new Date()));
			model.setModelUserID(doctorUid);
			int  rtID = replyModelService.insertReplyModelVo(model);
			JSONObject rt = new JSONObject();
			JSONObject rtJs =  new JSONObject();
			rtJs.put("replyModelID", rtID);
			rt  = JSONObject.fromObject(ApiUtil
					.getJsonRt(10000, "成功"));
			rt.put("Result",rtJs);
			return rt.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	/**
	 * 修改医生模板
	 * 
	 * @param 
	 * @param 
	 * @return
	 */
	public String upModels(InterfaceMessage msg) {
		String tag = "upModels";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			Integer status = json.get("status") == null ? null : json
					.getInt("status");
			String template =  json.get("template") == null ? null : json
					.getString("template");	
			String title =  json.get("title") == null ? null : json
					.getString("title");	
			Integer modelID = json.get("modelID") == null ? null : json
					.getInt("modelID");
			ReplyModelVo model = new ReplyModelVo();
			model.setModelID(modelID);
			model.setStatus(status);
			model.setTemplate(template);
			model.setTitle(title);
			model.setInsertTime(DateUtil.dateFormat(new Date()));
			Integer rtCount = replyModelService.queryReplyModelIfSystem(model);
			if(rtCount>0){
				return ApiUtil	.getJsonRt(-2000, "系统模板无法修改。");
			}
			replyModelService.updateReplyModel(model);
			JSONObject rt = new JSONObject();
			rt  = JSONObject.fromObject(ApiUtil
					.getJsonRt(10000, "成功"));
			return rt.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	/**
	 * 获取医生是否停诊
	 * 
	 * @param 
	 * @param 
	 * @return
	 */
	public String getDcPauseWork(InterfaceMessage msg) {
		String tag = "getDcPauseWork";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			Integer doctorUid = json.get("doctorUid") == null ? null : json
					.getInt("doctorUid");
			QuesMainVo qmv = new QuesMainVo();
			qmv.setASK_DoctorID(doctorUid);
			
			JSONObject rt = new JSONObject();
			String rtJson = pauseService.getEntityValue(doctorUid, DateOper.getNow(DateUtil.HMS_FORMAT),
					DateOper.getNow(DateUtil.YMD_FORMAT));
			JSONObject rtJs =  JSONObject.fromObject(rtJson);
			rt  = JSONObject.fromObject(ApiUtil
					.getJsonRt(10000, "成功"));
			rt.put("Result", rtJs.getJSONArray("result"));
			return rt.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	/**
	 * 设置医生停止时间
	 * 
	 * @param 
	 * @param 
	 * @return
	 */
	public String setDcPauseWork(InterfaceMessage msg) {
		String tag = "setDcPauseWork";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			Integer doctorUid = json.get("doctorUid") == null ? null : json
					.getInt("doctorUid");
			String startDate = json.get("startDate") == null ? null : json
					.getString("startDate");;
			String endDate = json.get("endDate") == null ? null : json
					.getString("endDate");;
			String startTime = json.get("startTime") == null ? null : json
					.getString("startTime");;
			String endTime =  json.get("endTime") == null ? null : json
					.getString("endTime");;
			String doctorName =  json.get("doctorName") == null ? null : json
							.getString("doctorName");		
			QuesMainVo qmv = new QuesMainVo();
			qmv.setASK_DoctorID(doctorUid);
			JSONObject rt = new JSONObject();
			MytPauseworkBean pausework = new MytPauseworkBean();
			pausework.setOperconfid(doctorUid);
			pausework.setStartdate(new Timestamp(DateOper.parse(
					startDate).getTime()));
			pausework.setEnddate(new Timestamp(DateOper.parse(
					endDate).getTime()));
			pausework.setStarttime(startTime);
			pausework.setEndtime(endTime);
			pausework.setState(1);
			pausework.setOpertime(DateOper.getNowDateTime());
			pausework.setOperatorname(doctorName);
			pausework.setOperatorid(doctorUid.toString());
			ReturnValue rtPa = pauseService.addPauseForWeb(pausework);
			if(rtPa.getCode()>0){
				if(rtPa.getCode() == 20001){
					rt  = JSONObject.fromObject(ApiUtil
							.getJsonRt(10000, rtPa.getMessage()));
				}else{
					rt  = JSONObject.fromObject(ApiUtil
							.getJsonRt(10000, "成功"));
				}
				JSONObject pas = new JSONObject();
				pas.put("pauseid", rtPa.getCode());
				rt.put("Result",pas );
			}else{
				rt  = JSONObject.fromObject(ApiUtil
						.getJsonRt(-14444, "设置停诊失败"));
			}
			return rt.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	/**
	 * 取消停诊（删除）
	 * 
	 * @param 
	 * @param 
	 * @return
	 */
	public String deleteDcPauseWork(InterfaceMessage msg) {
		String tag = "updateDcPauseWork";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			Integer pauseWorkID = json.get("pauseWorkID") == null ? null : json
					.getInt("pauseWorkID");
			Integer doctorUid = json.get("doctorUid") == null ? null : json
					.getInt("doctorUid");;
			String doctorName = json.get("doctorName") == null ? null : json
					.getString("doctorName");;
			/*String startTime = json.get("startTime") == null ? null : json
					.getString("startTime");;
			String endTime =  json.get("endTime") == null ? null : json
					.getString("endTime");;*/
			/*qmv.setASK_DoctorID(doctorUid);
		
			MytPauseworkBean pausework = new MytPauseworkBean();
			pausework.setOperconfid(doctorUid);
			pausework.setStartdate(new Timestamp(DateOper.parse(
					startDate).getTime()));
			pausework.setEnddate(new Timestamp(DateOper.parse(
					endDate).getTime()));
			pausework.setStarttime(startTime);
			pausework.setEndtime(endTime);*/
			JSONObject rt = new JSONObject();
			OperatorInfo op = new OperatorInfo();
			op.setOperatorID(doctorUid);
			op.setOperatorName(doctorName);
			ReturnValue rtPa = pauseService.delete(pauseWorkID,op);
			if(rtPa.getCode()>0){
				rt  = JSONObject.fromObject(ApiUtil
						.getJsonRt(10000, "成功"));
			}else{
				rt  = JSONObject.fromObject(ApiUtil
						.getJsonRt(-14444,rtPa.getMessage()));
			}
			return rt.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	/**
	 * 获取未回答数
	 * 
	 * @param 
	 * @param 
	 * @return
	 */
	public String getDoctorWaitAnswerCount(InterfaceMessage msg) {
		String tag = "getDoctorWaitAnswerCount";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			Integer doctorUid = json.get("doctorUid") == null ? null : json
					.getInt("doctorUid");
			QuesMainVo qmv = new QuesMainVo();
			qmv.setASK_DoctorID(doctorUid);
			String rtTime = quesMainService.quesListForDoctorWaitAnswerCountNoFreeLastTime(qmv);
			Integer rtCount = quesMainService 
				.quesListForDoctorWaitAnswerCountNoFree(qmv);
			JSONObject  con = new JSONObject();
			con.put("Total", rtCount);
			if(StringUtil.isNotEmpty(rtTime)){
				JSONObject time = JSONObject.fromObject(rtTime); 
				con.put("LastTime", time.getJSONArray("result").getJSONObject(0).get("lasttime"));
			}
			JSONObject rt = JSONObject.fromObject(ApiUtil
					.getJsonRt(10000, "成功"));
			rt.put("Result", con);
		return rt.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} 
	}
	
	/**
	 * 问题列表
	 * 
	 * @param 
	 * @param 
	 * @return
	 */
	public String getDoctorConsultingList(InterfaceMessage msg) {
		String tag = "getDoctorConsultingList";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			Integer quesType = json.get("quesType") == null ? null : json
					.getInt("quesType");
			Integer pageSize = json.get("pageSize") == null ? null : json
					.getInt("pageSize");
			Integer pageIndex = json.get("pageIndex") == null ? null : json
					.getInt("pageIndex");
			Integer doctorUid = json.get("doctorUid") == null ? null : json
					.getInt("doctorUid");
			/*Integer deptID = json.get("deptID") == null ? null : json
					.getInt("deptID");*/
			String twoDeptID = json.get("twoDeptID") == null ? null : json
					.getString("twoDeptID");
			QuesMainVo qmv = new QuesMainVo();
			qmv.setASK_DoctorID(doctorUid);
			//qmv.setQD_AskDeptID(deptID);
			qmv.setQD_DeptTwoIDS(twoDeptID);
			Integer rtCount = quesMainService
					.queryDesignationCountQuesListForApp(qmv, quesType);
			String rtStr = "";
			com.yihu.baseinfo.api.DoctorInfoApi doctorInfoApi = Rpc.get(
					DoctorInfoApi.class,
					ConfigUtil.getInstance().getUrl("url.baseinfo"));
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("doctorUid", doctorUid);
			// jsonObj.put("displayStatus", 1);
			jsonObj.put("main", 1);
			jsonObj.put("startRow", 0);
			jsonObj.put("pageSize", 0);
			jsonObj.put("columns", "serviceStatus");
			InterfaceMessage interfacemessage = new InterfaceMessage();
			interfacemessage.setParam(jsonObj.toString());
			int searchType =1;
			JSONObject dcRt = JSONObject.fromObject(doctorInfoApi
					.queryComplexDoctorList(interfacemessage));
			int countDoc = dcRt.getInt("Total");
		
			if(quesType==1){  
				rtCount = quesMainService 
						.quesListForDoctorAnswerAndOverCount(qmv );
				rtStr = quesMainService.quesListForDoctorAnswerAndOver(
						qmv,   pageSize, pageIndex);
			}else if (quesType ==2){
				rtCount = quesMainService
						.quesListForDoctorAnswerNoOverCount(qmv );
				rtStr = quesMainService.quesListForDoctorAnswerNoOver(
						qmv, pageSize, pageIndex);
			}else if (quesType == 3){
				JSONArray disease = new JSONArray();
				PostService ps = new PostService();
				JSONObject doctorDisease =JSONObject.fromObject(ps.getDoctorDiseaseList(doctorUid.toString())) ;
				if(doctorDisease.getInt("Code")>0){
					searchType = doctorDisease.getInt("searchType");
					if(searchType == 2){
						disease = doctorDisease.getJSONArray("Result");
						if(disease.size()==0){
							return ApiUtil.getJsonRt(-10001, "未设置疾病数据，无法检索");
						}
					}
				}
				rtCount = quesMainService
						.quesListForDoctorWaitAnswerCount(qmv,searchType,disease.toString());
				rtStr = quesMainService.quesListForDoctorWaitAnswer(
						qmv,   pageSize, pageIndex,searchType,disease.toString());
			}
			if (StringUtil.isEmpty(rtStr)) {
				return ApiUtil.getJsonRt(-20000, "获取数据失败");
			}
			/*rtStr = quesMainService.queryDesignationQuesListForApp(qmv,
					quesType, pageSize, pageIndex);
			if (StringUtil.isEmpty(rtStr)) {
				return ApiUtil.getJsonRt(-20000, "获取数据失败");
			}*/
			QuesMainVo qusVo = new QuesMainVo();
			qusVo.setASK_DoctorID(doctorUid);
			int ct = quesMainService.querCountOverQus(qusVo);
			/*if (countDoc > 0 && quesType == 3 && rtCount <= 0 &&ct<1) {	
				String serviceStatus = dcJson.getJSONObject(0).getString(
						"serviceStatus");
				if (serviceStatus.substring(1, 2).equals("0")) {
				ReplyVo rpl = new ReplyVo();
				rpl.setASK_DoctorID(doctorUid);
				rpl.setREPLY_UserType(1);
				rpl.setREPLY_Status(1);
				int count = replyService.queryReplyCount(rpl);
				if (count <= 0) {
					rtCount = quesMainService
							.queryQuestionLibraryCountForApp(qmv, quesType);
					rtStr = quesMainService.queryQuestionLibraryListForApp(
							qmv, quesType, pageSize, pageIndex);
					if (StringUtil.isEmpty(rtStr)) {
						return ApiUtil.getJsonRt(-20000, "获取数据失败");
					}
				}
				}
			}*/
			JSONObject rtJson = JSONObject.fromObject(rtStr);
			JSONObject rt = JSONObject.fromObject(ApiUtil
					.getJsonRt(10000, "成功"));
			JSONArray rtResult = rtJson.getJSONArray("result");
			for (int i = 0; i < rtResult.size(); i++) {
			}
			rt.put("Result", rtJson.getJSONArray("result"));
			rt.put("Total", rtCount);
			rt.put("PageSize", pageSize);
			rt.put("PageIndex", pageIndex);
			return rt.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}catch(SmallDepartmentListNullException e){
			e.printStackTrace();
			JSONObject rt = JSONObject.fromObject(ApiUtil
					.getJsonRt(-80000, "标准科室错误！"));
			return  rt.toString();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	
	
	/**
	 * 问题列表(加入可以取医生列表)
	 * 
	 * @param 
	 * @param 
	 * @return
	 */
	public String getDoctorConsultingListV2(InterfaceMessage msg) {
		String tag = "getDoctorConsultingListV2";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			Integer quesType = json.get("quesType") == null ? null : json
					.getInt("quesType");
			Integer pageSize = json.get("pageSize") == null ? null : json
					.getInt("pageSize");
			Integer pageIndex = json.get("pageIndex") == null ? null : json
					.getInt("pageIndex");
			Integer doctorUid = json.get("doctorUid") == null ? null : json
					.getInt("doctorUid");
			String doctorUids = json.get("doctorUids") == null ? null : json
					.getString("doctorUids");
			Integer userid =  json.get("userID") == null ? null : json
					.getInt("userID");
			/*Integer deptID = json.get("deptID") == null ? null : json
					.getInt("deptID");*/
			String twoDeptID = json.get("twoDeptID") == null ? null : json
					.getString("twoDeptID");
			QuesMainVo qmv = new QuesMainVo();
			qmv.setASK_DoctorID(doctorUid);
			//qmv.setQD_AskDeptID(deptID);
			qmv.setQD_DeptTwoIDS(twoDeptID);
			qmv.setDoctorUids(doctorUids);
			qmv.setASK_UserID(userid);
			Integer rtCount = quesMainService
					.queryDesignationCountQuesListForApp(qmv, quesType);
			String rtStr = "";
			com.yihu.baseinfo.api.DoctorInfoApi doctorInfoApi = Rpc.get(
					DoctorInfoApi.class,
					ConfigUtil.getInstance().getUrl("url.baseinfo"));
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("doctorUid", doctorUid);
			// jsonObj.put("displayStatus", 1);
			int searchType =1;
			jsonObj.put("main", 1);
			jsonObj.put("startRow", 0);
			jsonObj.put("pageSize", 0);
			jsonObj.put("columns", "serviceStatus");
			InterfaceMessage interfacemessage = new InterfaceMessage();
			interfacemessage.setParam(jsonObj.toString());
			int countDoc = 0;
			if(doctorUid > 0){
				JSONObject dcRt = JSONObject.fromObject(doctorInfoApi
						.queryComplexDoctorList(interfacemessage));
				  countDoc = dcRt.getInt("Total");
			} 
			if(quesType==1){  
				rtCount = quesMainService 
						.quesListForDoctorAnswerAndOverCount(qmv );
				rtStr = quesMainService.quesListForDoctorAnswerAndOver(
						qmv,   pageSize, pageIndex);
			}else if (quesType ==2){
				rtCount = quesMainService
						.quesListForDoctorAnswerNoOverCount(qmv );
				rtStr = quesMainService.quesListForDoctorAnswerNoOver(
						qmv, pageSize, pageIndex);
			}else if (quesType == 3){
				JSONArray disease = new JSONArray();
				PostService ps = new PostService();
				JSONObject doctorDisease =JSONObject.fromObject(ps.getDoctorDiseaseListTest(doctorUid.toString())) ;
				if(doctorDisease.getInt("Code")>0){
					searchType = doctorDisease.getInt("searchType");
					if(searchType == 2){
						disease = doctorDisease.getJSONArray("Result");
						if(disease.size()==0){
							return ApiUtil.getJsonRt(-14444, "未设置疾病数据，无法检索");
						}
					}
				}
				rtCount = quesMainService
						.quesListForDoctorWaitAnswerCount(qmv,searchType,disease.toString());
				rtStr = quesMainService.quesListForDoctorWaitAnswer(
						qmv,   pageSize, pageIndex,searchType,disease.toString());
			}
			if (StringUtil.isEmpty(rtStr)) {
				return ApiUtil.getJsonRt(-20000, "当前无新咨询");
			}
			/*rtStr = quesMainService.queryDesignationQuesListForApp(qmv,
					quesType, pageSize, pageIndex);
			if (StringUtil.isEmpty(rtStr)) {
				return ApiUtil.getJsonRt(-20000, "获取数据失败");
			}*/
			/*QuesMainVo qusVo = new QuesMainVo();
			qusVo.setASK_DoctorID(doctorUid);
			int ct = quesMainService.querCountOverQus(qusVo);
			if (countDoc > 0 && quesType == 3 && rtCount <= 0 &&ct<1) {	
				String serviceStatus = dcJson.getJSONObject(0).getString(
						"serviceStatus");
				if (serviceStatus.substring(1, 2).equals("0")) {
				ReplyVo rpl = new ReplyVo();
				rpl.setASK_DoctorID(doctorUid);
				rpl.setREPLY_UserType(1);
				rpl.setREPLY_Status(1);
				int count = replyService.queryReplyCount(rpl);
				if (count <= 0) {
					rtCount = quesMainService
							.queryQuestionLibraryCountForApp(qmv, quesType);
					rtStr = quesMainService.queryQuestionLibraryListForApp(
							qmv, quesType, pageSize, pageIndex);
					if (StringUtil.isEmpty(rtStr)) {
						return ApiUtil.getJsonRt(-20000, "获取数据失败");
					}
				}
				}
			}*/
			JSONObject rtJson = JSONObject.fromObject(rtStr);
			JSONObject rt = JSONObject.fromObject(ApiUtil
					.getJsonRt(10000, "成功"));
			JSONArray rtResult = rtJson.getJSONArray("result");
			for (int i = 0; i < rtResult.size(); i++) {
			}
			rt.put("Result", rtJson.getJSONArray("result"));
			rt.put("Total", rtCount);
			rt.put("PageSize", pageSize);
			rt.put("PageIndex", pageIndex);
			return rt.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}catch(SmallDepartmentListNullException e){
			e.printStackTrace();
			JSONObject rt = JSONObject.fromObject(ApiUtil
					.getJsonRt(-80000, "标准科室错误！"));
			return  rt.toString();
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	
	/**
	 * 问题列表
	 * 
	 * @param 
	 * @param 
	 * @return
	 */
	public String getDoctorConsultingListV3(InterfaceMessage msg) {
	
		String tag = "getDoctorConsultingListV3";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			Integer quesType = json.get("quesType") == null ? null : json
					.getInt("quesType");
			Integer pageSize = json.get("pageSize") == null ? null : json
					.getInt("pageSize");
			Integer pageIndex = json.get("pageIndex") == null ? null : json
					.getInt("pageIndex");
			Integer doctorUid = json.get("doctorUid") == null ? null : json
					.getInt("doctorUid");
			/*
			 * Integer deptID = json.get("deptID") == null ? null : json
			 * .getInt("deptID");
			 */
			/*String twoDeptID = json.get("twoDeptID") == null ? null : json
					.getString("twoDeptID");*/
			QuesMainVo qmv = new QuesMainVo();
			qmv.setASK_DoctorID(doctorUid);
			// qmv.setQD_AskDeptID(deptID);
			//qmv.setQD_DeptTwoIDS(twoDeptID);
			Integer rtCount = 0 ;
			/*quesMainService
					.queryDesignationCountQuesListForApp(qmv, quesType);*/
			String rtStr = "";
			/*com.yihu.baseinfo.api.DoctorInfoApi doctorInfoApi = Rpc.get(
					DoctorInfoApi.class,
					ConfigUtil.getInstance().getUrl("url.baseinfo"));*/
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("doctorUid", doctorUid);
			// jsonObj.put("displayStatus", 1);
			jsonObj.put("main", 1);
			jsonObj.put("pageIndex", 1);
			jsonObj.put("pageSize", 100);
			jsonObj.put("columns", "serviceStatus,provinceId");
			InterfaceMessage interfacemessage = new InterfaceMessage();
			interfacemessage.setParam(jsonObj.toString());
			int searchType = 1;
			/*JSONObject dcRt = JSONObject.fromObject(doctorInfoApi
					.queryComplexDoctorList(interfacemessage));*/
			JSONObject dcRt  =JSONObject.fromObject(postService.queryComplexDoctorList_v2(jsonObj.toString())) ;
			int code = dcRt.getInt("Code");
			int proid = 0;
			if(code > 0 ){
				JSONArray rts= dcRt.getJSONArray("Result");
				if(rts.size() == 0){
					return ApiUtil.getJsonRt(-10011, "未找到医生信息。");
				}
				proid = rts.getJSONObject(0).getInt("provinceId");
			}else{
				return ApiUtil.getJsonRt(-10011, "未找到医生信息。");
			}
			if (quesType == 1) {
				rtCount = quesMainService
						.quesListForDoctorAnswerAndOverCount(qmv);
				rtStr = quesMainService.quesListForDoctorAnswerAndOver(qmv,
						pageSize, pageIndex);
			} else if (quesType == 2) {
				rtCount = quesMainService
						.quesListForDoctorAnswerNoOverCount(qmv);
				rtStr = quesMainService.quesListForDoctorAnswerNoOver(qmv,
						pageSize, pageIndex);
			} else if (quesType == 3) {
				rtCount = quesMainService.quesListForDoctorWaitAnswerCountNoFreeQues(qmv);
				rtStr = quesMainService.quesListForDoctorWaitAnswerNoFreeQues(qmv,
						pageSize, pageIndex);
			}else if(quesType == 4){
				PostService ps = new PostService();
				JSONObject doctorDisease = JSONObject.fromObject(ps
						.getDoctorDiseaseList(doctorUid.toString()));
				if (doctorDisease.getInt("Code") > 0) {
					DoctorQueSeachStoneVo vo = new DoctorQueSeachStoneVo();
					vo.setDoctorUid(doctorUid);
					vo.setState(1);
					searchType = doctorDisease.getInt("searchType");
					int type;
					if (searchType == 2) {
						vo.setSeachType(1);
						type = 1;
					} else {
						vo.setSeachType(2);
						type = 2;
					}
					JSONArray stones = JSONArray
							.fromObject(doctorQueSeachStoneService
									.queryDoctorQueSeachStoneListByCondition(vo));
					if(stones.size()>0){
						rtCount = quesMainService.quesListForDoctorWaitAnswerFreeQuesCount(qmv,type,stones.toString());
						rtStr = quesMainService.quesListForDoctorWaitAnswerFreeQues(qmv,
								pageSize, pageIndex,type,stones.toString(),proid );
					}
					
				}else{
					return ApiUtil.getJsonRt(-10008, "未设置感兴趣的科室或者疾病。");
				}
			}
			if (StringUtil.isEmpty(rtStr)) {
				return ApiUtil.getJsonRt(-20000, "已被领光了， 刷新看看");
			}
			
			JSONObject rtJson = JSONObject.fromObject(rtStr);
			JSONObject rt = JSONObject.fromObject(ApiUtil
					.getJsonRt(10000, "成功"));
			JSONArray rtResult = rtJson.getJSONArray("result");
			for (int i = 0; i < rtResult.size(); i++) {
			}
			rt.put("Result", rtJson.getJSONArray("result"));
			rt.put("Total", rtCount);
			rt.put("PageSize", pageSize);
			rt.put("PageIndex", pageIndex);
			return rt.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}catch(SmallDepartmentListNullException e){
			e.printStackTrace();
			JSONObject rt = JSONObject.fromObject(ApiUtil
					.getJsonRt(-80000, "标准科室错误！"));
			return  rt.toString();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	
	
	/**
	 * 问题列表(科室咨询)
	 * 
	 * @param 
	 * @param 
	 * @return
	 */
	public String getDoctorDeptsList(InterfaceMessage msg) {
		String tag = "getDoctorDeptsList";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			Integer quesType = json.get("quesType") == null ? null : json
					.getInt("quesType");
			Integer pageSize = json.get("pageSize") == null ? null : json
					.getInt("pageSize");
			Integer pageIndex = json.get("pageIndex") == null ? null : json
					.getInt("pageIndex");
			Integer doctorUid = json.get("doctorUid") == null ? null : json
					.getInt("doctorUid");
			Integer deptID = json.get("deptID") == null ? null : json
					.getInt("deptID");
			/*String twoDeptID = json.get("twoDeptID") == null ? null : json
					.getString("twoDeptID");*/
			QuesMainVo qmv = new QuesMainVo();
			qmv.setASK_DoctorID(doctorUid);
			//qmv.setQD_AskDeptID(deptID);
			qmv.setQD_AskDeptID(deptID);
			Integer rtCount =  0;
			String rtStr = "";
			/*com.yihu.baseinfo.api.DoctorInfoApi doctorInfoApi = Rpc.get(
					DoctorInfoApi.class,
					ConfigUtil.getInstance().getUrl("url.baseinfo"));
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("doctorUid", doctorUid);
			// jsonObj.put("displayStatus", 1);
			jsonObj.put("main", 1);
			jsonObj.put("startRow", 0);
			jsonObj.put("pageSize", 0);
			jsonObj.put("columns", "serviceStatus");
			InterfaceMessage interfacemessage = new InterfaceMessage();
			interfacemessage.setParam(jsonObj.toString());
			JSONObject dcRt = JSONObject.fromObject(doctorInfoApi
					.queryComplexDoctorList(interfacemessage));
			int countDoc = dcRt.getInt("Total");*/

			if(quesType==1){  
				rtCount = quesMainService 
						.quesListForDoctorAnswerForDeptsAndOverCountChangzheng(qmv );
				rtStr = quesMainService.quesListForDoctorAnswerForDeptsAndOverChangzheng(
						qmv,   pageSize, pageIndex);
			}else if (quesType ==2){
				rtCount = quesMainService
						.quesListForDoctorAnswerNoOverDeptsCount(qmv );
				rtStr = quesMainService.quesListForDoctorAnswerDeptsNoOver(
						qmv, pageSize, pageIndex);
			}else if (quesType == 3){
				rtCount = quesMainService.
						quesListForDoctorWaitAnswerDeptsCount(qmv);
				rtStr = quesMainService.quesListForDoctorWaitAnswerDepts(
						qmv,   pageSize, pageIndex);
			}
			/*if (StringUtil.isEmpty(rtStr)) {
				return ApiUtil.getJsonRt(-20000, "获取数据失败");
			}*/
			/*rtStr = quesMainService.queryDesignationQuesListForApp(qmv,
					quesType, pageSize, pageIndex);
			if (StringUtil.isEmpty(rtStr)) {
				return ApiUtil.getJsonRt(-20000, "获取数据失败");
			}*/
			/*QuesMainVo qusVo = new QuesMainVo();
			qusVo.setASK_DoctorID(doctorUid);
			int ct = quesMainService.querCountOverQus(qusVo);
			if (countDoc > 0 && quesType == 3 && rtCount <= 0 &&ct<1) {	
				String serviceStatus = dcJson.getJSONObject(0).getString(
						"serviceStatus");
				if (serviceStatus.substring(1, 2).equals("0")) {
				ReplyVo rpl = new ReplyVo();
				rpl.setASK_DoctorID(doctorUid);
				rpl.setREPLY_UserType(1);
				rpl.setREPLY_Status(1);
				int count = replyService.queryReplyCount(rpl);
				if (count <= 0) {
					rtCount = quesMainService
							.queryQuestionLibraryCountForApp(qmv, quesType);
					rtStr = quesMainService.queryQuestionLibraryListForApp(
							qmv, quesType, pageSize, pageIndex);
					if (StringUtil.isEmpty(rtStr)) {
						return ApiUtil.getJsonRt(-20000, "获取数据失败");
					}
				}
				}
			}*/
			JSONObject rtJson = JSONObject.fromObject(rtStr);
			JSONObject rt = JSONObject.fromObject(ApiUtil
					.getJsonRt(10000, "成功"));
			JSONArray rtResult = rtJson.getJSONArray("result");
			for (int i = 0; i < rtResult.size(); i++) {
			}
			rt.put("Result", rtJson.getJSONArray("result"));
			rt.put("Total", rtCount);
			rt.put("PageSize", pageSize);
			rt.put("PageIndex", pageIndex);
			return rt.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	/**
	 * 保存问题不付费
	 * 
	 * @param
	 * @params
	 * @return
	 */
	public String saveQuestionNoFreeze(InterfaceMessage msg) {
		String tag = "saveQuestionNoFreeze";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			// System.out.print(msg.getParam());
			Integer doctorID = json.get("doctorID") == null ? 0 : json
					.getInt("doctorID");
			String tipPhone = json.get("tipPhone") == null ? null : json
					.getString("tipPhone");
			Integer userID = json.get("userID") == null ? 0 : json
					.getInt("userID");
			Integer deptID = json.get("deptID") == null ? 0 : json
					.getInt("deptID");
			String userName = json.get("userName") == null ? null : json
					.getString("userName");
			String content = json.get("content") == null ? null : json
					.getString("content");
			Integer sourceType = json.get("sourceType") == null ? 0 : json
					.getInt("sourceType");
			Integer filesCount = json.get("filesCount") == null ? 0 : json
					.getInt("filesCount");
			Integer handid = json.get("handid") == null ? 0 : json
					.getInt("handid");
			Integer Platform = json.get("platform") == null ? 0 : json
					.getInt("platform");
			String diseaseStr = json.get("diseaseStr") == null ? null : json
					.getString("diseaseStr");
			Integer price = json.get("price") == null ? 0 : json.getInt("price");
			Integer feeTemplateId = json.get("feeTemplateId") == null ? 0 : json
					.getInt("feeTemplateId");
			String DeptList = json.get("deptList") == null ? null : json
					.getString("deptList");
			Integer quesType = json.get("quesType") == null ? null : json
					.getInt("quesType");
			Integer docFreeID = json.get("docFreeID") == null ? 0 : json
					.getInt("docFreeID");
			JSONObject patientMess = json.get("patientMess") == null ? null
					: json.getJSONObject("patientMess");
			JSONArray fileMess = json.get("fileMess") == null ? null : json
					.getJSONArray("fileMess");
			
			Integer coVoID = 0;
			com.yihu.baseinfo.api.DoctorInfoApi doctorInfoApi = Rpc.get(
					DoctorInfoApi.class,
					ConfigUtil.getInstance().getUrl("url.baseinfo"));
			com.yihu.baseinfo.api.CommonApi commonApi = Rpc.get(
					CommonApi.class,
					ConfigUtil.getInstance().getUrl("url.baseinfo"));
			com.yihu.baseinfo.api.HosDeptApi hosDeptApi = Rpc.get(
					HosDeptApi.class,
					ConfigUtil.getInstance().getUrl("url.baseinfo"));
			String deptIds = "";
			

			QuesMainVo qvo = new QuesMainVo();
			PatientVo pat = new PatientVo();
			qvo.setQUESMAIN_Content(content);
			qvo.setASK_UserID(userID);
			int qsCon = quesMainService.querySameQueCont(qvo);
			if (qsCon > 0) {
				return WsUtil.getRetVal(msg.getOutType(), -2000, "您所咨询的问题已存在。");
			}
			if (docFreeID > 0) {
				UserFreeCountVo uFreeCount = new UserFreeCountVo();
				uFreeCount.setUserId(userID);
				uFreeCount.setUfcId(docFreeID);
				uFreeCount.setIfOver(0);
				uFreeCount = userFreeCountService
						.queryUserFreeCount(uFreeCount);
				if (uFreeCount == null) {
					return WsUtil
							.getRetVal(msg.getOutType(), -2000, "该抢答已经过期.");
				} else {
					uFreeCount.setIfOver(1);
					uFreeCount.setEndTime(DateUtil.dateFormat(new Date()));
					int rt = userFreeCountService.updateUserFree(uFreeCount);
					if (rt < 0) {
						return WsUtil.getRetVal(msg.getOutType(), -14444,
								"更新抢答数据失败.");
					}
					UserFreeCountVo ufc = new UserFreeCountVo();
					ufc.setUfcId(docFreeID);
					ufc = userFreeCountService.queryUserFree(ufc);
					DoctorFreeCountVo dvo = new DoctorFreeCountVo();
					dvo.setDfcId(ufc.getDfcId());
					dvo = doctorFreeCountService.queryDoctorFreeCount(dvo);
					dvo.setOccupyFreeCount(dvo.getOccupyFreeCount() - 1);
					dvo.setLastChangeTime(DateUtil.dateFormat(new Date()));
					int rtd = doctorFreeCountService
							.updateDoctorFreeCountForUpFreeCount(dvo);
					if (rtd < 0) {
						return WsUtil.getRetVal(msg.getOutType(), -14444,
								"更新抢答数据失败.");
					}
				}
			}
			qvo.setQD_TipPhone(tipPhone);
			qvo.setASK_SourceAskID(0);
			qvo.setQD_DiseaseStr(diseaseStr);
			qvo.setQD_AskedCount(0);
			qvo.setQD_Ating(0);
			qvo.setQD_CheckManID(0);
			qvo.setQD_ClickCount(0);
			qvo.setQD_Credits(0);
			qvo.setQD_DocReplayCount(0);
			qvo.setQD_HandleID(1);
			qvo.setQD_IsAppAttend(0);
			qvo.setQD_IsReplay(0);
			qvo.setQD_IsUserReplay(1);
			qvo.setQD_Reason("");
			qvo.setQD_Status(0);
			qvo.setQD_SupplyContent("");
			qvo.setQUESMAIN_IsOpen(1);
			qvo.setQUESMAIN_Platform(Platform); // 来自
			qvo.setQUESMAIN_Status(1);
			qvo.setQD_ShowStatus(",0,");
			qvo.setQD_CheckTime(DateUtil.dateFormat(new Date()));
			qvo.setQD_RecordExpiredTime(DateUtil.dateFormat(new Date()));
			qvo.setQD_DiseaseIDS("");
			qvo.setASK_UserID(userID);
			qvo.setASK_UserName(userName);
			qvo.setQUESMAIN_Content(content);
			qvo.setQUESMAIN_CreateTime(DateUtil.dateFormat(new Date()));
			qvo.setQUESMAIN_Title("");
			qvo.setQD_FilesCount(filesCount);
			qvo.setQD_QuesType(quesType);
			qvo.setQD_DocFreeID(docFreeID);
			Calendar mfNow = Calendar.getInstance();
			mfNow.setTime(new Date());
			mfNow.add(Calendar.YEAR, 100);
			Date mf = mfNow.getTime();
			qvo.setQUESMAIN_ExpiredTime(DateUtil.dateFormat(mf)); // 如果公共2问题a失效时间+100
			qvo.setQD_OrdersId("");
			qvo.setQD_OrdersStatus(0); // 未生成订单.set不用支付s
			qvo.setQD_Price(0);
			qvo.setQD_SourceType(sourceType); // 1公共2问题a
			qvo.setASK_DoctorID(0);
			qvo.setQD_AskDeptID(0);
			qvo.setQD_DeptIDS("");
			qvo.setQD_DeptTwoIDS("");
			// 患者数据保存
			pat.setPATIENT_Name(patientMess.getString("patname"));
			pat.setPATIENT_Birth(patientMess.getString("patbirth"));
			pat.setPATIENT_Sex(patientMess.getInt("patsex"));
			pat.setASK_CityID(patientMess.getInt("patcityid"));
			pat.setASK_CityName(patientMess.getString("patcityname"));
			pat.setASK_ProvinceID(patientMess.getInt("patprvid"));
			pat.setASK_ProvinceName(patientMess.getString("patprvname"));
			int patID = patientService.insertPatient(pat);

			if (patID < 0) {
				return WsUtil.getRetVal(msg.getOutType(), -14444, "插入患者失败。");
			}
			qvo.setASK_PatientID(patID);
			qvo.setQD_FilesCount(filesCount);
			// 保存数据
			FilesVo file;
			String filsIds = "";
			if (filesCount > 0) {
				for (int i = 0; i < fileMess.size(); i++) {
					file = new FilesVo();
					file.setFILES_Status(fileMess.getJSONObject(i).getInt(
							"status"));
					file.setFILES_CreateTime(fileMess.getJSONObject(i)
							.getString("createTime"));
					file.setFILES_OldName(fileMess.getJSONObject(i).getString(
							"oldName"));
					file.setFILES_NewName(fileMess.getJSONObject(i).getString(
							"newName"));
					file.setFILES_TypeID(fileMess.getJSONObject(i).getInt(
							"typeID"));
					file.setFILES_ObjTypeID(fileMess.getJSONObject(i).getInt(
							"objTypeID"));
					file.setFILES_Path(fileMess.getJSONObject(i).getString(
							"path"));
					file.setFILES_Size(fileMess.getJSONObject(i).getInt("size"));
					file.setFILES_OperatorID(fileMess.getJSONObject(i).getInt(
							"operatorID"));
					int rtFlid = filesService.insertFiles(file);
					if (rtFlid < 0) {
						return WsUtil.getRetVal(msg.getOutType(), -14444,
								"附件保存失败。");
					}
					filsIds = filsIds + rtFlid + ",";
				}
			}

			// qvo.setASK_DepartID(String.valueOf(rt));
			ConsumerOrdersVo coVo = new ConsumerOrdersVo();
			if (sourceType == 0 && doctorID > 0)// 指定医生
			{
				ServiceResult<String> rtdc = doctorInfoApi
						.getComplexDoctorByUid(doctorID);
				if (rtdc.getCode() > 0) {
					JSONObject dc = JSONObject.fromObject(rtdc.getResult());
					qvo.setASK_SourceAskID(dc.getInt("doctorUid"));
					qvo.setASK_DoctorID(dc.getInt("doctorUid"));
					qvo.setQD_CheckStatus(1);
					qvo.setQUESMAIN_Platform(1);
					qvo.setQD_DeptIDS(dc.getString("bigDepartment"));// 标准科室
					qvo.setQD_DeptTwoIDS(dc.getString("standardDeptId"));// 标准二级科室
					qvo.setQD_AskDeptID(dc.getInt("hosDeptId"));// 医生所在科室
					qvo.setQD_Price(price);
					qvo.setQD_OrdersStatus(1);
					if (price > 0 || quesType == 1) {
						Calendar dcNow = Calendar.getInstance();
						dcNow.setTime(new Date());
						dcNow.add(Calendar.DAY_OF_YEAR, 2);
						Date dc1 = dcNow.getTime();
						qvo.setQUESMAIN_ExpiredTime(DateUtil.dateFormat(dc1));// 收费问题过期24小时
					} else {
						Calendar dcNow = Calendar.getInstance();
						dcNow.setTime(new Date());
						dcNow.add(Calendar.YEAR, 100);
						Date dc1 = dcNow.getTime();
						qvo.setQUESMAIN_ExpiredTime(DateUtil.dateFormat(dc1));// 72小时过去
						// （免费问题）
						// 保存流水
					}
				} else {
					return WsUtil.getRetVal(msg.getOutType(), -14444,
							"指定医生信息不存在。");
				}
			} else if ((sourceType == 2 || sourceType == 3) && deptID > 0) {
				// 指定科室咨询
				qvo.setQD_CheckStatus(1);
				qvo.setQD_Price(price);
				ServiceResult<String> hos = hosDeptApi
						.queryComplexHosDeptById(deptID);
				if (hos.getCode() > 0) {
					JSONObject hospt = JSONObject.fromObject(hos.getResult());
					qvo.setASK_SourceAskID(deptID);
					qvo.setQD_DeptIDS(hospt.getString("bigDepartment"));// 标准科室
					qvo.setQD_DeptTwoIDS(hospt.getString("standardDeptId"));// 标准二级科室
					qvo.setQD_AskDeptID(deptID);// 指定的科室咨询所在科室
					Calendar rightNow = Calendar.getInstance();
					rightNow.setTime(new Date());
					rightNow.add(Calendar.DAY_OF_YEAR, 2);
					Date dt1 = rightNow.getTime();
					qvo.setQUESMAIN_ExpiredTime(DateUtil.dateFormat(dt1));// 收费问题24小时过期
					if (price > 0) {// 如果问题收费则需求生成流水订单
						qvo.setQD_OrdersStatus(1); // 未支付
					}
				} else {
					return WsUtil.getRetVal(msg.getOutType(), -14444,
							"指定医生信息不存在。");
				}
			} else {
				String bigDeptIds = ",";
				String ltDeptIds = ",";
				JSONObject dpjson = new JSONObject();
				dpjson.put("deptIds", DeptList);
				if (DeptList.equals("") || DeptList == null
						|| DeptList.equals("0")) {
					qvo.setQD_CheckStatus(0);
				} else {
					ServiceResult<String> rtdepts = commonApi
							.getSmallDepartmentList(dpjson.toString());
					if (rtdepts.getCode() > 0) {
						JSONArray depts = JSONArray.fromObject(rtdepts
								.getResult());
						DepartmentsVo deptVo;
						for (int i = 0; i < depts.size(); i++) {
							deptVo = new DepartmentsVo();
							deptVo.setASK_DepartIDOne(depts.getJSONObject(i)
									.getInt("deptSn"));
							deptVo.setASK_DepartNameOne(depts.getJSONObject(i)
									.getString("bigName"));
							deptVo.setASK_DepartIDTwo(depts.getJSONObject(i)
									.getString("deptId"));
							deptVo.setASK_DepartNameTwo(depts.getJSONObject(i)
									.getString("name"));
							deptVo.setDEPART_OperatorType(0);
							deptVo.setDEPART_CreateTime(DateUtil
									.dateFormat(new Date()));
							int rtdp = departmentsService
									.insertDepartments(deptVo);
							deptIds = deptIds + rtdp + ",";
							bigDeptIds = bigDeptIds
									+ depts.getJSONObject(i).getInt("deptSn")
									+ ",";
							ltDeptIds = ltDeptIds
									+ depts.getJSONObject(i)
											.getString("deptId") + ",";
						}
						qvo.setQD_CheckStatus(1);
					}
				}
				qvo.setQD_DeptIDS(bigDeptIds);
				qvo.setQD_DeptTwoIDS(ltDeptIds);
			}

			qvo.setQD_HandleID(handid);
			qvo.setQD_SourceType(sourceType);

			// 保存问题
			int quID = quesMainService.insertQuesMain(qvo);
			if (quID < 0) {
				return WsUtil.getRetVal(msg.getOutType(), -14444, "保存问题失败。");
			}
			if (price > 0) {
				// 生成流水
				coVo.setASK_QuesID(quID);
				coVo.setASK_Content(content);
				coVo.setASK_QuesType(7);
				coVo.setASK_UserID(userID);
				coVo.setCO_CreateTime(DateUtil.dateFormat(new Date()));
				coVo.setCO_Price(price);
				coVo.setCO_Status(-2);
				coVo.setASK_DoctorID(doctorID);
				coVo.setASK_DeptID(deptID);
				coVo.setFeeTemplateId(feeTemplateId);
				if (sourceType == 2) {
					coVo.setCO_AskType(1);
				} else {
					coVo.setCO_AskType(0);
				}
				coVoID = consumerOrdersService.insertConsumerOrders(coVo);
				if (coVoID == 0) {
					return WsUtil
							.getRetVal(msg.getOutType(), -14444, "生成订单失败。");
				}
				qvo.setQD_OrdersId(String.valueOf(coVoID));
				/*
				 * // 冻结用户账户金额 ReturnValueBean ret =
				 * accountService.frozenWswyFee(userID, price, coVoID, "12",
				 * "01", operatorID, operatorName); if (ret.getCode() < 0) {
				 * return WsUtil.getRetVal(msg.getOutType(), -14444,
				 * ret.getMessage()); }
				 */
			} else {
				QuesMainVo qusVo = new QuesMainVo();
				qusVo.setQUESMAIN_ID(quID);
				qusVo.setQD_OrdersStatus(0);
				quesMainService.updateQuesMain(qusVo);
			}
			/*
			 * QuesMainVo qusVo = new QuesMainVo(); qusVo.setQUESMAIN_ID(quID);
			 * qusVo.setQD_OrdersStatus(2);
			 * quesMainService.updateQuesMain(qusVo);
			 */
			// 更新附件表ID
			FilesVo fl;
			fl = new FilesVo();
			fl.setFILES_ObjID(quID);
			if (StringUtil.isNotEmpty(filsIds)) {
				filsIds = StringUtils.substringBeforeLast(filsIds, ",");
				int rtfl = filesService.updateFilesForQuesID(fl, filsIds);
				if (rtfl < 0) {
					return WsUtil.getRetVal(msg.getOutType(), -14444,
							"更新附件数据失败。");
				}
			}

			// 更新免费咨询部门表
			if (StringUtil.isNotEmpty(deptIds)) {
				deptIds = StringUtils.substringBeforeLast(deptIds, ",");
				DepartmentsVo dVo = new DepartmentsVo();
				dVo.setASK_QuesID(quID);
				int dert = departmentsService.updateDepartmentsForQuesID(dVo,
						deptIds);
				if (dert < 0) {
					return WsUtil.getRetVal(msg.getOutType(), -14444,
							"更新免费咨询部门列表失败。");
				}
			}
			// 更新订单表ID数据
			/*
			 * ConsumerOrdersVo cVo = new ConsumerOrdersVo(); if (coVoID != 0) {
			 * cVo.setASK_QuesID(quID); cVo.setCO_ID(coVoID);
			 * cVo.setCO_Status(0); int rtcon = consumerOrdersService
			 * .updateConsumerOrdersByCondition(cVo); if (rtcon < 0) { return
			 * WsUtil.getRetVal(msg.getOutType(), -14444, "更新订单数据失败。");
			 * 
			 * } }
			 */
			if (price == 0) {
				QuesReplyMutualVo qrmVo = new QuesReplyMutualVo();
				qrmVo.setQueID(quID);
				qrmVo.setReplyCount(1);
				qrmVo.setReplyType(0);
				qrmVo.setLastUpdateTime(DateUtil.dateFormat(new Date()));
				qrmVo.setBeginTime(DateUtil.dateFormat(new Date()));
				Calendar rightNow = Calendar.getInstance();
				rightNow.setTime(new Date());
				rightNow.add(Calendar.YEAR, 100);
				Date dt1 = rightNow.getTime();
				qrmVo.setEndTime(DateUtil.dateFormat(dt1));
				int rtQRM = quesReplyMutualService.insertQuesReplyMutual(qrmVo);
				if (rtQRM < 0) {
					return ApiUtil.getJsonRt(-14444, "生成交互数据失败。");
				}
			}

			JSONObject qus = new JSONObject();
			qus.put("quertionID", quID);
			JSONObject rtJs = new JSONObject();
			rtJs.put("Code", 10000);
			rtJs.put("Message", "成功");
			JSONArray rtJr = new JSONArray();
			rtJr.add(qus);
			rtJs.put("Result", rtJr.toString());
			return rtJs.toString();
			// return WsUtil.getRetVal(msg.getOutType(), 1, qus.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	
	/**
	 * 根据问题ID获取问题和回复详细信息列表模块
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public String getQueReplyByQuesID(InterfaceMessage msg) {
		String tag = "getQueReplyByQuesID";
		try {
			
			
			
			com.yihu.account.api.IAccountService accountService = Rpc.get(
					IAccountService.class,
					ConfigUtil.getInstance().getUrl("url.account"));
			com.yihu.baseinfo.api.DoctorInfoApi doctorInfoApi = Rpc.get(
					DoctorInfoApi.class,
					ConfigUtil.getInstance().getUrl("url.baseinfo"));
			JSONObject json = JSONObject.fromObject(msg.getParam());
			Integer quID = json.get("quID") == null ? null : json
					.getInt("quID");
			Integer replyID = json.get("replyID") == null ? 0 : json
					.getInt("replyID");
			String replyIDs = json.get("replyIDs") == null ? null : json
					.getString("replyIDs");
			Integer pageSize = json.get("pageSize") == null ? null : json
					.getInt("pageSize");
			Integer pageIndex = json.get("pageIndex") == null ? null : json
					.getInt("pageIndex");
			Integer userType = json.get("userType") == null ? null : json
					.getInt("userType");
			QuesMainVo qusVo = new QuesMainVo();
			qusVo.setQUESMAIN_ID(quID);
			qusVo = quesMainService.queryQuesMainByCondition(qusVo);
			if (qusVo.equals(null) || qusVo == null) {
				return ApiUtil.getJsonRt(-2000, "未获得问题数据.");
			}
			JSONObject queJson = new JSONObject();
			queJson.put("quID", qusVo.getQUESMAIN_ID());
			queJson.put("doctorUid", qusVo.getASK_DoctorID());
			queJson.put("price", qusVo.getQD_Price());
			queJson.put("content", qusVo.getQUESMAIN_Content());
			queJson.put("tipPhone", qusVo.getQD_TipPhone());
			queJson.put("createTime", qusVo.getQUESMAIN_CreateTime());
			queJson.put("handid", qusVo.getQD_HandleID());
			queJson.put("filesCount", qusVo.getQD_FilesCount());
			queJson.put("platform", qusVo.getQUESMAIN_Platform());
			queJson.put("diseaseStr", qusVo.getQD_DiseaseStr());
			queJson.put("userid", qusVo.getASK_UserID());
			queJson.put("sourceType", qusVo.getQD_SourceType());
			queJson.put("expiredTime", qusVo.getQUESMAIN_ExpiredTime());//过期时间
			queJson.put("recordTime", qusVo.getQD_RecordExpiredTime());//抢答过期时间
			queJson.put("pathogenesisTime", qusVo.getQD_PathogenesisTime());//发病时间
			queJson.put("illness", qusVo.getQD_Illness());//主要病症
			queJson.put("treatmentExperience", qusVo.getQD_TreatmentExperience()); //就诊经历
			queJson.put("doctorGetPrice", qusVo.getQD_DoctorGetPrice());//医生获取的收入
			queJson.put("docReplayCount", qusVo.getQD_DocReplayCount());//
			
			JSONObject jsonObj = new JSONObject();
			// System.out.println(que.getASK_DoctorID());
			JSONObject docJson = new JSONObject();
			if (qusVo.getASK_DoctorID() > 0) {
				jsonObj.put("doctorUid", qusVo.getASK_DoctorID());
				jsonObj.put("erviceStatusSearch", 3);
				// jsonObj.put("displayStatus", 1);
				jsonObj.put("main", 1);
				jsonObj.put("startRow", 0);
				jsonObj.put("pageSize", 0);
				jsonObj.put(
						"columns",
						"skill,textPrice,lczcName,hosDeptGuid,hosGuid,lczc,serviceStatus,provinceName,hospitalId,states,serviceStatusSearch,hosName,doctorGuid,doctorUid,doctorName,doctorSex,hosDeptId,deptName,standardDeptId,bigDepartmentName,doctorUid,photoPrefix,photoUri");
				//InterfaceMessage interfacemessage = new InterfaceMessage();
				//interfacemessage.setParam(jsonObj.toString());
				
			//	JSONObject dcRt = JSONObject.fromObject(doctorInfoApi.queryComplexDoctorList(interfacemessage));
				
	
				
				JSONObject dcRt = JSONObject.fromObject(postService.queryComplexDoctorList(jsonObj.toString()));

				 
           		if((Integer) dcRt.get("Code")<0){
				return ApiUtil.getJsonRt(-14444,(String)dcRt.get("Message"));
				}
		
				
				JSONArray dcJson = JSONArray.fromObject(dcRt
						.getJSONArray("Result"));
				int countDoc = dcRt.getInt("Total");
				if (countDoc > 0) {
					docJson.put("skill", dcJson.getJSONObject(0).get("skill"));
					docJson.put("textPrice",
							dcJson.getJSONObject(0).get("textPrice"));
					docJson.put("lczcName",
							dcJson.getJSONObject(0).get("lczcName"));
					docJson.put("hosDeptGuid",
							dcJson.getJSONObject(0).get("hosDeptGuid"));
					docJson.put("hosGuid",
							dcJson.getJSONObject(0).get("hosGuid"));
					docJson.put("lczc", dcJson.getJSONObject(0).get("lczc"));
					docJson.put("serviceStatus",
							dcJson.getJSONObject(0).get("serviceStatus"));
					docJson.put("provinceName",
							dcJson.getJSONObject(0).get("provinceName"));
					docJson.put("hospitalId",
							dcJson.getJSONObject(0).get("hospitalId"));
					docJson.put("states", dcJson.getJSONObject(0).get("states"));
					docJson.put("serviceStatusSearch", dcJson.getJSONObject(0)
							.get("serviceStatusSearch"));
					docJson.put("hosName",
							dcJson.getJSONObject(0).get("hosName"));
					docJson.put("doctorGuid",
							dcJson.getJSONObject(0).get("doctorGuid"));
					docJson.put("doctorUid",
							dcJson.getJSONObject(0).get("doctorUid"));
					docJson.put("doctorName",
							dcJson.getJSONObject(0).get("doctorName"));
					docJson.put("doctorSex",
							dcJson.getJSONObject(0).get("doctorSex"));
					docJson.put("hosDeptId",
							dcJson.getJSONObject(0).get("hosDeptId"));
					docJson.put("deptName",
							dcJson.getJSONObject(0).get("deptName"));
					docJson.put("standardDeptId",
							dcJson.getJSONObject(0).get("standardDeptId"));
					docJson.put("bigDepartmentName", dcJson.getJSONObject(0)
							.get("bigDepartmentName"));
					docJson.put("doctorUid",
							dcJson.getJSONObject(0).get("doctorUid"));
					docJson.put("photoPrefix",
							dcJson.getJSONObject(0).get("photoPrefix"));
					docJson.put("photoUri",
							dcJson.getJSONObject(0).get("photoUri"));
					QuesMainVo qus = new QuesMainVo();
					qus.setASK_DoctorID(qusVo.getASK_DoctorID());
					int count = quesMainService.querCountOverQus(qus);
					docJson.put("doctorQuesCount", count);
				}
			}
				
				

			String url = "http://www.yihu.com";
			JSONArray jary = new JSONArray();
			JSONArray replysJson = new JSONArray();
			FilesVo vo = new FilesVo();
			vo.setFILES_ObjID(quID);
			vo.setFILES_ObjTypeID(1);
			List<FilesVo> fileList = filesService
					.queryFilesListByCondition(vo);
			jary =JSONArray.fromObject(fileList);
			/*if (replyID == null || replyID == 0) {
				for (FilesVo file : fileList) {
					JSONObject rpl = new JSONObject();
					rpl.put("content", file.getFILES_Path());
					String rUrl = file.getFILES_Path();
					if (rUrl != null) {
						if (rUrl.substring(0, 4).equals("http")) {
							rpl.put("content", file.getFILES_Path());
						} else {
							rpl.put("content", url + file.getFILES_Path());
						}
					}
					rpl.put("rpType", 2);
					rpl.put("userType", 2);
					rpl.put("rpType", 2);
					rpl.put("platform", 1);
					rpl.put("filesCount", 0);
					rpl.put("checkTime", file.getFILES_CreateTime());
					replysJson.add(rpl);
				}
			}*/
			queJson.put("fileMess", jary);
			PatientVo pVo = new PatientVo();
			pVo.setPATIENT_ID(qusVo.getASK_PatientID());
			pVo = patientService.queryPatient(pVo);
			if (pVo == null) {
				return ApiUtil.getJsonRt(-14444, "未获得患者数据.");
			}
			JSONObject pjson = new JSONObject();
			pjson.put("patname", pVo.getPATIENT_Name());
			pjson.put("patsex", pVo.getPATIENT_Sex());
			pjson.put("patprvid", pVo.getASK_ProvinceID());
			pjson.put("patcityid", pVo.getASK_CityID());
			pjson.put("patprvname", pVo.getASK_ProvinceName());
			pjson.put("patcityname", pVo.getASK_CityName());
			pjson.put("patbirth", pVo.getPATIENT_Birth());
			BalanceReturnBean userBalance = accountService.getAllBalance(
					qusVo.getASK_UserID(), "12", "01");
			queJson.put("userBalance", userBalance.getAvailableBalance());
			ReplyVo reply = new ReplyVo();
			reply.setASK_QuesID(qusVo.getQUESMAIN_ID());
			reply.setREPLY_ID(replyID);
			if(StringUtil.isNotEmpty(replyIDs)){
				if(replyIDs!= "0"){
					reply.setReplyIDs(replyIDs);
				}
			}
			List<ReplyVo> replys = replyService.queryReplyListByCondition(
					reply, pageIndex, pageSize);
			int total = replyService.queryReplyCount(reply);

			for (ReplyVo rp : replys) {
				JSONObject rply = new JSONObject();
				if (rp.getREPLY_Type() == 2 || rp.getREPLY_Type() == 1) {
					String rtUrl = rp.getREPLY_Content();
					if (rtUrl != null) {
						if (rtUrl.substring(0, 4).equals("http")) {
							rply.put("content", rp.getREPLY_Content());
						} else {
							rply.put("content", url + rp.getREPLY_Content());
						}
					}
				} else {
					rply.put("content", rp.getREPLY_Content());
				}
				rply.put("replyid", rp.getREPLY_ID());
				rply.put("userType", rp.getREPLY_UserType());
				rply.put("rpType", rp.getREPLY_Type());
				rply.put("platform", rp.getREPLY_Platform());
				rply.put("filesCount", rp.getREPLY_FilesCount());
				rply.put("checkTime", rp.getREPLY_CheckTime());
				replysJson.add(rply);
				if (rp.getREPLY_FilesCount() > 0 && rp.getREPLY_Type() == 3) {
					FilesVo file = new FilesVo();
					file.setFILES_ObjID(rp.getREPLY_ID());
					file.setFILES_ObjTypeID(2);
					List<FilesVo> fls = filesService
							.queryFilesListByCondition(file);
					for (FilesVo flvo : fls) {
						rply = new JSONObject();
						rply.put("replyid", rp.getREPLY_ID());
						if (flvo.getFILES_Path() != null) {
							if (flvo.getFILES_Path().substring(0, 4).equals("http")) {
								rply.put("content", flvo.getFILES_Path());
							} else {
								rply.put("content", url + flvo.getFILES_Path());
							}
						}else{
							rply.put("content","");
						}
						rply.put("userType", rp.getREPLY_UserType());
						rply.put("rpType", 2);
						rply.put("platform", rp.getREPLY_Platform());
						rply.put("filesCount", 0);
						rply.put("checkTime", rp.getREPLY_CheckTime());
						replysJson.add(rply);
					}
				}
			}
			queJson.put("replys", replysJson);
			queJson.put("doctor", docJson);
			queJson.put("patientMess", pjson);
			QuesReplyMutualVo qrmVo = new QuesReplyMutualVo();
			qrmVo.setQueID(quID);
			qrmVo = quesReplyMutualService.queryQuesReplyMutual(qrmVo);
			if (qrmVo == null) {
				qrmVo = new QuesReplyMutualVo();
				qrmVo.setQueID(quID);
				qrmVo.setReplyCount(0);
				if (userType == 0) {
					qrmVo.setReplyType(0);
				} else {
					qrmVo.setReplyType(1);
				}
				qrmVo.setLastUpdateTime(DateUtil.dateFormat(new Date()));
				qrmVo.setBeginTime(DateUtil.dateFormat(new Date()));
				Calendar rightNow = Calendar.getInstance();
				rightNow.setTime(new Date());
				rightNow.add(Calendar.YEAR, 100);
				Date dt1 = rightNow.getTime();
				qrmVo.setEndTime(DateUtil.dateFormat(dt1));
				int rtQRM = quesReplyMutualService.insertQuesReplyMutual(qrmVo);
				if (rtQRM < 0) {
					return ApiUtil.getJsonRt(-14444, "生成交互数据失败。");
				}
			} else {
				if (qrmVo.getReplyType() == 1 && userType == 0) {
					qrmVo.setReplyType(0);
					qrmVo.setReplyCount(0);
					qrmVo.setLastUpdateTime(DateUtil.dateFormat(new Date()));
					int rtQRM = quesReplyMutualService
							.updateQuesReplyMutual(qrmVo);
					if (rtQRM < 0) {
						return ApiUtil.getJsonRt(-14444, "生成交互数据失败。");
					}
				} else if (qrmVo.getReplyType() == 0 && userType == 1) {
					qrmVo.setReplyType(1);
					qrmVo.setReplyCount(0);
					qrmVo.setLastUpdateTime(DateUtil.dateFormat(new Date()));
					int rtQRM = quesReplyMutualService
							.updateQuesReplyMutual(qrmVo);
					if (rtQRM < 0) {
						return ApiUtil.getJsonRt(-14444, "生成交互数据失败。");
					}
				}
			}
			JSONObject rt = JSONObject.fromObject(ApiUtil
					.getJsonRt(10000, "成功"));
			rt.put("Result", queJson);
			rt.put("Total", total);
			return rt.toString();
		} catch (JSONException e) {
			log.error(LoggerUtil.getTrace(e));
			log.error(msg.getParam());
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			log.error(LoggerUtil.getTrace(e));
			log.error(msg.getParam());
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}

	public String saveDoctorSeachStone(InterfaceMessage msg) {
		String tag = "saveDoctorSeachStone";
		try {
			
			log.error("saveDoctorSeachStone+++请求入参：：："+msg.getParam());
			
			JSONObject json = JSONObject.fromObject(msg.getParam());
			String doctorStone = json.get("doctorStone") == null ? null : json
					.getString("doctorStone");
			Integer saveType = json.get("saveType") == null ? 0 : json
					.getInt("saveType");
			JSONObject rtJson = JSONObject.fromObject(ApiUtil.getJsonRt(10000,
					"操作成功"));
			JSONArray stones = JSONArray.fromObject(doctorStone);
			DoctorQueSeachStoneVo vo;
			
			
			
			
			if(stones.size()>0){
				if (saveType == 1) {
					//清除原先数据  （state =0 ）  ，保存集合   不能超过 5 个感兴趣 
					if(stones.size()>5){
						return  ApiUtil.getJsonRt(-100001,	"感兴趣的科室不能超过5个！");
					}
					
					int doctorUid = stones.getJSONObject(0).getInt("doctorUid");
					int seachType=  stones.getJSONObject(0).getInt("seachType");
					DoctorQueSeachStoneVo uvo = new DoctorQueSeachStoneVo();
					uvo.setDoctorUid(doctorUid);
					uvo.setState(0);
					uvo.setSeachType(seachType);
					doctorQueSeachStoneService
							.updateDoctorQueSeachStoneForDoctorUid(uvo);
					for(int i =0;i<stones.size();i++){
						vo = new DoctorQueSeachStoneVo();
						vo.setDoctorUid(stones.getJSONObject(i).getInt("doctorUid"));
						vo.setNum(stones.getJSONObject(i).getInt("num"));
						vo.setSeachID(stones.getJSONObject(i).getString("seachID"));
						vo.setSeachType(stones.getJSONObject(i).getInt("seachType"));
						vo.setState(stones.getJSONObject(i).getInt("state"));
						vo.setOperTime(DateUtil.dateFormat(new Date()));
						doctorQueSeachStoneService.insertDoctorQueSeachStone(vo);
					}
				}else{
					//不清除原先数据  ，保存单条    不能超过 5 个感兴趣 

					DoctorQueSeachStoneVo qvo = new DoctorQueSeachStoneVo();
					qvo.setDoctorUid(stones.getJSONObject(0).getInt("doctorUid"));
					qvo.setState(1);
					List<DoctorQueSeachStoneVo>  vos= doctorQueSeachStoneService.queryDoctorQueSeachStoneListByCondition(qvo);
					
					if(vos.size()>5&&stones.getJSONObject(0).getInt("seachType")==2){
						return  ApiUtil.getJsonRt(-100001,	"感兴趣的科室不能超过5个！");
					}
					
					
					vo = new DoctorQueSeachStoneVo();
					vo.setDoctorUid(stones.getJSONObject(0).getInt("doctorUid"));
					vo.setNum(stones.getJSONObject(0).getInt("num"));
					vo.setSeachID(stones.getJSONObject(0).getString("seachID"));
					vo.setSeachType(stones.getJSONObject(0).getInt("seachType"));
					vo.setState(stones.getJSONObject(0).getInt("state"));
					vo.setOperTime(DateUtil.dateFormat(new Date()));
					int rt =doctorQueSeachStoneService.insertDoctorQueSeachStoneRt(vo);
					JSONObject rts = new JSONObject();
					rts.put("id", rt);
					JSONArray rtJ = new JSONArray();
					rtJ.add(rts);
					rtJson.put("Result", rtJ);
				}
			}
			return rtJson.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	
	public String deleteDoctorSeachStone(InterfaceMessage msg) {
		String tag = "deleteDoctorSeachStone";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			Integer id = json.get("id") == null ? 0 : json
					.getInt("id");
			JSONObject rtJson = JSONObject.fromObject(ApiUtil.getJsonRt(10000,
					"操作成功"));
			DoctorQueSeachStoneVo vo = new DoctorQueSeachStoneVo() ;
			vo.setID(id);
			vo.setState(0);
			doctorQueSeachStoneService.updateDoctorQueSeachStone(vo);
			return rtJson.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	
	public String getDoctorSeachStone(InterfaceMessage msg) {
		String tag = "getDoctorSeachStone";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			Integer doctorUid = json.get("doctorUid") == null ? null : json
					.getInt("doctorUid");
			Integer seachType = json.get("seachType") == null ? null : json
					.getInt("seachType");
			JSONObject rtJson = JSONObject.fromObject(ApiUtil.getJsonRt(10000,
					"操作成功"));
			DoctorQueSeachStoneVo vo = new DoctorQueSeachStoneVo();
			vo.setDoctorUid(doctorUid);
			vo.setSeachType(seachType);
			vo.setState(1);
			JSONArray stones = JSONArray.fromObject(doctorQueSeachStoneService.queryDoctorQueSeachStoneListByCondition(vo))	;
			int total = doctorQueSeachStoneService.queryDoctorQueSeachStoneCountByCondition(vo);
			rtJson.put("Result", stones);
			rtJson.put("Total", total);
			return rtJson.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	
	/**
	 * 冻结用户费用
	 * 
	 * @param
	 * @param
	 * @return
	 */

	public String quesFreezeForUpdate(InterfaceMessage msg) {
		String tag = "quesFreeze";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			int operatorID = json.get("OperatorId") == null ? null : json
					.getInt("OperatorId");
			String operatorName = json.get("OperatorName") == null ? null
					: json.getString("OperatorName");
			int userID = json.get("userID") == null ? null : json
					.getInt("userID");
			int quID = json.get("quID") == null ? null : json.getInt("quID");
			com.yihu.baseinfo.api.DoctorInfoApi doctorInfoApi = Rpc.get(
					DoctorInfoApi.class,
					ConfigUtil.getInstance().getUrl("url.baseinfo"));
			/*
			 * int price = json.get("price") == null ? null :
			 * json.getInt("price");
			 */
			/*
			 * int feeTemplateId = json.get("feeTemplateId") == null ? null :
			 * json .getInt("feeTemplateId");
			 */
			com.yihu.account.api.IAccountService accountService = Rpc.get(
					IAccountService.class,
					ConfigUtil.getInstance().getUrl("url.account"));
			// 冻结用户账户金额
			ConsumerOrdersVo conVo = new ConsumerOrdersVo();
			conVo.setASK_QuesID(quID);
			conVo = consumerOrdersService.queryConsumerOrdersByQuesID(conVo);
			if (conVo == null || conVo.equals(null)) {
				return ApiUtil.getJsonRt(-14444, "获取订单数据失败。");
			}
			
			ReturnValueBean ret = accountService.frozenWswyFee(userID,
					conVo.getCO_Price(), conVo.getCO_ID(), "12", "01",
					operatorID, operatorName);
			if (ret.getCode() < 0) {
				return ApiUtil.getJsonRt(-14444, ret.getMessage());
			}
			QuesMainVo qusVo = new QuesMainVo();
			qusVo.setQUESMAIN_ID(quID);
			qusVo.setQD_OrdersStatus(2);
			int rt = quesMainService.updateQuesMain(qusVo);
			if (rt < 0) {
				return ApiUtil.getJsonRt(-14444, "更新订单数据失败。");
			}
			ConsumerOrdersVo cVo = new ConsumerOrdersVo();
			cVo.setASK_QuesID(quID);
			cVo.setCO_ID(conVo.getCO_ID());
			cVo.setCO_Status(0);
			int rtcon = consumerOrdersService
					.updateConsumerOrdersByCondition(cVo);
			if (rtcon < 0) {
				return ApiUtil.getJsonRt(-14444, "更新订单数据失败。");
			}
			String s1 = DateUtil.dateFormat(new Date(), DateUtil.YMD_FORMAT);
			String s2 = s1 + " 09:00:00";
			String s3 = s1 + " 22:00:00";
			if (DateUtil.getIfInTime(s2, s3) && conVo.getASK_DoctorID() > 0) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("doctorUid", conVo.getASK_DoctorID());
				jsonObj.put("erviceStatusSearch", 3);
				// jsonObj.put("displayStatus", 1);
				jsonObj.put("main", 1);
				jsonObj.put("startRow", 0);
				jsonObj.put("pageSize", 0);
				jsonObj.put("columns", "doctorAccount,doctorName");
				InterfaceMessage dcMsg = new InterfaceMessage();
				dcMsg.setParam(jsonObj.toString());
				JSONObject dcRt = JSONObject.fromObject(doctorInfoApi
						.queryComplexDoctorList(dcMsg));
				JSONArray dcJson = JSONArray.fromObject(dcRt
						.getJSONArray("Result"));
				int countDoc = dcRt.getInt("Total");
				if (countDoc < 0) {
					return ApiUtil.getJsonRt(10000, "获取短信信息失败。");
				}
				/*PublicForSmsService sms = Rpc.get(PublicForSmsService.class,
						ConfigUtil.getInstance().getUrl("url.smsgw"), 16000);
				sms.send(
						dcJson.getJSONObject(0).getString("doctorAccount"),
						"尊敬的"
								+ dcJson.getJSONObject(0).getString(
										"doctorName")
										+ "医生，您好！刚刚有一位信赖您的患者向您提交了咨询，正等待您的解答！马上登录健康之路手机APP ：http://m.yihu.cn/d32或医护网进行回复吧！",
										10111210);*/
			}
			QuesReplyMutualVo qrmVo = new QuesReplyMutualVo();
			qrmVo.setQueID(quID);
			qrmVo.setReplyCount(1);
			qrmVo.setReplyType(0);
			qrmVo.setLastUpdateTime(DateUtil.dateFormat(new Date()));
			qrmVo.setBeginTime(DateUtil.dateFormat(new Date()));
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(new Date());
			rightNow.add(Calendar.YEAR, 100);
			Date dt1 = rightNow.getTime();
			qrmVo.setEndTime(DateUtil.dateFormat(dt1));
			int qvoNo = quesReplyMutualService.queryQuesReplyMutualCountByCondition(qrmVo);
			if(qvoNo ==0){
				int rtQRM = quesReplyMutualService.insertQuesReplyMutual(qrmVo);
				if (rtQRM < 0) {
					return ApiUtil.getJsonRt(-14444, "生成交互数据失败。");
				}
			}
			BalanceReturnBean rtb = accountService.getAllBalance(userID, "0",
					"0");
			JSONObject balance = new JSONObject();
			balance.put("balance", rtb.getAvailableBalance());
			balance.put("frozenBalance", rtb.getFrozenBalance());
			balance.put("specialAmounts", rtb.getSpecialAmounts());
			balance.put("frozenSpecialBalance", rtb.getFrozenSpecialBalance());
			balance.put("giftAmounts", rtb.getGiftAmounts());
			balance.put("balanceNew", rtb.getBalance());
			balance.put("frozenGiftBalance", rtb.getFrozenGiftBalance());
			 
			JSONArray Result = new JSONArray();
			Result.add(balance);
			JSONObject rtJS = JSONObject.fromObject(ApiUtil.getJsonRt(10000,
					"冻结成功"));
			rtJS.put("Result", Result);
			JSONObject pushJson = new JSONObject();
			JSONObject mesJson = new JSONObject();
			mesJson.put("sourceid",conVo.getASK_UserID());
			mesJson.put("interactType", "100000");// 用户回复
			mesJson.put("targetid", quID);
			mesJson.put("content", quID);
			pushJson.put("Type", "100000");// 用户回复
			pushJson.put("SendCode", conVo.getASK_DoctorID());
			pushJson.put("SendKind", 10);
			pushJson.put("RecvKind", 11);
			pushJson.put("SendName", "APP");
			pushJson.put("SendWay", 1);
			pushJson.put("RecvWay", "12");
			pushJson.put("AppKey", "554d8e9ac52715ddca89311a");
			pushJson.put("masterSecret", "97c59461bce86655c94f8f6f");
			pushJson.put("RecvCode", conVo.getASK_DoctorID());
			pushJson.put("Content", "APP");
			pushJson.put("ContentExtend", mesJson);
			// System.out.println(pushJson);
			MsgManagerService sms = Rpc.get(MsgManagerService.class, ConfigUtil
					.getInstance().getUrl("url.MsgManager"), 3000);
			InterfaceMessage pushMsg = new InterfaceMessage();
			pushMsg.setParam(pushJson.toString());
			//String rtMsg = sms.pushMsg(pushMsg);
			//System.out.println(rtMsg);
			MsgApi msgApi = Rpc.get(MsgApi.class, ConfigUtil.getInstance().getUrl("url.jpushMsg"));
			JSONObject nMesJson = new JSONObject();
			JSONObject androidJson = new JSONObject();
			JSONObject iosJson = new JSONObject();
			androidJson.put("uri", "");
			androidJson.put("date",DateUtil.dateFormat(new Date()));
			androidJson.put("sourceId", userID);
			androidJson.put("sourceName", "");
			androidJson.put("contentId", rt);
			androidJson.put("content", conVo.getASK_Content());
			androidJson.put("questionId", quID);
			//androidJson.put("title", "您收到1条新咨询，请点击查看。");
			QuesMainVo qu = new QuesMainVo();
			qu.setQUESMAIN_ID(quID);
			qu = quesMainService.queryQuesMainByCondition(qu);
			if( qu.getQUESMAIN_Content().length() > 15 ){
				androidJson.put("title", qu.getQUESMAIN_Content().subSequence(0, 15) +"......");
			}else{
				androidJson.put("title", qu.getQUESMAIN_Content().subSequence(0,  qu.getQUESMAIN_Content().length()-1) +"......");
			}
			androidJson.put("operCode", "12000");
			androidJson.put("msgType", "");
			iosJson.put("questionId", quID);
			if( qu.getQUESMAIN_Content().length() > 15 ){
				iosJson.put("ti", qu.getQUESMAIN_Content().subSequence(0, 15) +"......");
			}else{
				iosJson.put("ti", qu.getQUESMAIN_Content().subSequence(0,  qu.getQUESMAIN_Content().length()-1) +"......");
			}
			//iosJson.put("ti", "您收到1条新咨询，请点击查看。");
			iosJson.put("operCode", 12000);
			iosJson.put("contentId", rt);
			iosJson.put("sourceId", userID);
			iosJson.put("sourceName", "");
			nMesJson.put("androidParam", androidJson);
			nMesJson.put("toAppType", 1);
			nMesJson.put("toUsers", conVo.getASK_DoctorID());
			nMesJson.put("iosParam", iosJson);
			InterfaceMessage pushMsgN = new InterfaceMessage();
			pushMsgN.setParam(nMesJson.toString());
			JSONObject authinfo = new JSONObject();
			authinfo.put("ClientId", "client.myt");
			pushMsgN.setAuthInfo(authinfo.toString());
			//String purt= msgApi.sendMsg(pushMsgN);
			//System.out.println(purt);
			return rtJS.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));

		}
	}
	
	
	
	
	
	
	
	
	

	/**
	 * 问题列表  
	 * @author WUJIAJUN
	 * @time  2014 - 11
	 */
	public String getDoctorConsultingListV4(InterfaceMessage msg) {
		String tag = "getDoctorConsultingListV4";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			Integer quesType = json.get("quesType") == null ? null : json
					.getInt("quesType");
			Integer pageSize = json.get("pageSize") == null ? null : json
					.getInt("pageSize");
			Integer pageIndex = json.get("pageIndex") == null ? null : json
					.getInt("pageIndex");
			Integer doctorUid = json.get("doctorUid") == null ? null : json
					.getInt("doctorUid");

			String twoDeptID = json.get("twoDeptID") == null ? null : json
					.getString("twoDeptID");
			/*
			 * Integer deptID = json.get("deptID") == null ? null : json
			 * .getInt("deptID");
			 */
			/*String twoDeptID = json.get("twoDeptID") == null ? null : json
					.getString("twoDeptID");*/
			QuesMainVo qmv = new QuesMainVo();
			qmv.setASK_DoctorID(doctorUid);
			qmv.setQD_DeptTwoIDS(twoDeptID);
			
			// qmv.setQD_AskDeptID(deptID);
			//qmv.setQD_DeptTwoIDS(twoDeptID);
			Integer rtCount = 0 ;
			/*quesMainService
					.queryDesignationCountQuesListForApp(qmv, quesType);*/
			String rtStr = "";
			/*com.yihu.baseinfo.api.DoctorInfoApi doctorInfoApi = Rpc.get(
					DoctorInfoApi.class,
					ConfigUtil.getInstance().getUrl("url.baseinfo"));*/
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("doctorUid", doctorUid);
			// jsonObj.put("displayStatus", 1);
			jsonObj.put("main", 1);
			jsonObj.put("pageIndex", 1);
			jsonObj.put("pageSize", 100);
			jsonObj.put("columns", "serviceStatus,provinceId");
			InterfaceMessage interfacemessage = new InterfaceMessage();
			interfacemessage.setParam(jsonObj.toString());
			int searchType = 1;
			/*JSONObject dcRt = JSONObject.fromObject(doctorInfoApi
					.queryComplexDoctorList(interfacemessage));*/
			JSONObject dcRt  =JSONObject.fromObject(postService.queryComplexDoctorList_v2(jsonObj.toString())) ;
			int code = dcRt.getInt("Code");
			int proid = 0;
			if(code > 0 ){
				JSONArray rts= dcRt.getJSONArray("Result");
				if(rts.size() == 0){
					return ApiUtil.getJsonRt(-10011, "未找到医生信息。");
				}
				proid = rts.getJSONObject(0).getInt("provinceId");
			}else{
				return ApiUtil.getJsonRt(-10011, "未找到医生信息。");
			}
			
			int type = 0;
			if (quesType == 1) {
				System.out.println("type 1111111");
				int  bacc =rtCount = quesMainService
						.quesListForDoctorAnswerAndOverCount(qmv);
				rtStr = quesMainService.quesListForDoctorAnswerAndOverV2(qmv,
						pageSize, pageIndex);
			} else if (quesType == 2) {
				System.out.println("type 22222");
				rtCount = quesMainService
						.quesListForDoctorAnswerNoOverCount(qmv);
				rtStr = quesMainService.quesListForDoctorAnswerNoOverV2(qmv,
						pageSize, pageIndex);
			} else if (quesType == 3) {
				rtCount = quesMainService.quesListForDoctorWaitAnswerCountNoFreeQues(qmv);
				rtStr = quesMainService.quesListForDoctorWaitAnswerNoFreeQuesV2(qmv,
						pageSize, pageIndex);
			}else if(quesType == 4){
				PostService ps = new PostService();
				JSONObject doctorDisease = JSONObject.fromObject(ps
						.getDoctorDiseaseList(doctorUid.toString()));
				if (doctorDisease.getInt("Code") > 0) {
					DoctorQueSeachStoneVo vo = new DoctorQueSeachStoneVo();
					vo.setDoctorUid(doctorUid);
					vo.setState(1);
					searchType = doctorDisease.getInt("searchType");
				//	int type;
					if (searchType == 2) {
						vo.setSeachType(1);
						type = 1;
					} else {
						vo.setSeachType(2);
						type = 2;
					}
					JSONArray stones = JSONArray
							.fromObject(doctorQueSeachStoneService
									.queryDoctorQueSeachStoneListByCondition(vo));
					if(stones.size()>0){
						rtCount = quesMainService.quesListForDoctorWaitAnswerFreeQuesCount(qmv,type,stones.toString());
						rtStr = quesMainService.quesListForDoctorWaitAnswerFreeQuesV2(qmv,
								pageSize, pageIndex,type,stones.toString(),proid );
					}

				}else{
					return ApiUtil.getJsonRt(-10008, "未设置感兴趣的科室或者疾病。");
				}
			}
		
			if (StringUtil.isEmpty(rtStr)&&type==2) {
				return ApiUtil.getJsonRt(-10008, "没有您感兴趣的科室问题！");
			}if(StringUtil.isEmpty(rtStr)&&type==1){
				return ApiUtil.getJsonRt(-10009, "没有您感兴趣的疾病问题！");
			}
			
			JSONObject rtJson = JSONObject.fromObject(rtStr);
			JSONObject rt = JSONObject.fromObject(ApiUtil
					.getJsonRt(10000, "成功"));
			JSONArray rtResult = rtJson.getJSONArray("result");
			for (int i = 0; i < rtResult.size(); i++) {
			}
			rt.put("Result", rtJson.getJSONArray("result"));
			rt.put("Total", rtCount);
			rt.put("PageSize", pageSize);
			rt.put("PageIndex", pageIndex);
			return rt.toString();
		} catch (JSONException e) {
			log.error(LoggerUtil.getTrace(e));
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}catch(SmallDepartmentListNullException e){
			log.error(LoggerUtil.getTrace(e));
			e.printStackTrace();
			JSONObject rt = JSONObject.fromObject(ApiUtil
					.getJsonRt(-80000, "标准科室错误！"));
			return  rt.toString();
			
		} catch (Exception e) {
			log.error(LoggerUtil.getTrace(e));
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	
	
	
	
	

	/**
	 * 问题列表          注：需要支持 标准科室的查询
	 * @author WUJIAJUN    
	 * @time  2014 - 12-24       
	 */
	public String getDoctorConsultingListV5(InterfaceMessage msg) {
		String tag = "getDoctorConsultingListV5";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			Integer quesType = json.get("quesType") == null ? null : json
					.getInt("quesType");
			Integer pageSize = json.get("pageSize") == null ? null : json
					.getInt("pageSize");
			Integer pageIndex = json.get("pageIndex") == null ? null : json
					.getInt("pageIndex");
			Integer doctorUid = json.get("doctorUid") == null ? null : json
					.getInt("doctorUid");

			String twoDeptID = json.get("twoDeptID") == null ? null : json
					.getString("twoDeptID");
			/*
			 * Integer deptID = json.get("deptID") == null ? null : json
			 * .getInt("deptID");
			 */
			/*String twoDeptID = json.get("twoDeptID") == null ? null : json
					.getString("twoDeptID");*/
			QuesMainVo qmv = new QuesMainVo();
			qmv.setASK_DoctorID(doctorUid);
			qmv.setQD_DeptTwoIDS(twoDeptID);
			
			// qmv.setQD_AskDeptID(deptID);
			//qmv.setQD_DeptTwoIDS(twoDeptID);
			Integer rtCount = 0 ;
			/*quesMainService
					.queryDesignationCountQuesListForApp(qmv, quesType);*/
			String rtStr = "";
			/*com.yihu.baseinfo.api.DoctorInfoApi doctorInfoApi = Rpc.get(
					DoctorInfoApi.class,
					ConfigUtil.getInstance().getUrl("url.baseinfo"));*/
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("doctorUid", doctorUid);
			// jsonObj.put("displayStatus", 1);
			jsonObj.put("main", 1);
			jsonObj.put("pageIndex", 1);
			jsonObj.put("pageSize", 100);
			jsonObj.put("columns", "serviceStatus,provinceId");
			InterfaceMessage interfacemessage = new InterfaceMessage();
			interfacemessage.setParam(jsonObj.toString());
			int searchType = 1;
			/*JSONObject dcRt = JSONObject.fromObject(doctorInfoApi
					.queryComplexDoctorList(interfacemessage));*/
			JSONObject dcRt  =JSONObject.fromObject(postService.queryComplexDoctorList_v2(jsonObj.toString())) ;
			int code = dcRt.getInt("Code");
			int proid = 0;
			if(code > 0 ){
				JSONArray rts= dcRt.getJSONArray("Result");
				if(rts.size() == 0){
					return ApiUtil.getJsonRt(-10011, "未找到医生信息。");
				}
				proid = rts.getJSONObject(0).getInt("provinceId");
			}else{
				return ApiUtil.getJsonRt(-10011, "未找到医生信息。");
			}
			if (quesType == 1) {
				System.out.println("type 1111111");
			        rtCount = quesMainService
						.quesListForDoctorAnswerAndOverCountV5(qmv);

				rtStr = quesMainService.quesListForDoctorAnswerAndOverV5(qmv,
						pageSize, pageIndex);
			} else if (quesType == 2) {
				System.out.println("type 22222");
				rtCount = quesMainService
						.quesListForDoctorAnswerNoOverCount(qmv);
				rtStr = quesMainService.quesListForDoctorAnswerNoOverV2(qmv,
						pageSize, pageIndex);
			} else if (quesType == 3) {
				rtCount = quesMainService.quesListForDoctorWaitAnswerCountNoFreeQues(qmv);
				rtStr = quesMainService.quesListForDoctorWaitAnswerNoFreeQuesV2(qmv,
						pageSize, pageIndex);
			}else if(quesType == 4){
				PostService ps = new PostService();
				JSONObject doctorDisease = JSONObject.fromObject(ps
						.getDoctorDiseaseList(doctorUid.toString()));
				if (doctorDisease.getInt("Code") > 0) {
					DoctorQueSeachStoneVo vo = new DoctorQueSeachStoneVo();
					vo.setDoctorUid(doctorUid);
					vo.setState(1);
					searchType = doctorDisease.getInt("searchType");
					int type;
					if (searchType == 2) {
						vo.setSeachType(1);
						type = 1;
					} else {
						vo.setSeachType(2);
						type = 2;
					}
					JSONArray stones = JSONArray
							.fromObject(doctorQueSeachStoneService
									.queryDoctorQueSeachStoneListByCondition(vo));
					if(stones.size()>0){
						rtCount = quesMainService.quesListForDoctorWaitAnswerFreeQuesCount(qmv,type,stones.toString());
						rtStr = quesMainService.quesListForDoctorWaitAnswerFreeQuesV2(qmv,
								pageSize, pageIndex,type,stones.toString(),proid );
					}
					
				}else{
					return ApiUtil.getJsonRt(-10008, "未设置感兴趣的科室或者疾病。");
				}
			}
			if (StringUtil.isEmpty(rtStr)) {
				return ApiUtil.getJsonRt(-20000, "已被领光了， 刷新看看");
			}
			
			JSONObject rtJson = JSONObject.fromObject(rtStr);
			JSONObject rt = JSONObject.fromObject(ApiUtil
					.getJsonRt(10000, "成功"));
			JSONArray rtResult = rtJson.getJSONArray("result");
			for (int i = 0; i < rtResult.size(); i++) {
			}
			rt.put("Result", rtJson.getJSONArray("result"));
			rt.put("Total", rtCount);
			rt.put("PageSize", pageSize);
			rt.put("PageIndex", pageIndex);
			return rt.toString();
		} catch (JSONException e) {
			log.error(LoggerUtil.getTrace(e));
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch(SmallDepartmentListNullException e){
			e.printStackTrace();
			log.error(LoggerUtil.getTrace(e));
			JSONObject rt = JSONObject.fromObject(ApiUtil
					.getJsonRt(-80000,e.getMessage()));
			return  rt.toString();
			
		} catch (Exception e) {
			log.error(LoggerUtil.getTrace(e));
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	
	
	
	
	
	/**
	 * 查看问题的详细
	 * */

	public String getQueReplyByQuesIDV2(InterfaceMessage msg) {
		String tag = "getQueReplyByQuesIDV2";
		try {
			
			log.error("getQueReplyByQuesIDV2     +++  入参++++"+msg.getParam());
	
			
			com.yihu.account.api.IAccountService accountService = Rpc.get(
					IAccountService.class,
					ConfigUtil.getInstance().getUrl("url.account"));
			com.yihu.baseinfo.api.DoctorInfoApi doctorInfoApi = Rpc.get(
					DoctorInfoApi.class,
					ConfigUtil.getInstance().getUrl("url.baseinfo"));
			JSONObject json = JSONObject.fromObject(msg.getParam());
			Integer quID = json.get("quID") == null ? null : json
					.getInt("quID");
			Integer replyID = json.get("replyID") == null ? 0 : json
					.getInt("replyID");
			String replyIDs = json.get("replyIDs") == null ? null : json
					.getString("replyIDs");
			Integer pageSize = json.get("pageSize") == null ? null : json
					.getInt("pageSize");
			Integer pageIndex = json.get("pageIndex") == null ? null : json
					.getInt("pageIndex");
			Integer userType = json.get("userType") == null ? null : json
					.getInt("userType");
			QuesMainVo qusVo = new QuesMainVo();
			qusVo.setQUESMAIN_ID(quID);
			qusVo = quesMainService.queryQuesMainByCondition(qusVo);
			if (qusVo.equals(null) || qusVo == null) {
				return ApiUtil.getJsonRt(-2000, "未获得问题数据.");
			}
			
			
	

			
			
		try{
			
			// 用queid  获取到 openauth 记录！   获取到 clicks 字段！ +1
			
			OpenAuthVo  openvo =new OpenAuthVo();
			openvo.setQuesMainId(String.valueOf(quID));
			List<OpenAuthVo> openvos= openAuthService.queryOpenAuthListByCondition(openvo);
			
			int  click = openvos.get(0).getClicks();
			
			
			click=click+1;
			
			
			openAuthService.updateClicksByID(click,openvos.get(0).getID());
			
			
			}catch (Exception e) {
				log.error(LoggerUtil.getTrace(e));
				return ApiUtil.getJsonRt(-10001, "点击量初始化失败.");
			}
			
			
			
			
			
			
			JSONObject queJson = new JSONObject();
			queJson.put("quID", qusVo.getQUESMAIN_ID());
			queJson.put("doctorUid", qusVo.getASK_DoctorID());
			queJson.put("openAuthFlag", qusVo.getOpenAuthFlag());
			queJson.put("price", qusVo.getQD_Price());
			queJson.put("content", qusVo.getQUESMAIN_Content());
			queJson.put("tipPhone", qusVo.getQD_TipPhone());
			queJson.put("createTime", qusVo.getQUESMAIN_CreateTime());
			queJson.put("handid", qusVo.getQD_HandleID());
			queJson.put("filesCount", qusVo.getQD_FilesCount());
			queJson.put("platform", qusVo.getQUESMAIN_Platform());
			queJson.put("diseaseStr", qusVo.getQD_DiseaseStr());
			queJson.put("userid", qusVo.getASK_UserID());
			queJson.put("sourceType", qusVo.getQD_SourceType());
			queJson.put("expiredTime", qusVo.getQUESMAIN_ExpiredTime());//过期时间
			queJson.put("recordTime", qusVo.getQD_RecordExpiredTime());//抢答过期时间
			queJson.put("pathogenesisTime", qusVo.getQD_PathogenesisTime());//发病时间
			queJson.put("illness", qusVo.getQD_Illness());//主要病症
			
			queJson.put("QD_OrdersStatus", qusVo.getQD_OrdersStatus());//2014-12-22  订单状态 0.不用支付.没订单1.生成订单未支付 2.支付成功 3.支付失败 4.需要支付，还没订单，未支付  5.退款
			
			queJson.put("treatmentExperience", qusVo.getQD_TreatmentExperience()); //就诊经历
			queJson.put("doctorGetPrice", qusVo.getQD_DoctorGetPrice());//医生获取的收入
			queJson.put("docReplayCount", qusVo.getQD_DocReplayCount());//
			
			JSONObject jsonObj = new JSONObject();
			// System.out.println(que.getASK_DoctorID());
			JSONObject docJson = new JSONObject();
			if (qusVo.getASK_DoctorID() > 0) {
				jsonObj.put("doctorUid", qusVo.getASK_DoctorID());
				jsonObj.put("erviceStatusSearch", 3);
				// jsonObj.put("displayStatus", 1);
				jsonObj.put("main", 1);
				jsonObj.put("startRow", 0);
				jsonObj.put("pageSize", 0);
				jsonObj.put(
						"columns",
						"skill,textPrice,lczcName,hosDeptGuid,hosGuid,lczc,serviceStatus,provinceName,hospitalId,states,serviceStatusSearch,hosName,doctorGuid,doctorUid,doctorName,doctorSex,hosDeptId,deptName,standardDeptId,bigDepartmentName,doctorUid,photoPrefix,photoUri");
				InterfaceMessage interfacemessage = new InterfaceMessage();
				interfacemessage.setParam(jsonObj.toString());
				JSONObject dcRt = JSONObject.fromObject(doctorInfoApi
						.queryComplexDoctorList(interfacemessage));
				JSONArray dcJson = JSONArray.fromObject(dcRt
						.getJSONArray("Result"));
				int countDoc = dcRt.getInt("Total");
				if (countDoc > 0) {
					docJson.put("skill", dcJson.getJSONObject(0).get("skill"));
					docJson.put("textPrice",
							dcJson.getJSONObject(0).get("textPrice"));
					docJson.put("lczcName",
							dcJson.getJSONObject(0).get("lczcName"));
					docJson.put("hosDeptGuid",
							dcJson.getJSONObject(0).get("hosDeptGuid"));
					docJson.put("hosGuid",
							dcJson.getJSONObject(0).get("hosGuid"));
					docJson.put("lczc", dcJson.getJSONObject(0).get("lczc"));
					docJson.put("serviceStatus",
							dcJson.getJSONObject(0).get("serviceStatus"));
					docJson.put("provinceName",
							dcJson.getJSONObject(0).get("provinceName"));
					docJson.put("hospitalId",
							dcJson.getJSONObject(0).get("hospitalId"));
					docJson.put("states", dcJson.getJSONObject(0).get("states"));
					docJson.put("serviceStatusSearch", dcJson.getJSONObject(0)
							.get("serviceStatusSearch"));
					docJson.put("hosName",
							dcJson.getJSONObject(0).get("hosName"));
					docJson.put("doctorGuid",
							dcJson.getJSONObject(0).get("doctorGuid"));
					docJson.put("doctorUid",
							dcJson.getJSONObject(0).get("doctorUid"));
					docJson.put("doctorName",
							dcJson.getJSONObject(0).get("doctorName"));
					docJson.put("doctorSex",
							dcJson.getJSONObject(0).get("doctorSex"));
					docJson.put("hosDeptId",
							dcJson.getJSONObject(0).get("hosDeptId"));
					docJson.put("deptName",
							dcJson.getJSONObject(0).get("deptName"));
					docJson.put("standardDeptId",
							dcJson.getJSONObject(0).get("standardDeptId"));
					docJson.put("bigDepartmentName", dcJson.getJSONObject(0)
							.get("bigDepartmentName"));
					docJson.put("doctorUid",
							dcJson.getJSONObject(0).get("doctorUid"));
					docJson.put("photoPrefix",
							dcJson.getJSONObject(0).get("photoPrefix"));
					docJson.put("photoUri",
							dcJson.getJSONObject(0).get("photoUri"));
					QuesMainVo qus = new QuesMainVo();
					
					qus.setASK_DoctorID(qusVo.getASK_DoctorID());
					qus.setQD_AskDeptID(qusVo.getQD_AskDeptID());
					
					int count = quesMainService.querCountOverQus(qus);
					docJson.put("doctorQuesCount", count);
				}
			}
			String url = "http://www.yihu.com";
			JSONArray jary = new JSONArray();
			JSONArray replysJson = new JSONArray();
			FilesVo vo = new FilesVo();
			vo.setFILES_ObjID(quID);
			vo.setFILES_ObjTypeID(1);
			List<FilesVo> fileList = filesService
					.queryFilesListByCondition(vo);
			jary =JSONArray.fromObject(fileList);
			/*if (replyID == null || replyID == 0) {
				for (FilesVo file : fileList) {
					JSONObject rpl = new JSONObject();
					rpl.put("content", file.getFILES_Path());
					String rUrl = file.getFILES_Path();
					if (rUrl != null) {
						if (rUrl.substring(0, 4).equals("http")) {
							rpl.put("content", file.getFILES_Path());
						} else {
							rpl.put("content", url + file.getFILES_Path());
						}
					}
					rpl.put("rpType", 2);
					rpl.put("userType", 2);
					rpl.put("rpType", 2);
					rpl.put("platform", 1);
					rpl.put("filesCount", 0);
					rpl.put("checkTime", file.getFILES_CreateTime());
					replysJson.add(rpl);
				}
			}*/
			queJson.put("fileMess", jary);
			PatientVo pVo = new PatientVo();
			pVo.setPATIENT_ID(qusVo.getASK_PatientID());
			pVo = patientService.queryPatient(pVo);
			if (pVo == null) {
				return ApiUtil.getJsonRt(-14444, "未获得患者数据.");
			}
			JSONObject pjson = new JSONObject();
			pjson.put("patname", pVo.getPATIENT_Name());
			pjson.put("patsex", pVo.getPATIENT_Sex());
			pjson.put("patprvid", pVo.getASK_ProvinceID());
			pjson.put("patcityid", pVo.getASK_CityID());
			pjson.put("patprvname", pVo.getASK_ProvinceName());
			pjson.put("patcityname", pVo.getASK_CityName());
			pjson.put("patbirth", pVo.getPATIENT_Birth());
			BalanceReturnBean userBalance = accountService.getAllBalance(
					qusVo.getASK_UserID(), "12", "01");
			queJson.put("userBalance", userBalance.getAvailableBalance());
			ReplyVo reply = new ReplyVo();
			reply.setASK_QuesID(qusVo.getQUESMAIN_ID());
			reply.setREPLY_ID(replyID);
			if(StringUtil.isNotEmpty(replyIDs)){
				if(replyIDs!= "0"){
					reply.setReplyIDs(replyIDs);
				}
			}
			List<ReplyVo> replys = replyService.queryReplyListByCondition(
					reply, pageIndex, pageSize);
			int total = replyService.queryReplyCount(reply);

			for (ReplyVo rp : replys) {
				JSONObject rply = new JSONObject();
				if (rp.getREPLY_Type() == 2 || rp.getREPLY_Type() == 1) {
					String rtUrl = rp.getREPLY_Content();
					if (rtUrl != null) {
						if (rtUrl.substring(0, 4).equals("http")) {
							rply.put("content", rp.getREPLY_Content());
						} else {
							rply.put("content", url + rp.getREPLY_Content());
						}
					}
				} else {
					rply.put("content", rp.getREPLY_Content());
				}
				rply.put("replyid", rp.getREPLY_ID());
				rply.put("userType", rp.getREPLY_UserType());
				rply.put("rpType", rp.getREPLY_Type());
				rply.put("platform", rp.getREPLY_Platform());
				rply.put("filesCount", rp.getREPLY_FilesCount());
				rply.put("checkTime", rp.getREPLY_CheckTime());
				replysJson.add(rply);
				if (rp.getREPLY_FilesCount() > 0 && rp.getREPLY_Type() == 3) {
					FilesVo file = new FilesVo();
					file.setFILES_ObjID(rp.getREPLY_ID());
					file.setFILES_ObjTypeID(2);
					List<FilesVo> fls = filesService
							.queryFilesListByCondition(file);
					for (FilesVo flvo : fls) {
						rply = new JSONObject();
						rply.put("replyid", rp.getREPLY_ID());
						if (flvo.getFILES_Path() != null) {
							if (flvo.getFILES_Path().substring(0, 4).equals("http")) {
								rply.put("content", flvo.getFILES_Path());
							} else {
								rply.put("content", url + flvo.getFILES_Path());
							}
						}else{
							rply.put("content","");
						}
						rply.put("userType", rp.getREPLY_UserType());
						rply.put("rpType", 2);
						rply.put("platform", rp.getREPLY_Platform());
						rply.put("filesCount", 0);
						rply.put("checkTime", rp.getREPLY_CheckTime());
						replysJson.add(rply);
					}
				}
			}
			queJson.put("replys", replysJson);
			queJson.put("doctor", docJson);
			queJson.put("patientMess", pjson);
			QuesReplyMutualVo qrmVo = new QuesReplyMutualVo();
			qrmVo.setQueID(quID);
			qrmVo = quesReplyMutualService.queryQuesReplyMutual(qrmVo);
			if (qrmVo == null) {
				qrmVo = new QuesReplyMutualVo();
				qrmVo.setQueID(quID);
				qrmVo.setReplyCount(0);
				if (userType == 0) {
					qrmVo.setReplyType(0);
				} else {
					qrmVo.setReplyType(1);
				}
				qrmVo.setLastUpdateTime(DateUtil.dateFormat(new Date()));
				qrmVo.setBeginTime(DateUtil.dateFormat(new Date()));
				Calendar rightNow = Calendar.getInstance();
				rightNow.setTime(new Date());
				rightNow.add(Calendar.YEAR, 100);
				Date dt1 = rightNow.getTime();
				qrmVo.setEndTime(DateUtil.dateFormat(dt1));
				int rtQRM = quesReplyMutualService.insertQuesReplyMutual(qrmVo);
				if (rtQRM < 0) {
					return ApiUtil.getJsonRt(-14444, "生成交互数据失败。");
				}
			} else {
				if (qrmVo.getReplyType() == 1 && userType == 0) {
					qrmVo.setReplyType(0);
					qrmVo.setReplyCount(0);
					qrmVo.setLastUpdateTime(DateUtil.dateFormat(new Date()));
					int rtQRM = quesReplyMutualService
							.updateQuesReplyMutual(qrmVo);
					if (rtQRM < 0) {
						return ApiUtil.getJsonRt(-14444, "生成交互数据失败。");
					}
				} else if (qrmVo.getReplyType() == 0 && userType == 1) {
					qrmVo.setReplyType(1);
					qrmVo.setReplyCount(0);
					qrmVo.setLastUpdateTime(DateUtil.dateFormat(new Date()));
					int rtQRM = quesReplyMutualService
							.updateQuesReplyMutual(qrmVo);
					if (rtQRM < 0) {
						return ApiUtil.getJsonRt(-14444, "生成交互数据失败。");
					}
				}
			}
			JSONObject rt = JSONObject.fromObject(ApiUtil
					.getJsonRt(10000, "成功"));
			rt.put("Result", queJson);
			rt.put("Total", total);
			return rt.toString();
		} catch (JSONException e) {
			log.error(LoggerUtil.getTrace(e));
			log.error(msg.getParam());
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			log.error(LoggerUtil.getTrace(e));
			log.error(msg.getParam());
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	
	
	
	
	//-============
	
	private static IOpenAuthService openAuthService = Ioc
			.get(IOpenAuthService.class);
	
	/**
	 * 公开（关闭公开）单个问题的接口
	 * @param queID     openflag
	 * @return
	 */
	public String setOneQueOpenOrClose(InterfaceMessage msg) {
	
		String tag = "setOneQueOpenOrClose";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			// 问题id
			Integer queID = json.get("queID") == null ? null : json
					.getInt("queID");
			String openflag = json.get("openflag") == null ? null : json
					.getString("openflag");
			
			OpenAuthVo vo = new OpenAuthVo();
			vo.setQuesMainId(String.valueOf(queID));
			vo.setOpenAuthFlag(openflag);
			int  back =openAuthService.updateOpenAuthFlagByID(vo);
			
			System.out.println(back+"::::2222");
			
			if(back>=0){
				JSONObject rt = JSONObject.fromObject(ApiUtil
						.getJsonRt(10000, "成功"));
				rt.put("Result", null);
				rt.put("Total", null);
				return rt.toString();
			}else{
				
				return  ApiUtil.getJsonRt(-14444,
						"setOneQueOpenOrClose 数据操作 Error，请确定参数是否正确");
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	
	
	
	/**
	 * 医生已有问题全部公开的接口
	 * @param  doctorID openflag
	 * @return
	 */
	public String setDocQueOpenOrClose(InterfaceMessage msg) {
	
		String tag = "setDocQueOpenOrClose";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			// 问题id
			Integer doctorID = json.get("doctorID") == null ? null : json
					.getInt("doctorID");
			String openflag = json.get("openflag") == null ? null : json
					.getString("openflag");
			
			OpenAuthVo vo = new OpenAuthVo();
			vo.setDoctorId(String.valueOf(doctorID));
			vo.setOpenAuthFlag(openflag);
			int  back =openAuthService.updateOpenAuthFlagByDocid(vo);
			
			System.out.println(back+"::::2222");
			
			if(back>=0){
				JSONObject rt = JSONObject.fromObject(ApiUtil
						.getJsonRt(10000, "成功"));
				rt.put("Result", null);
				rt.put("Total", null);
				return rt.toString();
			}else{
				
				return  ApiUtil.getJsonRt(-14444,
						"setDocQueOpenOrClose 数据操作 Error，请确定参数是否正确");
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	
	
	
	
	/**
	 * 新增问题 ，默认公开或不公开 公开的接口
	 * @param  doctorID openflag
	 * @return
	 */
	

	private static IDoctorDefaultAuthService doctorDefaultAuthService = Ioc
	.get(IDoctorDefaultAuthService.class);
	public String setDefualOCstatus(InterfaceMessage msg) {
	
		String tag = "setDefualOCstatus";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			// 问题id
			Integer doctorID = json.get("doctorID") == null ? null : json
					.getInt("doctorID");
			String openflag = json.get("openflag") == null ? null : json
					.getString("openflag");
			
		
			//查询是否存在  这个医生的记录
			DoctorDefaultAuthVo vosearch =  new  DoctorDefaultAuthVo();
			vosearch.setDoctorId(String.valueOf(doctorID));
			
			List<DoctorDefaultAuthVo>  list=  doctorDefaultAuthService.queryDoctorDefaultAuthListByDoctorId(vosearch);
			if(list.size()!=0){
				//传入 医生id  查询 ！  返回 有结果   update
				System.out.println("有记录  。。。。修改");
				DoctorDefaultAuthVo vo =  list.get(0);
				vo.setOpenFlag(openflag);
				int  backnum= doctorDefaultAuthService.updateDoctorDefaultAuthByCondition(vo);
				
				System.out.println(backnum+":::::::");
				
				if(backnum<0){
					//失败
					JSONObject rt = JSONObject.fromObject(ApiUtil
							.getJsonRt(-10004, "失败"));
					rt.put("Result", null);
					rt.put("Total", null);
					return rt.toString();
				}else{
					JSONObject rt = JSONObject.fromObject(ApiUtil
							.getJsonRt(10000, "成功"));
					rt.put("Result", null);
					rt.put("Total", null);
					return rt.toString();
					
				}
			
				
			}else {
				//没有结果 insert
				System.out.println("没有记录  。。。。插入");
				DoctorDefaultAuthVo vo =  new  DoctorDefaultAuthVo();
				vo.setDoctorId(String.valueOf(doctorID));
				vo.setOpenFlag(openflag);
				
				doctorDefaultAuthService.insertDoctorDefaultAuth(vo);
				
				
				JSONObject rt = JSONObject.fromObject(ApiUtil
						.getJsonRt(10000, "成功"));
				rt.put("Result", null);
				rt.put("Total", null);
				return rt.toString();
				
			}

			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	
	
	
	/**
	 * 退回退回咨询 
	 * @param request
	 * @return
	 */
	private static DoctorBillDataApi doctorBillDataApi = Ioc
			.get(DoctorBillDataApi.class);
	public String returnMoneyAndClose(InterfaceMessage msg) {
		try 
		{	
			
			JSONObject json = JSONObject.fromObject(msg.getParam());
			Integer queid = json.get("queID") == null ? null : json
					.getInt("queID");
			
			QuesMainVo vo = new QuesMainVo();
			vo.setQUESMAIN_ID(queid);
			vo = quesMainService.queryQuesMainByCondition(vo);
			InterfaceMessage overMsg = new InterfaceMessage();
			JSONObject overPar = new  JSONObject();
			JSONObject comeFrom = new JSONObject();
			comeFrom.put("ClientId", 0);
			String asrt = "";
			
			
			
			String  closeDocName="";
			String  closeDocID="";
			try {
				//post    用 医生id 查询医生  的信息 
				JSONObject jsObj = new JSONObject();
				jsObj.put("doctorUid", vo.getASK_DoctorID());
				jsObj.put("erviceStatusSearch", 3);
				// jsonObj.put("displayStatus", 1);
				jsObj.put("main", 1);
				jsObj.put("pageIndex", 1);
				jsObj.put("pageSize", 100);
				jsObj.put("columns", "doctorAccount,doctorName");
				net.sf.json.JSONObject dcsRt = 	net.sf.json.JSONObject.fromObject(postService.queryComplexDoctorList_v2(jsObj.toString()));
				net.sf.json.JSONArray dcsJson = net.sf.json.JSONArray.fromObject(dcsRt
						.getJSONArray("Result"));
				net.sf.json.JSONObject  doctornamemmmmmm = (net.sf.json.JSONObject) dcsJson.get(0);
				System.out.println("查询的 json：：："+doctornamemmmmmm.getString("doctorName"));
				
				doctornamemmmmmm.getString("doctorName");
				String.valueOf( vo.getASK_DoctorID());
			
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return  ApiUtil.getJsonRt(-14444,
						"加载异常!" + StringUtil.getException(e));
			}
			
			
			
			
			
			//退回平且关闭
				ConsumerOrdersVo cvo = new ConsumerOrdersVo();
				cvo.setASK_QuesID(queid);
				cvo = consumerOrdersService.queryConsumerOrdersByQuesID(cvo);
				if(StringUtil.isEmpty(cvo)){
					return    ApiUtil.getJsonRt(-14444,
							"订单数据获取失败。" );    
				}
				overPar.put("queID", queid);
				overPar.put("coid",cvo.getCO_ID());
				overPar.put("userid", vo.getASK_UserID());
				overPar.put("OperatorId", "9000023");
				overPar.put("OperatorName", "医护网医生");
				overPar.put("doctorID", vo.getASK_DoctorID());
				
				overMsg.setParam(overPar.toString());
				
				System.out.println("医生的 id：：：：" +vo.getASK_DoctorID());
				asrt = doctorBillDataApi.overQuesRefundforAPI(overMsg);
			
			

			
			com.common.json.JSONObject asrtJs = new com.common.json.JSONObject(asrt);
			if(asrtJs.getInt("Code")<=0){
				return  ApiUtil.getJsonRt(-14444,
						"问题数据获取失败。" );    
			}
			return asrtJs.toString();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			Logger.get().error("com.yihu.myt", LogBody.me().set(e));
			return ApiUtil.getJsonRt(-14444,
					"异常！！！" );    
		}
	}
	
	
	/**
	 *根据医院id（guid/orgId/hospitalId）查询该医院下所有医生回复的问题，根据点击数倒序排序，需要返回问题内容及发布时间，需支持条数入参
	 *
	 *@desc   查询 医院（id）下的所有医生的咨询问题（开放的） 按照点击量desc  支持分页！！！
	 */
	
	
	private static INewDoctorService newDoctorService = Ioc
			.get(INewDoctorService.class); 
	public String queryQuesMainByHospitalID(InterfaceMessage msg) {
	
		String tag = "queryQuesMainByHospitalID";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			String hospital = json.get("hospital") == null ? null : json
					.getString("hospital");
			Integer pageSize = json.get("pageSize") == null ? null : json
					.getInt("pageSize");
			Integer pageIndex = json.get("pageIndex") == null ? null : json
					.getInt("pageIndex");
			
			
			List<HotQuesMainVo> list= newDoctorService.queryQuesMainByHospitalID(hospital);
		
			
			List<HotQuesMainVo>  backlist = new ArrayList<HotQuesMainVo>();
			
			//a 是  从第几页开始
			int a=(pageIndex-1)*pageSize;
			System.out.println(a);
			for (int i = (pageIndex-1)*pageSize; i < a+pageSize; i++) {
				
				if(i>list.size()-1){
					break;
				}else{
				
				backlist.add(list.get(i));
				}
			}

				JSONObject rt = JSONObject.fromObject(ApiUtil
						.getJsonRt(10000, "成功"));
				rt.put("Result", backlist);
				rt.put("Total", list.size());
				return rt.toString();

			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	
	
	
	/**
	 * 根据科室id查询指定科室下所有医生回复过的问题，根据点击数倒序排序，需要返回问题内容及发布时间，需支持条数入参 
	 * @desc  科室来查  同上
	 */
	
	

	public String queryQuesMainByDepartmentID(InterfaceMessage msg) {
	
		String tag = "queryQuesMainByDepartmentID";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			Integer pageSize = json.get("pageSize") == null ? null : json
					.getInt("pageSize");
			Integer pageIndex = json.get("pageIndex") == null ? null : json
					.getInt("pageIndex");

			String department = json.get("department") == null ? null : json
					.getString("department");
			
		
			//TODO
			
			
			List<HotQuesMainVo> list= newDoctorService.queryQuesMainByDepartmentID(department);
		
			
			List<HotQuesMainVo>  backlist = new ArrayList<HotQuesMainVo>();
			
			//a 是  从第几页开始
			int a=(pageIndex-1)*pageSize;
			System.out.println(a);
			for (int i = (pageIndex-1)*pageSize; i < a+pageSize; i++) {
				
				if(i>list.size()-1){
					break;
				}else{
				
				backlist.add(list.get(i));
				}
			}

				JSONObject rt = JSONObject.fromObject(ApiUtil
						.getJsonRt(10000, "成功"));
				rt.put("Result", backlist);
				rt.put("Total", list.size());
				return rt.toString();
			
		


			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	
	
	
	/**
	 * 查询某个医生的 默认开关状态
	 * @param msg
	 * @return
	 */
	
	
	public String queryDefualOCstatus(InterfaceMessage msg) {
		
		String tag = "queryDefualOCstatus";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			// 问题id
			Integer doctorID = json.get("doctorID") == null ? null : json
					.getInt("doctorID");

			
		
			//查询是否存在  这个医生的记录
			DoctorDefaultAuthVo vosearch =  new  DoctorDefaultAuthVo();
			vosearch.setDoctorId(String.valueOf(doctorID));
			
			List<DoctorDefaultAuthVo>  list=  doctorDefaultAuthService.queryDoctorDefaultAuthListByDoctorId(vosearch);
			
			if(list.size()!=0){
				
				System.out.println("有记录  。。把数据返回回去 ！");
				DoctorDefaultAuthVo vo =  list.get(0);
				
				JSONObject backjson = new JSONObject();
				backjson.put("openflag", vo.getOpenFlag());
				
				JSONObject rt = JSONObject.fromObject(ApiUtil
						.getJsonRt(10000, "成功"));
				rt.put("Result", backjson);
				rt.put("Total", null);
				return rt.toString();
				
			}else {
				//这个医生 没有 数据    返回 O
				System.out.println("没有记录  。。。。插入");
				DoctorDefaultAuthVo vo =  new  DoctorDefaultAuthVo();
				vo.setDoctorId(String.valueOf(doctorID));
				vo.setOpenFlag("O");
				
				
				
				
				
				doctorDefaultAuthService.insertDoctorDefaultAuth(vo);
				
				
				
				JSONObject backjson = new JSONObject();
				backjson.put("openflag", "O");
				
				JSONObject rt = JSONObject.fromObject(ApiUtil
						.getJsonRt(10000, "成功"));
				rt.put("Result", backjson);
				rt.put("Total", null);
				return rt.toString();
				
			}

			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	
	
	
	
	//疾病id 查询 问题

	public String queryQuesByDiseaseID(InterfaceMessage msg) {
		
		String tag = "queryQuesByDiseaseID";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());

			Integer diseaseId = json.get("DiseaseId") == null ? null : json
					.getInt("DiseaseId");
			
			
			
			Integer pageSize = json.get("pageSize") == null ? null : json
					.getInt("pageSize");
			
			if(pageSize==null){
				return ApiUtil.getJsonRt(-14444,
					"pageSize 没有值");
			}
			
			Integer pageIndex = json.get("pageIndex") == null ? null : json
					.getInt("pageIndex");
			if(pageIndex==null){
				return ApiUtil.getJsonRt(-14444,
					"pageIndex 没有值");
			}
			

			
		
	
			List<QuesMainVo>  list=  doctorDefaultAuthService.queryQuesByDiseaseID(diseaseId);

			
			List<QuesMainVo>  backlist = new ArrayList<QuesMainVo>();
			
			try{
			
			//a 是  从第几页开始
			int a=(pageIndex-1)*pageSize;
			System.out.println(a);
			for (int i = (pageIndex-1)*pageSize; i < a+pageSize; i++) {
				
				if(i>list.size()-1){
					break;
				}else{
				
				backlist.add(list.get(i));
				}
			}
			}catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
				return ApiUtil.getJsonRt(-14444,
						"分页错误 ERROR 请确保参数正确 " + StringUtil.getException(e));
			}


				JSONObject rt = JSONObject.fromObject(ApiUtil
						.getJsonRt(10000, "成功"));
				rt.put("Result", backlist);
				rt.put("Total", list.size());
				return rt.toString();
			
		
				
			
				
			

			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}

	
	
	
	/**
	 * 用户关闭问题
	 * @param msg
	 * @return
	 */
	public String userCloseQue(InterfaceMessage msg) {
		
		String tag = "queryDefualOCstatus";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			// 问题id
			Integer userId = json.get("userId") == null ? null : json
					.getInt("userId");
			Integer queId = json.get("queId") == null ? null : json
					.getInt("queId");
			
			
			if(userId==null){
				JSONObject rtf = JSONObject.fromObject(ApiUtil
						.getJsonRt(-10000, "userId  不能为空"));
				
				return rtf.toString();
			}
			
			if(queId==null){
				JSONObject rtf = JSONObject.fromObject(ApiUtil
						.getJsonRt(-10000, "queId  不能为空"));
				
				return rtf.toString();
				
			}
			
			
			
		
			OpenAuthVo vo   = new OpenAuthVo();
			vo.setQuesMainId(String.valueOf(queId));
			vo.setOpenAuthFlag("D");
			vo.setDoctorId(String.valueOf(userId));
			
			int  back= openAuthService.updateDocidandopenflagByqueID(vo);
			
			if(back>=0){
				JSONObject rt = JSONObject.fromObject(ApiUtil
						.getJsonRt(10000, "成功"));
				return rt.toString();
				
			}else{
			
			JSONObject rtf = JSONObject.fromObject(ApiUtil
					.getJsonRt(-10000, "用户关闭问题失败"));
			
			return rtf.toString();
		
			}
		
			}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	
	
	

	
	
	
/**
 * 
 * @param msg  提供用户app的接口   获取某个医生的公开问题接口
 * @return
 */
	public String getDoctorOpenListForUser(InterfaceMessage msg) {
		String tag = "getDoctorOpenListForUser";
		try {
			
			JSONObject json = JSONObject.fromObject(msg.getParam());
			Integer quesType =1;
			Integer pageSize = json.get("pageSize") == null ? null : json
					.getInt("pageSize");
			Integer pageIndex = json.get("pageIndex") == null ? null : json
					.getInt("pageIndex");
			Integer doctorUid = json.get("doctorUid") == null ? null : json
					.getInt("doctorUid");
			Integer openFlag = json.get("openFlag") == null ? null : json
					.getInt("openFlag");
			/*
			 * Integer deptID = json.get("deptID") == null ? null : json
			 * .getInt("deptID");
			 */
			/*String twoDeptID = json.get("twoDeptID") == null ? null : json
					.getString("twoDeptID");*/
			QuesMainVo qmv = new QuesMainVo();
			qmv.setASK_DoctorID(doctorUid);
			// qmv.setQD_AskDeptID(deptID);
			//qmv.setQD_DeptTwoIDS(twoDeptID);
			Integer rtCount = 0 ;
			/*quesMainService
					.queryDesignationCountQuesListForApp(qmv, quesType);*/
			String rtStr = "";
			/*com.yihu.baseinfo.api.DoctorInfoApi doctorInfoApi = Rpc.get(
					DoctorInfoApi.class,
					ConfigUtil.getInstance().getUrl("url.baseinfo"));*/
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("doctorUid", doctorUid);
			// jsonObj.put("displayStatus", 1);
			jsonObj.put("main", 1);
			jsonObj.put("pageIndex", 1);
			jsonObj.put("pageSize", 100);
			jsonObj.put("columns", "serviceStatus,provinceId");
			InterfaceMessage interfacemessage = new InterfaceMessage();
			interfacemessage.setParam(jsonObj.toString());
			int searchType = 1;
			/*JSONObject dcRt = JSONObject.fromObject(doctorInfoApi
					.queryComplexDoctorList(interfacemessage));*/
			JSONObject dcRt  =JSONObject.fromObject(postService.queryComplexDoctorList_v2(jsonObj.toString())) ;
			int code = dcRt.getInt("Code");
			int proid = 0;
			if(code > 0 ){
				JSONArray rts= dcRt.getJSONArray("Result");
				if(rts.size() == 0){
					return ApiUtil.getJsonRt(-10011, "未找到医生信息。");
				}
				proid = rts.getJSONObject(0).getInt("provinceId");
			}else{
				return ApiUtil.getJsonRt(-10011, "未找到医生信息。");
			}
			
			System.out.println(pageIndex);
			System.out.println(pageSize);
			if (quesType == 1) {
				System.out.println("type 1111111");
				rtCount = quesMainService
						.quesListForDoctorAnswerAndOverCountForUser(qmv);
				
				int rowstart=(pageIndex*pageSize)-(pageSize-1);
				int rowend=pageIndex*pageSize;
				
				rtStr = quesMainService.quesListForDoctorAnswerAndOverV2ForUser(qmv,
						rowstart, rowend);
			} 
			
			
			JSONObject rtJson = JSONObject.fromObject(rtStr);
			JSONObject rt = JSONObject.fromObject(ApiUtil
					.getJsonRt(10000, "成功"));
			JSONArray rtResult = rtJson.getJSONArray("result");
			for (int i = 0; i < rtResult.size(); i++) {
			}
			rt.put("Result", rtJson.getJSONArray("result"));
			rt.put("Total", rtCount);
			rt.put("PageSize", pageSize);
			rt.put("PageIndex", pageIndex);
			return rt.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	
	
	
	
	

	
	/**
	 * 用户id  获取最新的的10 条 docuid
	 * @param msg
	 * @return
	 */
	public String userIdgetDoctorUid(InterfaceMessage msg) {
		
		String tag = "userIdgetDoctorUid";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());

			Integer userId = json.get("userId") == null ? null : json
					.getInt("userId");

			
			
			if(userId==null){
				JSONObject rtf = JSONObject.fromObject(ApiUtil
						.getJsonRt(-10000, "userId  不能为空"));
				
				return rtf.toString();
			}
			

			//做查询    传入  userid  查询doctoruid
			
			String result=  quesMainService.userIdgetDoctorUid(userId);
			
			
			
			
			
				JSONObject rt = JSONObject.fromObject(ApiUtil
						.getJsonRt(10000, "成功"));
				rt.put("Result",result);
				return rt.toString();

	
		
			}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.get().error(
					tag,
					new LogBody().set("方法", tag).set("参数", msg.getParam())
							.set("异常", StringUtil.getException(e)));
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	
	
	
	

	/**
	 * @title
	 * 提供实时查询本人所回复并完成的问题数量
	 * 
	 * 
	 * @desc
	 * 传出
	 * 医生本人已回复XX个免费问题
	 * 医生本人已回复XX个免费指定问题
	 * 医生本月需要完成的问题总数（目前为150，可以使用BOSS字典来存储这个数值）
	 * @param msg
	 * @return
	 */
	public String queryDoctorQueFree(InterfaceMessage msg) {
		
		String tag = "queryDoctorQueFree";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			Integer doctorID = json.get("doctorID") == null ? null : json
					.getInt("doctorID");

			
			
			String    back= askDoctorNewInterfaceService.queryDoctorQueFree(doctorID);
			
			JSONObject backjson=JSONObject.fromObject(back);
			
			 JSONArray rs =   backjson.getJSONArray("result");
			 
			 if(rs.size()<=0){
					
					JSONObject rtjon = new JSONObject();
					JSONArray result1 = new JSONArray();
					
					
					JSONObject json1 = new JSONObject();
					json1.put("mtofreecount", 0);
					json1.put("mfreecount", 0);
					json1.put("dayfreecount", 0);
					json1.put("daytofreecount", 0);
					json1.put("docid", doctorID);
					
					result1.add(json1);
					
					rtjon.put("Code", "10000");
					rtjon.put("Message", "成功!无数据");
					rtjon.put("Result", result1);
					rtjon.put("finNum",150);
					return  rtjon.toString();
			 } 

			
			JSONObject rt = JSONObject.fromObject(ApiUtil
					.getJsonRt(10000, "成功"));
			
			
			rt.put("Result",rs.toString());
			rt.put("finNum",150);
			
			
		
			return  rt.toString();
		
			}catch (JSONException e) {
		     log.error(LoggerUtil.getTrace(e));
			e.printStackTrace();
			
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			log.error(LoggerUtil.getTrace(e));
			e.printStackTrace();
			
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	
	
	
	


	/**
	 * @title
	 * 科室热门 咨询！！ 
	 * @desc
	 * @param msg
	 * @return
	 */
	public String queryHotQueByDept(InterfaceMessage msg) {
		
		String tag = "queryHotQueByDept";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());
			String deptid = json.get("deptid") == null ? null : json
					.getString("deptid");
			//pageIndex  从1 开始
			Integer pageIndex = json.get("pageIndex") == null ? null : json
					.getInt("pageIndex");
			Integer pageSize = json.get("pageSize") == null ? null : json
					.getInt("pageSize");
			
			String startdate = json.get("startdate") == null ? null : json
					.getString("startdate");
			String enddate = json.get("enddate") == null ? null : json
					.getString("enddate");
			
			
			
			if(deptid==null||deptid==""){
				
				return ApiUtil.getJsonRt(-14444,"deptid 是必输字段");
			}
			
			if(pageIndex==null||pageIndex==0){
				return ApiUtil.getJsonRt(-14444,"pageIndex 是必输字段");
			}
			
			if(pageSize==null||pageSize==0){
				return ApiUtil.getJsonRt(-14444,"pageSize 是必输字段");
			}
			
			
			int star=pageIndex;
			int end=pageIndex+pageSize-1;
			
			
			
			String    back= askDoctorNewInterfaceService.queryHotQueByDept(deptid,star,end,startdate,enddate);
			
			
			
			
			JSONObject backjson=JSONObject.fromObject(back);
			
			 JSONArray rs =   backjson.getJSONArray("result");
			

			
			JSONObject rt = JSONObject.fromObject(ApiUtil
					.getJsonRt(10000, "成功"));
			
			
			rt.put("Result",rs.toString());
			
			
		
			return  rt.toString();
		
			}catch (JSONException e) {
		     log.error(LoggerUtil.getTrace(e));
			e.printStackTrace();
			
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			log.error(LoggerUtil.getTrace(e));
			e.printStackTrace();
			
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	
	
	
	
	
	


	/**
	 * @title
	 *  提供 医生 网络咨询，待回复和新咨询 数量
	 * @desc
	 * @param msg
	 * @return
	 */
	public String queryWaitAndNewReplyCount(InterfaceMessage msg) {
		
		String tag = "queryWaitAndNewReplyCount";
		try {
			JSONObject json = JSONObject.fromObject(msg.getParam());

			Integer doctorUid = json.get("doctorUid") == null ? null : json
					.getInt("doctorUid");

			
			
			
		
			
			
			String    back= askDoctorNewInterfaceService.queryWaitAndNewReplyCount(doctorUid);
			
			
			
			
			JSONObject backjson=JSONObject.fromObject(back);
			
			 JSONArray rs =   backjson.getJSONArray("result");
			

			
			JSONObject rt = JSONObject.fromObject(ApiUtil
					.getJsonRt(10000, "成功"));
			
			
			rt.put("Result",rs.toString());
			
			
		
			return  rt.toString();
		
			}catch (JSONException e) {
		     log.error(LoggerUtil.getTrace(e));
			e.printStackTrace();
			
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		} catch (Exception e) {
			log.error(LoggerUtil.getTrace(e));
			e.printStackTrace();
			
			return ApiUtil.getJsonRt(-14444,
					"加载异常!" + StringUtil.getException(e));
		}
	}
	
	
	
	
	
	
	

	
}
