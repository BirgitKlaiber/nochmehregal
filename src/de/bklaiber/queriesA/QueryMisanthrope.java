package de.bklaiber.queriesA;

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
import edu.cs.ai.log4KR.relational.util.RelationalUtils;
import edu.cs.ai.log4KR.structuredLogics.GroundingSemantics;
import edu.cs.ai.log4KR.structuredLogics.reasoning.RelationalOptimumEntropyEpistemicStateLBFGS;

public class QueryMisanthrope {
	public static void main(String[] args) {
		String spacebetween = new String();
		double possible = -1;

		// Collection<Atom<RelationalAtom>> atomCons = null;
		// Collection<Atom<RelationalAtom>> atomAnt = null;

		Log4KRReader reader = new Log4KRReader();
		reader.read("ressources/fileQuerys/Misanthrope.rcl");

		// Collection<Variable> variables = reader.getVariables();
		Collection<Constant> constants = reader.getConstants();
		// Collection<Predicate> predicates = reader.getPredicates();
		//Collection<RelationalConditional> conditionals = reader
		//	.getConditionals();
		Collection<RelationalConditional> knowledgebase = reader.getKnowledgeBase("kb");
		Collection<RelationalConditional> queryLikesUV = reader.getKnowledgeBase("query1");
		Collection<RelationalConditional> queryLikesaV = reader.getKnowledgeBase("query2");
		Collection<RelationalConditional> queryLikesUVLikesVU = reader.getKnowledgeBase("query3");

		GroundingOperator gop = new ConstraintBasedGroundingOperator();
		GroundingSemantics semantics = new GroundingSemantics(gop, constants);
		RelationalOptimumEntropyEpistemicStateLBFGS epState = new RelationalOptimumEntropyEpistemicStateLBFGS(
				semantics);

		PossibleWorldFactory<RelationalAtom> worldFactory = new RelationalPossibleWorldMapRepresentationFactory();

		Interpretation<RelationalAtom>[] possibleWorlds = worldFactory
				.createPossibleWorlds(RelationalUtils.getAtomsFromKnowledgeBase(knowledgebase, constants, gop));

		epState.initialize(possibleWorlds, knowledgebase);

		Collection<RelationalConditional> groundKnowledgeBaseLikesUV = gop.groundKnowledgeBase(queryLikesUV, constants);
		Collection<RelationalConditional> groundKnowledgeBaseLikesaV = gop.groundKnowledgeBase(queryLikesaV, constants);
		Collection<RelationalConditional> groundKnowledgeBaseLikesUVLikesVU = gop
				.groundKnowledgeBase(queryLikesUVLikesVU, constants);

		for (RelationalConditional relationalConditional : groundKnowledgeBaseLikesUV) {

			Collection<RelationalConditional> groundCond = gop.groundConditional(relationalConditional, constants);

			for (RelationalConditional groundConditional : groundCond) {
				Formula<RelationalAtom> formulaCons = groundConditional.getConsequence();
				Formula<RelationalAtom> formulaAnt = groundConditional.getAntecedence();

				possible = epState.queryConditionalProbability(formulaCons, formulaAnt);

				if (possible == -1) {
					System.out.println(
							"unzulaessige Anfrage: " + "P(" + formulaCons + " " + "|" + " " + formulaAnt + " )  ");
				} else {

					System.out.print("P(");
					System.out.print(groundConditional.toString());
					System.out.print(") = ");
					System.out.println(epState.queryConditionalProbability(formulaCons, formulaAnt));
				}

			}

		}

		System.out.println(spacebetween);

		for (RelationalConditional relationalConditional : groundKnowledgeBaseLikesaV) {

			Collection<RelationalConditional> groundCond = gop.groundConditional(relationalConditional, constants);

			for (RelationalConditional groundConditional : groundCond) {
				Formula<RelationalAtom> formulaCons = groundConditional.getConsequence();
				Formula<RelationalAtom> formulaAnt = groundConditional.getAntecedence();

				possible = epState.queryConditionalProbability(formulaCons, formulaAnt);

				if (possible == -1) {
					System.out.println(
							"unzulaessige Anfrage: " + "P(" + formulaCons + " " + "|" + " " + formulaAnt + " )  ");
				} else {

					System.out.print("P(");
					System.out.print(groundConditional.toString());
					System.out.print(") = ");
					System.out.println(epState.queryConditionalProbability(formulaCons, formulaAnt));
				}

			}

		}
		System.out.println(spacebetween);

		for (RelationalConditional relationalConditional : groundKnowledgeBaseLikesUVLikesVU) {

			//Collection<RelationalConditional> groundCond = gop
			//	.groundConditional(relationalConditional, constants);

			Formula<RelationalAtom> formulaCons = relationalConditional.getConsequence();
			Formula<RelationalAtom> formAnt = relationalConditional.getAntecedence();

			possible = epState.queryConditionalProbability(formulaCons, formAnt);

			if (possible == -1) {
				System.out.println("unzulaessige Anfrage: " + "P(" + formulaCons + " " + "|" + " " + formAnt + " )  ");
			} else {
				System.out.println("P(" + formulaCons + " " + "|" + " " + formAnt + " ) = "
						+ epState.queryConditionalProbability(formulaCons, formAnt));
			}

			//}// endforQuery1

			/*	for (RelationalConditional groundConditional : groundCond)
				{
					Formula<RelationalAtom> formulaCons = groundConditional
							.getConsequence();
					Formula<RelationalAtom> formulaAnt = groundConditional
							.getAntecedence();
			
			*/ /*System.out.print("P(");
				System.out.print(groundConditional.toString());
				System.out.print(") = ");
				System.out.println(epState.queryConditionalProbability(
						formulaCons, formulaAnt));
				
				*/ } // endfor

	}// endfor

}// endof main
