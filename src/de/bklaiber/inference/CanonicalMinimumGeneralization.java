package de.bklaiber.inference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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

//TODO test, ob generische Konditionale mit der gleichen Wahrscheinlichkeit auch zusammengefasst werden

//TODO reflexives Wissen rausfiltern 

/**
 * This class is used to generalize the classes.
 * 
 * 
 */
public class CanonicalMinimumGeneralization extends AbstractGeneralization {

	@Override
	public Collection<RelationalConditional> generalize(RelationalConditional c,
			Collection<Collection<RelationalConditional>> classifiedClasses) {

		ArrayList<Collection<RelationalConditional>> classifiedClassesList = new ArrayList<>(classifiedClasses);

		Collection<RelationalConditional> generalization = new ArrayList<RelationalConditional>();
		Collection<RelationalConditional> generalizationNegative = new ArrayList<RelationalConditional>();
		Collection<RelationalConditional> generalizationPositive = new ArrayList<RelationalConditional>();

		Comparator<Collection> compareSize = new Comparator<Collection>() {

			@Override
			public int compare(Collection o1, Collection o2) {
				Integer size1 = o1.size();
				Integer size2 = o2.size();
				return size2.compareTo(size1);
			}

		};

		//sorts the classes by size, descendant
		Collections.sort(classifiedClassesList, compareSize);

		//if the size of the biggest class smaller than the sum of the sizes of the other classes than: negative constraint for biggest class with all elements of the other classes
		//else positive constraint 
		//for the smaller classes positive constraints

		Iterator<Collection<RelationalConditional>> iterator = classifiedClassesList.iterator();
		Collection<RelationalConditional> biggestClass = iterator.next();

		int numberOfElements = 0;

		while (iterator.hasNext()) {
			Collection<RelationalConditional> classification = iterator.next();
			numberOfElements = numberOfElements + classification.size();
		}

		if (biggestClass.size() > numberOfElements) {
			generalization = generalizeNegative(c, classifiedClassesList);

		} else {
			generalization = generalizePositive(c, classifiedClasses);
		}

		//generalizationNegative = generalizeNegative(c, classifiedClassesList);
		//generalizationPositive = generalizePositive(c, classifiedClasses);

		return generalization;
	}

	/*
	 * @param c
	 * @param classifiedClasses
	 * @return generalization
	 */
	private Collection<RelationalConditional> generalizePositive(RelationalConditional c,
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

	/*
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
	private Collection<RelationalConditional> generalizeNegative(RelationalConditional c,
			ArrayList<Collection<RelationalConditional>> classifiedClassesList) {

		//TODO ordentlich umschreiben

		Collection<Atom<RelationalAtom>> atomsOfQuery = getAtomsOfQuery(c);
		Collection<RelationalConditional> generalization = new ArrayList<RelationalConditional>();

		Iterator<Collection<RelationalConditional>> iterator = classifiedClassesList.iterator();
		Collection<RelationalConditional> biggestClass = iterator.next();
		Collection<Collection<Atom<RelationalAtom>>> atomsOfClass = new ArrayList<Collection<Atom<RelationalAtom>>>();
		Collection<Collection<Atom<RelationalAtom>>> atomsOfOtherClass = new ArrayList<Collection<Atom<RelationalAtom>>>();

		RelationalConditional generalizationOfClass = null;

		Fraction probability = getProbabilitiesOfClass(biggestClass);

		while (iterator.hasNext()) {
			Collection<RelationalConditional> classification = iterator.next();
			atomsOfClass.addAll(getAtomOfClass(classification));

		}

		Formula<AtomicConstraint> constraintOfClass = generateNegativeConstraint(atomsOfClass, atomsOfQuery);
		generalizationOfClass = generateConditional(c, constraintOfClass, probability);
		generalization.add(generalizationOfClass);

		iterator = classifiedClassesList.iterator();
		iterator.next();
		while (iterator.hasNext()) {
			Collection<RelationalConditional> classification = iterator.next();
			atomsOfOtherClass.addAll(getAtomOfClass(classification));
			Fraction probOfOtherClass = getProbabilitiesOfClass(classification);
			Formula<AtomicConstraint> constraintOfOtherClass = generateConstraint(atomsOfOtherClass, atomsOfQuery);
			generalizationOfClass = generateConditional(c, constraintOfOtherClass, probOfOtherClass);
			generalization.add(generalizationOfClass);

		}

		return generalization;
	}

	/*
	 * 
	 * @param atomsOfClass
	 * @param atomsOfQuery
	 * @return constraint
	 */
	private Formula<AtomicConstraint> generateConstraint(Collection<Collection<Atom<RelationalAtom>>> atomsOfClass,
			Collection<Atom<RelationalAtom>> atomsOfQuery) {

		Collection<Formula<AtomicConstraint>> argsOfClass = new ArrayList<Formula<AtomicConstraint>>();
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
						constraint = generateDisjunctionConstraint(argsOfClass);
					}

				}

			}

		}

		return constraint;
	}

	/*
	 * Creates a conjunction of InequalityConstraints
	 */
	private Formula<AtomicConstraint> generateNegativeConstraint(
			Collection<Collection<Atom<RelationalAtom>>> atomsOfClass, Collection<Atom<RelationalAtom>> atomsOfQuery) {

		Collection<Formula<AtomicConstraint>> negativeArgsOfClass = new ArrayList<Formula<AtomicConstraint>>();
		ArrayList<Formula<AtomicConstraint>> elementsOfNegativeConstraintsOfClass = new ArrayList<Formula<AtomicConstraint>>();
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

					//the predicate of the query and the predicate of the ground instance are equal create 
					//inequalityconstraints and a conjunction of the inequalityconstraints
					if (((RelationalAtom) atom).getPredicate().equals(((RelationalAtom) atomOfQuery).getPredicate())) {

						Term[] argsOfQueryAtom = ((RelationalAtom) atomOfQuery).getArguments();
						Term[] argsOfConditional = ((RelationalAtom) atom).getArguments();

						elementsOfNegativeConstraintsOfClass = generateElementsOfNegativeConstraint(argsOfConditional,
								argsOfQueryAtom);
						negativeArgsOfClass.addAll(elementsOfNegativeConstraintsOfClass);
						constraint = generateDisjunctionConstraint(negativeArgsOfClass);
						//constraint = generateConjunctionConstraint(negativeArgsOfClass);

					}

				}

			}

		}

		return constraint;

	}

	/*
	 * This method generates a conjunction of Constraints
	 */
	private Formula<AtomicConstraint> generateConjunctionConstraint(Collection<Formula<AtomicConstraint>> argsOfClass) {

		Formula<AtomicConstraint> constraint = null;

		Iterator<Formula<AtomicConstraint>> equiCons = argsOfClass.iterator();
		constraint = equiCons.next();
		if (!(argsOfClass.size() > 1)) {

			return constraint;

		}

		while (equiCons.hasNext()) {
			Formula<AtomicConstraint> cons = equiCons.next();
			constraint = constraint.and(cons);

		}
		return constraint;

	}

	/*
	 * Creates a disjunction of Constraints.
	 */
	private Formula<AtomicConstraint> generateDisjunctionConstraint(Collection<Formula<AtomicConstraint>> argsOfClass) {

		Formula<AtomicConstraint> constraint = null;

		Iterator<Formula<AtomicConstraint>> cons = argsOfClass.iterator();
		constraint = cons.next();
		if (!(argsOfClass.size() > 1)) {

			return constraint;

		}

		while (cons.hasNext()) {
			Formula<AtomicConstraint> elementOfConstraint = cons.next();

			constraint = constraint.or(elementOfConstraint);

		}
		return constraint;
	}

	/*
	 * Creates an InequalityConstraint using the variables of the query and constants of the generated conditionals
	 */
	private ArrayList<InequalityConstraint> generateElementsOfNegativeConstraint2(Term[] argsOfCond,
			Term[] argsOfQueryAtom) {
		ArrayList<InequalityConstraint> listOfConstraints = new ArrayList<InequalityConstraint>();

		for (int i = 0; i < argsOfCond.length; i++) {
			for (int j = 0; j < argsOfQueryAtom.length; j++) {

				Variable var = new Variable(argsOfQueryAtom[j].toString(), argsOfQueryAtom[j].getType());
				Constant cons = new Constant(argsOfCond[i].toString(), argsOfCond[i].getType());
				InequalityConstraint inequalConstraint = new InequalityConstraint(var, cons);
				listOfConstraints.add(inequalConstraint);

			}

		}

		return listOfConstraints;

	}
	//TODO nullstelliges Prädikat abfangen

	private ArrayList<Formula<AtomicConstraint>> generateElementsOfNegativeConstraint(Term[] argsOfCond,
			Term[] argsOfQueryAtom) {
		ArrayList<Formula<AtomicConstraint>> listOfConstraints = new ArrayList<Formula<AtomicConstraint>>();
		Collection<Formula<AtomicConstraint>> listOfInequalityConstraints = new ArrayList<Formula<AtomicConstraint>>();

		/*
		 * Creates an InequalityConstraint using the variables of the query and constants of the generated conditionals
		 * if the artity of the predicate is 1 then create an InequalityConstraint, else 
		 */

		for (int i = 0; i < argsOfCond.length; i++) {
			for (int j = 0; j < argsOfQueryAtom.length; j++) {

				Variable var = new Variable(argsOfQueryAtom[j].toString(), argsOfQueryAtom[j].getType());
				Constant cons = new Constant(argsOfCond[i].toString(), argsOfCond[i].getType());
				if (argsOfCond.length == 1) {
					InequalityConstraint atomicConstraint = new InequalityConstraint(var, cons);
					listOfConstraints.add(atomicConstraint);

				}
				if (argsOfCond.length > 1) {
					InequalityConstraint atomicConstraint = new InequalityConstraint(var, cons);
					listOfInequalityConstraints.add(atomicConstraint);
				}
			}

		}
		for (int i = 0; i < listOfInequalityConstraints.size(); i++) {
			Formula<AtomicConstraint> conjunction = generateConjunctionConstraint(listOfInequalityConstraints);
			listOfConstraints.add(conjunction);
		}

		return listOfConstraints;
	}

	/*
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

	/*
	 * Creates an EqualityConstraint using the variables of the query and constants of the generated conditionals.
	 */
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

	/*
	 * Generates a conditional out of the relationalConditonal of the query, the generated connstraint and 
	 * the probability.
	 * 
	 * @param constraintOfClass
	 * @param consequence
	 * @param antecedence
	 * @param probability
	 * @return
	 */
	private RelationalConditional generateConditional(RelationalConditional c,
			Formula<AtomicConstraint> constraintOfClass, Fraction probability) {

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

	/*
	 * Get the atoms for each conditional of a class.
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

	/*
	 * Returns the atoms of the conditional of the query.
	 * 
	 * @param c
	 * @return atomsOfConditional
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
