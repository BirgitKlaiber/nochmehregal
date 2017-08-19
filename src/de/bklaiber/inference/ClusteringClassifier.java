package de.bklaiber.inference;

import java.util.Collection;

import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

/**
 * Clusters the classes.
 * 
 * @author klaiber
 *
 */
public class ClusteringClassifier extends AbstractClassifier {

	private ClusteringAlgorithm algorithm = null;

	@Override
	public boolean isTransitive() {
		return false;
	}

	@Override
	public Collection<Collection<RelationalConditional>> classify(
			Collection<RelationalConditional> probabilisticGroundInstances) {
		// TODO Auto-generated method stub
		return cluster(probabilisticGroundInstances);
	}

	/**
	 * Enables to use generic clustering algorithms on
	 * <code>Collection<RelationalConditional></code>
	 * 
	 * @param probabilisticGroundInstances
	 * @return
	 */
	public Collection<Collection<RelationalConditional>> cluster(
			Collection<RelationalConditional> probabilisticGroundInstances) {
		// TODO Auto-generated method stub
		return super.classify(probabilisticGroundInstances);
	}

	public ClusteringAlgorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(ClusteringAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

}
