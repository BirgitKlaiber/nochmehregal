package de.bklaiber.inference;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.junit.Test;

import edu.cs.ai.log4KR.logical.syntax.Atom;
import edu.cs.ai.log4KR.logical.syntax.Tautology;
import edu.cs.ai.log4KR.math.types.Fraction;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.RelationalAtom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Constant;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Predicate;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Sort;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Variable;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

public class HandleReflexiveTest {

	@Test
	public void checkHandleReflexive() {

		String reflexiveString = new String("(likes(U,V))[0.0]<U=V>");

		Collection<Atom<RelationalAtom>> atomCons = null;
		Collection<Atom<RelationalAtom>> atomAnt = null;

		Sort group = new Sort("Group");

		Variable U = new Variable("U", group);
		Variable V = new Variable("V", group);

		LinkedList<Constant> constants = new LinkedList<Constant>();
		Constant a = new Constant("a", group);
		constants.add(a);
		Constant b = new Constant("b", group);
		constants.add(b);
		Constant c = new Constant("c", group);
		constants.add(c);

		Predicate likes = new Predicate("likes", group, group);

		double probability = 0.0;

		RelationalAtom likesaa = new RelationalAtom(likes, a, a);
		RelationalAtom likesbb = new RelationalAtom(likes, b, b);
		RelationalAtom likescc = new RelationalAtom(likes, c, c);

		RelationalConditional aaCond = new RelationalConditional(likesaa, Tautology.create(),
				new Fraction(probability));
		RelationalConditional bbCond = new RelationalConditional(likesbb, Tautology.create(),
				new Fraction(probability));
		RelationalConditional ccCond = new RelationalConditional(likescc, Tautology.create(),
				new Fraction(probability));

		Collection<RelationalConditional> reflexiveClass = new ArrayList<>();
		reflexiveClass.add(aaCond);
		reflexiveClass.add(bbCond);
		reflexiveClass.add(ccCond);

		RelationalConditional handledReflexiveClass = null;

		//handledReflexiveClass = handleReflexive(reflexiveClass);

		assertEquals(reflexiveString, handledReflexiveClass.toString());

	}

}
