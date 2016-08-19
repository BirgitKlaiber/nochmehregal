package de.bklaiber.inference;

import java.util.Collection;

import edu.cs.ai.log4KR.math.types.Fraction;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

public class SimpleRoundingClassifier extends AbstractClassifier {

	Fraction roundScale = null;

	public Fraction getRoundScale() {
		return roundScale;
	}

	/**
	 * 
	 * @param roundScale
	 *            must be 1/n.
	 */
	public void setRoundScale(Fraction roundScale) {
		this.roundScale = roundScale;
	}

	public SimpleRoundingClassifier() {
		super();
		roundScale = new Fraction(1, Integer.parseInt(properties.getProperty("roundScale", "100")));
	}

	@Override
	public boolean isEquivalent(RelationalConditional probGroundInsA, RelationalConditional probGroundInsB,
			Collection<RelationalConditional> probabilisticGroundInstances) {
		if (Math.rint(probGroundInsA.getProbability().toFloatingPoint() * roundScale.getDenominator()) == Math
				.rint(probGroundInsB.getProbability().toFloatingPoint() * roundScale.getDenominator())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isConfigurable() {
		return true;
	}

}
