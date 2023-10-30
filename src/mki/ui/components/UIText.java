package mki.ui.components;

import java.awt.Font;
import java.awt.FontMetrics;

import java.awt.Graphics2D;

import mki.ui.control.UIColours;

/**
 * A {@code UIComponent} which simply displays a line of text
 */
public class UIText extends UIComponent {

  private final double fontScale;
  private final int fontStyle;

  /**
   * Constructs a {@code UIText} component which simply displays a line of text on a {@code UIElement}
   * 
   * @param text the text to display
   * @param fontScale the scale with which to size the font relative to the encapsulating {@code UIComponent}
   * @param fontStyle the style with which to draw the text @see java.awt.Font
   */
  public UIText(String text, double fontScale, int fontStyle) {
    this.text = text;
    this.fontScale = fontScale;
    this.fontStyle = fontStyle;
  }

  @Override
  public void draw(Graphics2D g, UIColours.ColourSet c) {
    Font font = new Font("Copperplate", fontStyle, (int) Math.round((height*fontScale)));
    FontMetrics metrics = g.getFontMetrics(font);
    g.setFont(font);
    g.setColor(c.text());

    g.drawString(text, x+(width-metrics.stringWidth(text))/2, y+((height - metrics.getHeight())/2) + metrics.getAscent());
  }
  
}
