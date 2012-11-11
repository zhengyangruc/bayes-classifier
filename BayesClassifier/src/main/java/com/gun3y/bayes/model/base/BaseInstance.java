package com.gun3y.bayes.model.base;

import com.gun3y.bayes.model.Attribute;
import com.gun3y.bayes.model.AttributeSet;
import com.gun3y.bayes.model.Instance;

public class BaseInstance implements Instance {

	private String concept;
	private AttributeSet attributes;

	public BaseInstance(Attribute[] attributes){
		super();
		this.attributes = new AttributeSet(attributes);
	}
	
	public BaseInstance(String concept, Attribute[] attributes) {
		super();
		this.concept = concept;
		this.attributes = new AttributeSet(attributes);
	}

	public BaseInstance(String concept, AttributeSet attributes) {
		super();
		this.concept = concept;
		this.attributes = attributes;
	}

	@Override
	public Attribute[] getAtrributes() {
		return this.attributes.getAttributes();
	}

	@Override
	public String getConcept() {
		return this.concept;
	}

	@Override
	public Attribute getAttributeByName(String name) {
		if (attributes != null)
			return attributes.getAttribute(name);

		return null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("@Instance[Concept=").append(this.concept);
		for (Attribute att : attributes.getAttributes()) {
			sb.append(", " + att);
		}
		sb.append("]");

		return sb.toString();
	}

}
