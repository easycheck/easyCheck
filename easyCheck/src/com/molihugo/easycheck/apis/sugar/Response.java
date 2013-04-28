package com.molihugo.easycheck.apis.sugar;

import java.util.List;

public class Response {
	private List<Contact> entry_list;
	private List<String> relationship_list;
	public List<Contact> getEntry_list() {
		return entry_list;
	}
	public void setEntry_list(List<Contact> entry_list) {
		this.entry_list = entry_list;
	}
	public List<String> getRelationship_list() {
		return relationship_list;
	}
	public void setRelationship_list(List<String> relationship_list) {
		this.relationship_list = relationship_list;
	}
	public Response(List<Contact> entry_list, List<String> relationship_list) {
		super();
		this.entry_list = entry_list;
		this.relationship_list = relationship_list;
	}
	public Response() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
