package com.yihu.myt.service.service.impl;import java.sql.SQLException;import java.util.List;import org.apache.commons.lang3.StringUtils;import net.sf.json.JSONObject;import com.coreframework.db.DB;import com.coreframework.db.JdbcConnection;import com.coreframework.db.Sql;import com.yihu.myt.enums.MyDatabaseEnum;import com.yihu.myt.enums.MyTableNameEnum;import com.yihu.myt.enums.ReplySqlNameEnum;import com.yihu.myt.util.StringUtil;import com.yihu.myt.vo.ReplyVo;import com.yihu.myt.service.service.IReplyService;
public class ReplyService implements IReplyService{
	/**	*获取列表记录数	*/	public Integer queryReplyCountByCondition(ReplyVo vo) throws Exception{		Sql sql = DB.me().createSql(ReplySqlNameEnum.queryReplyCountByCondition);		StringBuilder condition = new StringBuilder();		sql.addVar("@condition", condition.toString());		Integer count = DB.me().queryForInteger(MyDatabaseEnum.YiHuNet2008, sql);		return count;	}
	/**	*获取列表	*/	public List<ReplyVo> queryReplyListByCondition(ReplyVo vo) throws Exception{		Sql sql = DB.me().createSql(ReplySqlNameEnum.queryReplyListByCondition);		StringBuilder condition = new StringBuilder();				if(vo.getASK_QuesID()!=null){			condition.append(" and ASK_QuesID = " + vo.getASK_QuesID());		}		if(vo.getREPLY_ID()!=null){			condition.append(" and REPLY_ID = " + vo.getREPLY_ID());		}		if(StringUtil.isNotEmpty(vo.getReplyIDs())){			condition.append(" and REPLY_ID in ( " +vo.getReplyIDs()+" )");		}		if(vo.getREPLY_Status()!=null){			condition.append(" and REPLY_Status = " + vo.getREPLY_Status());		}		sql.addVar("@condition", condition.toString());		sql.addVar("@page", "");		//System.out.println(sql.toString());		List<ReplyVo> list = DB.me().queryForBeanList(MyDatabaseEnum.YiHuNet2008, sql,ReplyVo.class);		return list;	}	/**	*获取列表	*/	public List<ReplyVo> queryReplyListByCondition(ReplyVo vo,Integer pageIndex,Integer pageSize) throws Exception{		Sql sql = DB.me().createSql(ReplySqlNameEnum.queryReplyListByCondition);		StringBuilder condition = new StringBuilder();		if(vo.getASK_QuesID()!=null){			condition.append(" and ASK_QuesID = " + vo.getASK_QuesID());		}		if(vo.getREPLY_ID()!=null ){			if(vo.getREPLY_ID()!=0){				condition.append(" and REPLY_ID = " + vo.getREPLY_ID());			}		}		if(vo.getREPLY_Status()!=null){			condition.append(" and REPLY_Status = " + vo.getREPLY_Status());		}		if(StringUtil.isNotEmpty(vo.getReplyIDs())){			condition.append(" and REPLY_ID in ( " +vo.getReplyIDs()+" )");		}		sql.addVar("@condition", condition.toString());		if (pageSize != 0 || pageIndex != 0) {			sql.addVar("@page", " and rowId >" +pageSize*pageIndex + " and rowId <=" + (pageIndex + 1 )*pageSize );			} else {			sql.addVar("@page", "");			}		List<ReplyVo> list = DB.me().queryForBeanList(MyDatabaseEnum.YiHuNet2008, sql,ReplyVo.class);		return list;	}	public  int queryReplyCount(ReplyVo vo) throws Exception{		Sql sql = DB.me().createSql(ReplySqlNameEnum.queryReplyCountByCondition);		StringBuilder condition = new StringBuilder();		if(vo.getASK_QuesID()!=null){			condition.append(" and ASK_QuesID = " + vo.getASK_QuesID());		}		if(vo.getREPLY_Status()!=null){			condition.append(" and REPLY_Status = " + vo.getREPLY_Status());		}		if(vo.getREPLY_UserType()!=null){			condition.append(" and REPLY_UserType = " + vo.getREPLY_UserType());		}		if(vo.getASK_DoctorID()!=null){			condition.append(" and ASK_DoctorID = " + vo.getASK_DoctorID());		}		sql.addVar("@condition", condition.toString());		int rt = DB.me().queryForInteger(MyDatabaseEnum.YiHuNet2008, sql);		return rt;	}	public com.common.json.JSONObject queryReplyListAll(ReplyVo vo) throws Exception{		Sql sql = DB.me().createSql(ReplySqlNameEnum.queryReplyListAll);		StringBuilder condition = new StringBuilder();		if(vo.getASK_QuesID()!=null){			condition.append(" and ASK_QuesID = " + vo.getASK_QuesID());		}		if(vo.getREPLY_Status()!=null){			condition.append(" and REPLY_Status = " + vo.getREPLY_Status());		}		sql.addVar("@condition", condition.toString());		sql.addVar("@page", "");		com.common.json.JSONObject rtJson = DB.me().queryForJson(MyDatabaseEnum.YiHuNet2008, sql);		return rtJson;	}		public com.common.json.JSONObject getYestodayReplyCount() throws Exception{		Sql sql = DB.me().createSql(ReplySqlNameEnum.getYestodayReplyCount);		com.common.json.JSONObject rtJson = DB.me().queryForJson(MyDatabaseEnum.YiHuNet2008, sql);		return rtJson;	}	public ReplyVo queryReplyLastOne(ReplyVo vo) throws Exception{		Sql sql = DB.me().createSql(ReplySqlNameEnum.queryReplyLastOne);		sql.addParamValue(vo.getASK_QuesID());		ReplyVo rpVo = DB.me().queryForBean(MyDatabaseEnum.YiHuNet2008, sql,ReplyVo.class);		return rpVo;	}
	/**	*添加	*/	public int insertReply(ReplyVo vo) throws Exception{				try {			int r = DB.me().insert(					MyDatabaseEnum.YiHuNet2008,					DB.me().createInsertSql(vo, MyTableNameEnum.ZiXun_Reply,							"dbo"));			return r;		} catch (SQLException e) {			// TODO Auto-generated catch block			e.printStackTrace();			return -1;		}			}		public int insertReplyByCondition(ReplyVo vo,JdbcConnection conn) throws Exception{		try {			int r = DB.me().insert(conn,					DB.me().createInsertSql(vo, MyTableNameEnum.ZiXun_Reply,							"dbo"));			return r;		} catch (SQLException e) {			// TODO Auto-generated catch block			e.printStackTrace();			return -1;		}			}
	/**	*修改	*/	public int updateReplyByCondition(ReplyVo vo) throws Exception{		StringBuilder condition = new StringBuilder();		if(vo.getREPLY_ID()!=null){			condition.append("  REPLY_ID = "+ vo.getREPLY_ID());			vo.setREPLY_ID(null);			Sql sql =DB.me().createUpdateSql(vo, MyTableNameEnum.ZiXun_Reply,condition.toString());			System.out.print(sql.toString());			int r = DB.me().update(					MyDatabaseEnum.YiHuNet2008,sql					);			return r;		}else{			return -1;		}		}
}