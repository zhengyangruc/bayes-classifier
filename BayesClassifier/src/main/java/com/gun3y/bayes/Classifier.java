package com.gun3y.bayes;

import com.gun3y.bayes.model.Instance;

public interface Classifier {
	public boolean train();
	public String classify(Instance instance);
}
