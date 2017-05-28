import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Tutorial extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3074107466951144775L;
	
	private BufferedImage tutorial;
	public Tutorial() {
		try {
			tutorial = ImageIO.read(new File("src/gameAssets/essentials/tutorial.png"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	protected void paintComponent(Graphics g) {
		g.drawImage(tutorial, 0, 0, this);
		g.dispose();
	}
}
