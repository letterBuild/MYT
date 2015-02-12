package com.yihu.myt.service.service.impl;import java.util.List;import com.coreframework.db.DB;import com.coreframework.db.JdbcConnection;import com.coreframework.db.Sql;import com.yihu.myt.dao.DoctorInfoDAO;import com.yihu.myt.enums.MyDatabaseEnum;import com.yihu.myt.enums.DoctorInfoSqlNameEnum;import com.yihu.myt.util.StringUtil;import com.yihu.myt.vo.DoctorInfoVo;import com.yihu.myt.service.service.IDoctorInfoService;
public class DoctorInfoService implements IDoctorInfoService{
DoctorInfoDAO doctorInfoDAO = new DoctorInfoDAO();
	/**	*获取列表记录数	*/	public Integer queryDoctorInfoCountByCondition(DoctorInfoVo vo) throws Exception{		Sql sql = DB.me().createSql(DoctorInfoSqlNameEnum.queryDoctorInfoCountByCondition);		StringBuilder condition = new StringBuilder();		if(vo.getProvinceID()!=null ){			if(vo.getProvinceID()>0){				condition.append(" and ProvinceID = " + vo.getProvinceID());			}		}		if(StringUtil.isNotEmpty(vo.getDoctorService())){			condition.append(" and SUBSTRING(doctorService, 2, 1) = ? ");			sql.addParamValue(vo.getDoctorService());		}		sql.addVar("@condition", condition.toString());		Integer count = DB.me().queryForInteger(MyDatabaseEnum.boss, sql);		return count;	}	/**	*获取单个对象	*/	public DoctorInfoVo queryDoctorInfo(DoctorInfoVo vo) throws Exception{		List<DoctorInfoVo> list = this.queryDoctorInfoListByCondition(vo);		if (list == null || list.size() == 0) {			return null;		} else {			return list.get(0);		}	}		
	/**	*获取列表	*/	public List<DoctorInfoVo> queryDoctorInfoListByCondition(DoctorInfoVo vo) throws Exception{		Sql sql = DB.me().createSql(DoctorInfoSqlNameEnum.queryDoctorInfoListByCondition);		StringBuilder condition = new StringBuilder();		if(vo.getProvinceID()!=null ){			if(vo.getProvinceID()>0){				condition.append(" and ProvinceID=" + vo.getProvinceID());			}		}		if(StringUtil.isNotEmpty(vo.getStandardDeptID())){			condition.append(" and StandardDeptID = ? ");			sql.addParamValue(vo.getStandardDeptID());		}		if(StringUtil.isNotEmpty(vo.getDoctorService())){			condition.append(" and SUBSTRING(doctorService, 2, 1) = ? ");			sql.addParamValue(vo.getDoctorService());		}		if(vo.getMain()!=null){			condition.append(" and Main=" + vo.getMain());		}		if(vo.getDoctorUid()!=null){			condition.append(" and DoctorUid=" + vo.getDoctorUid());		}		if(vo.getBaseDoctorID()!=null){			condition.append(" and BaseDoctorID=" + vo.getBaseDoctorID());		}		if(StringUtil.isNotEmpty(vo.getDoctorName())){			condition.append(" and DoctorName = ? ");			sql.addParamValue(vo.getDoctorName());		}		sql.addVar("@condition", condition.toString());		sql.addVar("@page", "");		//System.out.print(sql.toString());		List<DoctorInfoVo> list = DB.me().queryForBeanList(MyDatabaseEnum.boss, sql,DoctorInfoVo.class);		return list;	}
	/**	*添加	*/	public void insertDoctorInfo(DoctorInfoVo vo) throws Exception{		JdbcConnection conn = DB.me().getConnection(MyDatabaseEnum.boss);		try {		conn.beginTransaction();		doctorInfoDAO.insertDoctorInfo(vo,conn);		conn.commitTransaction(true);		} catch (Exception e) {		conn.rollbackAndclose();		throw e;}}			/**	*添加	*/	public void updateDoctorInfo(DoctorInfoVo vo) throws Exception{		JdbcConnection conn = DB.me().getConnection(MyDatabaseEnum.boss);		try {		conn.beginTransaction();		doctorInfoDAO.updateDoctorInfoByCondition(vo,conn);		conn.commitTransaction(true);		} catch (Exception e) {		conn.rollbackAndclose();		throw e;}}		}