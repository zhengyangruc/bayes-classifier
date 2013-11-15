package com.gun3y.bayes.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.gun3y.bayes.NaiveBayes;
import com.gun3y.bayes.model.Attribute;
import com.gun3y.bayes.model.TextSample;
import com.gun3y.bayes.model.TrainingSet;
import com.gun3y.bayes.utils.HtmlUtil;
import com.gun3y.bayes.utils.WordUtil;
import com.gun3y.bayes.xml.model.News;

public class BayesTest {

    static class Pair {
	List<News> trainSet;
	List<News> testSet;
    }

    public static Pair generateSet(List<News> news, int trainSize, int testSize) {
	if ((trainSize + testSize) > news.size())
	    return null;

	Random rand = new Random();

	List<Integer> indexes = new ArrayList<Integer>();
	for (int i = 0; i < news.size(); i++) {
	    indexes.add(i);
	}

	Pair pair = new Pair();

	pair.trainSet = new ArrayList<News>();
	pair.testSet = new ArrayList<News>();
	while (pair.trainSet.size() != trainSize) {
	    int idx = rand.nextInt(indexes.size());
	    pair.trainSet.add(news.get(idx));
	    indexes.remove(idx);
	}

	while (pair.testSet.size() != testSize) {
	    int idx = rand.nextInt(indexes.size());
	    pair.testSet.add(news.get(idx));
	    indexes.remove(idx);
	}

	return pair;
    }

    public static void displayConfusionMatrix(Map<String, Map<String, Double>> cofMatrix) {

	double total = 0;
	for (Entry<String, Map<String, Double>> rowEntry : cofMatrix.entrySet()) {
	    System.out.print(rowEntry.getKey() + "\t");
	    for (Entry<String, Double> colEntry : rowEntry.getValue().entrySet()) {
		System.out.print(colEntry.getKey() + ":" + colEntry.getValue());
		total += colEntry.getValue();
	    }
	    System.out.println();
	}
	System.out.println(total);

    }

    public static void similarContent() {
	List<News> golf = HtmlUtil.read(".\\Sport Set\\GOLF");
	List<News> nba = HtmlUtil.read(".\\Sport Set\\NBA");
	List<News> nfl = HtmlUtil.read(".\\Sport Set\\NFL");
	List<News> tennis = HtmlUtil.read(".\\Sport Set\\TENNIS");

	Map<String, Map<String, Double>> cofMatrix = run(100, 10, 10, 20, golf, nba, nfl, tennis);
	displayConfusionMatrix(cofMatrix);

	Map<String, Map<String, Double>> cofMatrix2 = run(100, 10, 10, 30, golf, nba, nfl, tennis);
	displayConfusionMatrix(cofMatrix2);

	Map<String, Map<String, Double>> cofMatrix3 = run(100, 10, 10, 40, golf, nba, nfl, tennis);
	displayConfusionMatrix(cofMatrix3);

	Map<String, Map<String, Double>> cofMatrix4 = run(100, 10, 10, 50, golf, nba, nfl, tennis);
	displayConfusionMatrix(cofMatrix4);

	Map<String, Map<String, Double>> cofMatrix5 = run(100, 10, 10, 60, golf, nba, nfl, tennis);
	displayConfusionMatrix(cofMatrix5);
    }

    public static void differentContent() {
	List<News> enter = HtmlUtil.read(".\\News Set\\ENTER");
	List<News> health = HtmlUtil.read(".\\News Set\\HEALTH");
	List<News> politic = HtmlUtil.read(".\\News Set\\POLITICS");
	List<News> sport = HtmlUtil.read(".\\News Set\\SPORT");
	//
	Map<String, Map<String, Double>> cofMatrix = run(100, 10, 10, 20, enter, health, politic, sport);
	displayConfusionMatrix(cofMatrix);

	Map<String, Map<String, Double>> cofMatrix2 = run(100, 10, 10, 30, enter, health, politic, sport);
	displayConfusionMatrix(cofMatrix2);

	Map<String, Map<String, Double>> cofMatrix3 = run(100, 10, 10, 40, enter, health, politic, sport);
	displayConfusionMatrix(cofMatrix3);
	//
	Map<String, Map<String, Double>> cofMatrix4 = run(100, 10, 10, 50, enter, health, politic, sport);
	displayConfusionMatrix(cofMatrix4);
	//
	Map<String, Map<String, Double>> cofMatrix5 = run(100, 10, 10, 60, enter, health, politic, sport);
	displayConfusionMatrix(cofMatrix5);
    }

    public static void main(String[] args) {

	/**
	 * Ýki farklý grup için  differentContent ve similarContent fonksiyonlarý kullanýlabilir.
	 */
	
	//Spor kategorilerine ait dökümanlarý sýnýflandýrýr.
	similarContent();
	
	//4 farklý ana haber türündeki dökümanlarý sýnýflandýr.
	differentContent();
	
	//Program çalýþtýrýldýðýnda ConfisionMatrix'leri konsola basar.

    }

    public static Map<String, Map<String, Double>> run(int iteration, int trainSize, int testSize, int featureSize, List<News> l1,
	    List<News> l2, List<News> l3, List<News> l4) {
	double total = 0;

	Map<String, Map<String, Double>> confusionMatrix = new HashMap<String, Map<String, Double>>();

	for (int i = 0; i < iteration; i++) {
	    Pair pair1 = generateSet(l1, trainSize, testSize);
	    Pair pair2 = generateSet(l2, trainSize, testSize);
	    Pair pair3 = generateSet(l3, trainSize, testSize);
	    Pair pair4 = generateSet(l4, trainSize, testSize);

	    List<Attribute> mostPopular = WordUtil.selectTopAttributes(featureSize, pair1.trainSet, pair2.trainSet, pair3.trainSet,
		    pair4.trainSet);

	    Collections.sort(mostPopular, new Comparator<Attribute>() {

		@Override
		public int compare(Attribute o1, Attribute o2) {
		    return Double.compare((double) o2.getValue(), (double) o1.getValue());
		}
	    });

//	    System.out.println(mostPopular);

	    List<TextSample> trainSamples = WordUtil.readTextSamples(mostPopular, pair1.trainSet, pair2.trainSet, pair3.trainSet,
		    pair4.trainSet);
	    List<TextSample> testSamples = WordUtil
		    .readTextSamples(mostPopular, pair1.testSet, pair2.testSet, pair3.testSet, pair4.testSet);

	    TrainingSet trainingSet = new TrainingSet(mostPopular.toArray(new Attribute[mostPopular.size()]),
		    trainSamples.toArray(new TextSample[trainSamples.size()]));

	    NaiveBayes bayes = new NaiveBayes(trainingSet);

	    double correct = 0;
	    for (TextSample textSample : testSamples) {

		String concept = bayes.classify(textSample);

		if (confusionMatrix.containsKey(textSample.getConcept())) {
		    Map<String, Double> map = confusionMatrix.get(textSample.getConcept());

		    if (map.containsKey(concept))
			map.put(concept, map.get(concept) + 1.);
		    else
			map.put(concept, 1.);
		}
		else {
		    Map<String, Double> map = new HashMap<String, Double>();
		    map.put(concept, 1.);
		    confusionMatrix.put(textSample.getConcept(), map);

		}

		if (textSample.getConcept().equals(concept))
		    correct++;

	    }
	    double rate = (correct / testSamples.size()) * 100;
	    // System.out.println(rate);
	    total += rate;
	}

	System.out.println(total / (double) iteration);
	return confusionMatrix;
    }

}
