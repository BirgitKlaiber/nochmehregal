package de.bklaiber.inference;

import java.util.Collection;

import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

public interface Classifier {

	Collection<Collection<RelationalConditional>> classify(
			Collection<RelationalConditional> probabilisticGroundInstances);

	public boolean isEquivalent(RelationalConditional probGroundInsA, RelationalConditional probGroundInsB,
			Collection<RelationalConditional> probabilisticGroundInstances);

	/**
	 * 
	 * @return true if and only if the relation deciding the equivalence is
	 *         transitive
	 */
	public boolean isTransitive();

	public boolean isConfigurable();
}
