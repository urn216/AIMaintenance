package mki.ui.components.interactables;

import java.awt.Color;
import java.awt.geom.RoundRectangle2D;

import mki.ui.components.UIInteractable;
import mki.ui.control.UIActionGetter;
import mki.ui.control.UIActionSetter;

import java.awt.Graphics2D;

/**
* Class for making functional Toggle Buttons
*/
public class UIToggle extends UIInteractable {

  private static final float BUFFER_SCALE = 1f/4f;

  private final UIActionGetter<Boolean> get;

  /**
   * Constructs interacatble buttons which toggle a setting on interaction
   * 
   * @param text the user-visible label for the button
   * @param get the setting to read the current state from
   * @param set the setting to input a new value to - ideally linked to the same {@code boolean} as {@code get}
   */
  public UIToggle(String text, UIActionGetter<Boolean> get, UIActionSetter<Boolean> set) {
    this.text = text;
    this.get = get;
    this.primeAction = () -> {
      set.set(!get.get());
    };
  }

  @Override
  protected void drawBody(Graphics2D g, int off, Color bodyCol, Color textCol) {
    g.setColor(bodyCol);
    g.fill(new RoundRectangle2D.Float(x+off, y+off, width-off, height-off, height/8, height/8));
    g.setColor(textCol);
    g.draw(new RoundRectangle2D.Float(x, y, width, height, height/8, height/8));
    float bodyW = width-height;
    float buffer = height*BUFFER_SCALE;

    g.draw(new RoundRectangle2D.Float(x+bodyW, y, height, height, height/8, height/8));
    g.drawString(text, x+off+(bodyW-metrics.stringWidth(text))/2, y+off+((height - metrics.getHeight())/2) + metrics.getAscent());

    g.draw(new RoundRectangle2D.Float(x+bodyW+buffer, y+buffer, height-buffer*2, height-buffer*2, height/8, height/8));
    if (get.get()) g.fill(new RoundRectangle2D.Float(x+bodyW+buffer*1.125f+off/2, y+buffer*1.125f+off/2, height-buffer*2.25f, height-buffer*2.25f, height/8, height/8));
  }
}
