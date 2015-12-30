package de.bklaiber.PrintProbabilities;

import java.util.ArrayList;

import de.bklaiber.Types.ProbabilityConditional;

/**
 * @author birgit klaiber
 *
 */
public class PrintNoEqualProbabilities implements PrintConditionalProbabilities
{

	private ArrayList<ProbabilityConditional> listOfConditionals;

	/**
	 * 
	 * 
	 * @param listOfConditionals
	 */
	public PrintNoEqualProbabilities(
			ArrayList<ProbabilityConditional> listOfConditionals)
	{
		this.listOfConditionals = listOfConditionals;

	}// endofconstructor

	@Override
	public String toString()
	{
		StringBuilder probCond = new StringBuilder();

		for (ProbabilityConditional probabilityConditional : listOfConditionals)
		{

			probCond.append(probabilityConditional.toString());
			probCond.append("\n");

		}// endfor
		return probCond.toString();
	}// endoftoString
}// endofPrintNoEqualProbabilities

