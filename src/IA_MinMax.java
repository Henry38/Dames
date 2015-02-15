import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;


public class IA_MinMax extends Thread {
	
	private Plateau p;
	private int level;
	private LinkedList<Coup> coups;
	
	/** Constructeur */
	public IA_MinMax(Plateau p, int level) {
		this.p = p.clone();
		p.setJoueur(-1);
		this.level = level;
	}
	
	/** Récupère le coup calculé par l'IA */
	public LinkedList<Coup> getCoup() {
		return coups;
	}
	
	public void jouerMinMax() {
		int value, value_min = Integer.MAX_VALUE;
		ArrayList<LinkedList<Coup>> allc = p.getCoupFromJoueur();
		Collections.shuffle(allc);
		
		//String s = "";
		//for (int i=level-depth; i>0; i--) { s += "\t"; }
		//s = "(" + llc.getFirst().pLine + "," + llc.getFirst().pColumn + ")";
		//for (Coup c : llc) {
		//	s += "(" + c.tLine + "," + c.tColumn + ")";
		//}
		//System.out.println("jouerMinMax : " + s);
		
		for (LinkedList<Coup> llc : allc) {
			p.jouerListCoup(llc);
			value = max_min(p, level);
			
			if (value_min > value) {
				value_min = value;
				coups = llc;
			}
			p.getBack(llc);
		}
	}
	
	/** Algorithme MinMax */
	public int min_max(Plateau p_courant, int depth) {
		
		if (p_courant.gagner() || depth == 0) {
			return p_courant.evaluation();
		}
		
		// p_child servira comme support pour tester tout les coups possibles
		Plateau p_child = p_courant.clone();
		p_child.setJoueur(-1);
		// Création de la liste des coups possibles
		ArrayList<LinkedList<Coup>> listCoup = p_child.getCoupFromJoueur();
		int value, value_min = Integer.MAX_VALUE;
		
		// Boucle de parcourt de tout les enfants
		for (LinkedList<Coup> llc : listCoup) {
			p_child.jouerListCoup(llc);
			value = max_min(p_child, depth-1);
			if (value_min > value) {
				value_min = value;
			}
			p_child.getBack(llc);
		}
		
		return value_min;
	}
	
	/** Algorithme MaxMin */
	public int max_min(Plateau p_courant, int depth) {
		
		if (p_courant.gagner() || depth == 0) {
			return p_courant.evaluation();
		}
		
		// p_child servira comme support pour tester tout les coups possibles
		Plateau p_child = p_courant.clone();
		p_child.setJoueur(1);
		// Création de la liste des coups possibles
		ArrayList<LinkedList<Coup>> listCoup = p_child.getCoupFromJoueur();
		int value, value_max = Integer.MIN_VALUE;
		
		// Boucle de parcourt de tout les enfants
		for (LinkedList<Coup> llc : listCoup) {
			p_child.jouerListCoup(llc);
			value = min_max(p_child, depth-1);
			if (value_max < value) {
				value_max = value;
			}
			p_child.getBack(llc);
		}
		
		return value_max;
	}
	
	/** Lance le calcul du meilleur coup pour sur plateau */
	public void run() {
		coups = null;
		jouerMinMax();
	}
}
