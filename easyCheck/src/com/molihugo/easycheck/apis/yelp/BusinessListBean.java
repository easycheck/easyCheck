package com.molihugo.easycheck.apis.yelp;

import java.util.List;


public class BusinessListBean {

	private Region region;
	private int total;
	private List<BusinessDetail> businesses;

	public BusinessListBean(Region region, int total,
			List<BusinessDetail> businesses) {
		super();
		this.region = region;
		this.total = total;
		this.businesses = businesses;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<BusinessDetail> getBusinesses() {
		return businesses;
	}

	public void setBusinesses(List<BusinessDetail> businesses) {
		this.businesses = businesses;
	}
}
