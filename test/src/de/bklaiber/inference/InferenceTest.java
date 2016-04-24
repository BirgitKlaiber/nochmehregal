package de.bklaiber.inference;

import java.io.File;

import org.junit.Test;

import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.kbParser.log4KRReader.Log4KRReader;

public class InferenceTest {

	@Test
	public void testSetKnowledgebase() {
		Inference inference = new Inference();

		inference.setKnowledgebase(new Log4KRReader(), new File(
				"test/res/Birds.rcl"));
	}

}
