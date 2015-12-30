package de.bklaiber.PrintProbabilities;

import java.util.ArrayList;

import de.bklaiber.Types.ProbabilityConditional;

public class PrintSingleException
{
	private ArrayList<ProbabilityConditional> listOfConditionals;

	public PrintSingleException(
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

		}// endfor
		return probCond.toString();
	}// endoftoString

}//endofPrintSingleException
