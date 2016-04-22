package de.bklaiber.examples;

import java.util.*;

import edu.cs.ai.log4KR.logical.semantics.Interpretation;
import edu.cs.ai.log4KR.logical.semantics.PossibleWorldFactory;
import edu.cs.ai.log4KR.logical.syntax.Atom;
import edu.cs.ai.log4KR.logical.syntax.Formula;
import edu.cs.ai.log4KR.math.types.Fraction;
import edu.cs.ai.log4KR.relational.classicalLogic.grounding.*;
import edu.cs.ai.log4KR.relational.classicalLogic.semantics.RelationalPossibleWorldMapRepresentationFactory;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.RelationalAtom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.InequalityConstraint;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.*;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalFact;
import edu.cs.ai.log4KR.relational.util.RelationalUtils;
import edu.cs.ai.log4KR.structuredLogics.GroundingSemantics;
import edu.cs.ai.log4KR.structuredLogics.reasoning.RelationalOptimumEntropyEpistemicStateLBFGS;

public class ExampleMonkeys
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
		Constant c1 = new Constant("andy", monkey);
		constants.add(c1);
		Constant c2 = new Constant("bob", monkey);
		constants.add(c2);
		Constant c3 = new Constant("charly", monkey);
		constants.add(c3);

		Predicate feeds = new Predicate("feeds", monkey, monkey);
		Predicate hungry = new Predicate("hungry", monkey);

		LinkedList<RelationalConditional> knowledgeBase = new LinkedList<RelationalConditional>();

		RelationalAtom fxy = new RelationalAtom(feeds, x, y);
		RelationalAtom hx = new RelationalAtom(hungry, x);
		RelationalAtom hy = new RelationalAtom(hungry, y);
		RelationalAtom fxcharly = new RelationalAtom(feeds, x, c3);
		RelationalAtom fxx = new RelationalAtom(feeds, x, x);

		RelationalConditional cond1 = new RelationalConditional(fxy, hx.not()
				.and(hy), new Fraction(8, 10), new InequalityConstraint(x, y));
		knowledgeBase.add(cond1);
		RelationalConditional cond2 = new RelationalConditional(fxy.not(), hx,
				new Fraction(999, 1000), new InequalityConstraint(x, y));
		knowledgeBase.add(cond2);
		RelationalConditional cond3 = new RelationalConditional(fxy.not(), hx
				.not().and(hy.not()), new Fraction(9, 10),
				new InequalityConstraint(x, y));
		knowledgeBase.add(cond3);
		RelationalConditional cond4 = new RelationalConditional(fxcharly,
				hx.not(), new Fraction(95, 100));
		knowledgeBase.add(cond4);
		RelationalFact f = new RelationalFact(fxx, new Fraction(1, 1000));
		knowledgeBase.add(f);

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
				.println("Grundatome der Konsequenz mit Wahrscheinlichkeiten:");
		for (RelationalConditional relationalConditional : groundKnowledgeBase)
		{
			Formula<RelationalAtom> formulaCons = relationalConditional
					.getConsequence();
			System.out.println("P(" + formulaCons + " ) = "
					+ epState.queryProbability(formulaCons));
		}

		System.out.println(spacebetween);

		System.out.println("Grundatome der Prämisse mit Wahrscheinlichkeiten:");
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

		}//endfor
		
		
		
		System.out.println("Beispiele:");
		
		/*RelationalAtom atom = new RelationalAtom(hungry, x);
		GroundSubstitution groundAtom = new GroundSubstitution();
		
		groundAtom.map(atom);
		
		System.out.println("P(" + mAtom + " ) = "
				+ epState.queryProbability(mAtom));

*/		
		for (Constant constant : constants)
		{
			Formula<RelationalAtom> cond = new RelationalAtom(hungry, constant);
			System.out.println("P(" + cond + " ) = "
					+ epState.queryProbability(cond));

			
		}


	
	}// endofmain

}
