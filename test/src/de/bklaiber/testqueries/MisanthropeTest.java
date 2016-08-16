package de.bklaiber.testqueries;

import java.io.File;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import de.bklaiber.Utils.QueryReader;
import de.bklaiber.inference.Inference;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.kbParser.log4KRReader.Log4KRReader;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

public class MisanthropeTest extends AbstractQueryTest {

	@Before
	public void setup() {
		inference = new Inference();

		inference.setKnowledgebase(new Log4KRReader(), new File("test/res/Misanthrope.rcl"));
		queries = new Vector<RelationalConditional>(QueryReader.readQueries(new File("test/res/Misanthrope.rcl")));
	}

	@Test
	public void queryTest() {
		super.queryTest();
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
	public void checkRuntimeOfGeneralization() {

		super.checkRuntimeOfGeneralization(60000);
	}

}
