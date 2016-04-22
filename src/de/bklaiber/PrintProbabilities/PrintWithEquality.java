package de.bklaiber.PrintProbabilities;

import java.util.ArrayList;
import java.util.Collection;

import de.bklaiber.Types.ProbabilityConditional;
import edu.cs.ai.log4KR.logical.syntax.Atom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.RelationalAtom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Constant;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Variable;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

/**
 * 
 * Generate a conditional with equality constraint.
 * 
 * @author Birgit Klaiber
 * 
 * 
 *
 */
public class PrintWithEquality {
	private Collection<Constant> constants;
	private ProbabilityConditional probabilityConditional;
	private RelationalConditional relationalConditional;

	Collection<Atom<RelationalAtom>> relationalAtoms;

	/**
	 * 
	 * First constructor of PrintWithEquality.
	 * 
	 * @param listOfConditionals
	 * @param constants
	 */
	public PrintWithEquality(
			ArrayList<ProbabilityConditional> listOfConditionals,
			Collection<Constant> constants) {
		this.constants = constants;
		this.probabilityConditional = listOfConditionals.get(0);
		this.relationalConditional = probabilityConditional
				.getRelationalConditional();
		this.relationalAtoms = relationalConditional.getAtoms();

	}// endofconstructor

	/**
	 * 
	 * Second constructor of PrintWithEqulity
	 * 
	 * @param probabilityConditional
	 * @param constants
	 */
	public PrintWithEquality(ProbabilityConditional probabilityConditional,
			Collection<Constant> constants) {
		this.constants = constants;
		this.probabilityConditional = probabilityConditional;
		this.relationalConditional = probabilityConditional
				.getRelationalConditional();
		this.relationalAtoms = relationalConditional.getAtoms();

	}// endofconstructor

	/**
	 * 
	 * Sets constants.
	 * 
	 * @param constants
	 */
	public void setConstant(Collection<Constant> constants) {
		this.constants = constants;
	}

	@Override
	public String toString() {
		StringBuilder probCond = new StringBuilder();
		Collection<Variable> variables = null;

		for (Atom<RelationalAtom> atom : relationalAtoms) {
			variables = ((RelationalAtom) atom).getVariables();
		}// endoffor

		probCond.append(probabilityConditional.relationalToString(true));
		probCond.append("["
				+ Math.rint(probabilityConditional.getProbability() * 1000)
				/ 1000 + "]");
		for (Variable variable : variables) {

			for (Constant c : constants) {
				probCond.append("<" + variable + "=" + c.toString() + "> + ");

			} // endoffor
		}//endoffor
		probCond.deleteCharAt(probCond.length() - 1);
		probCond.deleteCharAt(probCond.length() - 1);
		// probCond.deleteCharAt(probCond.length()-1);

		return probCond.toString();
	}// endoftostring

}// endofPrintWithEquality
