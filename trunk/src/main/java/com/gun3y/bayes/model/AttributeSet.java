package com.gun3y.bayes.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AttributeSet implements Serializable{

	private static final long serialVersionUID = -9158795357485015773L;
	private Map<String, Attribute> attributes;

	public AttributeSet() {
		attributes = new HashMap<String, Attribute>();
	}

	public AttributeSet(Attribute[] att) {
		attributes = new HashMap<String, Attribute>();

		if (att != null) {
			for (Attribute attribute : att) {
				attributes.put(attribute.getName(), attribute);
			}
		}
	}

	public AttributeSet(Collection<Attribute> att) {
		attributes = new HashMap<String, Attribute>();
		if (att != null) {
			for (Attribute attribute : att) {
				attributes.put(attribute.getName(), attribute);
			}
		}
	}
	
	public Attribute[] getAttributes(){
		if(attributes != null)
			return attributes.values().toArray(new Attribute[attributes.size()]);
		
		return new Attribute[0];
	}
	
	public Attribute getAttribute(String name){
		if(attributes != null)
			return attributes.get(name);
		
		return null;
	}

}
