package de.bklaiber.queries;

import de.bklaiber.inference.Query;

public class QueryColdTrans {
	public static void main(String[] args) {
		Query queryColdTrans = new Query("ressources/fileQuerys/ColdTrans.rcl");
		queryColdTrans.query();

	}

}
