package mki.ui.control;

import java.awt.event.KeyEvent;

import java.util.*;

import mki.ui.components.UIComponent;
import mki.ui.components.UIInteractable;
import mki.ui.components.interactables.UISlider;
import mki.ui.components.interactables.UITextfield;
import mki.ui.elements.ElemInfo;
import mki.ui.elements.UIElement;

import mki.math.vector.Vector2;
import mki.math.vector.Vector2I;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
* UI class
*/
public abstract class UIController {

  private static final long DEFAULT_ANIMATION_TIME_MILLIS = 175;
  private static final long DEFAULT_ANIMATION_TIME_MILLIS_LONG = 1000;

  private static long animTimeMillis     = DEFAULT_ANIMATION_TIME_MILLIS;
  private static long animTimeMillisLong = DEFAULT_ANIMATION_TIME_MILLIS_LONG;
  
  private static final HashMap<String, UIPane> panes = new HashMap<String, UIPane>();;
  private static UIPane currentPane = new UIPane();
  
  private static UIInteractable highlightedInteractable = null;
  private static UITextfield activeTextfield = null;
  private static UISlider activeSlider = null;
  
  /**
   * Displays a warning to the screen at a given size
   * 
   * @param bufferHeight the percentage of the screen to use as a buffer in each direction around the warning, filled in with the background colour of components
   * @param componentHeight the percentage of the screen to use as the height of each line of the text
   * @param message an array with each element representing a line of text in the warning
   */
  public static synchronized void displayWarning(double bufferHeight, double componentHeight, String... message) {
    double height = UIHelp.calculateListHeight(bufferHeight, UIHelp.calculateComponentHeights(componentHeight, message));
    double width = UIHelp.calculateElementWidth(bufferHeight, UIHelp.calculateComponentWidths(componentHeight/2, message));

    ElemInfo info = new ElemInfo(
    new Vector2(0.5-width/2, 0.5-height/2),
    new Vector2(0.5+width/2, 0.5+height/2),
    bufferHeight, 
    UIElement.TRANSITION_FADE_IN_PLACE, 
    message
    );
    
    displayTempElement(info);
  }

  /**
   * Displays a temporary {@code UIElement} over top of the current {@code UIPane}
   * 
   * @param temp the {@code UIElement} to display
   */
  public static synchronized void displayTempElement(UIElement temp) {
    currentPane.setTempElement(temp);
    temp.transIn(animTimeMillis);
  }

  /**
   * Removes the temporary {@code UIElement} from the User Interface
   */
  public static synchronized void clearTempElement() {
    currentPane.clearTempElement();
  }
  
  /**
   * Adds a {@code UIPane} to this {@code UIController} under a given pseudonym
   * 
   * @param name the name to represent the given {@code UIPane} with
   * @param pane the {@code UIPane} to add
   */
  public static void putPane(String name, UIPane pane) {
    panes.put(name, pane);
  }
  
  /**
   * Retrieves a {@code UIPane} represented by the given {@code String}
   * 
   * @param name the name of the {@code UIPane} to get
   * 
   * @return the desired {@code UIPane}, or {@code null} if none was present
   */
  public static UIPane getPane(String name) {
    return panes.get(name);
  }
  
  /**
   * Changes the current {@code UIPane} to the one represented by the given {@code String}
   * 
   * @param name the name of the {@code UIPane} to switch to
   * 
   * @return the now active {@code UIPane}, or {@code null} if none was present
   */
  public static synchronized UIPane setCurrentPane(String name) {
    if (panes.get(name) == null) return null;
    currentPane.clear();
    currentPane = panes.get(name);
    currentPane.reset();
    return currentPane;
  }

  /**
   * Retrieves the currently set time in milliseconds for standard animations to complete
   * 
   * @return the animation time in milliseconds
   */
  public static long getAnimTimeMillis() {
    return animTimeMillis;
  }

  /**
   * Retrieves the currently set time in milliseconds for longer animations to complete
   * 
   * @return the long-animation time in milliseconds
   */
  public static long getAnimTimeMillisLong() {
    return animTimeMillisLong;
  }

  /**
   * Sets the time for standard animations to complete
   * 
   * @param animTimeMillis the time for animations to complete in milliseconds
   */
  public static void setAnimTimeMillis(long animTimeMillis) {
    UIController.animTimeMillis = animTimeMillis;
  }

  /**
   * Sets the time for longer animations to complete
   * 
   * @param animTimeMillisLong the time for long-animations to complete in milliseconds
   */
  public static void setAnimTimeMillisLong(long animTimeMillisLong) {
    UIController.animTimeMillisLong = animTimeMillisLong;
  }
  
  /**
   * Changes the current {@code UIPane}'s state to the given {@code UIState}, assuming the given {@code UIState} is valid 
   * and the current state is not actively transitioning
   * 
   * @param state the {@code UIState} to switch to
   */
  public static synchronized void setState(UIState state) {
    currentPane.setState(state, animTimeMillis);
  }
  
  /**
   * Changes the current {@code UIPane}'s state to the given {@code UIState}, assuming the given {@code UIState} is valid 
   * even when the current state is actively transitioning
   * 
   * @param state the {@code UIState} to switch to
   */
  public static synchronized void forceState(UIState state) {
    currentPane.forceState(state, animTimeMillis);
  }
  
  /**
   * Gets the current {@code UIState} that is currently being displayed through the current {@code UIPane}
   * 
   * @return the {@code UIState} currently active
   */
  public static UIState getState() {
    return currentPane.getState();
  }

  /**
   * Checks to see if the current {@code UIPane} is in the state represented by the given {@code UIState}
   * 
   * @param state the {@code UIState} to look for
   * 
   * @return {@code true} if the current {@code UIPane} is in the desired state
   */
  public static boolean isState(UIState state) {
    return currentPane.getState() == state;
  }
  
  /**
   * Sets the current state of the active {@code UIPane} to its previous one, most commonly changing to its parent {@code UIState}
   */
  public static synchronized void back() {
    currentPane.back();
  }
  
  /**
   * Sets the current state of the active {@code UIPane} to the current state's parent
   */
  public static synchronized void retState() {
    currentPane.retState();
  }
  
  /**
   * Transitions all the {@code UIElement}s within the currently active {@code UIPane} to their inactive state
   */
  public static synchronized void transOut() {
    currentPane.transOut();
  }
  
  /**
   * Transitions all the {@code UIElement}s within the currently active {@code UIPane} to their active state
   */
  public static synchronized void transIn() {
    currentPane.transIn();
  }
  
  /**
   * Checks to see if any {@code UIElement}s within the currently active {@code UIPane} are transitioning
   * 
   * @return {@code true} if there is at least one {@code UIElement} transitioning
   */
  public static boolean isTransitioning() {
    return currentPane.isTransitioning();
  }

  /**
   * Adds a new theme to the user interface. Activates it by default
   * 
   * @param themeName the name of the new theme
   * @param c the theme to add
   */
  public static void addColourTheme(String themeName, UIColours.ColourSet c) {
    UIColours.THEMES.put(themeName, c);
    UIColours.ACTIVE = c;
  }

  /**
   * sets the active theme of this user interface if a valid theme name is given
   * 
   * @param themeName the name of the theme to select
   */
  public static void setActiveColourTheme(String themeName) {
    if (!UIColours.THEMES.containsKey(themeName)) return;
    UIColours.ACTIVE = UIColours.THEMES.get(themeName);
  }
  
  /**
   * Retrieves the top-most {@code UIComponent} within the current {@code UIPane} at the given coordinates - or {@code null} if none were found
   * 
   * @param x the {@code x} coordinate of the cursor
   * @param y the {@code y} coordinate of the cursor
   * 
   * @return the top-most {@code UIComponent} present at the coordinates, or {@code null} if none were found.
   */
  public static UIComponent getComponent(double x, double y) {
    return currentPane.getComponent(x, y);
  }
  
  /**
   * Resets all the {@code UIInteractable}s of the currently active {@code UIPane} to their unpressed state
   */
  public static synchronized void resetClickables() {
    currentPane.resetClickables();
  }
  
  /**
  * Sets the highlighted {@code UIInteractable}
  *
  * @param highlighted The {@code UIInteractable} to set as highlighted
  */
  public static void setHighlightedInteractable(UIInteractable highlighted) {UIController.highlightedInteractable = highlighted;}
  
  /**
  * Gets the highlighted {@code UIInteractable}
  *
  * @return The {@code UIInteractable} currently highlighted
  */
  public static UIInteractable getHighlightedInteractable() {return UIController.highlightedInteractable;}
  
  /**
  * Sets the active {@code UITextfield}
  *
  * @param textfield The {@code UITextfield} to set as active
  */
  public static void setActiveTextfield(UITextfield textfield) {UIController.activeTextfield = textfield;}
  
  /**
  * Gets the active {@code UITextfield}
  *
  * @return The {@code UITextfield} currently active
  */
  public static UITextfield getActiveTextfield() {return UIController.activeTextfield;}

  /**
   * Sets the location of the cursor to the given {@code Vector2I}
   * 
   * @param pos the {@code Vector2I} representing the {@code x} and {@code y} coordinates of the cursor
   */
  public static void cursorMove(Vector2I pos) {
    cursorMove(pos.x, pos.y);
  }
  
  /**
   * Sets the location of the cursor to the given coordinates
   * 
   * @param x the {@code x} coordinate of the cursor
   * @param y the {@code y} coordinate of the cursor
   */
  public static void cursorMove(int x, int y) {
    UIComponent comp = getComponent(x, y);
    setHighlightedInteractable(comp instanceof UIInteractable ? (UIInteractable) comp : null);

    useSlider(x);
  }
  
  /**
   * Presses the highlighted {@code UIInteractable} in, if one is present.
   * 
   * @return true if a highlighted {@code UIInteractible} was pressed
   */
  public static boolean press() {
    if (highlightedInteractable == null) return false;
    if (highlightedInteractable.isLocked()) return true;
    highlightedInteractable.setIn();
    if (highlightedInteractable instanceof UISlider) activeSlider = (UISlider)highlightedInteractable;
    return true;
  }
  
  /**
   * Releases the cursor, selecting whatever {@code UIInteractable} is selected under it.
   */
  public static void release() {
    selectInteractable(highlightedInteractable);
    activeSlider = null;
  }
  
  /**
   * Draws the contents of the current {@code UIPane} to the screen
   * 
   * @param g the {@code Graphics2D} object to draw to
   * @param screenSizeX the width of the screen in pixels
   * @param screenSizeY the height of the screen in pixels
   */
  public static void draw(Graphics2D g, int screenSizeX, int screenSizeY) {
    if (highlightedInteractable != null) highlightedInteractable.highlightForAFrame();
    currentPane.draw(g, screenSizeX, screenSizeY);
  }
  
  /**
   * Draws a bounding box over a region of the screen
   * 
   * @param g the {@code Graphics2D} object to draw to
   * @param a the first set of {@code x} and {@code y} coordinates, representing a corner of the bounding box
   * @param b the second set of {@code x} and {@code y} coordinates, representing the opposite corner of the bounding box
   */
  public static void drawBoundingBox(Graphics2D g, Vector2 a, Vector2 b) {
    double u = a.y;
    double d = b.y;
    if (a.y > b.y) {
      u = b.y;
      d = a.y;
    }
    
    double l = a.x;
    double r = b.x;
    if (a.x > b.x) {
      l = b.x;
      r = a.x;
    }
    
    g.setColor(UIColours.ACTIVE.buttonAccentOut());
    g.draw(new Rectangle2D.Double(l, u, r-l, d-u));
  }
  
  /**
  * Types a key into the active {@code UITextfield}. Assumes active {@code UITextfield} is not null
  *
  * @param e The KeyEvent to type into the active {@code UITextfield}
  */
  public static void typeKey(KeyEvent e) {
    int keyCode = e.getKeyCode();
    if (keyCode == KeyEvent.VK_ENTER) {
      if (highlightedInteractable == null || highlightedInteractable == activeTextfield) {
        activeTextfield.enterAct();
        return;
      }
    }
    if (keyCode >= 32 && keyCode <= 122) {activeTextfield.print(e.getKeyChar()); return;}
    if (keyCode == KeyEvent.VK_BACK_SPACE) {activeTextfield.backspace(); return;}
  }
  
  /**
  * Changes the value held in the active {@code UISlider}.
  *
  * @param x The x coordinate of the cursor
  */
  private static void useSlider(int x) {
    if (activeSlider != null) activeSlider.moveNode(x);
  }
  
  /**
  * Activates a given {@code UIInteractable}
  *
  * @param interact The {@code UIInteractable} to activate
  */
  private static void selectInteractable(UIInteractable interact) {
    if (activeTextfield != null) activeTextfield.clearAct();
    if (interact != null && interact.isIn()) {
      interact.primeAct();
      resetClickables();
      if (interact instanceof UITextfield) interact.setIn();
      return;
    }
    resetClickables();
  }
}
