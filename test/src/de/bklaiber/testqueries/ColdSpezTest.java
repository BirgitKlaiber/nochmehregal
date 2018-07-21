package de.bklaiber.testqueries;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Collection;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import de.bklaiber.Utils.QueryReader;
import de.bklaiber.inference.CanonicalMinimumGeneralization;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.kbParser.log4KRReader.Log4KRReader;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

public class ColdSpezTest extends AbstractQueryTest {

	@Before
	public void setup() {
		super.setup();
		inference.setKnowledgebase(new Log4KRReader(), new File("test/res/ColdSpez.rcl"));
		queries = new Vector<RelationalConditional>(QueryReader.readQueries(new File("test/res/ColdSpez.rcl")));
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
				"(contact(X,Y) * cold(Y))[0.02068521355652176]<((X=bob * Y=carl) + (X=carl * Y=bob)) * X!=Y>");
		generalizations.addElement(
				"(contact(X,Y) * cold(Y))[0.3588]<((((X=carl + Y=anna) + (X=anna * Y=carl)) + X=anna * Y=bob) + X=bob * Y=anna)>");
		generalizations.addElement("(contact(X,Y) * cold(Y))[0.0]< X=Y>");

		Collection<RelationalConditional> groundInstances = inference.ground(queries.elementAt(0));
		Collection<Collection<RelationalConditional>> classifiedClasses = inference.classify(queries.elementAt(0),
				groundInstances);
		CanonicalMinimumGeneralization generalization = (CanonicalMinimumGeneralization) inference.getGeneralization();

		Vector<RelationalConditional> generalizedClasses = new Vector<RelationalConditional>(
				generalization.generalizePositive(queries.elementAt(0), classifiedClasses));

		System.out.println("Erwartete Ausgabe: " + generalizations.toString());
		System.out.println("Ausgabe der Komponente: " + generalizations.toString());
		assertEquals(generalizations.elementAt(0), generalizedClasses.elementAt(0).toString());
		assertEquals(generalizations.elementAt(1), generalizedClasses.elementAt(1).toString());
		assertEquals(generalizations.elementAt(2), generalizedClasses.elementAt(2).toString());
		assertEquals(generalizations.size(), generalizedClasses.size());
		System.out.println("Positive Generalisierung: ");
		System.out.println("Erwartete Ausgabe: " + generalizations.toString());
		System.out.println("Ausgabe der Komponente: " + generalizations.toString());

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
		generalizations.addElement("(contact(X,Y) * cold(Y))[0]<(X=anna + Y=anna) * X!=Y>");
		generalizations.addElement("(contact(X,Y) * cold(Y))[0]<X!=anna * Y!anna * X!=Y>");

		assertEquals(generalizations.elementAt(0), generalizedClasses.elementAt(0).toString());
		assertEquals(generalizations.elementAt(1), generalizedClasses.elementAt(1).toString());
		assertEquals(generalizations.elementAt(2), generalizedClasses.elementAt(2).toString());
		assertEquals(generalizations.size(), generalizedClasses.size());
		System.out.println("Negative Generalisierung: ");
		System.out.println("Erwartete Ausgabe: " + generalizations.toString());
		System.out.println("Ausgabe der Komponente: " + generalizations.toString());
	}

	/**
	 * Tests if the generalization produces the results expected.
	 */
	@Test
	public void checkGeneralization() {
		Vector<String> generalizations = new Vector<String>();
		generalizations.addElement("(contact(X,Y) * cold(Y))[0]<X=Y>");
		generalizations.addElement("(contact(X,Y) * cold(Y))[0]<(X=anna + Y=anna) * X!=Y>");
		generalizations.addElement("(contact(X,Y) * cold(Y))[0]<X!=anna * Y!anna * X!=Y>");

		Collection<RelationalConditional> groundInstances = inference.ground(queries.elementAt(0));
		Collection<Collection<RelationalConditional>> classifiedClasses = inference.classify(queries.elementAt(0),
				groundInstances);
		CanonicalMinimumGeneralization generalization = (CanonicalMinimumGeneralization) inference.getGeneralization();

		Vector<RelationalConditional> generalizedClasses = new Vector<RelationalConditional>(
				generalization.generalize(queries.elementAt(0), classifiedClasses, inference.getKnowledegbase()));

		assertEquals(generalizations.elementAt(0), generalizedClasses.elementAt(0).toString());
		assertEquals(generalizations.elementAt(1), generalizedClasses.elementAt(1).toString());
		assertEquals(generalizations.elementAt(2), generalizedClasses.elementAt(2).toString());
		assertEquals(generalizations.size(), generalizedClasses.size());
		System.out.println("Generalisierung: ");
		System.out.println("Erwartete Ausgabe: " + generalizations.toString());
		System.out.println("Ausgabe der Komponente: " + generalizations.toString());
	}

	/**
	 * Tests if the runtime is below expected threshold.
	 */
	@Test
	public void checkRuntimeOfGeneralization() {

		super.checkRuntimeOfGeneralization(60000);
	}

}