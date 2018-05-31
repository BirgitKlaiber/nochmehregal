
package de.bklaiber.queries;

import de.bklaiber.inference.Query;

public class QueryBirds {

	public static void main(String[] args) {
		Query queryBirds = new Query("ressources/fileQuerys/Birds.rcl");
		queryBirds.query();
	}
}
