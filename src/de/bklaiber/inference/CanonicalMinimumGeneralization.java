package de.bklaiber.inference;

import java.util.Collection;
import java.util.Iterator;

import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

/*
 * This class is
 */
public class CanonicalMinimumGeneralization extends AbstractGeneralization {

	@Override
	public Collection<RelationalConditional> generalize(RelationalConditional c,
			Collection<Collection<RelationalConditional>> classifiedClasses) {

		Collection<RelationalConditional> generalizedClasses = null;

		for (Iterator<Collection<RelationalConditional>> iterator = classifiedClasses.iterator(); iterator.hasNext();) {
			Collection<RelationalConditional> equivalenceClass = iterator.next();

			for (Iterator<RelationalConditional> elements = equivalenceClass.iterator(); elements.hasNext();) {
				RelationalConditional element = (RelationalConditional) elements.next();

			}
		}

		return generalizedClasses;
	}

}
