package de.bklaiber.inference;

import java.util.Collection;

import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

public interface Generalization {

	public Collection<RelationalConditional> generalize(Collection<Collection<RelationalConditional>> classification);
}
