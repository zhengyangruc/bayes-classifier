package com.gun3y.bayes.model;

import java.io.Serializable;

import com.gun3y.bayes.model.base.BaseAttribute;

public class StringAttribute extends BaseAttribute implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3607613394269067305L;
	private String value;
	
	public StringAttribute(String name) {
		super(name);
	}
	public StringAttribute(String name, String value) {
		super(name);
		this.value = value;
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append("@ATTS[name=").append(this.getName()).append(", Value=")
				.append(this.getValue()).append("]").toString();
	}

}
