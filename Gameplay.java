package com.company;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author Michal Nawrot
 */

public class Gameplay extends JPanel implements KeyListener, ActionListener{

    private static int MAX_RAND = 3;
    private boolean play = false;
    private boolean isKeyRight;
    private boolean isKeyLeft;
    private boolean randomPlay = false;
    private int score = 0;
    private String playerName;

    private Integer totalBricks = 10;

    private Timer timer;
    private int delay = 10;

    private int playerX = 310;

    Random rand = new Random();
    private int r1;
    private int r2;
    private int randomnegative = rand.nextInt(1);
    private int ballposX = 350;
    private int ballposY = 525;
    private int ballXdir;
    private int ballYdir;

    private MapGenerator map;

    /**
     * Create InputDialog objects to take information from user
     * about speed,bricks,name.
     * Create bricks map
     * Start game global timer to generate frames.
     */
    public Gameplay() {
        if(JOptionPane.showConfirmDialog(null, "Random speed?(more points)") == 0) {
            randomPlay = true;
        }else{
            randomPlay = false;
        }

        try {
            totalBricks = Integer.parseInt(JOptionPane.showInputDialog(null, "How many bricks? (max 40)"));
            if(totalBricks <= 0){
                totalBricks = 1;
            }
        }catch(NumberFormatException e){
            totalBricks = 10;
        }

        try {
            playerName = JOptionPane.showInputDialog(null, "Type your name");
        }catch(NumberFormatException e){
            playerName = "Player 1";
        }

        map = new MapGenerator(totalBricks);

        addKeyListener(this);
        setFocusable(true);
        timer = new Timer(delay, this);
        timer.start();
    }
    /**
     * Paint map, window, score, player paddle, ball,
     * information about win and lose.
     */
    public void paint(Graphics g) {
        //tlo
        g.setColor(Color.lightGray);
        g.fillRect(1,1,1080, 720);

        //map
        map.draw((Graphics2D)g);

        //okno
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, 3, 720);
        g.fillRect(0, 0, 1080, 3);
        g.fillRect(1080, 0, 3, 723);
        g.fillRect(0, 720, 1080,3);

        //wynik
        g.setColor(Color.darkGray);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Scores:"+score, 510, 30);

        //paletka
        g.setColor(Color.darkGray);
        g.fillRect(playerX, 550, 100, 8);

        //pilka
        g.setColor(Color.magenta);
        g.fillOval(ballposX, ballposY, 20, 20);

        if(totalBricks == 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;

            g.setColor(Color.darkGray);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won, Score: " +score, 420, 300);
        }

        if(ballposY > 710) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;

            g.setColor(Color.darkGray);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Scores: " +score, 420, 300);
        }

        g.dispose(); //zwolnienie zasobów od grafiki
    }
    /**
     * Save score to file 'scores.txt'
     */
    public void saveScore(){
        try {
            final Path path = Paths.get("scores.txt");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime date = LocalDateTime.now();
            Files.write(path, Arrays.asList(dtf.format(date)+ " " + playerName + ": "+ score +"\n"), StandardCharsets.UTF_8,
                    Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
        } catch (final IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Score not updated");
        }
    }
    /**
     * Response to player actions.
     * Controls ball behaviour.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if(play) {
            if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100,8))){
                ballYdir = -ballYdir;
            }

            A: for(int i = 0; i<map.getMap().length; i++) {
                for(int j=0; j<map.getMap()[0].length; j++) {
                    if(map.getMap()[i][j] > 0) {
                        int brickX = j*map.getBrickWidth() + 80;
                        int brickY = i*map.getBrickHeight() +50;
                        int brickWidth = map.getBrickWidth();
                        int brickHeight = map.getBrickHeight();

                        Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);

                        if(ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            if(randomPlay){
                                score += 5*3;
                            }else {
                                score += 5;
                            }
                            if(ballposX + 15 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
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
                if(playerX >= 1080-100) {
                    playerX = 1080-100;
                }else {
                    moveRight();
                }
            }else if (isKeyLeft) {
                if(playerX < 10) {
                    playerX = 4;
                }else {
                    moveLeft();
                }
            }

            ballposX += ballXdir;

            ballposY += ballYdir;

            if(ballposX < 3)
            {
                ballXdir = -ballXdir;
            }
            if(ballposY < 3)
            {
                ballYdir = -ballYdir;
            }
            if(ballposX > 1060)
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
    public void keyReleased(KeyEvent e) {				//wywolywane, gdy puscimy przycisk
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            isKeyRight = false;
        }else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            isKeyLeft = false;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {				//wywolywane, gdy wcisniety przycisk
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            isKeyRight = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            isKeyLeft = true;
        }

        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            if(!play) {
                play = true;

                r1 = rand.nextInt(MAX_RAND +2);
                r2 = rand.nextInt(MAX_RAND +2);
                randomnegative = rand.nextInt(1);
                //ballposX = 350;
                //ballposY = 525;
                if(randomPlay) {
                    if (randomnegative == 1) { //jesli 1 to w prawo
                        if (r1 == 0) {
                            r1++;
                        }
                        ballXdir = r1;
                    } else {                   //jesli 0 to w lewo
                        if (r1 == 0) {
                            r1--;
                        }
                        ballXdir = r1;
                    }
                    //-1 na wypadek r2=0
                    ballYdir = -r2-1;
                }else{
                    ballXdir = 2;
                    ballYdir = 5;
                }

                //playerX = 310;
                //score = 0;
            }
        }
    }
    /**
     * Move paddle to the right.
     */
    public void moveRight() {
        play = true;
        playerX+=5;
    }

    /**
     * Move paddle to the left.
     */
    public void moveLeft() {
        play = true;
        playerX-=5;
    }

}