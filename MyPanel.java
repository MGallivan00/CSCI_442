
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class MyPanel extends JPanel
{
 
int startX, flag, startY, endX, endY;

    int[] freq;
    BufferedImage grid;
    Graphics2D gc;
    int rgb[];
    private int max;

	public MyPanel(int[] freq)
	{
	   startX = startY = 0;
       endX = endY = 100;
       this.freq = freq;
 	}

     public void clear()
    {
       grid = null;
       repaint();
    }

    public void paintComponent(Graphics g)
    {  
         super.paintComponent(g);
         Graphics2D g2 = (Graphics2D)g;
         if(grid == null){
            int w = this.getWidth();
            int h = this.getHeight();
            grid = (BufferedImage)(this.createImage(w,h));
            gc = grid.createGraphics();

         }
         g2.drawImage(grid, null, 0, 0);
         this.drawing(freq);
     }
    public void drawing(int[] freq)
    {
        for(int i = 0; i < 255; i++) {
            gc.drawLine(5+i, this.getHeight(), 5+i, this.getHeight()-freq[i]);
            repaint();
        }
    }

    public void drawHistogram() {
        mapValues();
        for(int i=20; i<275; i++) {
            gc.drawLine(i, 595, i, 595-(int)(rgb[i-20]/5));
        }
        repaint();
    }

    public void mapValues() {
        double offset = 590.0/max;
        System.out.println(max + " offset " + offset);
        for (int i = 0; i<rgb.length; i++) {
            rgb[i] = (int)(offset * rgb[i]);
        }
    }
   
}
