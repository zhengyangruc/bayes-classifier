package com.gun3y.bayes.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;

import com.google.common.base.Strings;
import com.gun3y.bayes.utils.WordUtil;

public class TextSample {

	private List<String> textClasses = null;
	private String title = null;
	private String rawValue = null;
	private int totalCount = 0;
	private int reducedCount = 0;
	private Map<String, Double> words = null;

	public TextSample(String textClass, String title, String rawValue) {
		super();

		this.title = title;
		this.rawValue = rawValue;

		this.addClass(textClass);
	}

	public TextSample(List<String> textClasses, String title, String rawValue) {
		super();
		this.textClasses = textClasses;
		this.title = title;
		this.rawValue = rawValue;
	}

	public TextSample(String title, String rawValue) {
		super();
		this.title = title;
		this.rawValue = rawValue;
	}

	public void addClass(String str) {
		if (textClasses == null)
			textClasses = new ArrayList<String>();

		if (!textClasses.contains(str))
			textClasses.add(str);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TextSample) {
			return this.title.equals(((TextSample) obj).getTitle());
		}
		return super.equals(obj);
	}

	public boolean isValid() {
		if (textClasses == null || textClasses.isEmpty()
				|| Strings.isNullOrEmpty(title)
				|| Strings.isNullOrEmpty(rawValue))
			return false;

		return true;
	}

	public void extracts() {
		if (this.isValid()) {
			Map<String, Double> extractWordList = WordUtil
					.extractWordList(this.rawValue);
			
			this.reducedCount = extractWordList.size();
			this.totalCount = WordUtil.calcTotalCount(extractWordList);
			
			this.words = WordUtil.normalizeWords(extractWordList);
		}
	}

	public List<String> getTextClasses() {
		return textClasses;
	}

	public void setTextClasses(List<String> textClasses) {
		this.textClasses = textClasses;
	}

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

	public Map<String, Double> getWords() {
		return words;
	}

	public void setWords(Map<String, Double> words) {
		this.words = words;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getReducedCount() {
		return reducedCount;
	}

	public void setReducedCount(int reducedCount) {
		this.reducedCount = reducedCount;
	}

}
