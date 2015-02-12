package com.yihu.myt.service.service.impl;import java.util.List;import com.coreframework.db.DB;import com.coreframework.db.JdbcConnection;import com.coreframework.db.Sql;import com.yihu.myt.enums.CloseMainSqlNameEnum;import com.yihu.myt.enums.MyDatabaseEnum;import com.yihu.myt.service.service.ICloseMainService;import com.yihu.myt.util.StringUtil;import com.yihu.myt.vo.CloseMainVo;import com.yihu.myt.vo.DoctorVo;
public class CloseMainService implements ICloseMainService{
	/**	*获取列表记录数	*/	public Integer queryCloseMainCountByCondition(CloseMainVo vo) throws Exception{		Sql sql = DB.me().createSql(CloseMainSqlNameEnum.queryCloseMainCountByCondition);		StringBuilder condition = new StringBuilder();		sql.addVar("@condition", condition.toString());		Integer count = DB.me().queryForInteger(MyDatabaseEnum.BaseInfo, sql);		return count;	}
	/**	*获取列表	*/	public List<CloseMainVo> queryCloseMainListByCondition(CloseMainVo vo) throws Exception{		Sql sql = DB.me().createSql(CloseMainSqlNameEnum.queryCloseMainListByCondition);		StringBuilder condition = new StringBuilder();		sql.addVar("@condition", condition.toString());		sql.addVar("@page", "");		List<CloseMainVo> list = DB.me().queryForBeanList(MyDatabaseEnum.BaseInfo, sql,CloseMainVo.class);		return list;	}
	/**	*添加	*/	public void insertCloseMain(CloseMainVo vo) throws Exception{		Sql sql = DB.me().createSql(CloseMainSqlNameEnum.insertCloseMain);		sql.addParamValue(vo.getDOCID());		sql.addParamValue(vo.getDOCNAME());		sql.addParamValue(vo.getDOCDIC());				//System.out.println("插入的语句"+sql.getSqlString());		DB.me().insert(MyDatabaseEnum.YiHuNet2008,sql);		}
	/**	*修改	*/	public void updateCloseMainByCondition(CloseMainVo vo,JdbcConnection conn) throws Exception{Sql sql = DB.me().createSql(CloseMainSqlNameEnum.updateCloseMainByCondition);		if (vo == null  || StringUtil.isEmpty(vo.getID())) {			throw new Exception(" 不能为空或者 主键id 不能为空");		}		StringBuilder condition = new StringBuilder();		if (condition.length() == 0) {		throw new Exception("未有更新SQL被执行！");		} else {		condition.deleteCharAt(condition.length() - 1);		sql.addVar("@condition", condition.toString());		sql.addParamValue(vo.getID());		}		DB.me().update(conn, sql);		}	@Override		public DoctorVo getDocById(String docid) throws Exception {				Sql sql = DB.me().createSql(CloseMainSqlNameEnum.findDocById);		StringBuilder condition = new StringBuilder();						condition.append("where 1=1 and DoctorUid="+docid);				sql.addVar("@find", condition.toString());					System.out.println(	sql.getSqlString()+"查找医生的sql ");				List<DoctorVo> list = DB.me().queryForBeanList(MyDatabaseEnum.YiHuNet2008, sql,DoctorVo.class);		return list.get(0);			}
}