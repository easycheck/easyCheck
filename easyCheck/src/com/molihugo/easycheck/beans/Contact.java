package com.molihugo.easycheck.beans;

import java.io.Serializable;

public class Contact implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private String name, telephone, mail, position;

	public Contact(String nombre, String telefono, String mail, String posicion) {
		super();
		this.name = nombre;
		this.telephone = telefono;
		this.mail = mail;
		this.position = posicion;
	}

	public String getName() {
		return name;
	}

	public void setName(String nombre) {
		this.name = nombre;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telefono) {
		this.telephone = telefono;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String posicion) {
		this.position = posicion;
	}
	

}
