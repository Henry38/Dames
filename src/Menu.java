import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class Menu extends JMenuBar {
	
	private JMenu fichier, difficulte;
	private JMenuItem start, end, niveau1, niveau2, niveau3, hvh, hvo, ovh;
	private EchecsListener listener;
	
	/** Constructeur */
	public Menu() {
		super();
		
		// Creation du sous-menu nouvelle partie
		start = new JMenuItem("Nouvelle partie");
		end = new JMenuItem("Quitter");
		hvh = new JMenuItem("Joueur contre Joueur");
		hvo = new JMenuItem("Joueur contre ordinateur");
		ovh = new JMenuItem("Ordinateur contre Joueur");
		start.add(hvh);
		start.add(hvo);
		start.add(ovh);
		
		// Creation du menu fichier avec ses sous menus
		fichier = new JMenu("Fichier");
		fichier.add(start);
		fichier.add(end);
		
		// Creation du menu difficulté IA avec ses sous menus
		difficulte = new JMenu("Difficulté IA");		
		niveau1 = new JMenuItem("facile");
		niveau2 = new JMenuItem("intermédiaire");
		niveau3 = new JMenuItem("difficile");
		difficulte.add(niveau1);
		difficulte.add(niveau2);
		difficulte.add(niveau3);
		
		// Ajout des JMenuItem au menu
		this.add(fichier);
		this.add(difficulte);
		
		end.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		// Creation des listener pour chaque sous menu
		hvh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.new_game(0);
			}
		});
		
		hvo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.new_game(1);
			}
		});
		
		ovh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.new_game(2);
			}
		});
	}
	
	/** Initialise le listener */
	public void setListener(EchecsListener listener) {
        this.listener = listener;
    }
}
