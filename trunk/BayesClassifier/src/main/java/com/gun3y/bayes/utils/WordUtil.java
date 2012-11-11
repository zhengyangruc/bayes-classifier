package com.gun3y.bayes.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Strings;
import com.gun3y.bayes.beans.TextSample;
import com.gun3y.bayes.log.BayesLogger;

public class WordUtil {
	private final static Logger logger = BayesLogger.getLogger(WordUtil.class
			.getName());
	private static int minLength = 3;

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

	public static Map<String, Double> extractWordList(String data) {

		Map<String, Double> map = new HashMap<String, Double>();

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
					if (words[i].length() >= minLength
							&& !StringUtils.isNumeric(words[i])) {

						words[i] = words[i].toLowerCase(Locale.ENGLISH).trim();

						Double frequency = (Double) map.get(words[i]);

						if (frequency == null)
							frequency = .0;

						map.put(words[i], ++frequency);
					}
				}
			}

		}

		return map;

	}

	public static void main(String[] args) throws IOException {
		Map<String, TextSample> readTextSamples = readTextSamples("Drama");
		for (Entry<String, TextSample> entry : readTextSamples.entrySet()) {
			TextSample value = entry.getValue();

			for (Entry<String, Double> pair : value.getWords().entrySet()) {
				logger.log(Level.INFO,
						value.getTitle() + " " + value.getTotalCount() + " "
								+ value.getReducedCount() + " " + pair.getKey()
								+ " " + pair.getValue());
			}
		}
	}

	public static Map<String, TextSample> readTextSamples() {
		return readTextSamples(null);
	}

	public static Map<String, TextSample> readTextSamples(String className) {
		File[] classList;
		Map<String, TextSample> textSamples = new HashMap<String, TextSample>();
		File rootDir = new File(FileUtil.ROOT_DIR);

		if (Strings.isNullOrEmpty(className)) {
			classList = rootDir.listFiles();
		} else {
			File classDir = new File(FileUtil.ROOT_DIR + className);

			if (classDir != null && classDir.exists() && classDir.isDirectory())
				classList = new File[] { classDir };
			else
				return textSamples;
		}

		for (File dirs : classList) {
			if (dirs != null && dirs.isDirectory()) {
				File[] samples = dirs.listFiles();
				for (File file : samples) {
					if (file != null && file.isFile()) {
						String title = file.getName();
						title = title.substring(0, title.indexOf("."));
						String rawdata = FileUtil.readFile(file
								.getAbsolutePath());
						String sampleClass = dirs.getName();

						TextSample textSample = textSamples.get(title);

						if (textSample == null) {
							textSample = new TextSample(sampleClass, title,
									rawdata);
						}

						textSample.addClass(sampleClass);
						textSample.extracts();

						textSamples.put(title, textSample);
					}
				}
			}
		}

		return textSamples;
	}

}
