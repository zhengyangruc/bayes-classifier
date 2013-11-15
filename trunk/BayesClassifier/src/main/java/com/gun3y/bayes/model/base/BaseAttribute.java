package com.gun3y.bayes.model.base;

import com.google.common.base.Strings;
import com.gun3y.bayes.model.Attribute;

public abstract class BaseAttribute implements Attribute {


	String name;
	
	public BaseAttribute(String name) {
		super();
		this.name = name;
	}



	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BaseAttribute){
			if(!Strings.isNullOrEmpty(this.name))
				return this.name.equals(((BaseAttribute) obj).getName());
		}
		
		return false;
	}
	
	public String getName() {
		return this.name;
	}

	public abstract Object getValue();
	
	@Override
	public abstract String toString();

}
