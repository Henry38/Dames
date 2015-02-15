import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Graphics.JPiece;
import Graphics.JPoint;


@SuppressWarnings("serial")
public class Fenetre extends JFrame {
	
	private JPanel panneau;
	private JLabel sprite_damier, sprite_selection;
	private Menu menu;
	private EchecsListener listener;
	
	private ArrayList<JPiece> al;
	private ArrayList<JPoint> points;
	private Thread t;
	
	/** Constructeur */
	public Fenetre() {
		super("Dames v1.0");
		setLocation(50, 50);
		setSize(632, 568);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		al = new ArrayList<JPiece>();
		points = new ArrayList<JPoint>();
		
		// Panneau de fond
		panneau = new JPanel();
		panneau.setLayout(null);
		
		// Image du plateau
		sprite_damier = new JLabel();
		sprite_damier.setIcon(new ImageIcon("Damier2.png"));
		sprite_damier.setBounds(0, 0, 433, 433);
		sprite_damier.addMouseListener(new damier_actionlistener());
		
		// Image de la surbrillance
		sprite_selection = new JLabel();
		sprite_selection.setIcon(new ImageIcon("Selection.png"));
		sprite_selection.setBounds(0, 0, 40, 40);
		sprite_selection.setVisible(false);
		
		panneau.add(sprite_selection);
		panneau.add(sprite_damier);
		
		// Definit les priorites de superposition des images
		panneau.setComponentZOrder(sprite_damier, 1);
		panneau.setComponentZOrder(sprite_selection, 0);
		
		menu = new Menu();
		setJMenuBar(menu);
		
		setContentPane(panneau);
		setVisible(true);
	}
	
	/** Initialize le plateau graphique à partir du plateau de jeu */
	public void initializeGraphics(Plateau p) {
		JPiece jp;
		// Parcourt du tableau
		for (int i=0; i<10; i++) {
			for (int j=(i+1)%2; j<10; j+=2) {
				//if (p.getPiece(i, j) != 0) {
				if (!p.caseVide(i, j)) {
					jp = new JPiece(i, j, p.getPiece(i, j));
					jp.addMouseListener(new PieceListener(jp));
					al.add(jp);
					panneau.add(jp);
					panneau.setComponentZOrder(jp, 1);
				}
			}
		}
		repaint();
	}
	
	public EchecsListener getListener() {
		return listener;
	}
	
	public void setListener(EchecsListener listener) {
		this.listener = listener;
		menu.setListener(listener);
	}
	
	public void addPoint() {
		JPoint jp = new JPoint();
		points.add(jp);
		panneau.add(jp);
		panneau.setComponentZOrder(jp, 1);
	}
	
	/** Retourne le jeton pointé */
	public JPiece getJeton(int line, int column) {
		JPiece res = null;
		for (JPiece jp : al) {
			if (jp.line == line && jp.column == column) {
				res = jp;
				break;
			}
		}
		return res;
	}
	
	public void effaceJeton(int line, int column) {
		JPiece jp = getJeton(line, column);
		jp.setVisible(false);
		al.remove(jp);
		panneau.remove(jp);
	}
	
	/** Lance l'animation du jeton */
	public void animation(JPiece jp, int tLine, int tColumn) {
		t = new Animation(jp, tLine, tColumn);
		t.start();
	}
	
	/** Affiche l'ensemble des coups jouables pour le jeton selectionné */
	public void afficheCoup(ArrayList<LinkedList<Coup>> listCoup) {
		for (int i=0; i<listCoup.size(); i++) {
			if (i >= points.size()) {
				addPoint();
			}
			points.get(i).setVisible(true);
			points.get(i).setLocation(32 + 40 * listCoup.get(i).getLast().tColumn, 32 + 40 * listCoup.get(i).getLast().tLine);
		}
		for (int i=listCoup.size(); i<points.size(); i++) {
			points.get(i).setVisible(false);
		}
	}
	
	/** Efface l'ensemble des marqueurs */
	public void effaceCoup() {
		for (JPoint p : points) {
			p.setVisible(false);
		}
	}
	
	public void afficherSelection(JPiece jp) {
		//sprite_selection.setVisible(true);
		//sprite_selection.setLocation(32 + jp.column * 40, 32 + jp.line * 40);
	}
	
	public void effacerSelection() {
		sprite_selection.setVisible(false);
	}
	
	/** Classe privée Listener */
	private class PieceListener extends MouseAdapter {
		
		private JPiece jp;
		
		public PieceListener(JPiece jp) {
			super();
			this.jp = jp;
		}
		
		public void mouseClicked(MouseEvent ev) {
			listener.pieceSelection(jp);
		}
	}
	
	/** Classe privee Listener */
	private class damier_actionlistener extends MouseAdapter {
		
		public void mouseClicked(MouseEvent ev) {
			listener.caseSelection((ev.getY() - 40) / 40, (ev.getX() - 40) / 40);
		}
	}
	
	/** Classe privee Thread */
	private class Animation extends Thread {
		
		private JPiece jp;
		private int jpx, jpy, tx, ty;
		
		/** Constructeur */
		public Animation(JPiece jp, int line, int column) {
			this.jp = jp;
			this.jpx = jp.target(jp.getColumn());
			this.jpy = jp.target(jp.getLine());
			this.tx = jp.target(column);
			this.ty = jp.target(line);
		}
		
		public void run() {
			double x, y;
			for (int t=0; t<=16; t++) {
				x = jpx + (tx - jpx) * (t / 16.0);
				y = jpy + (ty - jpy) * (t / 16.0);
				jp.setLocation((int)x, (int)y);
				try {
					sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			jp.line = (jp.getY() - 32) / 40;
			jp.column = (jp.getX() - 32) / 40;
			listener.finAnimation();
		}
	}
}
