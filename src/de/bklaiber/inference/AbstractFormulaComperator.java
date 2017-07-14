package de.bklaiber.inference;

import java.util.Comparator;

import edu.cs.ai.log4KR.logical.syntax.Formula;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.AtomicConstraint;

public abstract class AbstractFormulaComperator implements Comparator<Formula<AtomicConstraint>> {

	@Override
	public int compare(Formula<AtomicConstraint> f1, Formula<AtomicConstraint> f2) {
		// TODO Auto-generated method stub
		return 0;
	}

}
