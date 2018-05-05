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

public class MisanthropeTest extends AbstractQueryTest {

	@Before
	public void setup() {
		super.setup();
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
		Vector<String> generalizations = new Vector<String>();

		generalizations.addElement("(likes(U,V))[0.5869947844745569]<((U=c * V=b + U=b * V=c) * U!=V)>");
		generalizations.addElement(
				"(likes(U,V))[0.05000001133151919]<((((U=c * V=a + U=b * V=a) + U=a * V=c) + U=a * V=b) * U!=V)>");
		generalizations.addElement("(likes(U,V))[0.0]<U=V>");
		//TODO how does the negative generalization have to work
		//generalizations.addElement("(likes(U,V))[0.0500000017879257]<((((U=a * V=c) + (U=a * V=b)) + (U=b * V=a))+ (U=c * V=a))>");
		//generalizations.addElement("(likes(U,V))[0.0500000017879257]<((U!=c + V!=b) * (U!=b + V!=c))>"); horribly wrong...but what is right??? is there a pattern???

		//Vector<RelationalConditional> generalization = new Vector<RelationalConditional>(
		//	inference.queryConditional(queries.elementAt(0)));

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
	 * Tests if the runtime is below expected threshold.
	 */
	@Test
	public void checkRuntimeOfGeneralization() {

		super.checkRuntimeOfGeneralization(60000);
	}

}
