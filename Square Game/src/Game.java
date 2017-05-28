import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel implements ActionListener, MouseListener, KeyListener, Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 638967204208026612L;
    
    // Integer variables for screen adjustments

    private int boxWidth = 60;
    private int boxHeight = 60;
    
    private int stageScore = 0;
    private int totalScore = 0;
    
    
    private int stage = 1;
    private int lives = 3;
    private int gridDimension = 4;
    
    private boolean reset = false;
    private boolean started = true;
    
    private Timer timer;
    private Grid grid;
    private AudioPlayer soundX;
    private AudioPlayer soundO;
    private AudioPlayer noContinue;
    
    private int[][] scoreMap;
    private boolean[][] negateMap = GameManager.allFalseArray(gridDimension);
    private boolean[][] bombMap;
    private BufferedImage boxes;
    private BufferedImage xBar;
    private BufferedImage yBar;
    private BufferedImage o;
    private BufferedImage x;
    private BufferedImage statusBar;
    
    private int arrayPositionX;
    private int arrayPositionY;
    
    private int passScore;
    private GameTime twoMinute;
    private Color backgroundColor = new Color(207,207,207);

    @SuppressWarnings("unused")
    private boolean filesFound = true;
    
    private int bombAmountX;
    private int pointsAvailableX;
    private int bombAmountY;
    private int pointsAvailableY;
    
    public Game() {
    	//TODO
        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(10, this);
        timer.start();
        grid = new Grid(gridDimension);
        scoreMap = grid.getScoreMap();
        bombMap = grid.getBombMap();
        passScore = GameManager.totalPoints(scoreMap);
        twoMinute = new GameTime(120);
        soundX = new AudioPlayer("src/gameAssets/sounds/xDrop.wav");
        soundO = new AudioPlayer("src/gameAssets/sounds/oDrop.wav");
        noContinue = new AudioPlayer("src/gameAssets/sounds/noContinue.wav");
        
        try {
        	statusBar = ImageIO.read(new File("src/gameAssets/essentials/statusBar.png"));
        	boxes = ImageIO.read(new File("src/gameAssets/essentials/boxes.png"));
        	xBar = ImageIO.read(new File("src/gameAssets/essentials/xBar.png"));
        	yBar = ImageIO.read(new File("src/gameAssets/essentials/yBar.png"));
        	o = ImageIO.read(new File("src/gameAssets/essentials/o.png"));
        	x = ImageIO.read(new File("src/gameAssets/essentials/x.png"));
        } catch (IOException ex) {
        	System.out.println("some files not found");
        	filesFound = false;
        	ex.printStackTrace();
        }
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (stageScore == 0 && lives == 3 && Main.timerOn() && started) {
        	resetTime(Main.timerOn());
        	started = false;
        }
        
        // the background for the game
        g.setColor(backgroundColor);
        g.fillRect(0, 0, ProjectManager.screenSizeWidth(),ProjectManager.screenSizeHeight());
        
        // Draws the boxes for the game
        for (int rowIndex = 0; rowIndex < gridDimension; rowIndex = rowIndex + 1) {
            for (int columnIndex = 0; columnIndex < gridDimension; columnIndex = columnIndex + 1) {
                g.drawImage(boxes, 20+ (rowIndex * 80),  20 + (columnIndex * 80), this);
            }
        }
        
        // drawing the grid
        if (filesFound = false) {
        	// Creates drawn boxes in event of IOException
        	grid.paintComponent((Graphics2D) g);
        }
        
        //Draws the info box bars  for the numbers(Consists of only 2 images, not the grid)
        g.drawImage(xBar, 15,  570, this);
        g.drawImage(yBar, 570,  10, this);
        
        // info Boxes and its properties (bottom most and right most boxes)
        // Created from left-bottom/right-top to bottom-right
        for (int coordinate = 20; coordinate <= 420; coordinate = coordinate + 80) {
            
            
        	if (filesFound = false) {
        		// Bottom side info box creation
        		g.setColor(Color.WHITE);
        		g.fillRect(coordinate, 570, boxWidth*2, boxHeight/2);
            
        		// Right side info box creation
        		g.setColor(Color.WHITE);
        		g.fillRect(570, coordinate , boxWidth, boxHeight);
        	}
           
            // main Info box on the right most side of the screen
            g.setColor(Color.BLACK);
            g.drawImage(statusBar, 660, 10, this); // the status bar, 660, 10
            
            g.setFont(new Font("serif", Font.BOLD, 25));
           // g.drawString("" + stage, 850, 70);
            if (stage <= 9) {
            	g.drawImage(ProjectManager.numberToImage(stage), 850, 70, this);
            } else if (99 >= stage && stage > 9) {
            	StringBuilder string = new StringBuilder();
            	string.append("" + stage);
            	char[] integers = string.toString().toCharArray();
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 850, 70, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 870, 70, this);
            } else if (999 >= stage && stage >99) {
            	StringBuilder string = new StringBuilder();
            	string.append("" + stage);
            	char[] integers = string.toString().toCharArray();
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 850, 70, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 870, 70, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[2])), 890, 70, this);
            } else if (9999 >= stage && stage > 999) {
            	StringBuilder string = new StringBuilder();
            	string.append("" + stage);
            	char[] integers = string.toString().toCharArray();
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 850, 70, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 870, 70, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[2])), 890, 70, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[3])), 910, 70, this);
            }
            
            // drawing the lives
            //g.drawString("" +lives, 850, 200); // lives 870, 200
            if (9 >= lives ) {
            	g.drawImage(ProjectManager.numberToImage(lives), 850, 175, this);
            } else if (99 >= lives && lives > 9) {
            	StringBuilder string = new StringBuilder();
            	string.append("" + lives);
            	char[] integers = string.toString().toCharArray();
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 850, 175, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 870, 175, this);
            } else if (999 >= lives && lives >99) {
            	StringBuilder string = new StringBuilder();
            	string.append("" + lives);
            	char[] integers = string.toString().toCharArray();
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 850, 175, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 870, 175, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[2])), 890, 175, this);
            } else if (9999 >= lives && lives > 999) {
            	StringBuilder string = new StringBuilder();
            	string.append("" + lives);
            	char[] integers = string.toString().toCharArray();
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 850, 175, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 870, 175, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[2])), 890, 175, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[3])), 910, 175, this);
            } else {
            	
            }
            
            // drawing the score
            if (9 >= stageScore ) {
            	g.drawImage(ProjectManager.numberToImage(stageScore), 850, 280, this);
            } else if (99 >= stageScore && stageScore > 9) {
            	StringBuilder string = new StringBuilder();
            	string.append("" + stageScore);
            	char[] integers = string.toString().toCharArray();
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 850, 280, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 870, 280, this);
            } else if (999 >= stageScore && stageScore > 99) {
            	StringBuilder string = new StringBuilder();
            	string.append("" + stageScore);
            	char[] integers = string.toString().toCharArray();
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 850, 280, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 870, 280, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[2])), 890, 280, this);
            } else if (9999 >= stageScore && stageScore > 999) {
            	StringBuilder string = new StringBuilder();
            	string.append("" + stageScore);
            	char[] integers = string.toString().toCharArray();
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 850, 280, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 870, 280, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[2])), 890, 280, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[3])), 910, 280, this);
            }
            
            // The timer

            if(twoMinute.getTimerTime() >= 0) {
                if (twoMinute.getTimerTime() > 99) {
                	StringBuilder string = new StringBuilder();
                	string.append("" + twoMinute.getTimerTime());
                	char[] integers = string.toString().toCharArray();
                	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 850, 385, this);
                	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 870, 385, this);
                	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[2])), 890, 385, this);
                } else if (99 >twoMinute.getTimerTime() && twoMinute.getTimerTime() > 9) {
                	StringBuilder string = new StringBuilder();
                	string.append("" + twoMinute.getTimerTime());
                	char[] integers = string.toString().toCharArray();
                	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 850, 385, this);
                	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 870, 385, this);
                } else if (twoMinute.getTimerTime() < 9) {
                	g.drawImage(ProjectManager.numberToImage(twoMinute.getTimerTime()), 850, 385, this);
                }
            } else {
            	g.drawImage(ProjectManager.numberToImage(0), 850, 385, this);
            }
        }

        // Info panel boxes and their contents where the left side are the bombs
        // And the right side pertains to the amount of points available in the row/column
        for (int count = 0; count < gridDimension; count = count + 1) {
        	g.setColor(Color.GRAY);
        	g.setFont(new Font("serif", Font.BOLD, 15));
        	bombAmountX =GameManager.bombDetection(bombMap, count, gridDimension, true);
        	pointsAvailableX = GameManager.pointDetection(scoreMap, count, gridDimension, true);
            bombAmountY = GameManager.bombDetection(bombMap, gridDimension, count, false);
            pointsAvailableY = GameManager.pointDetection(scoreMap, gridDimension, count, false);
            g.drawString("    "+ bombAmountY + " || " + " " + pointsAvailableY, 570, 40 + (80 *count));
        	g.drawString("    "+ bombAmountX + " || " + " " + pointsAvailableX, 20 + (80 *count), 590);
        }
        
        // Draws corresponding image to the screen for all areas clicked by the user
        for (int rowIndex = 0; rowIndex < gridDimension; rowIndex = rowIndex + 1){
            //g.setColor(Color.BLACK);
            g.setFont(new Font("Serif", Font.BOLD, 60));
            for (int count = 0; count < gridDimension; count = count + 1) {
            	if (scoreMap[rowIndex][count] == 0 && bombMap[rowIndex][count] == false) {
            		g.drawImage(x,  (25 +(80 *count)), 25 + (rowIndex * 80), this);
            	} 
            	if (negateMap[rowIndex][count] ==true && bombMap[rowIndex][count] == true) {
            		g.drawImage(o,  (25 +(80 *count)), 25 + (rowIndex * 80), this);
            	}
            }
        }

        if (lives == 0 || reset == true) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Serif", Font.BOLD, 15));
            g.drawString("Press Enter to restart the game", 720,  515);
        }
        
        if (stageScore >= passScore && lives > 0) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Serif", Font.BOLD, 15));
            g.drawString("Press Enter to advance to next stage", 720,  515);
        }
        
        g.dispose();
    }

    public void mousePressed(MouseEvent e) {
        
    	
    	// Variables initialize array search at the beginning index
        boolean bombFound = false;
        arrayPositionX = 0;
        arrayPositionY = 0;
        
        GTG:
        	if (lives > 0 && twoMinute.getTimerTime() > 0) {
                // Grid position determines which scans row first then next row top-to-bottom
        		// Only goes through when the user has adequate amount of lives
                for (int gridPositionY = 20; gridPositionY <= 20 + (gridDimension*100); gridPositionY = gridPositionY + 80 ) {
                	for (int advanceRight = 0; advanceRight <= gridDimension*100; advanceRight = advanceRight + 80)  {
                        if ((e.getX() >= 20 + advanceRight && e.getX() <= (20 + boxWidth) + advanceRight) &&  (e.getY() >= gridPositionY  && e.getY() <= gridPositionY + boxHeight)) {
                            if (bombMap[arrayPositionY][arrayPositionX] == true) {
                            	// Executes then the user hits true on the bombMap[][] , You hit a bomb
                            	// Plays corresponding sound and decrements a life 
                            	soundO.playSound();
                                System.out.println("Bomb located at: " + (arrayPositionX + 1) + "," +  (arrayPositionY + 1));
                                negateMap[arrayPositionY][arrayPositionX] = true;
                                lives = lives - 1;
                                bombFound = true;
                            	} else {
                            		// Executes when the user doesn't click on a bomb
                            		// Sets the  score equal to the amount you clicked and adds it to total score
                            	soundX.playSound();
                                System.out.println("No bomb found at: " + (arrayPositionX + 1) + "," +  (arrayPositionY + 1));
                                	if (scoreMap[arrayPositionY][arrayPositionX] > 0) {
                                		stageScore = stageScore + scoreMap[arrayPositionY][arrayPositionX];
                                		scoreMap[arrayPositionY][arrayPositionX] = 0;
                                	}
                            	}
                            }
                        if (bombFound == true) {
                        	// When the bomb is found , jump out of loop
                        	break GTG;
                        }
                        arrayPositionX = arrayPositionX + 1;
                	}
                	arrayPositionX = 0;
                	arrayPositionY = arrayPositionY + 1;
                }
        	} else {
        		noContinue.playSound();
        		reset = true;
        	}   
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
    	
    	//What happens when the user loses the game.
        if(e.getKeyCode() == KeyEvent.VK_ENTER && (lives == 0 || reset == true)){    
            grid = new Grid(gridDimension);
            scoreMap = grid.getScoreMap();
            negateMap = GameManager.allFalseArray(gridDimension);
            bombMap = grid.getBombMap();
            passScore = GameManager.totalPoints(scoreMap);
            stageScore = 0;
            totalScore = 0;
            lives = 3;
            stage = 1;
            gridDimension = 4;
            reset = false;
            twoMinute.resetTimer(120);
        }
        
        // What happens the user has an adequate amount of points to advance to the next stage
        // adequate score if based on the amount of points available on scoreMap
        if((e.getKeyCode() == KeyEvent.VK_ENTER && stageScore >= passScore && lives > 0 && twoMinute.getTimerTime() >=0) || e.getKeyCode() == KeyEvent.VK_DELETE){
        	stageScore = 0;
        	totalScore = totalScore + stageScore;
            stage = stage + 1;
            lives = lives + 2;
            twoMinute.moreTime();
            
            if (stage == 5) {
            	gridDimension = 5;
            }
            
            if (stage == 7) {
            	gridDimension = 6;
            }
            
            if(stage == 9) {
            	gridDimension = 7;
            }
            
            grid = new Grid(gridDimension);
            scoreMap = grid.getScoreMap();
            bombMap = grid.getBombMap();
            negateMap = GameManager.allFalseArray(gridDimension);
            passScore = GameManager.totalPoints(scoreMap);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            repaint();
        }
    }
    
    private void resetTime(boolean timer) {
    	if (timer) {
    		twoMinute.resetTimer(120);
    	}
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}