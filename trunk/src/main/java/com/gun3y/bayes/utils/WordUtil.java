package com.gun3y.bayes.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Strings;
import com.gun3y.bayes.model.Attribute;
import com.gun3y.bayes.model.DoubleAttribute;
import com.gun3y.bayes.model.TextSample;
import com.gun3y.bayes.xml.model.News;

public class WordUtil {
    // private final static Logger logger =
    // BayesLogger.getLogger(WordUtil.class.getName());
    private static int minLength = 3;

    public final static String[] ATTRIBUTE_NAMES = { "alien", "army", "camp", "captain", "colonel", "crew", "earth", "family", "friend",
	    "future", "gang", "general", "german", "gun", "horse", "human", "kill", "kiss", "life", "love", "man", "officer", "people",
	    "planet", "power", "relationship", "ride", "rose", "school", "sergeant", "sex", "sheriff", "ship", "shoot", "soldier", "space",
	    "together", "train", "truck", "war", "west", "world", };

    // public final static String[] ATTRIBUTE_NAMES = { "tuco", "wang", "nick",
    // "rick", "roy", "johnny", "union", "range", "mattie", "montero",
    // "scarlett", "charlie", "blondie", "neo", "arthur", "clementine",
    // "sarah", "diego", "wolverine", "guido", "akira", "cogburn",
    // "summer", "rango", "schindler", "dylan", "james", "luke", "jacob",
    // "alejandro", "miller", "paden", "earp", "lolas", "sam", "spearman",
    // "waite", "memories", "kirk", "jesse", };

    public static int calcTotalCount(Map<String, Double> words) {
	int count = 0;
	if (words != null && !words.isEmpty()) {
	    for (Entry<String, Double> entry : words.entrySet()) {
		count += entry.getValue();
	    }
	}
	return count;
    }

    public static Map<String, Double> normalizeWords(Map<String, Double> words) {

	if (words != null && !words.isEmpty()) {

	    Map<String, Double> newMap = new HashMap<String, Double>(words);
	    double total = 0;

	    // Calculates the total word in the map
	    for (Entry<String, Double> entry : newMap.entrySet()) {
		total += entry.getValue();
	    }

	    for (Entry<String, Double> entry : newMap.entrySet()) {
		entry.setValue(entry.getValue() / total);
	    }

	    return newMap;
	}

	return null;
    }

    public static List<String> getWords(String data) {

	List<String> resultList = new ArrayList<String>();

	if (!Strings.isNullOrEmpty(data)) {

	    // Create line pattern
	    Pattern linePattern = Pattern.compile(".*$", Pattern.MULTILINE);

	    // Create word pattern
	    Pattern wordBreakPattern = Pattern.compile("[\\p{Punct}\\s}]");

	    // Match line pattern to buffer
	    Matcher lineMatcher = linePattern.matcher(data);

	    // For each line
	    while (lineMatcher.find()) {
		// Get line
		CharSequence line = lineMatcher.group();

		// Get array of words on line
		String words[] = wordBreakPattern.split(line);

		// For each word
		for (int i = 0, n = words.length; i < n; i++) {
		    if (words[i].length() >= minLength && !StringUtils.isNumeric(words[i])) {

			resultList.add(words[i].toLowerCase(Locale.ENGLISH).trim());
		    }
		}
	    }

	}

	return resultList;
    }

    public static Attribute[] extractWordList(String data) {

	Map<String, Double> attributes = new HashMap<String, Double>();

	for (String attName : WordUtil.ATTRIBUTE_NAMES) {
	    attributes.put(attName, DoubleAttribute.DEFAULT);
	}

	double count = 0;

	if (!Strings.isNullOrEmpty(data)) {

	    // Create line pattern
	    Pattern linePattern = Pattern.compile(".*$", Pattern.MULTILINE);

	    // Create word pattern
	    Pattern wordBreakPattern = Pattern.compile("[\\p{Punct}\\s}]");

	    // Match line pattern to buffer
	    Matcher lineMatcher = linePattern.matcher(data);

	    // For each line
	    while (lineMatcher.find()) {
		// Get line
		CharSequence line = lineMatcher.group();

		// Get array of words on line
		String words[] = wordBreakPattern.split(line);

		// For each word
		for (int i = 0, n = words.length; i < n; i++) {
		    if (words[i].length() >= minLength && !StringUtils.isNumeric(words[i])) {

			count++;
			words[i] = words[i].toLowerCase(Locale.ENGLISH).trim();

			if (attributes.containsKey(words[i])) {
			    Double frequency = (Double) attributes.get(words[i]);

			    attributes.put(words[i], ++frequency);
			}
		    }
		}
	    }

	}

	List<DoubleAttribute> doubleAttribute = new ArrayList<DoubleAttribute>();
	for (Entry<String, Double> entry : attributes.entrySet()) {
	    doubleAttribute.add(new DoubleAttribute(entry.getKey(), entry.getValue() / count));
	}

	return doubleAttribute.toArray(new DoubleAttribute[doubleAttribute.size()]);

    }

    public static void main(String[] args) throws IOException {

	printFiles();
    }

    public static void printFiles() throws IOException {
	String restrictedWordFileName = "C:\\Users\\Keysersoze\\Desktop\\words.txt";
	String genreFilePath = "C:\\Users\\Keysersoze\\Desktop\\Genres\\";
	Set<String> restrictedWordSet = new HashSet<String>();
	String restrictedWords = FileUtil.readFile(restrictedWordFileName);

	restrictedWordSet.addAll(Arrays.asList(restrictedWords.split("\r\n")));

	for (File conceptDir : (new File(FileUtil.ROOT_DIR).listFiles())) {
	    if (conceptDir != null && conceptDir.exists() && conceptDir.isDirectory()) {
		Map<String, Double> map = new HashMap<String, Double>();

		for (File file : conceptDir.listFiles()) {
		    if (file != null && file.isFile() && file.exists()) {

			Map<String, Double> fileMap = new HashMap<String, Double>();

			String raw = FileUtil.readFile(file.getAbsolutePath());
			List<String> words = getWords(raw);
			for (String string : words) {
			    Double d = fileMap.get(string);
			    if (d == null)
				d = 1.;
			    fileMap.put(string, d + 1);
			}

			for (Entry<String, Double> entry : fileMap.entrySet()) {
			    double val = (Double) entry.getValue();

			    val = val / words.size();

			    Double att = map.get(entry.getKey());

			    if (att == null)
				att = val;
			    att += val;
			    map.put(entry.getKey(), att / 2);
			}
		    }
		}

		FileWriter fw = new FileWriter(genreFilePath + conceptDir.getName() + ".txt", true);

		for (Entry<String, Double> entry : map.entrySet()) {

		    String num = String.format("%1$,.9f", entry.getValue());

		    if (!restrictedWordSet.contains(entry.getKey()))
			fw.write(entry.getKey() + "\t" + num + "\n");
		}
		fw.close();

	    }

	}

    }

    public static List<Attribute> selectTopAttributes(int attSize, List<News> l1, List<News> l2, List<News> l3, List<News> l4) {

	List<Attribute> l1Attr = getPopularAttributes(l1);
	List<Attribute> l2Attr = getPopularAttributes(l2);
	List<Attribute> l3Attr = getPopularAttributes(l3);
	List<Attribute> l4Attr = getPopularAttributes(l4);

	Set<Attribute> attributeSet = new HashSet<Attribute>();

	Iterator<Attribute> iterator1 = l1Attr.iterator();
	Iterator<Attribute> iterator2 = l2Attr.iterator();
	Iterator<Attribute> iterator3 = l3Attr.iterator();
	Iterator<Attribute> iterator4 = l4Attr.iterator();

	while (attributeSet.size() < attSize) {
	    while (iterator1.hasNext()) {
		Attribute next = iterator1.next();
		if (!attributeSet.contains(next)) {
		    attributeSet.add(next);

		    if (attributeSet.size() >= attSize)
			return Arrays.asList(attributeSet.toArray(new Attribute[attributeSet.size()]));

		    break;
		}

	    }
	    while (iterator2.hasNext()) {
		Attribute next = iterator2.next();
		if (!attributeSet.contains(next)) {
		    attributeSet.add(next);

		    if (attributeSet.size() >= attSize)
			return Arrays.asList(attributeSet.toArray(new Attribute[attributeSet.size()]));

		    break;
		}

	    }
	    while (iterator3.hasNext()) {
		Attribute next = iterator3.next();
		if (!attributeSet.contains(next)) {
		    attributeSet.add(next);

		    if (attributeSet.size() >= attSize)
			return Arrays.asList(attributeSet.toArray(new Attribute[attributeSet.size()]));

		    break;
		}

	    }
	    while (iterator4.hasNext()) {
		Attribute next = iterator4.next();
		if (!attributeSet.contains(next)) {
		    attributeSet.add(next);

		    if (attributeSet.size() >= attSize)
			return Arrays.asList(attributeSet.toArray(new Attribute[attributeSet.size()]));

		    break;
		}

	    }
	}

	return Arrays.asList(attributeSet.toArray(new Attribute[attributeSet.size()]));
    }

    public static List<Attribute> getPopularAttributes(List<News> allNews) {
	List<Attribute> attList = new ArrayList<Attribute>();

	Map<String, Integer> wordMap = new HashMap<String, Integer>();

	for (News news : allNews) {
	    Map<String, Integer> tempWordMap = news.getWordMap();

	    for (Entry<String, Integer> entry : tempWordMap.entrySet()) {

		if (wordMap.containsKey(entry.getKey()))
		    wordMap.put(entry.getKey(), wordMap.get(entry.getKey()) + entry.getValue());
		else
		    wordMap.put(entry.getKey(), 1);
	    }
	}

	List<Map.Entry<String, Integer>> freq = new ArrayList<Map.Entry<String, Integer>>(wordMap.entrySet());
	Collections.sort(freq, new Comparator<Map.Entry<String, Integer>>() {
	    public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
		return Integer.compare(o2.getValue(), o1.getValue());
	    }
	});

	for (Entry<String, Integer> entry : freq) {
	    DoubleAttribute at = new DoubleAttribute(entry.getKey(), entry.getValue());
	    attList.add(at);
	    //System.out.println(at);
	}

	return attList;
    }

    @SafeVarargs
    public static List<TextSample> readTextSamples(List<Attribute> mostPopular, List<News>... newsArray) {
	List<TextSample> samples = new ArrayList<TextSample>();

	double total = 0;
	for (List<News> newList : newsArray) {
	    for (News news : newList) {
		total += (double)news.getWordCount();
	    }
	}

	for (List<News> newList : newsArray) {
	    for (News news : newList) {
		Map<String, Integer> wordMap = news.getWordMap();
		Attribute[] attributes = new DoubleAttribute[mostPopular.size()];
		for (int i = 0; i < attributes.length; i++) {
		    Attribute attr = mostPopular.get(i);

		    double freq = 1;
		    if (wordMap.containsKey(attr.getName())) {
			freq = (double) wordMap.get(attr.getName());
			freq++;
		    }

		    
		    attributes[i] = new DoubleAttribute(attr.getName(), freq/(total*((double)news.getWordCount())));
		}

		TextSample sample = new TextSample(news.getCategory().name(), attributes);
		sample.setRawValue(news.getData());
		sample.setTitle(news.getTitle());

		samples.add(sample);

	    }
	}

	return samples;
    }

}
