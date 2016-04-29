package de.bklaiber.inference;

import java.io.File;
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

/**
 * 
 * 
 * @author klaiber
 *
 */
public class Inference {

	private static final String MSG_NOKNOWLEDGEBASE = "no knowledgebase set";

	Collection<RelationalConditional> kb = null; // non grounded knowledgebase
	RelationalOptimumEntropyEpistemicStateLBFGS epState = null; // epistemic
																// state
	GroundingOperator gop = null;
	Collection<Constant> constants = null;

	public Collection<RelationalConditional> queryConditional(RelationalConditional c) {

		Collection<RelationalConditional> groundedQuery = ground(c);

		Collection<RelationalConditional> generalizedClasses = classify(groundedQuery);

		return generalizedClasses;

	}

	private Collection<RelationalConditional> classify(Collection<RelationalConditional> groundedQuery) {
		Collection<RelationalConditional> query = compute(groundedQuery);
		query = generalize(query);

		return query;
	}

	private Collection<RelationalConditional> generalize(Collection<RelationalConditional> query) {

		return query;
	}

	/**
	 * Calls Log4KR and copmutes the probability for each grounded conditional
	 * 
	 * @param groundedQuery
	 * @return
	 */
	private Collection<RelationalConditional> compute(Collection<RelationalConditional> groundedQuery) {
		for (RelationalConditional relationalGroundConditional : groundedQuery) {

			Formula<RelationalAtom> formulaCons = relationalGroundConditional.getConsequence();
			Formula<RelationalAtom> formAnt = relationalGroundConditional.getAntecedence();

			double probability = epState.queryConditionalProbability(formulaCons, formAnt);

			//RelationalConditional probabilityConitional = new RelationalConditional(formulaCons, formAnt, probability);
			/*
						ProbabilityConditional probabilityConditional = new ProbabilityConditional(
								probability, relationalGroundConditional);
						probabilityConditional
								.setRelationalConditional(relationalConditionals.get(0));
			
						probabilityConditionals.add(probabilityConditional);
			*/

		} // endfor

		return null;
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	private Collection<RelationalConditional> ground(RelationalConditional c) {
		if (kb == null) {
			throw new NullPointerException(MSG_NOKNOWLEDGEBASE);
		}
		Collection<RelationalConditional> groundQuery = gop.groundConditional(c, constants);

		return groundQuery;
	}

	/**
	 * 
	 * Reads the knowledgebase from a file, initializes the epistemic state
	 * witch is best the from the possible worlds regarding the principle of
	 * maximum entropy
	 * 
	 * @param reader
	 * @param kbFile
	 */
	public void setKnowledgebase(Log4KRReader reader, File kbFile) {
		reader.read(kbFile);
		kb = reader.getKnowledgeBase("kb");

		constants = reader.getConstants();

		gop = new ConstraintBasedGroundingOperator();

		GroundingSemantics semantics = new GroundingSemantics(gop, constants);

		PossibleWorldFactory<RelationalAtom> worldFactory = new RelationalPossibleWorldMapRepresentationFactory();

		Interpretation<RelationalAtom>[] possibleWorlds = worldFactory
				.createPossibleWorlds(RelationalUtils.getAtomsFromKnowledgeBase(kb, constants, gop));

		epState = new RelationalOptimumEntropyEpistemicStateLBFGS(semantics);
		epState.initialize(possibleWorlds, kb);

	}
}
