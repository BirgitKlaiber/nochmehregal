package de.bklaiber.inference;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Collection;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import de.bklaiber.Utils.QueryReader;
import de.bklaiber.testqueries.AbstractQueryTest;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.kbParser.log4KRReader.Log4KRReader;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

public class GeneralizeColdTest extends AbstractQueryTest {

	@Before
	public void setup() {
		super.setup();
		inference.setKnowledgebase(new Log4KRReader(), new File("test/res/Cold.rcl"));
		queries = new Vector<RelationalConditional>(QueryReader.readQueries(new File("test/res/Cold.rcl")));
	}

	@Test
	public void queryTest() {
		super.queryTest();
	}

	/**
	 * Tests if the generalization produces the results expected.
	 */
	@Test
	public void checkGeneralizationPositive() {
		Vector<String> generalizations = new Vector<String>();

		generalizations.addElement(
				"(contact(X,Y) * cold(Y))[0.008090976869599835]<((((((X=anna * Y=bob) + (X=carl * Y=bob)) + (X=bob * Y=anna)) + (X=anna * Y=carl)) + (X=bob * Y=carl)) + (X=carl * Y=anna)) * X!=Y>");
		generalizations.addElement("(contact(X,Y) * cold(Y))[0]<X!=Y>");

		Collection<RelationalConditional> groundInstances = inference.ground(queries.elementAt(0));
		Collection<Collection<RelationalConditional>> classifiedClasses = inference.classify(queries.elementAt(0),
				groundInstances);
		CanonicalMinimumGeneralization generalization = (CanonicalMinimumGeneralization) inference.getGeneralization();

		Vector<RelationalConditional> generalizedClasses = new Vector<RelationalConditional>(
				generalization.generalizePositive(queries.elementAt(0), classifiedClasses));

		assertEquals(generalizations.elementAt(0), generalizedClasses.elementAt(0).toString());
		assertEquals(generalizations.elementAt(1), generalizedClasses.elementAt(1).toString());
		assertEquals(generalizations.elementAt(2), generalizedClasses.elementAt(2).toString());
		assertEquals(generalizations.size(), generalizedClasses.size());
	}

	/**
	 * Tests if the generalization produces the results expected.
	 */
	@Test
	public void checkGeneralizationNegative() {
		Vector<String> generalizations = new Vector<String>();

		Collection<RelationalConditional> groundInstances = inference.ground(queries.elementAt(0));
		Collection<Collection<RelationalConditional>> classifiedClasses = inference.classify(queries.elementAt(0),
				groundInstances);
		CanonicalMinimumGeneralization generalization = (CanonicalMinimumGeneralization) inference.getGeneralization();

		Vector<RelationalConditional> generalizedClasses = new Vector<RelationalConditional>(generalization
				.generalizeNegative(queries.elementAt(0), classifiedClasses, inference.getKnowledegbase()));

		generalizations.addElement("(contact(X,Y) * cold(Y))[0]<X=Y>");
		generalizations.addElement("(contact(X,Y) * cold(Y))[0]<X!=Y>");

		assertEquals(generalizations.elementAt(0), generalizedClasses.elementAt(0).toString());
		assertEquals(generalizations.elementAt(1), generalizedClasses.elementAt(1).toString());
		assertEquals(generalizations.size(), generalizedClasses.size());
	}

	/**
	 * Tests if the generalization produces the results expected.
	 */
	@Test
	public void checkGeneralization() {
		Vector<String> generalizations = new Vector<String>();
		Collection<RelationalConditional> groundInstances = inference.ground(queries.elementAt(0));
		Collection<Collection<RelationalConditional>> classifiedClasses = inference.classify(queries.elementAt(0),
				groundInstances);
		CanonicalMinimumGeneralization generalization = (CanonicalMinimumGeneralization) inference.getGeneralization();

		Vector<RelationalConditional> generalizedClasses = new Vector<RelationalConditional>(generalization
				.generalizeNegative(queries.elementAt(0), classifiedClasses, inference.getKnowledegbase()));

		generalizations.addElement("(contact(X,Y) * cold(Y))[0]<X=Y>");
		generalizations.addElement("(contact(X,Y) * cold(Y))[0]<X!=Y>");

		assertEquals(generalizations.elementAt(0), generalizedClasses.elementAt(0).toString());
		assertEquals(generalizations.elementAt(1), generalizedClasses.elementAt(1).toString());
		assertEquals(generalizations.size(), generalizedClasses.size());
	}

	/**
	 * Tests if the runtime is below expected threshold.
	 */
	@Test
	public void checkRuntimeOfGeneralization() {

		super.checkRuntimeOfGeneralization(60000);
	}
}