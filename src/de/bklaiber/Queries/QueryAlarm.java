package de.bklaiber.Queries;

import de.bklaiber.Inference.Query;

public class QueryAlarm
{
	public static void main(String[] args)
	{
		Query queryAlarm = new Query("ressources/fileQuerys/Alarm.rcl");
		 queryAlarm.query();

	}

}
