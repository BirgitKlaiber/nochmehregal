package de.bklaiber.types;

import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

public class ProbabilityConditional implements
		Comparable<ProbabilityConditional>
{
	private double probability;
	private RelationalConditional relationalConditional;
	private RelationalConditional relationalGroundConditional;
	

	public ProbabilityConditional(double probability,
		RelationalConditional relationalGroundConditional)
	{
		super();
		this.probability = probability;
		//this.relationalConditional = relationalConditional;
		this.relationalGroundConditional = relationalGroundConditional;
	}

	public RelationalConditional getRelationalGroundConditional()
	{
		return relationalGroundConditional;
	}

	public void setRelationalGroundConditional(
			RelationalConditional relationalGroundConditional)
	{
		this.relationalGroundConditional = relationalGroundConditional;
	}

	public double getProbability()
	{
		return probability;
	}

	public RelationalConditional getRelationalConditional()
	{
		return relationalConditional;
	}

	public void setProbability(double probability)
	{
		this.probability = probability;
	}

	public void setRelationalConditional(
			RelationalConditional relationalConditional)
	{
		this.relationalConditional = relationalConditional;
	}

	@Override
	public String toString()
	{
		if(roundScale(probability) == -1)
		{
			return ("P" + relationalGroundConditional.toString() + " ist eine unzulaessige Anfrage und kann nicht ermittelt werden.");
		}
		else
		{
		return relationalGroundConditional.toString() + "[" + roundScale(probability) +"]";
			}
	}
	
	public String relationalToString(boolean withoutProb)
	{
		//if(roundScale(probability) == -1)
		//{
			//return ("P" + relationalConditional.toString() + " ist eine unzulaessige Anfrage und kann nicht ermittelt werden.");
		//}
		//else
		//{
			if(withoutProb)
			{
			return relationalConditional.toString();
			}
			else
			{
				return relationalConditional.toString() + "[" + roundScale(probability) + "]";
			
			}
	
			//}
	}

	public static double roundScale(double d)
	{
		return Math.rint(d * 1000) / 1000;
	}// endofroundscale

	
		@Override
	public int compareTo(ProbabilityConditional o)
	{
		int comp = 0;

		double prob = o.getProbability();
		comp = Double.compare(prob, this.getProbability());

		return comp;
	}

}
