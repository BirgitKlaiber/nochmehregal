package de.bklaiber.testqueries;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Vector;

import de.bklaiber.inference.Inference;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

public abstract class AbstractQueryTest {

	protected Inference inference = null;
	protected Vector<RelationalConditional> queries = null;

	/**
	 * Tests if the query can be processed.
	 */
	public void queryTest() {

		Collection<RelationalConditional> generalization = inference.queryConditional(queries.elementAt(0));

	}

	/**
	 * Tests if the runtime is below expected threshold.
	 * 
	 * @param threshold
	 *            in milliseconds
	 */
	public void checkRuntimeOfGenerlization(long threshold) {

		long before = System.currentTimeMillis();

		Collection<RelationalConditional> generalization = inference.queryConditional(queries.elementAt(0));

		long after = System.currentTimeMillis();
		assertEquals(after - before < threshold, true);

	}

}
