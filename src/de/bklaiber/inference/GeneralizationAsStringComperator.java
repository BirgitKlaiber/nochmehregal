package de.bklaiber.inference;

import java.util.Collection;
import java.util.Iterator;

import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

public class GeneralizationAsStringComperator extends AbstractGeneralizatonComperator {

	int smallest = 0;

	public int compare(Collection<RelationalConditional> g1, Collection<RelationalConditional> g2) {
		String stringOfG1 = null;
		String stringOfG2 = null;

		for (Iterator<RelationalConditional> iterator1 = g1.iterator(); iterator1.hasNext();) {
			RelationalConditional conditional = iterator1.next();

			stringOfG1 = conditional.getConstraint().toString();

		}

		for (Iterator<RelationalConditional> iterator2 = g2.iterator(); iterator2.hasNext();) {
			RelationalConditional conditional = iterator2.next();

			stringOfG2 = conditional.getConstraint().toString();

		}

		if (stringOfG1.length() < stringOfG2.length()) {
			smallest = 1;
		}
		if (stringOfG2.length() < stringOfG1.length()) {
			smallest = 2;
		}
		return smallest;

	}
}
