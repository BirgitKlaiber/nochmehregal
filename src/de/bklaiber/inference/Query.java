package de.bklaiber.inference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.bklaiber.printProbabilities.PrintAllEqualProbabilities;
import de.bklaiber.printProbabilities.PrintNoEqualProbabilities;
import de.bklaiber.printProbabilities.PrintRelationalWithConstraint;
import de.bklaiber.printProbabilities.PrintSingleException;
import de.bklaiber.printProbabilities.PrintWithEquality;
import de.bklaiber.printProbabilities.PrintWithInequality;
import de.bklaiber.types.ConstantSet;
import de.bklaiber.types.ProbabilityConditional;
import edu.cs.ai.log4KR.logical.semantics.Interpretation;
import edu.cs.ai.log4KR.logical.semantics.PossibleWorldFactory;
import edu.cs.ai.log4KR.logical.syntax.Atom;
import edu.cs.ai.log4KR.logical.syntax.Formula;
import edu.cs.ai.log4KR.relational.classicalLogic.grounding.ConstraintBasedGroundingOperator;
import edu.cs.ai.log4KR.relational.classicalLogic.grounding.GroundingOperator;
import edu.cs.ai.log4KR.relational.classicalLogic.semantics.RelationalPossibleWorldMapRepresentationFactory;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.RelationalAtom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Constant;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.kbParser.log4KRReader.Log4KRReader;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalFact;
import edu.cs.ai.log4KR.relational.util.RelationalUtils;
import edu.cs.ai.log4KR.structuredLogics.GroundingSemantics;
import edu.cs.ai.log4KR.structuredLogics.reasoning.RelationalOptimumEntropyEpistemicStateLBFGS;
import edu.cs.ai.log4KR.structuredLogics.satisfiability.SatisfiabilityTestOjAlgo;

/**
 * 
 * 
 * This class is necessary to query the kb. It contains the main method.
 * 
 * @author klaiber
 *
 * 
 *
 */
public class Query {

	private static String file;
	double probability;
	int probCondsSize;

	/**
	 * Constructor of Query.
	 * 
	 * @param file
	 */
	public Query(String file) {
		super();
		Query.file = file;
		probability = -1;
		probCondsSize = 0;

	}

	/**
	 * This is the method to do queries.
	 */
	public void query() {
		String spacebetween = new String();

		// get the kb and the queries from a file

		Log4KRReader reader = new Log4KRReader();
		reader.read(file);

		Map<String, Collection<RelationalConditional>> allKnowledgeBases = reader.getKnowledgeBases();
		Set<Entry<String, Collection<RelationalConditional>>> knowledgebases = allKnowledgeBases.entrySet();
		Collection<RelationalConditional> knowledgebase = reader.getKnowledgeBase("kb");
		// Collection<Variable> variables = reader.getVariables();
		Collection<Constant> constants = reader.getConstants();
		// Collection<Predicate> predicates = reader.getPredicates();

		// ground the kb and the queries and compute an epistemic state out of
		// the possible worlds
		GroundingOperator gop = new ConstraintBasedGroundingOperator();
		GroundingSemantics semantics = new GroundingSemantics(gop, constants);
		RelationalOptimumEntropyEpistemicStateLBFGS epState = new RelationalOptimumEntropyEpistemicStateLBFGS(
				semantics);

		PossibleWorldFactory<RelationalAtom> worldFactory = new RelationalPossibleWorldMapRepresentationFactory();

		Interpretation<RelationalAtom>[] possibleWorlds = worldFactory
				.createPossibleWorlds(RelationalUtils.getAtomsFromKnowledgeBase(knowledgebase, constants, gop));

		epState.initialize(possibleWorlds, knowledgebase);

		SatisfiabilityTestOjAlgo<RelationalAtom> satTest = new SatisfiabilityTestOjAlgo<>(possibleWorlds, semantics);

		/*
		 * System.out.println("Is consistent? " +
		 * satTest.isSatisfiable(knowledgebase));
		 */

		Collection<RelationalConditional> groundKb = gop.groundKnowledgeBase(knowledgebase, constants);

		System.out.println("Grundkonditionale der Wissensbasis: ");

		for (RelationalConditional relationalgroundConditional : groundKb) {

			/*
			 * System.out.println("Grundkonditionale der WB: " +
			 * relationalgroundConditional.toString());
			 */

			System.out.println(relationalgroundConditional.toString());
		}

		// System.out.println("Größe WB: " + knowledgebases.size());

		for (int i = 1; i < knowledgebases.size(); i++) {
			// every query is in a knowledgebase
			Collection<RelationalConditional> queryCond = reader.getKnowledgeBase("query" + i);

			Collection<RelationalConditional> groundKnowledgeBase = gop.groundKnowledgeBase(queryCond, constants);

			ArrayList<ProbabilityConditional> probabilityConditionals = new ArrayList<ProbabilityConditional>();
			ArrayList<RelationalConditional> relationalConditionals = new ArrayList<RelationalConditional>();
			int numberOfEquiClasses = 0;

			//System.out.println("");
			// System.out.println("Konditional mit Wahrscheinlichkeiten:");

			for (RelationalConditional relationalConditional : queryCond) {

				System.out.println("Anfrage: " + relationalConditional.toString());

			}
			System.out.println("");
			/*
			 * the probabilities for every conditional are computed, the ground
			 * conditionals are created
			 */

			for (RelationalConditional relationalGroundConditional : groundKnowledgeBase) {

				Formula<RelationalAtom> formulaCons = relationalGroundConditional.getConsequence();
				Formula<RelationalAtom> formAnt = relationalGroundConditional.getAntecedence();

				for (RelationalConditional relationalConditional : queryCond) {
					relationalConditionals.add(relationalConditional);

				}

				probability = epState.queryConditionalProbability(formulaCons, formAnt);

				ProbabilityConditional probabilityConditional = new ProbabilityConditional(probability,
						relationalGroundConditional);
				probabilityConditional.setRelationalConditional(relationalConditionals.get(0));

				probabilityConditionals.add(probabilityConditional);

			} // endfor

			/*
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter("query.csv"));
			
				writer.format("%f", (epState.getDist().getProbabilityVector()));
			
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			//epState.printCurrentProbabilities();

			//System.out.println("");

			probCondsSize = probabilityConditionals.size();
			Collections.sort(probabilityConditionals);
			//double compareTo = roundScale(probabilityConditionals.get(0).getProbability());
			double compareTo = probabilityConditionals.get(0).getProbability();

			ArrayList<ProbabilityConditional> listOfEqualProbs = new ArrayList<ProbabilityConditional>();
			Set<ArrayList<ProbabilityConditional>> setOfEquiClass = new HashSet<ArrayList<ProbabilityConditional>>();
			ConstantSet setOfConstantOfClass = new ConstantSet();
			// Set<Constant> intersectionConstants = new HashSet<Constant>();
			ArrayList<ConstantSet> listOfSetOfConstants = new ArrayList<ConstantSet>();
			Collection<Constant> collectionOfConstants = null;
			Collection<Constant> constantsOfAtom = null;
			RelationalConditional relationalGroundCond = null;
			// boolean isNull = false;

			listOfEqualProbs.add(probabilityConditionals.get(0));

			for (int k = 1; k < probCondsSize; k++) {
				ProbabilityConditional nextCond = probabilityConditionals.get(k);
				// all conditionals with equal probabilities in there own list

				//if (roundScale(nextCond.getProbability()) == compareTo) {
				if ((nextCond.getProbability()) == compareTo) {

					listOfEqualProbs.add(nextCond);
				}
				// if the probability is greater
				else {
					setOfEquiClass.add(listOfEqualProbs);
					listOfEqualProbs = new ArrayList<ProbabilityConditional>();
					listOfEqualProbs.add(nextCond);
					//compareTo = roundScale(nextCond.getProbability());
					compareTo = nextCond.getProbability();
				} // endelse

			} // endfor
			setOfEquiClass.add(listOfEqualProbs);
			numberOfEquiClasses = setOfEquiClass.size();

			// all constants of the class of equal probabilities are collected
			for (ArrayList<ProbabilityConditional> probabilityConditionalList : setOfEquiClass) {

				setOfConstantOfClass = new ConstantSet();

				for (ProbabilityConditional probabilityConditional : probabilityConditionalList)

				{
					ProbabilityConditional probCond = probabilityConditional;

					/*
					 * ((ConstantSet) setOfConstantOfClass)
					 * .setProbabilityConditional(probCond);
					 * 
					 * setOfConstantOfClass = ((ConstantSet)
					 * setOfConstantOfClass).generateConstantSet();
					 */

					(setOfConstantOfClass).setProbabilityConditional(probCond);

					Collection<Atom<RelationalAtom>> atomsOfClass = probabilityConditional
							.getRelationalGroundConditional().getAtoms();

					for (Atom<RelationalAtom> atom : atomsOfClass) {
						RelationalAtom relationalAtom = (RelationalAtom) atom;
						collectionOfConstants = relationalAtom.getConstants();
						for (Constant constant : collectionOfConstants) {
							setOfConstantOfClass.add(constant);

						} // endfor

					} // endfor
						// }

				} // endfor
				double prob = (setOfConstantOfClass).getProbabilityConditional().getProbability();
				//prob = roundScale(prob);

				if (prob != 0 && prob != -1) {
					// System.out.println("Prob: " + prob);
					listOfSetOfConstants.add(setOfConstantOfClass);
				}

			} // endfor

			// convert the HashSet in a ArrayList to make it possible to get
			// single elements
			Set<ArrayList<ProbabilityConditional>> set = new HashSet<ArrayList<ProbabilityConditional>>();
			for (ArrayList<ProbabilityConditional> probabilityConditionalList : setOfEquiClass) {
				set.add(probabilityConditionalList);

			}

			List<ArrayList<ProbabilityConditional>> listOfLists = new ArrayList<ArrayList<ProbabilityConditional>>(set);
			ArrayList<ProbabilityConditional> probabilityList1 = listOfLists.get(0);

			// here is only one List; that means all ground conditionals have
			// the
			// same probability
			if (numberOfEquiClasses == 1) {

				System.out.println("Berechnung der Wahrscheinlichkeiten der Grundkonditionale: ");
				for (ArrayList<ProbabilityConditional> probabilityList : listOfLists) {
					// System.out.println("zum Vergleich");

					for (ProbabilityConditional probabilityConditional : (ArrayList<ProbabilityConditional>) probabilityList) {

						//System.out.println(probabilityConditional.getRelationalGroundConditional().toString() + "["
						//	+ roundScale(probabilityConditional.getProbability()) + "]");
						System.out.println(probabilityConditional.getRelationalGroundConditional().toString() + "["
								+ probabilityConditional.getProbability() + "]");

					} // endfor
				}
				System.out.println("");
				System.out.println("Erwartete Ausgabe: ");
				System.out.println("Hier sind die Wahrscheinlichkeiten des Konditionals alle gleich: ");
				PrintAllEqualProbabilities printAllProbs1 = new PrintAllEqualProbabilities(probabilityList1);
				System.out.println(printAllProbs1.toString());
			}

			// here there is more than one list
			else {
				System.out.println("Berechnung der Wahrscheinlichkeiten der Grundkonditionale: ");
				for (ArrayList<ProbabilityConditional> probabilityList : listOfLists) {
					// System.out.println("zum Vergleich");

					for (ProbabilityConditional probabilityConditional : (ArrayList<ProbabilityConditional>) probabilityList) {

						//System.out.println(probabilityConditional.getRelationalGroundConditional().toString() + "["
						//	+ roundScale(probabilityConditional.getProbability()) + "]");
						System.out.println(probabilityConditional.getRelationalGroundConditional().toString() + "["
								+ (probabilityConditional.getProbability()) + "]");

					} // endfor
						// System.out.println("Ende Vergleich");
						// System.out.println("xxx");

				}

				ArrayList<ProbabilityConditional> probabilityList2 = listOfLists.get(1);

				// when there are two lists
				if (numberOfEquiClasses == 2) {

					int sizeOfList1 = probabilityList1.size();
					int sizeOfList2 = probabilityList2.size();

					// if both lists have only one ground conditional
					if (sizeOfList1 == 1 && sizeOfList2 == 1) {
						System.out.println("");
						System.out.println("Erwartete Ausgabe:");

						for (int j = 0; j < listOfLists.size(); j++) {

							relationalGroundCond = listOfLists.get(j).get(0).getRelationalGroundConditional();
							Collection<Atom<RelationalAtom>> relAtomList = null;

							// if it is a fact
							if (relationalGroundCond instanceof RelationalFact) {
								relAtomList = relationalGroundCond.getConsequence().getAtoms();

							} else {

								relAtomList = relationalGroundCond.getAntecedence().getAtoms();

							}

							for (Atom<RelationalAtom> atom : relAtomList) {
								RelationalAtom relationalAtom = (RelationalAtom) atom;
								constantsOfAtom = relationalAtom.getConstants();

							}
							PrintSingleException exception = new PrintSingleException(listOfLists.get(j));
							System.out.println(exception.toString());

						} // endfor

					}
					// when one list contains more than one conditional and the
					// other only one conditional
					else {
						if (sizeOfList1 == 1) {
							relationalGroundCond = probabilityList1.get(0).getRelationalGroundConditional();
							Collection<Atom<RelationalAtom>> relAtomList = null;

							// if it is a fact
							if (relationalGroundCond instanceof RelationalFact) {
								relAtomList = relationalGroundCond.getConsequence().getAtoms();

							} else {

								relAtomList = relationalGroundCond.getAntecedence().getAtoms();

							}

							for (Atom<RelationalAtom> atom : relAtomList) {
								RelationalAtom relationalAtom = (RelationalAtom) atom;
								constantsOfAtom = relationalAtom.getConstants();

							}
							PrintSingleException exception = new PrintSingleException(probabilityList1);
							System.out.println("");
							System.out.println("Erwartete Ausgabe:");
							System.out.println(exception.toString());
							PrintWithInequality printWithSingleEx = new PrintWithInequality(probabilityList2,
									constantsOfAtom);
							System.out.println(printWithSingleEx.toString());

						}

						if (sizeOfList2 == 1) {

							relationalGroundCond = probabilityList2.get(0).getRelationalGroundConditional();
							Collection<Atom<RelationalAtom>> relAtomList = null;

							// if it is a fact
							if (relationalGroundCond instanceof RelationalFact) {
								relAtomList = relationalGroundCond.getConsequence().getAtoms();

							} else {

								relAtomList = relationalGroundCond.getAntecedence().getAtoms();

							}

							for (Atom<RelationalAtom> atom : relAtomList) {
								RelationalAtom relationalAtom = (RelationalAtom) atom;
								constantsOfAtom = relationalAtom.getConstants();

							}
							PrintSingleException exception = new PrintSingleException(probabilityList2);

							System.out.println(exception.toString());

							PrintWithInequality printWithSingleEx = new PrintWithInequality(probabilityList1,
									constantsOfAtom);
							System.out.println(printWithSingleEx.toString());

						}

						// default fuer zwei Listen mit je mehr als einem
						// Element
						// bis Loesung gefunden

						if (sizeOfList1 > 1 && sizeOfList2 > 1) {

							// System.out.println("blabla");

							/*
							 * // hier wird die Schnittmenge der Konstanten der
							 * // Konditionalen der verschiedenen Klassen
							 * gebildet for (Set<Constant> constantSet1 :
							 * listOfSetOfConstants) {
							 * 
							 * for (Set<Constant> constantSet2 :
							 * listOfSetOfConstants) {
							 * constantSet1.removeAll(constantSet2);
							 * 
							 * } }
							 */
							// here we look at the cases, where both of the
							// conditionals have the probatility zero, - 1 or
							// 1(reflexive knowlede)

							ArrayList<ProbabilityConditional> list1 = listOfLists.get(0);
							ArrayList<ProbabilityConditional> list2 = listOfLists.get(1);

							ProbabilityConditional probCond1 = list1.get(0);
							ProbabilityConditional probCond2 = list2.get(0);

							PrintRelationalWithConstraint prwithC1 = new PrintRelationalWithConstraint(probCond1);
							PrintRelationalWithConstraint prwithC2 = new PrintRelationalWithConstraint(probCond2);

							// when the first of both conditionals has the
							// probability zero, -1 or 1
							if (probCond1.getProbability() == 0 || probCond1.getProbability() == -1
									|| probCond1.getProbability() == 1) {
								// when the conditional has the probability -1

								if (probCond1.getProbability() == -1) {
									prwithC1.setPossible(false);
								}
								prwithC2.setEqual(false);

								// when the conditional has the probability zero
								// or 1
								if (probCond1.getProbability() == 0) {
									prwithC1.setPossible(true);
									prwithC1.setNull(true);
								}

								System.out.println(prwithC1.toString());
								System.out.println(prwithC2.toString());

							} // endif

							else {
								// if the second conditional has the probability
								// Zero or - 1 probably reflexive knowledge)

								if (probCond2.getProbability() == 0 || probCond2.getProbability() == -1
										|| probCond2.getProbability() == 1) {
									if (probCond2.getProbability() == -1) {
										prwithC2.setPossible(false);
									}
									prwithC1.setEqual(false);

									if (probCond2.getProbability() == 0) {
										prwithC2.setNull(true);
									}

									if (probCond2.getProbability() == 1) {
										// prwithC2.setNull(true);
									}

									System.out.println(prwithC1.toString());
									System.out.println(prwithC2.toString());

								} // endif

								// none of the both conditionas has zero
								// probability
								else {
									System.out.println("else");
									System.out.println("toDo ");
									System.out.println("prob1: " + probCond1.getProbability());
									System.out.println("prob2: " + probCond2.getProbability());
									System.out.println("zum Vergleich ");
									PrintNoEqualProbabilities paep1 = new PrintNoEqualProbabilities(list1);
									PrintNoEqualProbabilities paep2 = new PrintNoEqualProbabilities(list2);
									System.out.println(paep1.toString());
									System.out.println(paep2.toString());
									//

								}
							}

						} // endif(two lists each with more than one conditional)

					} // endelse

				} // endifnumberofClasses=2

				// here there are more than two lists
				else {

					// if every contional has its own probability
					if (numberOfEquiClasses == probabilityConditionals.size()) {
						System.out.println("");
						System.out.println("Erwartete Ausgabe:");

						PrintNoEqualProbabilities printDifferentConditionals = new PrintNoEqualProbabilities(
								probabilityConditionals);
						System.out.println(printDifferentConditionals.toString());

					} else {
						boolean consNull = false;
						boolean andNull = false;
						List<ArrayList<ProbabilityConditional>> newListOfLists = new ArrayList<ArrayList<ProbabilityConditional>>();
						for (int j = 0; j < listOfLists.size(); j++) {

							ArrayList<ProbabilityConditional> nextlist = listOfLists.get(j);
							ProbabilityConditional probabilityCond = nextlist.get(0);
							PrintRelationalWithConstraint prwithC = new PrintRelationalWithConstraint(probabilityCond);
							System.out.println("hier gibt es einen Fehler!!!");
							// look at the border cases, where the consequence
							// or
							// the antecedence have the probability zero
							if (probabilityCond.getProbability() == 0 || probabilityCond.getProbability() == -1) {
								// here the probability of the antecedence is
								// zero
								if (probabilityCond.getProbability() == -1) {
									andNull = true;
									prwithC.setPossible(false);

								}
								// here the probability of the consequence is
								// zero
								if (probabilityCond.getProbability() == 0) {
									prwithC.setPossible(true);
									consNull = true;
									prwithC.setNull(true);

								}

								System.out.println(prwithC.toString());

							}
							/*
							 * delete all conditionals with probability zero or
							 * -1; i.e. create a new list with conditionals that
							 * have a probability unequal to zero or -1
							 */
							else {
								newListOfLists.add(nextlist);
							} // endelse

						} // endfor

						ConstantSet constantSet1 = listOfSetOfConstants.get(0);
						ConstantSet constantsIneq = null;
						ConstantSet constantsEq = null;
						ConstantSet constantsToPrint1 = null;
						ConstantSet constantsToPrint2 = null;

						// if all conditionals have the same amount of constants
						if (listOfSetOfConstants.size() == 1) {
							// if the new list only contains one conditional
							if (newListOfLists.size() == 1) {
								PrintRelationalWithConstraint printRWC = new PrintRelationalWithConstraint(
										newListOfLists.get(0).get(0));
								printRWC.setPossible(true);
								printRWC.setEqual(false);

								/*
								 * //if there were conditionals with the
								 * probability null as well as conditionals with
								 * the probability -1
								 */

								if (consNull && andNull) {
									System.out.println(printRWC.toString());

								} else {
									// if there were conditionals with zero
									// probability
									if (consNull) {
										printRWC.setNull(true);
										System.out.println(printRWC.toString());

									} else {
										/*
										 * //if there are conditionals with the
										 * probability -1 (what means that the
										 * antecedence has the probability null)
										 */
										if (andNull) {
											printRWC.setAnt(true);
											System.out.println(printRWC.toString());

										} // endif
									} // endelse
								} // endelse
							} // endif
							else {
								System.out.println("TODO3");
								// hier muss man die Konditionale betrachten
								// sind die Konstanten in den Atomen der
								// Praemisse und der Konsquenz gleich? Wenn ja
								// InEqualityConstraint und ???

							}
						}

						for (int k = 1; k < listOfSetOfConstants.size(); k++) {
							ConstantSet constantSet2 = listOfSetOfConstants.get(k);
							// if one set contains more elements
							if (constantSet1.size() > constantSet2.size()) {
								constantsToPrint1 = constantSet2;
								constantsToPrint2 = constantSet1;
								constantSet1.removeAll(constantSet2);
								constantsIneq = constantSet1;
								constantsEq = constantSet1;
								System.out.println("bla1");
							} else {
								if (constantSet1.size() < constantSet2.size()) {
									constantsToPrint1 = constantSet1;
									constantsToPrint2 = constantSet2;
									constantSet2.removeAll(constantSet1);
									constantsIneq = constantSet2;
									constantsEq = constantSet2;
									System.out.println("bla2");
								}

								// wenn die Konstantenmengen sich in der Anzahl
								// nicht
								// unterscheiden, trotzdem unterschiedliche
								// Wahrscheinlichkeiten ermittelt wurde

								else {
									System.out.println("TODO2");
									// System.out.println("groesse neue Liste: "
									// + newListOfLists.size());
									ProbabilityConditional prob1 = constantSet1.getProbabilityConditional();
									ProbabilityConditional prob2 = constantSet2.getProbabilityConditional();

									// here the constants of the atoms of
									// antecedence and consequence are compared

									RelationalConditional relCond1 = prob1.getRelationalGroundConditional();
									Collection<Atom<RelationalAtom>> atomsAnt1 = relCond1.getAntecedence().getAtoms();
									Collection<Atom<RelationalAtom>> atomsCons1 = relCond1.getConsequence().getAtoms();
									ConstantSet setOfConstantsOfAnt1 = new ConstantSet();
									ConstantSet setOfConstantsOfCons1 = new ConstantSet();

									setOfConstantsOfAnt1.setProbabilityConditional(prob1);
									setOfConstantsOfCons1.setProbabilityConditional(prob1);

									// constants of the antecendence
									for (Atom<RelationalAtom> atom : atomsAnt1) {
										RelationalAtom relationalAtom = (RelationalAtom) atom;
										collectionOfConstants = relationalAtom.getConstants();
										for (Constant constant : collectionOfConstants) {
											setOfConstantsOfAnt1.add(constant);
										} // endfor

									} // endfor

									// constants of the consequence
									for (Atom<RelationalAtom> atom : atomsCons1) {
										RelationalAtom relationalAtom = (RelationalAtom) atom;
										collectionOfConstants = relationalAtom.getConstants();
										for (Constant constant : collectionOfConstants) {
											setOfConstantsOfCons1.add(constant);
										} // endfor

									} // endfor

									/*
									 * //here antecendence and consequence
									 * contain the same constants
									 * setOfConstantsOfAnt1
									 * .removeAll(setOfConstantsOfCons1);
									 * 
									 * if(!setOfConstantsOfAnt1.isEmpty()) {
									 */
									/*
									 * System.out.println("TODO41");
									 * PrintRelationalWithConstraint print = new
									 * PrintRelationalWithConstraint(prob1);
									 * print.setEqual(false);
									 * System.out.println(print.toString());
									 * 
									 * 
									 * } else { System.out.println("TODO51");
									 * PrintRelationalWithConstraint print = new
									 * PrintRelationalWithConstraint(prob1);
									 * print.setEqual(false);
									 * print.setAnt(true); print.setCons(true);
									 * System.out.println(print.toString());
									 * 
									 * }
									 */

									RelationalConditional relCond2 = prob2.getRelationalGroundConditional();
									Collection<Atom<RelationalAtom>> atomsAnt2 = relCond2.getAntecedence().getAtoms();
									Collection<Atom<RelationalAtom>> atomsCons2 = relCond2.getConsequence().getAtoms();

									ConstantSet setOfConstantsOfAnt2 = new ConstantSet();
									ConstantSet setOfConstantsOfCons2 = new ConstantSet();
									setOfConstantsOfAnt2.setProbabilityConditional(prob2);
									setOfConstantsOfCons2.setProbabilityConditional(prob2);

									// Konstanten der Antezendenz
									for (Atom<RelationalAtom> atom : atomsAnt2) {
										RelationalAtom relationalAtom = (RelationalAtom) atom;
										collectionOfConstants = relationalAtom.getConstants();
										for (Constant constant : collectionOfConstants) {
											setOfConstantsOfAnt2.add(constant);

										} // endfor

									} // endfor

									// Konstanten der Konsequenz
									for (Atom<RelationalAtom> atom : atomsCons2) {
										RelationalAtom relationalAtom = (RelationalAtom) atom;
										collectionOfConstants = relationalAtom.getConstants();
										for (Constant constant : collectionOfConstants) {
											setOfConstantsOfCons2.add(constant);

										} // endfor

									} // endfor

									/*
									 * setOfConstantsOfAnt2.removeAll(
									 * setOfConstantsOfCons2);
									 * if(!setOfConstantsOfAnt2.isEmpty()) {
									 * System.out.println("TODO42");
									 * if(setOfConstantsOfAnt2.size() == 1) {
									 * PrintWithInequality printWIn = new
									 * PrintWithInequality(prob2,
									 * setOfConstantsOfAnt2); System.out
									 * .println(printWIn.toString()); } else {
									 * PrintRelationalWithConstraint print = new
									 * PrintRelationalWithConstraint(prob2);
									 * print.setEqual(false);
									 * System.out.println(print.toString()); }
									 * 
									 * } else {
									 * 
									 * 
									 * PrintRelationalWithConstraint print = new
									 * PrintRelationalWithConstraint(prob2);
									 * print.setEqual(false);
									 * print.setAnt(true); print.setCons(true);
									 * System.out.println("TODO52");
									 * System.out.println(print.toString()); }
									 */

								} // endelse
							} // endelse

							if (constantsIneq != null) {
								PrintWithInequality printWithInEq = new PrintWithInequality(
										constantsToPrint1.getProbabilityConditional(), constantsIneq);
								System.out.println(printWithInEq.toString());

							}

							if (constantsEq != null) {
								PrintWithEquality printWithEq = new PrintWithEquality(
										constantsToPrint2.getProbabilityConditional(), constantsEq);
								System.out.println(printWithEq.toString());

							}

						}
						/*
						 * for (ArrayList<ProbabilityConditional>
						 * probabilityConditionalList : listOfLists) {
						 * System.out.println("zum Vergleich"); for
						 * (ProbabilityConditional probabilityConditional :
						 * (ArrayList<ProbabilityConditional>)
						 * probabilityConditionalList) {
						 * 
						 * System.out.println(probabilityConditional
						 * .getRelationalGroundConditional() .toString() + "[" +
						 * roundScale(probabilityConditional .getProbability())
						 * + "]");
						 * 
						 * }// endfor
						 * 
						 * }// endfor
						 *//*
							 * for (ArrayList<ProbabilityConditional>
							 * probabilityConditionalList : listOfLists) { for
							 * (ProbabilityConditional probCond :
							 * probabilityConditionalList) {
							 * probCond.getRelationalGroundConditional()
							 * .getAtoms(); }
							 * 
							 * }// endfor
							 */
					} // endelse
				} // endelse

			} // endelse

			System.out.println(spacebetween);

		} // endfor

	}// endofquery

	/**
	 * 
	 * In this method doubles were rounded.
	 * 
	 * @param d
	 * @return
	 */
	public static double roundScale(double d) {
		return Math.rint(d * 1000) / 1000;
	}// endofroundscale

}// endofQueryExample
