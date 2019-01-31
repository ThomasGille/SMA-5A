package agent.rlapproxagent;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import agent.rlagent.QLearningAgent;
import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
/**
 * Agent qui apprend avec QLearning en utilisant approximation de la Q-valeur : 
 * approximation lineaire de fonctions caracteristiques 
 * 
 * @author laetitiamatignon
 *
 */
public class QLApproxAgent extends QLearningAgent{
	
	//CETTE LIGNE NE DEVRAIT PAS ETRE LA... i guess
	private FeatureFunction _featFunc;
	private double[] poids;

	
	public QLApproxAgent(double alpha, double gamma, Environnement _env,FeatureFunction _featurefunction) {
		super(alpha, gamma, _env);
		this._featFunc = _featurefunction;
		poids = new double[_featurefunction.getFeatureNb()];
		for(int i = 0; i < _featurefunction.getFeatureNb(); i++)
			poids[i] = 0.d;
	}

	
	@Override
	public double getQValeur(Etat e, Action a) {
		double tableau[] = _featFunc.getFeatures(e, a);
		double Q = 0.d;
		for(int i = 0; i < _featFunc.getFeatureNb(); i++){
			Q += tableau[i] * poids[i];
			//System.out.println("tableau : " + tableau[i]);
			//System.out.println("poids : " + poids[i]);
		}
		return Q;
	}
	
	
	
	
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
		if (RLAgent.DISPRL){
			System.out.println("QL: mise a jour poids pour etat \n"+e+" action "+a+" etat' \n"+esuivant+ " r "+reward);
		}
       //inutile de verifier si e etat absorbant car dans runEpisode et threadepisode 
		//arrete episode lq etat courant absorbant	
		double [] features = _featFunc.getFeatures(e, a);
		double currentQValeur = getQValeur(e, a);
		double currentValeur = getValeur(esuivant);
		double alpha = getAlpha();
		double gamma = getGamma();
		
		int nbFeatures = _featFunc.getFeatureNb();
		double newpoids;
		for(int i = 0; i < _featFunc.getFeatureNb(); i++){
			newpoids = poids[i] + alpha*(reward + gamma * currentValeur - currentQValeur) * features[i];
			poids[i] = newpoids;
		}
		
	}
	
	@Override
	public void reset() {
		super.reset();
		this.qvaleurs.clear();
		poids = new double[_featFunc.getFeatureNb()];
		for(int i = 0; i < _featFunc.getFeatureNb(); i++)
			poids[i] = 0.d;
		//this.qVecteurValeurs.clear();
		//*** VOTRE CODE
		
		this.episodeNb =0;
		this.notifyObs();
	}
	
	
}
