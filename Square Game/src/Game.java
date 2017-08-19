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
import java.util.Random;

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
    
    private Random randomizer = new Random();
    
    private int stageScore = 0;
    private int totalScore = 0;
    
    private boolean peekButtonActive;
    private boolean peekMode;
    private int amountOfPeeks = 0;
    private int stage = 1;
    private int lives = 3;
    private int gridDimension = 4;
    private int time = 150;
    private int timeBonus = 10;
    
    private boolean reset = false;
    private boolean started = true;
    
    private Timer timer;
    private Grid grid;
    private AudioPlayer soundX;
    private AudioPlayer soundO;
    private AudioPlayer noContinue;
    private AudioPlayer peeking;
    
    private int stagePosition;
    private int livesPosition;
    private int scorePosition;
    private int timerPosition;
    
    private int[][] scoreMap;
    private boolean[][] negateMap = GameManager.allFalseArray(gridDimension);
    private boolean[][] bombMap;
    private BufferedImage boxes;
    private BufferedImage xBar;
    private BufferedImage yBar;
    private BufferedImage o;
    private BufferedImage x;
    private BufferedImage statusBar;
    private BufferedImage peekPic;
    private BufferedImage peekPicOff;
    
    private int arrayPositionX;
    private int arrayPositionY;
    
    private int passScore;
    private GameTime gameTimer;
    private Color backgroundColor = new Color(207,207,207);

    @SuppressWarnings("unused")
    private boolean filesFound = true;
    
    private int bombAmountX;
    private int pointsAvailableX;
    private int bombAmountY;
    private int pointsAvailableY;
    
    public Game() {
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
        gameTimer = new GameTime(time);
        soundX = new AudioPlayer("src/gameAssets/sounds/xDrop.wav");
        soundO = new AudioPlayer("src/gameAssets/sounds/oDrop.wav");
        noContinue = new AudioPlayer("src/gameAssets/sounds/noContinue.wav");
        peeking = new AudioPlayer("src/gameAssets/sounds/peeking.wav");
        
        try {
        	statusBar = ImageIO.read(new File("src/gameAssets/essentials/statusBar.png"));
        	boxes = ImageIO.read(new File("src/gameAssets/essentials/boxes.png"));
        	xBar = ImageIO.read(new File("src/gameAssets/essentials/xBar.png"));
        	yBar = ImageIO.read(new File("src/gameAssets/essentials/yBar.png"));
        	o = ImageIO.read(new File("src/gameAssets/essentials/o.png"));
        	x = ImageIO.read(new File("src/gameAssets/essentials/x.png"));
        	peekPic = ImageIO.read(new File("src/gameAssets/essentials/peekPic.png"));
        	peekPicOff = ImageIO.read(new File("src/gameAssets/essentials/peekPicOff.png"));
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
        	
            
        	// Coloring over the white boxes
            if (peekMode == true) {
                for (int rowIndex = 0; rowIndex < gridDimension; rowIndex = rowIndex + 1) {
                	for (int columnIndex = 0; columnIndex < gridDimension; columnIndex = columnIndex + 1) {
                		g.setColor(new Color(randomizer.nextInt(155) + 100, randomizer.nextInt(155) + 100, randomizer.nextInt(155) + 100));
                           // The Boxes
                         g.fillRect(25 + (rowIndex * 80), 25 + (columnIndex * 80) , 48, 48);
                        }
                    }
            }

            // The right Info Bar
            //g.fillRect(577, 25, 59, 525);
            // The bottom Info Bar
            //g.fillRect(27, 573, 592, 34);
            
        	
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
            g.drawImage(statusBar, 640, 10, this); // the status bar, 660, 10
            
            g.setFont(new Font("serif", Font.BOLD, 25));
           // g.drawString("" + stage, 850, 70);
            stagePosition = 45;
            if (9 >= stage) {
            	g.drawImage(ProjectManager.numberToImage(stage), 870, stagePosition, this);
            } else if (99 >= stage && stage > 9) {
            	StringBuilder string = new StringBuilder();
            	string.append("" + stage);
            	char[] integers = string.toString().toCharArray();
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 870, stagePosition, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 890, stagePosition, this);
            } else if (999 >= stage && stage >99) {
            	StringBuilder string = new StringBuilder();
            	string.append("" + stage);
            	char[] integers = string.toString().toCharArray();
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 870, stagePosition, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 890, stagePosition, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[2])), 910, stagePosition, this);
            } else if (9999 >= stage && stage > 999) {
            	StringBuilder string = new StringBuilder();
            	string.append("" + stage);
            	char[] integers = string.toString().toCharArray();
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 870, stagePosition, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 890, stagePosition, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[2])), 910, stagePosition, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[3])), 930, stagePosition, this);
            }
            
            // drawing the lives
            livesPosition = 140;
            //g.drawString("" +lives, 850, 200); // lives 870, 200
            if (9 >= lives ) {
            	g.drawImage(ProjectManager.numberToImage(lives), 870, livesPosition, this);
            } else if (99 >= lives && lives > 9) {
            	StringBuilder string = new StringBuilder();
            	string.append("" + lives);
            	char[] integers = string.toString().toCharArray();
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 870, livesPosition, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 890, livesPosition, this);
            } else if (999 >= lives && lives >99) {
            	StringBuilder string = new StringBuilder();
            	string.append("" + lives);
            	char[] integers = string.toString().toCharArray();
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 870, livesPosition, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 890, livesPosition, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[2])), 910, livesPosition, this);
            } else if (9999 >= lives && lives > 999) {
            	StringBuilder string = new StringBuilder();
            	string.append("" + lives);
            	char[] integers = string.toString().toCharArray();
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 870, livesPosition, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 890, livesPosition, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[2])), 910, livesPosition, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[3])), 930, livesPosition, this);
            } else {
            	
            }
            
            // drawing the score
            scorePosition = 235;
            if (9 >= stageScore ) {
            	g.drawImage(ProjectManager.numberToImage(stageScore), 870, scorePosition, this);
            } else if (99 >= stageScore && stageScore > 9) {
            	StringBuilder string = new StringBuilder();
            	string.append("" + stageScore);
            	char[] integers = string.toString().toCharArray();
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 870, scorePosition, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 890, scorePosition, this);
            } else if (999 >= stageScore && stageScore > 99) {
            	StringBuilder string = new StringBuilder();
            	string.append("" + stageScore);
            	char[] integers = string.toString().toCharArray();
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 870, scorePosition, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 890, scorePosition, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[2])), 910, scorePosition, this);
            } else if (9999 >= stageScore && stageScore > 999) {
            	StringBuilder string = new StringBuilder();
            	string.append("" + stageScore);
            	char[] integers = string.toString().toCharArray();
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 870, scorePosition, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 890, scorePosition, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[2])), 910, scorePosition, this);
            	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[3])), 930, scorePosition, this);
            }
            
            // The timer
            timerPosition = 335;
            if(gameTimer.getTime() >= 0) {
            	if (9999 >= gameTimer.getTime() && gameTimer.getTime() >999) {
                	StringBuilder string = new StringBuilder();
                	string.append("" + gameTimer.getTime());
                	char[] integers = string.toString().toCharArray();
                	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 870, timerPosition, this);
                	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 890, timerPosition, this);
                	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[2])), 910, timerPosition, this);
                	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[3])), 930, timerPosition, this);
            	} else if (999 >= gameTimer.getTime() && gameTimer.getTime() > 99) {
                	StringBuilder string = new StringBuilder();
                	string.append("" + gameTimer.getTime());
                	char[] integers = string.toString().toCharArray();
                	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 870, timerPosition, this);
                	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 890, timerPosition, this);
                	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[2])), 910, timerPosition, this);
                } else if (99 >= gameTimer.getTime() && gameTimer.getTime() > 9) {
                	StringBuilder string = new StringBuilder();
                	string.append("" + gameTimer.getTime());
                	char[] integers = string.toString().toCharArray();
                	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[0])), 870, timerPosition, this);
                	g.drawImage(ProjectManager.numberToImage(Character.getNumericValue(integers[1])), 890, timerPosition, this);
                } else if (gameTimer.getTime() <= 9) {
                	g.drawImage(ProjectManager.numberToImage(gameTimer.getTime()), 870, timerPosition, this);
                }
            } else {
            	g.drawImage(ProjectManager.numberToImage(0), 870, timerPosition, this);
            }
            
            //amount of peeks
            if ((3 >= amountOfPeeks && amountOfPeeks >= 0)) {
            	g.drawImage(ProjectManager.numberToImage(amountOfPeeks), 870, 430, this);
            	if (peekButtonActive) {
            		g.drawImage(peekPic, 930, 440, this);
            	} else {
            		g.drawImage(peekPicOff, 930, 440, this);
            	}
            	
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
        
        if (lives == 0 || reset == true || gameTimer.getTime() == 0 ) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Serif", Font.BOLD, 15));
            g.drawString("Press Enter to restart the game", 720,  565);
        }
        
        if (stageScore >= passScore && lives > 0) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Serif", Font.BOLD, 15));
            g.drawString("Press Enter to advance to next stage", 720,  565);
        }
        
        g.dispose();
    }

    public void mousePressed(MouseEvent e) {
        
    	//System.out.println("The are a total of " + GameManager.totalBombs(bombMap) + " bombs" );
    	
    	System.out.println("X value is " + e.getXOnScreen() + " Y Value is :  " + e.getYOnScreen());
    	
    	//Allow user to click on bottom most box to advance to next stage
    	if (e.getX() >= 675 && e.getX() <= 1180 && e.getY() >= 505 && e.getY() <= 600) {
    		System.out.println("Yoo");
    		if (stageScore >= passScore && lives > 0) {
            	this.win();
    		}
    		if (lives == 0 || reset == true || gameTimer.getTime() == 0) {
                this.lose();
    		}
    	}
    	
    	// Variables initialize array search at the beginning index
        boolean bombFound = false;
        arrayPositionX = 0;
        arrayPositionY = 0;
        
        // allows user to hit the button to find a bomb location
        // Also known as peeking
        if(e.getX() >= 930 && e.getX() <= 959 && e.getY() >= 440 && e.getY() <=469 && amountOfPeeks <= 3 && peekButtonActive && GameManager.totalBombs(bombMap) >= amountOfPeeks) {
        	peekMode = true;
        	System.out.println("You hit the peek!");
        	peeking.playSound();
     		while (peekButtonActive == true) {
     			int x = randomizer.nextInt(gridDimension);
     			int y = randomizer.nextInt(gridDimension);
    			if (bombMap[y][x] == true && negateMap[y][x] == false) {
    				negateMap[y][x] = true;
    				System.out.println(y + " , " + x + " is a bomb");
    				amountOfPeeks = amountOfPeeks - 1;
    				if(amountOfPeeks == 0) {
    					peekButtonActive = false;
    				}
    			}
    		}
     		peekMode = false;
        }
        
        GTG:
        	if (lives > 0 && gameTimer.getTime() >= 0) {
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
                	//peekMode = false;
                }
        	} else {
        		noContinue.playSound();
        		reset = true;
        	}   
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
    	
    	//What happens when the user loses the game.
        if((e.getKeyCode() == KeyEvent.VK_ENTER && (lives == 0 || reset == true))){    
        	this.lose();
        }
        
        // What happens the user has an adequate amount of points to advance to the next stage
        // adequate score if based on the amount of points available on scoreMap
        if((e.getKeyCode() == KeyEvent.VK_ENTER && stageScore >= passScore && lives > 0 && gameTimer.getTime() >=0) || 
        	   e.getKeyCode() == KeyEvent.VK_DELETE){
        	this.win();
        }
    }
    
    private void win() {
    	stageScore = 0;
    	totalScore = totalScore + stageScore;
        stage = stage + 1;
        lives = lives + 2;
        gameTimer.moreTime(timeBonus);
        
        if (stage == 5) {
        	gridDimension = 5;
        	timeBonus = 15;
        }
        
        if (stage == 7) {
        	gridDimension = 6;
        	timeBonus = 25;
        }
        
        if(stage == 9) {
        	gridDimension = 7;
        	timeBonus = 40;
        }
        
        if(stage % 3 == 0 && amountOfPeeks < 3) {
        	amountOfPeeks = amountOfPeeks + 1;
        	peekButtonActive = true;
        	System.out.println("You got a peek");
        }
        
        grid = new Grid(gridDimension);
        scoreMap = grid.getScoreMap();
        bombMap = grid.getBombMap();
        negateMap = GameManager.allFalseArray(gridDimension);
        passScore = GameManager.totalPoints(scoreMap);
    }
    
    private void lose() {
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
        gameTimer.resetTimer(time);
        reset = false;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            repaint();
        }
    }
    
    private void resetTime(boolean timer) {
    	if (timer) {
    		gameTimer.resetTimer(time);
    	}
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}