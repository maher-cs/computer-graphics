import javax.swing.*;
import java.awt.*;

public class Circles extends JApplet {
    public static void main(String args[]) {
        JFrame frame = new JFrame();
        frame.setTitle("Draw Cirles Lab");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JApplet applet = new Circles();
        applet.init();
        frame.getContentPane().add(applet);
        frame.pack();
        frame.setVisible(true);
    }

    public void init() {
        JPanel panel = new CirclesPanel();
        getContentPane().add(panel);
    }
}

class CirclesPanel extends JPanel {
    private static final int FRM_WIDTH = 600;
    private static final int FRM_HEIGHT = 600;

    public CirclesPanel() {
		setPreferredSize(new Dimension(FRM_WIDTH, FRM_HEIGHT));
		this.setBackground(Color.white);
	}

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int cirD1 = 200;
        int cirD2 = 200;

        // First Circle
        int x1 = ( FRM_WIDTH - (cirD1 + cirD2) ) / 3;
        int y1 = ( FRM_HEIGHT - cirD1 ) / 2;
        g2.setColor(Color.RED);
        g2.drawOval(x1, y1, cirD1, cirD1);

        // Second Circle
        int x2 = x1 + x1 + cirD1;
        int y2 = ( FRM_HEIGHT - cirD2 ) / 2;
        g2.setColor(Color.GREEN);
        g2.fillOval(x2, y2, cirD2, cirD2);

    }

}