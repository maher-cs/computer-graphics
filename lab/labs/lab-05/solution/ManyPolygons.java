/*
 * UQU - CS - Computer Graphics - spring 2020
 * pr. Murtaza Khan
 * subject: lab-05: triangles polygons
 * authors:
 * - MHD Maher Azkoul
 *   438017578
 * 
 * program description:
 *  this program will draw 4 triangles in the center 
 * of the frame using draw polygon method.
 */

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ManyPolygons { 
	public static void main(String[] args) {
		MyFrame frame = new MyFrame();
		frame.setTitle("Polygons");
		frame.setVisible(true);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
  			}
  		});		
	} // end of main 
} 

class MyFrame extends Frame {
	private final int FW = 600;
	private final int FH = 600;

	private final int PW = 200;
	private final int PH = 100;

	private int centerX = FW/2;
	private int centerY = FH/2;

	private int leftX = centerX - (PW/2);
	private int rightX = centerX + (PW/2);
	

	private int topY = centerY - (PH);
	private int bottomY = centerY + (PH);

	private ArrayList<Triangle> triangles;

	public MyFrame() {
		setSize(FW, FH);
		this.initPolygons();
	} 
	
	public void paint(Graphics g)
	{
		for(Triangle tri : this.triangles) {
			g.setColor(tri.getColor());
			g.fillPolygon(
				tri.getXValues(),
				tri.getYValues(),
				tri.getNoOfPoints()
			);   
		}
	}
	
	private void initPolygons() {
		this.triangles = new ArrayList<Triangle>();

		this.triangles.add(
			new Triangle(
				new int[]{leftX, rightX, centerX},
				new int[]{topY, topY, centerY},
				Color.RED
			)
		);

		this.triangles.add(
			new Triangle(
				new int[]{rightX, rightX, centerX},
				new int[]{topY, bottomY, centerY},
				Color.BLUE
			)
		);

		this.triangles.add(
			new Triangle(
				new int[]{leftX, rightX, centerX},
				new int[]{bottomY, bottomY, centerY},
				Color.GREEN
			)
		);

		this.triangles.add(
			new Triangle(
				new int[]{leftX, leftX, centerX},
				new int[]{topY, bottomY, centerY},
				Color.YELLOW
			)
		);
	}
	
}

class Triangle {
	private final int NO_OF_POINTS = 3;
	private int[] xValues;
	private int[] yValues;
	private Color color;

	public Triangle(int[] xValues, int[] yValues, Color color) {
		this.setXValues(xValues);
		this.setYValues(yValues);
		this.setColor(color);
	}

	public void setXValues(int... xValues) {
		this.xValues = xValues;
	}

	public void setYValues(int... yValues) {
		this.yValues = yValues;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getNoOfPoints() {
		return this.NO_OF_POINTS;
	}

	public int[] getXValues() {
		return Arrays.copyOf(this.xValues, this.xValues.length);
	}

	public int[] getYValues() {
		return Arrays.copyOf(this.yValues, this.yValues.length);
	}

	public Color getColor() {
		return this.color;
	}
}

