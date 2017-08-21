package de.bklaiber.queries;

import de.bklaiber.inference.Query;

public class QuerySportExNFam {

	public static void main(String[] args) {

		Query querySportEx = new Query("ressources/fileQuerys/SportExNFam.rcl");
		querySportEx.query();

	}

}
