import javax.swing.*;
import java.awt.*;

public class SplatPanel extends JPanel
{
   private Circle circle1, circle2, circle3, circle4, circle5, circle6, circle7;
   public SplatPanel(gacircle[] G, gacircle A)
   {
      //System.out.println("Disk #"+i+" : x-location:"+G[i].X_location+", y-location:"+G[i].Y_location+", radius:"+G[i].radius)
      circle1 = new Circle((int)(G[0].radius * 60),Color.red,(int)((G[0].X_location - G[0].radius) * 30),(int)((G[0].Y_location - G[0].radius) * 30));
      circle2 = new Circle((int)(G[1].radius * 60),Color.red,(int)((G[1].X_location - G[1].radius) * 30),(int)((G[1].Y_location - G[1].radius) * 30));
      circle3 = new Circle((int)(G[2].radius * 60),Color.red,(int)((G[2].X_location - G[2].radius) * 30),(int)((G[2].Y_location - G[2].radius) * 30));
      circle4 = new Circle((int)(G[3].radius * 60),Color.red,(int)((G[3].X_location - G[3].radius) * 30),(int)((G[3].Y_location - G[3].radius) * 30));
      circle5 = new Circle((int)(G[4].radius * 60),Color.red,(int)((G[4].X_location - G[4].radius) * 30),(int)((G[4].Y_location - G[4].radius) * 30));
      
      circle6 = new Circle((int)(A.radius * 60),Color.green,(int)((A.X_location - A.radius) * 30),(int)((A.Y_location -A.radius) * 30));
       
      circle7 = new Circle(0,Color.black,0,0);
      
      setPreferredSize(new Dimension(330,330));
      setBackground(Color.black);
      
   }
   public void paintComponent(Graphics page)
   {
      super.paintComponent(page);
      circle1.draw(page);
      circle2.draw(page);
      circle3.draw(page);
      circle4.draw(page);
      circle5.draw(page);
      circle6.draw(page);
      circle7.draw(page);
      
   }
   public void update(gacircle A)
   {
       
       circle6.setX((int)((A.X_location - A.radius) * 30));
       circle6.setY((int)((A.Y_location - A.radius) * 30));
       circle6.setDiameter((int)(A.radius * 60));
    }
    public void supdate(gacircle A)
    {
        circle7.setX((int)((A.X_location - 0.05) * 30));
        circle7.setY((int)((A.Y_location - 0.05) * 30));
        circle7.setDiameter((int)(0.05 * 60));
        circle7.setColor(Color.blue);
    }
   
}