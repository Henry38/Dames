package Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


@SuppressWarnings("serial")
public class JPiece extends JLabel {
	
	public int line, column;
	
	/** Constructeur */
	public JPiece(int line, int column, int piece) {
		super();
		this.line = line;
		this.column = column;
		
		if (piece > 0) {
			setIcon(new ImageIcon("JetonBlanc" + piece + ".png"));
		} else {
			setIcon(new ImageIcon("JetonNoir" + (piece*-1) + ".png"));
		}
		setBounds(32 + (column*40), 32 + (line*40), 40, 40);
	}
	
	public int getLine() {
		return this.line;
	}
	
	public int getColumn() {
		return this.column;
	}
	
	public int target(int d) {
		return 32 + (d * 40);
	}
	
	public void setQueen(int piece) {
		if (piece > 0) {
			setIcon(new ImageIcon("JetonBlanc10.png"));
		} else {
			setIcon(new ImageIcon("JetonNoir10.png"));
		}
	}
}
