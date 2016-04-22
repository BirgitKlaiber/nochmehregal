package de.bklaiber.PrintProbabilities;

import java.util.*;

import de.bklaiber.Types.ProbabilityConditional;
import edu.cs.ai.log4KR.logical.syntax.Atom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.RelationalAtom;
import edu.cs.ai.log4KR.relational.classicalLogic.syntax.signature.Variable;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

/**
 * 
 * Used to generate a relational conditonal with a single exception.
 * 
 * @author Birgit Klaiber
 *
 */
public class PrintRelationalWithConstraint implements
		PrintConditionalProbabilities {
	private RelationalConditional relationalConditional;
	private ProbabilityConditional probalilityConditional;
	private Collection<Atom<RelationalAtom>> relationalAtoms;
	private Collection<Atom<RelationalAtom>> atomsOfAntecendence;
	private Collection<Atom<RelationalAtom>> atomsOfConsequence;
	private Set<Variable> variables;
	private Set<Variable> variablesCons;
	private boolean equal = true;
	private boolean possible = true;
	private boolean isNull = false;
	private boolean ant = false;
	private boolean cons = false;

	/**
	 * Sets ant.
	 * 
	 * @param ant
	 */
	public void setAnt(boolean ant) {
		this.ant = ant;
	}

	/**
	 * 
	 * Sets cons.
	 * 
	 * @param cons
	 */
	public void setCons(boolean cons) {
		this.cons = cons;
	}

	/*
	 * public void setAtomsOfAntecendence( Collection<Atom<RelationalAtom>>
	 * atomsOfAntecendence) { this.atomsOfAntecendence = atomsOfAntecendence; }
	 * 
	 * public void setAtomsOfConsequence( Collection<Atom<RelationalAtom>>
	 * atomsOfConsequence) { this.atomsOfConsequence = atomsOfConsequence; }
	 */

	/**
	 * 
	 * Sets isNull.
	 * 
	 * @param isNull
	 */
	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}

	/**
	 * 
	 * Constructor of class PrintRelationalWithConstraint.
	 * 
	 * @param probalilityConditional
	 */
	public PrintRelationalWithConstraint(
			ProbabilityConditional probalilityConditional) {
		super();
		this.probalilityConditional = probalilityConditional;
		this.relationalConditional = probalilityConditional
				.getRelationalConditional();
		this.relationalAtoms = relationalConditional.getAtoms();
		variables = new HashSet<Variable>();
		variablesCons = new HashSet<Variable>();
	}// endof PrintrelationalwithConstraint

	@Override
	public String toString() {
		StringBuilder probCond = new StringBuilder();

		// if the probability is Null, look at the atoms of the consequence,
		// because the probability of the consequence is Null
		//get all the variables; later used for intersection

		if (isNull) {
			atomsOfConsequence = relationalConditional.getConsequence()
					.getAtoms();
			for (Atom<RelationalAtom> atom : atomsOfConsequence) {
				Collection<Variable> variablesOfConsequence = ((RelationalAtom) atom)
						.getVariables();
				for (Variable variable : variablesOfConsequence) {
					// System.out.println(variable.toString());
					variables.add(variable);
				}// endfor

			}// endfor

		} else {
			// if the probability is -1, then have a look at the atoms of the
			// antecedence, because the probability of the antecedence is Null
			if (!possible || ant) {

				atomsOfAntecendence = relationalConditional.getAntecedence()
						.getAtoms();
				for (Atom<RelationalAtom> atom : atomsOfAntecendence) {
					Collection<Variable> variablesOfAntecendence = ((RelationalAtom) atom)
							.getVariables();
					for (Variable variable : variablesOfAntecendence) {
						// System.out.println(variable.toString());
						variables.add(variable);
					}// endfor

				}// endfor

			}// endif
				// else
				// {
			
			//get all the variables; later used for intersection
			if (cons) {
				atomsOfConsequence = relationalConditional.getConsequence()
						.getAtoms();
				for (Atom<RelationalAtom> atom : atomsOfConsequence) {
					Collection<Variable> variablesOfConsequence = ((RelationalAtom) atom)
							.getVariables();
					for (Variable variable : variablesOfConsequence) {
						// System.out.println(variable.toString());
						variablesCons.add(variable);
					}// endfor

				}// endfor

			}
			// else
			// {
			//get the variables; later used for generating the exceptions
			if (possible && !ant && !cons) {
				for (Atom<RelationalAtom> atom : relationalAtoms) {
					Collection<Variable> variablesOfAtom = ((RelationalAtom) atom)
							.getVariables();
					// System.out.println("Anzahl Variablen: " +
					// variablesOfAtom.size());
					// System.out.println(variablesOfAtom.toString());
					for (Variable variable : variablesOfAtom) {
						// System.out.println(variable.toString());
						variables.add(variable);
					}// endfor

				}// endfor
			}
			// }//endelse
			// }
		}// endelse

		probCond.append(probalilityConditional.relationalToString(true));
		if (possible) {
			// probCond.append(probalilityConditional.relationalToString(true));
			probCond.append("["
					+ Math.rint(probalilityConditional.getProbability() * 1000)
					/ 1000 + "]");
		} else {
			probCond.append(" ist keine zulaessige Anfrage fuer ");
		}
		// for (Variable variable : variables)
		ArrayList<Variable> variablesList = new ArrayList<>(variables);

		/*
		 * System.out.println("Variablen"); for (Variable variable : variables)
		 * {
		 * 
		 * System.out.print(variable.toString()); System.out.println("\n"); }
		 */

		//here the conditonals with constraints were generated
		if (variablesCons != null) {
			// ArrayList<Variable> variablesListCons = new
			// ArrayList<>(variablesCons);
			for (int i = 0; i < variablesCons.size(); i++) {
				ArrayList<Variable> variablesListCons = new ArrayList<>(
						variablesCons);
				// System.out.println("i: " + variablesListCons.get(i));

				// for (Variable otherVariable : variables)
				for (int j = 1; j < variablesListCons.size(); j++)

				{
					// System.out.println("j: " + variablesListCons.get(j));

					// if(!(variablesList.get(i).toString().equals((variablesList).get(j).toString())));
					if (variablesListCons.get(i).toString() != variablesListCons
							.get(j).toString()) {
						probCond.append("<" + variablesListCons.get(i));
						if (equal) {
							probCond.append("=" + variablesListCons.get(j));
							probCond.append("> + ");
						} else {
							probCond.append("!=" + variablesListCons.get(j));
							probCond.append("> * ");
						}//endelse

					}//endif
				}//endfor
			}//endfor

		}//endif

		for (int i = 0; i < variables.size(); i++) {

			// System.out.println("i: " + variablesList.get(i));

			// for (Variable otherVariable : variables)
			for (int j = 1; j < variablesList.size(); j++)

			{
				// System.out.println("j: " + variablesList.get(j));

				// if(!(variablesList.get(i).toString().equals((variablesList).get(j).toString())));
				if (variablesList.size() == 2 || j < variablesList.size() - 1) {
					if (variablesList.get(i).toString() != variablesList.get(j)
							.toString()) {
						probCond.append("<" + variablesList.get(i));
						if (equal) {
							probCond.append("=" + variablesList.get(j));
							probCond.append("> + ");
						} else {
							probCond.append("!=" + variablesList.get(j));
							probCond.append("> * ");
						}//endelse

					}//endif

				}//endif
			}//endfor
		}//endfor
		// ArrayList<Variable> variablesListCons;
		// = new ArrayList<>(variablesCons);

		probCond.deleteCharAt(probCond.length() - 1);
		probCond.deleteCharAt(probCond.length() - 1);
		probCond.deleteCharAt(probCond.length() - 1);

		return probCond.toString();
	}//endof toString

	/**
	 * 
	 * Sets possible.
	 * 
	 * @param possible
	 */
	public void setPossible(boolean possible) {
		this.possible = possible;
	}

	/**
	 * 
	 * Sets equal.
	 * 
	 * @param equal
	 */
	public void setEqual(boolean equal) {
		this.equal = equal;
	}
}// endofPrintRelationalWithConstraint
