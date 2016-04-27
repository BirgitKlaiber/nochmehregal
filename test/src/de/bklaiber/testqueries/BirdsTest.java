package de.bklaiber.testqueries;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;

import de.bklaiber.inference.Inference;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.kbParser.log4KRReader.Log4KRReader;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

public class BirdsTest {

	private static Inference inference = null;
	private static RelationalConditional query = null;

	@BeforeClass
	public static void setup() {
		inference = new Inference();

		inference.setKnowledgebase(new Log4KRReader(), new File("test/res/Birds.rcl"));
	}

	/**
	 * Tests if the query can be processed.
	 */
	@Test
	public void queryTest() {

		Collection<RelationalConditional> generalization = inference.queryConditional(query);

	}

	/**
	 * Tests if the generalization works as expected.
	 */
	@Test
	public void checkGeneralization() {
		Collection<RelationalConditional> generalization = inference.queryConditional(query);

	}

	/**
	 * Tests if the runtime is below expected threshold.
	 */
	@Test
	public void checkRuntimeOfGenerlization() {

		long before = System.currentTimeMillis();

		Collection<RelationalConditional> generalization = inference.queryConditional(query);

		long after = System.currentTimeMillis();
		assertEquals(after - before < 60 * 1000, true);
	}

}
