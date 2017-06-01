package de.bklaiber.inference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import edu.cs.ai.log4KR.logical.syntax.Atom;
import edu.cs.ai.log4KR.logical.syntax.Formula;
import edu.cs.ai.log4KR.math.types.Fraction;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.RelationalAtom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.AtomicConstraint;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalFact;

/*
 * This class is
 */
public class CanonicalMinimumGeneralization extends AbstractGeneralization {

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

		if (c instanceof RelationalFact) {

			atomsOfConditional = c.getConsequence().getAtoms();

		} else {

			atomsOfConditional = c.getAntecedence().getAtoms();

		} //end else

		//((A(a) v B(b))[0,2]; (A(b) v B(c))[0,2]); (A(c) v B(d)[0,8])
		//(A(X) v B(Y)[0,2]<(X=a und Y=b) oder (X=b und Y=c)>); (A(X) v B(Y)[0,8]<X=c und Y=d>);

		//for each equivalence class in classifiedClasses create a relational conditional with constraint
		for (Iterator<Collection<RelationalConditional>> iterator = classifiedClasses.iterator(); iterator.hasNext();) {
			Collection<RelationalConditional> equivalenceClass = iterator.next();

			Collection<Collection<Atom<RelationalAtom>>> atomsOfClass = new ArrayList<Collection<Atom<RelationalAtom>>>();

			//take each conditional in the equivalence class and create an element of the constraint with the variables
			for (Iterator<RelationalConditional> elements = equivalenceClass.iterator(); elements.hasNext();) {
				RelationalConditional element = (RelationalConditional) elements.next();
				Collection<Atom<RelationalAtom>> atomsOfElement = new ArrayList<Atom<RelationalAtom>>();
				probability = element.getProbability();//TODO Mittelwert berechnen

				//if the relational conditional is a fact, look at the consequence, else look at the antecedence
				if (c instanceof RelationalFact) {

					atomsOfElement = element.getConsequence().getAtoms();
					atomsOfClass.add(atomsOfElement);

				} else {

					atomsOfElement = element.getAntecedence().getAtoms();
					atomsOfClass.add(atomsOfElement);
				} //end else
			} //end for

			for (Iterator<Collection<Atom<RelationalAtom>>> allAtoms = atomsOfClass.iterator(); allAtoms.hasNext();) {
				Collection<Atom<RelationalAtom>> atomC = (Collection<Atom<RelationalAtom>>) allAtoms.next();

			}
			RelationalConditional RelationalConditionalOfClass = new RelationalConditional(consequence, antecedence,
					probability, constraint);

			generalizedClasses.add(RelationalConditionalOfClass);

		} //end for

		return generalizedClasses;
	}// end for

}//end generalize
