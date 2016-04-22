package de.bklaiber.queries;

import de.bklaiber.inference.Query;

public class QueryAlarm
{
	public static void main(String[] args)
	{
		Query queryAlarm = new Query("ressources/fileQuerys/Alarm.rcl");
		 queryAlarm.query();

	}

}
