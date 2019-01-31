package agent.rlapproxagent;

import pacman.elements.ActionPacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import pacman.environnementRL.EnvironnementPacmanMDPClassic;
import environnement.Action;
import environnement.Etat;
/**
 * Vecteur de fonctions caracteristiques pour jeu de pacman: 4 fonctions phi_i(s,a)
 *  
 * @author laetitiamatignon
 *
 */
public class FeatureFunctionPacman implements FeatureFunction{
	private double[] vfeatures;
	private static double diagonale = 0.d;
	
		private static int NBACTIONS = 5;//5 avec NONE possible pour pacman, 4 sinon 
	//--> doit etre coherent avec EnvironnementPacmanRL::getActionsPossibles


	public FeatureFunctionPacman() {
	}

	@Override
	public int getFeatureNb() {
		return NBACTIONS;
	}

	@Override
	public double[] getFeatures(Etat e, Action a) {
		vfeatures = new double[NBACTIONS];
		StateGamePacman stategamepacman ;
		//EnvironnementPacmanMDPClassic envipacmanmdp = (EnvironnementPacmanMDPClassic) e;

		//calcule pacman resulting position a partir de Etat e
		if (e instanceof StateGamePacman){
			stategamepacman = (StateGamePacman)e;
		}
		else{
			System.out.println("erreur dans FeatureFunctionPacman::getFeatures n'est pas un StateGamePacman");
			return vfeatures;
		}
	
		StateAgentPacman pacmanstate_next= stategamepacman.movePacmanSimu(0, new ActionPacman(a.ordinal()));
		 
		vfeatures[0] = firstFunction(stategamepacman, pacmanstate_next, a);
		vfeatures[1] = secondFunction(stategamepacman, pacmanstate_next, a);
		vfeatures[2] = thirdFunction(stategamepacman, pacmanstate_next, a);
		vfeatures[3] = forthFunction(stategamepacman, pacmanstate_next, a);
		vfeatures[4] = fifthFunction(stategamepacman, pacmanstate_next, a);
		return vfeatures;
	}

	
	private double firstFunction(StateGamePacman e,StateAgentPacman p, Action a) {
		return 1.d;
	}
	
	private double secondFunction(StateGamePacman e, StateAgentPacman p,  Action a) {
		int numberOfGhostNear = 0;
		for(int i = 0; i < e.getNumberOfGhosts(); i++){
			StateAgentPacman ghost = e.getGhostState(i);
			int deltaX = p.getX() - ghost.getX();
			int deltaY = p.getY() - ghost.getY();
			
			if((deltaX >= -1 && deltaX <= 1 && deltaY == 0) || (deltaY >= -1 && deltaY <= 1 && deltaX == 0) || (deltaX == 0 && deltaY == 0))
				numberOfGhostNear++;
		}
		return numberOfGhostNear;
	}
	
	private double thirdFunction(StateGamePacman e, StateAgentPacman p, Action a) {
		return (e.getClosestDot(p) == 0 ? 1 : 0);
	}
	
	private double forthFunction(StateGamePacman e, StateAgentPacman p,  Action a) {
		if(diagonale == 0.d){
			diagonale = Math.sqrt((e.getMaze().getSizeX()*(e.getMaze().getSizeX() + e.getMaze().getSizeY()*e.getMaze().getSizeY())));
		}
		return e.getClosestDot(p) / diagonale;
	}

	private double fifthFunction(StateGamePacman e, StateAgentPacman p,  Action a) {
		double result = 10000.d;
		for(int i = 0; i < e.getNumberOfGhosts(); i++){
			StateAgentPacman ghost = e.getGhostState(i);
			int deltaX = p.getX() - ghost.getX();
			int deltaY = p.getY() - ghost.getY();
			if(deltaX + deltaY < result){
				result = Math.sqrt((deltaX*deltaX) + (deltaY*deltaY));
			}
		}

		if(diagonale == 0.d){
			diagonale = Math.sqrt((e.getMaze().getSizeX()*(e.getMaze().getSizeX() + e.getMaze().getSizeY()*e.getMaze().getSizeY())));
		}
		
		return result / diagonale;
	}
	

}
