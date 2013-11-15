package com.gun3y.bayes.model;


public interface Instance {

	public Attribute[] getAtrributes();
	public String getConcept();
	public Attribute getAttributeByName(String name);
}
