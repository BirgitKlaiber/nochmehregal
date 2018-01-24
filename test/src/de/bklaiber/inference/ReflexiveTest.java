package de.bklaiber.inference;

import java.util.Collection;
import java.util.HashSet;
import java.util.Vector;

import org.junit.Test;

import de.bklaiber.testqueries.AbstractQueryTest;
import edu.cs.ai.log4KR.logical.syntax.Atom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.RelationalAtom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Constant;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Predicate;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Sort;

public class ReflexiveTest extends AbstractQueryTest {

	private Sort type;

	@Test
	public void testReflexiveGeneralization() {
		CanonicalMinimumGeneralization generalization = new CanonicalMinimumGeneralization();
		Collection<Atom<RelationalAtom>> atomsOfQuery = null;
		Constant a = new Constant("a", type);
		Constant b = new Constant("b", type);
		Constant c = new Constant("c", type);
		String rel = "rel";
		RelationalAtom atomAa = new RelationalAtom(new Predicate(rel, type, type), a, a);
		RelationalAtom atomAb = new RelationalAtom(new Predicate(rel, type, type), b, b);
		RelationalAtom atomAc = new RelationalAtom(new Predicate(rel, type, type), c, c);
		atomsOfQuery = new HashSet<>();
		atomsOfQuery.add(atomAa);
		atomsOfQuery.add(atomAb);
		atomsOfQuery.add(atomAc);
		//generalization.generateReflexiveConstraint(atomsOfQuery);
		Vector<String> generalizations = new Vector<String>();
		generalizations.addElement("(rel(U,V))");
		//assertEquals(generalizations.elementAt(0), generalization.elementAt(0).toString());
		//assertEquals(generalizations.elementAt(1), generalization.elementAt(1).toString());

	}

}
