package de.bklaiber.examples;

import java.util.*;

import edu.cs.ai.log4KR.logical.semantics.Interpretation;
import edu.cs.ai.log4KR.logical.semantics.PossibleWorldFactory;
import edu.cs.ai.log4KR.logical.syntax.*;
import edu.cs.ai.log4KR.math.types.Fraction;
import edu.cs.ai.log4KR.relational.classicalLogic.grounding.ConstraintBasedGroundingOperator;
import edu.cs.ai.log4KR.relational.classicalLogic.grounding.GroundingOperator;
import edu.cs.ai.log4KR.relational.classicalLogic.semantics.RelationalPossibleWorldMapRepresentationFactory;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.RelationalAtom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.InequalityConstraint;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.*;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;
import edu.cs.ai.log4KR.relational.util.RelationalUtils;
import edu.cs.ai.log4KR.structuredLogics.GroundingSemantics;
import edu.cs.ai.log4KR.structuredLogics.reasoning.RelationalOptimumEntropyEpistemicStateLBFGS;

public class ExampleMonkey
{

	public static void main(String[] args)
	{
		String spacebetween = new String();

		Collection<Atom<RelationalAtom>> atomCons = null;
		Collection<Atom<RelationalAtom>> atomAnt = null;
		Sort monkey = new Sort("monkey");

		Variable x = new Variable("x", monkey);
		Variable y = new Variable("y", monkey);

		LinkedList<Constant> constants = new LinkedList<Constant>();
		Constant c1 = new Constant("c1", monkey);
		constants.add(c1);
		Constant c2 = new Constant("c2", monkey);
		constants.add(c2);
		Constant c3 = new Constant("c3", monkey);
		constants.add(c3);

		Predicate feeds = new Predicate("feeds", monkey, monkey);
		Predicate hungry = new Predicate("hungry", monkey);

		LinkedList<RelationalConditional> knowledgeBase = new LinkedList<RelationalConditional>();

		RelationalAtom fxy = new RelationalAtom(feeds, x, y);
		RelationalAtom hx = new RelationalAtom(hungry, x);
		RelationalAtom hy = new RelationalAtom(hungry, y);

		RelationalConditional cond1 = new RelationalConditional(fxy, hx.not()
				.and(hy), new Fraction(8, 10), new InequalityConstraint(x, y));
		knowledgeBase.add(cond1);

		RelationalConditional cond2 = new RelationalConditional(fxy.not(), hx,
				new Fraction(999, 1000), new InequalityConstraint(x, y));
		knowledgeBase.add(cond2);

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
		
		System.out.println("Beispiel aus Aufgabe:");
		RelationalAtom f12 = new RelationalAtom(feeds, c1, c2);
		Formula<RelationalAtom> cons = f12;
		Formula<RelationalAtom> ant = new RelationalAtom(hungry, c2).not();
		System.out
		.println("P("
				+ cons
				+ " "
				+ "|"
				+ " "
				+ ant
				+ " ) = "
				+ epState.queryConditionalProbability(cons,
						ant));


	}

}
