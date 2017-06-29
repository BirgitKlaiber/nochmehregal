package de.bklaiber.inference;

import edu.cs.ai.log4KR.logical.syntax.Formula;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.constraints.AtomicConstraint;

public interface LogicalOptimization {
	public Formula<AtomicConstraint> optimizeLogical(Formula<AtomicConstraint> constraint);
}
