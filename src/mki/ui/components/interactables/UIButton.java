package mki.ui.components.interactables;

import java.awt.Color;

import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import mki.ui.components.UIInteractable;
import mki.ui.control.UIAction;

/**
* Class for making functional Buttons
*/
public class UIButton extends UIInteractable {

  /**
  * Constructor for Buttons
  */
  public UIButton(String text, UIAction action) {
    this.text = text;
    this.primeAction = action;
  }

  @Override
  protected void drawBody(Graphics2D g, int off, Color bodyCol, Color textCol) {
    g.setColor(bodyCol);
    g.fill(new RoundRectangle2D.Float(x+off, y+off, width-off, height-off, height/8, height/8));
    g.setColor(textCol);
    g.draw(new RoundRectangle2D.Float(x, y, width, height, height/8, height/8));
    g.drawString(text, x+off+(width-metrics.stringWidth(text))/2, y+off+((height - metrics.getHeight())/2) + metrics.getAscent());
  }
}
