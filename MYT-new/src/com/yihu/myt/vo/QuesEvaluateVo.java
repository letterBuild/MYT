package com.yihu.myt.vo;

	private Integer ASK_QuesID;  

	private Integer ASK_UserID;  

	private Integer ASK_DoctorID;  

	private Integer QD_Ating;  

	private String ELT_Content;  

	private String ELT_CRTime;  
	
	public int getELT_isSysEva() {
		return this.ELT_ID;
	}
	public void setELT_ID(Integer ELT_ID){
		this.ELT_ID=ELT_ID;
	}

	public Integer getASK_QuesID(){
		return this.ASK_QuesID;
	}
	public void setASK_QuesID(Integer ASK_QuesID){
		this.ASK_QuesID=ASK_QuesID;
	}

	public Integer getASK_UserID(){
		return this.ASK_UserID;
	}
	public void setASK_UserID(Integer ASK_UserID){
		this.ASK_UserID=ASK_UserID;
	}

	public Integer getASK_DoctorID(){
		return this.ASK_DoctorID;
	}
	public void setASK_DoctorID(Integer ASK_DoctorID){
		this.ASK_DoctorID=ASK_DoctorID;
	}

	public Integer getQD_Ating(){
		return this.QD_Ating;
	}
	public void setQD_Ating(Integer QD_Ating){
		this.QD_Ating=QD_Ating;
	}

	public String getELT_Content(){
		return this.ELT_Content;
	}
	public void setELT_Content(String ELT_Content){
		this.ELT_Content=ELT_Content;
	}

	public String getELT_CRTime(){
		return this.ELT_CRTime;
	}
	public void setELT_CRTime(String ELT_CRTime){
		this.ELT_CRTime=ELT_CRTime;
	}

}