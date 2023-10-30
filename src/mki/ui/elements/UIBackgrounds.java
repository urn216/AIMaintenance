package mki.ui.elements;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import mki.ui.components.UIComponent;
import mki.ui.control.UIColours;

abstract class UIBackgrounds {
  public static final UIComponent rectangleNormal = new UIComponent() {
    
    @Override
    protected void draw(Graphics2D g, UIColours.ColourSet c) {
      g.setColor(c.background());
      g.fill(new Rectangle2D.Double(x, y, width, height));
    }
  };
  
  public static final UIComponent rectangleSolidBack = new UIComponent() {
    
    @Override
    protected void draw(Graphics2D g, UIColours.ColourSet c) {
      g.setColor(c.background());
      g.fill(new Rectangle2D.Double(x, y, width, height));

      double buffer = 0.07*height;
      Rectangle2D s = new Rectangle2D.Double(x+buffer, y+buffer, width-buffer*2, height-buffer*2);
      
      g.setColor(c.buttonBody());
      g.fill(s);
      g.setColor(c.buttonAccentOut());
      g.draw(s);
    }
  };
}