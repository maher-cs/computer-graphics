import javax.swing.*;
import java.awt.*;

public class RectangleInsideRectangle extends JApplet {
    public static void main(String args[]) {
        JFrame frame = new JFrame();
        frame.setTitle("Draw Rectangle Inside Rectangle Lab");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JApplet applet = new RectangleInsideRectangle();
        applet.init();
        frame.getContentPane().add(applet);
        frame.pack();
        frame.setVisible(true);
    }

    public void init() {
        JPanel panel = new RectangleInsideRectanglePanel();
        getContentPane().add(panel);
    }
}

class RectangleInsideRectanglePanel extends JPanel {
    private static final int FRM_WIDTH = 400;
    private static final int FRM_HEIGHT = 400;

    public RectangleInsideRectanglePanel() {
		setPreferredSize(new Dimension(FRM_WIDTH, FRM_HEIGHT));
		this.setBackground(Color.white);
	}

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int recW1 = 300;
        int recH1 = 200;

        int recW2 = 200;
        int recH2 = 100;

        // Outer Rectangle
        int x1 = ( FRM_WIDTH - recW1 ) / 2;
        int y1 = ( FRM_HEIGHT - recH1 ) / 2;
        g2.setColor(Color.BLACK);
        g2.drawRect(x1, y1, recW1, recH1);

        // Inner Rectangle
        int x2 = ( FRM_WIDTH - recW2 ) / 2;
        int y2 = ( FRM_HEIGHT - recH2 ) / 2;
        g2.setColor(Color.BLUE);
        g2.drawRect(x2, y2, recW2, recH2);

    }

}