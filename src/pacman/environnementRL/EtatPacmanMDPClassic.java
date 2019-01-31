	package pacman.environnementRL;

import java.util.ArrayList;
import java.util.Arrays;

import com.sun.javafx.geom.transform.GeneralTransform3D;

import pacman.elements.MazePacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import environnement.Etat;
import javafx.util.Pair;
/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire

 */
public class EtatPacmanMDPClassic implements Etat , Cloneable{

	private ArrayList<Pair<Integer, Integer>> ghosts = new ArrayList<>();
	private ArrayList<Pair<Integer, Integer>> pacmans = new ArrayList<>();
	private ArrayList<Pair<Integer, Integer>> foods = new ArrayList<>();
	/*private int nearest_Dot;
	private int nearest_Ghost;
	private int direction;
	private int danger;*/
	
	
	
	private static Double nbEtat = 0.d;
	
	public EtatPacmanMDPClassic(StateGamePacman _stategamepacman){		
		/*
		int posX = _stategamepacman.getPacmanState(0).getX();
		int posY = _stategamepacman.getPacmanState(0).getY();
		MazePacman maze = _stategamepacman.getMaze();
		nearest_Dot = 9999;
		for(int i = 0; i < maze.getSizeX(); i++){
			for(int j = 0; j < maze.getSizeY(); j++){
				if(maze.isFood(i, j)){
					int dist = Math.abs((i - posX) + (j - posY));
					if(dist < nearest_Dot){
						nearest_Dot = dist;						
						if (Math.abs(i - posX) > Math.abs(j - posY)){
							if(i - posX > 0){
								direction = 1;										
							}else{			
								direction = 2;					
							}
						}else{
							if(j - posY > 0){
								direction = 3;										
							}else{			
								direction = 4;					
							}
							
						}
					}
				}
			}
		}

		nearest_Ghost = 9999;
		danger = 0;
		for(int i = 0; i < maze.getNumberOfGhosts(); i++){
			StateAgentPacman ghost = _stategamepacman.getGhostState(i);
			int dist = Math.abs((ghost.getX() - posX) + (ghost.getY() - posY));
			if(dist == 1){
				danger = 1;
			}
			if(dist < nearest_Ghost){
				nearest_Ghost = dist;
			}			
		}
		*/
		
		for(int i = 0; i < _stategamepacman.getNumberOfGhosts(); i++){
			this.ghosts.add(new Pair<>(_stategamepacman.getGhostState(i).getX(), _stategamepacman.getGhostState(i).getY()));
		}

		for(int i = 0; i < _stategamepacman.getNumberOfPacmans(); i++){
			this.pacmans.add(new Pair<>(_stategamepacman.getPacmanState(i).getX(), _stategamepacman.getPacmanState(i).getY()));
		}
	

		int nbOfFood = 0;
		for(int i = 0; i < _stategamepacman.getMaze().getSizeX(); i++){
			for(int j = 0; j < _stategamepacman.getMaze().getSizeY(); j++){
				if(_stategamepacman.getMaze().isFood(i, j)){
					foods.add(new Pair<>(i,j));
					nbOfFood++;
				}
			}
		}

		int nbCasesValides = 0;
		for( int x = 0; x < _stategamepacman.getMaze().getSizeX(); x++){
			for( int y = 0; y < _stategamepacman.getMaze().getSizeY(); y++){
				if(!_stategamepacman.getMaze().isWall(x, y))
					nbCasesValides++;
			}
		}
		nbEtat = 1.d;
		for(int i = 0; i < _stategamepacman.getNumberOfPacmans(); i ++){
			nbEtat *= nbCasesValides;
			nbCasesValides--;
		}
		for(int i = 0; i < _stategamepacman.getNumberOfPacmans(); i ++){
			nbEtat *= nbCasesValides;
			nbCasesValides--;
		}
		nbEtat *= Math.pow(2, nbOfFood);	
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((foods == null) ? 0 : foods.hashCode());
		result = prime * result + ((ghosts == null) ? 0 : ghosts.hashCode());
		result = prime * result + ((pacmans == null) ? 0 : pacmans.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EtatPacmanMDPClassic other = (EtatPacmanMDPClassic) obj;
		if (foods == null) {
			if (other.foods != null)
				return false;
		} else if (!foods.equals(other.foods))
			return false;
		if (ghosts == null) {
			if (other.ghosts != null)
				return false;
		} else if (!ghosts.equals(other.ghosts))
			return false;
		if (pacmans == null) {
			if (other.pacmans != null)
				return false;
		} else if (!pacmans.equals(other.pacmans))
			return false;
		return true;
	}
	
	public Object clone() {
		EtatPacmanMDPClassic clone = null;
		try {
			// On recupere l'instance a renvoyer par l'appel de la 
			// methode super.clone()
			clone = (EtatPacmanMDPClassic)super.clone();
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implementons 
			// l'interface Cloneable
			cnse.printStackTrace(System.err);
		}
		


		// on renvoie le clone
		return clone;
	}
	
	public int getDimensions(){
		return nbEtat.intValue();
	}



	

}
