package de.bklaiber.testqueries;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import de.bklaiber.Utils.QueryReader;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.kbParser.log4KRReader.Log4KRReader;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

/**
 * Tests how <code>Inference</code> works for the birds example from...
 * 
 * 
 * @author bklaiber
 *
 */
public class BirdsTest extends AbstractQueryTest {

	/**
	 * Setup the knowledgebase for all further testing. As the knowledgebase
	 * does not change for the method provided by the <code>Inference</code>
	 * component it is set up.
	 */
	@Before
	public void setup() {
		super.setup();

		inference.setKnowledgebase(new Log4KRReader(), new File("test/res/Birds.rcl"));

		queries = new Vector<RelationalConditional>(QueryReader.readQueries(new File("test/res/Birds.rcl")));

	}

	/**
	 * Tests if the query can be processed.
	 */
	@Test
	public void queryTest() {

		super.queryTest();
	}

	/**
	 * Tests if the generalization produces the results expected.
	 */
	@Test
	public void checkGeneralization() {
		Vector<String> generalizations = new Vector<String>();

		generalizations.addElement("(flies(X))[0.6636034383842102]<((X=Sylvester + X=Kirby) + X=Bully)> ");
		generalizations.addElement("(flies(X))[0.02494332918283484]<Tweety> ");
		Vector<RelationalConditional> generalization = new Vector<RelationalConditional>(
				inference.queryConditional(queries.elementAt(0)));
		assertEquals(generalizations.elementAt(0), generalization.elementAt(0).toString());
		assertEquals(generalizations.elementAt(1), generalization.elementAt(1).toString());
		assertEquals(generalizations.size(), generalization.size());

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
