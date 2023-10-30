package mki.ui.elements;

import java.awt.Graphics2D;
// import java.awt.geom.Rectangle2D;

import mki.ui.components.UIComponent;
import mki.ui.components.UIText;
import mki.ui.components.interactables.UISlider;
import mki.ui.control.UIColours;

import mki.math.vector.Vector2;

/**
* An element consisting of a vertical list of {@code UIComponent}s
*
* @author William Kilty
* @version 0.1
*/
public class ElemListVert extends UIElement {

  protected double buffer;
  protected double componentHeight;

  /**
  * Constructor for a vertical list of {@code UIComponent}s.
  *
  * @param tL The percent inwards from the top left corner of the screen for the top left corner of this element
  * @param bR The percent inwards from the top left corner of the screen for the bottom right corner of this element
  * @param componentHeight The height in pixels of each component in this element
  * @param buffer The amount of buffer space between buttons
  * @param components an array containing all the components to have in the column
  * @param transition a byte representing the type of animation to perform upon transitioning this element.
  */
  public ElemListVert(Vector2 tL, Vector2 bR, double componentHeight, double buffer, UIComponent[] components, byte transition) {
    super(tL, bR, transition);
    this.buffer = buffer;
    this.componentHeight = componentHeight;
    this.components = components;
  }

  @Override
  public void draw(Graphics2D g, int screenSizeY, Vector2 tL, Vector2 bR, UIColours.ColourSet c) {
    
    float buff = (float) (buffer * screenSizeY);

    float x = (float) tL.x + buff;
    float y = (float) tL.y + buff;
    float width = (float) bR.x - buff - x;

    for (UIComponent i : components) {
      float height = (float) (componentHeight * screenSizeY);
      if (i instanceof UIText) height/=2;
      i.draw(g, x, y, width, height, c);
      y += buff + height;
      if (i instanceof UISlider) y += height;
    }
  }
}
