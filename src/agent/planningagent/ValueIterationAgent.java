package agent.planningagent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import util.HashMapUtil;

import java.util.HashMap;

import environnement.Action;
import environnement.Etat;
import environnement.IllegalActionException;
import environnement.MDP;
import environnement.Action2D;


/**
 * Cet agent met a jour sa fonction de valeur avec value iteration
 * et choisit ses actions selon la politique calculee.
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent{
	/**
	 * discount facteur
	 */
	protected double gamma;

	/**
	 * fonction de valeur des etats
	 */
	protected HashMap<Etat,Double> V;

	/**
	 *
	 * @param gamma
	 * @param mdp
	 */
	public ValueIterationAgent(double gamma,  MDP mdp) {
		super(mdp);
		this.gamma = gamma;
		//*** VOTRE CODE
		V = new HashMap<>();
		for(Etat e : mdp.getEtatsAccessibles()){
			V.put(e, 0.d);
		}
	}




	public ValueIterationAgent(MDP mdp) {
		this(0.9,mdp);
	}

	/**
	 *
	 * Mise a jour de V: effectue UNE iteration de value iteration (calcule V_k(s) en fonction de V_{k-1}(s'))
	 * et notifie ses observateurs.
	 * Ce n'est pas la version inplace (qui utilise nouvelle valeur de V pour mettre a jour ...)
	 */
	@Override
	public void updateV(){
		//delta est utilise pour detecter la convergence de l'algorithme
		//lorsque l'on planifie jusqu'a convergence, on arrete les iterations lorsque
		//delta < epsilon
		this.delta=0.0;
		//*** VOTRE CODE
		double vmin = Double.MAX_VALUE;
		double vmax = Double.MIN_VALUE;
		HashMap<Etat,Double> Vtemp = new HashMap<Etat, Double>();
		
		for(Etat e: this.mdp.getEtatsAccessibles()){
			double maxScore = 0.d;
			for(Action a : mdp.getActionsPossibles(e)){
				double score = 0.d;
				try{
					for( Entry<Etat, Double> set : mdp.getEtatTransitionProba(e, a).entrySet()){
						score += (set.getValue() * (mdp.getRecompense(e, a, set.getKey()) + gamma*this.getValeur(set.getKey())));
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
				if(score > maxScore)
					maxScore = score;
			}
			Vtemp.put(e, maxScore);
			if(maxScore > vmax)
				vmax = maxScore;
			if(maxScore < vmin)
				vmin = maxScore;
		}
		V = Vtemp;
		System.out.println(vmax);
		System.out.println(vmin);
		// mise a jour vmax et vmin pour affichage du gradient de couleur:
		//vmax est la valeur max de V pour tout s
		//vmin est la valeur min de V pour tout s 
		// ...
		//******************* laisser notification a la fin de la methode
		this.notifyObs();
	}


	/**
	 * renvoi l'action executee par l'agent dans l'etat e
	 * Si aucune actions possibles, renvoi Action2D.NONE
	 */
	@Override
	public Action getAction(Etat e) {
		List<Action> actions = this.getPolitique(e);
		if(actions.isEmpty())
			return Action2D.NONE;
		else{
			Integer randIndex = (int) Math.random() * (actions.size()-1);
			return actions.get(randIndex);
		}
	}
	
	@Override
	public double getValeur(Etat _e) {
		return this.V.get(_e);
	}
	/**
	 * renvoi action(s) de plus forte(s) valeur(s) dans etat
	 * (plusieurs actions sont renvoyees si valeurs identiques, liste vide si aucune action n'est possible)
	 */
	@Override
	public List<Action> getPolitique(Etat _e) {
		
		List<Action> actions = mdp.getActionsPossibles(_e);
		HashMap<Action, Double> actionsScore = new HashMap<>();
		
		try{
			for(Action a: actions){
				Map<Etat, Double> probas = mdp.getEtatTransitionProba(_e, a);
				double score = 0;
				for(Map.Entry<Etat, Double> pair: probas.entrySet()){
					double recompense = mdp.getRecompense(_e, a, pair.getKey());
					score += (pair.getValue()*(recompense + gamma*this.getValeur(pair.getKey())));
				}
				actionsScore.put(a, score);
			}
		}
		catch(Exception e){
		}

		ArrayList<Action> returner = new ArrayList<>();
		double max = 0;
		for(Map.Entry<Action, Double> pair: actionsScore.entrySet()){
			if(max < pair.getValue()){
				max = pair.getValue();
				returner.clear();
				returner.add(pair.getKey());
			}
			else if(max == pair.getValue()){
				returner.add(pair.getKey());
			}
		}
		
		return returner;
	}

	@Override
	public void reset() {
		super.reset();


		this.V.clear();
		for (Etat etat:this.mdp.getEtatsAccessibles()){
			V.put(etat, 0.0);
		}
		this.notifyObs();
	}





	public HashMap<Etat,Double> getV() {
		return V;
	}
	public double getGamma() {
		return gamma;
	}
	@Override
	public void setGamma(double _g){
		System.out.println("gamma= "+gamma);
		this.gamma = _g;
	}






}
