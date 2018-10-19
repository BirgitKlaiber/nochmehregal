package de.bklaiber.testqueries;

import org.junit.runner.JUnitCore;

public abstract class Testrunner {

	public static void main(String[] args) throws Exception {
		JUnitCore.main("de.bklaiber.inference.AllTests");
	}
}
