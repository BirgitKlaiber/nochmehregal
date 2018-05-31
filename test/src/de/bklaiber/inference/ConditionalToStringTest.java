package de.bklaiber.inference;

import static org.junit.Assert.assertEquals;

import java.util.Vector;

import org.junit.Test;

import edu.cs.ai.log4KR.logical.syntax.Disjunction;
import edu.cs.ai.log4KR.logical.syntax.Formula;
import edu.cs.ai.log4KR.math.types.Fraction;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.RelationalAtom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.AtomicConstraint;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.EqualityConstraint;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Constant;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Predicate;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Sort;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Variable;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalFact;

public class ConditionalToStringTest {

	@Test
	public void test() {

		Sort bird = new Sort("bird");

		Variable x = new Variable("X", bird);
		Constant tweety = new Constant("Tweety", bird);
		Predicate flies = new Predicate("flies", bird);
		RelationalAtom fliesX = new RelationalAtom(flies, x);
		RelationalAtom fliesTweety = new RelationalAtom(flies, tweety);
		Formula<AtomicConstraint> constraint = null;

		Formula<AtomicConstraint> sylvester = new EqualityConstraint(x, new Constant("Sylvester", bird));
		Formula<AtomicConstraint> bully = new EqualityConstraint(x, new Constant("Bully", bird));
		Formula<AtomicConstraint> kirby = new EqualityConstraint(x, new Constant("Kirby", bird));

		constraint = new Disjunction<>(new Disjunction<>(sylvester, kirby), bully);

		RelationalConditional fact = new RelationalFact(fliesX, new Fraction(1, 2), constraint);
		RelationalConditional tweetyFact = new RelationalFact(fliesTweety, 0.008344314635674319);

		Vector<RelationalConditional> generalizedClasses = new Vector<RelationalConditional>();
		generalizedClasses.add(tweetyFact);
		generalizedClasses.add(fact);

		assertEquals("(flies(X))[0.5]<((X=Sylvester + X=Kirby) + X=Bully)>", fact.toString());
		assertEquals("(flies(Tweety))[0.008344314635674319]", tweetyFact.toString());
		assertEquals("(flies(Tweety))[0.008344314635674319]", generalizedClasses.elementAt(0).toString());
		assertEquals("(flies(X))[0.5]<((X=Sylvester + X=Kirby) + X=Bully)>",
				generalizedClasses.elementAt(1).toString());

	}

}
