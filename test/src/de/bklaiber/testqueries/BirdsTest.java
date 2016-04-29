package de.bklaiber.testqueries;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Collection;
import java.util.Vector;

import org.junit.BeforeClass;
import org.junit.Test;

import de.bklaiber.Utils.QueryReader;
import de.bklaiber.inference.Inference;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.kbParser.log4KRReader.Log4KRReader;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

/**
 * Tests how <code>Inference</code> works for the birds example from...
 * 
 * 
 * @author bklaiber
 *
 */
public class BirdsTest {

	private static Inference inference = null;
	private static Vector<RelationalConditional> queries = null;

	/**
	 * Setup the knowledgebase for all further testing. As the knowledgebase
	 * does not change for the method provided by the <code>Inference</code>
	 * component it is set up in advance and only once.
	 */
	@BeforeClass
	public static void setup() {
		inference = new Inference();

		inference.setKnowledgebase(new Log4KRReader(), new File(
				"test/res/Birds.rcl"));

		queries = new Vector<RelationalConditional>(
				QueryReader.readQueries(new File("test/res/Birds.rcl")));

	}

	/**
	 * Tests if the query can be processed.
	 */
	@Test
	public void queryTest() {

		Collection<RelationalConditional> generalization = inference
				.queryConditional(queries.elementAt(0));

	}

	/**
	 * Tests if the generalization produces the results expected.
	 */
	@Test
	public void checkGeneralization() {
		Vector<RelationalConditional> generalization = new Vector<RelationalConditional>(
				inference.queryConditional(queries.elementAt(0)));
		System.out.println(generalization.elementAt(0).toString());

	}

	/**
	 * Tests if the runtime is below expected threshold.
	 */
	@Test
	public void checkRuntimeOfGenerlization() {

		long before = System.currentTimeMillis();

		Collection<RelationalConditional> generalization = inference
				.queryConditional(queries.elementAt(0));

		long after = System.currentTimeMillis();
		assertEquals(after - before < 60 * 1000, true);

	}

}
