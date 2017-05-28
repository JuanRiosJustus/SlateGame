import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public final class ProjectManager {

    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static int screenWidth = (int) screenSize.getWidth();
    private static int screenHeight = (int) screenSize.getHeight();
    
    private static BufferedImage[] numberImageList;
	
	public static int screenSizeWidth() {
		return (((screenWidth*2)/4) + (screenWidth/4));
	}
	
	public static int screenSizeHeight () {
		return (((screenHeight*2)/5)) + (screenWidth/4);
	}
	
	//TODO
	public static void loadNumbers() {
    	File numberFolder = new File("src/gameAssets/numbers/");
    	File[] llistOfNumbers = numberFolder.listFiles();
    	try {
    			numberImageList = new BufferedImage[10];
    			for (int count = 0; count < 10; count = count + 1) {
    				if (llistOfNumbers[count].isFile()) {
    					numberImageList[count] = ImageIO.read(new File("src/gameAssets/numbers/"+ llistOfNumbers[count].getName()));
    					System.out.println("src/gameAssets/numbers/" + llistOfNumbers[count].getName());
    				}
    			}
    		} catch (Exception ex) {
    			ex.printStackTrace();
    		}
	}
	
	public static BufferedImage numberToImage(int num) {
		return numberImageList[num];
	}
	
	public static void projectProps() {
		System.out.println("The edited screen width and height is :" + screenSizeWidth() + "x" +  screenSizeHeight());
		System.out.println("The raw screen width and height:" + screenWidth + "x" + screenHeight);
	}


}
