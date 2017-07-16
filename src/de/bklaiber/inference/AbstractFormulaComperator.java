package de.bklaiber.inference;

import java.util.Collection;
import java.util.Comparator;

import edu.cs.ai.log4KR.logical.syntax.Atom;
import edu.cs.ai.log4KR.logical.syntax.Formula;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.AtomicConstraint;

public abstract class AbstractFormulaComperator implements Comparator<Formula<AtomicConstraint>> {

	@Override
	public int compare(Formula<AtomicConstraint> f1, Formula<AtomicConstraint> f2) {
		int smallest = 0;
		Collection<Atom<AtomicConstraint>> atomsOfF1 = f1.getAtoms();
		Collection<Atom<AtomicConstraint>> atomsOfF2 = f2.getAtoms();
		if (atomsOfF1.size() > atomsOfF2.size()) {
			smallest = 1;
		}
		if (atomsOfF2.size() > atomsOfF1.size()) {
			smallest = 2;
		}
		return smallest;
	}

}
