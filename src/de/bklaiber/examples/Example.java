package de.bklaiber.examples;

import java.util.LinkedList;

import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

public class Example
{

	private LinkedList<RelationalConditional> knowledgeBase ;
	
	
	
	
	public Example(LinkedList<RelationalConditional> knowledgeBase)
	{
		super();
		this.knowledgeBase = knowledgeBase;
	}





		// TODO Auto-generated method stub
		public LinkedList<RelationalConditional> getKnowledgeBase()
	{
		return knowledgeBase;
	}
	public void setKnowledgeBase(LinkedList<RelationalConditional> knowledgeBase)
	{
		this.knowledgeBase = knowledgeBase;
	}





		String spacebetween = new String();

		/*
		
		GroundingOperator gop = new ConstraintBasedGroundingOperator();
		GroundingSemantics semantics = new GroundingSemantics(gop, constants);
		RelationalOptimumEntropyEpistemicStateLBFGS epState = new RelationalOptimumEntropyEpistemicStateLBFGS(
				semantics);

		PossibleWorldFactory<RelationalAtom> worldFactory = new RelationalPossibleWorldMapRepresentationFactory();

		Interpretation<RelationalAtom>[] possibleWorlds = worldFactory
				.createPossibleWorlds(RelationalUtils
						.getAtomsFromKnowledgeBase(knowledgeBase, constants,
								gop));

		epState.initialize(possibleWorlds, knowledgeBase);

		// epState.printCurrentProbabilities();

		Collection<RelationalAtom> atoms = RelationalUtils
				.getAtomsFromKnowledgeBase(knowledgeBase, constants, gop);
		System.out.println("Grundatome:");
		for (RelationalAtom relationalAtom : atoms)
		{
			System.out.println(relationalAtom.toString());

		}

		System.out.println(spacebetween);

		Collection<RelationalConditional> groundKnowledgeBase = gop
				.groundKnowledgeBase(knowledgeBase, constants);
		System.out.println("Grundkonditionale:");
		for (RelationalConditional relationalConditional : groundKnowledgeBase)
		{
			System.out.println(relationalConditional.toString());

		}

		System.out.println(spacebetween);

		System.out.println("Grundkonditionale mit Wahrscheinlichkeiten");
		for (RelationalConditional relationalConditional : groundKnowledgeBase)
		{
			Formula<RelationalAtom> formulaCons = relationalConditional
					.getConsequence();
			Formula<RelationalAtom> formAnt = relationalConditional
					.getAntecedence();
			System.out
					.println("P("
							+ formulaCons
							+ " "
							+ "|"
							+ " "
							+ formAnt
							+ " ) = "
							+ epState.queryConditionalProbability(formulaCons,
									formAnt));
		}

		System.out.println(spacebetween);

		System.out
				.println("Grundatome der Konsequenz mit Wahrscheinlichkeiten:");
		for (RelationalConditional relationalConditional : groundKnowledgeBase)
		{
			Formula<RelationalAtom> formulaCons = relationalConditional
					.getConsequence();
			System.out.println("P(" + formulaCons + " ) = "
					+ epState.queryProbability(formulaCons));
		}

		System.out.println(spacebetween);

		System.out.println("Grundatome der Prämisse mit Wahrscheinlichkeiten:");
		for (RelationalConditional relationalConditional : groundKnowledgeBase)
		{
			Formula<RelationalAtom> formulaAnt = relationalConditional
					.getAntecedence();
			System.out.println("P(" + formulaAnt + " ) = "
					+ epState.queryProbability(formulaAnt));
		}
		}

*/	

}
