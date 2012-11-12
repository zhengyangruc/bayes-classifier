package com.gun3y.bayes.model;

import java.io.Serializable;

import com.gun3y.bayes.model.base.BaseAttribute;

public class DoubleAttribute extends BaseAttribute implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 160772916772864023L;

	public static final double DEFAULT = 1.0;
	
	private double value;
	
	public DoubleAttribute(String name) {
		super(name);
		this.value  =new Double(DEFAULT);
	}
	
	public DoubleAttribute(String name, double value) {
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
		return sb.append("@ATTD[name=").append(this.getName()).append(", Value=")
				.append(this.getValue()).append("]").toString();
	}

}
