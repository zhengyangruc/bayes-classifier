package com.gun3y.bayes;

import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.google.common.base.Strings;
import com.gun3y.bayes.model.Attribute;
import com.gun3y.bayes.model.DoubleAttribute;
import com.gun3y.bayes.model.Instance;
import com.gun3y.bayes.model.TrainingSet;
import com.gun3y.bayes.model.base.BaseInstance;

public class NaiveBayes implements Classifier {

	private TrainingSet trainingSet;

	public static void main(String[] args) {
		Random r = new Random(System.currentTimeMillis());

		String[] concepts = { "Sci-Fi", // general
				"Western", // technical
				"Comedy", // sales
				"Romance" // marketing
		};

		Instance[] instances = new Instance[20];

		Attribute[] att = null;
		for (int i = 0; i < instances.length; i++) {

			att = new Attribute[4];

			for (int j = 0; j < att.length; j++) {
				att[j] = new DoubleAttribute("Att" + j, r.nextGaussian());
			}

			instances[i] = new BaseInstance(
					concepts[r.nextInt(concepts.length)], att);
		}

		TrainingSet trainingSet = new TrainingSet(new String[] { "Att0",
				"Att1", "Att2", "Att3" }, instances);

		System.out.println(trainingSet);

		System.out.println(trainingSet.printConcepts());

		NaiveBayes bayes = new NaiveBayes(trainingSet);

		
		System.out.println(bayes.classify(new BaseInstance(att)));
	}

	public NaiveBayes(TrainingSet trainingSet) {
		super();
		this.trainingSet = trainingSet;
	}

	@Override
	public boolean train() {

		return false;
	}

	@Override
	public String classify(Instance instance) {

		double bestProbality = 0.0;
		String bestConcept = "";

		if (trainingSet != null) {
			for (Entry<String, List<Instance>> entry : trainingSet
					.getConcepts().entrySet()) {
				double accP = getProbability(entry.getKey(), instance);

				if (accP >= bestProbality) {
					bestProbality = accP;
					bestConcept = entry.getKey();
				}
			}
		}
		
		return bestConcept;
	}

	private double getProbability(String conceptName, Instance testInstance) {
		List<Instance> subConcepts = trainingSet.getConcepts().get(conceptName);

		double prob = (double) subConcepts.size() / trainingSet.getInstanceSize();

		for (String attName : trainingSet.getAttributeNames()) {
			double[] values = getValuesFromAttibute(attName, subConcepts);

			prob *= calcGaussianProabality((Double) testInstance
					.getAttributeByName(attName).getValue(), values);
			// function.
		}

		return prob;
	}

	private double calcGaussianProabality(double num, double[] list) {
		SummaryStatistics statistics = new SummaryStatistics();
		for (double d : list) {
			statistics.addValue(d);
		}

		double mean = statistics.getMean();
		double std = statistics.getStandardDeviation();
		double var = statistics.getVariance();

		return Math.exp((-0.5) * Math.pow((num - mean) / std, 2))
				/ Math.sqrt(2 * Math.PI * var);

	}

	public double[] getValuesFromAttibute(String attName, List<Instance> ins) {
		if (ins != null && !Strings.isNullOrEmpty(attName)) {
			double[] vec = new double[ins.size()];
			for (int i = 0; i < vec.length; i++) {
				vec[i] = (Double) ins.get(i).getAttributeByName(attName)
						.getValue();
			}
			return vec;
		}

		return new double[0];
	}

	public TrainingSet getTrainingSet() {
		return trainingSet;
	}

	public void setTrainingSet(TrainingSet trainingSet) {
		this.trainingSet = trainingSet;
	}

}
