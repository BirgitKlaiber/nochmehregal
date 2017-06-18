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

	@SuppressWarnings("unchecked")
	private Formula<AtomicConstraint> generateConstraintOfClass(Collection<EqualityConstraint> argsOfClass) {

		Formula<AtomicConstraint> constraint = null;

		Iterator<EqualityConstraint> equiCons = argsOfClass.iterator();
		constraint = equiCons.next();
		if (!(argsOfClass.size() > 1)) {

			return (Formula<AtomicConstraint>) equiCons;

		}

		while (equiCons.hasNext()) {
			EqualityConstraint equalityConstraint = equiCons.next();

			constraint = constraint.or((Formula<AtomicConstraint>) equalityConstraint);

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

	/*
	
	@Override
	public Collection<RelationalConditional> generalize1(RelationalConditional c,
			Collection<Collection<RelationalConditional>> classifiedClasses) {
	
		Collection<RelationalConditional> generalizedClasses = new ArrayList<RelationalConditional>();
	
		Formula<RelationalAtom> consequence = c.getConsequence();
		Formula<RelationalAtom> antecedence = c.getAntecedence();
		Formula<AtomicConstraint> constraint = null;
		Fraction probability = null;
	
		Collection<Atom<RelationalAtom>> atomsOfConditional = new ArrayList<Atom<RelationalAtom>>();
	
		if (classifiedClasses instanceof RelationalFact) {
	
			atomsOfConditional = c.getConsequence().getAtoms();
	
		} else {
	
			atomsOfConditional = c.getAntecedence().getAtoms();
	
		} //end else
	
		//((A(a) v B(b))[0,2]; (A(b) v B(c))[0,2]); (A(c) v B(d)[0,8]) Praedikat: A und Terme a und Praedikat B und Term b 
		//(A(X) v B(Y)[0,2]<(X=a und Y=b) oder (X=b und Y=c)>); (A(X) v B(Y)[0,8]<X=c und Y=d>);
	
		//for each equivalence class in classifiedClasses create a relational conditional with constraint
		for (Iterator<Collection<RelationalConditional>> iterator = classifiedClasses.iterator(); iterator.hasNext();) {
			Collection<RelationalConditional> equivalenceClass = iterator.next();
	
			Collection<Collection<Atom<RelationalAtom>>> atomsOfClass = new ArrayList<Collection<Atom<RelationalAtom>>>();
	
			//take each conditional in the equivalence class, get the relational atoms of the class
			for (Iterator<RelationalConditional> elements = equivalenceClass.iterator(); elements.hasNext();) {
				RelationalConditional element = (RelationalConditional) elements.next();
				Collection<Atom<RelationalAtom>> atomsOfElement = new ArrayList<Atom<RelationalAtom>>();
				probability = element.getProbability();//TODO Mittelwert berechnen
	
				//if the relational conditional is a fact, look at the consequence, else look at the antecedence
				if (classifiedClasses instanceof RelationalFact) {
	
					atomsOfElement = element.getConsequence().getAtoms();
					atomsOfClass.add(atomsOfElement);
	
				} else {
	
					atomsOfElement = element.getAntecedence().getAtoms();
					atomsOfClass.add(atomsOfElement);
				} //end else
			} //end for
	
			Term[] argsOfQuery = null;
			ArrayList<Term> argsOfCond = new ArrayList<Term>();
			ArrayList<Term> allTermsOfClass = new ArrayList<Term>();
			ArrayList<EqualityConstraint> listOfConstraintsOfClass = new ArrayList<EqualityConstraint>();
	
			for (Iterator<Collection<Atom<RelationalAtom>>> atomElements = atomsOfClass.iterator(); atomElements
					.hasNext();) {
				Collection<Atom<RelationalAtom>> collectionOfAtoms = (Collection<Atom<RelationalAtom>>) atomElements
						.next();
				Term[] argsOfClass = null;
	
				for (Iterator<Atom<RelationalAtom>> atomElement = collectionOfAtoms.iterator(); atomElement
						.hasNext();) {
					Atom<RelationalAtom> atomOfClass = (Atom<RelationalAtom>) atomElement.next();
					for (Iterator<Atom<RelationalAtom>> queryAtoms = atomsOfConditional.iterator(); queryAtoms
							.hasNext();) {
						Atom<RelationalAtom> atomOfConditional = (Atom<RelationalAtom>) queryAtoms.next();
						argsOfQuery = ((RelationalAtom) atomOfConditional).getArguments();
						if (((RelationalAtom) atomOfClass).getPredicate()
								.equals(((RelationalAtom) atomOfConditional).getPredicate())) {
	
							argsOfClass = ((RelationalAtom) atomOfClass).getArguments();
	
							for (int i = 0; i < argsOfClass.length; i++) {
								argsOfCond.add(argsOfClass[i]);
	
							}
	
						}
	
					}
	
					
					for (int i = 0; i < argsOfCond.size(); i++) {
					
						Term termOfClass = argsOfCond.get(i);
						allTermsOfClass.add(termOfClass);
					}
					
	
					//for (int i = 0; i < allTermsOfClass.size(); i++) {
	
					for (int i = 0; i < argsOfCond.size(); i++) {
						for (int j = 0; j < argsOfQuery.length; j++) {
	
							Variable var = new Variable(argsOfQuery[j].toString(), argsOfQuery[j].getType());
							Constant cons = new Constant((argsOfCond.get(i)).toString(), (argsOfCond.get(i)).getType());
							EqualityConstraint equalConstraint = new EqualityConstraint(var, cons);
							listOfConstraintsOfClass.add(equalConstraint);
	
						}
	
					}
					for (int i = 0; i < listOfConstraintsOfClass.size(); i++) {
						constraint = (Formula<AtomicConstraint>) listOfConstraintsOfClass.get(i);
	
						for (int k = 1; k < argsOfClass.length; k++) {
	
							if (!(listOfConstraintsOfClass.size() > 1)) {
								break;
							}
							constraint = constraint.or((Formula<AtomicConstraint>) listOfConstraintsOfClass.get(k));
							k++;
						}
					}
	
				}
			}
	
			if (classifiedClasses instanceof RelationalFact) {
				RelationalFact relationalConditionalOfClass = new RelationalFact(consequence, probability, constraint);
				generalizedClasses.add(relationalConditionalOfClass);
			} else {
	
				RelationalConditional relationalConditionalOfClass = new RelationalConditional(consequence, antecedence,
						probability, constraint);
				generalizedClasses.add(relationalConditionalOfClass);
			} //end else
	
		} //end for
	
		return generalizedClasses;
	}// end for
	
	
	
	*/

}//end generalize
