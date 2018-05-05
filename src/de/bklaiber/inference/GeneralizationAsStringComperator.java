package de.bklaiber.inference;

import java.util.Collection;
import java.util.Iterator;

import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

/**
 * Compares the string representation of two generalizations.
 * 
 * @author klaiber
 *
 */
public class GeneralizationAsStringComperator extends AbstractGeneralizatonComperator {

	int smallest = 0;

	/**
	 * This method compares the string representation of two generalizations.
	 * 
	 * @param g1
	 *            first generalization to compare
	 * 
	 * @param g2
	 *            second generalization to compare
	 */
	public int compare(Collection<RelationalConditional> g1, Collection<RelationalConditional> g2) {
		StringBuffer stringBufferOfG1 = new StringBuffer();
		StringBuffer stringBufferOfG2 = new StringBuffer();

		for (Iterator<RelationalConditional> iterator1 = g1.iterator(); iterator1.hasNext();) {
			RelationalConditional conditional = iterator1.next();

			if (conditional.getConstraint() != null) {
				String stringOfG1 = conditional.getConstraint().toString();
				stringBufferOfG1.append(stringOfG1);
			}

		}

		for (Iterator<RelationalConditional> iterator2 = g2.iterator(); iterator2.hasNext();) {
			RelationalConditional conditional = iterator2.next();
			if (conditional.getConstraint() != null) {
				String stringOfG2 = conditional.getConstraint().toString();
				stringBufferOfG2.append(stringOfG2);
			}
		}

		if (stringBufferOfG1.length() < stringBufferOfG2.length()) {
			smallest = 1;
		}
		if (stringBufferOfG2.length() < stringBufferOfG1.length()) {
			smallest = 2;
		}
		return smallest;

	}
}
