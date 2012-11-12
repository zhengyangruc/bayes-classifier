package com.gun3y.bayes.model;

import com.gun3y.bayes.model.base.BaseInstance;
import com.gun3y.bayes.utils.WordUtil;

public class TextSample extends BaseInstance {

	public static TextSample createInstance(String concept, String title,
			String rawData) {
		Attribute[] attributes = WordUtil.extractWordList(rawData);

		TextSample sample = new TextSample(concept, attributes);
		sample.setRawValue(rawData);
		sample.setTitle(title);

		return sample;
	}

	public TextSample(String concept, Attribute[] attributes) {
		super(concept, attributes);
	}

	public TextSample(Attribute[] attributes) {
		super(attributes);
	}

	public TextSample(String concept, AttributeSet attributes) {
		super(concept, attributes);
	}

	private String title = null;
	private String rawValue = null;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRawValue() {
		return rawValue;
	}

	public void setRawValue(String rawValue) {
		this.rawValue = rawValue;
	}
}
