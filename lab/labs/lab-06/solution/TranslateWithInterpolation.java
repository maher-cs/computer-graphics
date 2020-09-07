/*
 * UQU - CS - Computer Graphics - spring 2020
 * pr. Murtaza Khan
 * subject: lab-06: transformation with interpolation
 * authors:
 * - MHD Maher Azkoul
 *   438017578
 * 
 * program description:
 *  this code show translation, scaling and rotation 
 * operations on a rectangle, with interpolation
 * 
 */

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class TranslateWithInterpolation extends JApplet {
    public static void main(String s[]) {
        JFrame frame = new JFrame();
        frame.setTitle("Transformation: Translation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JApplet applet = new TranslateWithInterpolation();
        applet.init();
        frame.getContentPane().add(applet);
        frame.pack();
        frame.setVisible(true);
    }

    public void init() {
        JPanel panel = new TranslatePanel();
        getContentPane().add(panel);
    }
}

class TranslatePanel extends JPanel implements Runnable {

    private static final int FW = 800;
    private static final int FH = 600;
    private static final double RW = 80;
    private static final double RH = 60;
    private static final double X = (FW - RW) / 2;
    private static final double Y = (FH - RH) / 2;

    

    private double tx = 200;
    private double ty = -100;
    private double sx = 0.5;
    private double sy = 2;
    private double ang = 15;

    private int TRANSLATE_STEP = (int)tx;
    private int SCALE_STEP = (int)100;
    private int ROTATE_STEP = (int)ang*3;

    private Shape rec;
    private Color color;
    private AffineTransform transform;

    private Thread thread;
    private int sleep = 10;

    private int translateTracker = 0;
    private int scaleTracker = -1;
    private int rotateTracker = -1;
    private boolean finish = false;

    public TranslatePanel() {
        setPreferredSize(new Dimension(FW, FH));
        this.setBackground(Color.white);

        this.color = Color.BLUE;
        this.rec = new Rectangle2D.Double(X, Y, RW, RH);
        this.transform = new AffineTransform();

        this.thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {

            try {
                thread.sleep(sleep);
            } catch (Exception e) {
            }
            this.transform = new AffineTransform();
            
            translateInterpolate();
            scaleInterpolate();
            rotateInterpolate();
            checkFinish();
            if(!finish) {
                repaint();
            } else {
                sleep = 1000000;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(this.color);
        g2.fill(rec);

    }

    private void checkFinish() {
        finish = translateTracker == -1 && scaleTracker == -1 && rotateTracker == -1;
    }

    private void translateInterpolate() {
        if(translateTracker > -1) {
            transform.translate(tx/TRANSLATE_STEP, ty/TRANSLATE_STEP);
            transformColor(Color.BLUE, Color.RED, TRANSLATE_STEP);
            rec = transform.createTransformedShape(rec);
            translateTracker++;
            if(translateTracker >= TRANSLATE_STEP) {
                translateTracker = -1;
                scaleTracker = 0;
                try {
                    thread.sleep(1000);
                } catch(Exception e) {}
            }
        }
    }

    private void scaleInterpolate() {
        if(scaleTracker > -1) {
            double rootx = Math.pow(sx, 1.0/SCALE_STEP);
            double rooty = Math.pow(sy, 1.0/SCALE_STEP);
            transform.scale(rootx, rooty);;
            transformColor(Color.RED, Color.GREEN, SCALE_STEP);
            rec = transform.createTransformedShape(rec);
            scaleTracker++;
            if(scaleTracker > SCALE_STEP) {
                scaleTracker = -1;
                rotateTracker = 0;
                try {
                    thread.sleep(1000);
                } catch(Exception e) {}
            }
        }
    }

    private void rotateInterpolate() {
        if(rotateTracker > -1) {
            transform.setToRotation((ang/ROTATE_STEP) * (Math.PI/180));;
            transformColor(Color.GREEN, Color.YELLOW, ROTATE_STEP);
            rec = transform.createTransformedShape(rec);
            rotateTracker++;
            if(rotateTracker > ROTATE_STEP) {
                rotateTracker = -1;
                try {
                    thread.sleep(1000);
                } catch(Exception e) {}
            }
        }
    }

    private void transformColor(Color src, Color target, int frames) {
        int redDiff = target.getRed() - src.getRed();
        int greenDiff = target.getGreen() - src.getGreen();
        int blueDiff = target.getBlue() - src.getBlue();
        
        int thisRed = this.color.getRed();
        int thisGreen = this.color.getGreen();
        int thisBlue = this.color.getBlue();

        int newRed = thisRed + redDiff/frames > 0 ? thisRed + redDiff/frames : 0;
        int newGreen = thisGreen + greenDiff/frames > 0 ? thisGreen + greenDiff/frames : 0;
        int newBlue = thisBlue + blueDiff/frames > 0 ? thisBlue + blueDiff/frames : 0;
        this.color = new Color(newRed, newGreen, newBlue);
    }

    private double getRadianFromDegrees(double angle) {
        return angle * Math.PI / 180;
    }
}
