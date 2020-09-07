import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class RectangleAnimation extends JApplet {

    public static void main(String s[]) {
        JFrame frame = new JFrame();
        frame.setTitle("Rectangle Animation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JApplet applet = new RectangleAnimation();
        applet.init();
        frame.getContentPane().add(applet);
        frame.pack();
        frame.setLocationRelativeTo(null);  // Center window.
        frame.setVisible(true);
    }

    public void init() {
        JPanel panel = new PanelRectangleAnimation();
        getContentPane().add(panel);
    }
}

class PanelRectangleAnimation extends JPanel implements Runnable {

    private static final int FW = 400;
    private static final int FH = 400;
    private static final int RW = 60;
    private static final int RH = 40;

    int x, y;
    int stepX = 1, stepY = 1;
    
    Rectangle.Double rec;

    Thread mythread;
    
    public PanelRectangleAnimation() {
        setPreferredSize(new Dimension(FW, FH));
        this.setBackground(Color.white);

        this.mythread = new Thread(this);
        mythread.start();
    }

    @Override
    public void run() {
        while (true) {
        	try{
        		Thread.sleep(5);
        	}
            catch(InterruptedException e){}

            if (x+RW >= this.getWidth() || x < 0) {
        		stepX *= -1;
            }
            if (y+RH >= this.getHeight() || y < 0) {
                stepY *= -1;
            }
            
        	x = x + stepX;
        	y = y + stepY;
        	      	        	
        	repaint();
        }  
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;        
         
		g2.setColor(Color.red);				
		rec = new Rectangle2D.Double(x, y, RW, RH);
		
		
		g2.fill(rec);       
    }
}