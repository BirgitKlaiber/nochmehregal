package de.bklaiber.inference;

import org.junit.Test;

import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Predicate;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Sort;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Term;

public class ConditionalToStringTest {

	@Test
	public void test() {
		Sort birds = new Sort("birds");
		Predicate flies = new Predicate("flies", birds);
		Term[] termArray = new Term[1];
		//termArray[0] = new Literal<>(atom); 
		//Formula<RelationalAtom> consquence = new RelationalAtom(flies, termArray); 
		//RelationalFact fact = new RelationalFact(consequence);
	}

}
