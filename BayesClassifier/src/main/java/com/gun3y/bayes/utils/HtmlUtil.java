package com.gun3y.bayes.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.beans.FilterBean;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.gun3y.bayes.xml.model.News;

import edu.washington.cs.knowitall.morpha.MorphaStemmer;

public class HtmlUtil {

    public static Set<String> StopWords = new HashSet<String>();

    public static Map<String, String> IrregularVerbs = new HashMap<String, String>();

    static {
	loadIrregularVerbs("src/main/resources/irregular-verbs.txt");

	loadStopWords("src/main/resources/stop-words-english1.txt");
	loadStopWords("src/main/resources/stop-words-english2.txt");
	loadStopWords("src/main/resources/stop-words-english3-google.txt");
	loadStopWords("src/main/resources/stop-words-english4.txt");
	loadStopWords("src/main/resources/stop-words-english5.txt");

    }

    public static String stem(String word) {

	if (IrregularVerbs.containsKey(word))
	    return IrregularVerbs.get(word);

	// SnowballStemmer stemmer;
	// try {
	// Class<?> stemClass =
	// Class.forName("org.tartarus.snowball.ext.englishStemmer");
	// stemmer = (SnowballStemmer) stemClass.newInstance();
	// }
	// catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	//
	// stemmer.setCurrent(word);
	// stemmer.stem();
	// String stemmed = stemmer.getCurrent();

	return MorphaStemmer.stem(word);
    }

    private static void loadIrregularVerbs(String fileName) {
	// Abide,Abode/Abided,Abode/Abided/Abidden,Abides,Abiding
	String stops = FileUtil.readFile(fileName);
	stops = stops.toLowerCase(Locale.ENGLISH);
	if (stops != null) {
	    String[] lst = stops.split("\r\n");
	    for (String line : lst) {
		String[] words = line.split(",");
		if (words.length == 5) {
		    for (int i = 1; i < words.length; i++) {
			if (words[i].contains("/")) {
			    String[] splitted = words[i].split("/");
			    for (String w : splitted) {
				IrregularVerbs.put(w, words[0]);
			    }
			}
			else {
			    IrregularVerbs.put(words[i], words[0]);
			}
		    }
		}
	    }
	}
    }

    private static void loadStopWords(String fileName) {
	String stops = FileUtil.readFile(fileName);
	if (stops != null) {
	    String[] lst = stops.split("\r\n");
	    for (String s : lst) {
		StopWords.add(s.trim().toLowerCase(Locale.ENGLISH));
	    }
	}
    }

    public static void main(String[] args) {

	/*
	 * List<News> enter = read(".\\News Set\\ENTER"); for (News news :
	 * enter) { news.setCategory(Category.ENTERTAINMENT);
	 * writeNews(".\\News Set\\ENTER", news);}
	 * 
	 * 
	 * List<News> health = read(".\\News Set\\HEALTH"); for (News news :
	 * health) { news.setCategory(Category.HEALTH);
	 * writeNews(".\\News Set\\HEALTH", news);}
	 * 
	 * List<News> politic = read(".\\News Set\\POLITICS"); for (News news :
	 * politic) { news.setCategory(Category.POLITIC);
	 * writeNews(".\\News Set\\POLITICS", news);}
	 * 
	 * List<News> sport = read(".\\News Set\\SPORT"); for (News news :
	 * sport) { news.setCategory(Category.SPORT);
	 * writeNews(".\\News Set\\SPORT", news);}
	 * 
	 * 
	 * List<News> golf = read(".\\Sport Set\\GOLF"); for (News news : golf)
	 * { news.setCategory(Category.GOLF); writeNews(".\\Sport Set\\GOLF",
	 * news);}
	 * 
	 * List<News> nba = read(".\\Sport Set\\NBA"); for (News news : nba) {
	 * news.setCategory(Category.NBA); writeNews(".\\Sport Set\\NBA",
	 * news);}
	 * 
	 * List<News> tennis = read(".\\Sport Set\\TENNIS"); for (News news :
	 * tennis) { news.setCategory(Category.TENNIS);
	 * writeNews(".\\Sport Set\\TENNIS", news);}
	 * 
	 * List<News> nfl = read(".\\Sport Set\\NFL"); for (News news : nfl) {
	 * news.setCategory(Category.NFL); writeNews(".\\Sport Set\\NFL",
	 * news);}
	 */

	List<News> enter = read(".\\News Set\\ENTER");
	List<News> health = read(".\\News Set\\HEALTH");
	List<News> politic = read(".\\News Set\\POLITICS");
	List<News> sport = read(".\\News Set\\SPORT");
	List<News> golf = read(".\\Sport Set\\GOLF");
	List<News> nba = read(".\\Sport Set\\NBA");
	List<News> tennis = read(".\\Sport Set\\TENNIS");
	List<News> nfl = read(".\\Sport Set\\NFL");

	List<News> all = new ArrayList<News>();
	all.addAll(golf);
	all.addAll(nba);
	all.addAll(tennis);
	all.addAll(nfl);
	all.addAll(politic);
	all.addAll(health);
	all.addAll(enter);

	Map<String, Integer> wordMap = new HashMap<String, Integer>();
	int count = 0;
	for (News news : all) {
	    String data = news.getData().trim().toLowerCase(Locale.ENGLISH);
	    // System.out.println(data);
	    String[] words = data.split("[\\W\\d\\n\\r]");

	    for (String w : words) {
		if (StringUtils.isNotBlank(w) && w.length() > 1 && !StopWords.contains(w)) {
		    String stemmedWord = stem(w);

		    if (wordMap.containsKey(stemmedWord))
			wordMap.put(stemmedWord, wordMap.get(stemmedWord) + 1);
		    else
			wordMap.put(stemmedWord, 1);
		    count++;

		}
	    }
	}

	List<Map.Entry<String, Integer>> freq = new ArrayList<Map.Entry<String, Integer>>(wordMap.entrySet());
	Collections.sort(freq, new Comparator<Map.Entry<String, Integer>>() {
	    public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
		return Integer.compare(o2.getValue(), o1.getValue());
	    }
	});

	for (Entry<String, Integer> entry : freq) {
	    System.out.println("\"" + entry.getKey() + "\"," + entry.getValue());
	}
	System.out.println(count);

    }

    public static List<News> read(String dir) {
	File dirFile = new File(dir);
	List<News> newsList = new ArrayList<News>();

	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	if (dirFile != null && dirFile.isDirectory()) {
	    File[] listFiles = dirFile.listFiles();
	    for (File f : listFiles) {
		News news;
		try {
		    news = gson.fromJson(new BufferedReader(new FileReader(f)), News.class);

		    Map<String, Integer> wordMap = new HashMap<String, Integer>();
		    int count = 0;
		    String data = news.getData().trim().toLowerCase(Locale.ENGLISH);

		    String[] words = data.split("[\\W\\d\\n\\r]");

		    for (String w : words) {
			if (StringUtils.isNotBlank(w) && w.length() > 1 && !HtmlUtil.StopWords.contains(w)) {
			    String stemmedWord = HtmlUtil.stem(w);

			    if (wordMap.containsKey(stemmedWord))
				wordMap.put(stemmedWord, wordMap.get(stemmedWord) + 1);
			    else
				wordMap.put(stemmedWord, 1);

			    count++;

			}
		    }
		    // totalCount += count;
		    news.setWordCount(count);
		    news.setWordMap(wordMap);

		    newsList.add(news);
		}
		catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}

	    }
	}

	return newsList;
    }

    public static void retrieveAllNews() {
	String golfSource = readFile("C:\\Users\\musta_000\\Desktop\\Golf.htm");
	String nbaSource = readFile("C:/Users/musta_000/Desktop/NBA.htm");
	String tennisSource = readFile("C:/Users/musta_000/Desktop/Tennis.htm");
	String nflSource = readFile("C:/Users/musta_000/Desktop/NFL.htm");

	List<String> golfLinks = readLinks(golfSource);
	System.out.println("golf:" + golfLinks.size());

	List<String> nbaLinks = readLinks(nbaSource);
	System.out.println("nba:" + nbaLinks.size());

	List<String> tennisLinks = readLinks(tennisSource);
	System.out.println("tennis:" + tennisLinks.size());

	List<String> nflLinks = readLinks(nflSource);
	System.out.println("nfl:" + nflLinks.size());

	List<String> politicsLinks = readNews("http://www.cbsnews.com/2003-250_162-0.html");
	politicsLinks.addAll(readNews("http://www.cbsnews.com/2003-250_162-0-1.html"));
	politicsLinks.addAll(readNews("http://www.cbsnews.com/2003-250_162-0-2.html"));
	System.out.println("politics:" + politicsLinks.size());

	List<String> entertainmentLinks = readNews("http://www.cbsnews.com/2003-207_162-0.html");
	entertainmentLinks.addAll(readNews("http://www.cbsnews.com/2003-207_162-0-1.html"));
	entertainmentLinks.addAll(readNews("http://www.cbsnews.com/2003-207_162-0-2.html"));
	System.out.println("enter:" + entertainmentLinks.size());

	List<String> healthLinks = readNews("http://www.cbsnews.com/2003-204_162-0.html");
	healthLinks.addAll(readNews("http://www.cbsnews.com/2003-204_162-0-1.html"));
	healthLinks.addAll(readNews("http://www.cbsnews.com/2003-204_162-0-2.html"));
	System.out.println("health:" + healthLinks.size());

	for (int i = 0; i < 30; i++) {
	    News golf = readSportDetails(golfLinks.get(i));
	    writeNews("GOLF", golf);
	    News nba = readSportDetails(nbaLinks.get(i));
	    writeNews("NBA", nba);
	    News tennis = readSportDetails(tennisLinks.get(i));
	    writeNews("TENNIS", tennis);
	    News nfl = readSportDetails(nflLinks.get(i));
	    writeNews("NFL", nfl);

	    News politics = readNewsDetails(politicsLinks.get(i));
	    writeNews("POLITICS", politics);
	    News enter = readNewsDetails(entertainmentLinks.get(i));
	    writeNews("ENTER", enter);
	    News healt = readNewsDetails(healthLinks.get(i));
	    writeNews("HEALTH", healt);
	}
    }

    public static void writeNews(String dirName, News n) {
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	String json = gson.toJson(n);

	FileUtil.createFile(dirName + "\\" + n.getTitle() + ".json", json);
	System.out.println(n.getTitle());

    }

    public static News readNewsDetails(String url) {
	News news = new News();
	news.setUrl(url);
	String source = HttpUtil.httpGet(url);

	TagNameFilter filter0 = new TagNameFilter();
	filter0.setName("H1");
	NodeFilter[] array0 = new NodeFilter[1];
	array0[0] = filter0;
	AndFilter filter1 = new AndFilter();
	filter1.setPredicates(array0);
	TagNameFilter filter2 = new TagNameFilter();
	filter2.setName("DIV");
	HasAttributeFilter filter3 = new HasAttributeFilter();
	filter3.setAttributeName("class");
	filter3.setAttributeValue("storyText");
	NodeFilter[] array1 = new NodeFilter[2];
	array1[0] = filter2;
	array1[1] = filter3;
	AndFilter filter4 = new AndFilter();
	filter4.setPredicates(array1);
	NodeFilter[] array2 = new NodeFilter[2];
	array2[0] = filter1;
	array2[1] = filter4;
	try {
	    Parser parser = new Parser(source);
	    NodeList nodes = parser.parse(filter4);
	    news.setData(StringEscapeUtils.unescapeHtml4(nodes.asString()));

	    Parser parserTitle = new Parser(source);
	    NodeList title = parserTitle.parse(filter1);
	    news.setTitle(StringEscapeUtils.unescapeHtml4(title.asString()));
	}
	catch (ParserException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return news;
    }

    public static News readSportDetails(String url) {
	News news = new News();
	news.setUrl(url);
	String source = HttpUtil.httpGet(url);
	TagNameFilter filter0 = new TagNameFilter();
	filter0.setName("H1");
	HasAttributeFilter filter1 = new HasAttributeFilter();
	filter1.setAttributeName("class");
	filter1.setAttributeValue("storyHdl");
	NodeFilter[] array0 = new NodeFilter[2];
	array0[0] = filter0;
	array0[1] = filter1;
	AndFilter filter2 = new AndFilter();
	filter2.setPredicates(array0);
	TagNameFilter filter3 = new TagNameFilter();
	filter3.setName("DIV");
	HasAttributeFilter filter4 = new HasAttributeFilter();
	filter4.setAttributeName("class");
	filter4.setAttributeValue("storyCopy");
	NodeFilter[] array1 = new NodeFilter[2];
	array1[0] = filter3;
	array1[1] = filter4;
	AndFilter filter5 = new AndFilter();
	filter5.setPredicates(array1);
	NodeFilter[] array2 = new NodeFilter[2];
	array2[0] = filter2;
	array2[1] = filter5;

	try {
	    Parser parser = new Parser(source);
	    NodeList nodes = parser.parse(filter5);
	    news.setData(StringEscapeUtils.unescapeHtml4(nodes.asString()));

	    Parser parserTitle = new Parser(source);
	    NodeList title = parserTitle.parse(filter2);
	    news.setTitle(StringEscapeUtils.unescapeHtml4(title.asString()));
	}
	catch (ParserException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return news;
    }

    public static String readFile(String fileName) {
	FileInputStream inputStream = null;
	String everything = null;
	try {
	    inputStream = new FileInputStream(fileName);
	}
	catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	try {
	    everything = IOUtils.toString(inputStream);

	}
	catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	finally {
	    try {
		inputStream.close();
	    }
	    catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

	return everything;

    }

    public static List<String> readNews(String url) {
	List<String> links = new ArrayList<String>();

	TagNameFilter filter0 = new TagNameFilter();
	filter0.setName("A");
	TagNameFilter filter1 = new TagNameFilter();
	filter1.setName("H2");
	HasAttributeFilter filter2 = new HasAttributeFilter();
	filter2.setAttributeName("class");
	filter2.setAttributeValue("storyTitle");
	NodeFilter[] array0 = new NodeFilter[2];
	array0[0] = filter1;
	array0[1] = filter2;
	AndFilter filter3 = new AndFilter();
	filter3.setPredicates(array0);
	HasParentFilter filter4 = new HasParentFilter();
	filter4.setRecursive(true);
	filter4.setParentFilter(filter3);
	NodeFilter[] array1 = new NodeFilter[2];
	array1[0] = filter0;
	array1[1] = filter4;
	AndFilter filter5 = new AndFilter();
	filter5.setPredicates(array1);
	NodeFilter[] array2 = new NodeFilter[1];
	array2[0] = filter5;
	FilterBean bean = new FilterBean();
	bean.setFilters(array2);
	bean.setURL(url);
	NodeList nodes = bean.getNodes();

	if (nodes != null && nodes.size() > 0) {
	    for (int i = 0; i < nodes.size(); i++) {

		if (nodes.elementAt(i) instanceof LinkTag) {
		    LinkTag linkTag = (LinkTag) nodes.elementAt(i);
		    String link = linkTag.getAttribute("href");
		    if (!link.startsWith("http://www.cbsnews.com/video/")) {
			links.add(link);
			System.out.println(link);
		    }

		}
	    }
	}

	return links;
    }

    public static List<String> readLinks(String source) {
	List<String> links = new ArrayList<String>();

	TagNameFilter filter0 = new TagNameFilter();
	filter0.setName("A");
	TagNameFilter filter1 = new TagNameFilter();
	filter1.setName("DIV");
	HasAttributeFilter filter2 = new HasAttributeFilter();
	filter2.setAttributeName("class");
	filter2.setAttributeValue("titleContent mBottom5 fontUbuntuBold");
	NodeFilter[] array0 = new NodeFilter[2];
	array0[0] = filter1;
	array0[1] = filter2;
	AndFilter filter3 = new AndFilter();
	filter3.setPredicates(array0);
	HasParentFilter filter4 = new HasParentFilter();
	filter4.setRecursive(true);
	filter4.setParentFilter(filter3);
	NodeFilter[] array1 = new NodeFilter[2];
	array1[0] = filter0;
	array1[1] = filter4;
	AndFilter filter5 = new AndFilter();
	filter5.setPredicates(array1);

	Parser parser;
	try {
	    parser = new Parser(source);
	    NodeList nodes = parser.parse(filter5);

	    if (nodes != null && nodes.size() > 0) {
		for (int i = 0; i < nodes.size(); i++) {

		    String title = StringEscapeUtils.unescapeHtml4(nodes.elementAt(i).toPlainTextString().trim());
		    if (title != null && !title.startsWith("VIDEO:") && !title.startsWith("PHOTO:")
			    && nodes.elementAt(i) instanceof LinkTag) {
			LinkTag linkTag = (LinkTag) nodes.elementAt(i);
			String link = linkTag.getAttribute("href");
			if (link.startsWith("http://www.cbssports.com")) {
			    links.add(linkTag.getAttribute("href"));
			    System.out.println(linkTag.getAttribute("href"));
			}

		    }
		}
	    }
	}
	catch (ParserException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return links;

    }
}
