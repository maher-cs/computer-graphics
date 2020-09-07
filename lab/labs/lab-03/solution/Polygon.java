import java.awt.*;
import java.awt.event.*;

public class Polygon 
{ 
	public static void main(String[] args)
	{
		PolygonFrame frame = new PolygonFrame();
		frame.setTitle("Polygon");
		frame.setVisible(true);
		
		frame.addWindowListener(new WindowAdapter(){
  		public void windowClosing(WindowEvent we){
  		System.exit(0);
  		}
  		});		
	} // end of main 
} 

class PolygonFrame extends Frame
{
	public PolygonFrame()
	{
        final int FW = 500;  // frame width
        final int FH = 500; // frame height

		setSize(FW, FH);
	} 
	
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		
		// first polygon 
		int xValuesP1[] = {100,100,150,200,200, 150};
		int yValuesP1[] = {300,200,100,200,300, 400};
		g2.setColor(Color.blue);
		g2.fillPolygon(xValuesP1, yValuesP1, xValuesP1.length);   
	    

	} // End of paint() method
	
} 

