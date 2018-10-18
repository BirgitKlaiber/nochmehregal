package de.bklaiber.inference;

import java.util.Collection;

import edu.cs.ai.log4KR.math.types.Fraction;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

/**
 * 
 * Classifies the classes by equal probability when rounded.
 * 
 * @author klaiber
 *
 */
public class SimpleRoundingClassifier extends AbstractClassifier {

	Fraction roundScale = null;

	/**
	 * Returns the scale for rounding.
	 * 
	 * @return roundscale
	 */
	public Fraction getRoundScale() {
		return roundScale;
	}

	/**
	 * 
	 * Sets the scale for rounding.
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

	/**
	 * 
	 * Tests if two probabilitys are equal
	 * 
	 * 
	 * 
	 */
	@Override
	public boolean isEquivalent(RelationalConditional probGroundInsA, RelationalConditional probGroundInsB,
			Collection<RelationalConditional> probabilisticGroundInstances) {
		if (Math.rint(probGroundInsA.getProbability().toFloatingPoint() * roundScale.getDenominator()) == Math
				.rint(probGroundInsB.getProbability().toFloatingPoint() * roundScale.getDenominator())) {
			return true;
		}
		return false;
	}

	//not implemented
	@Override
	public boolean isConfigurable() {
		return true;
	}

}
