package com.yihu.myt.service.service.impl;import java.sql.SQLException;import java.text.SimpleDateFormat;import java.util.Date;import java.util.List;import net.sf.json.JSONArray;import net.sf.json.JSONObject;import com.coreframework.db.DB;import com.coreframework.db.JdbcConnection;import com.coreframework.db.Sql;import com.yihu.myt.enums.DoctorDefaultAuthSqlNameEnum;import com.yihu.myt.enums.MyDatabaseEnum;import com.yihu.myt.enums.OpenAuthSqlNameEnum;import com.yihu.myt.service.service.IDoctorDefaultAuthService;import com.yihu.myt.util.StringUtil;import com.yihu.myt.vo.DoctorDefaultAuthVo;import com.yihu.myt.vo.QuesMainVo;
public class DoctorDefaultAuthService implements IDoctorDefaultAuthService{
	/**	*获取列表记录数	*/	public Integer queryDoctorDefaultAuthCountByCondition(DoctorDefaultAuthVo vo) throws Exception{		Sql sql = DB.me().createSql(DoctorDefaultAuthSqlNameEnum.queryDoctorDefaultAuthCountByCondition);		StringBuilder condition = new StringBuilder();		sql.addVar("@condition", condition.toString());		Integer count = DB.me().queryForInteger(MyDatabaseEnum.YiHuNet2008, sql);		return count;	}
	/**	*获取列表	*/	public List<DoctorDefaultAuthVo> queryDoctorDefaultAuthListByCondition(DoctorDefaultAuthVo vo) throws Exception{		Sql sql = DB.me().createSql(DoctorDefaultAuthSqlNameEnum.queryDoctorDefaultAuthListByCondition);		StringBuilder condition = new StringBuilder();		sql.addVar("@condition", condition.toString());		sql.addVar("@page", "");		List<DoctorDefaultAuthVo> list = DB.me().queryForBeanList(MyDatabaseEnum.YiHuNet2008, sql,DoctorDefaultAuthVo.class);		return list;	}
	/**	*添加	*/	public void insertDoctorDefaultAuth(DoctorDefaultAuthVo vo) throws Exception{		Sql sql = DB.me().createSql(DoctorDefaultAuthSqlNameEnum.insertDoctorDefaultAuth);		sql.addParamValue(vo.getDoctorId());		sql.addParamValue(vo.getOpenFlag());		System.out.println(sql.getSqlString());		DB.me().insert(MyDatabaseEnum.YiHuNet2008,sql);		}
	/**	*修改	*/	public int updateDoctorDefaultAuthByCondition(DoctorDefaultAuthVo vo) throws Exception{				try {			Sql sql = DB.me().createSql(DoctorDefaultAuthSqlNameEnum.updateDoctorDefaultAuthByCondition);						StringBuilder condition = new StringBuilder();			if (Long.valueOf(vo.getDoctorId()) > 0) {				SimpleDateFormat fmt= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");				 String date =fmt.format(new Date());				condition.append("OpenFlag= '"+vo.getOpenFlag()+"', "+"Time= ' "+date+" ' ");								sql.addVar("@condition", condition.toString());				sql.addParamValue(vo.getId());				System.out.println(sql.toString());				int r = DB.me().update(MyDatabaseEnum.YiHuNet2008, sql);				return r;			} else {				return -1;			}		} catch (SQLException e) {			// TODO Auto-generated catch block			e.printStackTrace();			return -1;		}
}		/**	 * 根据  docid  查询	 */	@Override	public List<DoctorDefaultAuthVo> queryDoctorDefaultAuthListByDoctorId(			DoctorDefaultAuthVo vo) throws Exception {		// TODO Auto-generated method stub		Sql sql = DB.me().createSql(DoctorDefaultAuthSqlNameEnum.queryDoctorDefaultAuthListByDoctorId);				sql.addParamValue(vo.getDoctorId());				System.out.println(sql.getSqlString());		List<DoctorDefaultAuthVo> list = DB.me().queryForBeanList(MyDatabaseEnum.YiHuNet2008, sql,DoctorDefaultAuthVo.class);		return list;			}	@Override	public List<QuesMainVo> queryQuesByDiseaseID(Integer id) throws Exception {		// TODO Auto-generated method stub		// TODO Auto-generated method stub		Sql sql = DB.me().createSql(DoctorDefaultAuthSqlNameEnum.queryQuesByDiseaseID);				sql.addParamValue(id);				System.out.println(sql.getSqlString());		 List<QuesMainVo> back= DB.me().queryForBeanList(MyDatabaseEnum.YiHuNet2008, sql,QuesMainVo.class);		return back;	}	}