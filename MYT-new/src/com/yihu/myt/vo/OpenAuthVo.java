package com.yihu.myt.vo;

	private String QuesMainId;  

	private String DoctorId;  

	private String OpenAuthFlag;  

	private Integer Clicks;  

	private String BAK;  

	private String CreateDate;  

		return this.ID;
	}
	public void setID(Integer ID){
		this.ID=ID;
	}

	public String getQuesMainId(){
		return this.QuesMainId;
	}
	public void setQuesMainId(String QuesMainId){
		this.QuesMainId=QuesMainId;
	}

	public String getDoctorId(){
		return this.DoctorId;
	}
	public void setDoctorId(String DoctorId){
		this.DoctorId=DoctorId;
	}

	public String getOpenAuthFlag(){
		return this.OpenAuthFlag;
	}
	public void setOpenAuthFlag(String OpenAuthFlag){
		this.OpenAuthFlag=OpenAuthFlag;
	}





	public String getCreateDate(){
		return this.CreateDate;
	}
	public void setCreateDate(String CreateDate){
		this.CreateDate=CreateDate;
	}

}