/*
 * UQU - CS - Computer Graphics - spring 2020
 * pr. Murtaza Khan
 * subject: lab-06: transformation
 * authors:
 * - MHD Maher Azkoul
 *   438017578
 * 
 * program description:
 *  this code show translation, scaling and rotation 
 * operations on a rectangle
 * 
 */

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class TranslateScaleRotate extends JApplet {
    public static void main(String s[]) {
        JFrame frame = new JFrame();
        frame.setTitle("Transformation: Translation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JApplet applet = new TranslateScaleRotate();
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

class TranslatePanel extends JPanel {

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

    public TranslatePanel() {
        setPreferredSize(new Dimension(FW, FH));
        this.setBackground(Color.white);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // create shape
        Shape rec = new Rectangle2D.Double(X, Y, RW, RH);

        g2.setColor(Color.blue);
        g2.fill(rec);

        // create an object of class AffineTransform
        AffineTransform transform = new AffineTransform();

        // Translation
        transform.translate(tx, ty);
        rec = transform.createTransformedShape(rec);
        g2.setColor(Color.red);
        g2.fill(rec);

        // Scale
        transform.scale(sx, sy);
        rec = transform.createTransformedShape(rec);
        g2.setColor(Color.green);
        g2.fill(rec);

        // Rotation
        double radAng = getRadianFromDegrees(ang);
        transform.setToRotation(radAng);
        rec = transform.createTransformedShape(rec);
        g2.setColor(Color.yellow);
        g2.fill(rec);

    }

    private double getRadianFromDegrees(double angle) {
        return angle * Math.PI / 180;
    }

}
