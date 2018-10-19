package de.bklaiber.testqueries;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
//@SuiteClasses({ BirdsTest.class })

@SuiteClasses({ BirdsTestT.class, FriendshipTest.class, MisanthropeTest.class })
public class AllTests {

}
