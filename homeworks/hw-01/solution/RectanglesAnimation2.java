/*
 * UQU - CS - Computer Graphics - spring 2020
 * pr. Murtaza Khan
 * subject: assignment-01: reclangles animations
 * authors:
 * - MHD Maher Azkoul
 *   438017578
 * 
 * program description:
 *  this code runs an animation of rectangles that collide with
 * with the frame borders and collide with other rectangles borders
 * 
 */


import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

import java.util.ArrayList;
import java.util.Random;

// This class is responsible for run the program
// it will creates the applet and panel objects
public class RectanglesAnimation2 extends JApplet {

    public static void main(String s[]) {
        JFrame frame = new JFrame();
        frame.setTitle("Rectangle Animation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JApplet applet = new RectanglesAnimation2();
        applet.init();
        frame.getContentPane().add(applet);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void init() {
        JPanel panel = new PanelRectanglesAnimation();
        getContentPane().add(panel);
    }
}

// This is the panel class that controls the redrawing process 
class PanelRectanglesAnimation extends JPanel implements Runnable {

    private static final int FW = 800;
    private static final int FH = 400;
    private final int NUMBER_OF_RECS = 7;

    private int frameSpeed = 20;
    
    ArrayList<MyRectangle> rectangles;

    Thread mythread;
    
    public PanelRectanglesAnimation() {
        setPreferredSize(new Dimension(FW, FH));
        this.setBackground(Color.BLACK);
        this.initRectangles();
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

                // this loop will check if the current rectangle
                // collide with other rectangle
                for(MyRectangle other : this.rectangles) {
                    if(myrec == other) {
                        continue;
                    }

                    // the two folowing conditions will check 
                    // if the current rectangle collides with other rectagles
                    // if it does, then both colliding rectangles
                    // will go in the other direction and change thier color to a random color

                    if(myrec.collideTop(other) || myrec.collideBottom(other)) {
                        myrec.changeDirectionY();
                        myrec.setRandomColor();
                        myrec.updatePositionY();
                        other.changeDirectionY();
                        other.setRandomColor();
                        other.updatePositionY();
                    }

                    if(myrec.collideLeft(other) || myrec.collideRight(other)) {
                        myrec.changeDirectionX();
                        myrec.setRandomColor();
                        myrec.updatePositionX();
                        other.changeDirectionX();
                        other.setRandomColor();
                        other.updatePositionX();
                    }
                }

                // these two conditions with check if the rectangle collides with 
                // frame borders
                if(myrec.intersectsWithFrameX(this.getHeight())) {
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

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;        

		for(MyRectangle myrec : this.rectangles) {
            g2.setColor(myrec.getColor());	
            Rectangle.Double rec = myrec.redrawAndGetRectangle();
            g2.fill(rec);
        }
    }

    // this method will run in the constructor to
    // initialize the array of rectangles
    private void initRectangles() {
        this.rectangles = new ArrayList<MyRectangle>();
        for(int i = 0; i < NUMBER_OF_RECS; i++) {
            MyRectangle rec = new MyRectangle(FW, FH);
            this.rectangles.add(rec);
        }
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

    public MyRectangle(int FW, int FH) {
        this.setRandomPosition(FW, FH);
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
        double[] steps = {1};
        this.stepX = steps[rand.nextInt(steps.length)];
        this.stepY = steps[rand.nextInt(steps.length)];
    }

    private void setRandomPosition(double FW, double FH) {
        Random rand = new Random();
        this.x = rand.nextInt((int)(FW-this.RW));
        this.y = rand.nextInt((int)(FH-this.RH));
    }
}