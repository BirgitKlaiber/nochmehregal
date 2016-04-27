package de.bklaiber;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.bklaiber.inference.InferenceTest;
import de.bklaiber.testqueries.AlarmTest;
import de.bklaiber.testqueries.BirdsTest;
import de.bklaiber.testqueries.FriendshipTest;
import de.bklaiber.testqueries.MisanthropeTest;

@RunWith(Suite.class)
@SuiteClasses({ InferenceTest.class, AlarmTest.class, BirdsTest.class, FriendshipTest.class, MisanthropeTest.class })
public class AllTests {

}
