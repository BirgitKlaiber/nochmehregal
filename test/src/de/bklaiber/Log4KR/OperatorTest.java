package de.bklaiber.Log4KR;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.cs.ai.log4KR.logical.syntax.Formula;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.AtomicConstraint;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.EqualityConstraint;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Sort;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Variable;

public class OperatorTest {

	@Test
	public void testOperator() {
		Sort sort1 = new Sort("sort1");

		Variable x = new Variable("X", sort1);
		Variable y = new Variable("Y", sort1);
		Variable z = new Variable("Z", sort1);

		EqualityConstraint constraint1 = new EqualityConstraint(x, y);
		EqualityConstraint constraint2 = new EqualityConstraint(y, z);
		EqualityConstraint constraint3 = new EqualityConstraint(x, z);

		Formula<AtomicConstraint> constraintAnd1 = constraint1.and(constraint2.or(constraint3));
		Formula<AtomicConstraint> constraintAnd2 = constraint2.and(constraint1.or(constraint3));
		Formula<AtomicConstraint> constraintAndOrAnd = constraintAnd1.and(constraint3);
		Formula<AtomicConstraint> constraintAndAnd = constraint1.and(constraint2);
		Formula<AtomicConstraint> constraintAndOrOrAndOr = constraintAnd1.or(constraintAnd2);

		assertEquals(constraintAnd1.toString(), "(X=Y * (Y=Z + X=Z))");
		assertEquals(constraintAndOrAnd.toString(), "((X=Y * (Y=Z + X=Z)) * X=Z)");
		assertEquals(constraintAndAnd.toString(), "X=Y * Y=Z");
		assertEquals(constraintAndOrOrAndOr.toString(), "((X=Y * (Y=Z + X=Z)) + (Y=Z * (X=Y + X=Z)))");
		System.out.println(constraintAnd1);
		System.out.println(constraintAnd2);
		System.out.println(constraintAndOrOrAndOr);
	}

}
