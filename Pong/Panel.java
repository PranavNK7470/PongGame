package Pong;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
public class Panel extends JPanel implements ActionListener{
    
    int Screen_width = 600;
    int Screen_height = 600;
    boolean running;
    int delay = 25;
    Timer timer;
    static int bladeLen = 100;
    static int bladeWid = 30;
    static int spacing = 20;
    boolean up = false;
    boolean down = false;
    int leftScore = 0;
    int rightScore = 0;
    int leftBladex;
    int leftBladey;
    int rightBladex;
    int rightBladey;
    int ballx;
    int bally;
    int ballVelx;
    int ballVely;
    int i = 0;
    Random r = new Random();
    boolean gameover = false;

    Panel() {
        this.setPreferredSize(new Dimension(Screen_width , Screen_height));
        this.setBackground(Color.white);
        this.setFocusable(true);
        this.addKeyListener(new keyadapter());
        startGame();
    }

    void startGame() {
        newBlade();
        newBall();
        timer = new Timer(delay,this);
        timer.start();
    }

    public void paint(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    void newBall() {
        this.ballx = (Screen_width / 2) - 10;
        this.bally = (Screen_height / 2) - 10;
        if (this.i % 2 == 0 ) {
            this.ballVelx = 5;
            int num = (r.nextInt(1,11)  < 5) ? 1 : -1;
            this.ballVely = 7*num;    
        } else {
            this.ballVelx = -5;
            int num = (r.nextInt(1,11)  < 5) ? 1 : -1;
            this.ballVely = 7*num;
        }
    }

    void newBlade() {
        // top left corner of the blades.
        rightBladex = 550;
        rightBladey = 250;
        leftBladex = 20;
        leftBladey = 250;
    }

    void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if(!gameover) {
            g2.setColor(Color.black);
            // draw net
            BasicStroke stroke = new BasicStroke(5);
            g2.setStroke(stroke);
            g2.drawLine(Screen_width/2, 0,Screen_width/2, Screen_height);
            //show scores
            g2.setFont( new Font("Times New Roman",Font.BOLD, 25));
		    FontMetrics metrics2 = getFontMetrics(g.getFont());
		    g2.drawString(Integer.toString(leftScore), (Screen_width/2 - metrics2.stringWidth("You Suck"))/2, 50);
            g2.drawString(Integer.toString(rightScore), Screen_width-(Screen_width/2 - metrics2.stringWidth("You Suck"))/2, 50);
            //draw ball
            g2.fillOval(ballx,bally,10,10); 
            //draw blades
            g2.fillRect(rightBladex, rightBladey, bladeWid, bladeLen);
            g2.fillRect(leftBladex, leftBladey, bladeWid , bladeLen);
        } else {
            String winner = (leftScore > rightScore) ? "Left Wins!" : "Right Wins!";
            g2.setColor(Color.black); 
		    g2.setFont( new Font("Times New Roman",Font.BOLD, 75));
		    FontMetrics metrics2 = getFontMetrics(g.getFont());
		    g2.drawString(winner, (Screen_width - metrics2.stringWidth(winner))/2, Screen_height/2);
        }
    }

    boolean miss() {
        if(ballx >= Screen_width) {
            leftScore++;
            return true; 
        } else if(ballx <= 0) {
            rightScore++;
            return true;
        }
        return false;
    }
    // physics of the game
    void checkWalls() {
        //left, up, down walls.
        if(bally <= 0 || bally >= Screen_height) {
            ballVely = ballVely*(-1);
        }
    }

    void checkHit() {
        if(ballx == 560 && (rightBladey+5 <= bally) && (bally<= rightBladey+bladeLen-5)) {
            // like right wall
            ballx = 550;
            ballVelx = ballVelx*(-1);
        } 
        if(ballx == 50 && (leftBladey+5 <= bally) && (bally <= leftBladey+bladeLen-5)) {
            ballx = 50;
            ballVelx = ballVelx*(-1);
        }
    }

    void moveBall() {
        ballx += ballVelx;
        bally += ballVely;
    }

    void moveLeftBlade() {
        leftBladey = bally - bladeLen / 2;
    }

    void moveRightBladeDown() {
        rightBladey +=  20;
    }

    void moveRightBladeUp() {
        rightBladey -=  20;
    }

    void restart() {
        if(leftScore < 10 && rightScore< 10) {
            newBall();
            newBlade(); 
        } else {
            gameover = true;
        }
    }

   public void actionPerformed(ActionEvent e) {
        if(!miss()) {
            moveBall();
            moveLeftBlade();
            if(up) {
                moveRightBladeUp();
                up = false;
            } else if(down) {
                moveRightBladeDown();
                down = false;
            }
            checkHit();
            checkWalls();

        } else {
            restart();
        }
        repaint();
   }

  public class keyadapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){   
                case 38:
                    if(rightBladey >= 0)
                        up = true;
                    break;   
                case 40:
                    if(rightBladey <= (Screen_height-bladeLen))
                        down = true;
                    break;  
                // w       
                case 87:
                    if(leftBladey >= 0) 
                        leftBladey -= 20;
                    break;
                //s
                case 83:
                    if(leftBladey <= (Screen_height-bladeLen))    
                        leftBladey += 20;
                    break;        
            }
        }

    }
}