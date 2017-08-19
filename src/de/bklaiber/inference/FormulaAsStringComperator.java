package de.bklaiber.inference;

import edu.cs.ai.log4KR.logical.syntax.Formula;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.AtomicConstraint;

/**
 * Compares the string representation of formulas by size.
 * 
 * @author klaiber
 *
 */
public class FormulaAsStringComperator extends AbstractFormulaComperator {
	@Override
	public int compare(Formula<AtomicConstraint> f1, Formula<AtomicConstraint> f2) {
		int smallest = 0;
		String stringOfOfF1 = f1.toString();
		String stringOfOfF2 = f2.toString();

		if (stringOfOfF1.length() > stringOfOfF2.length()) {
			smallest = 1;
		}
		if (stringOfOfF2.length() > stringOfOfF1.length()) {
			smallest = 2;
		}
		return smallest;
	}
}
