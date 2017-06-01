package de.bklaiber.inference;

import java.util.Collection;

import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

public abstract class AbstractGeneralization implements Generalization {

	public Collection<RelationalConditional> generalize(RelationalConditional c,
			Collection<Collection<RelationalConditional>> classification) {
		return null;

	}
}
