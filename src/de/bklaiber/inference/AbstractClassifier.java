package de.bklaiber.inference;

import java.util.ArrayList;
import java.util.Collection;

import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

public abstract class AbstractClassifier implements Classifier {

	public Collection<Collection<RelationalConditional>> classify(
			Collection<RelationalConditional> probabilisticGroundInstances) {

		if (isTransitive()) {
			return classifyTransitively(probabilisticGroundInstances);
		}

		return null;
	}

	public Collection<Collection<RelationalConditional>> classifyTransitively(
			Collection<RelationalConditional> probabilisticGroundInstances) {

		ArrayList<Collection<RelationalConditional>> equivalenceClasses = new ArrayList<Collection<RelationalConditional>>();

		for (RelationalConditional relationalConditionalA : probabilisticGroundInstances) {
			boolean hasBeenClassified = false;

			for (Collection<RelationalConditional> collection : equivalenceClasses) {

				RelationalConditional relationalConditionalB = collection.iterator().next();

				if (isEquivalent(relationalConditionalA, relationalConditionalB, probabilisticGroundInstances)) {
					collection.add(relationalConditionalA);
					hasBeenClassified = true;
				}

			}
			if (!hasBeenClassified) {
				ArrayList<RelationalConditional> newClass = new ArrayList<RelationalConditional>();
				newClass.add(relationalConditionalA);
				equivalenceClasses.add(newClass);
			}

		}

		return equivalenceClasses;
	}

	public boolean isTransitive() {
		return true;
	}
}
