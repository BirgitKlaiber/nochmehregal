package de.bklaiber.inference;

import java.util.Collection;

import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

public interface Classifier {

	/**
	 * Classifies the probabilistic ground instances of the query conditional.
	 * 
	 * @param probabilisticGroundInstances
	 * 
	 * @return classes of equivalent probabilistic ground conditional of the
	 *         query conditional
	 */
	Collection<Collection<RelationalConditional>> classify(
			Collection<RelationalConditional> probabilisticGroundInstances);

	/**
	 * 
	 * @param probGroundInsA
	 * @param probGroundInsB
	 * @param probabilisticGroundInstances
	 * 
	 * @return true, if the probabilistic ground conditionals are equivalent
	 */
	public boolean isEquivalent(RelationalConditional probGroundInsA, RelationalConditional probGroundInsB,
			Collection<RelationalConditional> probabilisticGroundInstances);

	/**
	 * 
	 * @return true if and only if the relation deciding the equivalence is
	 *         transitive
	 */
	public boolean isTransitive();

	/**
	 * 
	 * @return true if...
	 */
	public boolean isConfigurable();
}
