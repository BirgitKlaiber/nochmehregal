package de.bklaiber.queries;

import de.bklaiber.inference.Query;

public class QueryTestReflexive {

	public static void main(String[] args) {
		Query queryBirds = new Query("ressources/fileQuerys/TestReflexive.rcl");
		queryBirds.query();
	}

}
