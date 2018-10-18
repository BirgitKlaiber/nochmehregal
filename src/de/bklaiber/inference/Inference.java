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
 * 
 * 
 * Given an unconditioned relational FO-PCL Conditional it returns classes of
 * relational conditioned conditionals and the computed related probability.
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
	Generalization generalization = null;

	/**
	 * Getter for generalization.
	 * 
	 * @return generalization
	 */
	public Generalization getGeneralization() {
		return generalization;
	}

	/**
	 * Setter for the generalization.
	 * 
	 * @param generalization
	 *            is used to set the generalization
	 */
	public void setGeneralization(Generalization generalization) {
		this.generalization = generalization;
	}

	/**
	 * Getter for classifier.
	 * 
	 * @return classifier
	 */
	public Classifier getClassifier() {
		return classifier;
	}

	/**
	 * Setter for Classifier.
	 * 
	 * @param classifier
	 *            is set
	 *
	 */
	public void setClassifier(Classifier classifier) {
		this.classifier = classifier;
	}

	Collection<Constant> constants = null;
	Collection<RelationalConditional> conditionals = null;

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
	 *  	"(flies(X))[0.2]&lt;X!=Tweety&gt;"
	 *  	"(flies(X))[0]&lt;X=Tweety&gt;"
	 *  </code>
	 * 
	 * @param c
	 *            conditional without probability
	 * @return generalized probabilistic conditionals
	 */
	public Collection<RelationalConditional> queryConditional(RelationalConditional c) {

		Collection<RelationalConditional> groundInstances = ground(c);
		Collection<Collection<RelationalConditional>> classifiedClasses = classify(c, groundInstances);
		Collection<RelationalConditional> generalizedClasses = generalization.generalize(c, classifiedClasses, kb);

		return generalizedClasses;
	}

	/**
	 * 
	 * @param c
	 *            conditional without probability
	 * @param groundInstances
	 *            the ground instances of the conditional
	 * 
	 * @return generalized probabilistic ground instances of the conditional of
	 *         the query
	 */
	public Collection<Collection<RelationalConditional>> classify(RelationalConditional c,
			Collection<RelationalConditional> groundInstances) {
		//probabilisticGroundInstances ist eine ArrayList
		Collection<RelationalConditional> probabilisticGroundInstances = compute(groundInstances);

		Collection<Collection<RelationalConditional>> equivalenceClasses = classifier
				.classify(probabilisticGroundInstances);
		//equivalenceClasses ist eine ArrayList
		return equivalenceClasses;
	}

	/**
	 * Calls Log4KR to compute the probability for each grounded conditional
	 * 
	 * @param groundedQuery
	 *            the grounded query
	 * 
	 * @return list of relational probabilistic conditionals with their computed
	 *         probability
	 */
	private Collection<RelationalConditional> compute(Collection<RelationalConditional> groundedQuery) {

		ArrayList<RelationalConditional> probabilisticConditionals = new ArrayList<RelationalConditional>();

		for (RelationalConditional relationalGroundConditional : groundedQuery) {

			Formula<RelationalAtom> formulaCons = relationalGroundConditional.getConsequence();
			Formula<RelationalAtom> formAnt = relationalGroundConditional.getAntecedence();

			double probability = epState.queryConditionalProbability(formulaCons, formAnt);
			Double p = probability;
			probabilisticConditionals
					.add(new RelationalConditional(formulaCons, formAnt, (new Fraction(probability)).simplify()));

		}

		return probabilisticConditionals;
	}

	/**
	 * 
	 * Grunds the query.
	 *
	 * @param c
	 *            relational unconditioned conditional of the query
	 * 
	 * @return grounded query conditional
	 */
	public Collection<RelationalConditional> ground(RelationalConditional c) {
		if (kb == null) {
			throw new NullPointerException(MSG_NOKNOWLEDGEBASE);
		}

		Collection<RelationalConditional> groundQuery = gop.groundConditional(c, constants);

		return groundQuery;
	}

	/**
	 * 
	 * Reads the knowledgebase from a file, initializes the epistemic state
	 * which is best the from the possible worlds regarding the principle of
	 * maximum entropy
	 * 
	 * @param reader
	 *            reader of the file
	 * @param kbFile
	 *            the file where the kb is saved
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

	/**
	 * Returns the knowledgebase.
	 * 
	 * @return knowledgebase
	 */
	public Collection<RelationalConditional> getKnowledegbase() {
		return kb;
	}

}
