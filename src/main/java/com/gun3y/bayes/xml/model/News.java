package com.gun3y.bayes.xml.model;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class News {
    
    

    String url;
    String title;
    String data;
    Category category;
    Map<String, Integer> wordMap;
    int wordCount;


    public boolean check() {
	return StringUtils.isBlank(this.url) || StringUtils.isBlank(this.title) || StringUtils.isBlank(this.data)
		|| this.data.length() < 100;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getData() {
	return data;
    }

    public void setData(String data) {
	this.data = data;
    }
    
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append(getClass().getName()).append(" {\n\turl: ").append(url).append("\n\ttitle: ").append(title).append("\n\tdata: ")
		.append(data).append("\n}");
	return builder.toString();
    }

    public Map<String, Integer> getWordMap() {
        return wordMap;
    }

    public void setWordMap(Map<String, Integer> wordMap) {
        this.wordMap = wordMap;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

}
