package de.bklaiber.inference;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.HashSet;
import java.util.Vector;

import org.junit.Test;

import de.bklaiber.testqueries.AbstractQueryTest;
import edu.cs.ai.log4KR.logical.syntax.Atom;
import edu.cs.ai.log4KR.logical.syntax.Formula;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.RelationalAtom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.AtomicConstraint;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Predicate;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Sort;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Variable;

public class ReflexiveTest extends AbstractQueryTest {

	private Sort type;
	private Sort[] argumentSorts;
	Formula<AtomicConstraint> reflexiveConstraint = null;

	@Test
	public void testReflexiveGeneralization() {

		//generate an reflexive EqualityContraint
		CanonicalMinimumGeneralization generalization = new CanonicalMinimumGeneralization();
		Collection<Atom<RelationalAtom>> atomsOfQuery = null;
		type = new Sort("letter");
		argumentSorts = new Sort[2];
		argumentSorts[0] = type;
		argumentSorts[1] = type;

		Variable u = new Variable("U", type);
		Variable v = new Variable("V", type);
		String P = "P";

		RelationalAtom atomUV = new RelationalAtom(new Predicate(P, argumentSorts), u, v);
		atomsOfQuery = new HashSet<>();

		atomsOfQuery.add(atomUV);

		reflexiveConstraint = generalization.generateReflexiveConstraint(atomsOfQuery);
		Vector<Formula<AtomicConstraint>> constraints = new Vector<Formula<AtomicConstraint>>();
		constraints.addElement(reflexiveConstraint);

		Vector<String> generalizations = new Vector<String>();
		generalizations.addElement("U=V");
		assertEquals(generalizations.elementAt(0), constraints.elementAt(0).toString());

	}

}
