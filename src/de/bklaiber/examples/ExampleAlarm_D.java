package de.bklaiber.examples;

import java.util.*;

import edu.cs.ai.log4KR.logical.semantics.Interpretation;
import edu.cs.ai.log4KR.logical.semantics.PossibleWorldFactory;
import edu.cs.ai.log4KR.logical.syntax.Atom;
import edu.cs.ai.log4KR.logical.syntax.Formula;
import edu.cs.ai.log4KR.logical.syntax.probabilistic.Conditional;
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

public class ExampleAlarm_D
{

	public static void main(String[] args)
	{
		String spacebetween = new String("\n");

		Collection<Atom<RelationalAtom>> atomCons = null;
		Collection<Atom<RelationalAtom>> atomAnt = null;

		Sort value = new Sort("value");

		Variable x = new Variable("x", value);
		Variable y = new Variable("y", value);

		LinkedList<Constant> constants = new LinkedList<Constant>();
		Constant yes = new Constant("yes", value);
		constants.add(yes);
		Constant no = new Constant("no", value);
		constants.add(no);

		Predicate burglary = new Predicate("burglary", value);
		Predicate earthquake = new Predicate("earthquake", value);
		Predicate alarm = new Predicate("alarm", value);
		Predicate johncalls = new Predicate("johncalls", value);
		Predicate marycalls = new Predicate("marycalls", value);

		LinkedList<RelationalConditional> knowledgeBase = new LinkedList<RelationalConditional>();

		// hier werden die relationalen Atome angelegt
		RelationalAtom burg1 = new RelationalAtom(burglary, x);
		RelationalAtom burg2 = new RelationalAtom(burglary, y);
		RelationalAtom burgYes = new RelationalAtom(burglary, yes);
		RelationalAtom burgNo = new RelationalAtom(burglary, no);
		RelationalAtom earth1 = new RelationalAtom(earthquake, x);
		RelationalAtom earth2 = new RelationalAtom(earthquake, y);
		RelationalAtom earthYes = new RelationalAtom(earthquake, yes);
		RelationalAtom earthNo = new RelationalAtom(earthquake, no);
		RelationalAtom al1 = new RelationalAtom(alarm, x);
		RelationalAtom al2 = new RelationalAtom(alarm, y);
		RelationalAtom alYes = new RelationalAtom(alarm, yes);
		RelationalAtom alNo = new RelationalAtom(alarm, no);
		RelationalAtom jcalls1 = new RelationalAtom(johncalls, x);
		RelationalAtom jcalls2 = new RelationalAtom(johncalls, y);
		RelationalAtom jcallsYes = new RelationalAtom(johncalls, yes);
		RelationalAtom mcalls1 = new RelationalAtom(marycalls, x);
		RelationalAtom mcalls2 = new RelationalAtom(marycalls, y);
		RelationalAtom mcallsYes = new RelationalAtom(johncalls, yes);

		// hier werden die relationalen Konditionale angelegt
		RelationalConditional cond11 = new RelationalConditional(burg1, burg2,
				new Fraction(0, 10), new InequalityConstraint(x, y));
		knowledgeBase.add(cond11);

		RelationalConditional cond12 = new RelationalConditional(earth1,
				earth2, new Fraction(0, 10), new InequalityConstraint(x, y));
		knowledgeBase.add(cond12);

		RelationalConditional cond13 = new RelationalConditional(al1, al2,
				new Fraction(0, 10), new InequalityConstraint(x, y));
		knowledgeBase.add(cond13);

		RelationalConditional cond14 = new RelationalConditional(jcalls1,
				jcalls2, new Fraction(0, 10), new InequalityConstraint(x, y));
		knowledgeBase.add(cond14);

		RelationalConditional cond15 = new RelationalConditional(mcalls1,
				mcalls2, new Fraction(0, 10), new InequalityConstraint(x, y));
		knowledgeBase.add(cond15);

		// hier werden die Fakten angelegt
		RelationalFact fact21 = new RelationalFact(burgYes,
				new Fraction(1, 100));
		knowledgeBase.add(fact21);

		RelationalFact fact22 = new RelationalFact(earthYes,
				new Fraction(1, 10));
		knowledgeBase.add(fact22);

		RelationalConditional cond21 = new RelationalConditional(alYes,
				burgYes.and(earthYes), new Fraction(95, 100));
		knowledgeBase.add(cond21);

		// hier wird der propositionale Teil der Wissensbasis angelegt
		RelationalConditional cond22 = new RelationalConditional(alYes,
				burgYes.and(earthNo), new Fraction(8, 10));
		knowledgeBase.add(cond22);

		RelationalConditional cond23 = new RelationalConditional(alYes,
				burgNo.and(earthYes), new Fraction(3, 10));
		knowledgeBase.add(cond23);

		RelationalConditional cond24 = new RelationalConditional(alYes,
				burgNo.and(earthNo), new Fraction(1, 1000));
		knowledgeBase.add(cond24);

		RelationalConditional cond25 = new RelationalConditional(jcallsYes,
				alYes, new Fraction(6, 10));
		knowledgeBase.add(cond25);

		RelationalConditional cond26 = new RelationalConditional(jcallsYes,
				alNo, new Fraction(1, 10));
		knowledgeBase.add(cond26);

		RelationalConditional cond27 = new RelationalConditional(mcallsYes,
				alYes, new Fraction(7, 10));
		knowledgeBase.add(cond27);

		RelationalConditional cond28 = new RelationalConditional(mcallsYes,
				alNo, new Fraction(5, 100));
		knowledgeBase.add(cond28);

		// epState.printCurrentProbabilities();

		/*
		 * Collection<RelationalAtom> atoms = RelationalUtils
		 * .getAtomsFromKnowledgeBase(knowledgeBase, constants, gop);
		 * System.out.println("Grundatome:"); for (RelationalAtom relationalAtom
		 * : atoms) { System.out.println(relationalAtom.toString());
		 * 
		 * }// endof for
		 */
		System.out.println(spacebetween);

		// Collection<RelationalConditional> groundKnowledgeBase = gop
		// .groundKnowledgeBase(knowledgeBase, constants);

		LinkedList<RelationalConditional> kbDet = new LinkedList<RelationalConditional>();
		LinkedList<RelationalConditional> kbProb = new LinkedList<RelationalConditional>();
		RelationalUtils.decomposeKnowledgeBaseDeteterministicProbabilistic(
				knowledgeBase, kbDet, kbProb);

		// hier wird die Wissensbasis grundiert
		GroundingOperator gop = new ConstraintBasedGroundingOperator();

		LinkedList<Conditional<RelationalAtom>> groundKnowledgeBase = new LinkedList<Conditional<RelationalAtom>>();
		groundKnowledgeBase.addAll(gop.groundKnowledgeBase(kbDet, constants));

		GroundingSemantics semantics = new GroundingSemantics(gop, constants);
		RelationalOptimumEntropyEpistemicStateLBFGS epState = new RelationalOptimumEntropyEpistemicStateLBFGS(
				semantics);

		PossibleWorldFactory<RelationalAtom> worldFactory = new RelationalPossibleWorldMapRepresentationFactory();

		/*
		 * Interpretation<RelationalAtom>[] possibleWorlds = worldFactory
		 * .createPossibleWorlds(RelationalUtils
		 * .getAtomsFromKnowledgeBase(knowledgeBase, constants, gop));
		 */
		Interpretation<RelationalAtom>[] possibleWorlds = worldFactory
				.createPossibleWorldsWithoutNullworlds(RelationalUtils
						.getAtomsFromKnowledgeBase(knowledgeBase, constants,
								gop), groundKnowledgeBase);

		epState.initialize(possibleWorlds, knowledgeBase);

		System.out.println("Grundkonditionale:");
		for (Conditional<RelationalAtom> relationalConditional : groundKnowledgeBase)
		{
			System.out.println(relationalConditional.toString());

		}// endof for
		for (Conditional<RelationalAtom> relConditional : kbProb)
		{
			System.out.println(relConditional.toString());
		}
		System.out.println(spacebetween);

		System.out.println("Grundkonditionale mit Wahrscheinlichkeiten:");
System.out.println("(deterministisch)");
		for (Conditional<RelationalAtom> relationalConditional : groundKnowledgeBase)
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

		}// endof for

		System.out.println("(probablisitisch)");
		for (Conditional<RelationalAtom> relConditional : kbProb)
		{
			System.out.println(relConditional.toString());
		}
		System.out.println(spacebetween);



		System.out
				.println("Grundformeln der Konsequenz mit Wahrscheinlichkeiten:");
		System.out.println("(deterministisch)");
		for (Conditional<RelationalAtom> relationalConditional : groundKnowledgeBase)
		{
			Formula<RelationalAtom> formulaCons = relationalConditional
					.getConsequence();
			System.out.println("P(" + formulaCons + " ) = "
					+ epState.queryProbability(formulaCons));
		}// endof for


		System.out.println("(probablisitisch)");
		for (Conditional<RelationalAtom> relationalConditional : kbProb)
		{
			Formula<RelationalAtom> formulaCons = relationalConditional
					.getConsequence();
			System.out.println("P(" + formulaCons + " ) = "
					+ epState.queryProbability(formulaCons));
		}// endof for

		System.out.println(spacebetween);


		System.out
				.println("Grundformeln der Prämisse mit Wahrscheinlichkeiten:");
		for (Conditional<RelationalAtom> relationalConditional : groundKnowledgeBase)
		{
			Formula<RelationalAtom> formulaAnt = relationalConditional
					.getAntecedence();
			System.out.println("P(" + formulaAnt + " ) = "
					+ epState.queryProbability(formulaAnt));
		}// endof for

		System.out.println(spacebetween);
		System.out
				.println("Grundformeln der Konsequenz mit Wahrscheinlichkeiten:");

		for (Conditional<RelationalAtom> relationalConditional : groundKnowledgeBase)
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
		for (Conditional<RelationalAtom> relationalConditional : groundKnowledgeBase)
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

				}// ewndfor
				System.out.println(spacebetween);

			}// endif

		}// endfor

	}// endof main

}// endof ExampleAlarm
