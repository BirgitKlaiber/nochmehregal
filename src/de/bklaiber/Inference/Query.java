package de.bklaiber.Inference;

import java.util.*;
import java.util.Map.Entry;

import de.bklaiber.PrintProbabilities.*;
import de.bklaiber.Types.ConstantSet;
import de.bklaiber.Types.ProbabilityConditional;
import edu.cs.ai.log4KR.logical.semantics.Interpretation;
import edu.cs.ai.log4KR.logical.semantics.PossibleWorldFactory;
import edu.cs.ai.log4KR.logical.syntax.Atom;
import edu.cs.ai.log4KR.logical.syntax.Formula;
import edu.cs.ai.log4KR.relational.classicalLogic.grounding.ConstraintBasedGroundingOperator;
import edu.cs.ai.log4KR.relational.classicalLogic.grounding.GroundingOperator;
import edu.cs.ai.log4KR.relational.classicalLogic.semantics.RelationalPossibleWorldMapRepresentationFactory;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.RelationalAtom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.*;
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
 * @author birgit klaiber
 *
 * 
 *
 */
public class Query
{

	private static String file;
	double probability;
	int probCondsSize;

	/**
	 * This is the constructor of Query. 
	 * @param file
	 */
	public Query(String file)
	{
		super();
		Query.file = file;
		probability = -1;
		probCondsSize = 0;

	}

	/**
	 * 
	 */
	public void query()
	{
		String spacebetween = new String();

		Log4KRReader reader = new Log4KRReader();
		reader.read(file);

		Map<String, Collection<RelationalConditional>> allKnowledgeBases = reader
				.getKnowledgeBases();
		Set<Entry<String, Collection<RelationalConditional>>> knowledgebases = allKnowledgeBases
				.entrySet();
		Collection<RelationalConditional> knowledgebase = reader
				.getKnowledgeBase("kb");
		// Collection<Variable> variables = reader.getVariables();
		Collection<Constant> constants = reader.getConstants();
		// Collection<Predicate> predicates = reader.getPredicates();

		GroundingOperator gop = new ConstraintBasedGroundingOperator();
		GroundingSemantics semantics = new GroundingSemantics(gop, constants);
		RelationalOptimumEntropyEpistemicStateLBFGS epState = new RelationalOptimumEntropyEpistemicStateLBFGS(
				semantics);

		PossibleWorldFactory<RelationalAtom> worldFactory = new RelationalPossibleWorldMapRepresentationFactory();

		Interpretation<RelationalAtom>[] possibleWorlds = worldFactory
				.createPossibleWorlds(RelationalUtils
						.getAtomsFromKnowledgeBase(knowledgebase, constants,
								gop));

		epState.initialize(possibleWorlds, knowledgebase);

		SatisfiabilityTestOjAlgo<RelationalAtom> satTest = new SatisfiabilityTestOjAlgo<>(
				possibleWorlds, semantics);
		System.out.println("Is consistent? "
				+ satTest.isSatisfiable(knowledgebase));

		for (int i = 1; i < knowledgebases.size(); i++)
		{
			// jede Query ist in einer Wissensbasis
			Collection<RelationalConditional> queryCond = reader
					.getKnowledgeBase("query" + i);

			Collection<RelationalConditional> groundKnowledgeBase = gop
					.groundKnowledgeBase(queryCond, constants);

			ArrayList<ProbabilityConditional> probabilityConditionals = new ArrayList<ProbabilityConditional>();
			ArrayList<RelationalConditional> relationalConditionals = new ArrayList<RelationalConditional>();
			int numberOfEquiClasses = 0;

			System.out.println("Konditional mit Wahrscheinlichkeiten:");

			for (RelationalConditional relationalConditional : queryCond)
			{

				System.out.println("Anfrage: "
						+ relationalConditional.toString());
			}

			// hier werden die Wahrscheinlichkeiten fuer jedes Konditional
			// berechnet, die grundierten Konditionale mit ihrer
			// Wahrscheinlichkeit angelegt
			for (RelationalConditional relationalGroundConditional : groundKnowledgeBase)
			{

				Formula<RelationalAtom> formulaCons = relationalGroundConditional
						.getConsequence();
				Formula<RelationalAtom> formAnt = relationalGroundConditional
						.getAntecedence();

				for (RelationalConditional relationalConditional : queryCond)
				{
					relationalConditionals.add(relationalConditional);

				}

				probability = epState.queryConditionalProbability(formulaCons,
						formAnt);

				ProbabilityConditional probabilityConditional = new ProbabilityConditional(
						probability, relationalGroundConditional);
				probabilityConditional
						.setRelationalConditional(relationalConditionals.get(0));

				probabilityConditionals.add(probabilityConditional);

			}// endfor

			probCondsSize = probabilityConditionals.size();
			Collections.sort(probabilityConditionals);
			double compareTo = roundScale(probabilityConditionals.get(0)
					.getProbability());

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

			for (int k = 1; k < probCondsSize; k++)
			{
				ProbabilityConditional nextCond = probabilityConditionals
						.get(k);
				// gleiche Wahrscheinlichkeit in die Liste einfuegen
				if (roundScale(nextCond.getProbability()) == compareTo)
				{

					listOfEqualProbs.add(nextCond);
				}
				// wenn die Wahrscheinlichkeit groesser ist
				else
				{
					setOfEquiClass.add(listOfEqualProbs);
					listOfEqualProbs = new ArrayList<ProbabilityConditional>();
					listOfEqualProbs.add(nextCond);
					compareTo = roundScale(nextCond.getProbability());
				}// endelse

			}// endfor
			setOfEquiClass.add(listOfEqualProbs);
			numberOfEquiClasses = setOfEquiClass.size();

			// hier werden die Konstanten jeder Aequivalenzklasse gesammelt
			for (ArrayList<ProbabilityConditional> probabilityConditionalList : setOfEquiClass)
			{

				setOfConstantOfClass = new ConstantSet();

				for (ProbabilityConditional probabilityConditional : probabilityConditionalList)

				{
					ProbabilityConditional probCond = probabilityConditional;
					
					
					/*((ConstantSet) setOfConstantOfClass)
					.setProbabilityConditional(probCond);

					setOfConstantOfClass = ((ConstantSet) setOfConstantOfClass).generateConstantSet();
*/					

					(setOfConstantOfClass)
							.setProbabilityConditional(probCond);

					Collection<Atom<RelationalAtom>> atomsOfClass = probabilityConditional
							.getRelationalGroundConditional().getAtoms();

					for (Atom<RelationalAtom> atom : atomsOfClass)
					{
						RelationalAtom relationalAtom = (RelationalAtom) atom;
						collectionOfConstants = relationalAtom.getConstants();
						for (Constant constant : collectionOfConstants)
						{
							setOfConstantOfClass.add(constant);

						}// endfor

					}// endfor
						// }

				}// endfor
				double prob = (setOfConstantOfClass)
						.getProbabilityConditional().getProbability();
				if (prob != 0 && prob != -1)
				{
					listOfSetOfConstants
							.add(setOfConstantOfClass);
				}

			}// endfor

			// das HashSet in eine ArrayList konvertieren, um auf die Elemente
			// zugreifen zu koennen
			Set<ArrayList<ProbabilityConditional>> set = new HashSet<ArrayList<ProbabilityConditional>>();
			for (ArrayList<ProbabilityConditional> probabilityConditionalList : setOfEquiClass)
			{
				set.add(probabilityConditionalList);

			}

			List<ArrayList<ProbabilityConditional>> listOfLists = new ArrayList<ArrayList<ProbabilityConditional>>(
					set);
			ArrayList<ProbabilityConditional> probabilityList1 = listOfLists
					.get(0);

			if (numberOfEquiClasses == 1)
			{
				// hier gibt es nur eine Liste; es haben also alle
				// Grundkonditionale dieselbe Wahrscheinlichkeit

				System.out
						.println("hier sind die Wahrscheinlichkeiten des Konditionals alle gleich: ");
				PrintAllEqualProbabilities printAllProbs1 = new PrintAllEqualProbabilities(
						probabilityList1);
				System.out.println(printAllProbs1.toString());
			}

			// hier gibt es mehrere Listen
			else
			{
				for (ArrayList<ProbabilityConditional> probabilityList : listOfLists)
				{
					System.out.println("zum Vergleich");
					for (ProbabilityConditional probabilityConditional : (ArrayList<ProbabilityConditional>) probabilityList)
					{

						System.out.println(probabilityConditional
								.getRelationalGroundConditional()
								.toString()
								+ "["
								+ roundScale(probabilityConditional
										.getProbability()) + "]");
						
					}// endfor
					System.out.println("Ende Vergleich:");

				}

				ArrayList<ProbabilityConditional> probabilityList2 = listOfLists
						.get(1);

				// wenn es zwei Listen gibt
				if (numberOfEquiClasses == 2)
				{

					int sizeOfList1 = probabilityList1.size();
					int sizeOfList2 = probabilityList2.size();

					// wenn nur beiden Listen nur ein Grundkonditional
					// enthaelt
					if (sizeOfList1 == 1 && sizeOfList2 == 1)
					{

						for (int j = 0; j < listOfLists.size(); j++)
						{

							relationalGroundCond = listOfLists.get(j).get(0)
									.getRelationalGroundConditional();
							Collection<Atom<RelationalAtom>> relAtomList = null;

							// wenn es sich um einen Fakt handelt
							if (relationalGroundCond instanceof RelationalFact)
							{
								relAtomList = relationalGroundCond
										.getConsequence().getAtoms();

							} else
							{

								relAtomList = relationalGroundCond
										.getAntecedence().getAtoms();

							}

							for (Atom<RelationalAtom> atom : relAtomList)
							{
								RelationalAtom relationalAtom = (RelationalAtom) atom;
								constantsOfAtom = relationalAtom.getConstants();

							}
							PrintSingleException exception = new PrintSingleException(
									listOfLists.get(j));
							System.out.println(exception.toString());

						}// endfor

					}
					// wenn nicht beide Listen nur ein Konditional enthalten
					else
					{
						if (sizeOfList1 == 1)
						{
							relationalGroundCond = probabilityList1.get(0)
									.getRelationalGroundConditional();
							Collection<Atom<RelationalAtom>> relAtomList = null;

							// wenn es sich um einen Fakt handelt
							if (relationalGroundCond instanceof RelationalFact)
							{
								relAtomList = relationalGroundCond
										.getConsequence().getAtoms();

							} else
							{

								relAtomList = relationalGroundCond
										.getAntecedence().getAtoms();

							}

							for (Atom<RelationalAtom> atom : relAtomList)
							{
								RelationalAtom relationalAtom = (RelationalAtom) atom;
								constantsOfAtom = relationalAtom.getConstants();

							}
							PrintSingleException exception = new PrintSingleException(
									probabilityList1);
							System.out.println(exception.toString());
							PrintWithInequality printWithSingleEx = new PrintWithInequality(
									probabilityList2, constantsOfAtom);
							System.out.println(printWithSingleEx.toString());

						}

						if (sizeOfList2 == 1)
						{

							relationalGroundCond = probabilityList2.get(0)
									.getRelationalGroundConditional();
							Collection<Atom<RelationalAtom>> relAtomList = null;

							// wenn es sich um einen Fakt handelt
							if (relationalGroundCond instanceof RelationalFact)
							{
								relAtomList = relationalGroundCond
										.getConsequence().getAtoms();

							} else
							{

								relAtomList = relationalGroundCond
										.getAntecedence().getAtoms();

							}

							for (Atom<RelationalAtom> atom : relAtomList)
							{
								RelationalAtom relationalAtom = (RelationalAtom) atom;
								constantsOfAtom = relationalAtom.getConstants();

							}
							PrintSingleException exception = new PrintSingleException(
									probabilityList2);
							System.out.println(exception.toString());
							PrintWithInequality printWithSingleEx = new PrintWithInequality(
									probabilityList1, constantsOfAtom);
							System.out.println(printWithSingleEx.toString());

						}

						// default fuer zwei Listen mit je mehr als einem Element
						// bis Loesung gefunden

						if (sizeOfList1 > 1 && sizeOfList2 > 1)
						{

							// System.out.println("bla");

/*							// hier wird die Schnittmenge der Konstanten der
							// Konditionalen der verschiedenen Klassen gebildet
							for (Set<Constant> constantSet1 : listOfSetOfConstants)
							{

								for (Set<Constant> constantSet2 : listOfSetOfConstants)
								{
									constantSet1.removeAll(constantSet2);

								}
							}
*/
							//hier werden die Faelle behandelt, in denen eines der beiden Konditionale die Wahrscheinlichkeit 0 oder -1 oder 1 hat (reflexives Wissen)
							ArrayList<ProbabilityConditional> list1 = listOfLists
									.get(0);
							ArrayList<ProbabilityConditional> list2 = listOfLists
									.get(1);

							ProbabilityConditional probCond1 = list1.get(0);
							ProbabilityConditional probCond2 = list2.get(0);

							PrintRelationalWithConstraint prwithC1 = new PrintRelationalWithConstraint(
									probCond1);
							PrintRelationalWithConstraint prwithC2 = new PrintRelationalWithConstraint(
									probCond2);

							//wenn das erste der beiden Konditionale die Wahrscheinlichkeit 0 oder -1 oder 1 hat
							if (roundScale(probCond1.getProbability()) == 0
									|| probCond1.getProbability() == -1 || probCond1.getProbability() == 1)
							{
								//wenn das Konditional die Wahrscheinlichkeit -1 hat
								if (probCond1.getProbability() == -1)
								{
									prwithC1.setPossible(false);
								}
								prwithC2.setEqual(false);

								//wenn das Konditional die Wahrscheinlichkeit Null hat oder 1
								if (probCond1.getProbability() == 0 )
								{
									prwithC1.setPossible(true);
									prwithC1.setNull(true);
								}
								
								System.out.println(prwithC1.toString());
								System.out.println(prwithC2.toString());

							} //endif
							
							else
							{
								//wenn das zweite der beiden Konditionale die Wahrscheinlichkeit 0 oder -1 oder 1 hat (reflexives Wissen)
								if (roundScale(probCond2.getProbability()) == 0
										|| probCond2.getProbability() == -1 || probCond2.getProbability() == 1)
								{
									if (probCond2.getProbability() == -1)
									{
										prwithC2.setPossible(false);
									}
									prwithC1.setEqual(false);

									if (probCond2.getProbability() == 0)
									{
										prwithC2.setNull(true);
									}
									
									System.out.println(prwithC1.toString());
									System.out.println(prwithC2.toString());
								}//endif

								//hier hat keines der beiden Konditionale die Wahrscheinlichkeit Null 
								else
								{
									System.out.println("else");
									System.out.println("toDo ");
									System.out.println("prob1: "
											+ probCond1.getProbability());
									System.out.println("prob2: "
											+ probCond2.getProbability());
									System.out.println("zum Vergleich: ");
									PrintNoEqualProbabilities paep1 = new PrintNoEqualProbabilities(
											list1);
									PrintNoEqualProbabilities paep2 = new PrintNoEqualProbabilities(
											list2);
									System.out.println(paep1.toString());
									System.out.println(paep2.toString());
									//hier muss 
									
								}
							}

						}// endif(zwei Listen mit jeweils mehr als einem
							// Konditional)

					}// endelse(wenn nicht beide Listen nur ein Konditional
						// enthalten)

				}// endifnumberofClasses=2

				// hier sind es mehr als zwei Listen
				else
				{

					// wenn es jedes Konditional eine eigene Wahrscheinlichkeit
					// hat
					if (numberOfEquiClasses == probabilityConditionals.size())
					{
						PrintNoEqualProbabilities printDifferentConditionals = new PrintNoEqualProbabilities(
								probabilityConditionals);
						System.out.println(printDifferentConditionals
								.toString());

					} else
					{
						boolean consNull = false;
						boolean andNull = false;
						List<ArrayList<ProbabilityConditional>> newListOfLists = new ArrayList<ArrayList<ProbabilityConditional>>();
						for (int j = 0; j < listOfLists.size(); j++)
						{

							ArrayList<ProbabilityConditional> nextlist = listOfLists
									.get(j);
							ProbabilityConditional probabilityCond = nextlist
									.get(0);
							PrintRelationalWithConstraint prwithC = new PrintRelationalWithConstraint(
									probabilityCond);

							// Randfaelle betrachten, in denen die
							// Wahrscheinlichkeit der Konsequenz oder Antezedenz
							// Null sind
							if (roundScale(probabilityCond.getProbability()) == 0
									|| probabilityCond.getProbability() == -1)
							{
								// hier ist die Wahrscheinlichkeit der
								// Antezedenz Null
								if (probabilityCond.getProbability() == -1)
								{
									andNull = true;
									prwithC.setPossible(false);

								}
								// hier ist die Wahrscheinlichkeit der
								// Konsequenz Null
								if (roundScale(probabilityCond.getProbability()) == 0)
								{
									prwithC.setPossible(true);
									consNull = true;
									prwithC.setNull(true);

								}

								System.out.println(prwithC.toString());

							}
							// alle Konditionale entfernen, die die
							// Wahrscheinlichkeit 0 oder -1 haben
							// also eine neue Liste bilden, die nur die
							// Konditionale mit einer Wahrscheinlichkeit
							// ungleich 0 oder -1 enthaelt
							else
							{
								newListOfLists.add(nextlist);
							}// endelse

						}// endfor

						ConstantSet constantSet1 = listOfSetOfConstants.get(0);
						ConstantSet constantsIneq = null;
						ConstantSet constantsEq = null;
						ConstantSet constantsToPrint1 = null;
						ConstantSet constantsToPrint2 = null;

						// wenn alle Konditionale die gleichen Mengen an
						// Konstanten haben
						if (listOfSetOfConstants.size() == 1)
						{
							//wenn die neue Liste nur ein Konditional enthaelt
							if (newListOfLists.size() == 1)
							{
								PrintRelationalWithConstraint printRWC = new PrintRelationalWithConstraint(
										newListOfLists.get(0).get(0));
								printRWC.setPossible(true);
								printRWC.setEqual(false);

								//wenn es sowohl Konditionale mit der Wahrscheinlichkeit Null, als auch mit der Wahrscheinlichkeit -1 gab
								if (consNull && andNull)
								{
									System.out.println(printRWC.toString());
								} else
								{
									//wenn es Konditionale mit der Wahrscheinlichkeit Null gab
									if (consNull)
									{
										printRWC.setNull(true);
										System.out.println(printRWC.toString());
									} else
									{
										//wenn es Konditionale mit der Wahrscheinlichkeit -1 gab (also die Antezedenz die Wahrscheinlichkeit Null hatte)
										if (andNull)
										{
											printRWC.setAnt(true);
											System.out.println(printRWC
													.toString());
										}//endif
									}//endelse
								}//endelse
							}// endif
							else
							{
								System.out.println("TODO3");
								// hier muss man die Konditionale betrachten
								// sind die Konstanten in den Atomen der
								// Praemisse und der Konsquenz gleich? Wenn ja
								// InEqualityConstraint und ???
								

							}
						}

						for (int k = 1; k < listOfSetOfConstants.size(); k++)
						{
							ConstantSet constantSet2 = listOfSetOfConstants
									.get(k);
							//wenn eine Menge mehr Elemente enthaelt
							if (constantSet1.size() > constantSet2.size())
							{
								constantsToPrint1 = constantSet2;
								constantsToPrint2 = constantSet1;
								constantSet1.removeAll(constantSet2);
								constantsIneq = constantSet1;
								constantsEq = constantSet1;
							} else
							{
								if (constantSet1.size() < constantSet2.size())
								{
									constantsToPrint1 = constantSet1;
									constantsToPrint2 = constantSet2;
									constantSet2.removeAll(constantSet1);
									constantsIneq = constantSet2;
									constantsEq = constantSet2;
								}

								// wenn die Konstantenmengen sich in der Anzahl nicht
								// unterscheiden, trotzdem unterschiedliche
								// Wahrscheinlichkeiten ermittelt wurde
								else
								{
									System.out.println("TODO2");
									//System.out.println("groesse neue Liste: "
										//	+ newListOfLists.size());
									ProbabilityConditional prob1 = constantSet1
											.getProbabilityConditional();
									ProbabilityConditional prob2 = constantSet2
											.getProbabilityConditional();
									
									
									//jetzt muessen die Konstanten der Atome der Antezedenz und der Konsequenz verglichen werden
									
									RelationalConditional relCond1 = prob1.getRelationalGroundConditional();
									Collection<Atom<RelationalAtom>> atomsAnt1 = relCond1.getAntecedence().getAtoms();
									Collection<Atom<RelationalAtom>> atomsCons1 = relCond1.getConsequence().getAtoms();
									ConstantSet setOfConstantsOfAnt1 = new ConstantSet();
									ConstantSet setOfConstantsOfCons1 = new ConstantSet();
									
										
										setOfConstantsOfAnt1.setProbabilityConditional(prob1);
										setOfConstantsOfCons1.setProbabilityConditional(prob1);
										
										
										//Konstanten der Antezendenz
										for (Atom<RelationalAtom> atom : atomsAnt1)
										{
											RelationalAtom relationalAtom = (RelationalAtom) atom;
											collectionOfConstants = relationalAtom.getConstants();
											for (Constant constant : collectionOfConstants)
											{
												setOfConstantsOfAnt1.add(constant);
											}// endfor

										}// endfor
											
										
										//Konstanten der Konsequenz
										for (Atom<RelationalAtom> atom : atomsCons1)
										{
											RelationalAtom relationalAtom = (RelationalAtom) atom;
											collectionOfConstants = relationalAtom.getConstants();
											for (Constant constant : collectionOfConstants)
											{
												setOfConstantsOfCons1.add(constant);
											}// endfor

										}// endfor
										
										/*//hier enthalten Antezedenz und Konsequenz die gleichen Konstanten
										setOfConstantsOfAnt1.removeAll(setOfConstantsOfCons1);
										
										if(!setOfConstantsOfAnt1.isEmpty())
											{*/
/*											System.out.println("TODO41");
												PrintRelationalWithConstraint print = new PrintRelationalWithConstraint(prob1);
												print.setEqual(false);
												System.out.println(print.toString());
											
										
											}
										else
										{
											System.out.println("TODO51");
											PrintRelationalWithConstraint print = new PrintRelationalWithConstraint(prob1);
											print.setEqual(false);
											print.setAnt(true);
											print.setCons(true);
											System.out.println(print.toString());
	
										}
*/
										
										RelationalConditional relCond2 = prob2.getRelationalGroundConditional();
										Collection<Atom<RelationalAtom>> atomsAnt2 = relCond2.getAntecedence().getAtoms();
										Collection<Atom<RelationalAtom>> atomsCons2 = relCond2.getConsequence().getAtoms();
										
										
										ConstantSet setOfConstantsOfAnt2 = new ConstantSet();
										ConstantSet setOfConstantsOfCons2 = new ConstantSet();
										setOfConstantsOfAnt2.setProbabilityConditional(prob2);
										setOfConstantsOfCons2.setProbabilityConditional(prob2);
										

										
										//Konstanten der Antezendenz
										for (Atom<RelationalAtom> atom : atomsAnt2)
										{
											RelationalAtom relationalAtom = (RelationalAtom) atom;
											collectionOfConstants = relationalAtom.getConstants();
											for (Constant constant : collectionOfConstants)
											{
												setOfConstantsOfAnt2.add(constant);

											}// endfor

										}// endfor
											
										//Konstanten der Konsequenz
										for (Atom<RelationalAtom> atom : atomsCons2)
										{
											RelationalAtom relationalAtom = (RelationalAtom) atom;
											collectionOfConstants = relationalAtom.getConstants();
											for (Constant constant : collectionOfConstants)
											{
												setOfConstantsOfCons2.add(constant);

											}// endfor

										}// endfor

									

									
/*										setOfConstantsOfAnt2.removeAll(setOfConstantsOfCons2);
										if(!setOfConstantsOfAnt2.isEmpty())
											{
											System.out.println("TODO42");
											if(setOfConstantsOfAnt2.size() == 1)
											{
												PrintWithInequality printWIn = new PrintWithInequality(prob2, setOfConstantsOfAnt2);
												System.out
														.println(printWIn.toString());
												}
											else
											{
											PrintRelationalWithConstraint print = new PrintRelationalWithConstraint(prob2);
											print.setEqual(false);
											System.out.println(print.toString());
											}
												
											}
										else
										{
											
											
											PrintRelationalWithConstraint print = new PrintRelationalWithConstraint(prob2);
											print.setEqual(false);
											print.setAnt(true);
											print.setCons(true);
											System.out.println("TODO52");
											System.out.println(print.toString());
										}

*/									
									
									
									
									

								}// endelse
							}// endelse

							if (constantsIneq != null)
							{
								PrintWithInequality printWithInEq = new PrintWithInequality(
										constantsToPrint1
												.getProbabilityConditional(),
										constantsIneq);
								System.out.println(printWithInEq.toString());

							}

							if (constantsEq != null)
							{
								PrintWithEquality printWithEq = new PrintWithEquality(
										constantsToPrint2
												.getProbabilityConditional(),
										constantsEq);
								System.out.println(printWithEq.toString());

							}

						}
						for (ArrayList<ProbabilityConditional> probabilityConditionalList : listOfLists)
						{
							System.out.println("zum Vergleich");
							for (ProbabilityConditional probabilityConditional : (ArrayList<ProbabilityConditional>) probabilityConditionalList)
							{

								System.out.println(probabilityConditional
										.getRelationalGroundConditional()
										.toString()
										+ "["
										+ roundScale(probabilityConditional
												.getProbability()) + "]");

							}// endfor

						}// endfor

/*						for (ArrayList<ProbabilityConditional> probabilityConditionalList : listOfLists)
						{
							for (ProbabilityConditional probCond : probabilityConditionalList)
							{
								probCond.getRelationalGroundConditional()
										.getAtoms();
							}

						}// endfor
*/
					}// endelse
				}// endelse

			}// endelse

			System.out.println(spacebetween);

		}// endfor

	}// endofquery

	/**
	 * @param d
	 * @return
	 */
	public static double roundScale(double d)
	{
		return Math.rint(d * 1000) / 1000;
	}// endofroundscale

}// endofQueryExample
