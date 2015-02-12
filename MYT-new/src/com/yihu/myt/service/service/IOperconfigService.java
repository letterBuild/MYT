package com.yihu.myt.service.service;import java.util.List;import com.coreframework.ioc.By;import com.coreframework.db.JdbcConnection;import com.yihu.myt.vo.OperconfigVo;import com.yihu.myt.service.service.impl.OperconfigService;@By(OperconfigService.class)public interface IOperconfigService {	/**	 * 获取列表记录数	 */	public Integer queryOperconfigCountByCondition(OperconfigVo vo)			throws Exception;	/**	 * 获取列表	 */	public List<OperconfigVo> queryOperconfigListByCondition(OperconfigVo vo)			throws Exception;	/**	 * 添加	 */	public int insertOperconfig(OperconfigVo vo) throws Exception;	/**	 * 修改	 */	public void updateOperconfigByCondition(OperconfigVo vo, JdbcConnection conn)			throws Exception;	public OperconfigVo queryOperconfid(OperconfigVo vo) throws Exception;	public List<OperconfigVo> queryOperconfidList(OperconfigVo vo)			throws Exception;		public int updatequeryOperconfid (OperconfigVo vo);	}