import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class RectanglesAnimation extends JApplet {

    public static void main(String s[]) {
        JFrame frame = new JFrame();
        frame.setTitle("Rectangle Animation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JApplet applet = new RectanglesAnimation();
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

class PanelRectanglesAnimation extends JPanel implements Runnable {

    private static final int FW = 400;
    private static final int FH = 400;
    private final int NUMBER_OF_RECS = 3;

    private int frameSpeed = 50;
    
    ArrayList<MyRectangle> rectangles;

    Thread mythread;
    
    public PanelRectanglesAnimation() {
        setPreferredSize(new Dimension(FW, FH));
        this.setBackground(Color.white);
            
        	      	   
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
                
                for(MyRectangle other : this.rectangles) {
                    if(myrec == other) {
                        continue;
                    }

                    if(myrec.collideTop(other) || myrec.collideBottom(other)) {
                        myrec.changeDirectionY();
                        myrec.setRandomColor();
                        myrec.updatePositionY();
                        myrec.saveState();
                        System.out.println("collide y");
                        other.changeDirectionY();
                        other.setRandomColor();
                        other.updatePositionY();
                        other.saveState();
                    }

                    if(myrec.collideLeft(other) || myrec.collideRight(other)) {
                        myrec.changeDirectionX();
                        myrec.setRandomColor();
                        myrec.updatePositionX();
                        myrec.saveState();
                        System.out.println("collide x");
                        other.changeDirectionX();
                        other.setRandomColor();
                        other.updatePositionX();
                        other.saveState();
                    }
                }

                if(myrec.intersectsWithFrameX(this.getHeight())) {
                    myrec.changeDirectionY();
                }

                if(myrec.intersectsWithFrameY(this.getWidth())) {
                    myrec.changeDirectionX();
                }

                myrec.updatePosition();
                myrec.saveState();
            }
     	
        	repaint();
        }  
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;        

		for(MyRectangle myrec : this.rectangles) {
            if(myrec.isRemoved()) {
                continue;
            }
            g2.setColor(myrec.getColor());	
            Rectangle.Double rec = myrec.redrawAndGetRectangle();
            g2.fill(rec);
        }
    }

    private void initRectangles() {
        this.rectangles = new ArrayList<MyRectangle>();
        for(int i = 0; i < NUMBER_OF_RECS; i++) {
            MyRectangle rec = new MyRectangle(FW, FH);
            this.rectangles.add(rec);
        }
    }
}

class MyRectangle {

    private static double RW = 60, RH = 40;
    private double x, y;
    private double stepX, stepY;
    private Color color;
    private Rectangle2D.Double rec;
    private Stack<MyRectangle> stateHistory;

    public MyRectangle(int FW, int FH) {
        this.setRandomPosition(FW, FH);
        this.setSteps();
        this.setRandomColor();
        this.stateHistory = new Stack<MyRectangle>();
        this.rec = new Rectangle2D.Double(x, y, RW, RH);
    }

    public MyRectangle(double x, double y) {
        this.x = x;
        this.y = y;
        this.setSteps();
        this.setRandomColor();
        this.stateHistory = new Stack<MyRectangle>();
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

    public void updatePositionX() {
        this.x += this.stepX;
    }

    public void updatePositionY() {
        this.y += this.stepY;
    }

    public void updatePosition() {
        this.x += this.stepX;
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
        boolean intersects = 
            r.getHeight() > 0
            && r.getWidth() > 0
            && this.getHeight() > 0
            && this.getWidth() > 0
            && r.getAX() < this.getBX()
            && r.getBX() > this.getAX()
            && r.getAY() < this.getCY()
            && r.getCY() > this.getAY();

        return intersects;
    }

    public boolean collideTop(MyRectangle r) {
        MyRectangle thisPrevState = this.getPreviosState();
        MyRectangle otherPrevState = r.getPreviosState();
        if(otherPrevState == null) {
            return this.intersects(r);
        }
        return 
            this.intersects(r)
            && !thisPrevState.intersects(otherPrevState)
            && thisPrevState.getAY() > otherPrevState.getCY()
            && thisPrevState.getYDirection() < 0;
    }

    public boolean collideBottom(MyRectangle r) {
        MyRectangle thisPrevState = this.getPreviosState();
        MyRectangle otherPrevState = r.getPreviosState();
        if(otherPrevState == null) {
            return this.intersects(r);
        }
        return 
            this.intersects(r)
            && !thisPrevState.intersects(otherPrevState)
            && thisPrevState.getAY() < otherPrevState.getCY()
            && thisPrevState.getYDirection() > 0;
    }

    public boolean collideLeft(MyRectangle r) {
        MyRectangle thisPrevState = this.getPreviosState();
        MyRectangle otherPrevState = r.getPreviosState();
        if(otherPrevState == null) {
            return this.intersects(r);
        }
        return 
            this.intersects(r)
            && !thisPrevState.intersects(otherPrevState)
            && thisPrevState.getAX() < otherPrevState.getBX()
            && thisPrevState.getXDirection() < 0;
    }

    public boolean collideRight(MyRectangle r) {
        MyRectangle thisPrevState = this.getPreviosState();
        MyRectangle otherPrevState = r.getPreviosState();
        if(otherPrevState == null) {
            return this.intersects(r);
        }
        return 
            this.intersects(r)
            && !thisPrevState.intersects(otherPrevState)
            && thisPrevState.getBX() < otherPrevState.getAX()
            && thisPrevState.getXDirection() > 0;
    }

    public Color setRandomColor() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        this.color = new Color(r, g, b);
        return this.color;
    }

    public MyRectangle saveState() {
        MyRectangle prevRec;
        if(this.stateHistory.empty()) {
            prevRec = null;
        } else {
            prevRec = this.stateHistory.pop();
        }
        this.stateHistory.push(this);
        return prevRec;
    }

    public MyRectangle getPreviosState() {
        if(this.stateHistory.empty()) {
            return null;
        }
        return this.stateHistory.peek();
    }

    @Override
    public String toString() {
        return
            "AX: " + this.getAX() + "\n"
            + "AY: " + this.getAY() + "\n"
            + "BX: " + this.getBX() + "\n"
            + "CY: " + this.getCY() + "\n"
            + "XDirection: " + this.getXDirection() + "\n"
            + "YDirection: " + this.getYDirection() + "\n"
            + "==========";
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