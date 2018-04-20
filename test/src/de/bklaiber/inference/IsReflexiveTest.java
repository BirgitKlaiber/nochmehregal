package de.bklaiber.inference;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;

import de.bklaiber.testqueries.AbstractQueryTest;
import edu.cs.ai.log4KR.logical.syntax.Atom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.RelationalAtom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Constant;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Predicate;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Sort;

public class IsReflexiveTest extends AbstractQueryTest {

	private Sort type;
	private Sort[] argumentSorts;
	private boolean isReflexive = false;
	//Formula<AtomicConstraint> reflexiveConstraint = null;

	@Test
	public void testIsReflexive() {
		CanonicalMinimumGeneralization generalization = new CanonicalMinimumGeneralization();
		Collection<Atom<RelationalAtom>> atomsOfQuery = null;
		type = new Sort("letter");
		argumentSorts = new Sort[2];
		argumentSorts[0] = type;
		argumentSorts[1] = type;
		Constant a = new Constant("a", type);
		Constant b = new Constant("b", type);
		Constant c = new Constant("c", type);
		String P = "P";
		RelationalAtom atomAa = new RelationalAtom(new Predicate(P, argumentSorts), a, a);
		RelationalAtom atomAb = new RelationalAtom(new Predicate(P, argumentSorts), b, b);
		RelationalAtom atomAc = new RelationalAtom(new Predicate(P, argumentSorts), c, c);
		atomsOfQuery = new HashSet<>();
		atomsOfQuery.add(atomAa);
		atomsOfQuery.add(atomAb);
		atomsOfQuery.add(atomAc);

		//TODO an dieser Stelle müssen aus dem Atomen Konditionale erzeugt werden und in eine Collection<RelationalConditional> gepack erden

		//generalization.isReflexive(classifiedClass)

		//hier muss ein Test rein, ob die Methode isReflexive aus CanonicalMinimumGeneralizaiton einen boolean liefert
		//assertEquals(generalizations.elementAt(0), constraints.elementAt(0).toString());

	}

}
