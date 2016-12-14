package de.bklaiber;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.bklaiber.inference.ClassifierTest;
import de.bklaiber.inference.InferenceTest;
import de.bklaiber.testqueries.AlarmTest;
import de.bklaiber.testqueries.BirdsTest;
import de.bklaiber.testqueries.FriendshipTest;
import de.bklaiber.testqueries.MisanthropeTest;

@RunWith(Suite.class)
@SuiteClasses({ AlarmTest.class, BirdsTest.class, ClassifierTest.class, FriendshipTest.class, InferenceTest.class,
		MisanthropeTest.class })
public class AllTests {

}
