package com.gun3y.bayes.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Strings;
import com.gun3y.bayes.log.BayesLogger;
import com.gun3y.bayes.model.Attribute;
import com.gun3y.bayes.model.AttributeSet;
import com.gun3y.bayes.model.DoubleAttribute;
import com.gun3y.bayes.model.TextSample;

public class WordUtil {
	private final static Logger logger = BayesLogger.getLogger(WordUtil.class
			.getName());
	private static int minLength = 3;

	public final static String[] ATTRIBUTE_NAMES = { "alien", "army", "camp",
			"captain", "colonel", "crew", "earth", "family", "friend",
			"future", "gang", "general", "german", "gun", "horse", "human",
			"kill", "kiss", "life", "love", "man", "officer", "people",
			"planet", "power", "relationship", "ride", "rose", "school",
			"sergeant", "sex", "sheriff", "ship", "shoot", "soldier", "space",
			"together", "train", "truck", "war", "west", "world", };

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

	public static Attribute[] extractWordList(String data) {

		Map<String, Double> attributes = new HashMap<String, Double>();

		double total = .0;

		for (String attName : WordUtil.ATTRIBUTE_NAMES) {
			attributes.put(attName, DoubleAttribute.DEFAULT);
		}

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

						if (attributes.containsKey(words[i])) {
							Double frequency = (Double) attributes
									.get(words[i]);

							attributes.put(words[i], ++frequency);
						}
					}
				}
			}

		}
		
		
		for (Entry<String, Double> entry : attributes.entrySet()) {
			total += entry.getValue();
		}
		List<DoubleAttribute> doubleAttribute = new ArrayList<DoubleAttribute>();
		for (Entry<String, Double> entry : attributes.entrySet()) {
			doubleAttribute.add(new DoubleAttribute(entry.getKey(), entry
					.getValue() / total));
		}
		
		return doubleAttribute.toArray(new DoubleAttribute[doubleAttribute
				.size()]);

	}

	public static void main(String[] args) throws IOException {

		// Set<String> w = new HashSet<String>();
		// String a = FileUtil
		// .readFile("C:\\Users\\Keysersoze\\Desktop\\words.txt");
		// w.addAll(Arrays.asList(a.split("\r\n")));
		//
		// for (File file : (new File(FileUtil.ROOT_DIR).listFiles())) {
		//
		// Map<String, TextSample> readTextSamples = readTextSamples(file
		// .getName());
		// Map<String, Double> wordMap = new HashMap<String, Double>();
		//
		// for (Entry<String, TextSample> entry : readTextSamples.entrySet()) {
		// TextSample value = entry.getValue();
		//
		// for (Entry<String, Double> pair : value.getWords().entrySet()) {
		//
		// if (wordMap.containsKey(pair.getKey()))
		// wordMap.put(pair.getKey(), wordMap.get(pair.getKey())
		// + pair.getValue());
		// else
		// wordMap.put(pair.getKey(), pair.getValue());
		// }
		// }
		//
		// String filename = "C:\\Users\\Keysersoze\\Desktop\\Genres\\"
		// + file.getName() + ".txt";
		// FileWriter fw = new FileWriter(filename, true);
		//
		// for (Entry<String, Double> entry : wordMap.entrySet()) {
		//
		// if (entry.getValue() > 10 && !w.contains(entry.getKey()))
		// fw.write(entry.getKey() + "\t" + entry.getValue() + "\n");
		// }
		//
		// fw.close();
		//
		// }

	}

	public static List<TextSample> readTextSamples() {

		List<TextSample> samples = new ArrayList<TextSample>();

		File root = new File(FileUtil.ROOT_DIR);

		for (File conceptDirs : root.listFiles()) {
			if (conceptDirs != null && conceptDirs.exists()
					&& conceptDirs.isDirectory()) {

				for (File sampleFile : conceptDirs.listFiles()) {
					if (sampleFile != null && sampleFile.exists()
							&& sampleFile.isFile()) {

						String title = sampleFile.getName();
						title = title.substring(0, title.indexOf("."));

						String rawData = FileUtil.readFile(sampleFile
								.getAbsolutePath());

						TextSample textSample = TextSample.createInstance(
								conceptDirs.getName(), title, rawData);
						samples.add(textSample);

					}
				}
			}
		}
		return samples;
	}

}
