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
import edu.cs.ai.log4KR.logical.syntax.Tautology;
import edu.cs.ai.log4KR.math.types.Fraction;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.RelationalAtom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Constant;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Predicate;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Sort;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.kbParser.log4KRReader.Log4KRReader;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

public class ClasssifyTest extends AbstractQueryTest {

	@Before
	public void setup() {
		super.setup();

		inference.setKnowledgebase(new Log4KRReader(), new File("test/res/Birds.rcl"));

		queries = new Vector<RelationalConditional>(QueryReader.readQueries(new File("test/res/Birds.rcl")));

	}

	@Test
	public void test() {

		Sort bird = new Sort("bird");

		Predicate flies = new Predicate("flies", bird);

		Constant sylvester = new Constant("Sylvester", bird);
		Constant kirby = new Constant("Kirby", bird);
		Constant bully = new Constant("Bully", bird);
		Constant tweety = new Constant("Tweety", bird);

		RelationalAtom fliesSylvester = new RelationalAtom(flies, sylvester);
		RelationalAtom fliesKirby = new RelationalAtom(flies, kirby);
		RelationalAtom fliesBully = new RelationalAtom(flies, bully);
		RelationalAtom fliesTweety = new RelationalAtom(flies, tweety);

		double probability1 = 0.6636033970821146;
		double probability2 = 0.6636037951546276;
		double probability3 = 0.6636034383842102;
		double probabilityTweety = 0.008344314635674319;

		RelationalConditional sylvesterCond = new RelationalConditional(fliesSylvester, Tautology.create(),
				new Fraction(probability1));
		RelationalConditional kirbyCond = new RelationalConditional(fliesKirby, Tautology.create(),
				new Fraction(probability2));
		RelationalConditional bullyCond = new RelationalConditional(fliesBully, Tautology.create(),
				new Fraction(probability3));
		RelationalConditional tweetyCond = new RelationalConditional(fliesTweety, Tautology.create(),
				new Fraction(probabilityTweety));

		Collection<Collection<RelationalConditional>> classifications = new ArrayList<>();
		Collection<RelationalConditional> tweetyCollection = new ArrayList<>();
		tweetyCollection.add(tweetyCond);
		Collection<RelationalConditional> otherCollection = new ArrayList<>();
		otherCollection.add(sylvesterCond);
		otherCollection.add(kirbyCond);
		otherCollection.add(bullyCond);
		classifications.add(otherCollection);
		classifications.add(tweetyCollection);

		Collection<RelationalConditional> groundInstances = inference.ground(queries.elementAt(0));
		Collection<Collection<RelationalConditional>> classifiedClasses = inference.classify(queries.elementAt(0),
				groundInstances);
		System.out.println(classifications.toString());
		System.out.println(classifiedClasses.toString());

		assertEquals(classifications, classifiedClasses);

		assertEquals(classifications.size(), classifiedClasses.size());

	}

}
