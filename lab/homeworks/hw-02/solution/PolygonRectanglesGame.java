/*
 * UQU - CS - Computer Graphics - spring 2020
 * pr. Murtaza Khan
 * subject: assignment-02: reclangles polygon game
 * authors:
 * - MHD Maher Azkoul
 *   438017578
 * 
 * program description:
 *  this code runs an animation of rectangles that collide with
 * the frame borders and collide with other rectangles borders.
 * With polygon to (kill) stop thier movement.
 * The speed of rectangles will increase with every collision.
 * If the rectangle reached a specific speed, it (wins) stop with green color
 */


import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.Random;

// This class is responsible for run the program
// it will creates the applet and panel objects
public class PolygonRectanglesGame extends JApplet {

    public static void main(String s[]) {
        JFrame frame = new JFrame();
        frame.setTitle("Rectangle Animation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JApplet applet = new PolygonRectanglesGame();
        applet.init();
        frame.getContentPane().add(applet);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void init() {
        JPanel panel = new PanelPolygonRectanglesGame();
        getContentPane().add(panel);
    }
}

// This is the panel class that controls the redrawing process 
class PanelPolygonRectanglesGame extends JPanel implements Runnable {

    String msg = "Use Arrow Keys to Move & D, A to rotate";
    private static final int OFFSET = 50;

    private static final int FW = 800;
    private static final int FH = 400;
    private static final int NUMBER_OF_RECS = 7;
    private static final int WIN_SPEED = 7;

    // (x,y) coordinate of points to be use in polygon	
    private static final int PW = 60;  //polygon-width
    private static final int PH = 100; //polygon-height
    private static final int POINTS_NO = 15; //number of points
    int x0 = 150;
    int y0 = FH - 150;
    int x1 = x0 + PW;
    int y1 = y0;
    int x2 = x0 + (x1 - x0) / 2;
    int y2 = y0 - PH;

    int xPoly[] = {x0, x1, x2}; // initialize array
    int yPoly[] = {y0, y1, y2};

    int pointsX[] = new int[POINTS_NO];
    int pointsY[] = new int[POINTS_NO];
    int pointsPointer = 0;

    int winCounter = 0;
    int looseCounter = 0;

    int tx = 10;     // tranlsation distances along x-axis
    int ty = tx;     // tranlsation distances along y-axis
    int theta = 10;   // rotation angle

    private int frameSpeed = 20;
    
    ArrayList<MyRectangle> rectangles;

    Thread mythread;
    
    public PanelPolygonRectanglesGame() {
        setPreferredSize(new Dimension(FW, FH));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);

        this.initRectangles();

        KeyPressListener listenerKey = new KeyPressListener();
        addKeyListener(listenerKey);

        this.mythread = new Thread(this);
        mythread.start();
    }

    @Override
    public void run() {
        while (true) {
        	try{
        		Thread.sleep(frameSpeed);
        	}
            catch(InterruptedException e){}

            for(MyRectangle myrec: this.rectangles) {

                if(myrec.won() || myrec.killed()) {
                    continue;
                }

                // this loop will check if the current rectangle
                // collide with other rectangle
                for(MyRectangle other : this.rectangles) {
                    if(myrec == other) {
                        continue;
                    }

                    if(other.won() || other.killed()) {
                        continue;
                    }

                    // the folowing condition will check 
                    // if the current rectangle collides with other rectagles
                    // if it does, then both colliding rectangles
                    // will go in the other direction and change thier color to a random color
                    if(myrec.intersects(other)) {
                        myrec.changeDirectionY();
                        myrec.changeDirectionX();
                        myrec.setRandomColor();
                        myrec.changeSpeed(1.1);
                        myrec.updatePosition();
                        other.changeDirectionY();
                        other.changeDirectionX();
                        other.setRandomColor();
                        other.changeSpeed(1.1);
                        other.updatePosition();
                    }
                }

                // if the rectangle collides with polygon track line
                for(int i = 0; i < POINTS_NO; i++) {
                    if(myrec.contains(pointsX[i], pointsY[i])) {
                        myrec.kill();
                        this.winCounter++;
                    }
                }

                // if rectangle reaches the win speed
                if(myrec.getXDirection() > WIN_SPEED || myrec.getYDirection() > WIN_SPEED) {
                    myrec.win();
                    this.looseCounter++;
                }

                // these two conditions with check if the rectangle collides with 
                // frame borders
                if(myrec.intersectsWithFrameX(this.getHeight(), OFFSET, 0)) {
                    myrec.changeDirectionY();
                }

                if(myrec.intersectsWithFrameY(this.getWidth())) {
                    myrec.changeDirectionX();
                }

                myrec.updatePosition();
            }
            
        	repaint();
        }  
    }

        // inner class to handle keyboard events
        private class KeyPressListener extends KeyAdapter {

            private ArrayList<Integer> keys = new ArrayList<Integer>();
    
            public synchronized void keyReleased(KeyEvent e) {
                keys.remove((Object) e.getKeyCode());
            }
    
            public synchronized void keyPressed(KeyEvent e) {
                if(!keys.contains(e.getKeyCode())) {
                    keys.add(e.getKeyCode());
                }
                updatePolygon();
                repaint();
            }
    
            public void updatePolygon() {
                if(keys.size() < 1) {
                    return;
                }
                for(Integer keyCode : keys) {
                    if (xPoly != null && yPoly != null) {
                        /* Translation by pressing arrow keys*/
                        if (keyCode == KeyEvent.VK_UP) {
                            moveUp();
                        }
                        if (keyCode == KeyEvent.VK_DOWN) {
                            moveDown();
                        }
                        if (keyCode == KeyEvent.VK_LEFT) {
                            moveLeft();
                        }
                        if (keyCode == KeyEvent.VK_RIGHT) {
                            moveRight();
                        }
                        if (keyCode == 'd' || keyCode == 'D') {
                            rotateCW();
                        }
                        if (keyCode == 'a' || keyCode == 'A') {
                            rotateCCW();
                        }
                    }
                }
        
                if(pointsX != null && pointsY != null) {
                    if(pointsPointer == POINTS_NO) {
                        pointsPointer = 0;
                    }
                    pointsX[pointsPointer] = xPoly[2];
                    pointsY[pointsPointer] = yPoly[2];
                    pointsPointer++;
                }
            }
        }
    
        public void moveUp() {
            for (int i = 0; i < yPoly.length; i++) {
                yPoly[i] = yPoly[i] - ty;
            }
        }
    
        public void moveDown() {
            for (int i = 0; i < yPoly.length; i++) {
                yPoly[i] = yPoly[i] + ty;
            }
        }
    
        public void moveRight() {
            for (int i = 0; i < xPoly.length; i++) {
                xPoly[i] = xPoly[i] + tx;
            }
        }
    
        public void moveLeft() {
            for (int i = 0; i < xPoly.length; i++) {
                xPoly[i] = xPoly[i] - tx;
            }
        }
    
        
        public void rotateCW() {
            double rad = Math.toRadians(-theta);
            for (int i = 0; i < xPoly.length; i++) {
                int originX = xPoly[2];
                int originY = (yPoly[1] + yPoly[2])/2;
                double x1 = xPoly[i] - originX;
                double y1 = yPoly[i] - originY;
                x1 = x1 * Math.cos(rad) - y1 * Math.sin(rad);
                y1 = x1 * Math.sin(rad) + y1 * Math.cos(rad);
                xPoly[i] = (int)Math.ceil(x1) + originX;
                yPoly[i] = (int)Math.ceil(y1) + originY;
            }
        }
    
        public void rotateCCW() {
            double rad = Math.toRadians(theta);
            for (int i = 0; i < xPoly.length; i++) {
                int originX = xPoly[2];
                int originY = (yPoly[1] + yPoly[2])/2;
                double x1 = xPoly[i] - originX;
                double y1 = yPoly[i] - originY;
                x1 = x1 * Math.cos(rad) - y1 * Math.sin(rad);
                y1 = x1 * Math.sin(rad) + y1 * Math.cos(rad);
                xPoly[i] = (int)Math.floor(x1) + originX;
                yPoly[i] = (int)Math.floor(y1) + originY;
            }
        }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;        

		for(MyRectangle myrec : this.rectangles) {
            g2.setColor(myrec.getColor());	
            Rectangle.Double rec = myrec.redrawAndGetRectangle();
            g2.fill(rec);
        }

        g2.setStroke(new BasicStroke(5.0f));

        g2.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString(msg + getScoreMsg(winCounter, looseCounter), 40, 40);
        g2.drawLine(this.getX(), this.getY()+OFFSET, this.getWidth(), this.getY()+OFFSET);

        Polygon poly = new Polygon(xPoly, yPoly, xPoly.length);
        Shape polyShape = poly;
        g2.setColor(Color.green);
        g2.fill(polyShape);

        g2.setColor(Color.WHITE);
        for(int i = 0; i < POINTS_NO; i++) {
            if(pointsX[i] == 0 && pointsY[i] == 0) {
                continue;
            }
            g2.drawLine(pointsX[i], pointsY[i], pointsX[i], pointsY[i]);
        }
    }

    // this method will run in the constructor to
    // initialize the array of rectangles
    private void initRectangles() {
        this.rectangles = new ArrayList<MyRectangle>();
        for(int i = 0; i < NUMBER_OF_RECS; i++) {
            MyRectangle rec = new MyRectangle(FW, FH, OFFSET);
            this.rectangles.add(rec);
        }
    }

    private String getScoreMsg(int win, int loose) {
        return " | win: " + win + ", loose: " + loose;
    }
}

// This class encapsulates the custom state and behavior
// of my rectange with the actual rectangle object 
class MyRectangle {

    private static double RW = 60, RH = 40;
    private double x, y;
    private double stepX, stepY;
    private Color color;
    private Rectangle2D.Double rec;
    private boolean won = false;
    private boolean killed = false;


    public MyRectangle(int FW, int FH) {
        this.setRandomPosition(FW, FH);
        this.setSteps();
        this.setRandomColor();
        this.rec = new Rectangle2D.Double(x, y, RW, RH);
    }

    public MyRectangle(int FW, int FH, int TOP_OFFSET) {
        this.setRandomPosition(FW, FH, TOP_OFFSET);
        this.setSteps();
        this.setRandomColor();
        this.rec = new Rectangle2D.Double(x, y, RW, RH);
    }

    public MyRectangle(double x, double y) {
        this.x = x;
        this.y = y;
        this.setSteps();
        this.setRandomColor();
        this.rec = new Rectangle2D.Double(this.x, this.y, RW, RH);
    }

    public Color getColor() {
        return this.color;
    }

    public double getAX() {
        return this.x;
    }
    public double getBX() {
        return this.x+this.RW;
    }
    public double getAY() {
        return this.y;
    }
    public double getCY() {
        return this.y+this.RH;
    }

    public double getWidth() {
        return this.RW;
    }

    public double getHeight() {
        return this.RH;
    }

    public double getYDirection() {
        return this.stepY;
    }

    public double getXDirection() {
        return this.stepX;
    }

    public Rectangle.Double redrawAndGetRectangle() {
        this.rec = new Rectangle2D.Double(x, y, RW, RH);
        return this.rec;
    }

    public void stop() {
        this.stepX = 0;
        this.stepY = 0;
    }

    public void changeDirectionX() {
        this.stepX *= -1;
    }

    public void changeDirectionY() {
        this.stepY *= -1;
    }

    public void updatePosition() {
        this.x += this.stepX;
        this.y += this.stepY;
    }

    public void updatePositionX() {
        this.x += this.stepX;
    }

    public void updatePositionY() {
        this.y += this.stepY;
    }

    public void changeSpeed(double ratio) {
        if(ratio <= 0) {
            return;
        }
        this.stepX *= ratio;
        this.stepY *= ratio;
    }

    public void kill() {
        this.color = Color.red;
        this.stop();
        this.killed = true;
    }

    public void win() {
        this.color = Color.green;
        this.stop();
        this.won = true;
    }

    public boolean won() {
        return this.won;
    }

    public boolean killed() {
        return this.killed;
    }

    public void remove() {
        this.rec = null;
    }

    public boolean isRemoved() {
        return this.rec == null;
    }

    public boolean intersectsWithFrameX(int FH) {
        return this.getCY() >= FH || this.getAY() < 0;
    }

    public boolean intersectsWithFrameY(int FW) {
        return this.getBX() >= FW || this.getAX() < 0;
    }

    public boolean intersectsWithFrameX(int FH, int TOP_OFFSET, int DOWN_OFFSET) {
        return this.getCY() >= FH - DOWN_OFFSET || this.getAY() < 0 + TOP_OFFSET;
    }

    public boolean intersectsWithFrameY(int FW, int LEFT_OFFSET, int RIGHT_OFFSET) {
        return this.getBX() >= FW - RIGHT_OFFSET || this.getAX() < 0 + LEFT_OFFSET;
    }

    public boolean intersects(MyRectangle r) {
        return 
            r.getHeight() > 0
            && r.getWidth() > 0
            && this.getHeight() > 0
            && this.getWidth() > 0
            && r.getAX() < this.getBX()
            && r.getBX() > this.getAX()
            && r.getAY() < this.getCY()
            && r.getCY() > this.getAY();
    }

    public boolean contains(double x, double y) {
        return 
            x < this.getBX()
            && x > this.getAX()
            && y < this.getCY()
            && y > this.getAY();
    }

    public boolean collideTop(MyRectangle other) {
        return 
            this.getAX() < other.getBX()
            && this.getBX() > other.getAX()
            && this.getAY() == other.getCY();
    }

    public boolean collideBottom(MyRectangle other) {
        return 
            this.getAX() < other.getBX()
            && this.getBX() > other.getAX()
            && this.getCY() == other.getAY();
    }

    public boolean collideLeft(MyRectangle other) {
        return 
            this.getAX() == other.getBX()
            && this.getAY() <= other.getCY()
            && this.getCY() >= other.getAY();
    }

    public boolean collideRight(MyRectangle other) {
        return 
            this.getBX() == other.getAX()
            && this.getAY() <= other.getCY()
            && this.getCY() >= other.getAY();
    }

    public Color setRandomColor() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        this.color = new Color(r, g, b);
        return this.color;
    }

    private void setSteps() {
        Random rand = new Random();
        double[] steps = {0.5, 0.6, 0.7, 0.8, 0.9};
        this.stepX = steps[rand.nextInt(steps.length)];
        this.stepY = steps[rand.nextInt(steps.length)];
    }

    private void setRandomPosition(double FW, double FH) {
        Random rand = new Random();
        this.x = rand.nextInt((int)(FW-this.RW));
        this.y = rand.nextInt((int)(FH-this.RH));
    }

    private void setRandomPosition(double FW, double FH, int TOP_OFFSET) {
        Random rand = new Random();
        this.x = rand.nextInt((int)(FW-this.RW));
        this.y = TOP_OFFSET + rand.nextInt((int)(FH-this.RH-TOP_OFFSET));
    }

    
}
