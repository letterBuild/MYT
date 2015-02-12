package com.yihu.myt.service.service.impl;import java.sql.SQLException;import java.util.List;import com.coreframework.db.DB;import com.coreframework.db.JdbcConnection;import com.coreframework.db.Sql;import com.yihu.myt.enums.MyDatabaseEnum;import com.yihu.myt.enums.AnswerRecordSqlNameEnum;import com.yihu.myt.enums.MyTableNameEnum;import com.yihu.myt.util.StringUtil;import com.yihu.myt.vo.AnswerRecordVo;import com.yihu.myt.service.service.IAnswerRecordService;
public class AnswerRecordService implements IAnswerRecordService{
	/**	*获取列表记录数	*/	public Integer queryAnswerRecordCountByCondition(AnswerRecordVo vo) throws Exception{		Sql sql = DB.me().createSql(AnswerRecordSqlNameEnum.queryAnswerRecordCountByCondition);		StringBuilder condition = new StringBuilder();		sql.addVar("@condition", condition.toString());		Integer count = DB.me().queryForInteger(MyDatabaseEnum.YiHuNet2008, sql);		return count;	}
	/**	*获取列表	*/	public List<AnswerRecordVo> queryAnswerRecordListByCondition(AnswerRecordVo vo) throws Exception{		Sql sql = DB.me().createSql(AnswerRecordSqlNameEnum.queryAnswerRecordListByCondition);		StringBuilder condition = new StringBuilder();		sql.addVar("@condition", condition.toString());		sql.addVar("@page", "");		List<AnswerRecordVo> list = DB.me().queryForBeanList(MyDatabaseEnum.YiHuNet2008, sql,AnswerRecordVo.class);		return list;	}
	/**	*添加	*/	public int insertAnswerRecord(AnswerRecordVo vo) throws Exception{		try {			int r = DB.me().insert(					MyDatabaseEnum.YiHuNet2008,					DB.me().createInsertSql(vo, MyTableNameEnum.ZiXun_AnswerRecord,							"dbo"));			return r;		} catch (SQLException e) {			// TODO Auto-generated catch block			e.printStackTrace();			return -1;		}	}
	/**	*修改	*/	public void updateAnswerRecordByCondition(AnswerRecordVo vo,JdbcConnection conn) throws Exception{Sql sql = DB.me().createSql(AnswerRecordSqlNameEnum.updateAnswerRecordByCondition);		if (vo == null  || StringUtil.isEmpty(vo.getAR_ID())) {			throw new Exception(" 不能为空或者 主键id 不能为空");		}		StringBuilder condition = new StringBuilder();		if (condition.length() == 0) {		throw new Exception("未有更新SQL被执行！");		} else {		condition.deleteCharAt(condition.length() - 1);		sql.addVar("@condition", condition.toString());		sql.addParamValue(vo.getAR_ID());		}		DB.me().update(conn, sql);		}
}