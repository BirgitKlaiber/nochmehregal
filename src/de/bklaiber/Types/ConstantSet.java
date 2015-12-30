package de.bklaiber.Types;

import java.util.*;

import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Constant;

public class ConstantSet extends HashSet<Constant>
{

	private ProbabilityConditional probabilityConditional;

	public ConstantSet()
	{
		super();
	}

	public ConstantSet(Collection<? extends Constant> c)
	{
		super(c);
	}

	public ConstantSet(int initialCapacity, float loadFactor)
	{
		super(initialCapacity, loadFactor);
	}

	public ConstantSet(int initialCapacity)
	{
		super(initialCapacity);
	}

	public ProbabilityConditional getProbabilityConditional()
	{
		return probabilityConditional;
	}

	public void setProbabilityConditional(
			ProbabilityConditional probabilityConditional)
	{
		this.probabilityConditional = probabilityConditional;
	}

	/*public ConstantSet generateConstantSet()

	{
		Set<Constant> setOfConstants = new ConstantSet();
		Collection<Constant> collectionOfConstants = null;

	
		Collection<Atom<RelationalAtom>> atomsOfClass = probabilityConditional
				.getRelationalGroundConditional().getAtoms();

		for (Atom<RelationalAtom> atom : atomsOfClass)
		{
			RelationalAtom relationalAtom = (RelationalAtom) atom;
			collectionOfConstants = relationalAtom.getConstants();
			for (Constant constant : collectionOfConstants)
			{
				setOfConstants.add(constant);

			}// endfor

		}// endfor

		return (ConstantSet) setOfConstants;

	}
*/	

}
