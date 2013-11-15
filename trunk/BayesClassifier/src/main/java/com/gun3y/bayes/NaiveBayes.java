package com.gun3y.bayes;

import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.google.common.base.Strings;
import com.gun3y.bayes.model.Attribute;
import com.gun3y.bayes.model.Instance;
import com.gun3y.bayes.model.TrainingSet;

public class NaiveBayes implements Classifier {

	private TrainingSet trainingSet;

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

				if (accP > bestProbality) {
					bestProbality = accP;
					bestConcept = entry.getKey();
				}
			}
		}

		return bestConcept;
	}

	private double getProbability(String conceptName, Instance testInstance) {
		List<Instance> subConcepts = trainingSet.getConcepts().get(conceptName);

		double prob = (double) subConcepts.size()
				/ trainingSet.getInstanceSize();

		for (Attribute att : trainingSet.getAttributes()) {
			double[] values = getValuesFromAttibute(att.getName(), subConcepts);

			prob *= calcGaussianProabality((Double) testInstance
					.getAttributeByName(att.getName()).getValue(), values);
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

		double result = Math.exp((-0.5) * Math.pow((num - mean) / std, 2))
				/ Math.sqrt(2 * Math.PI * var);
		
		if(Double.isNaN(result))
			result = Double.MIN_VALUE;

		return result;

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
