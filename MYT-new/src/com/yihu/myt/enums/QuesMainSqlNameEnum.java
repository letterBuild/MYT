package com.yihu.myt.enums;import com.coreframework.db.SqlNameEnum;
public enum QuesMainSqlNameEnum implements SqlNameEnum {					test150addmoney,
	//报表	deleteRepotData ,insertRepotData,
	//查询记录数
	queryQuesMainCountByCondition,
	//查询记录
	queryQuesMainListByCondition,	queryQuestionLibraryListByCondition,	queryQuesMainListCount,	//报表列表	getReportList,	queryDoctorListCount,	queryDoctorQuestionLibraryListCount,	getReportListCount,
	//插入
	insertQuesMain,
	//根据条件更新
	updateQuesMainByCondition,	updateQMainByCondition,	//查询是否存在重复问题	querySameQueCont,	//获取所有组团问题	queryTeamCont,	//获取问题列表包括患者数据	queryQuesAndPatient,	//医生咨询的问题列表	queryDoctorList,	//医生咨询的问题列表WEB_问题库	queryDoctorQuestionLibraryList,	//医生咨询的问题库列表	queryQuestionLibraryList,	//已解决的问题总数	queryOverQuesCont,	//获取未回答的问题总数	queryHvaeQuesCont,	//获取该问题被是否被抢答	querCountAnswerQus,	//获取该问题被是否被该医生抢答	querCountDCAnswerQus,	//获取需要发送短信的医生列表	queryQuesCountForSendSMS,	//获取免费问题审核列表	quesDoctorCheckList,	quesDoctorCheckListCount,	//获取已审核过问题列表	quesReturnList,	quesReturnListCount,	//过期问题列表	quesOverTimeList,	quesOverTimeListCount,	quesOverTimeTearmList,	quesOverTimeTeamListCount,	//医学团队查询问题详细	quesListForCheckTeam,	quesListCountForCheckTeam,	quesListDcSubOver,	quesListDcSubOverCount,		//新版医生列表	quesListForDoctorWaitAnswer,	quesListForDoctorWaitAnswerCount,	quesListForDoctorWaitAnswerDeptsCount,	quesListForDoctorWaitAnswerDepts,	quesDoctorWaitAnswerLastTime,	quesListForDoctorWaitAnswerCountNoFree,	quesListForDoctorAnswerNoOver,	quesListForDoctorAnswerNoOverV2,	quesListForDoctorAnswerNoOverCount,	quesListForDoctorAnswerAndOver,	quesListForDoctorAnswerAndOverV2,	quesListForDoctorAnswerAndOverV5,		quesListForDoctorAnswerAndOverCount,	quesListForDoctorAnswerAndOverCountV5,	quesListForDoctorAnswerAndOverCountChangzheng,	quesListForDoctorWaitAnswerForNoFreeQue,//医生问题列表 指定问题列表	quesListForDoctorWaitAnswerForNoFreeQueV2,	quesListForDoctorWaitAnswerForNoFreeQueCount,//医生问题列表 指定问题列表数量	quesListForDoctorWaitAnswerFreeQues,//医生问题列表 感兴趣问题列表	quesListForDoctorWaitAnswerFreeQuesV2,	quesListForDoctorWaitAnswerFreeQuesCount,//医生问题列表 感兴趣问题数量	//报表统计	queReportForDepts,	queReportForDeptsCount,	queReportForDays,	queReportForDaysCount,	queReportForCity,	queReportForCityCount,	queReportForPlatform,	queReportForPlatformCount,	quesListForDoctorAnswerAndOverCountForUser,	quesListForDoctorAnswerAndOverV2ForUser,	userIdgetDoctorUid	
}