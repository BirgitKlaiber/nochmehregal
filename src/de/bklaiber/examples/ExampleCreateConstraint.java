package de.bklaiber.examples;

import edu.cs.ai.log4KR.logical.syntax.Formula;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.RelationalAtom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.AtomicConstraint;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.EqualityConstraint;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Predicate;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Sort;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Variable;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

public class ExampleCreateConstraint {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Sort sort1 = new Sort("sort1");

		Variable x = new Variable("X", sort1);
		Variable y = new Variable("Y", sort1);
		Variable z = new Variable("Z", sort1);

		EqualityConstraint constraint1 = new EqualityConstraint(x, y);
		EqualityConstraint constraint2 = new EqualityConstraint(y, z);
		EqualityConstraint constraint3 = new EqualityConstraint(x, z);

		//Formula<AtomicConstraint> c1 = (Formula<AtomicConstraint>) constraint1;
		//Formula<AtomicConstraint> c2 = (Formula<AtomicConstraint>) constraint2;

		Formula<AtomicConstraint> constraintAnd = constraint1.and(constraint2.or(constraint3));
		Formula<AtomicConstraint> constraintAndAnd = constraintAnd.and(constraint3);

		//Formula<AtomicConstraint> c1andc2 = c1.and(c2);

		RelationalConditional con2 = new RelationalConditional(new RelationalAtom(new Predicate("B", sort1), x),
				new RelationalAtom(new Predicate("A", sort1), x), constraintAnd);
		String conStr2 = new String(con2.toString());

		RelationalConditional con1 = new RelationalConditional(new RelationalAtom(new Predicate("B", sort1), x),
				new RelationalAtom(new Predicate("A", sort1), x), constraintAndAnd);
		String conStr1 = new String(con1.toString());

		/*
		RelationalConditional con1a = new RelationalConditional(new RelationalAtom(new Predicate("B", sort1), x),
				new RelationalAtom(new Predicate("A", sort1), x), c1andc2);
		String conStr1a = new String(con1a.toString());
		
		*/

		System.out.println(conStr2);

		System.out.println(conStr1);

		//System.out.println(conStr1a);
	}

}
