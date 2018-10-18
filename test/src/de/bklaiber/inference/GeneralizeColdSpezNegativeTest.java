package de.bklaiber.inference;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Vector;

import org.junit.Test;

import edu.cs.ai.log4KR.logical.syntax.ElementaryConjunction;
import edu.cs.ai.log4KR.logical.syntax.Literal;
import edu.cs.ai.log4KR.logical.syntax.Tautology;
import edu.cs.ai.log4KR.math.types.Fraction;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.RelationalAtom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.InequalityConstraint;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Constant;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Predicate;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Sort;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Term;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Variable;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalFact;

public class GeneralizeColdSpezNegativeTest {

	private Sort person;
	private Sort[] argumentSorts;
	private Inference inference = new Inference();

	/**
	 * Tests if the generalization produces the results expected.
	 */

	@Test
	public void checkGeneralizationNegative() {
		inference.setGeneralization(new CanonicalMinimumGeneralization());

		Vector<String> generalizations = new Vector<String>();

		generalizations.addElement("(contact(X,Y) * cold(Y))[0.0]<X=Y>");
		generalizations.addElement("(contact(X,Y) * cold(Y))[0.03588]<((X=anna + Y=anna) * X!=Y)>");
		generalizations.addElement("(contact(X,Y) * cold(Y))[0.020752]<(X!=anna * Y!=anna * X!=Y)>");

		Collection<Collection<RelationalConditional>> classifiedClasses = new Vector<>();
		Collection<RelationalConditional> classifiedClass1 = new Vector<>();
		Collection<RelationalConditional> classifiedClass2 = new Vector<>();
		Collection<RelationalConditional> classifiedClass3 = new Vector<>();
		Collection<RelationalConditional> kb = new Vector<>();

		person = new Sort("person");
		argumentSorts = new Sort[2];
		argumentSorts[0] = person;
		argumentSorts[1] = person;

		Predicate contact = new Predicate("contact", person, person);
		Predicate cold = new Predicate("cold", person);
		Predicate sucseptible = new Predicate("sucseptible", person);

		boolean positive = true;

		LinkedList lits1 = new LinkedList<>();
		LinkedList lits2 = new LinkedList<>();
		LinkedList lits3 = new LinkedList<>();
		LinkedList lits4 = new LinkedList<>();
		LinkedList lits5 = new LinkedList<>();
		LinkedList lits6 = new LinkedList<>();
		LinkedList lits7 = new LinkedList<>();
		LinkedList lits8 = new LinkedList<>();
		LinkedList lits9 = new LinkedList<>();

		Term[] arguments11 = new Term[2];
		arguments11[0] = new Constant("anna", person);
		arguments11[1] = new Constant("carl", person);

		Term[] arguments12 = new Term[1];
		arguments12[0] = new Constant("carl", person);

		Literal literal_11 = new Literal<>(new RelationalAtom(contact, arguments11), positive);
		Literal literal_12 = new Literal<>(new RelationalAtom(cold, arguments12), positive);

		lits1.add(literal_11);
		lits1.add(literal_12);

		//lits1.add(new RelationalAtom(contact, arguments11));
		//lits1.add(new RelationalAtom(cold, arguments12));

		Term[] arguments21 = new Term[2];
		arguments21[0] = new Constant("anna", person);
		arguments21[1] = new Constant("bob", person);

		Term[] arguments22 = new Term[1];
		arguments22[0] = new Constant("bob", person);

		Literal literal_21 = new Literal<>(new RelationalAtom(contact, arguments21), positive);
		Literal literal_22 = new Literal<>(new RelationalAtom(cold, arguments22), positive);

		lits2.add(literal_21);
		lits2.add(literal_22);

		Term[] arguments31 = new Term[2];
		arguments31[0] = new Constant("carl", person);
		arguments31[1] = new Constant("anna", person);

		Term[] arguments32 = new Term[1];
		arguments32[0] = new Constant("anna", person);

		Literal literal_31 = new Literal<>(new RelationalAtom(contact, arguments31), positive);
		Literal literal_32 = new Literal<>(new RelationalAtom(cold, arguments32), positive);

		lits3.add(literal_31);
		lits3.add(literal_32);

		Term[] arguments41 = new Term[2];
		arguments41[0] = new Constant("bob", person);
		arguments41[1] = new Constant("carl", person);

		Term[] arguments42 = new Term[1];
		arguments42[0] = new Constant("carl", person);

		Literal literal_41 = new Literal<>(new RelationalAtom(contact, arguments41), positive);
		Literal literal_42 = new Literal<>(new RelationalAtom(cold, arguments42), positive);

		lits4.add(literal_41);
		lits4.add(literal_42);

		Term[] arguments51 = new Term[2];
		arguments51[0] = new Constant("carl", person);
		arguments51[1] = new Constant("bob", person);

		Term[] arguments52 = new Term[1];
		arguments52[0] = new Constant("bob", person);

		Literal literal_51 = new Literal<>(new RelationalAtom(contact, arguments51), positive);
		Literal literal_52 = new Literal<>(new RelationalAtom(cold, arguments52), positive);

		lits5.add(literal_51);
		lits5.add(literal_52);

		Term[] arguments61 = new Term[2];
		arguments61[0] = new Constant("bob", person);
		arguments61[1] = new Constant("anna", person);

		Term[] arguments62 = new Term[1];
		arguments62[0] = new Constant("anna", person);

		Literal literal_61 = new Literal<>(new RelationalAtom(contact, arguments61), positive);
		Literal literal_62 = new Literal<>(new RelationalAtom(cold, arguments62), positive);

		lits6.add(literal_61);
		lits6.add(literal_62);

		RelationalConditional ac = new RelationalConditional(new ElementaryConjunction<>(lits1), Tautology.create(),
				new Fraction(0.03588));
		RelationalConditional ab = new RelationalConditional(new ElementaryConjunction<>(lits2), Tautology.create(),
				new Fraction(0.03588));
		RelationalConditional ca = new RelationalConditional(new ElementaryConjunction<>(lits3), Tautology.create(),
				new Fraction(0.03588));
		RelationalConditional bc = new RelationalConditional(new ElementaryConjunction<>(lits4), Tautology.create(),
				new Fraction(0.020752));
		RelationalConditional cb = new RelationalConditional(new ElementaryConjunction<>(lits5), Tautology.create(),
				new Fraction(0.020752));
		RelationalConditional ba = new RelationalConditional(new ElementaryConjunction<>(lits6), Tautology.create(),
				new Fraction(0.03588));

		classifiedClass1.add(ac);
		classifiedClass1.add(ab);
		classifiedClass1.add(ca);
		classifiedClass1.add(ba);

		classifiedClass3.add(bc);
		classifiedClass3.add(cb);

		Term[] arguments71 = new Term[2];
		arguments71[0] = new Constant("bob", person);
		arguments71[1] = new Constant("bob", person);

		Term[] arguments72 = new Term[1];
		arguments72[0] = new Constant("bob", person);

		Literal literal_71 = new Literal<>(new RelationalAtom(contact, arguments71), positive);
		Literal literal_72 = new Literal<>(new RelationalAtom(cold, arguments72), positive);

		lits7.add(literal_71);
		lits7.add(literal_72);

		Term[] arguments81 = new Term[2];
		arguments81[0] = new Constant("anna", person);
		arguments81[1] = new Constant("anna", person);

		Term[] arguments82 = new Term[1];
		arguments82[0] = new Constant("anna", person);

		Literal literal_81 = new Literal<>(new RelationalAtom(contact, arguments81), positive);
		Literal literal_82 = new Literal<>(new RelationalAtom(cold, arguments82), positive);

		lits8.add(literal_81);
		lits8.add(literal_82);

		Term[] arguments91 = new Term[2];
		arguments91[0] = new Constant("carl", person);
		arguments91[1] = new Constant("carl", person);

		Term[] arguments92 = new Term[1];
		arguments92[0] = new Constant("carl", person);

		Literal literal_91 = new Literal<>(new RelationalAtom(contact, arguments91), positive);
		Literal literal_92 = new Literal<>(new RelationalAtom(cold, arguments92), positive);

		lits9.add(literal_91);
		lits9.add(literal_92);

		RelationalConditional bb = new RelationalConditional(new ElementaryConjunction<>(lits7), Tautology.create(),
				new Fraction(0.0));
		RelationalConditional aa = new RelationalConditional(new ElementaryConjunction<>(lits8), Tautology.create(),
				new Fraction(0.0));
		RelationalConditional cc = new RelationalConditional(new ElementaryConjunction<>(lits9), Tautology.create(),
				new Fraction(0.0));

		classifiedClass2.add(bb);
		classifiedClass2.add(aa);
		classifiedClass2.add(cc);

		classifiedClasses.add(classifiedClass1);
		classifiedClasses.add(classifiedClass2);
		classifiedClasses.add(classifiedClass3);

		CanonicalMinimumGeneralization generalization = (CanonicalMinimumGeneralization) inference.getGeneralization();

		LinkedList literals = new LinkedList<>();
		Term[] arguments101 = new Term[2];
		arguments101[0] = new Variable("X", person);
		arguments101[1] = new Variable("Y", person);
		Term[] arguments102 = new Term[1];
		arguments102[0] = new Variable("Y", person);

		Literal literal_101 = new Literal<>(new RelationalAtom(contact, arguments101), positive);
		Literal literal_102 = new Literal<>(new RelationalAtom(cold, arguments102), positive);

		literals.add(literal_101);
		literals.add(literal_102);

		//TODO kb
		LinkedList lits10 = new LinkedList<>();
		LinkedList lits11 = new LinkedList<>();
		LinkedList lits12 = new LinkedList<>();
		LinkedList lits13 = new LinkedList<>();
		LinkedList lits14 = new LinkedList<>();
		LinkedList lits15 = new LinkedList<>();

		Term[] arguments1011 = new Term[2];
		arguments1011[0] = new Variable("X", person);
		arguments1011[1] = new Variable("Y", person);

		Term[] arguments1011a = new Term[2];
		arguments1011a[0] = new Variable("Y", person);
		arguments1011a[1] = new Variable("X", person);

		Term[] arguments1012 = new Term[1];
		arguments1012[0] = new Variable("Y", person);

		Literal literal_1011 = new Literal<>(new RelationalAtom(contact, arguments1011), positive);
		Literal literal_1012 = new Literal<>(new RelationalAtom(cold, arguments1012), positive);

		lits10.add(literal_1011);
		lits10.add(literal_1012);

		Term[] arguments1012a = new Term[1];
		arguments1012a[0] = new Variable("X", person);

		Term[] arguments1012b = new Term[2];
		arguments1012b[0] = new Variable("X", person);
		arguments1012b[1] = new Variable("X", person);

		Term[] anna = new Term[1];
		anna[0] = new Constant("anna", person);

		Literal literal_10111 = new Literal<>(new RelationalAtom(contact, arguments1011), positive);
		Literal literal_10112 = new Literal<>(new RelationalAtom(cold, arguments1012), positive);

		RelationalConditional cond1 = new RelationalConditional(new RelationalAtom(cold, arguments1012a),
				new ElementaryConjunction<>(lits10), new Fraction(0.6),
				new InequalityConstraint(new Variable("X", person), new Variable("Y", person)));

		RelationalConditional cond2 = new RelationalConditional(new RelationalAtom(cold, arguments1012a),
				new RelationalAtom(sucseptible, arguments1012a), new Fraction(0.1));

		RelationalConditional cond3 = new RelationalConditional(new RelationalAtom(cold, arguments1012a),
				Tautology.create(), new Fraction(0.05),
				new InequalityConstraint(new Variable("X", person), new Variable("anna", person)));

		RelationalConditional cond4 = new RelationalConditional(new RelationalAtom(cold, anna), Tautology.create(),
				new Fraction(0.95));

		RelationalConditional cond5 = new RelationalConditional(new RelationalAtom(contact, arguments1011),
				new RelationalAtom(contact, arguments1011a), new Fraction(1.0),
				new InequalityConstraint(new Variable("X", person), new Variable("Y", person)));

		RelationalConditional cond6 = new RelationalConditional(new RelationalAtom(contact, arguments1012b),
				Tautology.create(), new Fraction(0.0));
		kb.add(cond1);
		kb.add(cond2);
		kb.add(cond3);
		kb.add(cond4);
		kb.add(cond5);
		kb.add(cond6);

		Vector<RelationalConditional> generalizedClasses = new Vector<RelationalConditional>(generalization
				.generalizeNegative(new RelationalFact(new ElementaryConjunction<>(literals), Tautology.create()),
						classifiedClasses, kb));

		assertEquals(generalizations.elementAt(0), generalizedClasses.elementAt(0).toString());
		assertEquals(generalizations.elementAt(1), generalizedClasses.elementAt(1).toString());
		assertEquals(generalizations.elementAt(2), generalizedClasses.elementAt(2).toString());
		assertEquals(generalizations.size(), generalizedClasses.size());
		System.out.println("Negative Generalisierung: ");
		System.out.println("Erwartete Ausgabe: " + generalizations.toString());
		System.out.println("Ausgabe der Komponente: " + generalizations.toString());
	}
}