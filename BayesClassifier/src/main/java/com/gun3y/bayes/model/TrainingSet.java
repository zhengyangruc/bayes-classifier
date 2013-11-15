package com.gun3y.bayes.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Strings;

public class TrainingSet implements Serializable {

	private static final long serialVersionUID = 4247395309973049468L;

	private Map<String, List<Instance>> concepts;
	private Attribute[] attributes;
	private Instance[] instances;

	public TrainingSet(Attribute[] attributes, Instance[] instanceSet) {
		super();
		this.attributes = attributes;
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
						for (Attribute att : this.attributes) {
							sb.append(ins.getAttributeByName(att.getName())).append(
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

		if (this.attributes != null && this.attributes.length > 0) {
			if (this.instances != null && this.instances.length > 0) {
				for (int i = 0; i < this.instances.length; i++) {
					sb.append((i + 1) + "\t");

					sb.append(instances[i].getConcept()).append("\t");

					for (Attribute att : this.attributes) {
						sb.append(instances[i].getAttributeByName(att.getName()))
								.append("\t");
					}
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}

	public Attribute[] getAttributes() {
	    return attributes;
	}

	public void setAttributes(Attribute[] attributes) {
	    this.attributes = attributes;
	}

}
