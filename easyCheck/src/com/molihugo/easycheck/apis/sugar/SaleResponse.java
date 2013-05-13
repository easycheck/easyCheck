package com.molihugo.easycheck.apis.sugar;

public class SaleResponse {
	
	private String contact;
	private String ammount;
	private String dateCreated;
	private String company;
	private String saleName;
	private String type;
	private String description;
	
	public SaleResponse(String contact, String ammount, String dateCreated,
			String company, String saleName, String type, String description) {
		super();
		this.contact = contact;
		this.ammount = ammount;
		this.dateCreated = dateCreated;
		this.company = company;
		this.saleName = saleName;
		this.type = type;
		this.description = description;
	}

	public SaleResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAmmount() {
		return ammount;
	}

	public void setAmmount(String ammount) {
		this.ammount = ammount;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getSaleName() {
		return saleName;
	}

	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	

}
