package com.molihugo.easycheck.apis.sugar;


public class Contact {
	private String id;
	private String module_name;
	private Values name_value_list;
	
	
	
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Contact(String id, String module_name, Values name_value_list) {
		super();
		this.id = id;
		this.module_name = module_name;
		this.name_value_list = name_value_list;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getModule_name() {
		return module_name;
	}
	public void setModule_name(String module_name) {
		this.module_name = module_name;
	}
	public Values getName_value_list() {
		return name_value_list;
	}
	public void setName_value_list(Values name_value_list) {
		this.name_value_list = name_value_list;
	}
	
	
	
}
