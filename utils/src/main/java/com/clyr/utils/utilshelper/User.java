package com.clyr.utils.utilshelper;

import java.io.Serializable;

public class User implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String password;
	private String tel;
	private String agriProductIds;
	private String img;
	private String remark;
	private Integer provinceId;
	private Integer cityId;
	private Integer districtId;
	private String registerTime;
	private Integer status;
	private Integer isNormalUser;
	private Integer isExpert;
	private Integer isGovDept;
	
	private String yqm;
	private String yqmOthers;
	private Integer bonus;
	private String agriGz;
	private String agriSc;
	private String sfzZm;
	private String sfzFm;
	private String realName;
	private String expertType;
	private String areaSc;
	private String address;
	private String company;
	private String job;
	private String province;
	private String city;
	private String district;
	private String sign;//签到
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getAgriProductIds() {
		return agriProductIds;
	}
	public void setAgriProductIds(String agriProductIds) {
		this.agriProductIds = agriProductIds;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public Integer getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}
	public String getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getIsNormalUser() {
		return isNormalUser;
	}
	public void setIsNormalUser(Integer isNormalUser) {
		this.isNormalUser = isNormalUser;
	}
	public Integer getIsExpert() {
		return isExpert;
	}
	public void setIsExpert(Integer isExpert) {
		this.isExpert = isExpert;
	}
	public Integer getIsGovDept() {
		return isGovDept;
	}
	public void setIsGovDept(Integer isGovDept) {
		this.isGovDept = isGovDept;
	}
	public String getYqm() {
		return yqm;
	}
	public void setYqm(String yqm) {
		this.yqm = yqm;
	}
	public String getYqmOthers() {
		return yqmOthers;
	}
	public void setYqmOthers(String yqmOthers) {
		this.yqmOthers = yqmOthers;
	}
	public Integer getBonus() {
		return bonus;
	}
	public void setBonus(Integer bonus) {
		this.bonus = bonus;
	}
	public String getAgriGz() {
		return agriGz;
	}
	public void setAgriGz(String agriGz) {
		this.agriGz = agriGz;
	}
	public String getAgriSc() {
		return agriSc;
	}
	public void setAgriSc(String agriSc) {
		this.agriSc = agriSc;
	}
	public String getSfzZm() {
		return sfzZm;
	}
	public void setSfzZm(String sfzZm) {
		this.sfzZm = sfzZm;
	}
	public String getSfzFm() {
		return sfzFm;
	}
	public void setSfzFm(String sfzFm) {
		this.sfzFm = sfzFm;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getExpertType() {
		return expertType;
	}
	public void setExpertType(String expertType) {
		this.expertType = expertType;
	}
	public String getAreaSc() {
		return areaSc;
	}
	public void setAreaSc(String areaSc) {
		this.areaSc = areaSc;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	
	
}
