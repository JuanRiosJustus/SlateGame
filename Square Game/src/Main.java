import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {
	
    private JFrame mainFrame = new JFrame("Slate");
    private JFrame gameFrame = new JFrame("In-Game");
    private JFrame tutFrame = new JFrame("Tutorial");
    private JFrame settingsFrame = new JFrame("Settings");
    
    private static boolean startTimer = false;
    
    private JPanel centerPanel = new JPanel();
    
    private AudioPlayer bGM = new AudioPlayer("src/gameAssets/sounds/bgm.wav");
    
    private JButton newGameButton, howToButton, settingsButton;
    
    private Game gameObject = new Game();
    private Tutorial tutorial = new Tutorial();
    
    

    private BufferedImage image;
    public Main() {
    	try {
    		image = ImageIO.read(new File("src/gameAssets/essentials/start.png"));
    	} catch (IOException ex) {
    		ex.printStackTrace();
    	}
    }
    
    
    public void main() {
    	
    	bGM.playSound();
    	ProjectManager.loadNumbers();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setBounds(10,10, ProjectManager.screenSizeWidth(), ProjectManager.screenSizeHeight());
        mainFrame.setLocationRelativeTo(null);
        
        gameFrame.setResizable(false);
        gameFrame.setVisible(false);
        gameFrame.setBounds(10,10, ProjectManager.screenSizeWidth(), ProjectManager.screenSizeHeight());
        gameFrame.setLocationRelativeTo(mainFrame);
        gameFrame.getContentPane().add(gameObject);
        gameFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                    mainFrame.setVisible(true);
            }
        });
        
        tutFrame.setResizable(false);
        tutFrame.setVisible(false);
        tutFrame.setBounds(10,10, ProjectManager.screenSizeWidth(), ProjectManager.screenSizeHeight());
        tutFrame.setLocationRelativeTo(mainFrame);
        tutFrame.getContentPane().add(tutorial);
        tutFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                    mainFrame.setVisible(true);
            }
        });
        
        settingsFrame.setResizable(false);
        settingsFrame.setVisible(false);
        settingsFrame.setBounds(10,10, ProjectManager.screenSizeWidth(), ProjectManager.screenSizeHeight());
        settingsFrame.setLocationRelativeTo(mainFrame);
        //settingsFrame.getContentPane().add(null, BorderLayout.CENTER);
        settingsFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                    mainFrame.setVisible(true);
            }
        });
        
        newGameButton = new JButton("Start Game");
        newGameButton.setBackground(Color.WHITE);
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    	mainFrame.setVisible(false);
                    	gameFrame.setVisible(true);
                    	startTimer = true;
                    	
                    }
                });
        
        howToButton = new JButton("How To Play");
        howToButton.setBackground(Color.WHITE);
        howToButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        howToButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    	mainFrame.setVisible(false);
                    	tutFrame.setVisible(true);
                    }
                });
        
        settingsButton = new JButton("Settings");
        settingsButton.setEnabled(false);
        settingsButton.setBackground(Color.WHITE);
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // TODO
                    	ProjectManager.projectProps();
                    }
                });
        
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(Box.createRigidArea(new Dimension(100,ProjectManager.screenSizeHeight()/3)));
        centerPanel.add(newGameButton);
        centerPanel.add(Box.createRigidArea(new Dimension(100,50)));
        centerPanel.add(howToButton);
        centerPanel.add(Box.createRigidArea(new Dimension(100,50)));
        centerPanel.add(settingsButton);
        centerPanel.add(Box.createRigidArea(new Dimension(100,50)));
        
        mainFrame.setContentPane(new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 0,0, this);
			}
        });
        mainFrame.getContentPane().add(centerPanel, BorderLayout.CENTER);
        mainFrame.setVisible(true);
    }
    
    public static boolean timerOn() {
    	return startTimer;
    }
}
