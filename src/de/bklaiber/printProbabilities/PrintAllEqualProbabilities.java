package de.bklaiber.printProbabilities;

import java.util.ArrayList;

import de.bklaiber.types.ProbabilityConditional;

/**
 * 
 * 
 * @author klaiber
 * 
 *         Used to generate the output if the probabilities of all conditionals
 *         are equal.
 *
 */
public class PrintAllEqualProbabilities implements
		PrintConditionalProbabilities {

	private ArrayList<ProbabilityConditional> listOfConditionals;

	/**
	 * 
	 * Constructor of the class PrintAllEqualProbabilities.
	 * 
	 * @param listOfConditionals
	 */
	public PrintAllEqualProbabilities(
			ArrayList<ProbabilityConditional> listOfConditionals) {
		this.listOfConditionals = listOfConditionals;

	}// endofconstructor

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder probCond = new StringBuilder();
		boolean withoutProb = false;

		ProbabilityConditional probabilityConditional = listOfConditionals
				.get(0);

		probCond.append(probabilityConditional.relationalToString(withoutProb));

		return probCond.toString();
	}

}// endofPrintAllEqualProbabilities
