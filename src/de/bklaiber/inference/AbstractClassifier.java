package de.bklaiber.inference;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

/*
 * This class is used to...
 * 
 * @author klaiber
 * 
 */
public abstract class AbstractClassifier implements Classifier {

	Properties properties = null;

	/*
	 * 
	 */
	public AbstractClassifier() {
		if (!isConfigurable()) {
			return;
		}

		properties = new Properties();
		try {
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream("ressources/config/" + this.getClass().getName() + ".properties"));
			properties.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Collection<Collection<RelationalConditional>> classify(
			Collection<RelationalConditional> probabilisticGroundInstances) {

		if (isTransitive()) {
			return classifyTransitively(probabilisticGroundInstances);
		}

		throw new UnsupportedOperationException("Classifier not transitive and not implementing classify");

	}

	/*
	 * 
	 */
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

	@Override
	public boolean isEquivalent(RelationalConditional probGroundInsA, RelationalConditional probGroundInsB,
			Collection<RelationalConditional> probabilisticGroundInstances) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isConfigurable() {
		return false;
	}
}
