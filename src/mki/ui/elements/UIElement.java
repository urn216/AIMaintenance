package mki.ui.elements;

import java.awt.Graphics2D;

import mki.ui.components.UIComponent;
import mki.ui.components.UIInteractable;
import mki.ui.control.UIColours;

import mki.math.MathHelp;
import mki.math.vector.Vector2;

/**
* Write a description of class Element here.
*
* @author (your name)
* @version (a version number or a date)
*/
public abstract class UIElement {

  public static final byte TRANSITION_FADE_IN_PLACE  = 0b0;
  public static final byte TRANSITION_STRETCH_RIGHT  = (byte) 0b00000001;
  public static final byte TRANSITION_SHRINK_LEFT    = (byte) 0b00000011;
  public static final byte TRANSITION_SHRINK_RIGHT   = (byte) 0b00000100;
  public static final byte TRANSITION_STRETCH_LEFT   = (byte) 0b00001100;
  public static final byte TRANSITION_STRETCH_DOWN   = (byte) 0b00010000;
  public static final byte TRANSITION_SHRINK_UP      = (byte) 0b00110000;
  public static final byte TRANSITION_SHRINK_DOWN    = (byte) 0b01000000;
  public static final byte TRANSITION_STRETCH_UP     = (byte) 0b11000000;

  public static final byte TRANSITION_SLIDE_UP    = TRANSITION_STRETCH_UP | TRANSITION_SHRINK_UP;
  public static final byte TRANSITION_SLIDE_DOWN  = TRANSITION_STRETCH_DOWN | TRANSITION_SHRINK_DOWN;
  public static final byte TRANSITION_SLIDE_LEFT  = TRANSITION_STRETCH_LEFT | TRANSITION_SHRINK_LEFT;
  public static final byte TRANSITION_SLIDE_RIGHT = TRANSITION_STRETCH_RIGHT | TRANSITION_SHRINK_RIGHT;

  public static final byte TRANSITION_SLIDE_UP_LEFT    = TRANSITION_SLIDE_UP | TRANSITION_SLIDE_LEFT;
  public static final byte TRANSITION_SLIDE_UP_RIGHT   = TRANSITION_SLIDE_UP | TRANSITION_SLIDE_RIGHT;
  public static final byte TRANSITION_SLIDE_DOWN_LEFT  = TRANSITION_SLIDE_DOWN | TRANSITION_SLIDE_LEFT;
  public static final byte TRANSITION_SLIDE_DOWN_RIGHT = TRANSITION_SLIDE_DOWN | TRANSITION_SLIDE_RIGHT;

  public static final byte TRANSITION_STRETCH_HORIZONTALLY = TRANSITION_STRETCH_LEFT | TRANSITION_STRETCH_RIGHT;
  public static final byte TRANSITION_STRETCH_VERTICALLY   = TRANSITION_STRETCH_UP | TRANSITION_STRETCH_DOWN;
  public static final byte TRANSITION_SHRINK_HORIZONTALLY = TRANSITION_SHRINK_LEFT | TRANSITION_SHRINK_RIGHT;
  public static final byte TRANSITION_SHRINK_VERTICALLY   = TRANSITION_SHRINK_UP | TRANSITION_SHRINK_DOWN;
  
  public static final byte TRANSITION_STRETCH = TRANSITION_STRETCH_HORIZONTALLY | TRANSITION_STRETCH_VERTICALLY;
  public static final byte TRANSITION_SHRINK  = TRANSITION_SHRINK_HORIZONTALLY | TRANSITION_SHRINK_VERTICALLY;

  private final Vector2 topLeft;
  private final Vector2 botRight;
  
  private final double fadeDist = 0.08;
  private double fadeCount = 0;
  
  private long animTimeMillis = 175;
  private long startTimeMillis = System.currentTimeMillis();
  
  private final int transDirTopEdge  ;
  private final int transDirLowerEdge;
  private final int transDirLeftEdge ;
  private final int transDirRightEdge;

  protected UIComponent background = UIBackgrounds.rectangleNormal;
  
  private boolean active = false;
  private boolean transIn = false;
  private boolean transOut = false;
  
  protected UIComponent[] components = {};
  
  /**
   * Constructs a generic {@code UIElement} with no contents, to be placed into a {@code UIPane}.
   * <p>
   * Override the {@code draw} method to display something within this element.
   * 
   * @param topLeft the percentage into the screen for the top left corner of the component to reside.
   * @param botRight the percentage into the screen for the bottom right corner of the component to reside.
   * @param transition a byte representing the type of animation to perform upon transitioning this element.
   */
  public UIElement(Vector2 topLeft, Vector2 botRight, byte transition) {
    this.topLeft = topLeft;
    this.botRight = botRight;
    
    transDirTopEdge    = -(((transition>>7)&1)*2 - 1) * ((transition>>6)&1);
    transDirLowerEdge  = -(((transition>>5)&1)*2 - 1) * ((transition>>4)&1);
    transDirLeftEdge   = -(((transition>>3)&1)*2 - 1) * ((transition>>2)&1);
    transDirRightEdge  = -(((transition>>1)&1)*2 - 1) * ((transition>>0)&1);

    init();
  }
  
  /**
   * Performed once on initialisation of {@code UIElement}. 
   * Does nothing by default; override for additional functionality on creation of this object.
   */
  protected void init() {}
  
  /**
  * Gets the active state of this element
  *
  * @return true if the element is active
  */
  public final boolean isActive() {
    return !transOut && active && !transIn;
  }
  
  /**
  * Sets the element to an inactive state immediately without transitioning
  */
  public final void deactivate() {
    transIn = transOut = active = false;
  }
  
  /**
  * Gets the transition state of this element
  *
  * @return true if the element is transitioning
  */
  public final boolean isTransitioning() {
    return transOut || transIn;
  }
  
  /**
  * Tells the element to transition out if it is not already doing so
  *
  * @param animTimeMillis the time in milliseconds with which to complete the transition
  *
  * @return {@code true} if the element has now changed to be transitioning out
  */
  public final boolean transOut(long animTimeMillis) {
    resetClickables();
    if (transOut || (!active && !transIn)) return false;

    this.startTimeMillis = System.currentTimeMillis();
    this.animTimeMillis = animTimeMillis;
    this.transIn = false;
    this.transOut = true;
    return true;
  }
  
  /**
  * Tells the element to transition in if it is not already doing so
  *
  * @param animTimeMillis the time in milliseconds with which to complete the transition
  *
  * @return {@code true} if the element has now changed to be transitioning in
  */
  public final boolean transIn(long animTimeMillis) {
    for (int i = 0; i < components.length; i++) components[i].onTransIn();
    if (transIn || (active && !transOut)) return false;

    this.startTimeMillis = System.currentTimeMillis();
    this.animTimeMillis = animTimeMillis;
    this.transIn = true;
    this.transOut = false;
    return true;
  }
  
  /**
  * toggles the state of this element
  *
  * @param animTimeMillis the time in milliseconds with which to complete the transition
  *
  * @return true if the element is now transitioning
  */
  public final boolean toggle(long animTimeMillis) {
    return active ? transOut(animTimeMillis) : transIn(animTimeMillis);
  }
  
  /**
  * retrieves the component at a given position
  *
  * @param x The x coord of the given position
  * @param y The y coord of the given position
  *
  * @return the UIComponent present at this location, provided it exists
  */
  public final UIComponent getComponent(double x, double y) {
    UIComponent res = null;
    for (int i = 0; i < components.length; i++) {
      UIComponent touching = components[i].touching(x, y);
      if (touching != null) res = touching;
    }
    return res;
  }
  
  /**
  * resets all the clickables in this element
  */
  public final void resetClickables() {
    for (int i = 0; i < components.length; i++) {
      if (components[i] instanceof UIInteractable c) c.setOut();
    }
  }
  
  /**
  * @return the components tied to this element
  */
  public final UIComponent[] getComponents() {return components;}
  
  /**
  * draws this element
  *
  * @param g The Graphics2D object to draw to
  * @param screenSizeX The length of the screen
  * @param screenSizeY The height of the screen
  */
  public final void draw(Graphics2D g, int screenSizeX, int screenSizeY) {
    if (!active && !transIn) {return;}
    UIColours.ColourSet c = UIColours.ACTIVE;
    Vector2[] lurd = {topLeft, botRight};
    
    //Transition if necessary
    if (transIn) {
      transOut = false;
      if (fadeCount >= fadeDist) {transIn = false; active = true; fadeCount = 0;}
      else {
        fadeCount = Math.min(fadeDist, MathHelp.lerp(0, fadeDist, (1.0*System.currentTimeMillis()-startTimeMillis)/animTimeMillis));
        c = c.withOpacity(fadeCount/fadeDist);
        lurd = fadeVecs(topLeft, botRight, fadeDist-fadeCount);
      }
    }
    else if (transOut) {
      if (fadeCount >= fadeDist) {transOut = false; active = false; fadeCount = 0; return;}
      else {
        fadeCount = Math.min(fadeDist, MathHelp.lerp(0, fadeDist, (1.0*System.currentTimeMillis()-startTimeMillis)/animTimeMillis));
        c = c.withOpacity(1-fadeCount/fadeDist);
        lurd = fadeVecs(topLeft, botRight, fadeCount);
      }
    }
    
    Vector2 tL = lurd[0].scale(screenSizeX, screenSizeY);
    Vector2 bR = lurd[1].scale(screenSizeX, screenSizeY);

    background.draw(g, (float)tL.x, (float)tL.y, (float)(bR.x-tL.x), (float)(bR.y-tL.y), c);
    
    draw(g, screenSizeY, tL, bR, c);
  }
  
  private final Vector2[] fadeVecs(Vector2 tL, Vector2 bR, double dist) {
    return new Vector2[] {
      tL.add(transDirLeftEdge  * dist, transDirTopEdge   * dist),
      bR.add(transDirRightEdge * dist, transDirLowerEdge * dist)
    };
  }
  
  /**
  * draws this element
  * 
  * @param g The Graphics2D object to draw to
  * @param screenSizeY The height of the screen
  * @param tL The Vector2 representing the top left corner of the element in pixels
  * @param bR The Vector2 representing the bottom right corner of the element in pixels
  * @param c The colours to use in drawing the element
  */
  protected abstract void draw(Graphics2D g, int screenSizeY, Vector2 tL, Vector2 bR, UIColours.ColourSet c);
}
