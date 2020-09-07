import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

public class StringAnimation extends JApplet {

    public static void main(String s[]) {
        JFrame frame = new JFrame();
        frame.setTitle("String Animation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JApplet applet = new StringAnimation();
        applet.init();
        frame.getContentPane().add(applet);
        frame.pack();
        frame.setVisible(true);
    }

    public void init() {
        JPanel panel = new PanelStringAnimation();
        getContentPane().add(panel);
    }
}

class PanelStringAnimation extends JPanel implements Runnable {

    private static final int FW = 600;
    private static final int FH = 300;
    Font fontObj = new Font("Arial", Font.BOLD, 30);;
    	
    int x, y;
    Thread mythread;

    public PanelStringAnimation() {
        setPreferredSize(new Dimension(FW, FH));
        this.setBackground(Color.black);

        mythread = new Thread(this);
        mythread.start();
    }

    public void run() {
    	
        while (true) {
        	try{
        		Thread.sleep(1000);
        	}
            catch(InterruptedException e){}
            
            x = randomNumberInRange(0, this.getWidth());
            y = randomNumberInRange(0, this.getHeight());
            
        	repaint();
        }                
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;        
         
		g2.setFont(fontObj);	
        g2.setColor(Color.green);        
        g2.drawString("Salam Shabab", x, y);
    }
    
    public static int randomNumberInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
    
}
