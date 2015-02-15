
public class Coup {
	
	public int pLine, pColumn, prise, tLine, tColumn;
	
	/** Constructeur */
	public Coup(int pLine, int pColumn, int prise, int tLine, int tColumn) {
		this.pLine = pLine;
		this.pColumn = pColumn;
		this.prise = prise;
		this.tLine = tLine;
		this.tColumn = tColumn;
	}
	
	/** Retourne la prise du coup (formatage spécial !) */
	public int getPrise() {
		return prise;
	}
	
	/** Retourne vrai si le coup prend une pièce à l'adversaire */
	public boolean estPrise() {
		return (prise != 0);
	}
	
	/** Retourne la pièce prise à l'aderversaire */
	public int piecePrise() {
		return (prise % 100) - 50;
	}
	
	/** Retourne la case prise par le coup, null si aucune prise */
	public int[] casePrise() {
		if (!estPrise()) {
			return null;
		}
		return new int[] {prise / 1000, (prise % 1000) / 100};
	}
	
	public boolean equals(int line, int column) {
		return (tLine == line && tColumn == column);
	}
	
	/** Représentation textuelle d'un coup */
	public String toString() {
		return "(" + pLine + "," + pColumn + ")[" + prise + "](" + tLine + "," + tColumn + ")";
	}
}
