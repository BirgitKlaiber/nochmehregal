package de.bklaiber.inference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import edu.cs.ai.log4KR.logical.syntax.Atom;
import edu.cs.ai.log4KR.logical.syntax.Conjunction;
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

/**
 * This class is used to generalize the classes.
 * 
 * 
 */
public class CanonicalMinimumGeneralization extends AbstractGeneralization {

	@Override
	/**
	 * Generalizes canonically all the variables with the corresponding
	 * constants (positive generalization). Sorts by size and generalizes with
	 * exclusions (negative generalization. Compares both generalizations and
	 * selects the better one. For example the shorter string representation and
	 * returns the "better" generalization.
	 * 
	 * 
	 * for example: <code>
	 * 
	 * c = flies(X)
	 * 
	 * positive generalization (flies(X))[0.6636035435403175]<((X=Sylvester + X=Kirby) + X=Bully)>);(flies(X))[3.9682291970525344E-9]<X=Tweety>)
	 * negative generalization (flies(X))[0.6636035435403175]<X!=Tweety>)(flies(X))[3.9682291970525344E-9]<X=Tweety>)
	 * 
	 * returns the negative generalization after comparison.
	 * 
	 * 
	 * </code>
	 * 
	 * @return generalization
	 */
	public Collection<RelationalConditional> generalize(RelationalConditional c,
			Collection<Collection<RelationalConditional>> classifiedClasses, Collection<RelationalConditional> kb) {

		Collection<RelationalConditional> generalizationNegative = new ArrayList<RelationalConditional>();
		Collection<RelationalConditional> generalizationPositive = new ArrayList<RelationalConditional>();
		Collection<RelationalConditional> generalization = new ArrayList<RelationalConditional>();
		Collection<RelationalConditional> condsOfKb = kb;

		GeneralizationAsStringComperator comperator = new GeneralizationAsStringComperator();
		int comparison = 0;

		generalizationPositive = generalizePositive(c, classifiedClasses);
		generalizationNegative = generalizeNegative(c, classifiedClasses, condsOfKb);

		comparison = comperator.compare(generalizationNegative, generalizationPositive);

		switch (comparison) {
		case 0:
			generalization = generalizationPositive;
			break;
		case 1:
			generalization = generalizationNegative;
			break;
		case 2:
			generalization = generalizationPositive;
			break;
		default:
			break;
		}

		return generalization;
	}

	/**
	 * Generalizes positive canonically. Which means an disjunction of equality
	 * constraints which are generates with each variable and its corresponding
	 * constant. The reflexive knowledge has an exclusive treatment.
	 * 
	 * for example: <code>
	 * 			
	 * c = flies(X)
	 * 
	 * (flies(X))[0.6636035435403175]&lt;((X=Sylvester + X=Kirby) + X=Bully)&gt;);(flies(Tweety))[3.9682291970525344E-9])
	 * 
	 * </code>
	 * 
	 * @param c
	 *            qualified conditional of the query
	 * @param classifiedClasses
	 *            the classified classes of the ground instance (those with
	 *            equal probability)
	 * @return generalization
	 */
	public Collection<RelationalConditional> generalizePositive(RelationalConditional c,
			Collection<Collection<RelationalConditional>> classifiedClasses) {

		Collection<Atom<RelationalAtom>> atomsOfQuery = getAtomsOfQuery(c);
		Collection<RelationalConditional> generalization = new ArrayList<RelationalConditional>();

		//if there is only one class: quantified conditional with probability = qualified conditional 
		if (classifiedClasses.size() == 1) {
			Iterator<Collection<RelationalConditional>> iteratorOneClass = classifiedClasses.iterator();
			Collection<RelationalConditional> classificationOne = iteratorOneClass.next();
			Fraction probabilityOne = getProbabilitiesOfClass(classificationOne);
			RelationalConditional generalizationOfClass = generateConditionalForOne(c, probabilityOne);
			generalization.add(generalizationOfClass);
			//System.out.println(generalization.toString());
		}

		Iterator<Collection<RelationalConditional>> iteratorTwoClasses = classifiedClasses.iterator();
		Collection<RelationalConditional> classificationTwo = iteratorTwoClasses.next();

		//System.out.println(classifiedClasses.toString());
		//if there are only two classes
		if (classifiedClasses.size() == 2) {

			Collection<Collection<Atom<RelationalAtom>>> atomsOfClass = getAtomOfClass(classificationTwo);
			Fraction probability = getProbabilitiesOfClass(classificationTwo);
			Fraction probability2 = null;
			Formula<AtomicConstraint> constraintOfFirstClass = null;
			Formula<AtomicConstraint> constraintOfSecondClass = null;

			//if the first of the two clsssifications is reflexive
			if (isReflexive(classificationTwo)) {
				constraintOfFirstClass = generateReflexiveConstraint(atomsOfQuery);
				constraintOfSecondClass = generateReflexiveNegativeConstraint(atomsOfQuery);
			} else {
				Collection<RelationalConditional> nextClassification = null;
				if (iteratorTwoClasses.hasNext()) {
					nextClassification = iteratorTwoClasses.next();
					probability2 = getProbabilitiesOfClass(nextClassification);
				}

				if (isReflexive(nextClassification)) {

					constraintOfFirstClass = generateReflexiveNegativeConstraint(atomsOfQuery);
					constraintOfSecondClass = generateReflexiveConstraint(atomsOfQuery);
				} else {
					Collection<Collection<Atom<RelationalAtom>>> atomsOfSecondClass = getAtomOfClass(
							nextClassification);
					constraintOfSecondClass = generatePositiveConstraint(atomsOfSecondClass, atomsOfQuery);
					constraintOfFirstClass = generatePositiveConstraint(atomsOfClass, atomsOfQuery);

				}
				Collection<Collection<Atom<RelationalAtom>>> atomsOfSecondClass = getAtomOfClass(nextClassification);

				//if the class only contains one conditional 
				if (nextClassification.size() == 1) {
					RelationalConditional con = null;
					Iterator<RelationalConditional> iterator1 = nextClassification.iterator();
					if (iterator1.hasNext()) {
						con = (RelationalConditional) iterator1.next();

					}

					RelationalConditional generalizationOfSecondClass = generateConditionalForOne(con, probability2);

					generalization.add(generalizationOfSecondClass);
				} else {

					RelationalConditional generalizationOfSecondClass = generateConditional(c, constraintOfSecondClass,
							probability2);

					generalization.add(generalizationOfSecondClass);

				}

				if (classificationTwo.size() == 1) {
					RelationalConditional con = null;
					Iterator<RelationalConditional> iterator2 = classificationTwo.iterator();
					if (iterator2.hasNext()) {
						con = (RelationalConditional) iterator2.next();

					}

					RelationalConditional generalizationOfClass = generateConditionalForOne(con, probability);

					generalization.add(generalizationOfClass);

				} else {
					RelationalConditional generalizationOfClass = generateConditional(c, constraintOfFirstClass,
							probability);

					generalization.add(generalizationOfClass);

				}

				//if both of the classes aren´t reflexive
				//bei zweistelligen Pädikaten Klassen muss immer eine reflexiv sein; der Fall, dass beide nicht reflexiv sind, kann da nicht eintreten
				if (!isReflexive(classificationTwo) && !isReflexive(nextClassification)) {
					constraintOfFirstClass = generatePositiveConstraint(atomsOfClass, atomsOfQuery);
					constraintOfSecondClass = generatePositiveConstraint(atomsOfSecondClass, atomsOfQuery);
					probability2 = getProbabilitiesOfClass(nextClassification);
				} else {
					if (isReflexive(nextClassification)) {
						//probability2 = getProbabilitiesOfClass(nextClassification);
						constraintOfSecondClass = generateReflexiveConstraint(atomsOfQuery);
						constraintOfFirstClass = generateReflexiveNegativeConstraint(atomsOfQuery);

					}
				}
			}

		} else {
			//there are more than two classes

			for (Iterator<Collection<RelationalConditional>> iterator = classifiedClasses.iterator(); iterator
					.hasNext();) {
				Collection<RelationalConditional> classification = iterator.next();

				Collection<Collection<Atom<RelationalAtom>>> atomsOfClass = getAtomOfClass(classification);
				Fraction probability = getProbabilitiesOfClass(classification);
				Formula<AtomicConstraint> constraintOfClass = null;

				//if the class only contains one conditional
				if (classification.size() == 1) {
					RelationalConditional con = null;
					Iterator<RelationalConditional> iterator1 = classification.iterator();
					if (iterator1.hasNext()) {
						con = (RelationalConditional) iterator1.next();
					}

					RelationalConditional generalizationOfClass = generateConditionalForOne(con, probability);

					generalization.add(generalizationOfClass);
				}
				if (isReflexive(classification)) {
					constraintOfClass = generateReflexiveConstraint(atomsOfQuery);
				} else {

					constraintOfClass = generatePositiveConstraint(atomsOfClass, atomsOfQuery);
				}
				RelationalConditional generalizationOfClass = generateConditional(c, constraintOfClass, probability);
				generalization.add(generalizationOfClass);

			}

		}
		System.out.println("generalisationPositive" + generalization.toString());
		return generalization;

	}//endofgeneralizePositive

	/**
	 * In this method the generalization of the class is generated depending on
	 * whether it contains specific constants or not
	 * 
	 * for example: <code>
	 * 
	 * c = flies(X)
	 * 
	 *(flies(X))[0.6636035435403175]<X!=Tweety>)(flies(X))[3.9682291970525344E-9]<X=Tweety>)
	 * 
	 * </code>
	 * 
	 * @param c
	 *            the conditional of the query
	 * @param classifiedClasses
	 *            ground instances of the query classified by equal probability
	 * @param condsOfKb
	 * @return generalization
	 */
	public Collection<RelationalConditional> generalizeNegative(RelationalConditional c,
			Collection<Collection<RelationalConditional>> classifiedClasses,
			Collection<RelationalConditional> condsOfKb) {

		ArrayList<Collection<RelationalConditional>> classifiedClassesList = new ArrayList<>(classifiedClasses);
		Collection<Atom<RelationalAtom>> atomsOfQuery = getAtomsOfQuery(c);
		Collection<Collection<Atom<RelationalAtom>>> atomsOfSecondClass = null;
		Collection<RelationalConditional> generalization = new ArrayList<RelationalConditional>();

		//if there is only one class: quantified conditional with probability = qualified conditional 
		if (classifiedClasses.size() == 1) {
			Iterator<Collection<RelationalConditional>> iteratorOneClass = classifiedClasses.iterator();
			Collection<RelationalConditional> classificationOne = iteratorOneClass.next();
			Fraction probabilityOne = getProbabilitiesOfClass(classificationOne);
			RelationalConditional generalizationOfClass = generateConditionalForOne(c, probabilityOne);
			generalization.add(generalizationOfClass);

		}
		Iterator<Collection<RelationalConditional>> iteratorTwoClasses = classifiedClasses.iterator();
		Collection<RelationalConditional> classificationTwo = iteratorTwoClasses.next();

		//if there are only two classes
		if (classifiedClasses.size() == 2) {

			Collection<Collection<Atom<RelationalAtom>>> atomsOfClass = getAtomOfClass(classificationTwo);
			Fraction probability = getProbabilitiesOfClass(classificationTwo);
			Fraction probability2 = null;
			Formula<AtomicConstraint> constraintOfFirstClass = null;
			Formula<AtomicConstraint> constraintOfSecondClass = null;

			//if the first of the two clsssifications is reflexive
			if (isReflexive(classificationTwo)) {
				constraintOfFirstClass = generateReflexiveConstraint(atomsOfQuery);
				constraintOfSecondClass = generateReflexiveNegativeConstraint(atomsOfQuery);
			} else {
				Collection<RelationalConditional> nextClassification = null;
				if (iteratorTwoClasses.hasNext()) {
					nextClassification = iteratorTwoClasses.next();
					probability2 = getProbabilitiesOfClass(nextClassification);
				}

				if (isReflexive(nextClassification)) {

					constraintOfFirstClass = generateReflexiveNegativeConstraint(atomsOfQuery);
					constraintOfSecondClass = generateReflexiveConstraint(atomsOfQuery);
				} else {

					atomsOfSecondClass = getAtomOfClass(nextClassification);
					//if the second class contains more atoms, then the constraint must be first class positive, second class negativ; i.e. ([ flies(Tweety), flies(Sylvester), flies(Bully), flies(Kirby))] constraint first class (here no contraint, because of Number of elments = 1, second class <X <> Tweety>  
					if (atomsOfSecondClass.size() > atomsOfClass.size()) {
						// es wird keine Abfrage auf Anzahl == 1 benoetigt, da es an anderer Stelle abgefragt wird
						constraintOfSecondClass = generateNegativeConstraint(atomsOfSecondClass, atomsOfQuery);
						constraintOfFirstClass = generatePositiveConstraint(atomsOfSecondClass, atomsOfQuery);
					} else {
						constraintOfFirstClass = generateNegativeConstraint(atomsOfSecondClass, atomsOfQuery);
						constraintOfSecondClass = generatePositiveConstraint(atomsOfSecondClass, atomsOfQuery);
					}
				}

				//if the class only contains one conditional 
				if (nextClassification.size() == 1) {
					RelationalConditional con = null;
					Iterator<RelationalConditional> iterator1 = nextClassification.iterator();
					if (iterator1.hasNext()) {
						con = (RelationalConditional) iterator1.next();

					}

					RelationalConditional generalizationOfSecondClass = generateConditionalForOne(con, probability2);

					generalization.add(generalizationOfSecondClass);
				} else {

					RelationalConditional generalizationOfSecondClass = generateConditional(c, constraintOfSecondClass,
							probability2);

					generalization.add(generalizationOfSecondClass);

				}

				if (classificationTwo.size() == 1) {
					RelationalConditional con = null;
					Iterator<RelationalConditional> iterator2 = classificationTwo.iterator();
					if (iterator2.hasNext()) {
						con = (RelationalConditional) iterator2.next();

					}

					RelationalConditional generalizationOfClass = generateConditionalForOne(con, probability);

					generalization.add(generalizationOfClass);

				} else {
					RelationalConditional generalizationOfClass = generateConditional(c, constraintOfFirstClass,
							probability);

					generalization.add(generalizationOfClass);

				}

				//if both of the classes aren´t reflexive
				//bei zweistelligen Pädikaten Klassen muss immer eine reflexiv sein; der Fall, dass beide nicht reflexiv sind, kann da nicht eintreten
				if (!isReflexive(classificationTwo) && !isReflexive(nextClassification)) {
					constraintOfFirstClass = generatePositiveConstraint(atomsOfClass, atomsOfQuery);
					constraintOfSecondClass = generatePositiveConstraint(atomsOfSecondClass, atomsOfQuery);
					probability2 = getProbabilitiesOfClass(nextClassification);
				} else {
					if (isReflexive(nextClassification)) {
						//probability2 = getProbabilitiesOfClass(nextClassification);
						constraintOfSecondClass = generateReflexiveConstraint(atomsOfQuery);
						constraintOfFirstClass = generateReflexiveNegativeConstraint(atomsOfQuery);

					}
				}
			}

		}
		//if there are more than two classes
		else {

			Formula<AtomicConstraint> constraintOfReflexiveClass = null;

			for (Iterator<Collection<RelationalConditional>> classificationsMore = classifiedClasses
					.iterator(); classificationsMore.hasNext();) {
				Collection<RelationalConditional> classificationM = classificationsMore.next();
				Fraction probability = getProbabilitiesOfClass(classificationM);

				if (isReflexive(classificationM)) {
					constraintOfReflexiveClass = generateReflexiveConstraint(atomsOfQuery);

					RelationalConditional generalizationOfClass = generateConditional(c, constraintOfReflexiveClass,
							probability);

					generalization.add(generalizationOfClass);

				}

			}

			//iterate over the classes which are not reflexive
			for (Iterator<Collection<RelationalConditional>> classificationsMore2 = classifiedClasses
					.iterator(); classificationsMore2.hasNext();) {
				Collection<RelationalConditional> classificationM = classificationsMore2.next();
				Fraction probability = getProbabilitiesOfClass(classificationM);

				//if the classification contains only one conditional
				if (classificationM.size() == 1) {
					RelationalConditional con = null;
					Iterator<RelationalConditional> iterator1 = classificationM.iterator();
					if (iterator1.hasNext()) {
						con = (RelationalConditional) iterator1.next();

					}

					RelationalConditional generalizationOfClass = generateConditionalForOne(con, probability);

					generalization.add(generalizationOfClass);
				} else {
					if (!isReflexive(classificationM)) {
						Collection<Constant> constantsOfClass = new Vector<>();
						Collection<Constant> specificConstants = new Vector<>();
						boolean containsSpecific = false;

						specificConstants = getSpecificConstants(condsOfKb);

						Collection<Atom<RelationalAtom>> atomsOfClass = new Vector<>();

						RelationalConditional con = null;
						Iterator<RelationalConditional> iterator1 = classificationM.iterator();
						if (iterator1.hasNext()) {
							con = (RelationalConditional) iterator1.next();

						}
						//get all the constants of the classification
						atomsOfClass = con.getAtoms();

						for (Iterator<Atom<RelationalAtom>> iterator = atomsOfClass.iterator(); iterator.hasNext();) {
							Atom<RelationalAtom> atom = (Atom<RelationalAtom>) iterator.next();
							constantsOfClass.addAll(((RelationalAtom) atom).getConstants());
						}
						//if the classification contains a specific constant, generate the constraint with specific constants
						Formula<AtomicConstraint> constraintOfClass = null;
						for (Iterator<Constant> constantsIterator = specificConstants.iterator(); constantsIterator
								.hasNext();) {
							Constant specificConstant = constantsIterator.next();
							for (Iterator<Constant> iterator = constantsOfClass.iterator(); iterator.hasNext();) {
								Constant constant = iterator.next();
								if (constant.equals(specificConstant)) {

									containsSpecific = true;
								}
							}

						} //endfor

						if (containsSpecific) {
							constraintOfClass = generateSpecificConstraint(c, atomsOfQuery, specificConstants);
						} else {
							constraintOfClass = generateSpecificNegativeConstraint(c, atomsOfQuery, specificConstants);
						}
						RelationalConditional generalizationOfClass = generateConditional(c, constraintOfClass,
								probability);

						generalization.add(generalizationOfClass);

					} //endif
				} //endelse

			}

		} //end else
		System.out.println("generalisation negative" + generalization.toString());
		return generalization;
	}

	public Formula<AtomicConstraint> generateReflexiveConstraint(Collection<Atom<RelationalAtom>> atomsOfQuery) {

		Collection<Formula<AtomicConstraint>> argsOfClass = new ArrayList<Formula<AtomicConstraint>>();
		ArrayList<Formula<AtomicConstraint>> elementsOfConstraintsOfClass = new ArrayList<Formula<AtomicConstraint>>();
		Formula<AtomicConstraint> constraint = null;

		for (Iterator<Atom<RelationalAtom>> iterator = atomsOfQuery.iterator(); iterator.hasNext();) {
			Atom<RelationalAtom> atom = (Atom<RelationalAtom>) iterator.next();

			Term[] argsOfQueryAtom = ((RelationalAtom) atom).getArguments();
			elementsOfConstraintsOfClass = generateElementsOfReflexiveConstraint(argsOfQueryAtom);
			argsOfClass.addAll(elementsOfConstraintsOfClass);
			constraint = generateDisjunctionConstraint(argsOfClass);

		}
		return constraint;

	}

	public Formula<AtomicConstraint> generateReflexiveNegativeConstraint(
			Collection<Atom<RelationalAtom>> atomsOfQuery) {

		Collection<Formula<AtomicConstraint>> argsOfClass = new ArrayList<Formula<AtomicConstraint>>();
		ArrayList<Formula<AtomicConstraint>> elementsOfConstraintsOfClass = new ArrayList<Formula<AtomicConstraint>>();
		Formula<AtomicConstraint> constraint = null;

		for (Iterator<Atom<RelationalAtom>> iterator = atomsOfQuery.iterator(); iterator.hasNext();) {
			Atom<RelationalAtom> atom = (Atom<RelationalAtom>) iterator.next();

			Term[] argsOfQueryAtom = ((RelationalAtom) atom).getArguments();
			elementsOfConstraintsOfClass = generateElementsOfReflexiveNegativeConstraint(argsOfQueryAtom);
			argsOfClass.addAll(elementsOfConstraintsOfClass);
			constraint = generateDisjunctionConstraint(argsOfClass);

		}
		return constraint;

	}

	/**
	 * Generates the constraint for the positive generalization,
	 * 
	 * @param atomsOfClass
	 * @param atomsOfQuery
	 * @return constraint
	 */
	private Formula<AtomicConstraint> generatePositiveConstraintAlt(
			Collection<Collection<Atom<RelationalAtom>>> atomsOfClass, Collection<Atom<RelationalAtom>> atomsOfQuery) {

		Collection<Formula<AtomicConstraint>> argsOfClass = new ArrayList<Formula<AtomicConstraint>>();
		ArrayList<Formula<AtomicConstraint>> elementsOfConstraintsOfClass = new ArrayList<Formula<AtomicConstraint>>();
		Formula<AtomicConstraint> constraint = null;
		Formula<AtomicConstraint> constraintTemp = null;
		boolean predicateMore = false;

		//TODO an dieser Stelle muss unterschieden werden, ob es sich um eine atomare Formel oder eine zusammengesetzte Formel handelt, bei atomarer Formel enthält atomOfConditional nur ein Element, dann bleibt alles, ansosten muss man  

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

						//elementsOfConstraintsOfClass = generateElementsOfConstraint(argsOfConditional, argsOfQueryAtom);
						argsOfClass.addAll(elementsOfConstraintsOfClass);
						//System.out.println("elementsofConstraint" + argsOfClass.toString());

					}

					if (argsOfClass.size() > 1) {
						constraintTemp = generateDisjunctionConstraint(argsOfClass);
						//System.out.println("constemp" + constraintTemp.toString());
					}
					if (((RelationalAtom) atom).getPredicate().getArity() > 1) {
						predicateMore = true;
					}

				}

			}

		}

		if (predicateMore) {
			Formula<AtomicConstraint> reflexiveNegativeConstraint = generateReflexiveNegativeConstraint(atomsOfQuery);
			constraint = new Conjunction<>(constraintTemp, reflexiveNegativeConstraint);
		} else {
			constraint = constraintTemp;
		}
		return constraint;
	}

	/**
	 * Generates the constraint for the positive generalization,
	 * 
	 * @param atomsOfClass
	 * @param atomsOfQuery
	 * @return constraint
	 */
	private Formula<AtomicConstraint> generatePositiveConstraint(
			Collection<Collection<Atom<RelationalAtom>>> atomsOfClass, Collection<Atom<RelationalAtom>> atomsOfQuery) {

		Collection<Formula<AtomicConstraint>> argsOfClass = new HashSet<Formula<AtomicConstraint>>();
		Collection<Formula<AtomicConstraint>> elementsOfConstraintsOfClass = new HashSet<Formula<AtomicConstraint>>();
		ArrayList<Formula<AtomicConstraint>> listOfConjunctions = new ArrayList<>();
		ArrayList<Formula<AtomicConstraint>> listOfConstraints = new ArrayList<>();
		Formula<AtomicConstraint> constraint = null;
		Formula<AtomicConstraint> constraintTemp = null;
		boolean predicateMore = false;

		//TODO an dieser Stelle muss unterschieden werden, ob es sich um eine atomare Formel oder eine zusammengesetzte Formel handelt, bei atomarer Formel enthält atomOfConditional nur ein Element, dann bleibt alles, ansosten muss man  

		//if the Query is an atomic formula
		//get the arguments for each conditional
		//take each collection for the conditional
		for (Iterator<Collection<Atom<RelationalAtom>>> atoms = atomsOfClass.iterator(); atoms.hasNext();) {
			Collection<Atom<RelationalAtom>> atomOfCondtional = atoms.next();
			//take each atom of each conditional
			if (atomOfCondtional.size() == 1) {

				for (Iterator<Atom<RelationalAtom>> element = atomOfCondtional.iterator(); element.hasNext();) {
					Atom<RelationalAtom> atom = (Atom<RelationalAtom>) element.next();
					//get the arguments of each atom of the conditional
					for (Iterator<Atom<RelationalAtom>> iteratorOfQuery = atomsOfQuery.iterator(); iteratorOfQuery
							.hasNext();) {
						Atom<RelationalAtom> atomOfQuery = (Atom<RelationalAtom>) iteratorOfQuery.next();

						if (((RelationalAtom) atom).getPredicate()
								.equals(((RelationalAtom) atomOfQuery).getPredicate())) {

							Term[] argsOfQueryAtom = ((RelationalAtom) atomOfQuery).getArguments();
							Term[] argsOfConditional = ((RelationalAtom) atom).getArguments();

							elementsOfConstraintsOfClass = generateElementsOfConstraint(argsOfConditional,
									argsOfQueryAtom);
							argsOfClass.addAll(elementsOfConstraintsOfClass);

							//Formula<AtomicConstraint> conjunction = generateConjunctionConstraint(argsOfClass);
							//listOfConstraints.add(conjunction);
							//System.out.println("elementsofConstraintif" + argsOfClass.toString());
							//System.out.println("argsofclassif" + argsOfClass.toString());

						}

						if (argsOfClass.size() > 1) {
							constraintTemp = generateDisjunctionConstraint(argsOfClass);
							//System.out.println("constemp" + constraintTemp.toString());
						}
						if (((RelationalAtom) atom).getPredicate().getArity() > 1) {
							predicateMore = true;
						}

					}

				}

			} else {

				//if the query is a composed formula

				/*for (Iterator<Atom<RelationalAtom>> iteratorOfQuery = atomsOfQuery.iterator(); iteratorOfQuery
						.hasNext();) {
					Atom<RelationalAtom> atomOfQuery = (Atom<RelationalAtom>) iteratorOfQuery.next();
				
					for (Iterator<Atom<RelationalAtom>> element = atomOfCondtional.iterator(); element.hasNext();) {
						Atom<RelationalAtom> atom = (Atom<RelationalAtom>) element.next();
						//get the arguments of each atom of the conditional
						if (((RelationalAtom) atom).getPredicate()
								.equals(((RelationalAtom) atomOfQuery).getPredicate())) {
				
							Term[] argsOfQueryAtom = ((RelationalAtom) atomOfQuery).getArguments();
							Term[] argsOfConditional = ((RelationalAtom) atom).getArguments();
				
							elementsOfConstraintsOfClass = generateElementsOfFormulaConstraint(argsOfConditional,
									argsOfQueryAtom);
				
							argsOfClass.addAll(elementsOfConstraintsOfClass);
				
							System.out.println("elementsofConstraintelse" + elementsOfConstraintsOfClass.toString());
							System.out.println("argsofclasselse" + argsOfClass.toString());
							Formula<AtomicConstraint> conjunction = generateConjunctionConstraint(argsOfClass);
							listOfConstraints.add(conjunction);
							System.out.println("listOfConstraintselse" + listOfConstraints.toString());
				
						}
						if (((RelationalAtom) atom).getPredicate().getArity() > 1) {
							predicateMore = true;
						}
				
					}
					
					
				*/

				argsOfClass = new HashSet<Formula<AtomicConstraint>>();

				listOfConstraints = new ArrayList<>();
				for (Iterator<Atom<RelationalAtom>> element = atomOfCondtional.iterator(); element.hasNext();) {
					Atom<RelationalAtom> atom = (Atom<RelationalAtom>) element.next();
					listOfConstraints = new ArrayList<>();

					elementsOfConstraintsOfClass = null;

					for (Iterator<Atom<RelationalAtom>> iteratorOfQuery = atomsOfQuery.iterator(); iteratorOfQuery
							.hasNext();) {
						Atom<RelationalAtom> atomOfQuery = (Atom<RelationalAtom>) iteratorOfQuery.next();

						if (((RelationalAtom) atom).getPredicate()
								.equals(((RelationalAtom) atomOfQuery).getPredicate())) {

							Term[] argsOfQueryAtom = ((RelationalAtom) atomOfQuery).getArguments();
							Term[] argsOfConditional = ((RelationalAtom) atom).getArguments();

							elementsOfConstraintsOfClass = generateElementsOfFormulaConstraint(argsOfConditional,
									argsOfQueryAtom);

							argsOfClass.addAll(elementsOfConstraintsOfClass);

						}

					}
					HashSet<Formula<AtomicConstraint>> argsOfClass2 = new HashSet<Formula<AtomicConstraint>>();
					for (Iterator iterator1 = argsOfClass.iterator(); iterator1.hasNext();) {
						Formula<AtomicConstraint> formula1 = (Formula<AtomicConstraint>) iterator1.next();

						for (Iterator iterator2 = argsOfClass.iterator(); iterator2.hasNext();) {
							Formula<AtomicConstraint> formula2 = (Formula<AtomicConstraint>) iterator2.next();
							if (formula1.toString().equals(formula2.toString())) {
								argsOfClass2.add(formula1);

							} else {

							}

						}
					}

					Formula<AtomicConstraint> conjunction = generateConjunctionConstraint(argsOfClass);
					listOfConstraints.add(conjunction);
					//System.out.println("listOfCondconsselse" + listOfConstraints.toString());

					if (((RelationalAtom) atom).getPredicate().getArity() > 1) {
						predicateMore = true;
					}

				}
				listOfConjunctions.addAll(listOfConstraints);
				//System.out.println("listofConjunctions" + listOfConjunctions.toString());

				if (listOfConjunctions.size() > 1) {
					constraintTemp = generateDisjunctionConstraint(listOfConjunctions);
					if (constraintTemp != null) {

					}
				}
			} //endelse

		}

		if (predicateMore) {
			Formula<AtomicConstraint> reflexiveNegativeConstraint = generateReflexiveNegativeConstraint(atomsOfQuery);
			constraint = new Conjunction<>(constraintTemp, reflexiveNegativeConstraint);
		} else {
			constraint = constraintTemp;
		}
		return constraint;
	}

	/**
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
						constraint = generateConjunctionConstraint(negativeArgsOfClass);

					}

				}

			}

		}

		return constraint;

	}

	/**
	 * Generates a Constraint containing specific constants.
	 * 
	 * @param c
	 * @param atomsOfQuery
	 * @param constants
	 * 
	 * @return Constraint containing specific constants
	 */
	public Formula<AtomicConstraint> generateSpecificConstraint(RelationalConditional c,
			Collection<Atom<RelationalAtom>> atomsOfQuery, Collection<Constant> constants) {

		Collection<Formula<AtomicConstraint>> elementsOfConstraintsOfClass = new ArrayList<Formula<AtomicConstraint>>();
		Formula<AtomicConstraint> constraint = null;
		Formula<AtomicConstraint> constraintTemp = null;
		Boolean predicateMore = false;
		Term[] args = null;
		ArrayList<Term> allArgs = new ArrayList<Term>();

		for (Iterator<Atom<RelationalAtom>> iterator = atomsOfQuery.iterator(); iterator.hasNext();) {
			Atom<RelationalAtom> atom = (Atom<RelationalAtom>) iterator.next();

			if (((RelationalAtom) atom).getPredicate().getArity() > 1) {
				predicateMore = true;
			}
			args = ((RelationalAtom) atom).getArguments();
			for (int i = 0; i < args.length; i++) {
				allArgs.add(args[i]);
			}

		}

		elementsOfConstraintsOfClass = generateElementsOfSpecificConstraint(allArgs, constants);
		constraintTemp = generateDisjunctionConstraint(elementsOfConstraintsOfClass);

		if (predicateMore) {
			Formula<AtomicConstraint> reflexiveNegativeConstraint = generateReflexiveNegativeConstraint(c.getAtoms());
			constraint = new Conjunction<>(constraintTemp, reflexiveNegativeConstraint);
		} else {
			constraint = constraintTemp;
		}

		return constraint;

	}

	private ArrayList<Formula<AtomicConstraint>> generateElementsOfSpecificConstraint(ArrayList<Term> allArgs,
			Collection<Constant> constants) {

		ArrayList<Formula<AtomicConstraint>> listOfConstraints = new ArrayList<Formula<AtomicConstraint>>();

		HashSet<Term> allArgsSet = new HashSet<>();
		for (Term term : allArgs) {
			allArgsSet.add(term);
		}

		for (Term term : allArgsSet) {

			Variable var = new Variable(term.toString(), term.getType());
			for (Iterator<Constant> iterator2 = constants.iterator(); iterator2.hasNext();) {
				Constant c = iterator2.next();
				EqualityConstraint atomicConstraint = new EqualityConstraint(var, c);

				listOfConstraints.add(atomicConstraint);

			}
		}

		return listOfConstraints;
	}

	/**
	 * Generates a Conjunction of Inequality Constraints mith the variables of
	 * the knowledgebase an the specific constants
	 * 
	 * @param c
	 * @param atomsOfQuery
	 * @param constants
	 * @return the constraint of a class that doesn´t contain specific constants
	 */
	public Formula<AtomicConstraint> generateSpecificNegativeConstraint(RelationalConditional c,
			Collection<Atom<RelationalAtom>> atomsOfQuery, Collection<Constant> constants) {

		Collection<Formula<AtomicConstraint>> elementsOfConstraintsOfClass = new ArrayList<Formula<AtomicConstraint>>();
		Formula<AtomicConstraint> constraint = null;
		Formula<AtomicConstraint> constraintTemp = null;
		Boolean predicateMore = false;
		Term[] args = null;
		ArrayList<Term> allArgs = new ArrayList<Term>();

		for (Iterator<Atom<RelationalAtom>> iterator = atomsOfQuery.iterator(); iterator.hasNext();) {
			Atom<RelationalAtom> atom = (Atom<RelationalAtom>) iterator.next();

			if (((RelationalAtom) atom).getPredicate().getArity() > 1) {
				predicateMore = true;
			}
			args = ((RelationalAtom) atom).getArguments();
			for (int i = 0; i < args.length; i++) {
				allArgs.add(args[i]);
			}
		}

		elementsOfConstraintsOfClass = generateElementsOfSpecificNegativeConstraint(allArgs, constants);
		constraintTemp = generateConjunctionConstraint(elementsOfConstraintsOfClass);

		if (predicateMore) {
			Formula<AtomicConstraint> reflexiveNegativeConstraint = generateReflexiveNegativeConstraint(c.getAtoms());
			constraint = new Conjunction<>(constraintTemp, reflexiveNegativeConstraint);
		} else {
			constraint = constraintTemp;
		}

		return constraint;
	}

	/**
	 * Generates the elements of a specific negative Constraint
	 * 
	 * @param allArgs
	 * @param constants
	 * @return
	 */
	private ArrayList<Formula<AtomicConstraint>> generateElementsOfSpecificNegativeConstraint(ArrayList<Term> allArgs,
			Collection<Constant> constants) {
		ArrayList<Formula<AtomicConstraint>> listOfConstraints = new ArrayList<Formula<AtomicConstraint>>();

		HashSet<Term> allArgsSet = new HashSet<>();
		for (Term term : allArgs) {
			allArgsSet.add(term);
		}

		for (Term term : allArgsSet) {
			Variable var = new Variable(term.toString(), term.getType());
			for (Iterator<Constant> iterator = constants.iterator(); iterator.hasNext();) {
				Constant c = iterator.next();

				InequalityConstraint atomicConstraint = new InequalityConstraint(var, c);
				listOfConstraints.add(atomicConstraint);

			}
		}

		return listOfConstraints;
	}

	/**
	 * This method generates a conjunction of Constraints.
	 */
	private Formula<AtomicConstraint> generateConjunctionConstraint(Collection<Formula<AtomicConstraint>> argsOfClass) {

		Formula<AtomicConstraint> constraint = null;
		Iterator<Formula<AtomicConstraint>> equiCons = argsOfClass.iterator();
		if (equiCons.hasNext()) {
			constraint = equiCons.next();
		}

		if (!(argsOfClass.size() > 1)) {

			return constraint;

		}

		while (equiCons.hasNext()) {
			Formula<AtomicConstraint> cons = equiCons.next();
			constraint = constraint.and(cons);

		}

		return constraint;

	}

	/**
	 * Creates a disjunction of Constraints.
	 */
	private Formula<AtomicConstraint> generateDisjunctionConstraint(Collection<Formula<AtomicConstraint>> argsOfClass) {

		Formula<AtomicConstraint> constraint = null;

		Iterator<Formula<AtomicConstraint>> cons = argsOfClass.iterator();
		if (cons.hasNext()) {
			constraint = cons.next();
		}
		if (!(argsOfClass.size() > 1)) {

			return constraint;

		}

		while (cons.hasNext()) {
			Formula<AtomicConstraint> elementOfConstraint = cons.next();

			constraint = constraint.or(elementOfConstraint);

		}
		return constraint;
	}

	/**
	 * Generates the elements of a constraint of a reflexive class (conjunction
	 * of equality constraints).
	 */
	private ArrayList<Formula<AtomicConstraint>> generateElementsOfReflexiveConstraint(Term[] args) {
		HashSet<EqualityConstraint> setOfEqualityConstraints = new HashSet<EqualityConstraint>();
		Collection<Formula<AtomicConstraint>> listOfEqualityContraints = new ArrayList<Formula<AtomicConstraint>>();
		ArrayList<Formula<AtomicConstraint>> listOfConstraints = new ArrayList<Formula<AtomicConstraint>>();

		/*
		 * Creates an EqualityConstraint using the variables of the query and constants of the generated conditionals
		 * if the artity of the predicate is 1 then create an InequalityConstraint, else 
		 */

		for (int i = 0; i < args.length; i++) {
			Variable var1 = new Variable(args[i].toString(), args[i].getType());
			for (int j = i + 1; j < args.length; j++) {
				Variable var2 = new Variable(args[j].toString(), args[j].getType());

				EqualityConstraint atomicConstraint = new EqualityConstraint(var1, var2);
				setOfEqualityConstraints.add(atomicConstraint);
			}
		}
		listOfEqualityContraints.addAll(setOfEqualityConstraints);

		if (listOfEqualityContraints.size() > 1) {
			Formula<AtomicConstraint> conjunction = generateConjunctionConstraint(listOfEqualityContraints);
			listOfConstraints.add(conjunction);
		} else
			listOfConstraints = (ArrayList<Formula<AtomicConstraint>>) listOfEqualityContraints;

		return listOfConstraints;

	}

	/**
	 * Generates a constraint of a not reflexive class (conjunction of
	 * inequality constraints) if there is a reflexive class.
	 */
	private ArrayList<Formula<AtomicConstraint>> generateElementsOfReflexiveNegativeConstraint(Term[] args) {
		HashSet<InequalityConstraint> setOfEqualityConstraints = new HashSet<InequalityConstraint>();
		Collection<Formula<AtomicConstraint>> listOfEqualityContraints = new ArrayList<Formula<AtomicConstraint>>();
		ArrayList<Formula<AtomicConstraint>> listOfConstraints = new ArrayList<Formula<AtomicConstraint>>();

		/**
		 * Creates an InequalityConstraint using the variables of the query and
		 * constants of the generated conditionals if the artity of the
		 * predicate is 1 then create an InequalityConstraint, else
		 */

		for (int i = 0; i < args.length; i++) {
			Variable var1 = new Variable(args[i].toString(), args[i].getType());
			for (int j = i + 1; j < args.length; j++) {
				Variable var2 = new Variable(args[j].toString(), args[j].getType());

				InequalityConstraint atomicConstraint = new InequalityConstraint(var1, var2);
				setOfEqualityConstraints.add(atomicConstraint);
			}
		}
		listOfEqualityContraints.addAll(setOfEqualityConstraints);

		if (listOfEqualityContraints.size() > 1) {
			Formula<AtomicConstraint> conjunction = generateConjunctionConstraint(listOfEqualityContraints);
			listOfConstraints.add(conjunction);
		} else
			listOfConstraints = (ArrayList<Formula<AtomicConstraint>>) listOfEqualityContraints;

		return listOfConstraints;
	}

	//TODO nullstelliges Prädikat abfangen???

	/**
	 * Generates the elements of a negative constraint. (Conjunction of
	 * inequality constraints).
	 */
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

	/**
	 * Computes the probability of the class.
	 * 
	 * @param classification
	 * @return the average probability of the class
	 */
	private Fraction getProbabilitiesOfClass(Collection<RelationalConditional> classification) {

		//The following section contains the correct handling of the probabilities of class, but Log4KR is not able to compute it correctly so the heuristic here used is to return the probabilty of the first conditional of the class

		Fraction sum = new Fraction(0);

		for (Iterator<RelationalConditional> condOfClass = classification.iterator(); condOfClass.hasNext();) {
			RelationalConditional relationalConditional = (RelationalConditional) condOfClass.next();

			Fraction probabilityOfCond = relationalConditional.getProbability();
			sum = Fraction.addition(sum, probabilityOfCond);

		}

		return Fraction.division(sum, new Fraction(classification.size()));

		//return classification.iterator().next().getProbability();
	}

	/**
	 * Creates an EqualityConstraint using the variables of the query and
	 * constants of the generated conditionals.
	 */
	private ArrayList<Formula<AtomicConstraint>> generateElementsOfConstraint(Term[] argsOfCond,
			Term[] argsOfQueryAtom) {

		ArrayList<Formula<AtomicConstraint>> listOfConstraints = new ArrayList<Formula<AtomicConstraint>>();
		ArrayList<Formula<AtomicConstraint>> listOfEqualityConstraints = new ArrayList<Formula<AtomicConstraint>>();

		for (int i = 0; i < argsOfCond.length; i++) {

			Variable var = new Variable(argsOfQueryAtom[i].toString(), argsOfQueryAtom[i].getType());
			Constant cons = new Constant(argsOfCond[i].toString(), argsOfCond[i].getType());
			EqualityConstraint equalConstraint = new EqualityConstraint(var, cons);
			listOfEqualityConstraints.add(equalConstraint);

		}

		Formula<AtomicConstraint> conjunction = generateConjunctionConstraint(listOfEqualityConstraints);
		listOfConstraints.add(conjunction);

		//generate the negative reflexive Constraint

		return listOfConstraints;
	}

	/**
	 * Creates an EqualityConstraint using the variables of the query and
	 * constants of the generated conditionals.
	 */
	private HashSet<Formula<AtomicConstraint>> generateElementsOfFormulaConstraint(Term[] argsOfCond,
			Term[] argsOfQueryAtom) {

		//ArrayList<Formula<AtomicConstraint>> listOfConstraints = new ArrayList<Formula<AtomicConstraint>>();
		HashSet<EqualityConstraint> setOfEqualityConstraints = new HashSet<EqualityConstraint>();
		HashSet<Formula<AtomicConstraint>> setOfFormula = new HashSet<Formula<AtomicConstraint>>();

		for (int i = 0; i < argsOfCond.length; i++) {

			Variable var = new Variable(argsOfQueryAtom[i].toString(), argsOfQueryAtom[i].getType());
			Constant cons = new Constant(argsOfCond[i].toString(), argsOfCond[i].getType());
			EqualityConstraint equalConstraint = new EqualityConstraint(var, cons);

			setOfEqualityConstraints.add(equalConstraint);

		}

		setOfFormula.addAll(setOfEqualityConstraints);

		//return setOfEqualityConstraints;
		return setOfFormula;
	}

	/**
	 * Generates a conditional out of the relationalConditonal of the query, the
	 * generated constraint and the probability.
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

			RelationalFact conditionalOfClass = null;

			if (constraintOfClass != null) {
				conditionalOfClass = new RelationalFact(consequence, probability, constraintOfClass);
			} else {
				conditionalOfClass = new RelationalFact(consequence, probability);
			}
			return conditionalOfClass;
		} else {
			RelationalConditional conditionalOfClass = null;
			Formula<RelationalAtom> antecedence = c.getAntecedence();
			conditionalOfClass = new RelationalConditional(consequence, antecedence, probability, constraintOfClass);
			return conditionalOfClass;
		}

	} //end

	private RelationalConditional generateConditionalForOne(RelationalConditional c, Fraction probability) {

		Formula<RelationalAtom> consequence = c.getConsequence();

		if (c instanceof RelationalFact) {
			RelationalFact conditionalOfClass = null;
			if (c.getConstraint() != null) {
				conditionalOfClass = new RelationalFact(consequence, probability, c.getConstraint());
			} else {
				conditionalOfClass = new RelationalFact(consequence, probability);
			}
			return conditionalOfClass;

		} else {
			Formula<RelationalAtom> antecedence = c.getAntecedence();
			RelationalConditional conditionalOfClass = null;
			if (antecedence instanceof Tautology<?>) {
				if (c.getConstraint() != null) {
					conditionalOfClass = new RelationalFact(consequence, probability, c.getConstraint());
				} else {
					conditionalOfClass = new RelationalFact(consequence, probability);
				}
			} else {
				conditionalOfClass = new RelationalConditional(consequence, antecedence, probability);
			}
			return conditionalOfClass;

		} //end else

	}

	/**
	 * Test if a class is reflexive (i.e. likes(a, a), likes (b, b), likes (c,
	 * c))
	 */
	public boolean isReflexiveAlt(Collection<RelationalConditional> classifiedClass) {

		boolean isreflexive = true;

		//if for each Relational Conditionals the constants are equal and the computed probability= 0.0 then the class is reflexive
		//for instance likes(a,a), likes(b,b), likes(c,c)

		//for each of the conditionals of the class get the atoms 
		//for instance likes(a,a), likes(b,b), likes(c,c)
		for (Iterator<RelationalConditional> iterator = classifiedClass.iterator(); iterator.hasNext();) {
			RelationalConditional relationalConditional = (RelationalConditional) iterator.next();
			Fraction prob = relationalConditional.getProbability();
			Double dprob = prob.toFloatingPoint();
			Collection<Atom<RelationalAtom>> atoms = new ArrayList<Atom<RelationalAtom>>();
			atoms = relationalConditional.getAtoms();
			//for each atom get the terms
			for (Iterator<Atom<RelationalAtom>> iterator2 = atoms.iterator(); iterator2.hasNext();) {
				Atom<RelationalAtom> atom = (Atom<RelationalAtom>) iterator2.next();
				Term[] arguments = ((RelationalAtom) atom).getArguments();
				if (arguments.length == 1) {
					isreflexive = false;
					break;
				}
				for (int i = 0; i < arguments.length - 1; i++) {

					if (!(arguments[0].equals(arguments[i + 1]))) {

						isreflexive = false;

					}

				}
				if (dprob != 0.0 && isreflexive) {
					isreflexive = false;
				}

			}
		}

		/*if ((getProbabilitiesOfClass(classifiedClass)).toFloatingPoint() == 0.0)
			return true;
		else
			return false;
		*/

		return isreflexive;
	}

	/**
	 * Test if a class is reflexive (i.e. likes(a, a), likes (b, b), likes (c,
	 * c))
	 */
	public boolean isReflexive(Collection<RelationalConditional> classifiedClass) {

		boolean isreflexive = false;

		//if for each Relational Conditionals the constants are equal and the computed probability= 0.0 then the class is reflexive
		//for instance likes(a,a), likes(b,b), likes(c,c)

		//for each of the conditionals of the class get the atoms 
		//for instance likes(a,a), likes(b,b), likes(c,c)
		for (Iterator<RelationalConditional> iterator = classifiedClass.iterator(); iterator.hasNext();) {
			RelationalConditional relationalConditional = (RelationalConditional) iterator.next();
			Fraction prob = relationalConditional.getProbability();
			Double dprob = prob.toFloatingPoint();
			Collection<Atom<RelationalAtom>> atoms = new ArrayList<Atom<RelationalAtom>>();
			atoms = relationalConditional.getAtoms();
			//for each atom get the terms
			for (Iterator<Atom<RelationalAtom>> iterator2 = atoms.iterator(); iterator2.hasNext();) {
				Atom<RelationalAtom> atom = (Atom<RelationalAtom>) iterator2.next();
				Term[] arguments = ((RelationalAtom) atom).getArguments();
				if (arguments.length > 1) {
					isreflexive = true;

				}
				for (int i = 0; i < arguments.length - 1; i++) {

					if (!(arguments[0].equals(arguments[i + 1]))) {

						isreflexive = false;

					}

				}
				/*if (dprob != 0.0 && isreflexive) {
					isreflexive = false;
				}*/

			}
		}

		/*if ((getProbabilitiesOfClass(classifiedClass)).toFloatingPoint() == 0.0)
			return true;
		else
			return false;
		*/

		return isreflexive;
	}

	/**
	 * Tests if a class has the probability -1, which means the probability can
	 * not be computed because the marginalisation requires a division by zero.
	 */
	private boolean isImpossible(Collection<RelationalConditional> classifiedClass) {
		if ((getProbabilitiesOfClass(classifiedClass)).toFloatingPoint() == -1)

			return true;
		else
			return false;

	}

	/**
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

	/**
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

	@Override
	public void addGeneralization() {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeGeneralization() {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<Generalization> getChildGeneralizations() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This method can be used to get the specific constants of a knowledgebase.
	 * 
	 * @param conditionalsOfKb
	 * @return the specific constants of the knowledgebase
	 */
	public Collection<Constant> getSpecificConstants(Collection<RelationalConditional> conditionalsOfKb) {

		Collection<Constant> constantsOfKb = new ArrayList<>();

		//gets all the atoms of the kb
		for (Iterator<RelationalConditional> iterator1 = conditionalsOfKb.iterator(); iterator1.hasNext();) {
			RelationalConditional conditional = (RelationalConditional) iterator1.next();

			Collection<Atom<RelationalAtom>> atomsOfCond = new HashSet<>();
			Collection<Atom<AtomicConstraint>> atomsOfConstraint = null;
			Collection<Constant> constantsOfCond = new HashSet<Constant>();
			Collection<Constant> constantsOfConstraint = new HashSet<Constant>();

			//atoms of antecedence and atoms of consequence
			atomsOfCond = conditional.getAtoms();
			atomsOfCond.addAll(atomsOfCond);

			//get the specific constants of the conditional
			for (Iterator<Atom<RelationalAtom>> iterator2 = atomsOfCond.iterator(); iterator2.hasNext();) {
				Atom<RelationalAtom> atomOfCond = (Atom<RelationalAtom>) iterator2.next();
				constantsOfCond = ((RelationalAtom) atomOfCond).getConstants();
				constantsOfKb.addAll(constantsOfCond);
			}

			//atoms of constraint
			if (conditional.getConstraint() != null) {
				atomsOfConstraint = conditional.getConstraint().getAtoms();
			}

			if (atomsOfConstraint != null) {
				atomsOfConstraint.addAll(atomsOfConstraint);
			}

			//get the specific constants of the constraint
			if (atomsOfConstraint != null) {
				for (Iterator<Atom<AtomicConstraint>> iterator3 = atomsOfConstraint.iterator(); iterator3.hasNext();) {
					Atom<AtomicConstraint> atomOfConstraint = (Atom<AtomicConstraint>) iterator3.next();

					Term t1 = ((AtomicConstraint) atomOfConstraint).getT1();
					Term t2 = ((AtomicConstraint) atomOfConstraint).getT2();

					if (t1 instanceof Constant) {
						constantsOfKb.add((Constant) t1);
					}
					if (t2 instanceof Constant) {
						constantsOfKb.add((Constant) t1);
					}
				}
			}

			/*Atom<AtomicConstraint> constraint = (Atom<AtomicConstraint>) conditional.getConstraint();
			if (constraint != null) {
			
				Term t1 = ((AtomicConstraint) constraint).getT1();
				Term t2 = ((AtomicConstraint) constraint).getT2();
			
				if (t1 instanceof Constant) {
					constantsOfKb.add((Constant) t1);
				}
				if (t2 instanceof Constant) {
					constantsOfKb.add((Constant) t1);
				}
			}*/
		}

		return constantsOfKb;
	}

}//end generalize
