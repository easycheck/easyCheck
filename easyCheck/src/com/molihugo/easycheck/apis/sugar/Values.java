package com.molihugo.easycheck.apis.sugar;

public class Values {
	private Attribute id;
	private Attribute name;
	
	
	
	public Values(Attribute id, Attribute name) {
		super();
		this.id = id;
		this.name = name;
	}
	public Values() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Attribute getId() {
		return id;
	}
	public void setId(Attribute id) {
		this.id = id;
	}
	public Attribute getName() {
		return name;
	}
	public void setName(Attribute name) {
		this.name = name;
	}

}
