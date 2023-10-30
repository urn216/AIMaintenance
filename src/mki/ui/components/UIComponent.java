package mki.ui.components;

import java.awt.Graphics2D;

import mki.ui.control.UIColours;

public abstract class UIComponent {
  
  /**
   * Text representing this component, ideally to be drawn onto the component in some way, so a user may identify the purpose of the component
   */
  protected String text;

  protected float x = 0;
  protected float y = 0;
  protected float width = 0;
  protected float height = 0;

  /**
   * Sets the text within this component to something new
   * 
   * @param text Replacement text
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * Gets the text representing this component
   * 
   * @return The text
   */
  public String getText() {
    return text;
  }

  /**
   * Determines whether a set of coordinates intersects the bounds of this component's last drawn position
   * 
   * @param oX The x coordinate of the location to check
   * @param oY The y coordinate of the location to check
   * @return this component if the given location intersects its most recent bounding box, or {@code null} if it doesn't
   */
  public UIComponent touching(double oX, double oY) {
    float w = width/2, h = height/2;
    if (Math.abs(oX-x-w) < w && Math.abs(oY-y-h) < h) {
      return this;
    }
    return null;
  }

  /**
   * Triggers whenever this component is activated. Funtionless by default. Override to create functionality.
   */
  public void onTransIn() {}

  /**
   * Draws this component to a Graphics2D object at a given position and size
   * 
   * @param g The Graphics2D object to draw to
   * @param x The x coordinate to draw the component from
   * @param y The y coordinate to draw the component from
   * @param width The width in pixels to scale the horizontal size of the component to
   * @param height The height in pixels to scale the vertical size of the component to
   * @param c The colours to use in drawing the component
   */
  public final void draw(Graphics2D g, float x, float y, float width, float height, UIColours.ColourSet c) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;

    draw(g, c);
  }

  /**
   * Draws this component to a Graphics2D object at a given position and size
   * 
   * @param g The Graphics2D object to draw to
   * @param c The colours to use in drawing the component
   */
  protected abstract void draw(Graphics2D g, UIColours.ColourSet c);
}
