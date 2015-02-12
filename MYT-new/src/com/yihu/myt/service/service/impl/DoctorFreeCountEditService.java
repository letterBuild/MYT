package com.yihu.myt.service.service.impl;import java.sql.SQLException;import java.util.List;import com.coreframework.db.DB;import com.coreframework.db.JdbcConnection;import com.coreframework.db.Sql;import com.yihu.myt.enums.MyDatabaseEnum;import com.yihu.myt.enums.DoctorFreeCountEditSqlNameEnum;import com.yihu.myt.enums.MyTableNameEnum;import com.yihu.myt.util.StringUtil;import com.yihu.myt.vo.DoctorFreeCountEditVo;import com.yihu.myt.service.service.IDoctorFreeCountEditService;
public class DoctorFreeCountEditService implements IDoctorFreeCountEditService{
	/**	*获取列表记录数	*/	public Integer queryDoctorFreeCountEditCountByCondition(DoctorFreeCountEditVo vo) throws Exception{		Sql sql = DB.me().createSql(DoctorFreeCountEditSqlNameEnum.queryDoctorFreeCountEditCountByCondition);		StringBuilder condition = new StringBuilder();		sql.addVar("@condition", condition.toString());		Integer count = DB.me().queryForInteger(MyDatabaseEnum.YiHuNet2008, sql);		return count;	}
	/**	*获取列表	*/	public List<DoctorFreeCountEditVo> queryDoctorFreeCountEditListByCondition(DoctorFreeCountEditVo vo) throws Exception{		Sql sql = DB.me().createSql(DoctorFreeCountEditSqlNameEnum.queryDoctorFreeCountEditListByCondition);		StringBuilder condition = new StringBuilder();		if(vo.getDfcId()!=null){			condition.append(" and DfcId = " + vo.getDfcId());		}		sql.addVar("@condition", condition.toString());		sql.addVar("@page", "");		List<DoctorFreeCountEditVo> list = DB.me().queryForBeanList(MyDatabaseEnum.YiHuNet2008, sql,DoctorFreeCountEditVo.class);		return list;	}	public List<DoctorFreeCountEditVo> queryDocFreeEditListForTop(DoctorFreeCountEditVo vo) throws Exception{		Sql sql = DB.me().createSql(DoctorFreeCountEditSqlNameEnum.queryDocFreeEditListForTop);		StringBuilder condition = new StringBuilder();		if(vo.getDfcId()!=null){			condition.append(" and DfcId = " + vo.getDfcId());		}		sql.addVar("@condition", condition.toString());		sql.addVar("@page", "");		List<DoctorFreeCountEditVo> list = DB.me().queryForBeanList(MyDatabaseEnum.YiHuNet2008, sql,DoctorFreeCountEditVo.class);		return list;	}	public List<DoctorFreeCountEditVo> queryDocFreeEditListForUpdate(DoctorFreeCountEditVo vo) throws Exception{		Sql sql = DB.me().createSql(DoctorFreeCountEditSqlNameEnum.queryDocFreeEditListForUpdate);		StringBuilder condition = new StringBuilder();		sql.addVar("@condition", condition.toString());		sql.addVar("@page", "");		List<DoctorFreeCountEditVo> list = DB.me().queryForBeanList(MyDatabaseEnum.YiHuNet2008, sql,DoctorFreeCountEditVo.class);		return list;	}
	/**	*添加	*/	public int insertDoctorFreeCountEdit(DoctorFreeCountEditVo vo) throws Exception{		try {			int r = DB.me().insert(					MyDatabaseEnum.YiHuNet2008,					DB.me().createInsertSql(vo,							MyTableNameEnum.ZiXun_DoctorFreeCountEdit, "dbo"));			return r;		} catch (SQLException e) {			// TODO Auto-generated catch block			e.printStackTrace();			return -1;		}	}	public int updateDoctorFreeCountEdit(DoctorFreeCountEditVo vo) throws Exception{		try {			StringBuilder condition = new StringBuilder();			if (vo.getDfcEdit() != null) {				condition.append("  DfcEdit = " + vo.getDfcEdit());				vo.setDfcEdit(null);				int r = DB.me().update(						MyDatabaseEnum.YiHuNet2008,						DB.me().createUpdateSql(vo,								MyTableNameEnum.ZiXun_DoctorFreeCountEdit,								condition.toString()));				return r;			} else {				return -1;			}		} catch (SQLException e) {			// TODO Auto-generated catch block			e.printStackTrace();			return -1;		}	}
	/**	*修改	*/	public void updateDoctorFreeCountEditByCondition(DoctorFreeCountEditVo vo,JdbcConnection conn) throws Exception{Sql sql = DB.me().createSql(DoctorFreeCountEditSqlNameEnum.updateDoctorFreeCountEditByCondition);		if (vo == null  || StringUtil.isEmpty(vo.getDfcEdit())) {			throw new Exception(" 不能为空或者 主键id 不能为空");		}		StringBuilder condition = new StringBuilder();		if (condition.length() == 0) {		throw new Exception("未有更新SQL被执行！");		} else {		condition.deleteCharAt(condition.length() - 1);		sql.addVar("@condition", condition.toString());		sql.addParamValue(vo.getDfcEdit());		}		DB.me().update(conn, sql);		}	public DoctorFreeCountEditVo queryDoctorFreeCountEdit(DoctorFreeCountEditVo vo) throws Exception{				List<DoctorFreeCountEditVo>  list = this.queryDocFreeEditListForTop(vo);		if (list == null || list.size() == 0) {			return null;		} else {			return list.get(0);		}	}
}