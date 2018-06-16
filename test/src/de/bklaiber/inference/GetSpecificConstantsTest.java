package de.bklaiber.inference;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import de.bklaiber.Utils.QueryReader;
import de.bklaiber.testqueries.AbstractQueryTest;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Constant;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Sort;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.kbParser.log4KRReader.Log4KRReader;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

public class GetSpecificConstantsTest extends AbstractQueryTest {

	private Sort type;
	CanonicalMinimumGeneralization generalization = new CanonicalMinimumGeneralization();
	Collection<RelationalConditional> kb = null;

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

	@Test
	public void checkGetSpecificConstantsA() {
		type = new Sort("letter");
		Constant a = new Constant("a", type);

		Collection<Constant> constants = new ArrayList<>();
		kb = inference.getKnowledegbase();

		constants = generalization.getSpecificConstants(kb);

		assertEquals(a.toString(), constants.toString());

	}

}
