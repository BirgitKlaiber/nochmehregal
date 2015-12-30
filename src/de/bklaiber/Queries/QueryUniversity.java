package de.bklaiber.Queries;

import de.bklaiber.Inference.Query;

public class QueryUniversity
{

	public static void main(String[] args)
	{
		Query queryUniversity = new Query("ressources/fileQuerys/University.rcl");
		 queryUniversity.query();

	}

}
