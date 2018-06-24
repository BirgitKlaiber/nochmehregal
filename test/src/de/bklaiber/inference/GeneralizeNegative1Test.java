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

public class GeneralizeNegative1Test extends AbstractQueryTest {

	@Before
	public void setup() {
		super.setup();

		inference.setKnowledgebase(new Log4KRReader(), new File("test/res/Birds.rcl"));

		queries = new Vector<RelationalConditional>(QueryReader.readQueries(new File("test/res/Birds.rcl")));

	}

	@Test
	public void checkGeneralizationNegative() {
		Vector<String> generalizations = new Vector<String>();

		generalizations.addElement("(flies(Tweety))[0.008344314635674319]");
		generalizations.addElement("(flies(X))[0.6636035435403175]<X!=Tweety>");

		Collection<RelationalConditional> groundInstances = inference.ground(queries.elementAt(0));
		Collection<Collection<RelationalConditional>> classifiedClasses = inference.classify(queries.elementAt(0),
				groundInstances);

		CanonicalMinimumGeneralization generalization = (CanonicalMinimumGeneralization) inference.getGeneralization();
		Vector<RelationalConditional> generalizedClasses = new Vector<RelationalConditional>(
				generalization.generalizeNegativeNeu(queries.elementAt(0), classifiedClasses, groundInstances));
		assertEquals(generalizations.elementAt(0), generalizedClasses.elementAt(0).toString());

		assertEquals(generalizations.elementAt(1), generalizedClasses.elementAt(1).toString());

		assertEquals(generalizations.size(), generalizedClasses.size());

	}

}
