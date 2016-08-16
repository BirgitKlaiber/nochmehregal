package de.bklaiber.queries;

import de.bklaiber.inference.Query;

public class QueryNotBirds {

	public static void main(String[] args) {
		Query queryBirds = new Query("ressources/fileQuerys/NotBirds.rcl");
		queryBirds.query();

	}

}
