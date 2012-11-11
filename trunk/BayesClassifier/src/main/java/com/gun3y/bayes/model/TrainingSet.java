package com.gun3y.bayes.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.google.common.base.Strings;
import com.gun3y.bayes.model.base.BaseInstance;

public class TrainingSet implements Serializable {

	private static final long serialVersionUID = 4247395309973049468L;

	private Map<String, List<Instance>> concepts;
	private String[] attributeNames;
	private Instance[] instances;

	public static void main(String[] args) {

		Random r = new Random(System.currentTimeMillis());

		String[] concepts = { "Sci-Fi", // general
				"Western", // technical
				"Comedy", // sales
				"Romance" // marketing
		};

		Instance[] instances = new Instance[20];

		Attribute[] att;
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
	}

	public TrainingSet(String[] attributeNames, Instance[] instanceSet) {
		super();
		this.attributeNames = attributeNames;
		this.instances = instanceSet;

		initialize();
	}

	private void initialize() {
		concepts = new HashMap<String, List<Instance>>();
		if (instances != null) {
			for (Instance ins : instances) {
				if (ins != null && !Strings.isNullOrEmpty(ins.getConcept())) {

					String conceptName = ins.getConcept();

					if (!concepts.containsKey(conceptName))
						concepts.put(conceptName, new ArrayList<Instance>());

					concepts.get(conceptName).add(ins);
				}
			}
		}

	}

	public Map<String, List<Instance>> getConcepts() {
		return concepts;
	}

	public void setConcepts(Map<String, List<Instance>> concepts) {
		this.concepts = concepts;
	}

	public String[] getAttributeNames() {
		return attributeNames;
	}

	public void setAttributeNames(String[] attributeNames) {
		this.attributeNames = attributeNames;
	}

	public Instance[] getInstanceSet() {
		return instances;
	}

	public void setInstanceSet(Instance[] instanceSet) {
		this.instances = instanceSet;
	}

	public int getInstanceSize() {
		if (instances != null)
			return instances.length;

		return 0;
	}

	public String printConcepts() {
		StringBuilder sb = new StringBuilder();

		if (concepts != null && !concepts.isEmpty()) {
			for (Entry<String, List<Instance>> entry : concepts.entrySet()) {
				sb.append(entry.getKey()).append(":\n");
				if (entry.getValue() != null) {
					int count = 1;
					for (Instance ins : entry.getValue()) {
						sb.append("\t").append(count++).append("-");
						for (String attName : this.attributeNames) {
							sb.append(ins.getAttributeByName(attName)).append(
									"\t");
						}
						sb.append("\n");
					}
				}
			}
		}

		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (this.attributeNames != null && this.attributeNames.length > 0) {
			if (this.instances != null && this.instances.length > 0) {
				for (int i = 0; i < this.instances.length; i++) {
					sb.append((i + 1) + "\t");

					sb.append(instances[i].getConcept()).append("\t");

					for (String attName : this.attributeNames) {
						sb.append(instances[i].getAttributeByName(attName))
								.append("\t");
					}
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}

}
