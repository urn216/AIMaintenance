package mki.ui.components.interactables;

// import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;

import mki.ui.components.UIInteractable;
import mki.ui.control.UIActionGetter;
import mki.ui.control.UIActionSetter;

public class UIDropDown<T> extends UIInteractable {

  private boolean dropDown = false;

  private final T[] options;

  private final UIActionGetter<T> get;

  @SafeVarargs
  public UIDropDown(String text, UIActionGetter<T> get, UIActionSetter<T> set, T... options) {
    this.text = text;
    this.primeAction = this::toggleDropDown;
    this.options = options;
    this.get = get;
  }

  public void toggleDropDown() {
    this.dropDown = !dropDown;
  }

  public T[] getOptions() {
    return options;
  }

  @Override
  protected void drawBody(java.awt.Graphics2D g, int off, java.awt.Color bodyCol, java.awt.Color textCol) {
    g.setColor(bodyCol);
    g.fill(new RoundRectangle2D.Float(x+off, y+off, width-off, height-off, height/8, height/8));
    g.setColor(textCol);
    g.draw(new RoundRectangle2D.Float(x, y, width, height, height/8, height/8));
    float bodyW = width-height;

    g.drawString(
      String.format(text, get.get()), 
      x+off+(bodyW - metrics.stringWidth(text))/2, 
      y+off+((height - metrics.getHeight())/2) + metrics.getAscent()
    );

    String ddChar = dropDown ? "\u02C5" : "\u02C4";
    
    g.drawString(
      ddChar, 
      x+off+bodyW+(height - metrics.stringWidth(ddChar))/2, 
      y+off+((height - metrics.getHeight())/2) + metrics.getAscent()
    );
  };
}
