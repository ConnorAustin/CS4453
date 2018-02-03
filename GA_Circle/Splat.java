import javax.swing.*;
import java.awt.*;

public class Splat
{
   JFrame frame;
   SplatPanel sp;
   public void run(gacircle[] G, gacircle A)
   {
      frame = new JFrame("GA-Circle");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      sp = new SplatPanel(G,A);
      
      frame.getContentPane().add(sp);
      
      frame.pack();
      
      frame.setVisible(true);
      
   }
   public void update(gacircle A)
   {
      sp.update(A);  
      frame.repaint();
   }
   public void supdate(gacircle A)
   {
        sp.supdate(A);
        frame.repaint();
   }
}