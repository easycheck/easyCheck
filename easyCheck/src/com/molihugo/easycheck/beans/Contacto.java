package com.molihugo.easycheck.beans;

import java.io.Serializable;

public class Contacto implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private String nombre, telefono, mail, posicion;

	public Contacto(String nombre, String telefono, String mail, String posicion) {
		super();
		this.nombre = nombre;
		this.telefono = telefono;
		this.mail = mail;
		this.posicion = posicion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPosicion() {
		return posicion;
	}

	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}
	

}
