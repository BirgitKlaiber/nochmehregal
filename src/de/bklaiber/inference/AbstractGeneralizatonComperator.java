package de.bklaiber.inference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import edu.cs.ai.log4KR.logical.syntax.Atom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.AtomicConstraint;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

/**
 * This class compares two generalizations by the size of the generalized
 * classes
 * 
 * @author klaiber
 *
 */
public abstract class AbstractGeneralizatonComperator implements Comparator<Collection<RelationalConditional>> {

	int smallest = 0;

	public int compare(Collection<RelationalConditional> g1, Collection<RelationalConditional> g2) {
		Collection<Atom<AtomicConstraint>> atomsOfG1 = new ArrayList<Atom<AtomicConstraint>>();
		Collection<Atom<AtomicConstraint>> atomsOfG2 = new ArrayList<Atom<AtomicConstraint>>();

		for (Iterator<RelationalConditional> iterator1 = g1.iterator(); iterator1.hasNext();) {
			RelationalConditional conditional = iterator1.next();

			Collection<Atom<AtomicConstraint>> atoms = conditional.getConstraint().getAtoms();
			atomsOfG1.addAll(atoms);

		}

		for (Iterator<RelationalConditional> iterator2 = g2.iterator(); iterator2.hasNext();) {
			RelationalConditional conditional = iterator2.next();

			Collection<Atom<AtomicConstraint>> atoms = conditional.getConstraint().getAtoms();
			atomsOfG2.addAll(atoms);

		}

		if (atomsOfG1.size() < atomsOfG2.size()) {
			smallest = 1;
		}
		if (atomsOfG2.size() < atomsOfG1.size()) {
			smallest = 2;
		}
		return smallest;

	}
}
