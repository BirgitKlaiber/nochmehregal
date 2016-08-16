package de.bklaiber.queriesA;

import java.util.Collection;

import edu.cs.ai.log4KR.logical.semantics.Interpretation;
import edu.cs.ai.log4KR.logical.semantics.PossibleWorldFactory;
//import edu.cs.ai.log4KR.logical.syntax.Atom;
import edu.cs.ai.log4KR.logical.syntax.Formula;
//import edu.cs.ai.log4KR.logical.syntax.probabilistic.Conditional;
import edu.cs.ai.log4KR.relational.classicalLogic.grounding.ConstraintBasedGroundingOperator;
import edu.cs.ai.log4KR.relational.classicalLogic.grounding.GroundingOperator;
import edu.cs.ai.log4KR.relational.classicalLogic.semantics.RelationalPossibleWorldMapRepresentationFactory;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.RelationalAtom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Constant;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.kbParser.log4KRReader.Log4KRReader;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;
//import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalFact;
import edu.cs.ai.log4KR.relational.util.RelationalUtils;
import edu.cs.ai.log4KR.structuredLogics.GroundingSemantics;
import edu.cs.ai.log4KR.structuredLogics.reasoning.RelationalOptimumEntropyEpistemicStateLBFGS;

public class QueryBirds {
	public static void main(String[] args) {
		String spacebetween = new String();

		// Collection<Atom<RelationalAtom>> atomCons = null;
		// Collection<Atom<RelationalAtom>> atomAnt = null;

		Log4KRReader reader = new Log4KRReader();
		reader.read("ressources/fileQuerys/Birds.rcl");

		// Collection<Variable> variables = reader.getVariables();
		Collection<Constant> constants = reader.getConstants();
		// Collection<Predicate> predicates = reader.getPredicates();
		//Collection<RelationalConditional> conditionals = reader
		//	.getConditionals();
		Collection<RelationalConditional> knowledgebase = reader.getKnowledgeBase("kb");
		Collection<RelationalConditional> queryFlies = reader.getKnowledgeBase("query1");
		Collection<RelationalConditional> queryIsBird = reader.getKnowledgeBase("query2");
		Collection<RelationalConditional> queryFliesIsBird = reader.getKnowledgeBase("query3");
		Collection<RelationalConditional> queryIsBirdFlies = reader.getKnowledgeBase("query4");

		GroundingOperator gop = new ConstraintBasedGroundingOperator();
		GroundingSemantics semantics = new GroundingSemantics(gop, constants);
		RelationalOptimumEntropyEpistemicStateLBFGS epState = new RelationalOptimumEntropyEpistemicStateLBFGS(
				semantics);

		PossibleWorldFactory<RelationalAtom> worldFactory = new RelationalPossibleWorldMapRepresentationFactory();

		Interpretation<RelationalAtom>[] possibleWorlds = worldFactory
				.createPossibleWorlds(RelationalUtils.getAtomsFromKnowledgeBase(knowledgebase, constants, gop));

		epState.initialize(possibleWorlds, knowledgebase);

		Collection<RelationalConditional> groundKnowledgeBaseFlies = gop.groundKnowledgeBase(queryFlies, constants);
		Collection<RelationalConditional> groundKnowledgeBaseIsBird = gop.groundKnowledgeBase(queryIsBird, constants);
		Collection<RelationalConditional> groundKnowledgeBaseFliesIsBird = gop.groundKnowledgeBase(queryFliesIsBird,
				constants);
		Collection<RelationalConditional> groundKnowledgeBaseIsBirdFlies = gop.groundKnowledgeBase(queryIsBirdFlies,
				constants);

		for (RelationalConditional relationalConditional : groundKnowledgeBaseFlies) {

			Collection<RelationalConditional> groundCond = gop.groundConditional(relationalConditional, constants);

			for (RelationalConditional groundConditional : groundCond) {
				Formula<RelationalAtom> formulaCons = groundConditional.getConsequence();
				Formula<RelationalAtom> formulaAnt = groundConditional.getAntecedence();

				System.out.print("P(");
				System.out.print(groundConditional.toString());
				System.out.print(") = ");
				System.out.println(epState.queryConditionalProbability(formulaCons, formulaAnt));

			}

		}

		System.out.println(spacebetween);

		for (RelationalConditional relationalConditional : groundKnowledgeBaseIsBird) {

			Collection<RelationalConditional> groundCond = gop.groundConditional(relationalConditional, constants);

			for (RelationalConditional groundConditional : groundCond) {
				Formula<RelationalAtom> formulaCons = groundConditional.getConsequence();
				Formula<RelationalAtom> formulaAnt = groundConditional.getAntecedence();

				System.out.print("P(");
				System.out.print(groundConditional.toString());
				System.out.print(") = ");
				System.out.println(epState.queryConditionalProbability(formulaCons, formulaAnt));

			}

		}
		System.out.println(spacebetween);

		for (RelationalConditional relationalConditional : groundKnowledgeBaseFliesIsBird) {

			Collection<RelationalConditional> groundCond = gop.groundConditional(relationalConditional, constants);

			for (RelationalConditional groundConditional : groundCond) {
				Formula<RelationalAtom> formulaCons = groundConditional.getConsequence();
				Formula<RelationalAtom> formulaAnt = groundConditional.getAntecedence();

				System.out.print("P(");
				System.out.print(groundConditional.toString());
				System.out.print(") = ");
				System.out.println(epState.queryConditionalProbability(formulaCons, formulaAnt));

			} // endfor

		} // endfor

		System.out.println(spacebetween);

		for (RelationalConditional relationalConditional : groundKnowledgeBaseIsBirdFlies) {

			Collection<RelationalConditional> groundCond = gop.groundConditional(relationalConditional, constants);

			for (RelationalConditional groundConditional : groundCond) {
				Formula<RelationalAtom> formulaCons = groundConditional.getConsequence();
				Formula<RelationalAtom> formulaAnt = groundConditional.getAntecedence();

				System.out.print("P(");
				System.out.print(groundConditional.toString());
				System.out.print(") = ");
				System.out.println(epState.queryConditionalProbability(formulaCons, formulaAnt));

			}

		}

		System.out.println(spacebetween);

	}// endof main

}
