package de.bklaiber.testqueries;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import de.bklaiber.inference.Inference;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.kbParser.log4KRReader.Log4KRReader;

public class FriendshipTest {
	@BeforeClass
	public static void setup() {
		Inference inference = new Inference();

		inference.setKnowledgebase(new Log4KRReader(), new File("test/res/Friendship.rcl"));
	}

	@Test
	public void queryTest() {

	}

}
