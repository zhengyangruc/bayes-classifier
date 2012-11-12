package com.gun3y.bayes.test;

import java.util.List;

import com.gun3y.bayes.NaiveBayes;
import com.gun3y.bayes.model.TextSample;
import com.gun3y.bayes.model.TrainingSet;
import com.gun3y.bayes.utils.FileUtil;
import com.gun3y.bayes.utils.WordUtil;

public class BayesTest {

	public static void main(String[] args) {
	
		FileUtil.ROOT_DIR = "C:\\Users\\Keysersoze\\Desktop\\Training Data Sets\\";
		
		List<TextSample> samples = WordUtil.readTextSamples();

		TrainingSet trainingSet = new TrainingSet(WordUtil.ATTRIBUTE_NAMES, samples.toArray(new TextSample[samples.size()]));
		
		System.out.println(trainingSet);
		
		NaiveBayes bayes = new NaiveBayes(trainingSet);
		
		FileUtil.ROOT_DIR = "C:\\Users\\Keysersoze\\Desktop\\Test Data Sets\\";
		List<TextSample> testSamples = WordUtil.readTextSamples();
		
		
		for (TextSample textSample : testSamples) {
			System.out.println(textSample.getConcept() + " - "+ bayes.classify(textSample));
		}
	}

}
