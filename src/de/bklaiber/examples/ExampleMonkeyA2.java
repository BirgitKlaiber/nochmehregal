package de.bklaiber.examples;

import java.util.Collection;

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
import edu.cs.ai.log4KR.relational.util.RelationalUtils;
import edu.cs.ai.log4KR.structuredLogics.GroundingSemantics;
import edu.cs.ai.log4KR.structuredLogics.reasoning.RelationalOptimumEntropyEpistemicStateLBFGS;

public class ExampleMonkeyA2
{
	public static void main(String[] args)
	{
		String spacebetween = new String();

		Collection<Atom<RelationalAtom>> atomCons = null;
		Collection<Atom<RelationalAtom>> atomAnt = null;

		Log4KRReader reader = new Log4KRReader();
		reader.read("ressources/fileExamples/relational/MonkeysA.rcl");

		//Collection<Variable> variables = reader.getVariables();
		Collection<Constant> constants = reader.getConstants();
		//Collection<Predicate> predicates = reader.getPredicates();
		Collection<RelationalConditional> knowledgebase = reader
				.getConditionals();

		System.out.println("Konditionale:");
		System.out.println(reader.getConditionals().toString());
		System.out.println(spacebetween);

		// LinkedList<RelationalConditional> knowledgeBase = new
		// LinkedList<RelationalConditional>();

		/*
		 * for (RelationalConditional relationalConditional : knowledgeBase) {
		 * 
		 * }
		 */

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

		// epState.printCurrentProbabilities();

		Collection<RelationalAtom> atoms = RelationalUtils
				.getAtomsFromKnowledgeBase(knowledgebase, constants, gop);
		System.out.println("Grundatome:");
		for (RelationalAtom relationalAtom : atoms)
		{
			System.out.println(relationalAtom.toString());

		}

		System.out.println(spacebetween);

		Collection<RelationalConditional> groundKnowledgeBase = gop
				.groundKnowledgeBase(knowledgebase, constants);
		System.out.println("Grundkonditionale:");
		for (RelationalConditional relationalConditional : groundKnowledgeBase)
		{
			System.out.println(relationalConditional.toString());

		}

		System.out.println(spacebetween);

		System.out.println("Grundkonditionale mit Wahrscheinlichkeiten");
		for (RelationalConditional relationalConditional : groundKnowledgeBase)
		{
			Formula<RelationalAtom> formulaCons = relationalConditional
					.getConsequence();
			Formula<RelationalAtom> formAnt = relationalConditional
					.getAntecedence();
			System.out
					.println("P("
							+ formulaCons
							+ " "
							+ "|"
							+ " "
							+ formAnt
							+ " ) = "
							+ epState.queryConditionalProbability(formulaCons,
									formAnt));
		}

		System.out.println(spacebetween);

		System.out
				.println("Grundformeln der Konsequenz mit Wahrscheinlichkeiten:");
		for (RelationalConditional relationalConditional : groundKnowledgeBase)
		{
			Formula<RelationalAtom> formulaCons = relationalConditional
					.getConsequence();
			System.out.println("P(" + formulaCons + " ) = "
					+ epState.queryProbability(formulaCons));
		}

		System.out.println(spacebetween);

		System.out
				.println("Grundformeln der Prämisse mit Wahrscheinlichkeiten:");
		for (RelationalConditional relationalConditional : groundKnowledgeBase)
		{
			Formula<RelationalAtom> formulaAnt = relationalConditional
					.getAntecedence();
			System.out.println("P(" + formulaAnt + " ) = "
					+ epState.queryProbability(formulaAnt));
		}

		System.out.println(spacebetween);
		System.out
				.println("Grundformeln der Konsequenz mit Wahrscheinlichkeiten:");

		for (RelationalConditional relationalConditional : groundKnowledgeBase)
		{
			Formula<RelationalAtom> formulaCons = relationalConditional
					.getConsequence();

			System.out.println("P(" + formulaCons + ") = "
					+ epState.queryProbability(formulaCons));

			System.out.println("Grundatome mit Wahrscheinlichkeiten:");
			atomCons = formulaCons.getAtoms();
			if (atomCons != null)
			{
				for (Atom<RelationalAtom> atom : atomCons)
				{
					System.out.println("P(" + atom + ") = "
							+ epState.queryProbability(atom));

				}
				System.out.println(spacebetween);

			}

		}

		System.out.println(spacebetween);

		System.out
				.println("Grundformeln der Prämisse mit Wahrscheinlichkeiten:");
		for (RelationalConditional relationalConditional : groundKnowledgeBase)
		{
			Formula<RelationalAtom> formulaAnt = relationalConditional
					.getAntecedence();
			System.out.println("P(" + formulaAnt + " ) = "
					+ epState.queryProbability(formulaAnt));

			System.out.println("Grundatome mit Wahrscheinlichkeiten:");

			atomAnt = formulaAnt.getAtoms();
			if (atomAnt != null)
			{
				for (Atom<RelationalAtom> atom : atomAnt)
				{
					System.out.println("P(" + atom + ") = "
							+ epState.queryProbability(atom));

				}
				System.out.println(spacebetween);

			}

		}

	}// endof main

}
