package de.bklaiber.inference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import edu.cs.ai.log4KR.logical.syntax.Atom;
import edu.cs.ai.log4KR.logical.syntax.Formula;
import edu.cs.ai.log4KR.math.types.Fraction;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.RelationalAtom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.AtomicConstraint;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.EqualityConstraint;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Constant;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Term;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Variable;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalFact;

/*
 * This class is
 */
public class CanonicalMinimumGeneralization extends AbstractGeneralization {

	public Collection<RelationalConditional> generalize2(RelationalConditional c,
			Collection<Collection<RelationalConditional>> classifiedClasses) {

		Collection<Atom<RelationalAtom>> atomsOfQuery = getAtomsOfQuery(c);
		Collection<Collection<Atom<RelationalAtom>>> atomsOfClasses = getAtomOfClasses(classifiedClasses);
		Collection<RelationalConditional> generalization = new ArrayList<RelationalConditional>();

		for (Iterator<Collection<Atom<RelationalAtom>>> iterator = atomsOfClasses.iterator(); iterator.hasNext();) {
			Collection<Atom<RelationalAtom>> classification = (Collection<Atom<RelationalAtom>>) iterator.next();
			RelationalConditional generalizationOfClass = generalize(classification, atomsOfQuery);
			generalization.add(generalizationOfClass);
		}

		return generalization;
	}

	private RelationalConditional generalize(Collection<Atom<RelationalAtom>> classification,
			Collection<Atom<RelationalAtom>> atomsOfQuery) {
		return null;
		// TODO Auto-generated method stub

	}

	private Collection<Collection<Atom<RelationalAtom>>> getAtomOfClasses(
			Collection<Collection<RelationalConditional>> classifiedClasses) {
		// TODO Auto-generated method stub
		return null;
	}

	private Collection<Atom<RelationalAtom>> getAtomsOfQuery(RelationalConditional c) {

		Collection<Atom<RelationalAtom>> atomsOfConditional = new ArrayList<Atom<RelationalAtom>>();

		if (c instanceof RelationalFact) {

			atomsOfConditional = c.getConsequence().getAtoms();

		} else {

			atomsOfConditional = c.getAntecedence().getAtoms();

		} //end else

		return atomsOfConditional;
	}

	@Override
	public Collection<RelationalConditional> generalize(RelationalConditional c,
			Collection<Collection<RelationalConditional>> classifiedClasses) {

		//alter Quellcode
		/*
		Collection<RelationalConditional> generalizedClasses = null;
		Collection<RelationalAtom> atomsOfClass = new Vector<RelationalAtom>();
		Collection<Atom<RelationalAtom>> atomsOfConditional = new Vector<Atom<RelationalAtom>>();
		Collection<Constant> constants = new Vector<Constant>();
		Collection<Constant> constantsOfClass = new Vector<Constant>();
		Collection<Variable> variables = new Vector<Variable>();
		Formula<RelationalAtom> consequence = null;
		Formula<RelationalAtom> antecedence = null;
		Formula<AtomicConstraint> constraint = null;
		
		atomsOfConditional = c.getAtoms();
		
		for (Iterator<Atom<RelationalAtom>> atoms = atomsOfConditional.iterator(); atoms.hasNext();) {
			Variable variable = (Variable) atoms.next();
			variables.add(variable);
		}
		
		for (Iterator<Collection<RelationalConditional>> iterator = classifiedClasses.iterator(); iterator.hasNext();) {
			Collection<RelationalConditional> equivalenceClass = iterator.next();
		
			for (Iterator<RelationalConditional> elements = equivalenceClass.iterator(); elements.hasNext();) {
				RelationalConditional element = (RelationalConditional) elements.next();
		
				atomsOfClass.add((RelationalAtom) element.getAtoms());
				for (Iterator<RelationalAtom> atomAt = atomsOfClass.iterator(); atomAt.hasNext();) {
		
					constants = (atomAt.next()).getConstants();
					constantsOfClass.addAll(constants);
				}
				consequence = c.getConsequence();
				antecedence = c.getAntecedence();
		
				for (Iterator allConstants = constantsOfClass.iterator(); allConstants.hasNext();) {
					Constant constant = (Constant) allConstants.next();
		
				}
		
				RelationalConditional RelationalConditionalOfClass = new RelationalConditional(consequence, antecedence,
						constraint);
		
				generalizedClasses.add(RelationalConditionalOfClass);
		
			}
		}
		*/

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

					/*
					for (int i = 0; i < argsOfCond.size(); i++) {
					
						Term termOfClass = argsOfCond.get(i);
						allTermsOfClass.add(termOfClass);
					}
					*/

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

}//end generalize
