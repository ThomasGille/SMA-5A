package agent.rlapproxagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import environnement.Action;
import environnement.Action2D;
import environnement.Etat;
import javafx.util.Pair;
/**
 * Vecteur de fonctions caracteristiques phi_i(s,a): autant de fonctions caracteristiques que de paire (s,a),
 * <li> pour chaque paire (s,a), un seul phi_i qui vaut 1  (vecteur avec un seul 1 et des 0 sinon).
 * <li> pas de biais ici 
 * 
 * @author laetitiamatignon
 *
 */
public class FeatureFunctionIdentity implements FeatureFunction {
	//*** VOTRE CODE
	private int tailleVecteur;
	private HashMap<Etat, HashMap<Action, double[]>> vfeatures;
	
	public FeatureFunctionIdentity(int _nbEtat, int _nbAction){
		tailleVecteur = _nbAction * _nbEtat;
		vfeatures = new HashMap<>();
		/*Double positionDuUn = (Math.random()*(tailleVecteur-1));
		Double positionDuUn = 1.d;
		for(int i = 0; i < tailleVecteur; i++){
			if(i == positionDuUn.intValue())
				vfeatures[i] = 1.d;
			else
				vfeatures[i] = 0.d;
		}*/	
	}
	
	@Override
	public int getFeatureNb() {
		return tailleVecteur;
	}

	@Override
	public double[] getFeatures(Etat e,Action a){
		if(vfeatures.get(e) == null)
			vfeatures.put(e, new HashMap<>());
		if(vfeatures.get(e).get(a) == null){
			vfeatures.get(e).put(a, new double[tailleVecteur]);
			Double positionDuUn = (Math.random()*(tailleVecteur-1));
			for(int i = 0; i < tailleVecteur; i++){
				if(i == positionDuUn.intValue())
					vfeatures.get(e).get(a)[i] = 1.d;
				else
					vfeatures.get(e).get(a)[i] = 0.d;
			}
		}
		return vfeatures.get(e).get(a);
	}
}
