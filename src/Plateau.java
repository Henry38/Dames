import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;


public class Plateau {
	
	private int[][] table;
	public int joueur, nbBlanc, nbNoir;
	
	/** Constructeur */
	public Plateau(int joueur) {
		new_game();
		setJoueur(joueur);
	}
	
	/** Constructeur */
	public Plateau(int[][] table, int joueur) {
		this.table = new int[10][10];
		for (int i=0; i<10; i++) {
			for (int j=(i+1)%2; j<10; j+=2) {
				this.table[i][j] = table[i][j];
				if (table[i][j] > 0) {
					nbBlanc++;
				} else if (table[i][j] < 0) {
					nbNoir++;
				}
			}
		}
		setJoueur(joueur);
	}
	
	/** Clone et renvoie le plateau de jeu */
	public Plateau clone() {
		return new Plateau(table, getJoueur());
	}
	
	/** Initialize une nouvelle table de jeu */
	public void new_game() {
		table = new int[][] {
			{ 0,-1, 0,-1, 0,-1, 0,-1, 0,-1},
			{-1, 0,-1, 0,-1, 0,-1, 0,-1, 0},
			{ 0,-1, 0,-1, 0,-1, 0,-1, 0,-1},
			{-1, 0,-1, 0,-1, 0,-1, 0,-1, 0},
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{ 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
			{ 1, 0, 1, 0, 1, 0, 1, 0, 1, 0},
			{ 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
			{ 1, 0, 1, 0, 1, 0, 1, 0, 1, 0}
		};
		nbBlanc = 40;
		nbNoir = 40;
	}
	
	/** Retourne le numero du joueur courant */
	public int getJoueur() {
		return joueur;
	}
	
	/** Défini le numéro du joueur */
	public void setJoueur(int joueur) {
		this.joueur = joueur;
	}
	
	/** Change le joueur */
	public void switchJoueur() {
		this.joueur *= -1;
	}
	
	/** Retourne la pièce pointée sur le plateau, 0 si on pointe à l'extérieur */
	public int getPiece(int line, int column) {
		if (!valid(line, column)) {
			return 0;
		}
		return table[line][column];
	}
	
	/** Retourne vrai si la case pointée n'est pas en dehors des limites du plateau */
	public boolean valid(int line, int column) {
		return (line >= 0 && line <= 9 && column >= 0 && column <= 9);
	}
	
	/** Retourne vrai si le jeton pointé appartient au joueur */
	public boolean caseJoueur(int line, int column) {
		return (getPiece(line, column) * getJoueur() > 0);
	}
	
	/** Retourne vrai si le jeton pointé appartient au joueur adverse */
	public boolean caseAdverse(int line, int column) {
		return (getPiece(line, column) * getJoueur() < 0);
	}
	
	/** Retourne vrai si la case pointé est une case vide du plateau */
	public boolean caseVide(int line, int column) {
		return (valid(line, column) && getPiece(line, column) == 0);
	}
	
	/** Retourne vrai si le jeton pointé est une dame */
	public boolean isQueen(int line, int column) {
		return (Math.abs(getPiece(line, column)) == 10);
	}
	
	/** Vérifie que la case pointée appartient au joueur */
	public boolean wrongPlayer(int line, int column) {
		return (!caseJoueur(line, column));
	}
	
	/** Retourne les cases adjacentes jouables pour un jeton */
	private ArrayList<Coup> getCoupFromPiece(int line, int column) {
		// Renvoie null si le jeton selectionné n'appartient pas au joueur courant
		if (wrongPlayer(line, column)) {
			System.out.println("Wrong Player getCoupFromPiece");
			return null;
		}
		
		ArrayList<Coup> allc = new ArrayList<Coup>();
		// Si le jeton n'est pas une dame
		if (!isQueen(line, column)) {
			// Parcourt des 4 cases adjacentes du jeton
			for (int i : new int[] {-1, 1}) {
				for (int j : new int[] {-1, 1}) {
					// Si la case sur la diagonale avant est vide
					if (caseVide(line+i, column+j) && i == getJoueur()*-1) {
						allc.add(new Coup(line, column, 0, line+i, column+j));
					}
				}
			}
		} else {
			// Parcourt des 4 diagonales du jeton
			int d;
			for (int i : new int[] {-1, 1}) {
				for (int j : new int[] {-1, 1}) {
					d = 1;
					// Tant que la case sur la diagonale est vide
					while (caseVide(line+d*i, column+d*j)) {
						allc.add(new Coup(line, column, 0, line+d*i, column+d*j));
						d++;
					}
				}
			}
		}
		
		return allc;
	}
	
	/** Retourne les cases adjacentes jouables imposée pour un jeton */
	private ArrayList<Coup> getCoupObligerFromPiece(int line, int column) {
		// Renvoie null si le jeton selectionné n'appartient pas au joueur courant
		if (wrongPlayer(line, column)) {
			System.out.println("Wrong Player getCoupObligerFromPiece");
			return null;
		}
		
		ArrayList<Coup> allc = new ArrayList<Coup>();
		// Si le jeton n'est pas une dame
		if (!isQueen(line, column)) {
			// Parcourt des 4 cases adjacentes du jeton
			for (int i : new int[] {-1, 1}) {
				for (int j : new int[] {-1, 1}) {
					// Si sur la diagonale, il y un jeton adverse suivit d'une case vide
					if (caseAdverse(line+i, column+j) && caseVide(line+(2*i), column+(2*j))) {
						int x = (line+i) * 1000 + (column+j) * 100 + 50 + getPiece(line+i, column+j);
						allc.add(new Coup(line, column, x, line+2*i, column+2*j));
					}
				}
			}
		} else {
			// Parcourt des 4 diagonales du jeton
			int d;
			for (int i : new int[] {-1, 1}) {
				for (int j : new int[] {-1, 1}) {
					d = 1;
					// Tant que la case sur la diagonale avant est vide
					while (caseVide(line+d*i, column+d*j)) {
						d++;
					}
					// Si la case suivante est un jeton adverse
					if (caseAdverse(line+d*i, column+d*j)) {
						int prise = (line+d*i) * 1000 + (column+d*j) * 100 + 50 + getPiece(line+d*i, column+d*j);
						d++;
						// Tant que la case sur la diagonale après est vide
						while (caseVide(line+d*i, column+d*j)) {
							allc.add(new Coup(line, column, prise, line+d*i, column+d*j));
							d++;
						}
					}
				}
			}
		}
		
		return allc;
	}
	
	/** Retourne l'arbre des suites de coups obligés pour un jeton */
	private Arbre getTreeCoupObligerFromPiece(int line, int column, int prise) {
		// Renvoie null si le jeton selectionné n'appartient pas au joueur courant
		if (wrongPlayer(line, column)) {
			System.out.println("Wrong Player getTree");
			return null;
		}
		
		Arbre ac = new Arbre(line, column, prise);
		ArrayList<Coup> coups = getCoupObligerFromPiece(line, column);
		
		Plateau p = clone();
		for (Coup c : coups) {
			p.jouerCoup(c);
			ac.add(p.getTreeCoupObligerFromPiece(c.tLine, c.tColumn, c.getPrise()));
			p.getBackCoup(c);
		}
		
		return ac;
	}
	
	/** Retourne l'ensemble des coups non obligés pour le joueur */
	private ArrayList<LinkedList<Coup>> getCoupNonObligerFromJoueur() {
		ArrayList<LinkedList<Coup>> res = new ArrayList<LinkedList<Coup>>();
		
		// Parcourt du damier
		for (int i=0; i<10; i++) {
			for (int j=(i+1)%2; j<10; j+=2) {
				// Récupèration des coups non obligés pour chaque jeton du joueur
				if (caseJoueur(i, j)) {
					for (Coup c : getCoupFromPiece(i, j)) {
						res.add(new LinkedList<Coup>());
						res.get(res.size()-1).add(c);
					}
				}
			}
		}
		
		return res;
	}
	
	/** Retourne l'ensemble des suites des coups obligés de plus grande taille pour le joueur */
	private ArrayList<LinkedList<Coup>> getCoupObligerFromJoueur() {
		ArrayList<LinkedList<Coup>> res = new ArrayList<LinkedList<Coup>>();
		
		Arbre tmp;
		ArrayList<Arbre> aa = new ArrayList<Arbre>();
		int profondeur;
		int h = -1;
		
		// Parcourt des jetons du damier, et détermination des arbres des parcourts des coups obligés
		for (int i=0; i<10; i++) {
			for (int j=(i+1)%2; j<10; j+=2) {
				if (caseJoueur(i, j)) {
					tmp = getTreeCoupObligerFromPiece(i, j, 0);
					profondeur = tmp.hauteur();
					// Si au moins un mouvement est imposé à un jeton et que celui-ci est plus grand
					// en nombre de coup
					if (profondeur > 0 && profondeur > h) {
						h = profondeur;
						aa.clear();
						aa.add(tmp);
					} else if (profondeur > 0 && profondeur == h) {
						aa.add(tmp);
					}
				}
			}
		}
		
		// On récupère dans res les coups obligés de plus grande taille
		int maxi = 0;
		// Parcourt de tout les arbres retenus
		for (Arbre a : aa) {
			// On ne retient que les LinkedList les plus grandes
			for (LinkedList<Coup> llc : a.toArrayOfLinkedList()) {
				if (llc.size() > maxi) {
					maxi = llc.size();
					res.clear();
					res.add(llc);
				} else if (llc.size() == maxi) {
					res.add(llc);
				}
			}
		}
		
		return res;
	}
	
	/** Retourne l'ensemble des coups pouvant être joués par le joueur */
	public ArrayList<LinkedList<Coup>> getCoupFromJoueur() {
		ArrayList<LinkedList<Coup>> allc_obliger = getCoupObligerFromJoueur();
		if (allc_obliger.size() > 0) {
			return allc_obliger;
		}
		return getCoupNonObligerFromJoueur();
	}
	
	/** Joue un coup sur le plateau */
	public void jouerCoup(Coup c) throws NoSuchElementException {
		// Si le coup ne joue une piece du joueur courant, on lève une exception
		if (wrongPlayer(c.pLine, c.pColumn)) {
			throw new NoSuchElementException();
		}
		
		table[c.tLine][c.tColumn] = table[c.pLine][c.pColumn];
		table[c.pLine][c.pColumn] = 0;
		if (c.estPrise()) {
			int[] casePrise = c.casePrise();
			table[casePrise[0]][casePrise[1]] = 0;
			if (getJoueur() == 1) {
				nbNoir -= 1;
			} else {
				nbBlanc -= 1;
			}
		}
		// Transformation en dame
		if (!isQueen(c.tLine, c.tColumn)) {
			if ((getJoueur() == 1 && c.tLine == 0) || (getJoueur() == -1 && c.tLine == 9)) {
				table[c.tLine][c.tColumn] *= 10;
			}
		}
		
	}
	
	/** Joue une suite de coup sur le plateau */
	public void jouerListCoup(LinkedList<Coup> llc) {
		for (Coup c : llc) {
			jouerCoup(c);
		}
	}
	
	/** Annule un coup qui vient d'être joué */
	public void getBackCoup(Coup c) {
		table[c.pLine][c.pColumn] = table[c.tLine][c.tColumn];
		table[c.tLine][c.tColumn] = 0;
		if (c.estPrise()) {
			int[] casePrise = c.casePrise();
			table[casePrise[0]][casePrise[1]] = c.piecePrise();
			if (getJoueur() == 1) {
				nbNoir += 1;
			} else {
				nbBlanc += 1;
			}
		}
	}
	
	/** Annule une suite de coup qui vient d'être joué */
	public void getBackListCoup(LinkedList<Coup> llc) {
		for (Coup c : llc) {
			getBackCoup(c);
		}
	}
	
	/** Annule la suite de coup qui vient d'être jouée */
	public void getBack(LinkedList<Coup> llc) {
		// A faire dans cet ordre car en cas de prise en cycle ça ne marche pas !
		int tmp = table[llc.getLast().tLine][llc.getLast().tColumn];
		table[llc.getLast().tLine][llc.getLast().tColumn] = 0;
		table[llc.getFirst().pLine][llc.getFirst().pColumn] = tmp;
		
		// Parcourt de tout les coups réalisés
		for (Coup c : llc) {
			// Replacement des pions avant la réalisation des coups
			if (c.estPrise()) {
				int[] casePrise = c.casePrise();
				table[casePrise[0]][casePrise[1]] = c.piecePrise();
				if (getJoueur() == 1) {
					nbNoir += 1;
				} else {
					nbBlanc += 1;
				}
			}
		}
	}
	
	/** Retourne vrai si l'un des 2 joueurs a gagné */
	public boolean gagner() {
		return (nbBlanc == 0 || nbNoir == 0);
	}
	
	/** Fonction evaluation du jeu */
	public int evaluation() {
		// Si le blanc à gagner
		if (nbNoir == 0) {
			return 1000;
		// Sinon si le joueur noir à gagner
		} else if (nbBlanc == 0) {
			return -1000;
		// Sinon retourne la différence du nombres de pions de chacun des joueur
		} else {
			return nbBlanc - nbNoir;
		}
	}
	
	/** Ecrit sur le terminal le plateau de jeu */
	public void imprime() {
		String s = "####### Plateau de JEU #######\n\n";
		int x;
		for (int i=0; i<10; i++) {
			for (int j=0; j<10; j++) {
				x = getPiece(i, j);
				if (x == 0) {
					s += " ";
				} else if (x == -1){
					s += "2";
				} else if (x == 1) {
					s += "1";
				} else if (x == -10) {
					s += "5";
				} else if (x == 10) {
					s += "4";
				}
			}
			s += "\n";
		}
		System.out.println(s);
	}
}
