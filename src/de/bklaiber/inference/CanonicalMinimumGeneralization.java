package de.bklaiber.inference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import edu.cs.ai.log4KR.logical.syntax.Atom;
import edu.cs.ai.log4KR.logical.syntax.Formula;
import edu.cs.ai.log4KR.logical.syntax.Tautology;
import edu.cs.ai.log4KR.math.types.Fraction;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.RelationalAtom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.AtomicConstraint;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.EqualityConstraint;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.InequalityConstraint;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Constant;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Term;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Variable;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalFact;

/**
 * This class is used to generalize the classes
 * 
 */
public class CanonicalMinimumGeneralization extends AbstractGeneralization {

	/**
	 * @param c
	 * @param classifiedClasses
	 * @return generalization
	 */
	public Collection<RelationalConditional> generalize(RelationalConditional c,
			Collection<Collection<RelationalConditional>> classifiedClasses) {

		Collection<Atom<RelationalAtom>> atomsOfQuery = getAtomsOfQuery(c);
		Collection<RelationalConditional> generalization = new ArrayList<RelationalConditional>();

		for (Iterator<Collection<RelationalConditional>> iterator = classifiedClasses.iterator(); iterator.hasNext();) {
			Collection<RelationalConditional> classification = iterator.next();

			Collection<Collection<Atom<RelationalAtom>>> atomsOfClass = getAtomOfClass(classification);
			Fraction probability = getProbabilitiesOfClass(classification);
			Formula<AtomicConstraint> constraintOfClass = generateConstraint(atomsOfClass, atomsOfQuery);
			RelationalConditional generalizationOfClass = generateConditional(c, constraintOfClass, probability);
			generalization.add(generalizationOfClass);

		}

		return generalization;
	}

	/**
	 * In this method the classes are sorted by size, biggest class first. In
	 * the biggest class there are inequality constraints for all of the
	 * elements of the other classes, when the number of the elements of the
	 * other classes is smaller than the size of the biggest class. For the
	 * elements of the smaller classes there will be equality constraints
	 * 
	 * @param atomsOfClass
	 * @param atomsOfQuery
	 * @return
	 */
	public Collection<RelationalConditional> generalizeNegative(RelationalConditional c,
			Collection<Collection<RelationalConditional>> classifiedClasses) {

		Collection<Atom<RelationalAtom>> atomsOfQuery = getAtomsOfQuery(c);
		Collection<RelationalConditional> generalization = new ArrayList<RelationalConditional>();

		//if the size of the biggest class smaller than the sum of the sizes of the other classes than: negative constraint for biggest class with all elements of the other classes
		//else positive constraint 
		//for the smaller classses positive constraints

		for (Iterator<Collection<RelationalConditional>> iterator = classifiedClasses.iterator(); iterator.hasNext();) {
			Collection<RelationalConditional> classification = iterator.next();

			Collection<Collection<Atom<RelationalAtom>>> atomsOfClass = getAtomOfClass(classification);
			Fraction probability = getProbabilitiesOfClass(classification);
			Formula<AtomicConstraint> constraintOfClass = generateMixedConstraint(atomsOfClass, atomsOfQuery);
			RelationalConditional generalizationOfClass = generateConditional(c, constraintOfClass, probability);
			generalization.add(generalizationOfClass);

		}

		return generalization;
	}

	/*
	 * Creates a
	 */
	private Formula<AtomicConstraint> generateMixedConstraint(Collection<Collection<Atom<RelationalAtom>>> atomsOfClass,
			Collection<Atom<RelationalAtom>> atomsOfQuery) {

		Collection<InequalityConstraint> negativeArgsOfClass = new ArrayList<InequalityConstraint>();
		Collection<EqualityConstraint> positiveArgsOfClass = new ArrayList<EqualityConstraint>();
		ArrayList<InequalityConstraint> elementsOfNegativeConstraintsOfClass = new ArrayList<InequalityConstraint>();
		ArrayList<EqualityConstraint> elementsOfPositiveConstraintsOfClass = new ArrayList<EqualityConstraint>();
		Formula<AtomicConstraint> constraint = null;

		//get the arguments for each conditional
		//take each collection for the conditional
		for (Iterator<Collection<Atom<RelationalAtom>>> atoms = atomsOfClass.iterator(); atoms.hasNext();) {
			Collection<Atom<RelationalAtom>> atomOfCondtional = atoms.next();
			//take each atom of each conditional
			for (Iterator<Atom<RelationalAtom>> element = atomOfCondtional.iterator(); element.hasNext();) {
				Atom<RelationalAtom> atom = (Atom<RelationalAtom>) element.next();
				//get the arguments of each atom of the conditional
				for (Iterator<Atom<RelationalAtom>> iteratorOfQuery = atomsOfQuery.iterator(); iteratorOfQuery
						.hasNext();) {
					Atom<RelationalAtom> atomOfQuery = (Atom<RelationalAtom>) iteratorOfQuery.next();

					if (((RelationalAtom) atom).getPredicate().equals(((RelationalAtom) atomOfQuery).getPredicate())) {

						Term[] argsOfQueryAtom = ((RelationalAtom) atomOfQuery).getArguments();
						Term[] argsOfConditional = ((RelationalAtom) atom).getArguments();
						elementsOfNegativeConstraintsOfClass = generateElementsOfNegativeConstraint(argsOfConditional,
								argsOfQueryAtom);
						negativeArgsOfClass.addAll(elementsOfNegativeConstraintsOfClass);
						elementsOfPositiveConstraintsOfClass = generateElementsOfConstraint(argsOfConditional,
								argsOfQueryAtom);
						positiveArgsOfClass.addAll(elementsOfPositiveConstraintsOfClass);
						constraint = generateMixedConstraintOfClass(negativeArgsOfClass, positiveArgsOfClass);
					}

				}

			}

		}

		return constraint;

	}

	private Formula<AtomicConstraint> generateMixedConstraintOfClass(Collection<InequalityConstraint> argsOfClass,
			Collection<EqualityConstraint> positiveArgsOfClass) {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<InequalityConstraint> generateElementsOfNegativeConstraint(Term[] argsOfConditional,
			Term[] argsOfQueryAtom) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param classification
	 * @return the average probability of the class
	 */
	private Fraction getProbabilitiesOfClass(Collection<RelationalConditional> classification) {

		Fraction sum = new Fraction(0);

		for (Iterator<RelationalConditional> condOfClass = classification.iterator(); condOfClass.hasNext();) {
			RelationalConditional relationalConditional = (RelationalConditional) condOfClass.next();

			Fraction probabilityOfCond = relationalConditional.getProbability();
			sum = Fraction.addition(sum, probabilityOfCond);

		}

		return Fraction.division(sum, new Fraction(classification.size()));
	}

	/**
	 * 
	 * @param atomsOfClass
	 * @param atomsOfQuery
	 * @return
	 */
	private Formula<AtomicConstraint> generateConstraint(Collection<Collection<Atom<RelationalAtom>>> atomsOfClass,
			Collection<Atom<RelationalAtom>> atomsOfQuery) {

		Collection<EqualityConstraint> argsOfClass = new ArrayList<EqualityConstraint>();
		ArrayList<EqualityConstraint> elementsOfConstraintsOfClass = new ArrayList<EqualityConstraint>();
		Formula<AtomicConstraint> constraint = null;

		//get the arguments for each conditional
		//take each collection for the conditional
		for (Iterator<Collection<Atom<RelationalAtom>>> atoms = atomsOfClass.iterator(); atoms.hasNext();) {
			Collection<Atom<RelationalAtom>> atomOfCondtional = atoms.next();
			//take each atom of each conditional
			for (Iterator<Atom<RelationalAtom>> element = atomOfCondtional.iterator(); element.hasNext();) {
				Atom<RelationalAtom> atom = (Atom<RelationalAtom>) element.next();
				//get the arguments of each atom of the conditional
				for (Iterator<Atom<RelationalAtom>> iteratorOfQuery = atomsOfQuery.iterator(); iteratorOfQuery
						.hasNext();) {
					Atom<RelationalAtom> atomOfQuery = (Atom<RelationalAtom>) iteratorOfQuery.next();

					if (((RelationalAtom) atom).getPredicate().equals(((RelationalAtom) atomOfQuery).getPredicate())) {

						Term[] argsOfQueryAtom = ((RelationalAtom) atomOfQuery).getArguments();
						Term[] argsOfConditional = ((RelationalAtom) atom).getArguments();
						elementsOfConstraintsOfClass = generateElementsOfConstraint(argsOfConditional, argsOfQueryAtom);
						argsOfClass.addAll(elementsOfConstraintsOfClass);
						constraint = generateConstraintOfClass(argsOfClass);
					}

				}

			}

		}

		return constraint;
	}

	private Formula<AtomicConstraint> generateConstraintOfClass(Collection<EqualityConstraint> argsOfClass) {

		Formula<AtomicConstraint> constraint = null;

		Iterator<EqualityConstraint> equiCons = argsOfClass.iterator();
		constraint = equiCons.next();
		if (!(argsOfClass.size() > 1)) {

			return constraint;

		}

		while (equiCons.hasNext()) {
			EqualityConstraint equalityConstraint = equiCons.next();

			constraint = constraint.or(equalityConstraint.getInterpretable());

		}
		return constraint;
	}

	private ArrayList<EqualityConstraint> generateElementsOfConstraint(Term[] argsOfCond, Term[] argsOfQueryAtom) {

		ArrayList<EqualityConstraint> listOfConstraints = new ArrayList<EqualityConstraint>();

		for (int i = 0; i < argsOfCond.length; i++) {
			for (int j = 0; j < argsOfQueryAtom.length; j++) {

				Variable var = new Variable(argsOfQueryAtom[j].toString(), argsOfQueryAtom[j].getType());
				Constant cons = new Constant(argsOfCond[i].toString(), argsOfCond[i].getType());
				EqualityConstraint equalConstraint = new EqualityConstraint(var, cons);
				listOfConstraints.add(equalConstraint);

			}

		}

		return listOfConstraints;
	}

	/**
	 * 
	 * @param constraintOfClass
	 * @param consequence
	 * @param antecedence
	 * @param probability
	 * @return
	 */
	private RelationalConditional generateConditional(RelationalConditional c,
			Formula<AtomicConstraint> constraintOfClass, Fraction probability) {

		//RelationalConditional conditionalOfClass;
		Formula<RelationalAtom> consequence = c.getConsequence();

		if (c instanceof RelationalFact) {

			RelationalFact conditionalOfClass = new RelationalFact(consequence, probability, constraintOfClass);
			return conditionalOfClass;

		} else {
			Formula<RelationalAtom> antecedence = c.getAntecedence();
			RelationalConditional conditionalOfClass = new RelationalConditional(consequence, antecedence, probability,
					constraintOfClass);
			return conditionalOfClass;

		} //end else

	} //end for

	/**
	 * Get the atoms for each conditional of a class
	 * 
	 * @param classifiedClasses
	 * @return
	 */
	private Collection<Collection<Atom<RelationalAtom>>> getAtomOfClass(
			Collection<RelationalConditional> classifiedClasses) {

		//it has to be a collection of collections of atoms because to create the constraint each variable of the query has to compared 
		//with its grounded instance for each conditional of the class
		Collection<Collection<Atom<RelationalAtom>>> atomsOfClass = new ArrayList<Collection<Atom<RelationalAtom>>>();

		//take each conditional in the equivalence class, get the relational atoms of the class
		for (Iterator<RelationalConditional> element = classifiedClasses.iterator(); element.hasNext();) {
			RelationalConditional conditional = (RelationalConditional) element.next();
			Collection<Atom<RelationalAtom>> atomsOfElement = new ArrayList<Atom<RelationalAtom>>();

			//if the relational conditional is a fact, look at the consequence, else look at the antecedence
			if (conditional.getAntecedence() instanceof Tautology<?>) {

				atomsOfElement = conditional.getConsequence().getAtoms();
				atomsOfClass.add(atomsOfElement);

			} else {

				atomsOfElement = conditional.getAntecedence().getAtoms();
				atomsOfClass.add(atomsOfElement);
			}
		}

		return atomsOfClass;
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	private Collection<Atom<RelationalAtom>> getAtomsOfQuery(RelationalConditional c) {

		Collection<Atom<RelationalAtom>> atomsOfConditional = new ArrayList<Atom<RelationalAtom>>();

		if (c instanceof RelationalFact) {

			atomsOfConditional = c.getConsequence().getAtoms();

		} else {

			atomsOfConditional = c.getAntecedence().getAtoms();

		} //end else

		return atomsOfConditional;
	}

}//end generalize
