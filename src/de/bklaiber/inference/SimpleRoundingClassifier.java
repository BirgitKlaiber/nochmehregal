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
		this(new Fraction(1, 100));
	}

	public SimpleRoundingClassifier(Fraction roundScale) {
		super();
		this.roundScale = roundScale;
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

}
