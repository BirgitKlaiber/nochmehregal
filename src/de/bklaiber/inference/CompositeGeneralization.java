package de.bklaiber.inference;

import java.util.Collection;

public interface CompositeGeneralization {

	public void addGeneralization();

	public void removeGeneralization();

	public Collection<Generalization> getChildGeneralizations();

}
