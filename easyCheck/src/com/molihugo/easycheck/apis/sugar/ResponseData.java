package com.molihugo.easycheck.apis.sugar;

import java.util.List;

public class ResponseData {
	private List<Sale> entry_list;
	private List<String> relationship_list;
	private String next_offset;
	private String result_count;
	private String total_count;
	
	public ResponseData(List<Sale> entry_list, List<String> relationship_list,
			String next_offset, String result_count, String total_count) {
		super();
		this.entry_list = entry_list;
		this.relationship_list = relationship_list;
		this.next_offset = next_offset;
		this.result_count = result_count;
		this.total_count = total_count;
	}

	public ResponseData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<Sale> getEntry_list() {
		return entry_list;
	}

	public void setEntry_list(List<Sale> entry_list) {
		this.entry_list = entry_list;
	}

	public List<String> getRelationship_list() {
		return relationship_list;
	}

	public void setRelationship_list(List<String> relationship_list) {
		this.relationship_list = relationship_list;
	}

	public String getNext_offset() {
		return next_offset;
	}

	public void setNext_offset(String next_offset) {
		this.next_offset = next_offset;
	}

	public String getResult_count() {
		return result_count;
	}

	public void setResult_count(String result_count) {
		this.result_count = result_count;
	}

	public String getTotal_count() {
		return total_count;
	}

	public void setTotal_count(String total_count) {
		this.total_count = total_count;
	}
	
	
	

}
