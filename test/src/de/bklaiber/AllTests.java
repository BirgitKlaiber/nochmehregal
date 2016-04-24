package de.bklaiber;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.bklaiber.inference.InferenceTest;

@RunWith(Suite.class)
@SuiteClasses({ InferenceTest.class })
public class AllTests {

}
