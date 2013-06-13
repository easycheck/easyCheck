package com.molihugo.easycheck.utils;

public class Stat implements Listable{
private String pos;
private String name;
private String numberSale;
public String getPos() {
	return pos;
}
public void setPos(String pos) {
	this.pos = pos;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getNumberSale() {
	return numberSale;
}
public void setNumberSale(String numberSale) {
	this.numberSale = numberSale;
}
public Stat(String pos, String name, String numberSale) {
	super();
	this.pos = pos;
	this.name = name;
	this.numberSale = numberSale;
}
public Stat() {
	super();
	// TODO Auto-generated constructor stub
}



}
