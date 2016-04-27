package de.bklaiber.testqueries;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import de.bklaiber.inference.Inference;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.kbParser.log4KRReader.Log4KRReader;

public class AlarmTest {

	@BeforeClass
	public static void setup() {
		Inference inference = new Inference();

		inference.setKnowledgebase(new Log4KRReader(), new File("test/res/Alarm.rcl"));
	}

	@Test
	public void queryTest() {

	}

}
