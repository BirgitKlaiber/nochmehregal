package de.bklaiber.queries;

import de.bklaiber.inference.Query;

public class QueryBirdsWithoutNull {

	public static void main(String[] args) {
		Query queryBirdsWith2 = new Query("ressources/fileQuerys/BirdsWithoutNull.rcl");
		queryBirdsWith2.query();

	}

}
