package de.bklaiber.queries;

import de.bklaiber.inference.Query;

public class Query2ClassesBirds {

	public static void main(String[] args) {
		Query query2Birds = new Query("ressources/fileQuerys/Birds2classes.rcl");
		query2Birds.query();

	}

}
