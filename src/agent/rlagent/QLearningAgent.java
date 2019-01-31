package agent.rlagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.sun.xml.internal.ws.addressing.model.ActionNotSupportedException;

import javafx.util.Pair;
import environnement.Action;
import environnement.Action2D;
import environnement.Environnement;
import environnement.Etat;
/**
 * Renvoi 0 pour valeurs initiales de Q
 * @author laetitiamatignon
 *
 */
public class QLearningAgent extends RLAgent {
	/**
	 *  format de memorisation des Q valeurs: utiliser partout setQValeur car cette methode notifie la vue
	 */
	protected HashMap<Etat,HashMap<Action,Double>> qvaleurs;

	//AU CHOIX: vous pouvez utiliser une Map avec des Pair pour clés si vous préférez
	//protected HashMap<Pair<Etat,Action>,Double> qvaleurs;


	/**
	 *
	 * @param alpha
	 * @param gamma
	 * @param Environnement

	 */
	public QLearningAgent(double alpha, double gamma,
			Environnement _env) {
		super(alpha, gamma,_env);
		qvaleurs = new HashMap<Etat,HashMap<Action,Double>>();
		


	}




	/**
	 * renvoi action(s) de plus forte(s) valeur(s) dans l'etat e
	 *  (plusieurs actions sont renvoyees si valeurs identiques)
	 *  renvoi liste vide si aucunes actions possibles dans l'etat (par ex. etat absorbant)

	 */
	@Override
	public List<Action> getPolitique(Etat e) {
		// retourne action de meilleures valeurs dans _e selon Q : utiliser getQValeur()
		// retourne liste vide si aucune action legale (etat terminal)
		List<Action> returnactions = new ArrayList<Action>();
		if (this.getActionsLegales(e).size() == 0){//etat  absorbant; impossible de le verifier via environnement
			System.out.println("aucune action legale");
			return new ArrayList<Action>();
		}

		//*** VOTRE CODE
		double max = Double.NEGATIVE_INFINITY;
		double score = 0.d;
		for(Action a : getActionsLegales(e)){
			score = getQValeur(e, a);
			if(score > max){
				max = score;
				returnactions.clear();
				returnactions.add(a);
			}
			else if(score == max){
				returnactions.add(a);
			}
		}
		if(returnactions.isEmpty())
			System.out.println("lal");
		return returnactions;
	}

	@Override
	public double getValeur(Etat e) {
		
		double returner = 0.d;
		if(qvaleurs.get(e) != null){
			for( Entry<Action, Double> set : qvaleurs.get(e).entrySet() ){
				if(returner < set.getValue())
					returner = set.getValue();
			}
		}
		return returner;

	}	

	@Override
	public double getQValeur(Etat e, Action a) {
		if(qvaleurs.get(e) == null)
			qvaleurs.put(e, new HashMap<>());
		if(qvaleurs.get(e).get(a) == null)
			qvaleurs.get(e).put(a,0.d);
		return this.qvaleurs.get(e).get(a);
	}



	@Override
	public void setQValeur(Etat e, Action a, double d) {
		//*** VOTRE CODE
		this.qvaleurs.get(e).put(a, d);
		if(d > vmax)
			vmax = d;
		if(d < vmin)	
			vmin = d;


		this.notifyObs();

	}


	/**
	 * mise a jour du couple etat-valeur (e,a) apres chaque interaction <etat e,action a, etatsuivant esuivant, recompense reward>
	 * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement apres avoir realise une action.
	 * @param e
	 * @param a
	 * @param esuivant
	 * @param reward
	 */
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
		if (RLAgent.DISPRL)
			System.out.println("QL mise a jour etat "+e+" action "+a+" etat' "+esuivant+ " r "+reward);
		
		if(qvaleurs.get(e) == null)
			qvaleurs.put(e, new HashMap<>());
		if(qvaleurs.get(e).get(a) == null)
			qvaleurs.get(e).put(a,0.d);
		
		if(qvaleurs.get(esuivant) == null)
			qvaleurs.put(esuivant, new HashMap<>());
		if(qvaleurs.get(esuivant).get(a) == null)
			qvaleurs.get(esuivant).put(a,0.d);
		
		double score = ((1 - this.getAlpha())*(qvaleurs.get(e).get(a))) + this.getAlpha() * (reward + (this.getGamma()*this.getValeur(esuivant)));		
		if(score > vmax)
			vmax = score;
		
		setQValeur(e, a, score);
		
	}

	@Override
	public Action getAction(Etat e) {
		this.actionChoisie = this.stratExplorationCourante.getAction(e);
		return this.actionChoisie;
	}

	@Override
	public void reset() {
		super.reset();
		qvaleurs = new HashMap<>(new HashMap<>());

		this.episodeNb =0;
		this.notifyObs();
	}












}
