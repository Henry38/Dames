import java.util.ArrayList;
import java.util.LinkedList;

import Graphics.JPiece;



public class Controleur {
	
	private Plateau p;
	private Fenetre fen;
	private MyListener listener;
	
	private JPiece piece_selected;
	private LinkedList<Coup> coupsJoueur;
	private ArrayList<LinkedList<Coup>> allc, listCoup;
	private boolean fini;
	
	IA_AlphaBeta cpu;
	
	/** Constructeur */
	public Controleur() {
		listener = new MyListener();
		
		p = new Plateau(1);
		fen = new Fenetre();
		fen.initializeGraphics(p);
		fen.setListener(listener);
		
		allc = p.getCoupFromJoueur();
		listCoup = new ArrayList<LinkedList<Coup>>();
		coupsJoueur = new LinkedList<Coup>();
		fini = false;
	}
	
	/** Fait jouer l'ordinateur contre le joueur humain */
	public void cpu_Joueur2() {
		// D�termine quelle IA doit prendre l'ordinateur
		cpu = new IA_AlphaBeta(p, 2);
		cpu.start();
		
		try {
			cpu.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Joue le coup calcul� par l'IA
		coupsJoueur = cpu.getCoup();
		piece_selected = fen.getJeton(coupsJoueur.getFirst().pLine, coupsJoueur.getFirst().pColumn);
		jouer();
	}
	
	/** Joue sur le plateau le coup � suivre */
	public void jouer() {
		Coup c = coupsJoueur.pollFirst();
		p.jouerCoup(c);
		if (c.estPrise()) {
			fen.effaceJeton(c.casePrise()[0], c.casePrise()[1]);
		}
		if ((p.getJoueur() == 1 && c.tLine == 0) || (p.getJoueur() == -1 && c.tLine == 9)) {
			fen.getJeton(c.pLine, c.pColumn).setQueen(p.getPiece(c.tLine, c.tColumn));
		}
		fen.animation(piece_selected, c.tLine, c.tColumn);
	}
	
	/** Classe priv�e Listener */
	private class MyListener implements EchecsListener {
		
		// Appel� quand on clique sur un jeton
		public void pieceSelection(JPiece jp) {
			if (coupsJoueur.size() == 0 && !fini) {
				listCoup.clear();
				// D�termination de la liste de coup en fonction du jeton selectionn�
				for (LinkedList<Coup> llc : allc) {
					// Si le jeton selectionn� est le d�but d'une liste de coup jouable
					if (jp.line == llc.getFirst().pLine && jp.column == llc.getFirst().pColumn) {
						listCoup.add(llc);
					}
				}
				
				// Si le jeton selectionn� est jouable
				if (listCoup.size() > 0) {
					piece_selected = jp;
					fen.afficheCoup(listCoup);
					fen.afficherSelection(jp);
				} else {
					piece_selected = null;
					fen.effaceCoup();
					fen.effacerSelection();
				}
			}
		}
		
		// Appel� quand on clique sur le damier
		public void caseSelection(int tLine, int tColumn) {
			// Si on a precedemment clique sur un jeton jouable
			if (piece_selected != null) {
				for (LinkedList<Coup> llc : listCoup) {
					// Si la case selectionn� est � la fin d'une liste de coup jouable
					if (llc.getLast().equals(tLine, tColumn)) {
						coupsJoueur = llc;
						finAnimation();
						break;
					}
				}
				fen.effaceCoup();
				fen.effacerSelection();
			}
		}
		
		// Appell� quand l'animation du jeton est termin�e
		public void finAnimation() {
			// On relance une animation s'il au moins reste un d�placement
			if (coupsJoueur.size() > 0) {
				jouer();
			// Sinon on change de joueur
			} else {
				piece_selected = null;
				p.switchJoueur();
				allc = p.getCoupFromJoueur();
				// si impossibilit� de jouer
				if (allc.size() == 0) {
					fini = true;
					System.out.println("Le joueur " + p.getJoueur() + " a perdu !");
					System.out.println("Le joueur " + p.getJoueur()*-1 + " a gagn� !");
				// Sinon fait jouer l'ordinateur si c'est � lui
				} else if (p.getJoueur() == -1) { 
					cpu_Joueur2();
				}
			}
		}
		
		// Nouveau jeu
		public void new_game(int type) {
			
		}
	}
}
