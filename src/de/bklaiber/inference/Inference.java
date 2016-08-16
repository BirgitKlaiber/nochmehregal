package de.bklaiber.inference;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import edu.cs.ai.log4KR.logical.semantics.Interpretation;
import edu.cs.ai.log4KR.logical.semantics.PossibleWorldFactory;
import edu.cs.ai.log4KR.logical.syntax.Formula;
import edu.cs.ai.log4KR.math.types.Fraction;
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
	RelationalOptimumEntropyEpistemicStateLBFGS epState = null; // epistemic state

	GroundingOperator gop = null;
	Classifier classifier = null;
	Collection<Constant> constants = null;

	/**
	 * Computes the probabilities for each ground instance of the query
	 * conditional and unifies ground instances with the same probability.
	 * 
	 * for example: <code>
	 * 	c = (flies(X))
	 *  against a knowledge base resulting in the computed answers
	 *  	P(flies(Tweety))=0
	 *  	P(flies(Bully))=0.2
	 *  	P(flies(Kirby))=0.2
	 *  	P(flies(Sylvester))=0.2
	 *  returns
	 *  	(flies(X))[0.2]<X!=Tweety>
	 *  	(flies(Tweety))[0]
	 *  </code>
	 * 
	 * @param c
	 *            conditional without probability
	 * @return generalized probabilistic conditionals
	 */
	public Collection<RelationalConditional> queryConditional(RelationalConditional c) {

		Collection<RelationalConditional> groundInstances = ground(c);
		Collection<RelationalConditional> generalizedClasses = classify(c, groundInstances);

		return generalizedClasses;

	}

	/**
	 * 
	 * @param c
	 * @param groundInstances
	 * @return
	 */
	private Collection<RelationalConditional> classify(RelationalConditional c,
			Collection<RelationalConditional> groundInstances) {
		Collection<RelationalConditional> probabilisticGroundInstances = compute(groundInstances);
		probabilisticGroundInstances = generalize(c, probabilisticGroundInstances);

		return probabilisticGroundInstances;
	}

	private Collection<RelationalConditional> generalize(RelationalConditional c,
			Collection<RelationalConditional> probabilisticGroundInstances) {
		Collection<Collection<RelationalConditional>> equivalenceClasses = classifier
				.classify(probabilisticGroundInstances);

		return probabilisticGroundInstances;
	}

	/**
	 * Calls Log4KR to compute the probability for each grounded conditional
	 * 
	 * @param groundedQuery
	 * @return
	 */
	private Collection<RelationalConditional> compute(Collection<RelationalConditional> groundedQuery) {

		ArrayList<RelationalConditional> probabilisticConditionals = new ArrayList<RelationalConditional>();

		for (RelationalConditional relationalGroundConditional : groundedQuery) {

			Formula<RelationalAtom> formulaCons = relationalGroundConditional.getConsequence();
			Formula<RelationalAtom> formAnt = relationalGroundConditional.getAntecedence();

			double probability = epState.queryConditionalProbability(formulaCons, formAnt);
			probabilisticConditionals
					.add(new RelationalConditional(formulaCons, formAnt, (new Fraction(probability)).simplify()));

		}

		for (RelationalConditional relationalConditional : probabilisticConditionals) {
			System.out.println("Konditional mit Wahrscheinlichkeit: " + relationalConditional);
		}

		return probabilisticConditionals;
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
