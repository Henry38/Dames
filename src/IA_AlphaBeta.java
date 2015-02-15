import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;


public class IA_AlphaBeta extends Thread {
	
	private Plateau p;
	private int level;
	private LinkedList<Coup> coups;
	
	/** Constructeur */
	public IA_AlphaBeta(Plateau p, int level) {
		this.p = p.clone();
		p.setJoueur(-1);
		this.level = level;
	}
	
	/** Récupère le coup calculé par l'IA */
	public LinkedList<Coup> getCoup() {
		return coups;
	}
	
	public void jouerAlphaBeta() {
		int value, value_min = Integer.MAX_VALUE;
		ArrayList<LinkedList<Coup>> allc = p.getCoupFromJoueur();
		Collections.shuffle(allc);
		
		for (LinkedList<Coup> llc : allc) {
			p.jouerListCoup(llc);
			value = ab_max_min(p, Integer.MIN_VALUE, Integer.MAX_VALUE, level);
			
			if (value_min > value) {
				value_min = value;
				coups = llc;
			}
			p.getBack(llc);
		}
	}
	
	/** Algorithme MinMax */
	public int ab_min_max(Plateau p_courant, int alpha, int beta, int depth) {
		
		if (p_courant.gagner() || depth == 0) {
			return p_courant.evaluation();
		}
		
		// p_child servira comme support pour tester tout les coups possibles
		Plateau p_child = p_courant.clone();
		p_child.setJoueur(-1);
		// Création de la liste des coups possibles
		ArrayList<LinkedList<Coup>> listCoup = p_child.getCoupFromJoueur();
		int value;
		
		// Boucle de parcourt de tout les enfants
		for (LinkedList<Coup> llc : listCoup) {
			p_child.jouerListCoup(llc);
			value = ab_max_min(p_child, alpha, beta, depth-1);
			if (value < beta) {
				beta = value;
				if (alpha > beta) {
					return beta;
				}
			}
			p_child.getBack(llc);
		}
		
		return beta;
	}
	
	/** Algorithme MaxMin */
	public int ab_max_min(Plateau p_courant, int alpha, int beta, int depth) {
		
		if (p_courant.gagner() || depth == 0) {
			return p_courant.evaluation();
		}
		
		// p_child servira comme support pour tester tout les coups possibles
		Plateau p_child = p_courant.clone();
		p_child.setJoueur(1);
		// Création de la liste des coups possibles
		ArrayList<LinkedList<Coup>> listCoup = p_child.getCoupFromJoueur();
		int value;
		
		// Boucle de parcourt de tout les enfants
		for (LinkedList<Coup> llc : listCoup) {
			p_child.jouerListCoup(llc);
			value = ab_min_max(p_child, alpha, beta, depth-1);
			if (value > alpha) {
				alpha = value;
				if (alpha > beta) {
					return alpha;
				}
			}
			p_child.getBack(llc);
		}
		
		return alpha;
	}
	
	/** Lance le calcul du meilleur coup pour sur plateau */
	public void run() {
		coups = null;
		jouerAlphaBeta();
	}
}
