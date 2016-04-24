package de.bklaiber.examples;

import java.util.*;

import edu.cs.ai.log4KR.logical.semantics.Interpretation;
import edu.cs.ai.log4KR.logical.semantics.PossibleWorldFactory;
import edu.cs.ai.log4KR.logical.syntax.Atom;
import edu.cs.ai.log4KR.logical.syntax.Formula;
import edu.cs.ai.log4KR.math.types.Fraction;
import edu.cs.ai.log4KR.relational.classicalLogic.grounding.ConstraintBasedGroundingOperator;
import edu.cs.ai.log4KR.relational.classicalLogic.grounding.GroundingOperator;
import edu.cs.ai.log4KR.relational.classicalLogic.semantics.RelationalPossibleWorldMapRepresentationFactory;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.RelationalAtom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.InequalityConstraint;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.*;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalFact;
import edu.cs.ai.log4KR.relational.util.RelationalUtils;
import edu.cs.ai.log4KR.structuredLogics.GroundingSemantics;
import edu.cs.ai.log4KR.structuredLogics.reasoning.RelationalOptimumEntropyEpistemicStateLBFGS;

public class ExampleMisanthrope
{

	public static void main(String[] args)
	{

		String spacebetween = new String();

		Collection<Atom<RelationalAtom>> atomCons = null;
		Collection<Atom<RelationalAtom>> atomAnt = null;

		Sort group = new Sort("Group");

		Variable U = new Variable("U", group);
		Variable V = new Variable("V", group);

		LinkedList<Constant> constants = new LinkedList<Constant>();
		Constant a = new Constant("anna", group);
		constants.add(a);
		Constant b = new Constant("bob", group);
		constants.add(b);
		Constant c = new Constant("chris", group);
		constants.add(c);

		Predicate likes = new Predicate("likes", group, group);

		LinkedList<RelationalConditional> knowledgeBase = new LinkedList<RelationalConditional>();

		RelationalAtom likesUV = new RelationalAtom(likes, U, V);
		RelationalAtom likesVU = new RelationalAtom(likes, V, U);
		RelationalAtom likesaV = new RelationalAtom(likes, a, V);

		RelationalConditional cond1 = new RelationalConditional(likesUV,
				likesVU, new Fraction(9, 10), new InequalityConstraint(U, V));
		knowledgeBase.add(cond1);

		RelationalFact fact1 = new RelationalFact(likesaV,
				new Fraction(5, 100), new InequalityConstraint(V, a));
		knowledgeBase.add(fact1);

		GroundingOperator gop = new ConstraintBasedGroundingOperator();
		GroundingSemantics semantics = new GroundingSemantics(gop, constants);
		RelationalOptimumEntropyEpistemicStateLBFGS epState = new RelationalOptimumEntropyEpistemicStateLBFGS(
				semantics);

		PossibleWorldFactory<RelationalAtom> worldFactory = new RelationalPossibleWorldMapRepresentationFactory();

		Interpretation<RelationalAtom>[] possibleWorlds = worldFactory
				.createPossibleWorlds(RelationalUtils
						.getAtomsFromKnowledgeBase(knowledgeBase, constants,
								gop));

		epState.initialize(possibleWorlds, knowledgeBase);

		// epState.printCurrentProbabilities();

		Collection<RelationalAtom> atoms = RelationalUtils
				.getAtomsFromKnowledgeBase(knowledgeBase, constants, gop);
		System.out.println("Grundatome:");
		for (RelationalAtom relationalAtom : atoms)
		{
			System.out.println(relationalAtom.toString());

		}

		System.out.println(spacebetween);

		Collection<RelationalConditional> groundKnowledgeBase = gop
				.groundKnowledgeBase(knowledgeBase, constants);
		System.out.println("Grundkonditionale:");
		for (RelationalConditional relationalConditional : groundKnowledgeBase)
		{
			System.out.println(relationalConditional.toString());

		}
		System.out.println(spacebetween);

		System.out.println("Grundkonditionale mit Wahrscheinlichkeiten:");
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

		System.out
				.println("Grundformeln der Konsequenz mit Wahrscheinlichkeiten:");
		for (RelationalConditional relationalConditional : groundKnowledgeBase)
		{
			Formula<RelationalAtom> formulaCons = relationalConditional
					.getConsequence();
			System.out.println("P(" + formulaCons + " ) = "
					+ epState.queryProbability(formulaCons));
		}// endof for

		System.out.println(spacebetween);

		System.out
				.println("Grundformeln der Prämisse mit Wahrscheinlichkeiten:");
		for (RelationalConditional relationalConditional : groundKnowledgeBase)
		{
			Formula<RelationalAtom> formulaAnt = relationalConditional
					.getAntecedence();
			System.out.println("P(" + formulaAnt + " ) = "
					+ epState.queryProbability(formulaAnt));
		}// endof for

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

	}// endofmain

}// endofExampleMisanthrope
