package de.bklaiber.inference;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.cs.ai.log4KR.math.types.Fraction;

public class ClassifierTest {

	@Test
	public void testConfigurationByProperties() {
		SimpleRoundingClassifier classifier = new SimpleRoundingClassifier();
		assertEquals(classifier.getRoundScale(), new Fraction(1, 1000));
	}

}
