package de.bklaiber.inference;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Vector;

import org.junit.Test;

import de.bklaiber.testqueries.AbstractQueryTest;
import edu.cs.ai.log4KR.logical.syntax.Atom;
import edu.cs.ai.log4KR.logical.syntax.Formula;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.RelationalAtom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.AtomicConstraint;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Constant;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Predicate;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Sort;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Variable;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalFact;

public class GenerateSpecificConstraintTest extends AbstractQueryTest {

	private Sort type;
	private Sort[] argumentSorts;
	Formula<AtomicConstraint> specificConstraint = null;

	@Test
	public void checkSpecificGeneralization() {

		//generate an reflexive EqualityContraint
		CanonicalMinimumGeneralization generalization = new CanonicalMinimumGeneralization();

		Collection<Constant> constants = null;

		type = new Sort("letter");
		argumentSorts = new Sort[2];
		argumentSorts[0] = type;
		argumentSorts[1] = type;

		Variable u = new Variable("U", type);
		Variable v = new Variable("V", type);
		Constant a = new Constant("a", type);
		String P = "P";

		RelationalAtom atomUV = new RelationalAtom(new Predicate(P, argumentSorts), u, v);
		RelationalConditional c = new RelationalFact(atomUV);
		Collection<Atom<RelationalAtom>> atoms = new Vector<>();
		atoms.add(atomUV);

		constants = new Vector<Constant>();
		constants.add(a);

		specificConstraint = generalization.generateSpecificConstraint(c, atoms, constants);
		Vector<Formula<AtomicConstraint>> constraints = new Vector<Formula<AtomicConstraint>>();
		constraints.addElement(specificConstraint);

		Vector<String> generalizations = new Vector<String>();
		generalizations.addElement("((U=a + V=a) * U!=V)");

		assertEquals(generalizations.elementAt(0), constraints.elementAt(0).toString());

	}

}
