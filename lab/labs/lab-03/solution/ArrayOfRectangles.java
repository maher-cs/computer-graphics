
import java.awt.*;
import java.awt.event.*;

public class ArrayOfRectangles {

    public static void main(String[] args) {
        ArrayOfRectanglesFrame frame = new ArrayOfRectanglesFrame();
        frame.setTitle("Tiles of Rectangles");
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    } // end of main 
}

class ArrayOfRectanglesFrame extends Frame {

    private static final int NO_OF_RECT_ROW = 3;
    private static final int NO_OF_RECT = NO_OF_RECT_ROW * NO_OF_RECT_ROW;

    private static final int FW = 700; // frame width
    private static final int FH = FW; // frame height

    private static final int RW = (int) ( (FW - 50) / (NO_OF_RECT_ROW * 1.5)); // rectangle width
    private static final int RH = RW;  // rectangle height

    private static final int H = RW / 2;  // horz. space b/w rectangles
    private static final int V = H;      // vert. space b/w rectangles

    
    

    private static final Color[] COLORS = {Color.BLUE, Color.GREEN, Color.RED};

    Rectangle rectAry[] = new Rectangle[NO_OF_RECT];

    public ArrayOfRectanglesFrame() {
        setSize(FW, FH);
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int x = 0;
        int y = -RH;
                
        
        for (int j = 0; j < NO_OF_RECT; j++) {
            x = x + RW + H;
            if(j % NO_OF_RECT_ROW == 0) {
                y = y + RH + V;
                x = H;
            }
            Color color = this.COLORS[(j/NO_OF_RECT_ROW) % this.COLORS.length];
            g2.setColor(color);
            rectAry[j] = new Rectangle(x, y, RW, RH);
            g2.fill(rectAry[j]);
        }

    }

}
