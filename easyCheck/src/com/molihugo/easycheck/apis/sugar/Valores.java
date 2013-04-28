package com.molihugo.easycheck.apis.sugar;

public class Valores {
	private Atributo id;
	private Atributo name;
	
	
	
	public Valores(Atributo id, Atributo name) {
		super();
		this.id = id;
		this.name = name;
	}
	public Valores() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Atributo getId() {
		return id;
	}
	public void setId(Atributo id) {
		this.id = id;
	}
	public Atributo getName() {
		return name;
	}
	public void setName(Atributo name) {
		this.name = name;
	}

}
