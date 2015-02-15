package Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class JPoint extends JLabel {
	
	public JPoint() {
		super();
		setIcon(new ImageIcon("point.png"));
		setBounds(0, 0, 40, 40);
		setVisible(false);
	}
}
