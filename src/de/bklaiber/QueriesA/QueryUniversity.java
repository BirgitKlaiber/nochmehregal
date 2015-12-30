package de.bklaiber.QueriesA;

import java.util.Collection;

import edu.cs.ai.log4KR.logical.semantics.Interpretation;
import edu.cs.ai.log4KR.logical.semantics.PossibleWorldFactory;
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

public class QueryUniversity
{

	public static void main(String[] args)
	{
		String spacebetween = new String();
		double possible = -1;

		// Collection<Atom<RelationalAtom>> atomCons = null;
		// Collection<Atom<RelationalAtom>> atomAnt = null;

		Log4KRReader reader = new Log4KRReader();
		reader.read("ressources/fileQuerys/University.rcl");

		// Collection<Variable> variables = reader.getVariables();
		Collection<Constant> constants = reader.getConstants();
		// Collection<Predicate> predicates = reader.getPredicates();
		Collection<RelationalConditional> knowledgebase = reader
				.getKnowledgeBase("kb");
		Collection<RelationalConditional> queryKond1 = reader
				.getKnowledgeBase("kb1");
		Collection<RelationalConditional> queryKond2 = reader
				.getKnowledgeBase("kb2");
		Collection<RelationalConditional> queryKond3 = reader
				.getKnowledgeBase("kb3");
		Collection<RelationalConditional> queryKond4 = reader
				.getKnowledgeBase("kb4");
		Collection<RelationalConditional> queryKond5 = reader
				.getKnowledgeBase("kb5");
		Collection<RelationalConditional> queryKond6 = reader
				.getKnowledgeBase("kb6");

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

		Collection<RelationalConditional> groundKnowledgeBase1 = gop
				.groundKnowledgeBase(queryKond1, constants);
		Collection<RelationalConditional> groundKnowledgeBase2 = gop
				.groundKnowledgeBase(queryKond2, constants);
		Collection<RelationalConditional> groundKnowledgeBase3 = gop
				.groundKnowledgeBase(queryKond3, constants);
		Collection<RelationalConditional> groundKnowledgeBase4 = gop
				.groundKnowledgeBase(queryKond4, constants);
		Collection<RelationalConditional> groundKnowledgeBase5 = gop
				.groundKnowledgeBase(queryKond5, constants);
		Collection<RelationalConditional> groundKnowledgeBase6 = gop
				.groundKnowledgeBase(queryKond6, constants);

		System.out.println("Konditionale mit Wahrscheinlichkeiten");
		System.out.println(spacebetween);

		for (RelationalConditional relationalConditional : groundKnowledgeBase1)
		{

			Formula<RelationalAtom> formulaCons = relationalConditional
					.getConsequence();
			Formula<RelationalAtom> formAnt = relationalConditional
					.getAntecedence();

			possible = epState
					.queryConditionalProbability(formulaCons, formAnt);

			if (relationalConditional instanceof RelationalFact)
			{
				if (possible == -1)
				{
					System.out.println("unzulässige Anfrage: " + "P(("
							+ formulaCons +  ")) ");
				} else
				{
					System.out.println("P(("
							+ formulaCons
					
							+ ")) = "
							+ epState.queryConditionalProbability(formulaCons,
									formAnt));
				}

			} else
			{
				if (possible == -1)
				{
					System.out.println("unzulässige Anfrage: " + "P(("
							+ formulaCons + " " + "|" + " " + formAnt + "))  ");
				} else
				{
					System.out.println("P(("
							+ formulaCons
							+ " "
							+ "|"
							+ " "
							+ formAnt
							+ ")) = "
							+ epState.queryConditionalProbability(formulaCons,
									formAnt));
				}
			}

		}// endforQuery1
		System.out.println(spacebetween);

		for (RelationalConditional relationalConditional : groundKnowledgeBase2)
		{

			Formula<RelationalAtom> formulaCons = relationalConditional
					.getConsequence();
			Formula<RelationalAtom> formAnt = relationalConditional
					.getAntecedence();

			possible = epState
					.queryConditionalProbability(formulaCons, formAnt);

			if (relationalConditional instanceof RelationalFact)
			{
				if (possible == -1)
				{
					System.out.println("unzulässige Anfrage: " + "P(("
							+ formulaCons + " " + ")) ");
				} else
				{
					System.out.println("P(("
							+ formulaCons
						
							+ ")) = "
							+ epState.queryConditionalProbability(formulaCons,
									formAnt));
				}

			} else
			{
				if (possible == -1)
				{
					System.out.println("unzulässige Anfrage: " + "P(("
							+ formulaCons + " " + "|" + " " + formAnt + "))  ");
				} else
				{
					System.out.println("P(("
							+ formulaCons
							+ " "
							+ "|"
							+ " "
							+ formAnt
							+ ")) = "
							+ epState.queryConditionalProbability(formulaCons,
									formAnt));
				}
			}

		}// endforQuery2
		System.out.println(spacebetween);

		for (RelationalConditional relationalConditional : groundKnowledgeBase3)
		{

			Formula<RelationalAtom> formulaCons = relationalConditional
					.getConsequence();
			Formula<RelationalAtom> formAnt = relationalConditional
					.getAntecedence();

			possible = epState
					.queryConditionalProbability(formulaCons, formAnt);

			if (relationalConditional instanceof RelationalFact)
			{
				if (possible == -1)
				{
					System.out.println("unzulässige Anfrage: " + "P(("
							+ formulaCons +  ") ");
				} else
				{
					System.out.println("P(("
							+ formulaCons
							+ " "
							+ ")) = "
							+ epState.queryConditionalProbability(formulaCons,
									formAnt));
				}

			} else
			{
				if (possible == -1)
				{
					System.out.println("unzulässige Anfrage: " + "P(("
							+ formulaCons + " " + "|" + " " + formAnt + "))  ");
				} else
				{
					System.out.println("P(("
							+ formulaCons
							+ " "
							+ "|"
							+ " "
							+ formAnt
							+ ")) = "
							+ epState.queryConditionalProbability(formulaCons,
									formAnt));
				}
			}

		}// endforQuery3
		System.out.println(spacebetween);

		for (RelationalConditional relationalConditional : groundKnowledgeBase4)
		{

			Formula<RelationalAtom> formulaCons = relationalConditional
					.getConsequence();
			Formula<RelationalAtom> formAnt = relationalConditional
					.getAntecedence();

			possible = epState
					.queryConditionalProbability(formulaCons, formAnt);

			if (relationalConditional instanceof RelationalFact)
			{
				if (possible == -1)
				{
					System.out.println("unzulässige Anfrage: " + "P(("
							+ formulaCons +  ")) ");
				} else
				{
					System.out.println("P(("
							+ formulaCons
							+ " "
							+ ")) = "
							+ epState.queryConditionalProbability(formulaCons,
									formAnt));
				}

			} else
			{
				if (possible == -1)
				{
					System.out.println("unzulässige Anfrage: " + "P(("
							+ formulaCons + " " + "|" + " " + formAnt + "))  ");
				} else
				{
					System.out.println("P(("
							+ formulaCons
							+ " "
							+ "|"
							+ " "
							+ formAnt
							+ ")) = "
							+ epState.queryConditionalProbability(formulaCons,
									formAnt));
				}
			}

		}// endofQuery4
		System.out.println(spacebetween);

		for (RelationalConditional relationalConditional : groundKnowledgeBase5)
		{

			Formula<RelationalAtom> formulaCons = relationalConditional
					.getConsequence();
			Formula<RelationalAtom> formAnt = relationalConditional
					.getAntecedence();

			possible = epState
					.queryConditionalProbability(formulaCons, formAnt);

			if (relationalConditional instanceof RelationalFact)
			{
				if (possible == -1)
				{
					System.out.println("unzulässige Anfrage: " + "P(("
							+ formulaCons  + ")) ");
				} else
				{
					System.out.println("P(("
							+ formulaCons
							+ " "
							+ ")) = "
							+ epState.queryConditionalProbability(formulaCons,
									formAnt));
				}

			} else
			{
				if (possible == -1)
				{
					System.out.println("unzulässige Anfrage: " + "P(("
							+ formulaCons + " " + "|" + " " + formAnt + "))  ");
				} else
				{
					System.out.println("P(("
							+ formulaCons
							+ " "
							+ "|"
							+ " "
							+ formAnt
							+ ")) = "
							+ epState.queryConditionalProbability(formulaCons,
									formAnt));
				}
			}

		}// endforquery5
		System.out.println(spacebetween);

		for (RelationalConditional relationalConditional : groundKnowledgeBase6)
		{

			Formula<RelationalAtom> formulaCons = relationalConditional
					.getConsequence();
			Formula<RelationalAtom> formAnt = relationalConditional
					.getAntecedence();

			possible = epState
					.queryConditionalProbability(formulaCons, formAnt);

			if (relationalConditional instanceof RelationalFact)
			{
				if (possible == -1)
				{
					System.out.println("unzulässige Anfrage: " + "P(("
							+ formulaCons + " " + ")) ");
				} else
				{
					System.out.println("P(("
							+ formulaCons
						
							+ ")) = "
							+ epState.queryConditionalProbability(formulaCons,
									formAnt));
				}

			} else
			{
				if (possible == -1)
				{
					System.out.println("unzulässige Anfrage: " + "P(("
							+ formulaCons + " " + "|" + " " + formAnt + "))  ");
				} else
				{
					System.out.println("P(("
							+ formulaCons
							+ " "
							+ "|"
							+ " "
							+ formAnt
							+ ")) = "
							+ epState.queryConditionalProbability(formulaCons,
									formAnt));
				}
			}

		}// endforquery6
		System.out.println(spacebetween);

	}// endof main



}
