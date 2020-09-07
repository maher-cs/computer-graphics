import javax.swing.*;
import java.awt.*;

public class TriangleByLines extends JApplet {
    public static void main(String args[]) {
        JFrame frame = new JFrame();
        frame.setTitle("Draw Triangle By Lines Lab");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JApplet applet = new TriangleByLines();
        applet.init();
        frame.getContentPane().add(applet);
        frame.pack();
        frame.setVisible(true);
    }

    public void init() {
        JPanel panel = new TriangleByLinesPanel();
        getContentPane().add(panel);
    }
}

class TriangleByLinesPanel extends JPanel {
    private static final int FRM_WIDTH = 600;
    private static final int FRM_HEIGHT = 600;

    public TriangleByLinesPanel() {
		setPreferredSize(new Dimension(FRM_WIDTH, FRM_HEIGHT));
		this.setBackground(Color.white);
	}

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int triBase = 200;
        int triHeight = 100;

        // trianles points
        int x1 = ( FRM_WIDTH - triBase ) / 2;
        int y1 = ( FRM_HEIGHT - triHeight ) / 2;

        int x2 = x1 + triBase;
        int y2 = y1;

        int x3 = x1 + triBase;
        int y3 = y1 + triHeight;

        g2.setColor(Color.BLACK);
        g2.drawLine(x1, y1, x2, y2);
        g2.drawLine(x2, y2, x3, y3);
        g2.drawLine(x3, y3, x1, y1);

    }

}