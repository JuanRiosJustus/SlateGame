import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class Grid {
    
    private int gridDimension;
    private boolean[][] bombMap;
    private int[][] scoreMap;
    
    private Random randomizer = new Random();
    private Color boxColor = new Color(randomizer.nextInt(255), randomizer.nextInt(255), randomizer.nextInt(255));
    
    public Grid(int gridLevel) {
        
    	//TODO
    	int difficulty = gridLevel;
    	
    	switch (gridLevel) {
    	case 1: difficulty = 2; break;
    	case 2: difficulty = 8; break;
    	case 3: difficulty = 14; break;
    	case 4: difficulty = 19; break;
    	case 5: difficulty = 25; break;
    	case 6: difficulty = 35; break;
    	case 7: difficulty = 40; break;
    	}
        int bombQuantity = randomizer.nextInt(difficulty);
        
        this.gridDimension= gridLevel;
        
        bombMap = new boolean[gridLevel][gridLevel];
        scoreMap = new int[gridLevel][gridLevel];
        
        for (int rowIndex = 0; rowIndex < gridLevel; rowIndex = rowIndex + 1){
            for (int columnIndex = 0; columnIndex < gridLevel; columnIndex = columnIndex + 1) {
               
            	// allocates spaces for the bombs
            	// if difficulty is larger than 2, sets the bomb
                if (bombQuantity > 0 && randomizer.nextInt(gridLevel) > 2) {
                    bombMap[rowIndex][columnIndex] = true;
                    bombQuantity = bombQuantity - 1;
                    System.out.print("[X]");
                } else {
                    bombMap[rowIndex][columnIndex] = false;
                    if (randomizer.nextInt(8) > (2)) {
                        scoreMap[rowIndex][columnIndex] = randomizer.nextInt(10) + 1;
                    } else {
                        scoreMap[rowIndex][columnIndex] = 1;
                    }
                    System.out.print("[" + scoreMap[rowIndex][columnIndex] + "]");
                }
            }
            System.out.println();
        }
    }
    
    public void paintComponent(Graphics2D g) {
    	
    	// TODO
    	
        //Grid
        for (int rowIndex = 0; rowIndex < gridDimension; rowIndex = rowIndex + 1) {
            for (int columnIndex = 0; columnIndex < gridDimension; columnIndex = columnIndex + 1) {
                g.setColor(boxColor);
                g.fillRect(20 + (rowIndex * 80), 20 + (columnIndex * 80) , 60, 60);
            }
        }
        
        //Bomb 
        for (int rowIndex = 0; rowIndex < gridDimension; rowIndex = rowIndex + 1){
            for (int columnIndex = 0; columnIndex < gridDimension; columnIndex = columnIndex + 1) {
                if(bombMap[rowIndex][columnIndex] == true) {
                    g.setColor(boxColor);
                    g.fillOval(20 + (columnIndex * 80), 20 + (rowIndex * 80), 60, 60);
                }
            }
        }
    }
    
    // The bomb map which is true if the bomb is at the index
    public boolean[][] getBombMap() {
        return bombMap;
    }
    public int[][] getScoreMap() {
        return scoreMap;
    }
    
    public Color getBoxColor() {
        return boxColor;
    }
}
