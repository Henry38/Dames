import java.util.EventListener;

import Graphics.JPiece;


public interface EchecsListener extends EventListener {
	public void pieceSelection(JPiece jp);
	public void caseSelection(int line, int column);
	public void finAnimation();
	public void new_game(int type);
}
