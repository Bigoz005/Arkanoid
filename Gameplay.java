package Arkanoid;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener{

	private boolean play = false;
	private boolean isKeyRight;
	private boolean isKeyLeft;
	private int score = 0;
	
	private int totalBricks = 21;
	
	private Timer timer;
	private int delay = 5;
	
	private int playerX = 310;
	
	private int ballposX = 350;
	private int ballposY = 350;
	private int ballXdir = -3;	//	stworzyc losowe liczby ujemne <-4,-1>
	private int ballYdir = -3;	//	stworzyc losowe liczby ujemne <-4,-1>
	
	private MapGenerator map;
	
	public Gameplay() {
		map = new MapGenerator(3,7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}
	
	public void paint(Graphics g) {
		//background
		g.setColor(Color.lightGray);
		g.fillRect(1,1,692, 592);
		
		//map
		map.draw((Graphics2D)g);
		
		//borders
		g.setColor(Color.black);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		//scores
		g.setColor(Color.darkGray);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("Scores:"+score, 560, 30);
		
		//the paddle
		g.setColor(Color.darkGray);
		g.fillRect(playerX, 550, 100, 8);
		
		// the ball
		g.setColor(Color.magenta);
		g.fillOval(ballposX, ballposY, 20, 20);	
		
		/*
		if(play == false) {
		g.setColor(Color.darkGray);
		g.setFont(new Font("serif", Font.BOLD, 40));
		g.drawString("Press Enter to start", 190, 280);
		}
		*/
		
		if(totalBricks <= 0) {
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.darkGray);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("You Won, Score: " +score, 190, 300);
		
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 230, 350);
		}
		
		if(ballposY > 570) {
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.darkGray);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Game Over, Scores: " +score, 190, 300);
		
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 230, 350);
		}
		
		g.dispose();
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		if(play) {
			if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100,8))){
				ballYdir = -ballYdir;
			}
			
			A: for(int i = 0; i<map.map.length; i++) {
				for(int j=0; j<map.map[0].length; j++) {
					if(map.map[i][j] > 0) {
						int brickX = j*map.brickWidth + 80;
						int brickY = i*map.brickHeight +50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
					
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect)) {
							map.setBrickValue(0, i, j);
							totalBricks--;
							score += 5;
							
							if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
								ballXdir = - ballXdir;
							} else {
								ballYdir = -ballYdir;
							}
							
							break A;
						}
					}
				}
			}
			
			if (isKeyRight) {
				if(playerX >=600) {
					playerX = 600;
				}else {
					moveRight();
				}
			}else if (isKeyLeft) {
				if(playerX < 10) {
					playerX = 10;
				}else {
					moveLeft();
				}
			}
			
			ballposX += ballXdir;
			ballposY += ballYdir;
			if(ballposX < 0)
			{
				ballXdir = -ballXdir;
			}
			if(ballposY < 0)
			{
				ballYdir = -ballYdir;
			}
			if(ballposX > 670)
			{
				ballXdir = -ballXdir;
			}
		}
		
		repaint();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			isKeyRight = false;
		}else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			isKeyLeft = false;
		}
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			 isKeyRight = true;
			}
			if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				isKeyLeft = true;
		 	}
		 	
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
		 if(!play) {
			 play = true;
			 ballposX = 350;
			 ballposY = 350;
			 ballXdir = -1;
			 ballYdir = -2;
			 playerX = 310;
			 score = 0;
			 totalBricks = 21;
			 map = new MapGenerator(3, 7);
			 
			 repaint();
		 }
		}
	}
	
	public void moveRight() {
		play = true;
		playerX+=5;
	}
	
	public void moveLeft() {
		play = true;
		playerX-=5;
	}
	
}
