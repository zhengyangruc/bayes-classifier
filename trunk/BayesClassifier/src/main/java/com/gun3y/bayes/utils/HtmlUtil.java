package com.gun3y.bayes.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasChildFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.HasSiblingFilter;
import org.htmlparser.filters.StringFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.google.common.base.Strings;

public class HtmlUtil {

	private static final String coverUrl = "http://www.imdb.com";
	private static final String topMoviesUrl = "http://www.imdb.com/chart/top";
	private static final String movieIdRegex = "(/title/tt)(\\d)+(/)";
	private static final String western = "http://www.imdb.com/search/title?genres=western&title_type=feature&sort=moviemeter,asc";
	private static final String sciFi = "http://www.imdb.com/search/title?genres=sci_fi&title_type=feature&sort=moviemeter,asc";
	private static final String romance = "http://www.imdb.com/search/title?genres=romance&title_type=feature&sort=moviemeter,asc";
	private static final String crime = "http://www.imdb.com/search/title?genres=crime&title_type=feature&sort=moviemeter,asc";

	public static List<String> retrieveTopMovies() throws ParserException {
		List<String> movies = new ArrayList<String>();

		TagNameFilter tagNameFilter = new TagNameFilter();
		tagNameFilter.setName("A");

		HasAttributeFilter hasAttributeFilter = new HasAttributeFilter();
		hasAttributeFilter.setAttributeName("href");
		NodeFilter[] nodeFilter = new NodeFilter[2];
		nodeFilter[0] = tagNameFilter;
		nodeFilter[1] = hasAttributeFilter;
		AndFilter andFilter = new AndFilter();
		andFilter.setPredicates(nodeFilter);

		Parser parser = new Parser(HttpUtil.httpGet(topMoviesUrl));

		NodeList nodes = parser.parse(andFilter);

		if (nodes != null && nodes.size() > 0) {
			for (int i = 0; i < nodes.size(); i++) {
				if (nodes.elementAt(i) instanceof LinkTag) {
					LinkTag linkTag = (LinkTag) nodes.elementAt(i);

					String attribute = linkTag.getAttribute("href");

					if (!Strings.isNullOrEmpty(attribute)) {
						Pattern r = Pattern.compile(movieIdRegex);
						Matcher m = r.matcher(attribute);
						if (m.find()) {
							if (!linkTag.getAttribute("href")
									.contains(coverUrl))
								movies.add(coverUrl
										+ linkTag.getAttribute("href"));
							else
								movies.add(linkTag.getAttribute("href"));

						}
					}

				}

			}
		}
		return movies;
	}

	public static String retrieveSynopsis(String url) throws ParserException {
		TagNameFilter tagNameFilter = new TagNameFilter();
		tagNameFilter.setName("DIV");

		HasAttributeFilter hasAttributeFilter = new HasAttributeFilter();
		hasAttributeFilter.setAttributeName("id");
		hasAttributeFilter.setAttributeValue("swiki.2.1");
		NodeFilter[] nodeFilter = new NodeFilter[2];
		nodeFilter[0] = tagNameFilter;
		nodeFilter[1] = hasAttributeFilter;
		AndFilter andFilter = new AndFilter();
		andFilter.setPredicates(nodeFilter);

		Parser parser = new Parser(HttpUtil.httpGet(url + "synopsis"));

		NodeList nodes = parser.parse(andFilter);

		if (nodes != null && nodes.size() > 0) {
			return StringEscapeUtils.unescapeHtml4(nodes.elementAt(0).toPlainTextString().trim());
		}

		return null;
	}

	public static List<String> retrieveMovieGenres(String data)
			throws ParserException {
		List<String> genres = new ArrayList<String>();

		TagNameFilter tagNameFilterLocal = new TagNameFilter();
		tagNameFilterLocal.setName("A");

		HasAttributeFilter hasAttributeFilterLocal = new HasAttributeFilter();
		hasAttributeFilterLocal.setAttributeName("itemprop");

		TagNameFilter tagNameFilterParent = new TagNameFilter();
		tagNameFilterParent.setName("DIV");

		HasAttributeFilter hasAttributeFilterParent = new HasAttributeFilter();
		hasAttributeFilterParent.setAttributeName("class");
		hasAttributeFilterParent.setAttributeValue("see-more inline canwrap");

		TagNameFilter tagNameFilterChild = new TagNameFilter();
		tagNameFilterChild.setName("H4");

		StringFilter stringFilter = new StringFilter();
		stringFilter.setCaseSensitive(true);
		stringFilter.setLocale(Locale.ENGLISH);
		stringFilter.setPattern("Genres:");

		NodeFilter[] childNodes = new NodeFilter[2];
		childNodes[0] = tagNameFilterChild;
		childNodes[1] = stringFilter;

		AndFilter childAndFilter = new AndFilter();
		childAndFilter.setPredicates(childNodes);

		HasChildFilter hasChildFilter = new HasChildFilter();
		hasChildFilter.setRecursive(false);
		hasChildFilter.setChildFilter(tagNameFilterChild);

		NodeFilter[] parentNodes = new NodeFilter[3];
		parentNodes[0] = tagNameFilterParent;
		parentNodes[1] = hasAttributeFilterParent;
		parentNodes[2] = hasChildFilter;

		AndFilter parentAndFilter = new AndFilter();
		parentAndFilter.setPredicates(parentNodes);

		HasParentFilter hasParentFilter = new HasParentFilter();
		hasParentFilter.setRecursive(false);
		hasParentFilter.setParentFilter(parentAndFilter);

		NodeFilter[] nodeFilters = new NodeFilter[3];
		nodeFilters[0] = tagNameFilterLocal;
		nodeFilters[1] = hasAttributeFilterLocal;
		nodeFilters[2] = hasParentFilter;

		AndFilter andFilter = new AndFilter();
		andFilter.setPredicates(nodeFilters);

		Parser parser = new Parser(data);

		NodeList nodes = parser.parse(andFilter);

		if (nodes != null && nodes.size() > 0) {
			for (int i = 0; i < nodes.size(); i++) {
				genres.add(StringEscapeUtils.unescapeHtml4(nodes.elementAt(i)
						.toPlainTextString().trim()));
			}
		}

		return genres;
	}

	public static String retrieveMovieHeader(String data)
			throws ParserException {
		TagNameFilter tagNameFilter = new TagNameFilter();
		tagNameFilter.setName("H1");

		HasAttributeFilter hasAttributeFilter = new HasAttributeFilter();
		hasAttributeFilter.setAttributeName("class");
		hasAttributeFilter.setAttributeValue("header");
		HasAttributeFilter hasAttributeFilter2 = new HasAttributeFilter();
		hasAttributeFilter2.setAttributeName("itemprop");
		hasAttributeFilter2.setAttributeValue("name");

		NodeFilter[] nodeFilter = new NodeFilter[3];
		nodeFilter[0] = tagNameFilter;
		nodeFilter[1] = hasAttributeFilter;
		nodeFilter[2] = hasAttributeFilter2;

		AndFilter andFilter = new AndFilter();
		andFilter.setPredicates(nodeFilter);

		Parser parser = new Parser(data);

		NodeList nodes = parser.parse(andFilter);

		if (nodes != null && nodes.size() > 0) {
			String text = nodes.elementAt(0).getFirstChild()
					.toPlainTextString().trim();
			return StringEscapeUtils.unescapeHtml4(text);
		}

		return null;
	}

	public static List<String> retrieveMovies(String url)
			throws ParserException {
		List<String> movies = new ArrayList<String>();

		TagNameFilter tagNameFilter = new TagNameFilter();
		tagNameFilter.setName("A");

		HasAttributeFilter hasAttributeFilter = new HasAttributeFilter();
		hasAttributeFilter.setAttributeName("href");

		TagNameFilter tagNameFilterSibling = new TagNameFilter();
		tagNameFilterSibling.setName("SPAN");

		HasAttributeFilter hasAttributeFilterSibling = new HasAttributeFilter();
		hasAttributeFilterSibling.setAttributeName("class");
		hasAttributeFilterSibling.setAttributeValue("year_type");

		AndFilter siblingAndFilter = new AndFilter();
		siblingAndFilter.setPredicates(new NodeFilter[] { tagNameFilterSibling,
				hasAttributeFilterSibling });

		HasSiblingFilter hasSiblingFilter = new HasSiblingFilter();
		hasSiblingFilter.setSiblingFilter(siblingAndFilter);

		AndFilter andFilter = new AndFilter();
		andFilter.setPredicates(new NodeFilter[] { tagNameFilter,
				hasAttributeFilter, hasSiblingFilter });

		Parser parser = new Parser(HttpUtil.httpGet(url));

		NodeList nodes = parser.parse(andFilter);

		if (nodes != null && nodes.size() > 0) {
			for (int i = 0; i < nodes.size(); i++) {
				if (nodes.elementAt(i) instanceof LinkTag) {
					LinkTag linkTag = (LinkTag) nodes.elementAt(i);

					String attribute = linkTag.getAttribute("href");

					if (!Strings.isNullOrEmpty(attribute)) {
						Pattern r = Pattern.compile(movieIdRegex);
						Matcher m = r.matcher(attribute);
						if (m.find()) {
							if (!linkTag.getAttribute("href")
									.contains(coverUrl))
								movies.add(coverUrl
										+ linkTag.getAttribute("href"));
							else
								movies.add(linkTag.getAttribute("href"));

						}
					}

				}

			}
		}
		return movies;
	}

	public static void main(String[] args) throws ParserException,
			UnsupportedEncodingException {

		for (String string : retrieveTopMovies()) {

			String data = HttpUtil.httpGet(string);

			List<String> retrieveMovieGenres = retrieveMovieGenres(data);
			String movieName = retrieveMovieHeader(data);
			System.out.println(movieName.trim() + " - " + retrieveMovieGenres);

			String synop = retrieveSynopsis(string);

			if (!Strings.isNullOrEmpty(synop)) {
				for (String genre : retrieveMovieGenres) {
					FileUtil.createFile(genre + "\\" + movieName + ".txt",
							synop);
				}
			}
		}
		
		for (String string : retrieveMovies(western)) {

			String data = HttpUtil.httpGet(string);

			List<String> retrieveMovieGenres = retrieveMovieGenres(data);
			String movieName = retrieveMovieHeader(data);
			System.out.println(movieName.trim() + " - " + retrieveMovieGenres);

			String synop = retrieveSynopsis(string);

			if (!Strings.isNullOrEmpty(synop)) {
				for (String genre : retrieveMovieGenres) {
					FileUtil.createFile(genre + "\\" + movieName + ".txt",
							synop);
				}
			}
		}
		
		for (String string : retrieveMovies(sciFi)) {

			String data = HttpUtil.httpGet(string);

			List<String> retrieveMovieGenres = retrieveMovieGenres(data);
			String movieName = retrieveMovieHeader(data);
			System.out.println(movieName.trim() + " - " + retrieveMovieGenres);

			String synop = retrieveSynopsis(string);

			if (!Strings.isNullOrEmpty(synop)) {
				for (String genre : retrieveMovieGenres) {
					FileUtil.createFile(genre + "\\" + movieName + ".txt",
							synop);
				}
			}
		}
		for (String string : retrieveMovies(romance)) {

			String data = HttpUtil.httpGet(string);

			List<String> retrieveMovieGenres = retrieveMovieGenres(data);
			String movieName = retrieveMovieHeader(data);
			System.out.println(movieName.trim() + " - " + retrieveMovieGenres);

			String synop = retrieveSynopsis(string);

			if (!Strings.isNullOrEmpty(synop)) {
				for (String genre : retrieveMovieGenres) {
					FileUtil.createFile(genre + "\\" + movieName + ".txt",
							synop);
				}
			}
		}
		for (String string : retrieveMovies(crime)) {

			String data = HttpUtil.httpGet(string);

			List<String> retrieveMovieGenres = retrieveMovieGenres(data);
			String movieName = retrieveMovieHeader(data);
			System.out.println(movieName.trim() + " - " + retrieveMovieGenres);

			String synop = retrieveSynopsis(string);

			if (!Strings.isNullOrEmpty(synop)) {
				for (String genre : retrieveMovieGenres) {
					FileUtil.createFile(genre + "\\" + movieName + ".txt",
							synop);
				}
			}
		}
	}
}
