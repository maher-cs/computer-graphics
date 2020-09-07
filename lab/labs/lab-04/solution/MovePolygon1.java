
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

public class MovePolygon1 extends JApplet {

    public static void main(String s[]) {
        JFrame frame = new JFrame();
        frame.setTitle("Transformation of Polygon via Event Handling");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JApplet applet = new MovePolygon1();
        applet.init();
        frame.getContentPane().add(applet);
        frame.pack();
        frame.setVisible(true);
    }

    public void init() {
        JPanel panel = new PanelMovePolygon1();
        getContentPane().add(panel);
    }
}

class PanelMovePolygon1 extends JPanel {

    String msg = "Use Arrow Keys to Move";

    private static final int FW = 900; //frame-width
    private static final int FH = 900; //frame-height
    private static final int PW = 60;  //polygon-width
    private static final int PH = 100; //polygon-height
    private static final int POINTS_NO = 15; //number of points

    // (x,y) coordinate of points to be use in polygon	
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

    int counter = 0;  // counter for moves

    int tx = 10;     // tranlsation distances along x-axis
    int ty = tx;     // tranlsation distances along y-axis
    int theta = 10;   // rotation angle

    public PanelMovePolygon1() {
        setPreferredSize(new Dimension(FW, FH));
        this.setBackground(Color.white);
        this.setFocusable(true);

        // register keyboard listener
        KeyPressListener listenerKey = new KeyPressListener();
        addKeyListener(listenerKey);

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setStroke(new BasicStroke(5.0f));

        g.setFont(new Font("Arial", Font.BOLD, 30));
        g2.drawString(msg, 40, 40);

        Polygon poly = new Polygon(xPoly, yPoly, xPoly.length);
        Shape polyShape = poly;
        g2.setColor(Color.green);
        g2.fill(polyShape);

        g2.setColor(Color.BLACK);
        for(int i = 0; i < POINTS_NO; i++) {
            if(pointsX[i] == 0 && pointsY[i] == 0) {
                continue;
            }
            g2.drawLine(pointsX[i], pointsY[i], pointsX[i], pointsY[i]);
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
            double x1 = xPoly[i] - xPoly[2];
            double y1 = yPoly[i] - yPoly[2];
            x1 = x1 * Math.cos(rad) - y1 * Math.sin(rad);
            y1 = x1 * Math.sin(rad) + y1 * Math.cos(rad);
            xPoly[i] = (int)Math.ceil(x1) + xPoly[2];
            yPoly[i] = (int)Math.ceil(y1) + yPoly[2];
        }
    }

    public void rotateCCW() {
        double rad = Math.toRadians(theta);
        for (int i = 0; i < xPoly.length; i++) {
            double x1 = xPoly[i] - xPoly[2];
            double y1 = yPoly[i] - yPoly[2];
            x1 = x1 * Math.cos(rad) - y1 * Math.sin(rad);
            y1 = x1 * Math.sin(rad) + y1 * Math.cos(rad);
            xPoly[i] = (int)Math.floor(x1) + xPoly[2];
            yPoly[i] = (int)Math.floor(y1) + yPoly[2];
        }
    }



    

}
