import java.util.ArrayList;
import java.util.LinkedList;


public class Arbre {
	
	private int line, column, prise;
	private ArrayList<Arbre> fils;
	
	/** Constructeur */
	public Arbre(int line, int column, int prise) {
		this.line = line;
		this.column = column;
		this.prise = prise;
		this.fils = new ArrayList<Arbre>();
	}
	
	/** Retourne la ligne */
	public int getLine() {
		return this.line;
	}
	
	/** Retourne la colonne */
	public int getColumn() {
		return this.column;
	}
	
	/** Retourne la pièce prise à l'adversaire */
	public int getPrise() {
		return prise;
	}
	
	/** Ajoute un fils à l'arbre */
	public void add(Arbre a) {
		fils.add(a);
	}
	
	/** Retourne la profondeur de l'arbre */
	public int hauteur() {
		if (estFeuille()) {
			return 0;
		}
		int res = 1;
		for (Arbre f : fils) {
			res = Math.max(f.hauteur()+1, res);
		}
		return res;
	}
	
	/** Retourne vrai si l'arbre est une feuille */
	public boolean estFeuille() {
		return (fils.size() == 0);
	}
	
	/** Retourne sous forme de tableau de LinkedList l'ensemble des coups représentés par l'arbre */
	public ArrayList<LinkedList<Coup>> toArrayOfLinkedList() {
		if (estFeuille()) {
			return null;
		}
		
		ArrayList<LinkedList<Coup>> tmp;
		ArrayList<LinkedList<Coup>> res = new ArrayList<LinkedList<Coup>>();
		LinkedList<Coup> ll;
		
		for (Arbre f : fils) {
			tmp = f.toArrayOfLinkedList();
			if (tmp == null) {
				ll = new LinkedList<Coup>();
				ll.add(new Coup(getLine(), getColumn(), f.getPrise(), f.getLine(), f.getColumn()));
				res.add(ll);
			} else {
				for (LinkedList<Coup> llc : tmp) {
					llc.addFirst(new Coup(getLine(), getColumn(), f.getPrise(), f.getLine(), f.getColumn()));
					res.add(llc);
				}
			}
		}
		
		return res;
	}
}
