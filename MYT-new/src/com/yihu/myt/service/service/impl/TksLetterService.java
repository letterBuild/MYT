package com.yihu.myt.service.service.impl;import java.util.List;import com.coreframework.db.DB;import com.coreframework.db.JdbcConnection;import com.coreframework.db.Sql;import com.yihu.myt.enums.MyDatabaseEnum;import com.yihu.myt.enums.TksLetterSqlNameEnum;import com.yihu.myt.util.StringUtil;import com.yihu.myt.vo.TksLetterVo;import com.yihu.myt.service.service.ITksLetterService;
public class TksLetterService implements ITksLetterService{
	/**	*获取列表记录数	*/	public Integer queryTksLetterCountByCondition(TksLetterVo vo) throws Exception{		Sql sql = DB.me().createSql(TksLetterSqlNameEnum.queryTksLetterCountByCondition);		StringBuilder condition = new StringBuilder();		sql.addVar("@condition", condition.toString());		Integer count = DB.me().queryForInteger(MyDatabaseEnum.YiHuNet2008, sql);		return count;	}
	/**	*获取列表	*/	public List<TksLetterVo> queryTksLetterListByCondition(TksLetterVo vo) throws Exception{		Sql sql = DB.me().createSql(TksLetterSqlNameEnum.queryTksLetterListByCondition);		StringBuilder condition = new StringBuilder();		sql.addVar("@condition", condition.toString());		sql.addVar("@page", "");		List<TksLetterVo> list = DB.me().queryForBeanList(MyDatabaseEnum.YiHuNet2008, sql,TksLetterVo.class);		return list;	}
	/**	*添加	*/	public void insertTksLetter(TksLetterVo vo) throws Exception{}
	/**	*修改	*/	public void updateTksLetterByCondition(TksLetterVo vo,JdbcConnection conn) throws Exception{Sql sql = DB.me().createSql(TksLetterSqlNameEnum.updateTksLetterByCondition);		if (vo == null  || StringUtil.isEmpty(vo.getTL_ID())) {			throw new Exception(" 不能为空或者 主键id 不能为空");		}		StringBuilder condition = new StringBuilder();		if (condition.length() == 0) {		throw new Exception("未有更新SQL被执行！");		} else {		condition.deleteCharAt(condition.length() - 1);		sql.addVar("@condition", condition.toString());		sql.addParamValue(vo.getTL_ID());		}		DB.me().update(conn, sql);		}
}