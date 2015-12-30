package de.bklaiber.Queries;

import de.bklaiber.Inference.Query;

public class QueryBirdsDiffer
{
	public static void main(String[] args)
	{
	 Query queryBirds = new Query("ressources/fileQuerys/Birds_differ.rcl");
	 queryBirds.query();
	}

}
